package com.advance.patterns.creational.factoryioc.v3.service;

import com.advance.patterns.creational.factoryioc.v3.client.MockAgencyClient;
import com.advance.patterns.creational.factoryioc.v3.domain.ReviewSession;
import org.springframework.beans.factory.ObjectFactory;

/**
 * 明确注入客户端与会话提供器；ObjectFactory 每次获取新的 prototype 会话。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class ReviewService {
    /** 模拟协作客户端。 */
    private final MockAgencyClient client;
    /** prototype 会话的延迟获取入口。 */
    private final ObjectFactory<ReviewSession> sessions;

    /**
     * 保存本版本所需依赖。
     * @param client 模拟客户端
     * @param sessions 会话提供器
     * @throws IllegalArgumentException 依赖为空
     */
    public ReviewService(MockAgencyClient client, ObjectFactory<ReviewSession> sessions) {
        if (client == null || sessions == null) {
            throw new IllegalArgumentException("服务依赖不能为空");
        }
        this.client = client;
        this.sessions = sessions;
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
        System.out.println("[factory-ioc/v3] ReviewService.review ---> 查询模拟人工复核状态");
        try (ReviewSession session = sessions.getObject()) {
            session.bind(caseId);
            return client.review(caseId);
        }
    }
}
