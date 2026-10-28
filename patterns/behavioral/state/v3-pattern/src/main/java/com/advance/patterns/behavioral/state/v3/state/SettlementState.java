package com.advance.patterns.behavioral.state.v3.state;

import com.advance.patterns.behavioral.state.v3.domain.SettlementStatus;
import com.advance.patterns.behavioral.state.v3.domain.SettlementEvent;

/**
 * 统一状态行为接口；每个具体状态只解释自己允许的事件，不读写数据库。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
public interface SettlementState {

    /**
     * 声明当前处理器对应的状态。
     *
     * @return 所负责的状态
     */
    SettlementStatus supports();

    /**
     * 根据事件执行当前状态的迁移规则。
     *
     * @param event 客户端请求的业务事件
     * @return 允许的下一状态
     * @throws com.advance.patterns.behavioral.state.v3.domain.InvalidTransitionException 事件不允许时抛出
     */
    SettlementStatus onEvent(SettlementEvent event);
}
