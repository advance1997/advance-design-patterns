package com.advance.patterns.structural.adapter.v3.external;

/**
 * 模拟不可修改的 Alpha SDK：使用字符串借据编号获取合同；不发起网络请求。
 *
 * @author advance
 * @since 2026-10-28 16:08:56
 */
public final class AlphaMaterialClient {

    /**
     * 查询模拟合同；仅 LOAN-10001 有材料，其他编号返回未找到。
     *
     * @param loanNo 平台要求的借据编号，教学约定为 LOAN- 加案件编号
     * @return Alpha 专有响应
     */
    public AlphaResponse fetchContract(String loanNo) {
        System.out.println("[适配器/v3] AlphaMaterialClient.fetchContract ---> 接收平台参数 loanNo=" + loanNo);
        if ("LOAN-10001".equals(loanNo)) {
            return new AlphaResponse("0000", "alpha-contract-10001");
        }
        return new AlphaResponse("4040", "");
    }
}
