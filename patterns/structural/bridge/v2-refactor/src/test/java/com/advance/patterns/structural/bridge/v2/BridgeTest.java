package com.advance.patterns.structural.bridge.v2;

import com.advance.patterns.structural.bridge.v2.domain.MaterialPackage;
import com.advance.patterns.structural.bridge.v2.service.PackageService;
import org.junit.jupiter.api.Test;

/**
 * 演示抽取数据对象后，输出渠道分支仍集中在服务中。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
class BridgeTest {
    /**
     * 输入同一案件诉讼材料包，分别选择内部运营和外部机构输出。
     * 期望：提取领域对象没有消除 PackageService 中的渠道分支。
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
