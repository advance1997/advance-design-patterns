package com.advance.patterns.structural.proxy.v5.repository;

import com.advance.patterns.structural.proxy.v5.jdbc.ConnectionContext;
import com.advance.patterns.structural.proxy.v5.jdbc.ConnectionLease;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * 案件和分派记录的 JDBC 仓储；两次写入需要处在同一连接事务中。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public final class CaseRepository {
    /** 与事务管理器相同的数据源。 */
    private final DataSource dataSource;
    /** 与事务管理器相同的线程连接上下文。 */
    private final ConnectionContext context;

    /**
     * 保存仓储依赖。
     * @param dataSource 教学数据源
     * @param context 事务连接上下文
     * @throws IllegalArgumentException 依赖为空
     */
    public CaseRepository(DataSource dataSource, ConnectionContext context) {
        if (dataSource == null || context == null) {
            throw new IllegalArgumentException("仓储依赖不能为空");
        }
        this.dataSource = dataSource;
        this.context = context;
    }

    /**
     * 更新虚构案件的承办复核组，不触达客户。
     * @param caseId 案件编号
     * @param collectorId 虚构复核组编号
     * @throws IllegalArgumentException 案件不存在
     * @throws IllegalStateException JDBC 操作失败（普通 JDBC 版本）
     */
    public void updateAssignee(String caseId, String collectorId) {
        System.out.println("[proxy/v5] CaseRepository.updateAssignee ---> 更新案件，事务活动="
                + (context.get() != null));
        try (ConnectionLease lease = new ConnectionLease(dataSource, context);
                PreparedStatement statement = lease.getConnection().prepareStatement(
                        "UPDATE collection_case SET assignee=? WHERE case_id=?")) {
            statement.setString(1, collectorId);
            statement.setString(2, caseId);
            if (statement.executeUpdate() != 1) {
                throw new IllegalArgumentException("案件不存在");
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("更新案件失败", exception);
        }
    }

    /**
     * 写入同一案件的分派记录；主键约束用于观察真实数据库失败回滚。
     * @param caseId 案件编号
     * @param collectorId 复核组编号
     * @throws IllegalStateException JDBC 操作失败（普通 JDBC 版本）
     */
    public void insertRecord(String caseId, String collectorId) {
        System.out.println("[proxy/v5] CaseRepository.insertRecord ---> 插入分派记录");
        try (ConnectionLease lease = new ConnectionLease(dataSource, context);
                PreparedStatement statement = lease.getConnection().prepareStatement(
                        "INSERT INTO assignment_record(case_id, collector_id) VALUES (?, ?)")) {
            statement.setString(1, caseId);
            statement.setString(2, collectorId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("写入分派记录失败", exception);
        }
    }
}
