package com.advance.patterns.behavioral.chainofresponsibility.v3.chain;

import com.advance.patterns.behavioral.chainofresponsibility.v3.domain.OutboundRequest;
import com.advance.patterns.behavioral.chainofresponsibility.v3.gateway.MockCallGateway;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 以索引保存下一节点位置的责任链；每次申请新建实例，不能作为共享单例或跨请求复用。
 *
 * @author advance
 * @since 2026-10-28 16:42:38
 */
public final class RuleChain {

    /** 固定顺序的节点快照，节点本身不保存请求状态。 */
    private final List<RuleHandler> handlers;

    /** 所有规则通过后的模拟业务终点。 */
    private final MockCallGateway gateway;

    /** 下一次 proceed 要执行的节点索引，仅属于本次请求。 */
    private int nextIndex;

    /**
     * 创建单次执行链，复制节点列表避免外部修改顺序。
     *
     * @param handlers 按执行顺序排列的规则节点
     * @param gateway 不执行真实外呼的终点
     */
    public RuleChain(List<RuleHandler> handlers, MockCallGateway gateway) {
        this.handlers = Collections.unmodifiableList(new ArrayList<RuleHandler>(handlers));
        this.gateway = gateway;
    }

    /**
     * 取出下一个节点并在调用前推进游标；没有剩余节点时调用终点。
     * 每层节点只能在放行分支调用本方法一次，拒绝分支不得调用。
     *
     * @param request 同一次申请
     * @return 当前或后续节点的处理结果
     */
    public String proceed(OutboundRequest request) {
        if (nextIndex < handlers.size()) {
            RuleHandler handler = handlers.get(nextIndex);
            nextIndex++;
            System.out.println("[责任链/v3] RuleChain.proceed ---> 调度节点 "
                    + handler.getClass().getSimpleName() + "，下一索引 nextIndex=" + nextIndex);
            return handler.handle(request, this);
        }
        System.out.println("[责任链/v3] RuleChain.proceed ---> 所有节点放行，调用模拟终点");
        return gateway.submit(request);
    }
}
