package com.advance.patterns.behavioral.chainofresponsibility.v1;

import com.advance.patterns.behavioral.chainofresponsibility.v1.domain.OutboundRequest;
import com.advance.patterns.behavioral.chainofresponsibility.v1.gateway.MockCallGateway;
import org.junit.jupiter.api.Test;
import com.advance.patterns.behavioral.chainofresponsibility.v1.service.OutboundService;

/**
 * 仅打印全部通过、首节点短路与后续节点短路三种责任链行为，按注释对照执行路径。
 *
 * @author advance
 * @since 2026-10-28 16:42:38
 */
class OutboundChainTest {

    /**
     * 输入：caseId=DEMO-001、permitted=true、hour=10。
     * 期望路径：权限通过 → 时间通过 → 模拟终点；链式版本随后先退出时间节点，再退出权限节点。
     * 期望结果：模拟申请通过，未发起真实外呼；终点调用次数为 1。
     */
    @Test
    void allowAfterAllRules() {
        MockCallGateway gateway = new MockCallGateway();
        OutboundService service = new OutboundService(gateway);
        OutboundRequest request = new OutboundRequest("DEMO-001", true, 10);
        System.out.println("实际结果：" + service.submit(request));
        System.out.println("模拟终点调用次数：" + gateway.getCallCount());
    }

    /**
     * 输入：caseId=DEMO-001、permitted=false、hour=20。
     * 期望路径：两个条件都不通过，但在第一个权限节点停止；不进入时间节点或业务终点。
     * 期望结果：拒绝：无案件操作权限；终点调用次数为 0。
     */
    @Test
    void stopAtFirstRule() {
        MockCallGateway gateway = new MockCallGateway();
        OutboundService service = new OutboundService(gateway);
        OutboundRequest request = new OutboundRequest("DEMO-001", false, 20);
        System.out.println("实际结果：" + service.submit(request));
        System.out.println("模拟终点调用次数：" + gateway.getCallCount());
    }

    /**
     * 输入：caseId=DEMO-001、permitted=true、hour=20。
     * 期望路径：权限通过并传递 → 时间拒绝；不执行终点；链式版本返回权限节点后退出。
     * 期望结果：拒绝：不在教学时间窗口；终点调用次数为 0。
     */
    @Test
    void stopAtSecondRule() {
        MockCallGateway gateway = new MockCallGateway();
        OutboundService service = new OutboundService(gateway);
        OutboundRequest request = new OutboundRequest("DEMO-001", true, 20);
        System.out.println("实际结果：" + service.submit(request));
        System.out.println("模拟终点调用次数：" + gateway.getCallCount());
    }
}
