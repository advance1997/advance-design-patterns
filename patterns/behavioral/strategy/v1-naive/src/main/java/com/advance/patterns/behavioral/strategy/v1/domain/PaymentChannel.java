package com.advance.patterns.behavioral.strategy.v1.domain;

/**
 * 支付渠道标识；枚举限定可选渠道，但不承担策略创建或执行职责。
 *
 * @author advance
 * @since 2026-10-27 18:35:37
 */
public enum PaymentChannel {
    /** 支付宝渠道。 */
    ALIPAY,
    /** 微信支付渠道。 */
    WECHAT_PAY,
    /** 银联渠道，用于观察未注册渠道和后续扩展。 */
    UNION_PAY
}
