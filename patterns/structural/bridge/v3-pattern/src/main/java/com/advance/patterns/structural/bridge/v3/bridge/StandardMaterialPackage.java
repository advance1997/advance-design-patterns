package com.advance.patterns.structural.bridge.v3.bridge;

import com.advance.patterns.structural.bridge.v3.domain.MaterialPackage;

/**
 * 标准材料包抽象实现，按材料场景生成统一内容。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
public class StandardMaterialPackage extends MaterialPackageAbstraction {
    public StandardMaterialPackage(OutputChannel channel){super(channel);}
    @Override public String export(MaterialPackage pkg){System.out.println("[桥接/v3] StandardMaterialPackage.export ---> 材料抽象调用输出实现");return channel.send(pkg.getScene()+"材料包，案件="+pkg.getCaseId());}
}
