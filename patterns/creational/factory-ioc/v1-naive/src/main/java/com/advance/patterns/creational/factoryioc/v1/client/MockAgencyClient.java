package com.advance.patterns.creational.factoryioc.v1.client;

/**
 * 催收协作机构的模拟复核客户端；仅打印，不联网、不传输真实客户信息。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class MockAgencyClient implements AutoCloseable {
    /** 客户端是否可用，只用于单线程生命周期演示。 */
    private boolean opened;

    /**
     * 模拟建立资源，重复调用时拒绝。
     * @throws IllegalStateException 已打开
     */
    public void open() {
        if (opened) {
            throw new IllegalStateException("客户端已打开");
        }
        opened = true;
        System.out.println("[factory-ioc/v1] MockAgencyClient.open ---> 模拟资源初始化");
    }

    /**
     * 模拟查询人工复核状态。
     * @param caseId 虚构案件编号
     * @return REVIEW_PENDING
     * @throws IllegalStateException 客户端未打开或已关闭
     * @throws IllegalArgumentException 编号为空
     */
    public String review(String caseId) {
        if (!opened) {
            throw new IllegalStateException("客户端未打开或已关闭");
        }
        if (caseId == null || caseId.trim().isEmpty()) {
            throw new IllegalArgumentException("caseId 不能为空");
        }
        System.out.println("[factory-ioc/v1] MockAgencyClient.review ---> 模拟复核，caseId=" + caseId);
        return "REVIEW_PENDING";
    }

    /**
     * 幂等释放模拟资源，不依赖垃圾回收。
     */
    @Override
    public void close() {
        if (opened) {
            opened = false;
            System.out.println("[factory-ioc/v1] MockAgencyClient.close ---> 模拟资源释放");
        }
    }
}
