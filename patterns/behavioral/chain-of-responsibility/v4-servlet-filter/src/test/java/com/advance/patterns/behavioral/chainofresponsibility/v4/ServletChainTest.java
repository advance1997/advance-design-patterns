package com.advance.patterns.behavioral.chainofresponsibility.v4;

import com.advance.patterns.behavioral.chainofresponsibility.v4.domain.OutboundRequest;
import com.advance.patterns.behavioral.chainofresponsibility.v4.gateway.MockCallGateway;
import org.junit.jupiter.api.Test;
import com.advance.patterns.behavioral.chainofresponsibility.v4.config.OutboundWebConfig;
import com.advance.patterns.behavioral.chainofresponsibility.v4.filter.PermissionFilter;
import com.advance.patterns.behavioral.chainofresponsibility.v4.filter.ContactWindowFilter;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * 仅打印全部通过、首节点短路与后续节点短路三种责任链行为，按注释对照执行路径。
 *
 * @author advance
 * @since 2026-10-28 16:42:38
 */
class ServletChainTest {

    /**
     * 输入：caseId=DEMO-001、permitted=true、hour=10。
     * 期望路径：权限通过 → 时间通过 → 模拟终点；链式版本随后先退出时间节点，再退出权限节点。
     * 期望结果：模拟申请通过，未发起真实外呼；终点调用次数为 1。
     * 期望 HTTP 状态：200；拒绝场景不进入 OutboundController。
     *
     * @throws Exception MVC 请求执行失败时交给 JUnit 报告
     */
    @Test
    void allowAfterAllRules() throws Exception {
        try (AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext()) {
            context.setServletContext(new MockServletContext());
            context.register(OutboundWebConfig.class);
            context.refresh();
            MockMvc mvc = MockMvcBuilders.webAppContextSetup(context)
                    .addFilters(context.getBean(PermissionFilter.class), context.getBean(ContactWindowFilter.class))
                    .build();
            OutboundRequest request = new OutboundRequest("DEMO-001", true, 10);
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/outbound/demo")
                    .requestAttr("outboundRequest", request)).andReturn();
            System.out.println("实际 HTTP 状态：" + result.getResponse().getStatus());
            System.out.println("实际结果：" + result.getResponse().getContentAsString());
            System.out.println("模拟终点调用次数：" + context.getBean(MockCallGateway.class).getCallCount());
        }
    }

    /**
     * 输入：caseId=DEMO-001、permitted=false、hour=20。
     * 期望路径：两个条件都不通过，但在第一个权限节点停止；不进入时间节点或业务终点。
     * 期望结果：拒绝：无案件操作权限；终点调用次数为 0。
     * 期望 HTTP 状态：403；拒绝场景不进入 OutboundController。
     *
     * @throws Exception MVC 请求执行失败时交给 JUnit 报告
     */
    @Test
    void stopAtFirstRule() throws Exception {
        try (AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext()) {
            context.setServletContext(new MockServletContext());
            context.register(OutboundWebConfig.class);
            context.refresh();
            MockMvc mvc = MockMvcBuilders.webAppContextSetup(context)
                    .addFilters(context.getBean(PermissionFilter.class), context.getBean(ContactWindowFilter.class))
                    .build();
            OutboundRequest request = new OutboundRequest("DEMO-001", false, 20);
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/outbound/demo")
                    .requestAttr("outboundRequest", request)).andReturn();
            System.out.println("实际 HTTP 状态：" + result.getResponse().getStatus());
            System.out.println("实际结果：" + result.getResponse().getContentAsString());
            System.out.println("模拟终点调用次数：" + context.getBean(MockCallGateway.class).getCallCount());
        }
    }

    /**
     * 输入：caseId=DEMO-001、permitted=true、hour=20。
     * 期望路径：权限通过并传递 → 时间拒绝；不执行终点；链式版本返回权限节点后退出。
     * 期望结果：拒绝：不在教学时间窗口；终点调用次数为 0。
     * 期望 HTTP 状态：403；拒绝场景不进入 OutboundController。
     *
     * @throws Exception MVC 请求执行失败时交给 JUnit 报告
     */
    @Test
    void stopAtSecondRule() throws Exception {
        try (AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext()) {
            context.setServletContext(new MockServletContext());
            context.register(OutboundWebConfig.class);
            context.refresh();
            MockMvc mvc = MockMvcBuilders.webAppContextSetup(context)
                    .addFilters(context.getBean(PermissionFilter.class), context.getBean(ContactWindowFilter.class))
                    .build();
            OutboundRequest request = new OutboundRequest("DEMO-001", true, 20);
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/outbound/demo")
                    .requestAttr("outboundRequest", request)).andReturn();
            System.out.println("实际 HTTP 状态：" + result.getResponse().getStatus());
            System.out.println("实际结果：" + result.getResponse().getContentAsString());
            System.out.println("模拟终点调用次数：" + context.getBean(MockCallGateway.class).getCallCount());
        }
    }
}
