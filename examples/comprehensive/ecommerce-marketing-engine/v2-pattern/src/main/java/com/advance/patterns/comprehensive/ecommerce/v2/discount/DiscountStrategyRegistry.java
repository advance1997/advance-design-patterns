package com.advance.patterns.comprehensive.ecommerce.v2.discount;

import com.advance.patterns.comprehensive.ecommerce.v2.domain.DiscountType;
import java.util.EnumMap;
import java.util.Map;

/**
 * 按活动优惠类型选择对应算法，隔离类型路由与实际优惠计算。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class DiscountStrategyRegistry {
    /** 优惠类型到无状态策略的映射。 */
    private final Map<DiscountType, DiscountStrategy> strategies =
            new EnumMap<DiscountType, DiscountStrategy>(DiscountType.class);

    /**
     * 注册固定金额和比例两种教学策略。
     */
    public DiscountStrategyRegistry() {
        strategies.put(DiscountType.FIXED, new FixedDiscountStrategy());
        strategies.put(DiscountType.PERCENTAGE, new PercentageDiscountStrategy());
        System.out.println("[优惠引擎/v2] DiscountStrategyRegistry 构造方法 ---> 注册固定立减和比例折扣策略");
    }

    /**
     * 获取指定优惠类型的计算算法。
     *
     * @param discountType 活动配置的优惠类型
     * @return 对应的优惠策略
     * @throws IllegalArgumentException 没有注册该类型时抛出
     */
    public DiscountStrategy get(DiscountType discountType) {
        DiscountStrategy strategy = strategies.get(discountType);
        if (strategy == null) {
            throw new IllegalArgumentException("未注册优惠策略：" + discountType);
        }
        return strategy;
    }
}
