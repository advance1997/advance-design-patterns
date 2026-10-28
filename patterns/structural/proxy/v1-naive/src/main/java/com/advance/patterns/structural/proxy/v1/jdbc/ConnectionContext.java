package com.advance.patterns.structural.proxy.v1.jdbc;

import java.sql.Connection;

/**
 * 实例级线程绑定连接；同一事务的仓储必须使用同一个上下文，禁止跨线程传播。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public final class ConnectionContext {
    /** 当前线程正在使用的业务事务连接。 */
    private final ThreadLocal<Connection> current = new ThreadLocal<Connection>();

    /**
     * 获取线程绑定连接。
     * @return 无事务时为 null
     */
    public Connection get() {
        return current.get();
    }

    /**
     * 绑定连接，拒绝覆盖尚未结束的事务。
     * @param connection 非空连接
     * @throws IllegalStateException 已有连接或传入为空
     */
    public void bind(Connection connection) {
        if (connection == null || current.get() != null) {
            throw new IllegalStateException("连接为空或当前线程已有事务连接");
        }
        current.set(connection);
    }

    /**
     * 释放线程引用，不由此方法关闭物理连接。
     */
    public void clear() {
        current.remove();
    }
}
