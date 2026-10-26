package com.advance.patterns.behavioral.templatemethod.v1.gateway;

@FunctionalInterface
public interface WechatPayClient {
    Response createTransaction(String paymentNo, long amountInCents);
    record Response(boolean success, String transactionId) {}
}
