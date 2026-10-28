package com.advance.patterns.structural.proxy.v2.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * 仓储连接租约：事务内借用连接不关闭，事务外独立连接在语句结束后关闭。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public final class ConnectionLease implements AutoCloseable {
    /** 当前语句使用的连接。 */
    private final Connection connection;
    /** 是否由本租约创建，决定关闭责任。 */
    private final boolean owned;

    /**
     * 取得事务连接或临时连接。
     * @param dataSource 数据源
     * @param context 同线程连接上下文
     * @throws SQLException 获取连接失败
     */
    public ConnectionLease(DataSource dataSource, ConnectionContext context) throws SQLException {
        Connection bound = context.get();
        owned = bound == null;
        connection = owned ? dataSource.getConnection() : bound;
    }

    /**
     * 获取实际连接。
     * @return 本次租约连接
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * 仅关闭本租约拥有的连接，保留事务内共享连接。
     * @throws SQLException 关闭失败
     */
    @Override
    public void close() throws SQLException {
        if (owned) {
            connection.close();
        }
    }
}
