package com.advance.patterns.comprehensive.ecommerce.v2.discount;

import com.advance.patterns.comprehensive.ecommerce.v2.domain.OrderContext;
import com.advance.patterns.comprehensive.ecommerce.v2.domain.Promotion;
import java.math.BigDecimal;

/**
 * 优惠计算策略统一协议；不负责判断订单是否满足优惠券条件。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public interface DiscountStrategy {
    /**
     * 根据活动配置和订单金额计算优惠值。
     *
     * @param order 当前订单上下文
     * @param promotion 当前活动配置
     * @return 不大于订单金额的非负优惠金额
     */
    BigDecimal calculate(OrderContext order, Promotion promotion);
}
