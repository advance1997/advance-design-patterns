package com.advance.patterns.behavioral.chainofresponsibility.v1.service;

import com.advance.patterns.behavioral.chainofresponsibility.v1.domain.OutboundRequest;
import com.advance.patterns.behavioral.chainofresponsibility.v1.gateway.MockCallGateway;

/**
 * 将规则、顺序与短路判断集中在业务方法中，展示最直接的实现。
 *
 * @author advance
 * @since 2026-10-28 16:42:38
 */
public class OutboundService {

    /** 所有规则通过后才调用的模拟终点。 */
    private final MockCallGateway gateway;

    /**
     * 注入用于观察调用次数的模拟终点。
     *
     * @param gateway 不发送真实外呼的模拟网关
     */
    public OutboundService(MockCallGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 按权限、时间窗口的固定顺序检查，拒绝时提前返回。
     *
     * @param request 虚构外呼申请
     * @return 拒绝原因或模拟申请通过信息
     */
    public String submit(OutboundRequest request) {
        System.out.println("[责任链/v1] OutboundService.submit ---> 开始权限检查");
        if (!request.isPermitted()) {
            System.out.println("[责任链/v1] OutboundService.submit ---> 权限拒绝，不再检查时间或调用终点");
            return "拒绝：无案件操作权限";
        }
        System.out.println("[责任链/v1] OutboundService.submit ---> 开始教学时间窗口检查");
        if (request.getHour() < 9 || request.getHour() >= 18) {
            System.out.println("[责任链/v1] OutboundService.submit ---> 时间拒绝，不调用终点");
            return "拒绝：不在教学时间窗口";
        }
        return gateway.submit(request);
    }
}
