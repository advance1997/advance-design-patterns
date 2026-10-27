package com.advance.patterns.creational.abstractfactory.v1.domain;

/**
 * 产品族标识；同一笔支付及其退款必须使用相同渠道的客户端。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public enum PaymentChannel {
    /** 支付宝产品族。 */
    ALIPAY,
    /** 微信支付产品族。 */
    WECHAT_PAY,
    /** 预留银联标识，用于观察不支持渠道的错误路径。 */
    UNION_PAY
}
