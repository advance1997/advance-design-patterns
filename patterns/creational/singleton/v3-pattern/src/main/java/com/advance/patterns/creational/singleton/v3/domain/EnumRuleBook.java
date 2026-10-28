package com.advance.patterns.creational.singleton.v3.domain;

/**
 * 枚举单例：由 JVM 管理实例，标准反序列化不会创建新的枚举对象。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public enum EnumRuleBook {
    /** 无单次案件状态的教学规则入口。 */
    INSTANCE;

    /**
     * 获取教学批次限制，不代表真实业务规则。
     * @return 100
     */
    public int getMaxBatchSize() {
        return 100;
    }
}
