package com.advance.patterns.behavioral.templatemethod.v3.gateway;

import java.math.BigDecimal;

@FunctionalInterface
public interface AlipayClient {
    Response pay(String paymentNo, BigDecimal amount);
    record Response(String code, String tradeNo) {}
}
