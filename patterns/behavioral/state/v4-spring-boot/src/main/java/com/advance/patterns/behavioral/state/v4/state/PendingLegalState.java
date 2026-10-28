package com.advance.patterns.behavioral.state.v4.state;

import com.advance.patterns.behavioral.state.v4.domain.SettlementStatus;
import com.advance.patterns.behavioral.state.v4.domain.SettlementEvent;
import com.advance.patterns.behavioral.state.v4.domain.InvalidTransitionException;
import org.springframework.stereotype.Component;

/**
 * 待法核对应的具体状态，仅处理本状态允许的事件。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
@Component
public class PendingLegalState implements SettlementState {

    /**
     * 声明本处理器代表待法核。
     *
     * @return PENDING_LEGAL
     */
    @Override
    public SettlementStatus supports() {
        return SettlementStatus.PENDING_LEGAL;
    }

    /**
     * 执行待法核的事件规则，不修改共享处理器字段。
     *
     * @param event 业务事件
     * @return 允许的下一状态
     * @throws InvalidTransitionException 当前状态不允许事件时抛出
     */
    @Override
    public SettlementStatus onEvent(SettlementEvent event) {
        System.out.println("[状态/v4] PendingLegalState.onEvent ---> 当前状态 PENDING_LEGAL，处理 event=" + event);
        if (event == SettlementEvent.LEGAL_PASS) {
            return SettlementStatus.PERFORMING;
        }
        if (event == SettlementEvent.REJECT) {
            return SettlementStatus.REJECTED;
        }
        throw new InvalidTransitionException(supports(), event);
    }
}
