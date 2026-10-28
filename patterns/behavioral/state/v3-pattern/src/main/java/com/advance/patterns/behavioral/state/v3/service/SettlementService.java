package com.advance.patterns.behavioral.state.v3.service;

import com.advance.patterns.behavioral.state.v3.domain.SettlementPlan;
import com.advance.patterns.behavioral.state.v3.domain.SettlementEvent;
import com.advance.patterns.behavioral.state.v3.domain.SettlementStatus;
import com.advance.patterns.behavioral.state.v3.state.StateHandlerRegistry;

/**
 * 使用方案当前状态选择行为对象；每次迁移后，下一次同一事件会由新状态解释。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
public class SettlementService {

    /** 状态行为查找入口。 */
    private final StateHandlerRegistry registry;

    /**
     * 注入完整状态注册表。
     *
     * @param registry 状态行为注册表
     */
    public SettlementService(StateHandlerRegistry registry) {
        this.registry = registry;
    }

    /**
     * 按当前状态委托行为，并在成功后更新上下文状态。
     *
     * @param plan 虚构方案上下文
     * @param event 业务事件
     */
    public void handle(SettlementPlan plan, SettlementEvent event) {
        SettlementStatus before = plan.getStatus();
        SettlementStatus after = registry.get(before).onEvent(event);
        plan.changeStatus(after);
        System.out.println("[状态/v3] SettlementService.handle ---> 迁移完成：" + before + " → " + after);
    }
}
