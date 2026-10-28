package com.advance.patterns.creational.factoryioc.v3.config;

import com.advance.patterns.creational.factoryioc.v3.client.MockAgencyClient;
import com.advance.patterns.creational.factoryioc.v3.service.ReviewService;
import com.advance.patterns.creational.factoryioc.v3.domain.ReviewSession;
import com.advance.patterns.creational.factoryioc.v3.factory.AgencyClientFactoryBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 将业务对象创建与容器生命周期配置集中在装配层。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
@Configuration(proxyBeanMethods = false)
public class ReviewConfig {
    /**
     * 声明延迟工厂；通过名称获取产品，通过 &amp;agencyClient 获取工厂本身。
     * @return 创建客户端的工厂 Bean
     */
    @Bean
    @Lazy
    public AgencyClientFactoryBean agencyClient() {
        return new AgencyClientFactoryBean();
    }

    /**
     * 声明每次获取均创建的会话，销毁责任归使用方。
     * @return 新会话
     */
    @Bean
    @Scope("prototype")
    public ReviewSession reviewSession() {
        return new ReviewSession();
    }

    /**
     * 创建复核服务。
     * @param client 容器管理的客户端
     * @param sessions 容器提供的会话获取入口
     * @return 复核服务
     */
    @Bean
    @Lazy
    public ReviewService reviewService(MockAgencyClient client, ObjectFactory<ReviewSession> sessions) {
        return new ReviewService(client, sessions);
    }
}
