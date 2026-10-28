package com.advance.patterns.creational.registeredfactory.v2.factory;

import com.advance.patterns.creational.registeredfactory.v2.domain.CaseStage;
import com.advance.patterns.creational.registeredfactory.v2.handler.CollectionHandler;
import com.advance.patterns.creational.registeredfactory.v2.handler.EarlyReviewHandler;
import com.advance.patterns.creational.registeredfactory.v2.handler.LateReviewHandler;
import java.util.EnumMap;
import java.util.Map;

/**
 * 显式注册工厂：接收 Spring 创建的具体处理器，逐一维护注册关系。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class CollectionHandlerFactory {
    /** 私有注册表，不向外暴露，也不提供运行时写入入口。 */
    private final Map<CaseStage, CollectionHandler> handlers =
            new EnumMap<CaseStage, CollectionHandler>(CaseStage.class);

    /**
     * 显式注册两种已由容器创建的处理器。
     * @param early 早期处理器
     * @param late 后期处理器
     * @throws IllegalArgumentException 空输入、空阶段或重复阶段
     */
    public CollectionHandlerFactory(EarlyReviewHandler early, LateReviewHandler late) {
        register(early);
        register(late);
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
        System.out.println("[registered-factory/v2] CollectionHandlerFactory.register ---> 注册 "
                + stage + "，实现类 " + handler.getClass().getSimpleName());
    }

    /**
     * 提供已注册的实例；对象创建和生命周期仍由 Spring 负责。
     * @param stage 案件阶段
     * @return 容器管理的处理器
     * @throws IllegalArgumentException 阶段为空或未注册
     */
    public CollectionHandler getHandler(CaseStage stage) {
        System.out.println("[registered-factory/v2] CollectionHandlerFactory.getHandler ---> 查找阶段 " + stage);
        if (stage == null || !handlers.containsKey(stage)) {
            throw new IllegalArgumentException("阶段未注册处理器：" + stage);
        }
        return handlers.get(stage);
    }
}
