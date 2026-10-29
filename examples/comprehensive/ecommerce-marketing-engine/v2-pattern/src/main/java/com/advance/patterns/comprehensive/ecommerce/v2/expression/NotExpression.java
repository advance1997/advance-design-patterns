package com.advance.patterns.comprehensive.ecommerce.v2.expression;

import com.advance.patterns.comprehensive.ecommerce.v2.domain.OrderContext;

/**
 * AST 一元组合节点：对一个子表达式的解释结果取反。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class NotExpression implements Expression {
    /** 被取反的子表达式。 */
    private final Expression expression;

    /**
     * 创建不可变 NOT 节点。
     *
     * @param expression 待取反表达式
     */
    public NotExpression(Expression expression) {
        this.expression = expression;
    }

    /**
     * 递归解释子节点并取反。
     *
     * @param context 当前订单上下文
     * @return 子表达式不成立时返回 true
     */
    @Override
    public boolean interpret(OrderContext context) {
        boolean result = !expression.interpret(context);
        System.out.println("[优惠引擎/v2] NotExpression.interpret ---> "
                + describe() + " = " + result);
        return result;
    }

    /**
     * 返回 NOT 规则描述。
     *
     * @return NOT 表达式文本
     */
    @Override
    public String describe() {
        return "!" + expression.describe();
    }
}
