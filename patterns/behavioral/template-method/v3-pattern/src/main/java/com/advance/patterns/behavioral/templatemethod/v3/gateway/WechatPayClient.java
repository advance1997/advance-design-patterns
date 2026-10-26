package com.advance.patterns.behavioral.templatemethod.v3.gateway;

@FunctionalInterface
public interface WechatPayClient {
    Response createTransaction(String paymentNo, long amountInCents);
    record Response(boolean success, String transactionId) {}
}
