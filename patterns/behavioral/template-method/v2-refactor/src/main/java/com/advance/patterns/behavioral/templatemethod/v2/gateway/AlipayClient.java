package com.advance.patterns.behavioral.templatemethod.v2.gateway;

import java.math.BigDecimal;

/**
 * 支付宝教学网关接口，按元金额调用模拟支付能力。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
@FunctionalInterface
public interface AlipayClient {
    /**
     * 执行支付流程，渠道或步骤异常向调用方传播。
     *
     * @param paymentNo 支付单号
     * @param amount 以元计价的支付金额
     * @return 当前步骤的处理结果
     */
    Response pay(String paymentNo, BigDecimal amount);
    /**
     * 渠道响应数据，保留渠道协议字段供业务层转换。
     *
     * @param code 渠道响应状态码
     * @param tradeNo 渠道交易流水号
     * @author advance
     * @since 2026-10-26 23:05:19
     */
    record Response(String code, String tradeNo) {}
}
