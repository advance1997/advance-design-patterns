package com.advance.patterns.behavioral.state.v1;

import com.advance.patterns.behavioral.state.v1.domain.InvalidTransitionException;
import com.advance.patterns.behavioral.state.v1.domain.SettlementEvent;
import com.advance.patterns.behavioral.state.v1.domain.SettlementPlan;
import com.advance.patterns.behavioral.state.v1.service.SettlementService;
import org.junit.jupiter.api.Test;

/**
 * 只验证正常状态路径、审批与法核驳回，以及相同事件在不同状态下的拒绝行为。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
class SettlementStateTest {

    /**
     * 输入草稿方案，依次提交 SUBMIT、APPROVE、LEGAL_PASS、CONFIRM_COMPLETION。
     * 期望：DRAFT → PENDING_APPROVAL → PENDING_LEGAL → PERFORMING → COMPLETED。
     * v3 中每次根据方案新状态选择不同处理器，而不是由测试直接指定处理器。
     */
    @Test
    void completeLifecycle() {
        SettlementService service = createService();
        SettlementPlan plan = new SettlementPlan(10001L);
        service.handle(plan, SettlementEvent.SUBMIT);
        service.handle(plan, SettlementEvent.APPROVE);
        service.handle(plan, SettlementEvent.LEGAL_PASS);
        service.handle(plan, SettlementEvent.CONFIRM_COMPLETION);
        System.out.println("最终状态：" + plan.getStatus());
    }

    /**
     * 输入两个草稿方案：第一个提交后审批驳回；第二个审批通过后法核驳回。
     * 期望：同一个 REJECT 事件分别由 PENDING_APPROVAL、PENDING_LEGAL 解释，最终均为 REJECTED。
     */
    @Test
    void rejectAtEitherReviewState() {
        SettlementService service = createService();
        SettlementPlan approval = new SettlementPlan(10001L);
        service.handle(approval, SettlementEvent.SUBMIT);
        service.handle(approval, SettlementEvent.REJECT);
        System.out.println("审批驳回后状态：" + approval.getStatus());
        SettlementPlan legal = new SettlementPlan(10002L);
        service.handle(legal, SettlementEvent.SUBMIT);
        service.handle(legal, SettlementEvent.APPROVE);
        service.handle(legal, SettlementEvent.REJECT);
        System.out.println("法核驳回后状态：" + legal.getStatus());
    }

    /**
     * 输入草稿，直接 APPROVE；随后合法提交并驳回，再尝试 SUBMIT。
     * 期望：两次非法操作均抛 InvalidTransitionException，状态分别保持 DRAFT、REJECTED。
     * 体现状态决定行为以及终态不能重新提交，而不是由事件直接覆盖状态。
     */
    @Test
    void rejectIllegalAndTerminalEvents() {
        SettlementService service = createService();
        SettlementPlan plan = new SettlementPlan(10001L);
        try {
            service.handle(plan, SettlementEvent.APPROVE);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (InvalidTransitionException exception) {
            System.out.println("实际异常：" + exception.getMessage() + "，状态=" + plan.getStatus());
        }
        service.handle(plan, SettlementEvent.SUBMIT);
        service.handle(plan, SettlementEvent.REJECT);
        try {
            service.handle(plan, SettlementEvent.SUBMIT);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (InvalidTransitionException exception) {
            System.out.println("实际异常：" + exception.getMessage() + "，状态=" + plan.getStatus());
        }
    }

    /**
     * 显式装配本版本服务，不使用函数式回调包装测试。
     *
     * @return 本版本独立的状态服务
     */
    private SettlementService createService() {
        return new SettlementService();
    }
}
