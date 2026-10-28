package com.advance.patterns.structural.proxy.v5.service;

import com.advance.patterns.structural.proxy.v5.repository.CaseRepository;
import com.advance.patterns.structural.proxy.v5.domain.AssignmentCheckedException;
import com.advance.patterns.structural.proxy.v5.tx.MiniTransactional;

/**
 * 业务目标对象：只编写分派步骤，事务边界由外部代理增强。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public class CaseAssignmentService implements AssignmentService {
    /** 案件仓储。 */
    private final CaseRepository repository;
    /** 独立 Bean，用于演示经过另一个代理的 REQUIRED 调用。 */
    private final AuditService auditService;

    /**
     * 注入业务依赖。
     * @param repository 案件仓储
     * @param auditService 记录服务（容器注入代理）
     * @throws IllegalArgumentException 依赖为空
     */
    public CaseAssignmentService(CaseRepository repository, AuditService auditService) {
        if (repository == null || auditService == null) {
            throw new IllegalArgumentException("分派服务依赖不能为空");
        }
        this.repository = repository;
        this.auditService = auditService;
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
    @MiniTransactional
    public void assign(String caseId, String collectorId, boolean failAfterUpdate) {
        validate(caseId, collectorId);
        System.out.println("[proxy/v5] CaseAssignmentService.assign ---> 更新案件并写入分派记录");
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

    /**
     * 演示受检异常的默认提交规则，真实业务是否回滚应显式决定。
     * @param caseId 案件编号
     * @param collectorId 复核组
     * @throws AssignmentCheckedException 固定模拟受检异常
     */
    @Override
    @MiniTransactional
    public void assignChecked(String caseId, String collectorId) throws AssignmentCheckedException {
        validate(caseId, collectorId);
        repository.updateAssignee(caseId, collectorId);
        throw new AssignmentCheckedException("模拟人工核查未完成");
    }

    /**
     * 将受检异常纳入回滚范围。
     * @param caseId 案件编号
     * @param collectorId 复核组
     * @throws AssignmentCheckedException 固定模拟受检异常
     */
    @Override
    @MiniTransactional(rollbackFor = Exception.class)
    public void assignCheckedRollback(String caseId, String collectorId) throws AssignmentCheckedException {
        validate(caseId, collectorId);
        repository.updateAssignee(caseId, collectorId);
        throw new AssignmentCheckedException("模拟人工核查未完成");
    }

    /**
     * this.assign 不经过外部代理，不能触发新的事务拦截。
     * @param caseId 案件编号
     * @param collectorId 复核组
     * @throws IllegalStateException 内部模拟失败
     */
    @Override
    public void assignViaSelf(String caseId, String collectorId) {
        System.out.println("[proxy/v5] CaseAssignmentService.assignViaSelf ---> this 调用，绕过代理");
        this.assign(caseId, collectorId, true);
    }

    /**
     * 跨 Bean 正常调用记录服务，两次写入共享一笔物理事务。
     * @param caseId 案件编号
     * @param collectorId 复核组
     */
    @Override
    @MiniTransactional
    public void assignWithAudit(String caseId, String collectorId) {
        validate(caseId, collectorId);
        repository.updateAssignee(caseId, collectorId);
        auditService.record(caseId, collectorId, false);
    }

    /**
     * 外层捕获内层失败，但不能清除已标记的 rollback-only。
     * @param caseId 案件编号
     * @param collectorId 复核组
     */
    @Override
    @MiniTransactional
    public void assignCatchingInnerFailure(String caseId, String collectorId) {
        validate(caseId, collectorId);
        repository.updateAssignee(caseId, collectorId);
        try {
            auditService.record(caseId, collectorId, true);
        } catch (IllegalStateException exception) {
            System.out.println("[proxy/v5] CaseAssignmentService.assignCatchingInnerFailure ---> 捕获内层失败");
        }
    }

}
