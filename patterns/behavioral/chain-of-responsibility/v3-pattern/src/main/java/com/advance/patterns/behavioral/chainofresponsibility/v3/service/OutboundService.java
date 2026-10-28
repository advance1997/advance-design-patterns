package com.advance.patterns.behavioral.chainofresponsibility.v3.service;

import com.advance.patterns.behavioral.chainofresponsibility.v3.chain.RuleChain;
import com.advance.patterns.behavioral.chainofresponsibility.v3.chain.RuleHandler;
import com.advance.patterns.behavioral.chainofresponsibility.v3.domain.OutboundRequest;
import com.advance.patterns.behavioral.chainofresponsibility.v3.gateway.MockCallGateway;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 只负责启动责任链，不知道具体规则名称，也不编写规则分支。
 *
 * @author advance
 * @since 2026-10-28 16:42:38
 */
public class OutboundService {

    /** 装配时确定的规则节点顺序。 */
    private final List<RuleHandler> handlers;

    /** 规则全部通过才会到达的模拟终点。 */
    private final MockCallGateway gateway;

    /**
     * 注入规则列表与终点，保存规则顺序快照。
     *
     * @param handlers 有序的无请求状态节点
     * @param gateway 模拟终点
     */
    public OutboundService(List<RuleHandler> handlers, MockCallGateway gateway) {
        this.handlers = Collections.unmodifiableList(new ArrayList<RuleHandler>(handlers));
        this.gateway = gateway;
    }

    /**
     * 每次新建带游标的责任链，避免上次执行位置影响下一次申请。
     *
     * @param request 虚构外呼申请
     * @return 拒绝原因或模拟通过信息
     */
    public String submit(OutboundRequest request) {
        System.out.println("[责任链/v3] OutboundService.submit ---> 创建本次请求独立的 RuleChain");
        RuleChain chain = new RuleChain(handlers, gateway);
        return chain.proceed(request);
    }
}
