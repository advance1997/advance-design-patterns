package com.advance.patterns.creational.factoryioc.v2.service;

import com.advance.patterns.creational.factoryioc.v2.client.MockAgencyClient;

/**
 * 构造器注入版本：只依赖客户端，不依赖 Spring 容器查找 API。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class ReviewService {
    /** 模拟协作客户端。 */
    private final MockAgencyClient client;

    /**
     * 保存本版本所需依赖。
     * @param client 模拟客户端
     * @throws IllegalArgumentException 依赖为空
     */
    public ReviewService(MockAgencyClient client) {
        if (client == null) {
            throw new IllegalArgumentException("服务依赖不能为空");
        }
        this.client = client;
    }

    /**
     * 查询虚构案件的人工复核状态，不触达客户。
     * @param caseId 非空教学编号
     * @return REVIEW_PENDING
     * @throws IllegalArgumentException 编号为空
     * @throws IllegalStateException 客户端不可用
     */
    public String review(String caseId) {
        if (caseId == null || caseId.trim().isEmpty()) {
            throw new IllegalArgumentException("caseId 不能为空");
        }
        System.out.println("[factory-ioc/v2] ReviewService.review ---> 查询模拟人工复核状态");
        return client.review(caseId);
    }
}
