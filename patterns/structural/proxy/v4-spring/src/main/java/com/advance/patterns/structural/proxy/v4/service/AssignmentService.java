package com.advance.patterns.structural.proxy.v4.service;

import com.advance.patterns.structural.proxy.v4.domain.AssignmentCheckedException;

/**
 * 催收案件内部复核分派入口；只操作虚构案件，不触达客户。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public interface AssignmentService {
    /**
     * 更新承办组并写分派记录，两次写入应原子完成。
     * @param caseId 虚构案件编号
     * @param collectorId 虚构复核组
     * @param failAfterUpdate 是否模拟更新后失败
     * @throws IllegalArgumentException 参数为空或案件不存在
     * @throws IllegalStateException 模拟业务失败
     */
    void assign(String caseId, String collectorId, boolean failAfterUpdate);

    /**
     * 更新后抛出受检异常；默认规则不因这种异常回滚。
     * @param caseId 案件编号
     * @param collectorId 复核组
     * @throws AssignmentCheckedException 模拟受检异常
     */
    void assignChecked(String caseId, String collectorId) throws AssignmentCheckedException;

    /**
     * 显式要求 Exception 回滚，更新后抛出同一种受检异常。
     * @param caseId 案件编号
     * @param collectorId 复核组
     * @throws AssignmentCheckedException 模拟受检异常
     */
    void assignCheckedRollback(String caseId, String collectorId) throws AssignmentCheckedException;

    /**
     * 不带事务的入口在对象内部调用带注解方法，故意展示自调用绕过代理。
     * @param caseId 案件编号
     * @param collectorId 复核组
     * @throws IllegalStateException 内部方法模拟失败
     */
    void assignViaSelf(String caseId, String collectorId);

    /**
     * 外层事务捕获内层 REQUIRED 失败，提交仍应因 rollback-only 被拒绝。
     * @param caseId 案件编号
     * @param collectorId 复核组
     */
    void assignCatchingInnerFailure(String caseId, String collectorId);

    /**
     * 外层与独立记录服务正常加入同一 REQUIRED 事务。
     * @param caseId 案件编号
     * @param collectorId 复核组
     */
    void assignWithAudit(String caseId, String collectorId);

    /**
     * final 方法用于观察 JDK 接口代理与 CGLIB 覆盖限制的差异。
     * @return 当前线程是否有实际 Spring 事务
     */
    boolean finalTransactionActive();

}
