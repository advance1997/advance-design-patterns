package com.advance.patterns.structural.adapter.v1.service;

import com.advance.patterns.structural.adapter.v1.domain.MaterialFile;
import com.advance.patterns.structural.adapter.v1.external.AlphaMaterialClient;
import com.advance.patterns.structural.adapter.v1.external.AlphaResponse;
import com.advance.patterns.structural.adapter.v1.external.BetaMaterialClient;
import com.advance.patterns.structural.adapter.v1.external.BetaResponse;

/**
 * 归集服务直接依赖两个外部 SDK，参数拼接、状态判断和字段转换散落在业务入口。
 *
 * @author advance
 * @since 2026-10-28 16:08:56
 */
public class MaterialCollectionService {

    /** Alpha 平台模拟客户端。 */
    private final AlphaMaterialClient alpha = new AlphaMaterialClient();

    /** Beta 平台模拟客户端。 */
    private final BetaMaterialClient beta = new BetaMaterialClient();

    /**
     * 直接从 Alpha 获取合同并在业务层转换。
     *
     * @param caseId 内部虚构案件编号
     * @return 统一合同材料结果
     * @throws IllegalStateException Alpha 返回未知状态码时抛出
     */
    public MaterialFile collectFromAlpha(long caseId) {
        System.out.println("[适配器/v1] MaterialCollectionService.collectFromAlpha ---> 将 caseId 转为 Alpha 的 loanNo");
        AlphaResponse response = alpha.fetchContract("LOAN-" + caseId);
        System.out.println("[适配器/v1] MaterialCollectionService.collectFromAlpha"
                + " ---> 将 code、fileToken 转为 found、fileKey");
        if ("0000".equals(response.getCode())) {
            return new MaterialFile(caseId, true, response.getFileToken());
        }
        if ("4040".equals(response.getCode())) {
            return new MaterialFile(caseId, false, "");
        }
        throw new IllegalStateException("Alpha 返回未知 code，不能当作材料未找到");
    }

    /**
     * 直接从 Beta 获取合同并在业务层转换。
     *
     * @param caseId 内部虚构案件编号
     * @return 统一合同材料结果
     */
    public MaterialFile collectFromBeta(long caseId) {
        System.out.println("[适配器/v1] MaterialCollectionService.collectFromBeta ---> 将 caseId 转为 businessId，合同类型转为 10");
        BetaResponse response = beta.queryFile(caseId, 10);
        System.out.println("[适配器/v1] MaterialCollectionService.collectFromBeta"
                + " ---> 将 exists、objectKey 转为 found、fileKey");
        return new MaterialFile(caseId, response.isExists(), response.isExists() ? response.getObjectKey() : "");
    }
}
