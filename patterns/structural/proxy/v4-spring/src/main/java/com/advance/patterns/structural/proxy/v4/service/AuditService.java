package com.advance.patterns.structural.proxy.v4.service;

/**
 * 独立的分派记录服务契约，让内部调用真正经过另一个代理。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public interface AuditService {
    /**
     * 插入分派记录或模拟失败。
     * @param caseId 案件编号
     * @param collectorId 复核组
     * @param fail 是否在插入前失败
     * @throws IllegalStateException 模拟失败
     */
    void record(String caseId, String collectorId, boolean fail);
}
