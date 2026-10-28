package com.advance.patterns.structural.proxy.v3.tx;

import com.advance.patterns.structural.proxy.v3.jdbc.ConnectionContext;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * 单数据源本地事务教学实现；只支持同线程 REQUIRED 参与，不支持保存点或新事务挂起。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public final class LocalTransactionManager {
    /** 业务仓储使用的同一数据源。 */
    private final DataSource dataSource;
    /** 给仓储暴露的线程连接。 */
    private final ConnectionContext context;
    /** 管理本线程的事务状态及 rollback-only 标记。 */
    private final ThreadLocal<State> current = new ThreadLocal<State>();

    /**
     * 注入数据源与连接上下文。
     * @param dataSource 数据源
     * @param context 仓储共用的连接上下文
     * @throws IllegalArgumentException 任一依赖为空
     */
    public LocalTransactionManager(DataSource dataSource, ConnectionContext context) {
        if (dataSource == null || context == null) {
            throw new IllegalArgumentException("事务管理器依赖不能为空");
        }
        this.dataSource = dataSource;
        this.context = context;
    }

    /**
     * 加入现有事务，或建立连接并关闭自动提交。
     * @return 标记当前调用是否拥有物理事务的作用域
     * @throws IllegalStateException 获取连接或开启事务失败
     */
    public Scope begin() {
        State existing = current.get();
        if (existing != null) {
            System.out.println("[proxy/v3] LocalTransactionManager.begin ---> REQUIRED 加入已有事务");
            return new Scope(existing, false);
        }
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            boolean oldAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            State state = new State(connection, oldAutoCommit);
            context.bind(connection);
            current.set(state);
            System.out.println("[proxy/v3] LocalTransactionManager.begin ---> 开启事务并绑定 JDBC 连接");
            return new Scope(state, true);
        } catch (SQLException | RuntimeException exception) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeFailure) {
                    exception.addSuppressed(closeFailure);
                }
            }
            throw new IllegalStateException("开启本地事务失败", exception);
        }
    }

    /**
     * 完成当前调用；参与者只能标记 rollback-only，拥有者才提交或回滚物理连接。
     * @param scope begin 返回的作用域
     * @param rollback 当前异常是否要求回滚
     * @throws IllegalStateException 作用域无效、完成事务或释放资源失败
     * @throws UnexpectedLocalRollbackException 外层尝试提交但内部已标记仅回滚
     */
    public void complete(Scope scope, boolean rollback) {
        if (scope == null || scope.completed || current.get() != scope.state) {
            throw new IllegalStateException("事务作用域为空、已结束或不属于当前线程");
        }
        scope.completed = true;
        State state = scope.state;
        if (rollback) {
            state.rollbackOnly = true;
        }
        if (!scope.owner) {
            System.out.println("[proxy/v3] LocalTransactionManager.complete ---> 参与者结束，rollbackOnly="
                    + state.rollbackOnly);
            return;
        }
        RuntimeException failure = null;
        boolean databaseCompleted = false;
        try {
            if (state.rollbackOnly) {
                System.out.println("[proxy/v3] LocalTransactionManager.complete ---> 执行 JDBC rollback");
                state.connection.rollback();
                databaseCompleted = true;
                if (!rollback) {
                    failure = new UnexpectedLocalRollbackException("内部事务已标记 rollback-only，外层不能提交");
                }
            } else {
                System.out.println("[proxy/v3] LocalTransactionManager.complete ---> 执行 JDBC commit");
                state.connection.commit();
                databaseCompleted = true;
            }
        } catch (SQLException exception) {
            failure = new IllegalStateException("事务完成失败，数据库结果可能未知", exception);
            try {
                state.connection.rollback();
                databaseCompleted = true;
            } catch (SQLException rollbackFailure) {
                failure.addSuppressed(rollbackFailure);
            }
        } finally {
            current.remove();
            context.clear();
            if (databaseCompleted) {
                try {
                    state.connection.setAutoCommit(state.oldAutoCommit);
                } catch (SQLException restoreFailure) {
                    failure = append(failure, "恢复 autoCommit 失败", restoreFailure);
                }
            }
            try {
                state.connection.close();
            } catch (SQLException closeFailure) {
                failure = append(failure, "关闭事务连接失败", closeFailure);
            }
            System.out.println("[proxy/v3] LocalTransactionManager.complete ---> 清理线程引用并尝试关闭连接");
        }
        if (failure != null) {
            throw failure;
        }
    }

    /**
     * 查询当前线程是否有本管理器的事务。
     * @return 有事务时为 true
     */
    public boolean isActive() {
        return current.get() != null;
    }

    /**
     * 保留先发生的异常，后续清理异常附加为 suppressed，不覆盖原始诊断信息。
     * @param first 已有异常
     * @param message 首个清理错误的中文说明
     * @param next 后续 SQL 异常
     * @return 可向上抛出的聚合异常
     */
    private RuntimeException append(RuntimeException first, String message, SQLException next) {
        if (first == null) {
            return new IllegalStateException(message, next);
        }
        first.addSuppressed(next);
        return first;
    }

    /**
     * 一次代理调用的事务参与令牌，不允许由业务代码构造。
     *
     * @author advance
     * @since 2026-10-28 10:15:29
     */
    public static final class Scope {
        /** 所参与的物理事务。 */
        private final State state;
        /** 是否为创建物理事务的外层调用。 */
        private final boolean owner;
        /** 防止同一作用域重复结束。 */
        private boolean completed;

        /**
         * 保存作用域信息。
         * @param state 事务状态
         * @param owner 是否拥有物理事务
         */
        private Scope(State state, boolean owner) {
            this.state = state;
            this.owner = owner;
        }
    }

    /**
     * 仅在本线程使用的连接及事务状态。
     *
     * @author advance
     * @since 2026-10-28 10:15:29
     */
    private static final class State {
        /** 物理连接。 */
        private final Connection connection;
        /** 开启事务前的自动提交标志。 */
        private final boolean oldAutoCommit;
        /** 任一参与者要求回滚后不能恢复为可提交。 */
        private boolean rollbackOnly;

        /**
         * 初始化物理事务状态。
         * @param connection 物理连接
         * @param oldAutoCommit 原自动提交状态
         */
        private State(Connection connection, boolean oldAutoCommit) {
            this.connection = connection;
            this.oldAutoCommit = oldAutoCommit;
        }
    }
}
