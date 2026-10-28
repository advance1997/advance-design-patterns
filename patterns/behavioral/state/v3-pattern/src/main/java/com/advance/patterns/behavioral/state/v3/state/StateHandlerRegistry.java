package com.advance.patterns.behavioral.state.v3.state;

import com.advance.patterns.behavioral.state.v3.domain.SettlementStatus;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 按方案当前状态查找行为对象；注册表只查找，不承担状态迁移规则。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
public class StateHandlerRegistry {

    /** 完整状态与行为对象的映射，构造后不修改。 */
    private final Map<SettlementStatus, SettlementState> handlers =
            new EnumMap<SettlementStatus, SettlementState>(SettlementStatus.class);

    /**
     * 注册所有状态处理器，并检查缺失或重复配置。
     *
     * @param states 独立的无请求状态处理器列表
     * @throws IllegalArgumentException 状态重复或缺少处理器时抛出
     */
    public StateHandlerRegistry(List<SettlementState> states) {
        for (SettlementState state : states) {
            if (handlers.put(state.supports(), state) != null) {
                throw new IllegalArgumentException("重复状态处理器：" + state.supports());
            }
        }
        for (SettlementStatus status : SettlementStatus.values()) {
            if (!handlers.containsKey(status)) {
                throw new IllegalArgumentException("缺少状态处理器：" + status);
            }
        }
    }

    /**
     * 根据上下文当前状态选择对应行为，而不是让调用方指定处理器。
     *
     * @param status 方案当前状态
     * @return 对应的状态行为对象
     */
    public SettlementState get(SettlementStatus status) {
        System.out.println("[状态/v3] StateHandlerRegistry.get ---> 按当前状态查找处理器：" + status);
        return handlers.get(status);
    }
}
