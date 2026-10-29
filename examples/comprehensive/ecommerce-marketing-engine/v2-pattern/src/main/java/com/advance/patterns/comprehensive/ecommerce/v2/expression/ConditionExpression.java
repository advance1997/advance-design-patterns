package com.advance.patterns.comprehensive.ecommerce.v2.expression;

import com.advance.patterns.comprehensive.ecommerce.v2.domain.OrderContext;
import java.math.BigDecimal;

/**
 * AST 叶子节点：解释一个固定名称的订单条件。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class ConditionExpression implements Expression {
    /** 支持的原子条件名称。 */
    private final String condition;

    /**
     * 创建受支持的规则条件叶子。
     *
     * @param condition 条件标识：vip、firstOrder、amount500 或 electronics
     * @throws IllegalArgumentException 条件标识未知时抛出
     */
    public ConditionExpression(String condition) {
        if (!"vip".equals(condition) && !"firstOrder".equals(condition)
                && !"amount500".equals(condition) && !"electronics".equals(condition)) {
            throw new IllegalArgumentException("不支持的规则条件：" + condition);
        }
        this.condition = condition;
    }

    /**
     * 根据订单上下文解释该原子条件。
     *
     * @param context 当前订单和用户状态
     * @return 条件成立时返回 true
     */
    @Override
    public boolean interpret(OrderContext context) {
        boolean result;
        if ("vip".equals(condition)) {
            result = context.isVip();
        } else if ("firstOrder".equals(condition)) {
            result = context.isFirstOrder();
        } else if ("amount500".equals(condition)) {
            result = context.getAmount().compareTo(new BigDecimal("500")) >= 0;
        } else {
            result = context.hasElectronics();
        }
        System.out.println("[优惠引擎/v2] ConditionExpression.interpret ---> 条件 "
                + condition + " = " + result + "（订单" + context.getOrderId() + "）");
        return result;
    }

    /**
     * 返回叶子条件名称。
     *
     * @return DSL 条件标识
     */
    @Override
    public String describe() {
        return condition;
    }
}
