package com.advance.patterns.structural.adapter.v4;

import com.advance.patterns.structural.adapter.v4.config.MaterialWebConfig;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.HandlerAdapter;

/**
 * 用 MockMvc 执行真实 MVC 调度，不启动网络端口，不使用响应断言。
 *
 * @author advance
 * @since 2026-10-28 16:08:56
 */
class MaterialMvcTest {

    /**
     * 请求 /materials/alpha?caseId=10001。
     * 期望：AlphaMaterialController 的匹配适配器为 SimpleControllerHandlerAdapter。
     * 路径：DispatcherServlet → SimpleControllerHandlerAdapter → AlphaMaterialController → 业务服务 → 平台适配器。
     * 响应期望：HTTP 200，caseId=10001、found=true、fileKey=alpha-contract-10001。
     *
     * @throws Exception 请求执行失败时交给 JUnit 报告
     */
    @Test
    void dispatchAlphaHandler() throws Exception {
        try (AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext()) {
            context.setServletContext(new MockServletContext());
            context.register(MaterialWebConfig.class);
            context.refresh();
            Object handler = context.getBean("alphaController");
            for (HandlerAdapter adapter : context.getBeansOfType(HandlerAdapter.class).values()) {
                if (adapter.supports(handler)) {
                    System.out.println("匹配的 MVC 适配器：" + adapter.getClass().getSimpleName());
                }
            }
            MockMvc mvc = MockMvcBuilders.webAppContextSetup(context).build();
            MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/materials/alpha")
                    .param("caseId", "10001")).andReturn();
            System.out.println("实际 HTTP 状态：" + result.getResponse().getStatus());
            System.out.println("实际材料响应：" + result.getResponse().getContentAsString());
        }
    }

    /**
     * 请求 /materials/beta?caseId=10001。
     * 期望：BetaMaterialHttpHandler 的匹配适配器为 HttpRequestHandlerAdapter。
     * 路径：DispatcherServlet → HttpRequestHandlerAdapter → BetaMaterialHttpHandler → 业务服务 → 平台适配器。
     * 响应期望：HTTP 200，caseId=10001、found=true、fileKey=beta-contract-10001。
     *
     * @throws Exception 请求执行失败时交给 JUnit 报告
     */
    @Test
    void dispatchBetaHandler() throws Exception {
        try (AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext()) {
            context.setServletContext(new MockServletContext());
            context.register(MaterialWebConfig.class);
            context.refresh();
            Object handler = context.getBean("betaHandler");
            for (HandlerAdapter adapter : context.getBeansOfType(HandlerAdapter.class).values()) {
                if (adapter.supports(handler)) {
                    System.out.println("匹配的 MVC 适配器：" + adapter.getClass().getSimpleName());
                }
            }
            MockMvc mvc = MockMvcBuilders.webAppContextSetup(context).build();
            MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/materials/beta")
                    .param("caseId", "10001")).andReturn();
            System.out.println("实际 HTTP 状态：" + result.getResponse().getStatus());
            System.out.println("实际材料响应：" + result.getResponse().getContentAsString());
        }
    }
}
