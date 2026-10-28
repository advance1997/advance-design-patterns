package com.advance.patterns.structural.proxy.v4.tx;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * 仅增加中文路径日志的真实 Spring JDBC 事务管理器，不重写事务决策规则。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public final class TracingTransactionManager extends DataSourceTransactionManager {
    /** 序列化版本。 */
    private static final long serialVersionUID = 1L;

    /**
     * 使用与 JdbcTemplate 相同的数据源。
     * @param dataSource 教学数据源
     */
    public TracingTransactionManager(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 委托 Spring 开启物理事务。
     * @param transaction Spring 事务对象
     * @param definition 事务属性
     */
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        System.out.println("[proxy/v4] TracingTransactionManager.doBegin ---> Spring 开启 JDBC 事务");
        super.doBegin(transaction, definition);
    }

    /**
     * 委托 Spring 提交。
     * @param status 当前状态
     */
    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        System.out.println("[proxy/v4] TracingTransactionManager.doCommit ---> Spring 提交");
        super.doCommit(status);
    }

    /**
     * 委托 Spring 回滚。
     * @param status 当前状态
     */
    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        System.out.println("[proxy/v4] TracingTransactionManager.doRollback ---> Spring 回滚");
        super.doRollback(status);
    }

    /**
     * 内层参与者失败时标记整个事务只可回滚。
     * @param status 当前状态
     */
    @Override
    protected void doSetRollbackOnly(DefaultTransactionStatus status) {
        System.out.println("[proxy/v4] TracingTransactionManager.doSetRollbackOnly ---> 标记 rollback-only");
        super.doSetRollbackOnly(status);
    }

    /**
     * 委托 Spring 解除线程绑定、恢复连接并释放资源。
     * @param transaction Spring 事务对象
     */
    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        System.out.println("[proxy/v4] TracingTransactionManager.doCleanupAfterCompletion ---> Spring 清理连接绑定");
    }
}
