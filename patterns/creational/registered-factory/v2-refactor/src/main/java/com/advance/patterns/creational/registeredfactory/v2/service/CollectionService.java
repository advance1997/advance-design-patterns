package com.advance.patterns.creational.registeredfactory.v2.service;

import com.advance.patterns.creational.registeredfactory.v2.domain.CaseFile;
import com.advance.patterns.creational.registeredfactory.v2.factory.CollectionHandlerFactory;

/**
 * 只通过业务工厂选择处理器的服务，不持有 BeanFactory 或 ApplicationContext。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class CollectionService {
    /** 注册式业务工厂。 */
    private final CollectionHandlerFactory factory;

    /**
     * 注入服务所需依赖。
     * @param factory 业务工厂
     * @throws IllegalArgumentException 依赖为空
     */
    public CollectionService(CollectionHandlerFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("服务依赖不能为空");
        }
        this.factory = factory;
    }

    /**
     * 根据阶段安排模拟人工复核，禁止静默降级到另一阶段。
     * @param caseFile 虚构案件
     * @return 处理器返回的复核标识
     * @throws IllegalArgumentException 案件为空或阶段不支持
     */
    public String process(CaseFile caseFile) {
        System.out.println("[registered-factory/v2] CollectionService.process ---> 安排模拟人工复核");
        if (caseFile == null) {
            throw new IllegalArgumentException("caseFile 不能为空");
        }
        return factory.getHandler(caseFile.getStage()).handle(caseFile);
    }
}
