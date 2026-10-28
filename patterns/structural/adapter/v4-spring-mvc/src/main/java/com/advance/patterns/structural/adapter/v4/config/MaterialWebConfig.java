package com.advance.patterns.structural.adapter.v4.config;

import com.advance.patterns.structural.adapter.v4.adapter.AlphaMaterialAdapter;
import com.advance.patterns.structural.adapter.v4.adapter.BetaMaterialAdapter;
import com.advance.patterns.structural.adapter.v4.external.AlphaMaterialClient;
import com.advance.patterns.structural.adapter.v4.external.BetaMaterialClient;
import com.advance.patterns.structural.adapter.v4.port.MaterialGateway;
import com.advance.patterns.structural.adapter.v4.service.MaterialCollectionService;
import com.advance.patterns.structural.adapter.v4.web.AlphaMaterialController;
import com.advance.patterns.structural.adapter.v4.web.BetaMaterialHttpHandler;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

/**
 * 注册业务适配器与两种 MVC 处理器；由真实 DispatcherServlet 和内置 HandlerAdapter 完成调度。
 *
 * @author advance
 * @since 2026-10-28 16:08:56
 */
@Configuration
@EnableWebMvc
public class MaterialWebConfig {

    /**
     * 创建 Alpha 平台的对象适配器。
     *
     * @return 内部材料查询接口
     */
    @Bean
    public MaterialGateway alphaGateway() {
        return new AlphaMaterialAdapter(new AlphaMaterialClient());
    }

    /**
     * 创建 Beta 平台的对象适配器。
     *
     * @return 内部材料查询接口
     */
    @Bean
    public MaterialGateway betaGateway() {
        return new BetaMaterialAdapter(new BetaMaterialClient());
    }

    /**
     * 为 Alpha 入口注入统一目标接口。
     *
     * @param gateway Alpha 平台适配器
     * @return 平台无关的业务服务实例
     */
    @Bean
    public MaterialCollectionService alphaService(@Qualifier("alphaGateway") MaterialGateway gateway) {
        return new MaterialCollectionService(gateway);
    }

    /**
     * 为 Beta 入口注入统一目标接口，服务类与 Alpha 完全相同。
     *
     * @param gateway Beta 平台适配器
     * @return 平台无关的业务服务实例
     */
    @Bean
    public MaterialCollectionService betaService(@Qualifier("betaGateway") MaterialGateway gateway) {
        return new MaterialCollectionService(gateway);
    }

    /**
     * 提供旧式 Controller 处理器以观察对应的 MVC 适配器。
     *
     * @param service 注入 Alpha 适配器的归集服务
     * @return 实现 Controller 的入口
     */
    @Bean
    public AlphaMaterialController alphaController(
            @Qualifier("alphaService") MaterialCollectionService service) {
        return new AlphaMaterialController(service);
    }

    /**
     * 提供 HttpRequestHandler 处理器以比较不同的 MVC 调用契约。
     *
     * @param service 注入 Beta 适配器的归集服务
     * @return 实现 HttpRequestHandler 的入口
     */
    @Bean
    public BetaMaterialHttpHandler betaHandler(
            @Qualifier("betaService") MaterialCollectionService service) {
        return new BetaMaterialHttpHandler(service);
    }

    /**
     * 只负责将教学 URL 映射到处理器；如何调用由 HandlerAdapter 决定。
     *
     * @param alphaController Alpha 入口
     * @param betaHandler Beta 入口
     * @return 两个固定路径的映射
     */
    @Bean
    public SimpleUrlHandlerMapping materialHandlerMapping(
            AlphaMaterialController alphaController, BetaMaterialHttpHandler betaHandler) {
        Map<String, Object> handlers = new LinkedHashMap<String, Object>();
        handlers.put("/materials/alpha", alphaController);
        handlers.put("/materials/beta", betaHandler);
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(handlers);
        mapping.setOrder(0);
        return mapping;
    }
}
