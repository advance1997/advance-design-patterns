package com.advance.patterns.creational.factoryioc.v1.config;

import com.advance.patterns.creational.factoryioc.v1.client.MockAgencyClient;
import com.advance.patterns.creational.factoryioc.v1.service.ReviewService;
import org.springframework.context.ApplicationContext;
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
     * 配置普通 Bean 的初始化与关闭回调。
     * @return 未打开的客户端，由容器调用 open
     */
    @Bean(initMethod = "open", destroyMethod = "close")
    public MockAgencyClient agencyClient() {
        return new MockAgencyClient();
    }

    /**
     * 创建复核服务。
     * @param context 容器（反例中的服务定位器）
     * @return 复核服务
     */
    @Bean
    public ReviewService reviewService(ApplicationContext context) {
        return new ReviewService(context);
    }
}
