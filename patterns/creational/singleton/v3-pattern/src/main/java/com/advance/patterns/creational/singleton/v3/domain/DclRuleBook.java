package com.advance.patterns.creational.singleton.v3.domain;

/**
 * 双重检查锁教学对照：volatile 负责可见性和安全发布，锁负责首次唯一构造。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class DclRuleBook {
    /** volatile 不能省略，避免观察到未正确发布的对象。 */
    private static volatile DclRuleBook instance;

    /**
     * 创建无单次请求状态的对象。
     */
    private DclRuleBook() {
        System.out.println("[singleton/v3] DclRuleBook ---> 双重检查锁首次创建");
    }

    /**
     * 已初始化时快速返回，首次构造在类监视器内再次检查。
     * @return 当前类加载器内的共享实例
     */
    public static DclRuleBook getInstance() {
        if (instance == null) {
            synchronized (DclRuleBook.class) {
                if (instance == null) {
                    instance = new DclRuleBook();
                }
            }
        }
        return instance;
    }
}
