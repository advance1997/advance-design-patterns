package com.advance.patterns.structural.bridge.v3;

import com.advance.patterns.structural.bridge.v3.domain.MaterialPackage;
import com.advance.patterns.structural.bridge.v3.bridge.MaterialPackageAbstraction;
import com.advance.patterns.structural.bridge.v3.bridge.StandardMaterialPackage;
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
     * 输入同一案件诉讼材料包，分别选择内部运营和外部机构输出。
     * 期望：v两个 StandardMaterialPackage 复用同一抽象结构，分别桥接到两个 OutputChannel。
     */
    @Test void independentDimensions(){MaterialPackage pkg=new MaterialPackage("DEMO-001","诉讼");
OutputChannel internal=new InternalChannelBridge();
OutputChannel external=new ExternalAgencyChannelBridge();
MaterialPackageAbstraction first=new StandardMaterialPackage(internal);
MaterialPackageAbstraction second=new StandardMaterialPackage(external);
System.out.println("内部结果："+first.export(pkg));
System.out.println("外部结果："+second.export(pkg));}
}
