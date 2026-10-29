package com.advance.patterns.comprehensive.ecommerce.v2.expression;

import com.advance.patterns.comprehensive.ecommerce.v2.domain.OrderContext;

/**
 * AST 表达式统一协议：组合节点和条件叶子均可递归解释。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public interface Expression {
    /**
     * 使用本次订单上下文计算表达式布尔值。
     *
     * @param context 当前订单和用户状态
     * @return 表达式成立时返回 true
     */
    boolean interpret(OrderContext context);

    /**
     * 返回该节点代表的规则片段，用于执行路径打印。
     *
     * @return 规则片段描述
     */
    String describe();
}
