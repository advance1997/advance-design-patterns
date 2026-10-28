package com.advance.patterns.behavioral.chainofresponsibility.v1.gateway;

import com.advance.patterns.behavioral.chainofresponsibility.v1.domain.OutboundRequest;

/**
 * 只记录调用次数并返回教学文本的外呼终点；不包含号码、不发送真实呼叫。
 *
 * @author advance
 * @since 2026-10-28 16:42:38
 */
public class MockCallGateway {

    /** 当前测试实例的调用次数；仅用于单线程打印观察，不是生产计数器。 */
    private int callCount;

    /**
     * 模拟处理规则全部通过的申请。
     *
     * @param request 虚构外呼申请
     * @return 明确标记未发起真实外呼的结果
     */
    public String submit(OutboundRequest request) {
        callCount++;
        System.out.println("[责任链/v1] MockCallGateway.submit ---> 到达终点，仅记录模拟申请，caseId="
                + request.getCaseId());
        return "模拟申请通过，未发起真实外呼";
    }

    /**
     * 获取终点被调用的次数，以观察短路是否阻止业务执行。
     *
     * @return 本实例的调用次数
     */
    public int getCallCount() {
        return callCount;
    }
}
