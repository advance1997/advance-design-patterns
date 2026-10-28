package com.advance.patterns.behavioral.chainofresponsibility.v3.chain;

import com.advance.patterns.behavioral.chainofresponsibility.v3.domain.OutboundRequest;

/**
 * 规则节点契约：当前节点自行决定返回拒绝，还是调用责任链继续处理。
 *
 * @author advance
 * @since 2026-10-28 16:42:38
 */
public interface RuleHandler {

    /**
     * 执行当前规则，只有通过时才调用 chain.proceed。
     *
     * @param request 本次虚构申请
     * @param chain 当前请求专用的责任链游标
     * @return 拒绝原因或后续节点返回的结果
     */
    String handle(OutboundRequest request, RuleChain chain);
}
