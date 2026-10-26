package com.advance.patterns.behavioral.templatemethod.v2.domain;

import java.math.BigDecimal;

/** 金额单位为元；校验留在流程入口，以展示稳定的前置步骤。 */
public record PaymentCommand(String paymentNo, BigDecimal amount) {
    public void validate() {
        System.out.println("[template-method/v2] PaymentCommand ---> validate");
        if (paymentNo == null || paymentNo.isBlank()) {
            System.out.println("[template-method/v2] PaymentCommand ---> validation failed");
            throw new IllegalArgumentException("支付单号不能为空");
        }
        if (amount == null || amount.signum() <= 0 || amount.stripTrailingZeros().scale() > 2) {
            System.out.println("[template-method/v2] PaymentCommand ---> validation failed");
            throw new IllegalArgumentException("金额必须为正数且最多两位小数");
        }
    }
}
