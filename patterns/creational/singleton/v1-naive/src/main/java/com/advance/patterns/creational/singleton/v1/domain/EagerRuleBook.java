package com.advance.patterns.creational.singleton.v1.domain;

/**
 * 饿汉式不可变规则示例：利用类初始化保证发布安全，但在类初始化时立即创建。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class EagerRuleBook {
    /** 当前类加载器下的唯一静态实例。 */
    private static final EagerRuleBook INSTANCE = new EagerRuleBook();

    /**
     * 创建无单次请求状态的教学规则对象。
     */
    private EagerRuleBook() {
        System.out.println("[singleton/v1] EagerRuleBook ---> 类初始化时创建");
    }

    /**
     * 获取已初始化实例。
     * @return 共享不可变实例
     */
    public static EagerRuleBook getInstance() {
        return INSTANCE;
    }
}
