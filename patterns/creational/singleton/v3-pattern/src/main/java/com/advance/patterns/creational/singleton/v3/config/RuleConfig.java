package com.advance.patterns.creational.singleton.v3.config;

import com.advance.patterns.creational.singleton.v3.domain.ContextRuleBook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用默认 singleton scope 演示每个容器、每个 Bean 定义的作用域边界。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
@Configuration(proxyBeanMethods = false)
public class RuleConfig {
    /**
     * 每个上下文为此 Bean 定义持有一个实例。
     * @return 普通规则对象
     */
    @Bean
    public ContextRuleBook contextRuleBook() {
        return new ContextRuleBook();
    }
}
