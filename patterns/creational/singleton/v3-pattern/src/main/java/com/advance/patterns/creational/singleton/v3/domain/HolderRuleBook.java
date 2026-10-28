package com.advance.patterns.creational.singleton.v3.domain;

/**
 * 静态内部类懒加载：依赖 JVM 类初始化，不保存单次案件状态。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class HolderRuleBook {
    /** 教学批次限制。 */
    private final int maxBatchSize = 100;

    /**
     * 创建不可变规则。
     */
    private HolderRuleBook() {
        System.out.println("[singleton/v3] HolderRuleBook ---> Holder 首次初始化时创建");
    }

    /**
     * 获取延迟初始化的规则。
     * @return 当前类加载器内的共享实例
     */
    public static HolderRuleBook getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 读取教学规则。
     * @return 100
     */
    public int getMaxBatchSize() {
        return maxBatchSize;
    }

    /**
     * 只有被主动使用时才初始化的持有类。
     *
     * @author advance
     * @since 2026-10-27 20:33:03
     */
    private static final class Holder {
        /** 依靠类初始化安全发布的实例。 */
        private static final HolderRuleBook INSTANCE = new HolderRuleBook();

        /**
         * 禁止实例化持有工具类。
         */
        private Holder() {
        }
    }
}
