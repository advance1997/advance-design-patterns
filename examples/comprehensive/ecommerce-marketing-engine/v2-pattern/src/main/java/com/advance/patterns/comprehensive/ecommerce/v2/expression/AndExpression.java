package com.advance.patterns.comprehensive.ecommerce.v2.expression;

import com.advance.patterns.comprehensive.ecommerce.v2.domain.OrderContext;

/**
 * AST 组合节点：只有左右子表达式都成立时 AND 才成立。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class AndExpression implements Expression {
    /** 左侧表达式，可以是叶子或其他组合节点。 */
    private final Expression left;
    /** 右侧表达式，可以是叶子或其他组合节点。 */
    private final Expression right;

    /**
     * 创建不可变 AND 组合节点。
     *
     * @param left 左侧子表达式
     * @param right 右侧子表达式
     */
    public AndExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * 以短路方式递归解释左右表达式。
     *
     * @param context 本次订单上下文
     * @return 左右节点都成立时返回 true
     */
    @Override
    public boolean interpret(OrderContext context) {
        boolean leftResult = left.interpret(context);
        boolean result = leftResult && right.interpret(context);
        System.out.println("[优惠引擎/v2] AndExpression.interpret ---> "
                + describe() + " = " + result);
        return result;
    }

    /**
     * 返回组合规则文本。
     *
     * @return AND 表达式描述
     */
    @Override
    public String describe() {
        return "(" + left.describe() + " && " + right.describe() + ")";
    }
}
