package com.advance.patterns.behavioral.chainofresponsibility.v2.service;

import com.advance.patterns.behavioral.chainofresponsibility.v2.domain.OutboundRequest;
import com.advance.patterns.behavioral.chainofresponsibility.v2.gateway.MockCallGateway;
import com.advance.patterns.behavioral.chainofresponsibility.v2.rule.PermissionRule;
import com.advance.patterns.behavioral.chainofresponsibility.v2.rule.ContactWindowRule;

/**
 * 规则已提取，但顺序和每个短路分支仍集中在业务服务中，尚未形成责任链。
 *
 * @author advance
 * @since 2026-10-28 16:42:38
 */
public class OutboundService {

    /** 所有规则通过后才调用的模拟终点。 */
    private final MockCallGateway gateway;

    /** 独立的权限规则。 */
    private final PermissionRule permissionRule = new PermissionRule();

    /** 独立的时间窗口规则。 */
    private final ContactWindowRule contactWindowRule = new ContactWindowRule();

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
        System.out.println("[责任链/v2] OutboundService.submit ---> 开始权限检查");
        if (!permissionRule.allows(request)) {
            System.out.println("[责任链/v2] OutboundService.submit ---> 权限拒绝，不再检查时间或调用终点");
            return "拒绝：无案件操作权限";
        }
        System.out.println("[责任链/v2] OutboundService.submit ---> 开始教学时间窗口检查");
        if (!contactWindowRule.allows(request)) {
            System.out.println("[责任链/v2] OutboundService.submit ---> 时间拒绝，不调用终点");
            return "拒绝：不在教学时间窗口";
        }
        return gateway.submit(request);
    }
}
