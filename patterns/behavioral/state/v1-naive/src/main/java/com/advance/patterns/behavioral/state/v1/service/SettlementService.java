package com.advance.patterns.behavioral.state.v1.service;

import com.advance.patterns.behavioral.state.v1.domain.InvalidTransitionException;
import com.advance.patterns.behavioral.state.v1.domain.SettlementEvent;
import com.advance.patterns.behavioral.state.v1.domain.SettlementStatus;
import com.advance.patterns.behavioral.state.v1.domain.SettlementPlan;

/**
 * 把全部状态分支放在服务中，展示状态增加后集中判断不断膨胀的起点。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
public class SettlementService {

    /**
     * 先验证事件，再修改当前状态；非法事件保持原状态。
     *
     * @param plan 虚构方案
     * @param event 业务事件
     * @throws InvalidTransitionException 当前状态不允许事件时抛出
     */
    public void handle(SettlementPlan plan, SettlementEvent event) {
        SettlementStatus before = plan.getStatus();
        System.out.println("[状态/v1] SettlementService.handle ---> 当前 status=" + before + "，event=" + event);
        SettlementStatus after = next(before, event);
        plan.changeStatus(after);
        System.out.println("[状态/v1] SettlementService.handle ---> 迁移完成：" + before + " → " + after);
    }

    /**
     * 集中解释所有状态的行为。
     *
     * @param status 当前状态
     * @param event 业务事件
     * @return 允许的下一状态
     * @throws InvalidTransitionException 当前状态不允许事件时抛出
     */
    private SettlementStatus next(SettlementStatus status, SettlementEvent event) {
        if (status == SettlementStatus.DRAFT && event == SettlementEvent.SUBMIT) {
            return SettlementStatus.PENDING_APPROVAL;
        }
        if (status == SettlementStatus.PENDING_APPROVAL) {
            if (event == SettlementEvent.APPROVE) {
                return SettlementStatus.PENDING_LEGAL;
            }
            if (event == SettlementEvent.REJECT) {
                return SettlementStatus.REJECTED;
            }
        }
        if (status == SettlementStatus.PENDING_LEGAL) {
            if (event == SettlementEvent.LEGAL_PASS) {
                return SettlementStatus.PERFORMING;
            }
            if (event == SettlementEvent.REJECT) {
                return SettlementStatus.REJECTED;
            }
        }
        if (status == SettlementStatus.PERFORMING && event == SettlementEvent.CONFIRM_COMPLETION) {
            return SettlementStatus.COMPLETED;
        }
        throw new InvalidTransitionException(status, event);
    }
}
