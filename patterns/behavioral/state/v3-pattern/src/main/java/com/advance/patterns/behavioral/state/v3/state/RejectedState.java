package com.advance.patterns.behavioral.state.v3.state;

import com.advance.patterns.behavioral.state.v3.domain.SettlementStatus;
import com.advance.patterns.behavioral.state.v3.domain.SettlementEvent;
import com.advance.patterns.behavioral.state.v3.domain.InvalidTransitionException;

/**
 * 已驳回对应的具体状态，作为终态拒绝所有后续事件。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
public class RejectedState implements SettlementState {

    /**
     * 声明本处理器代表已驳回。
     *
     * @return REJECTED
     */
    @Override
    public SettlementStatus supports() {
        return SettlementStatus.REJECTED;
    }

    /**
     * 执行已驳回的事件规则，不修改共享处理器字段。
     *
     * @param event 业务事件
     * @return 允许的下一状态
     * @throws InvalidTransitionException 当前状态不允许事件时抛出
     */
    @Override
    public SettlementStatus onEvent(SettlementEvent event) {
        System.out.println("[状态/v3] RejectedState.onEvent ---> 当前状态 REJECTED，处理 event=" + event);
        throw new InvalidTransitionException(supports(), event);
    }
}
