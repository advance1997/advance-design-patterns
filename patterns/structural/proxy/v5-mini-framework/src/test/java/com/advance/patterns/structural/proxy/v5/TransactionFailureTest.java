package com.advance.patterns.structural.proxy.v5;

import com.advance.patterns.structural.proxy.v5.database.DemoDatabase;
import com.advance.patterns.structural.proxy.v5.jdbc.ConnectionContext;
import com.advance.patterns.structural.proxy.v5.repository.CaseRepository;
import com.advance.patterns.structural.proxy.v5.service.AssignmentService;
import com.advance.patterns.structural.proxy.v5.service.CaseAssignmentService;
import com.advance.patterns.structural.proxy.v5.service.AssignmentAuditService;
import com.advance.patterns.structural.proxy.v5.tx.LocalTransactionManager;
import com.advance.patterns.structural.proxy.v5.tx.MiniTransactionInterceptor;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;

/**
 * 以具名 JDBC 代理注入事务完成阶段故障，观察原始异常、资源释放与提交结果边界。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public class TransactionFailureTest {
    /**
     * testCommitFailure：对照调用路径与数据库实际状态。
     * <p>期望结果：模拟 commit 失败后尝试 rollback；数据库 UNASSIGNED / 0，线程清理完成，连接关闭一次。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testCommitFailure() throws Exception {
        System.out.println("\ntestCommitFailure ---> 期望：模拟 commit 失败后尝试 rollback；数据库 UNASSIGNED / 0，线程清理完成，连接关闭一次");
        try (DemoDatabase database = new DemoDatabase()) {
            FaultDataSource dataSource = new FaultDataSource(database.getDataSource(),
                    true, false, false);
            ConnectionContext connections = new ConnectionContext();
            LocalTransactionManager manager = new LocalTransactionManager(dataSource, connections);
            CaseRepository repository = new CaseRepository(dataSource, connections);
            AssignmentService target = new CaseAssignmentService(repository, new AssignmentAuditService(repository));
            AssignmentService proxy = (AssignmentService) Proxy.newProxyInstance(
                    AssignmentService.class.getClassLoader(), new Class<?>[] {AssignmentService.class},
                    new MiniTransactionInterceptor(target, manager));
            try {
                proxy.assign("DEMO-001", "REVIEW-A", false);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
                for (Throwable suppressed : exception.getSuppressed()) {
                    System.out.println("附加异常：" + suppressed);
                    for (Throwable nested : suppressed.getSuppressed()) {
                        System.out.println("附加清理异常：" + nested);
                    }
                }
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("连接调用次数：" + dataSource.describe());
            System.out.println("事务活动：" + manager.isActive());
            System.out.println("线程连接清理：" + (connections.get() == null));
        }
    }

    /**
     * testRollbackFailurePreservesBusinessException：对照调用路径与数据库实际状态。
     * <p>期望结果：业务 IllegalStateException 保留；回滚异常附加为 suppressed，连接关闭一次；本 H2 测试关闭连接后撤销未提交更新。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testRollbackFailurePreservesBusinessException() throws Exception {
        System.out.println("\ntestRollbackFailurePreservesBusinessException ---> 期望：业务 IllegalStateException 保留；"
                + "回滚异常附加为 suppressed，连接关闭一次；本 H2 测试关闭连接后撤销未提交更新");
        try (DemoDatabase database = new DemoDatabase()) {
            FaultDataSource dataSource = new FaultDataSource(database.getDataSource(),
                    false, true, false);
            ConnectionContext connections = new ConnectionContext();
            LocalTransactionManager manager = new LocalTransactionManager(dataSource, connections);
            CaseRepository repository = new CaseRepository(dataSource, connections);
            AssignmentService target = new CaseAssignmentService(repository, new AssignmentAuditService(repository));
            AssignmentService proxy = (AssignmentService) Proxy.newProxyInstance(
                    AssignmentService.class.getClassLoader(), new Class<?>[] {AssignmentService.class},
                    new MiniTransactionInterceptor(target, manager));
            try {
                proxy.assign("DEMO-001", "REVIEW-A", true);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
                for (Throwable suppressed : exception.getSuppressed()) {
                    System.out.println("附加异常：" + suppressed);
                    for (Throwable nested : suppressed.getSuppressed()) {
                        System.out.println("附加清理异常：" + nested);
                    }
                }
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("连接调用次数：" + dataSource.describe());
            System.out.println("事务活动：" + manager.isActive());
            System.out.println("线程连接清理：" + (connections.get() == null));
        }
    }

    /**
     * testCloseFailureAfterCommit：对照调用路径与数据库实际状态。
     * <p>期望结果：事务已经提交，但关闭报告失败；数据库 REVIEW-A / 1，不应盲目重试。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testCloseFailureAfterCommit() throws Exception {
        System.out.println("\ntestCloseFailureAfterCommit ---> 期望：事务已经提交，但关闭报告失败；数据库 REVIEW-A / 1，不应盲目重试");
        try (DemoDatabase database = new DemoDatabase()) {
            FaultDataSource dataSource = new FaultDataSource(database.getDataSource(),
                    false, false, true);
            ConnectionContext connections = new ConnectionContext();
            LocalTransactionManager manager = new LocalTransactionManager(dataSource, connections);
            CaseRepository repository = new CaseRepository(dataSource, connections);
            AssignmentService target = new CaseAssignmentService(repository, new AssignmentAuditService(repository));
            AssignmentService proxy = (AssignmentService) Proxy.newProxyInstance(
                    AssignmentService.class.getClassLoader(), new Class<?>[] {AssignmentService.class},
                    new MiniTransactionInterceptor(target, manager));
            try {
                proxy.assign("DEMO-001", "REVIEW-A", false);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
                for (Throwable suppressed : exception.getSuppressed()) {
                    System.out.println("附加异常：" + suppressed);
                    for (Throwable nested : suppressed.getSuppressed()) {
                        System.out.println("附加清理异常：" + nested);
                    }
                }
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("连接调用次数：" + dataSource.describe());
            System.out.println("事务活动：" + manager.isActive());
            System.out.println("线程连接清理：" + (connections.get() == null));
        }
    }

    /**
     * testRollbackAndCloseFailures：对照调用路径与数据库实际状态。
     * <p>期望结果：原始业务异常保留，事务完成及关闭失败附加诊断信息；线程清理完成。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testRollbackAndCloseFailures() throws Exception {
        System.out.println("\ntestRollbackAndCloseFailures ---> 期望：原始业务异常保留，事务完成及关闭失败附加诊断信息；线程清理完成");
        try (DemoDatabase database = new DemoDatabase()) {
            FaultDataSource dataSource = new FaultDataSource(database.getDataSource(),
                    false, true, true);
            ConnectionContext connections = new ConnectionContext();
            LocalTransactionManager manager = new LocalTransactionManager(dataSource, connections);
            CaseRepository repository = new CaseRepository(dataSource, connections);
            AssignmentService target = new CaseAssignmentService(repository, new AssignmentAuditService(repository));
            AssignmentService proxy = (AssignmentService) Proxy.newProxyInstance(
                    AssignmentService.class.getClassLoader(), new Class<?>[] {AssignmentService.class},
                    new MiniTransactionInterceptor(target, manager));
            try {
                proxy.assign("DEMO-001", "REVIEW-A", true);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
                for (Throwable suppressed : exception.getSuppressed()) {
                    System.out.println("附加异常：" + suppressed);
                    for (Throwable nested : suppressed.getSuppressed()) {
                        System.out.println("附加清理异常：" + nested);
                    }
                }
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("连接调用次数：" + dataSource.describe());
            System.out.println("事务活动：" + manager.isActive());
            System.out.println("线程连接清理：" + (connections.get() == null));
        }
    }

    /**
     * 只包装连接操作的故障数据源，不使用断言或外部 Mock 框架。
     *
     * @author advance
     * @since 2026-10-28 10:15:29
     */
    private static final class FaultDataSource implements DataSource {
        /** 真正连接 H2 的数据源。 */
        private final DataSource delegate;
        /** 是否在真正 commit 前抛出异常。 */
        private final boolean failCommit;
        /** 是否在真正 rollback 前抛出异常。 */
        private final boolean failRollback;
        /** 是否在真正关闭连接后报告异常。 */
        private final boolean failClose;
        /** commit 调用次数。 */
        private int commits;
        /** rollback 调用次数。 */
        private int rollbacks;
        /** close 调用次数。 */
        private int closes;

        /**
         * 配置确定性的单线程故障点。
         * @param delegate 真实数据源
         * @param failCommit 提交前失败
         * @param failRollback 回滚前失败
         * @param failClose 关闭后报告失败
         */
        private FaultDataSource(DataSource delegate, boolean failCommit, boolean failRollback, boolean failClose) {
            this.delegate = delegate;
            this.failCommit = failCommit;
            this.failRollback = failRollback;
            this.failClose = failClose;
        }

        /**
         * 返回 JDBC 接口代理，SQL 本身仍由 H2 执行。
         * @return 包装连接
         * @throws SQLException 获取失败
         */
        @Override
        public Connection getConnection() throws SQLException {
            return wrap(delegate.getConnection());
        }

        /**
         * 保持 DataSource 协议，不记录凭据。
         * @param username 用户名
         * @param password 密码
         * @return 包装连接
         * @throws SQLException 获取失败
         */
        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return wrap(delegate.getConnection(username, password));
        }

        /**
         * 使用具名 InvocationHandler 创建故障连接。
         * @param connection 实际连接
         * @return 接口代理
         */
        private Connection wrap(Connection connection) {
            return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                    new Class<?>[] {Connection.class}, new FaultConnection(connection, this));
        }

        /**
         * 输出本测试实际调用次数。
         * @return 提交、回滚、关闭次数
         */
        private String describe() {
            return "commit=" + commits + ", rollback=" + rollbacks + ", close=" + closes;
        }

        /**
         * 委托日志写入器读取。
         * @return 原写入器
         * @throws SQLException 数据源操作失败
         */
        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return delegate.getLogWriter();
        }

        /**
         * 委托日志写入器设置。
         * @param writer 写入器
         * @throws SQLException 数据源操作失败
         */
        @Override
        public void setLogWriter(PrintWriter writer) throws SQLException {
            delegate.setLogWriter(writer);
        }

        /**
         * 委托登录超时设置。
         * @param seconds 超时秒数
         * @throws SQLException 数据源操作失败
         */
        @Override
        public void setLoginTimeout(int seconds) throws SQLException {
            delegate.setLoginTimeout(seconds);
        }

        /**
         * 委托登录超时读取。
         * @return 超时秒数
         * @throws SQLException 数据源操作失败
         */
        @Override
        public int getLoginTimeout() throws SQLException {
            return delegate.getLoginTimeout();
        }

        /**
         * 委托父日志读取。
         * @return 父日志
         * @throws SQLFeatureNotSupportedException 数据源不支持
         */
        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return delegate.getParentLogger();
        }

        /**
         * 解包真实数据源。
         * @param type 请求类型
         * @param <T> 请求接口类型
         * @return 原数据源提供的实例
         * @throws SQLException 不支持解包
         */
        @Override
        public <T> T unwrap(Class<T> type) throws SQLException {
            return delegate.unwrap(type);
        }

        /**
         * 查询真实数据源解包能力。
         * @param type 请求类型
         * @return 是否支持
         * @throws SQLException 查询失败
         */
        @Override
        public boolean isWrapperFor(Class<?> type) throws SQLException {
            return delegate.isWrapperFor(type);
        }
    }

    /**
     * 在完成事务前后制造可控异常，其他 JDBC 调用直接委托真实连接。
     *
     * @author advance
     * @since 2026-10-28 10:15:29
     */
    private static final class FaultConnection implements InvocationHandler {
        /** 真实连接。 */
        private final Connection target;
        /** 共享故障配置与计数。 */
        private final FaultDataSource owner;

        /**
         * 保存真实连接及配置。
         * @param target H2 连接
         * @param owner 故障数据源
         */
        private FaultConnection(Connection target, FaultDataSource owner) {
            this.target = target;
            this.owner = owner;
        }

        /**
         * 只拦截无参数 commit、rollback、close，本示例不用保存点。
         * @param proxy 连接代理
         * @param method JDBC 方法
         * @param args 参数
         * @return JDBC 返回值
         * @throws Throwable 模拟或真实 JDBC 异常
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("commit".equals(method.getName())) {
                owner.commits++;
                if (owner.failCommit) {
                    throw new SQLException("模拟 commit 前失败");
                }
            }
            if ("rollback".equals(method.getName())) {
                owner.rollbacks++;
                if (owner.failRollback) {
                    throw new SQLException("模拟 rollback 前失败");
                }
            }
            if ("close".equals(method.getName())) {
                owner.closes++;
                target.close();
                if (owner.failClose) {
                    throw new SQLException("模拟 close 后报告失败");
                }
                return null;
            }
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException exception) {
                throw exception.getCause();
            }
        }
    }
}
