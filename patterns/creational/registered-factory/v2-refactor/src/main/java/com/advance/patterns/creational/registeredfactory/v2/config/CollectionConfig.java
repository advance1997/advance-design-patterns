package com.advance.patterns.creational.registeredfactory.v2.config;

import com.advance.patterns.creational.registeredfactory.v2.handler.EarlyReviewHandler;
import com.advance.patterns.creational.registeredfactory.v2.handler.LateReviewHandler;
import com.advance.patterns.creational.registeredfactory.v2.factory.CollectionHandlerFactory;
import com.advance.patterns.creational.registeredfactory.v2.service.CollectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 仅负责 Spring 装配，业务服务不通过容器查找依赖。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
@Configuration(proxyBeanMethods = false)
public class CollectionConfig {
    /**
     * 创建由容器管理的早期处理器。
     * @return 新处理器
     */
    @Bean
    public EarlyReviewHandler earlyReviewHandler() {
        return new EarlyReviewHandler();
    }

    /**
     * 创建由容器管理的后期处理器。
     * @return 新处理器
     */
    @Bean
    public LateReviewHandler lateReviewHandler() {
        return new LateReviewHandler();
    }

    /**
     * 将容器创建的实例交给业务工厂注册。
     * @param early 早期处理器
     * @param late 后期处理器
     * @return 完成注册的工厂
     */
    @Bean
    public CollectionHandlerFactory collectionHandlerFactory(EarlyReviewHandler early, LateReviewHandler late) {
        return new CollectionHandlerFactory(early, late);
    }

    /**
     * 创建应用服务。
     * @param factory 业务工厂
     * @return 应用服务
     */
    @Bean
    public CollectionService collectionService(CollectionHandlerFactory factory) {
        return new CollectionService(factory);
    }
}
