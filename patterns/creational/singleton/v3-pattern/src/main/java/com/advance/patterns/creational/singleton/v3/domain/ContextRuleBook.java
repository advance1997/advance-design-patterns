package com.advance.patterns.creational.singleton.v3.domain;

/**
 * 普通规则类；本身不是 GoF 单例，由 Spring 的 Bean 定义决定作用域。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class ContextRuleBook {
    /**
     * 可被不同容器各自创建。
     */
    public ContextRuleBook() {
        System.out.println("[singleton/v3] ContextRuleBook ---> 当前 Spring 上下文创建规则 Bean");
    }
}
