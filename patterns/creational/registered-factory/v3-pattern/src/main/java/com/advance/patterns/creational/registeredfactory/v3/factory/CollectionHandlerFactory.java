package com.advance.patterns.creational.registeredfactory.v3.factory;

import com.advance.patterns.creational.registeredfactory.v3.domain.CaseStage;
import com.advance.patterns.creational.registeredfactory.v3.handler.CollectionHandler;
import java.util.List;
import java.util.EnumMap;
import java.util.Map;

/**
 * 注册式工厂：接收 Spring 集合注入的处理器，构造时校验并注册，运行时只查找。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class CollectionHandlerFactory {
    /** 私有注册表，不向外暴露，也不提供运行时写入入口。 */
    private final Map<CaseStage, CollectionHandler> handlers =
            new EnumMap<CaseStage, CollectionHandler>(CaseStage.class);

    /**
     * 注册容器发现的处理器；不覆盖重复阶段，也不保留传入列表。
     * @param candidates 非空处理器列表
     * @throws IllegalArgumentException 空输入、空阶段或重复阶段
     */
    public CollectionHandlerFactory(List<CollectionHandler> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            throw new IllegalArgumentException("candidates 不能为空且至少注册一个处理器");
        }
        for (CollectionHandler handler : candidates) {
            register(handler);
        }
    }

    /**
     * 校验并添加唯一的阶段映射。
     * @param handler 待注册实例
     * @throws IllegalArgumentException 处理器、阶段为空或阶段重复
     */
    private void register(CollectionHandler handler) {
        if (handler == null || handler.getStage() == null) {
            throw new IllegalArgumentException("处理器及其 stage 不能为空");
        }
        CaseStage stage = handler.getStage();
        if (handlers.containsKey(stage)) {
            throw new IllegalArgumentException("阶段重复注册：" + stage);
        }
        handlers.put(stage, handler);
        System.out.println("[registered-factory/v3] CollectionHandlerFactory.register ---> 注册 "
                + stage + "，实现类 " + handler.getClass().getSimpleName());
    }

    /**
     * 提供已注册的实例；对象创建和生命周期仍由 Spring 负责。
     * @param stage 案件阶段
     * @return 容器管理的处理器
     * @throws IllegalArgumentException 阶段为空或未注册
     */
    public CollectionHandler getHandler(CaseStage stage) {
        System.out.println("[registered-factory/v3] CollectionHandlerFactory.getHandler ---> 查找阶段 " + stage);
        if (stage == null || !handlers.containsKey(stage)) {
            throw new IllegalArgumentException("阶段未注册处理器：" + stage);
        }
        return handlers.get(stage);
    }
}
