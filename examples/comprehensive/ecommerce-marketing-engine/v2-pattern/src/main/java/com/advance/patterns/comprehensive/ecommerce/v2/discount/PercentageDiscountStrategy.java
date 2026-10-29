package com.advance.patterns.comprehensive.ecommerce.v2.discount;

import com.advance.patterns.comprehensive.ecommerce.v2.domain.OrderContext;
import com.advance.patterns.comprehensive.ecommerce.v2.domain.Promotion;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 按比例计算并受活动封顶金额限制的优惠策略。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class PercentageDiscountStrategy implements DiscountStrategy {
    /**
     * 计算比例优惠后按两位小数四舍五入，再应用封顶和订单金额限制。
     *
     * @param order 当前订单
     * @param promotion 比例及封顶活动配置
     * @return 不超过封顶值或订单金额的优惠金额
     */
    @Override
    public BigDecimal calculate(OrderContext order, Promotion promotion) {
        BigDecimal percentageAmount = order.getAmount()
                .multiply(promotion.getDiscountValue())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal result = percentageAmount.min(promotion.getMaxDiscount())
                .min(order.getAmount());
        System.out.println("[优惠引擎/v2] PercentageDiscountStrategy.calculate ---> 比例优惠："
                + percentageAmount + "，封顶后：" + result);
        return result;
    }
}
