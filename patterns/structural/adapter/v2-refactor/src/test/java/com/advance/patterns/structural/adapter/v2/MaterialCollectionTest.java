package com.advance.patterns.structural.adapter.v2;

import com.advance.patterns.structural.adapter.v2.service.MaterialCollectionService;
import com.advance.patterns.structural.adapter.v2.external.AlphaMaterialClient;
import com.advance.patterns.structural.adapter.v2.external.BetaMaterialClient;
import com.advance.patterns.structural.adapter.v2.reader.AlphaMaterialReader;
import com.advance.patterns.structural.adapter.v2.reader.BetaMaterialReader;
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
     * 路径：归集服务 → 专有 Reader → 平台客户端，Reader 转换。
     */
    @Test
    void collectFromAlpha() {
        MaterialCollectionService service = new MaterialCollectionService(
                new AlphaMaterialReader(new AlphaMaterialClient()),
                new BetaMaterialReader(new BetaMaterialClient()));
        System.out.println("实际材料：" + service.collectFromAlpha(10001L));
    }

    /**
     * 输入 caseId=10001。
     * 期望：Beta 收到 businessId=10001、documentType=10，exists=true 转为 found=true。
     * 返回 caseId=10001、fileKey=beta-contract-10001。
     * 路径：归集服务 → 专有 Reader → 平台客户端，Reader 转换。
     */
    @Test
    void collectFromBeta() {
        MaterialCollectionService service = new MaterialCollectionService(
                new AlphaMaterialReader(new AlphaMaterialClient()),
                new BetaMaterialReader(new BetaMaterialClient()));
        System.out.println("实际材料：" + service.collectFromBeta(10001L));
    }
}
