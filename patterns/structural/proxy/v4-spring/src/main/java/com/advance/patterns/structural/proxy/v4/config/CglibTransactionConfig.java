package com.advance.patterns.structural.proxy.v4.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 显式启用 Spring CGLIB 类事务代理，测试时只加载一种代理配置。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
@Configuration(proxyBeanMethods = false)
@Import(BusinessConfig.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class CglibTransactionConfig {
}
