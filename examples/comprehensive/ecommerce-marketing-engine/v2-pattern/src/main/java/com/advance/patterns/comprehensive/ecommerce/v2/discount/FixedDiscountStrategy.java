package com.advance.patterns.comprehensive.ecommerce.v2.discount;

import com.advance.patterns.comprehensive.ecommerce.v2.domain.OrderContext;
import com.advance.patterns.comprehensive.ecommerce.v2.domain.Promotion;
import java.math.BigDecimal;

/**
 * 固定金额立减策略。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class FixedDiscountStrategy implements DiscountStrategy {
    /**
     * 返回固定优惠金额，并避免优惠大于订单金额。
     *
     * @param order 当前订单
     * @param promotion 固定金额活动
     * @return 固定金额与订单金额中的较小值
     */
    @Override
    public BigDecimal calculate(OrderContext order, Promotion promotion) {
        BigDecimal result = promotion.getDiscountValue().min(order.getAmount());
        System.out.println("[优惠引擎/v2] FixedDiscountStrategy.calculate ---> 固定立减：" + result);
        return result;
    }
}
