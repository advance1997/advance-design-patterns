package com.advance.patterns.behavioral.chainofresponsibility.v4.web;

import com.advance.patterns.behavioral.chainofresponsibility.v4.domain.OutboundRequest;
import com.advance.patterns.behavioral.chainofresponsibility.v4.gateway.MockCallGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

/**
 * 过滤器全部放行后才执行的教学入口，终点只记录模拟申请，不发起真实外呼。
 *
 * @author advance
 * @since 2026-10-28 16:42:38
 */
@RestController
public class OutboundController {

    /** 可打印调用次数的模拟业务终点。 */
    private final MockCallGateway gateway;

    /**
     * 通过构造器接收模拟网关。
     *
     * @param gateway 不包含真实呼叫能力的终点
     */
    public OutboundController(MockCallGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 处理已通过两个过滤器的教学申请。
     *
     * @param request 测试预先注入的上下文，不是前端提交的权限声明
     * @return 模拟通过结果
     */
    @PostMapping(value = "/outbound/demo", produces = "text/plain;charset=UTF-8")
    public String submit(@RequestAttribute("outboundRequest") OutboundRequest request) {
        System.out.println("[责任链/v4] OutboundController.submit ---> 两个过滤器已放行，到达业务入口");
        return gateway.submit(request);
    }
}
