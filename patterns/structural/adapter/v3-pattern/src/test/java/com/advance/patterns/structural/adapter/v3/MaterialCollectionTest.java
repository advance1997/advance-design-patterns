package com.advance.patterns.structural.adapter.v3;

import com.advance.patterns.structural.adapter.v3.service.MaterialCollectionService;
import com.advance.patterns.structural.adapter.v3.external.AlphaMaterialClient;
import com.advance.patterns.structural.adapter.v3.external.BetaMaterialClient;
import com.advance.patterns.structural.adapter.v3.adapter.AlphaMaterialAdapter;
import com.advance.patterns.structural.adapter.v3.adapter.BetaMaterialAdapter;
import org.junit.jupiter.api.Test;

/**
 * 只通过打印观察平台参数、结果字段与统一调用契约的演进。
 *
 * @author advance
 * @since 2026-10-28 16:08:56
 */
class MaterialCollectionTest {

    /**
     * 输入 caseId=10001。
     * 期望：Alpha 收到 loanNo=LOAN-10001，code=0000 转为 found=true。
     * 返回 caseId=10001、fileKey=alpha-contract-10001。
     * 路径：归集服务 → MaterialGateway 的具体适配器 → 平台客户端，适配器转换。
     */
    @Test
    void collectFromAlpha() {
        MaterialCollectionService service = new MaterialCollectionService(
                new AlphaMaterialAdapter(new AlphaMaterialClient()));
        System.out.println("实际材料：" + service.collectContract(10001L));
    }

    /**
     * 输入 caseId=10001。
     * 期望：Beta 收到 businessId=10001、documentType=10，exists=true 转为 found=true。
     * 返回 caseId=10001、fileKey=beta-contract-10001。
     * 路径：归集服务 → MaterialGateway 的具体适配器 → 平台客户端，适配器转换。
     */
    @Test
    void collectFromBeta() {
        MaterialCollectionService service = new MaterialCollectionService(
                new BetaMaterialAdapter(new BetaMaterialClient()));
        System.out.println("实际材料：" + service.collectContract(10001L));
    }

    /**
     * 两个平台均输入 caseId=10002；此教学案件没有合同。
     * 期望：Alpha 的 4040 与 Beta 的 exists=false 均转换为 found=false、fileKey 为空。
     * 两次都走同一个 collectContract 方法，由各自适配器解释平台缺失语义，不暴露专有 DTO。
     */
    @Test
    void normalizeMissingMaterial() {
        MaterialCollectionService alphaService = new MaterialCollectionService(
                new AlphaMaterialAdapter(new AlphaMaterialClient()));
        MaterialCollectionService betaService = new MaterialCollectionService(
                new BetaMaterialAdapter(new BetaMaterialClient()));
        System.out.println("Alpha 统一结果：" + alphaService.collectContract(10002L));
        System.out.println("Beta 统一结果：" + betaService.collectContract(10002L));
    }
}
