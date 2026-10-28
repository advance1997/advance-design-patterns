package com.advance.patterns.behavioral.state.v2.policy;

import com.advance.patterns.behavioral.state.v2.domain.InvalidTransitionException;
import com.advance.patterns.behavioral.state.v2.domain.SettlementEvent;
import com.advance.patterns.behavioral.state.v2.domain.SettlementStatus;

/**
 * 提取迁移判断的过渡规则类，仍通过集中条件分支解释全部状态。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
public class TransitionRules {

    /**
     * 根据当前状态和事件计算下一状态，不直接修改方案。
     *
     * @param status 当前状态
     * @param event 业务事件
     * @return 允许的下一状态
     * @throws InvalidTransitionException 状态与事件不匹配时抛出
     */
    public SettlementStatus next(SettlementStatus status, SettlementEvent event) {
        System.out.println("[状态/v2] TransitionRules.next ---> 集中判断 status=" + status + "，event=" + event);
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
