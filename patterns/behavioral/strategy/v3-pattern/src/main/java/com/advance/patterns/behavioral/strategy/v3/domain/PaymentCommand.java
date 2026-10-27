package com.advance.patterns.behavioral.strategy.v3.domain;

import java.math.BigDecimal;

/**
 * 不可变支付输入；入口调用 validate() 校验，金额统一使用元。
 *
 * @author advance
 * @since 2026-10-27 18:35:37
 */
public final class PaymentCommand {
    /** 支付单号，仅用于教学追踪。 */
    private final String paymentNo;
    /** 正数元金额，最多两位有效小数。 */
    private final BigDecimal amount;
    /** 本次请求指定的渠道，不允许隐式降级到其他渠道。 */
    private final PaymentChannel channel;

    /**
     * 保存请求数据，校验延迟到执行入口，便于演示非法输入。
     * @param paymentNo 支付单号
     * @param amount 元金额
     * @param channel 指定渠道
     */
    public PaymentCommand(String paymentNo, BigDecimal amount, PaymentChannel channel) {
        this.paymentNo = paymentNo;
        this.amount = amount;
        this.channel = channel;
    }

    /**
     * 获取支付单号。
     * @return 支付单号
     */
    public String getPaymentNo() {
        return paymentNo;
    }

    /**
     * 获取元金额。
     * @return 元金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 获取渠道。
     * @return 指定渠道
     */
    public PaymentChannel getChannel() {
        return channel;
    }

    /**
     * 校验输入，禁止空单号、空渠道、非正数金额及超过两位有效小数的金额。
     * @throws IllegalArgumentException 任一输入不满足约束
     */
    public void validate() {
        System.out.println("[strategy/v3] PaymentCommand.validate ---> 校验支付单号、元金额和渠道");
        if (paymentNo == null || paymentNo.trim().isEmpty()) {
            throw new IllegalArgumentException("paymentNo 不能为空");
        }
        if (amount == null || amount.signum() <= 0 || amount.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException("amount 必须为正数元金额且最多两位有效小数");
        }
        if (channel == null) {
            throw new IllegalArgumentException("channel 不能为空");
        }
    }
}
