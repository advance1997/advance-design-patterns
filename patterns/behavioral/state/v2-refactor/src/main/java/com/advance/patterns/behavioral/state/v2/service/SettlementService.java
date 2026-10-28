package com.advance.patterns.behavioral.state.v2.service;

import com.advance.patterns.behavioral.state.v2.domain.InvalidTransitionException;
import com.advance.patterns.behavioral.state.v2.domain.SettlementEvent;
import com.advance.patterns.behavioral.state.v2.domain.SettlementStatus;
import com.advance.patterns.behavioral.state.v2.domain.SettlementPlan;
import com.advance.patterns.behavioral.state.v2.policy.TransitionRules;

/**
 * 服务只协调修改，判断移到规则类，但规则仍集中，尚未按状态多态分派。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
public class SettlementService {

    /** 集中定义状态迁移的过渡规则。 */
    private final TransitionRules rules = new TransitionRules();

    /**
     * 先验证事件，再修改当前状态；非法事件保持原状态。
     *
     * @param plan 虚构方案
     * @param event 业务事件
     * @throws InvalidTransitionException 当前状态不允许事件时抛出
     */
    public void handle(SettlementPlan plan, SettlementEvent event) {
        SettlementStatus before = plan.getStatus();
        System.out.println("[状态/v2] SettlementService.handle ---> 当前 status=" + before + "，event=" + event);
        SettlementStatus after = rules.next(before, event);
        plan.changeStatus(after);
        System.out.println("[状态/v2] SettlementService.handle ---> 迁移完成：" + before + " → " + after);
    }
}
