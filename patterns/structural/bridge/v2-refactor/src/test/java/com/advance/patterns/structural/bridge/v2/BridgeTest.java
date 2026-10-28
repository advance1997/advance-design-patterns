package com.advance.patterns.structural.bridge.v2;

import com.advance.patterns.structural.bridge.v2.domain.MaterialPackage;
import com.advance.patterns.structural.bridge.v2.service.PackageService;
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
     * 期望：v服务内部通过分支组合两个维度；
     */
    @Test void independentDimensions(){PackageService service=new PackageService();
System.out.println("内部结果："+service.export(new MaterialPackage("DEMO-001","诉讼"),"INTERNAL"));
System.out.println("外部结果："+service.export(new MaterialPackage("DEMO-001","诉讼"),"EXTERNAL"));}
}
