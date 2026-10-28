package com.advance.patterns.creational.factoryioc.v1.service;

import com.advance.patterns.creational.factoryioc.v1.client.MockAgencyClient;
import org.springframework.context.ApplicationContext;

/**
 * 反例：业务服务通过 ApplicationContext 主动查找依赖，隐藏了真实依赖。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class ReviewService {
    /** 服务定位器式容器依赖，后续版本将移除。 */
    private final ApplicationContext context;

    /**
     * 保存本版本所需依赖。
     * @param context Spring 容器
     * @throws IllegalArgumentException 依赖为空
     */
    public ReviewService(ApplicationContext context) {
        if (context == null) {
            throw new IllegalArgumentException("服务依赖不能为空");
        }
        this.context = context;
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
        System.out.println("[factory-ioc/v1] ReviewService.review ---> 查询模拟人工复核状态");
        return context.getBean(MockAgencyClient.class).review(caseId);
    }
}
