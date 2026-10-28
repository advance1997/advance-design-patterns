package com.advance.patterns.structural.bridge.v3.bridge;

import com.advance.patterns.structural.bridge.v3.domain.MaterialPackage;

/**
 * 仲裁材料包抽象，委托输出渠道完成具体输出。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class ArbitrationMaterialPackage extends MaterialPackageAbstraction {
    /**
     * 创建仲裁材料包。
     *
     * @param channel 输出渠道实现
     */
    public ArbitrationMaterialPackage(OutputChannel channel) { super(channel); }

    @Override
    public String export(MaterialPackage pkg) {
        System.out.println("[桥接/v3] ArbitrationMaterialPackage.export ---> 组织仲裁材料包内容");
        return channel.send("仲裁材料包，案件=" + pkg.getCaseId());
    }
}
