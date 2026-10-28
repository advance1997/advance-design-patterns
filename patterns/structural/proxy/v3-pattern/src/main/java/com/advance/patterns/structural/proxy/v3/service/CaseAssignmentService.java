package com.advance.patterns.structural.proxy.v3.service;

import com.advance.patterns.structural.proxy.v3.repository.CaseRepository;

/**
 * 业务目标对象：只编写分派步骤，事务边界由外部代理增强。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public class CaseAssignmentService implements AssignmentService {
    /** 案件仓储。 */
    private final CaseRepository repository;

    /**
     * 注入业务依赖。
     * @param repository 案件仓储
     * @throws IllegalArgumentException 依赖为空
     */
    public CaseAssignmentService(CaseRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("分派服务依赖不能为空");
        }
        this.repository = repository;
    }

    /**
     * 校验后更新案件，再插入记录；失败开关仅用于教学。
     * @param caseId 案件编号
     * @param collectorId 复核组编号
     * @param failAfterUpdate 是否在更新后模拟失败
     * @throws IllegalArgumentException 参数非法
     * @throws IllegalStateException 模拟写记录前失败
     */
    @Override
    public void assign(String caseId, String collectorId, boolean failAfterUpdate) {
        validate(caseId, collectorId);
        System.out.println("[proxy/v3] CaseAssignmentService.assign ---> 更新案件并写入分派记录");
        repository.updateAssignee(caseId, collectorId);
        if (failAfterUpdate) {
            throw new IllegalStateException("模拟分派记录写入前失败");
        }
        repository.insertRecord(caseId, collectorId);
    }

    /**
     * 检查虚构案件编号和复核组，不进行真实客户查询。
     * @param caseId 案件编号
     * @param collectorId 复核组编号
     * @throws IllegalArgumentException 任一编号为空
     */
    private void validate(String caseId, String collectorId) {
        if (caseId == null || caseId.trim().isEmpty() || collectorId == null || collectorId.trim().isEmpty()) {
            throw new IllegalArgumentException("caseId 和 collectorId 不能为空");
        }
    }

}
