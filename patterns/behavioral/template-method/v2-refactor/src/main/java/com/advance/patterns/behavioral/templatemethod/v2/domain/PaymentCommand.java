package com.advance.patterns.behavioral.templatemethod.v2.domain;

import java.math.BigDecimal;

/**
 * 支付命令，保存支付单号和以元计价的金额。
 *
 * @param paymentNo 支付单号
 * @param amount 以元计价的支付金额
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public record PaymentCommand(String paymentNo, BigDecimal amount) {
    /**
     * 校验业务输入；参数不合法时终止后续处理。
     */
    public void validate() {
        System.out.println("[template-method/v2] PaymentCommand ---> 开始校验请求参数");
        if (paymentNo == null || paymentNo.isBlank()) {
            System.out.println("[template-method/v2] PaymentCommand ---> 参数校验失败");
            throw new IllegalArgumentException("支付单号不能为空");
        }
        if (amount == null || amount.signum() <= 0 || amount.stripTrailingZeros().scale() > 2) {
            System.out.println("[template-method/v2] PaymentCommand ---> 参数校验失败");
            throw new IllegalArgumentException("金额必须为正数且最多两位小数");
        }
    }
}
