package com.advance.patterns.behavioral.strategy.v3.registry;

import com.advance.patterns.behavioral.strategy.v3.domain.PaymentChannel;
import com.advance.patterns.behavioral.strategy.v3.strategy.PaymentStrategy;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 按渠道查找已创建的策略；启动时校验重复注册，运行时不新增或修改映射。
 *
 * @author advance
 * @since 2026-10-27 18:35:37
 */
public final class PaymentStrategyRegistry {
    /** 私有映射，构造时复制集合内容，不向外暴露可变引用。 */
    private final Map<PaymentChannel, PaymentStrategy> strategies =
            new EnumMap<PaymentChannel, PaymentStrategy>(PaymentChannel.class);

    /**
     * 注册调用方显式创建的策略，不承担创建职责，也不依赖 Spring。
     * @param strategyList 非空且不含空元素的策略列表
     * @throws IllegalArgumentException 列表为空、含空元素、渠道为空或重复
     */
    public PaymentStrategyRegistry(List<PaymentStrategy> strategyList) {
        if (strategyList == null || strategyList.isEmpty()) {
            throw new IllegalArgumentException("strategyList 不能为空且至少包含一个策略");
        }
        for (PaymentStrategy strategy : strategyList) {
            if (strategy == null) {
                throw new IllegalArgumentException("strategyList 不能包含空策略");
            }
            PaymentChannel channel = strategy.getChannel();
            if (channel == null) {
                throw new IllegalArgumentException("PaymentStrategy.getChannel 不能返回空渠道");
            }
            if (strategies.containsKey(channel)) {
                throw new IllegalArgumentException("渠道重复注册：" + channel);
            }
            strategies.put(channel, strategy);
            System.out.println("[strategy/v3] PaymentStrategyRegistry ---> 注册渠道 "
                    + channel + "，实现类 " + strategy.getClass().getSimpleName());
        }
    }

    /**
     * 按显式渠道选择策略；未知渠道直接失败，禁止静默改走其他支付渠道。
     * @param channel 指定渠道
     * @return 已注册的策略实例
     * @throws IllegalArgumentException 渠道为空或未注册
     */
    public PaymentStrategy getStrategy(PaymentChannel channel) {
        System.out.println("[strategy/v3] PaymentStrategyRegistry.getStrategy ---> 查找渠道 " + channel);
        if (channel == null) {
            throw new IllegalArgumentException("channel 不能为空");
        }
        PaymentStrategy strategy = strategies.get(channel);
        if (strategy == null) {
            throw new IllegalArgumentException("渠道未注册策略：" + channel);
        }
        System.out.println("[strategy/v3] PaymentStrategyRegistry.getStrategy ---> 命中 "
                + strategy.getClass().getSimpleName());
        return strategy;
    }
}
