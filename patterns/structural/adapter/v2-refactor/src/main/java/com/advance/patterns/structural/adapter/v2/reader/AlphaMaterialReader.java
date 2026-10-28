package com.advance.patterns.structural.adapter.v2.reader;

import com.advance.patterns.structural.adapter.v2.domain.MaterialFile;
import com.advance.patterns.structural.adapter.v2.external.AlphaMaterialClient;
import com.advance.patterns.structural.adapter.v2.external.AlphaResponse;

/**
 * Alpha 专有转换类；已隔离外部字段，但尚未提供共同调用契约。
 *
 * @author advance
 * @since 2026-10-28 16:08:56
 */
public class AlphaMaterialReader {

    /** 被适配的外部平台客户端，通过组合使用，不修改其接口。 */
    private final AlphaMaterialClient client;

    /**
     * 注入被适配的平台客户端。
     *
     * @param client Alpha 模拟 SDK
     */
    public AlphaMaterialReader(AlphaMaterialClient client) {
        this.client = client;
    }

    /**
     * 将内部合同查询转换为 Alpha 调用，再转回统一材料结果。
     *
     * @param caseId 内部虚构案件编号
     * @return 平台无关的合同材料结果
     * @throws IllegalStateException 平台返回未知状态码时抛出
     */
    public MaterialFile readContract(long caseId) {
        System.out.println("[适配器/v2] AlphaMaterialReader.readContract ---> 将 caseId 转为 Alpha 的 loanNo");
        AlphaResponse response = client.fetchContract("LOAN-" + caseId);
        System.out.println("[适配器/v2] AlphaMaterialReader.readContract ---> 将 code、fileToken 转为 found、fileKey");
        if ("0000".equals(response.getCode())) {
            return new MaterialFile(caseId, true, response.getFileToken());
        }
        if ("4040".equals(response.getCode())) {
            return new MaterialFile(caseId, false, "");
        }
        throw new IllegalStateException("Alpha 返回未知 code，不能当作材料未找到");
    }
}
