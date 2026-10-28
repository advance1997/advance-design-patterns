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
    protected MaterialPackageAbstraction(OutputChannel channel){this.channel=channel;}
    public abstract String export(MaterialPackage pkg);
}
