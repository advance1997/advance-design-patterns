package com.advance.patterns.comprehensive.ecommerce.v2.rule;

import com.advance.patterns.comprehensive.ecommerce.v2.expression.Expression;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 享元池：按规则文本复用不可变 AST，不把订单数据写入共享表达式对象。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class RuleAstPool {
    /** 规则文本到不可变 AST 的并发缓存。 */
    private final ConcurrentMap<String, Expression> expressions =
            new ConcurrentHashMap<String, Expression>();

    /**
     * 获取规则 AST；同一规则文本只在首次请求时解析一次。
     *
     * @param rule 原始优惠规则文本，作为精确缓存键
     * @return 对应的共享不可变 AST
     * @throws IllegalArgumentException 规则文本为空或语法不合法时抛出
     */
    public synchronized Expression get(String rule) {
        if (rule == null || rule.trim().isEmpty()) {
            throw new IllegalArgumentException("规则表达式不能为空");
        }
        Expression expression = expressions.get(rule);
        if (expression != null) {
            System.out.println("[优惠引擎/v2] RuleAstPool.get ---> 命中共享 AST：" + rule);
            return expression;
        }
        System.out.println("[优惠引擎/v2] RuleAstPool.get ---> 首次解析并缓存规则：" + rule);
        expression = new RuleParser(rule).parse();
        expressions.put(rule, expression);
        return expression;
    }

    /**
     * 获取当前缓存的规则数量。
     *
     * @return 已缓存 AST 数量
     */
    public int size() {
        return expressions.size();
    }
}
