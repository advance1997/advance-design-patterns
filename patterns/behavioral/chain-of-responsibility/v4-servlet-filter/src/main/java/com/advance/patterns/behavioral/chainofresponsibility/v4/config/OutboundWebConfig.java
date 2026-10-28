package com.advance.patterns.behavioral.chainofresponsibility.v4.config;

import com.advance.patterns.behavioral.chainofresponsibility.v4.filter.PermissionFilter;
import com.advance.patterns.behavioral.chainofresponsibility.v4.filter.ContactWindowFilter;
import com.advance.patterns.behavioral.chainofresponsibility.v4.gateway.MockCallGateway;
import com.advance.patterns.behavioral.chainofresponsibility.v4.web.OutboundController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Spring 管理教学组件；测试显式将两个 Filter 按顺序注册到 MockMvc，而不是依赖 Bean 声明顺序。
 *
 * @author advance
 * @since 2026-10-28 16:42:38
 */
@Configuration
@EnableWebMvc
public class OutboundWebConfig {

    /**
     * 创建单个测试上下文独立的模拟网关。
     *
     * @return 不执行真实外呼的网关
     */
    @Bean
    public MockCallGateway mockCallGateway() {
        return new MockCallGateway();
    }

    /**
     * 创建权限过滤器。
     *
     * @return 无请求状态的规则节点
     */
    @Bean
    public PermissionFilter permissionFilter() {
        return new PermissionFilter();
    }

    /**
     * 创建教学时间过滤器。
     *
     * @return 无请求状态的规则节点
     */
    @Bean
    public ContactWindowFilter contactWindowFilter() {
        return new ContactWindowFilter();
    }

    /**
     * 注入模拟终点并创建 MVC 控制器。
     *
     * @param gateway 模拟网关
     * @return 教学业务入口
     */
    @Bean
    public OutboundController outboundController(MockCallGateway gateway) {
        return new OutboundController(gateway);
    }
}
