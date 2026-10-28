package com.advance.patterns.structural.proxy.v3.service;

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

}
