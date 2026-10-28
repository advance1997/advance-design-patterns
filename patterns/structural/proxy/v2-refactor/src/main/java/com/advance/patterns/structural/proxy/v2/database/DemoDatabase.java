package com.advance.patterns.structural.proxy.v2.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

/**
 * 隔离的 H2 内存教学库；只保存虚构案件和复核组，保留连接关闭后数据库自动释放。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public final class DemoDatabase implements AutoCloseable {
    /** 每个实例独有的数据源，不启动数据库服务器。 */
    private final JdbcDataSource dataSource;
    /** 保持内存库存活的连接，不参与业务事务。 */
    private final Connection anchor;

    /**
     * 创建教学表和两条未分派案件；初始化失败时关闭连接。
     * @throws SQLException 建库失败
     */
    public DemoDatabase() throws SQLException {
        dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:proxy_v2_" + UUID.randomUUID().toString());
        anchor = dataSource.getConnection();
        try (Statement statement = anchor.createStatement()) {
            statement.execute("CREATE TABLE collection_case "
                    + "(case_id VARCHAR(40) PRIMARY KEY, assignee VARCHAR(40) NOT NULL)");
            statement.execute("CREATE TABLE assignment_record "
                    + "(case_id VARCHAR(40) PRIMARY KEY, collector_id VARCHAR(40) NOT NULL)");
            statement.execute("INSERT INTO collection_case VALUES ('DEMO-001', 'UNASSIGNED')");
            statement.execute("INSERT INTO collection_case VALUES ('DEMO-002', 'UNASSIGNED')");
        } catch (SQLException exception) {
            try {
                anchor.close();
            } catch (SQLException closeFailure) {
                exception.addSuppressed(closeFailure);
            }
            throw exception;
        }
    }

    /**
     * 提供本库数据源供仓储与事务管理器共用。
     * @return 隔离的数据源
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 用独立连接读取已提交数据，而不是复用事务里的未提交视图。
     * @param caseId 虚构案件编号
     * @return assignee 与记录数量组成的状态摘要
     * @throws SQLException 查询失败
     */
    public String snapshot(String caseId) throws SQLException {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement query = connection.prepareStatement(
                        "SELECT assignee, (SELECT COUNT(*) FROM assignment_record r WHERE r.case_id=c.case_id) "
                                + "FROM collection_case c WHERE case_id=?")) {
            query.setString(1, caseId);
            try (ResultSet rows = query.executeQuery()) {
                if (!rows.next()) {
                    return "案件不存在";
                }
                return "assignee=" + rows.getString(1) + ", records=" + rows.getInt(2);
            }
        }
    }

    /**
     * 释放保持连接；业务连接均关闭时内存库随之消失。
     * @throws SQLException 关闭连接失败
     */
    @Override
    public void close() throws SQLException {
        anchor.close();
    }
}
