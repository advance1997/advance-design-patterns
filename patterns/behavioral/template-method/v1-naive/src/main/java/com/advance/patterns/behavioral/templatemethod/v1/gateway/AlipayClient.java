package com.advance.patterns.behavioral.templatemethod.v1.gateway;

import java.math.BigDecimal;

@FunctionalInterface
public interface AlipayClient {
    Response pay(String paymentNo, BigDecimal amount);
    record Response(String code, String tradeNo) {}
}
