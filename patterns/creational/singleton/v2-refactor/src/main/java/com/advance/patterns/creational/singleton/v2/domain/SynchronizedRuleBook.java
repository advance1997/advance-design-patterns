package com.advance.patterns.creational.singleton.v2.domain;

/**
 * 同步懒加载规则对象：用方法锁保护初始化，移除单次案件可变状态。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class SynchronizedRuleBook {
    /** 只通过同步方法读写的静态实例。 */
    private static SynchronizedRuleBook instance;
    /** 教学批次上限，不代表真实金融或监管规则。 */
    private final int maxBatchSize = 100;

    /**
     * 构造不可变教学规则。
     */
    private SynchronizedRuleBook() {
        System.out.println("[singleton/v2] SynchronizedRuleBook ---> 首次获取时创建");
    }

    /**
     * 以类监视器保护初始化，每次读取仍需要获取锁。
     * @return 同一类加载器中的共享实例
     */
    public static synchronized SynchronizedRuleBook getInstance() {
        if (instance == null) {
            instance = new SynchronizedRuleBook();
        }
        return instance;
    }

    /**
     * 获取教学批次限制，不读取或写入案件状态。
     * @return 100
     */
    public int getMaxBatchSize() {
        return maxBatchSize;
    }
}
