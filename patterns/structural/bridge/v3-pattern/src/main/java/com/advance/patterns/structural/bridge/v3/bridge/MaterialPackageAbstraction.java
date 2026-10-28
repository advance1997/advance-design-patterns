package com.advance.patterns.structural.bridge.v3.bridge;

import com.advance.patterns.structural.bridge.v3.domain.MaterialPackage;

/**
 * 材料包抽象维度，持有输出实现但不继承具体渠道。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
public abstract class MaterialPackageAbstraction {
    protected final OutputChannel channel;
    protected MaterialPackageAbstraction(OutputChannel channel) {
        this.channel = channel;
    }

    /**
     * 生成材料包内容并交给实现维度的输出渠道。
     *
     * @param pkg 当前案件材料包数据
     * @return 模拟渠道返回的输出结果
     */
    public abstract String export(MaterialPackage pkg);
}
