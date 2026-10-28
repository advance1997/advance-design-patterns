package com.advance.patterns.structural.bridge.v1;

import com.advance.patterns.structural.bridge.v1.domain.MaterialPackage;
import com.advance.patterns.structural.bridge.v1.service.PackageService;
import org.junit.jupiter.api.Test;

/**
 * 演示材料包内容由同一服务分支选择不同输出渠道。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
class BridgeTest {
    /**
     * 输入同一案件诉讼材料包，分别选择内部运营和外部机构输出。
     * 期望：两次调用均由 PackageService 的渠道分支选择输出类。
     */
    @Test
    void exportOnePackageThroughTwoChannels() {
        PackageService service = new PackageService();
        MaterialPackage packageData = new MaterialPackage("DEMO-001", "诉讼");

        System.out.println("期望：诉讼材料内容分别进入内部和外部渠道");
        System.out.println("内部结果：" + service.export(packageData, "INTERNAL"));
        System.out.println("外部结果：" + service.export(packageData, "EXTERNAL"));
    }
}
