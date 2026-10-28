package com.advance.patterns.structural.proxy.v5.service;

import com.advance.patterns.structural.proxy.v5.repository.CaseRepository;
import com.advance.patterns.structural.proxy.v5.tx.MiniTransactional;

/**
 * 独立事务参与者：默认 REQUIRED 加入外层分派事务。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public class AssignmentAuditService implements AuditService {
    /** 事务内共享连接的仓储。 */
    private final CaseRepository repository;

    /**
     * 注入仓储。
     * @param repository 分派记录仓储
     * @throws IllegalArgumentException 仓储为空
     */
    public AssignmentAuditService(CaseRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("repository 不能为空");
        }
        this.repository = repository;
    }

    /**
     * 在当前事务中写入记录，或模拟运行时异常。
     * @param caseId 案件编号
     * @param collectorId 复核组
     * @param fail 是否模拟失败
     * @throws IllegalStateException 模拟内层失败
     */
    @Override
    @MiniTransactional
    public void record(String caseId, String collectorId, boolean fail) {
        System.out.println("[proxy/v5] AssignmentAuditService.record ---> 进入内层 REQUIRED 记录服务");
        if (fail) {
            throw new IllegalStateException("模拟内层记录服务失败");
        }
        repository.insertRecord(caseId, collectorId);
    }
}
