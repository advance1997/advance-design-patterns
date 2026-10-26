package com.advance.patterns.behavioral.templatemethod.v2.domain;

/**
 * 统一支付结果，仅表示教学网关的同步确认结果。
 *
 * @param paymentNo 支付单号
 * @param channel 支付渠道标识
 * @param tradeNo 渠道交易流水号
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public record PaymentResult(String paymentNo, String channel, String tradeNo) {
}
