package com.advance.patterns.structural.proxy.v4.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import javax.sql.DataSource;

/**
 * 案件和分派记录的 JDBC 仓储；两次写入需要处在同一连接事务中。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public final class CaseRepository {
    /** 使用 DataSourceUtils 参与 Spring 绑定连接的模板。 */
    private final JdbcTemplate jdbc;

    /**
     * 保存仓储依赖。
     * @param dataSource 教学数据源
     * @throws IllegalArgumentException 依赖为空
     */
    public CaseRepository(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("仓储依赖不能为空");
        }
        this.jdbc = new JdbcTemplate(dataSource);
    }

    /**
     * 更新虚构案件的承办复核组，不触达客户。
     * @param caseId 案件编号
     * @param collectorId 虚构复核组编号
     * @throws IllegalArgumentException 案件不存在
     * @throws org.springframework.dao.DataAccessException JDBC 操作失败
     */
    public void updateAssignee(String caseId, String collectorId) {
        System.out.println("[proxy/v4] CaseRepository.updateAssignee ---> 更新案件，事务活动="
                + TransactionSynchronizationManager.isActualTransactionActive());
        int changed = jdbc.update("UPDATE collection_case SET assignee=? WHERE case_id=?", collectorId, caseId);
        if (changed != 1) {
            throw new IllegalArgumentException("案件不存在");
        }
    }

    /**
     * 写入同一案件的分派记录；主键约束用于观察真实数据库失败回滚。
     * @param caseId 案件编号
     * @param collectorId 复核组编号
     * @throws org.springframework.dao.DataAccessException JDBC 操作失败
     */
    public void insertRecord(String caseId, String collectorId) {
        System.out.println("[proxy/v4] CaseRepository.insertRecord ---> 插入分派记录");
        jdbc.update("INSERT INTO assignment_record(case_id, collector_id) VALUES (?, ?)", caseId, collectorId);
    }
}
