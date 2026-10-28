package com.advance.patterns.structural.adapter.v4.adapter;

import com.advance.patterns.structural.adapter.v4.domain.MaterialFile;
import com.advance.patterns.structural.adapter.v4.external.BetaMaterialClient;
import com.advance.patterns.structural.adapter.v4.external.BetaResponse;
import com.advance.patterns.structural.adapter.v4.port.MaterialGateway;

/**
 * 将 Beta 的专有 API 适配为内部 MaterialGateway，负责参数、状态和结果字段转换。
 *
 * @author advance
 * @since 2026-10-28 16:08:56
 */
public class BetaMaterialAdapter implements MaterialGateway {

    /** 被适配的外部平台客户端，通过组合使用，不修改其接口。 */
    private final BetaMaterialClient client;

    /**
     * 注入被适配的平台客户端。
     *
     * @param client Beta 模拟 SDK
     */
    public BetaMaterialAdapter(BetaMaterialClient client) {
        this.client = client;
    }

    /**
     * 将内部合同查询转换为 Beta 调用，再转回统一材料结果。
     *
     * @param caseId 内部虚构案件编号
     * @return 平台无关的合同材料结果
     */
    @Override
    public MaterialFile fetchContract(long caseId) {
        System.out.println("[适配器/v4] BetaMaterialAdapter.fetchContract ---> 将 caseId 转为 businessId，合同类型转为 10");
        BetaResponse response = client.queryFile(caseId, 10);
        System.out.println("[适配器/v4] BetaMaterialAdapter.fetchContract ---> 将 exists、objectKey 转为 found、fileKey");
        return new MaterialFile(caseId, response.isExists(), response.isExists() ? response.getObjectKey() : "");
    }
}
