package com.advance.patterns.structural.adapter.v3.external;

/**
 * 模拟不可修改的 Beta SDK：使用数值业务编号和材料类型获取文件；不发起网络请求。
 *
 * @author advance
 * @since 2026-10-28 16:08:56
 */
public final class BetaMaterialClient {

    /**
     * 查询模拟文件；仅业务编号 10001 且材料类型为 10 时存在合同。
     *
     * @param businessId 平台业务编号，教学场景与内部案件编号一致
     * @param documentType 平台材料编码，10 代表借款合同
     * @return Beta 专有响应
     * @throws IllegalArgumentException 材料编码不是 10 时抛出
     */
    public BetaResponse queryFile(long businessId, int documentType) {
        System.out.println("[适配器/v3] BetaMaterialClient.queryFile ---> 接收平台参数 businessId="
                + businessId + "，documentType=" + documentType);
        if (documentType != 10) {
            throw new IllegalArgumentException("documentType 仅支持教学合同编码 10");
        }
        if (businessId == 10001L) {
            return new BetaResponse(true, "beta-contract-10001");
        }
        return new BetaResponse(false, "");
    }
}
