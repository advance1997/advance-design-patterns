package com.advance.patterns.behavioral.templatemethod.v2.domain;

/** 仅表示演示网关返回的同步确认结果，不表示真实支付平台的异步受理状态。 */
public record PaymentResult(String paymentNo, String channel, String tradeNo) {
}
