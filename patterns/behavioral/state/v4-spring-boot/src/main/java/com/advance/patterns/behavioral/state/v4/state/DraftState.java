package com.advance.patterns.behavioral.state.v4.state;

import com.advance.patterns.behavioral.state.v4.domain.SettlementStatus;
import com.advance.patterns.behavioral.state.v4.domain.SettlementEvent;
import com.advance.patterns.behavioral.state.v4.domain.InvalidTransitionException;
import org.springframework.stereotype.Component;

/**
 * 草稿对应的具体状态，仅处理本状态允许的事件。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
@Component
public class DraftState implements SettlementState {

    /**
     * 声明本处理器代表草稿。
     *
     * @return DRAFT
     */
    @Override
    public SettlementStatus supports() {
        return SettlementStatus.DRAFT;
    }

    /**
     * 执行草稿的事件规则，不修改共享处理器字段。
     *
     * @param event 业务事件
     * @return 允许的下一状态
     * @throws InvalidTransitionException 当前状态不允许事件时抛出
     */
    @Override
    public SettlementStatus onEvent(SettlementEvent event) {
        System.out.println("[状态/v4] DraftState.onEvent ---> 当前状态 DRAFT，处理 event=" + event);
        if (event == SettlementEvent.SUBMIT) {
            return SettlementStatus.PENDING_APPROVAL;
        }
        throw new InvalidTransitionException(supports(), event);
    }
}
