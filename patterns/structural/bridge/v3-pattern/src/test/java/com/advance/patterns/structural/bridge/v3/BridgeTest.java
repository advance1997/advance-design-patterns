package com.advance.patterns.structural.bridge.v3;

import com.advance.patterns.structural.bridge.v3.domain.MaterialPackage;
import com.advance.patterns.structural.bridge.v3.bridge.ArbitrationMaterialPackage;
import com.advance.patterns.structural.bridge.v3.bridge.LitigationMaterialPackage;
import com.advance.patterns.structural.bridge.v3.bridge.MaterialPackageAbstraction;
import com.advance.patterns.structural.bridge.v3.bridge.OutputChannel;
import com.advance.patterns.structural.bridge.v3.channel.InternalChannelBridge;
import com.advance.patterns.structural.bridge.v3.channel.ExternalAgencyChannelBridge;
import org.junit.jupiter.api.Test;

/**
 * 只观察材料抽象维度与输出实现维度是否能够独立替换。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
class BridgeTest {
    /**
     * 分别组合诉讼、仲裁材料包与内部、外部输出渠道。
     * 期望：两个材料包抽象可以独立选择两个渠道实现，形成四种组合。
     */
    @Test
    void combinePackageTypesWithOutputChannels() {
        MaterialPackage packageData = new MaterialPackage("DEMO-001");
        OutputChannel internalChannel = new InternalChannelBridge();
        OutputChannel externalChannel = new ExternalAgencyChannelBridge();
        MaterialPackageAbstraction litigationPackage = new LitigationMaterialPackage(internalChannel);
        MaterialPackageAbstraction arbitrationPackage = new ArbitrationMaterialPackage(internalChannel);

        System.out.println("期望：诉讼和仲裁材料包都可选择任一输出渠道");
        System.out.println("诉讼 / 内部：" + litigationPackage.export(packageData));
        System.out.println("诉讼 / 外部：" + new LitigationMaterialPackage(externalChannel).export(packageData));
        System.out.println("仲裁 / 内部：" + arbitrationPackage.export(packageData));
        System.out.println("仲裁 / 外部：" + new ArbitrationMaterialPackage(externalChannel).export(packageData));
    }
}
