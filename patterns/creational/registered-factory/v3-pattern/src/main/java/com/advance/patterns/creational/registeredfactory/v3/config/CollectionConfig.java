package com.advance.patterns.creational.registeredfactory.v3.config;

import com.advance.patterns.creational.registeredfactory.v3.handler.EarlyReviewHandler;
import com.advance.patterns.creational.registeredfactory.v3.handler.CollectionHandler;
import java.util.List;
import org.springframework.context.annotation.ComponentScan;
import com.advance.patterns.creational.registeredfactory.v3.factory.CollectionHandlerFactory;
import com.advance.patterns.creational.registeredfactory.v3.service.CollectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 仅负责 Spring 装配，业务服务不通过容器查找依赖。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackageClasses = EarlyReviewHandler.class)
public class CollectionConfig {

    /**
     * 将容器创建的实例交给业务工厂注册。
     * @param handlers Spring 自动收集的全部处理器
     * @return 完成注册的工厂
     */
    @Bean
    public CollectionHandlerFactory collectionHandlerFactory(List<CollectionHandler> handlers) {
        return new CollectionHandlerFactory(handlers);
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
