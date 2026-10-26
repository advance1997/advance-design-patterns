package com.advance.patterns.behavioral.templatemethod.v2.gateway;

/**
 * 微信支付教学网关接口，按分金额调用模拟交易能力。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
@FunctionalInterface
public interface WechatPayClient {
    /**
     * 执行 createTransaction 对应的业务步骤。
     *
     * @param paymentNo 支付单号
     * @param amountInCents 以分计价的支付金额
     * @return 当前步骤的处理结果
     */
    Response createTransaction(String paymentNo, long amountInCents);
    /**
     * 渠道响应数据，保留渠道协议字段供业务层转换。
     *
     * @param success 渠道是否确认成功
     * @param transactionId 微信交易流水号
     * @author advance
     * @since 2026-10-26 23:05:19
     */
    record Response(boolean success, String transactionId) {}
}
