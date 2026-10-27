package com.advance.patterns.creational.abstractfactory.v3.domain;

import java.math.BigDecimal;

/**
 * 不可变的模拟支付凭证，保存退款需要核对的渠道、交易号和原支付金额。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public final class PaymentReceipt {
    /** 原支付所属渠道。 */
    private final PaymentChannel channel;
    /** 模拟渠道交易号，不能作为真实支付成功依据。 */
    private final String tradeNo;
    /** 原支付金额，单位元。 */
    private final BigDecimal paidAmount;

    /**
     * 保存已完成的模拟支付结果。
     * @param channel 原支付渠道
     * @param tradeNo 非空渠道交易号
     * @param paidAmount 正数元金额，最多两位有效小数
     * @throws IllegalArgumentException 任一字段不符合约束
     */
    public PaymentReceipt(PaymentChannel channel, String tradeNo, BigDecimal paidAmount) {
        if (channel == null) {
            throw new IllegalArgumentException("channel 不能为空");
        }
        if (tradeNo == null || tradeNo.trim().isEmpty()) {
            throw new IllegalArgumentException("tradeNo 不能为空");
        }
        PaymentValidation.validateAmount(paidAmount);
        this.channel = channel;
        this.tradeNo = tradeNo;
        this.paidAmount = paidAmount;
    }

    /**
     * 获取原支付渠道。
     * @return 原支付渠道
     */
    public PaymentChannel getChannel() {
        return channel;
    }

    /**
     * 获取模拟交易号。
     * @return 非空交易号
     */
    public String getTradeNo() {
        return tradeNo;
    }

    /**
     * 获取原支付金额。
     * @return 元金额
     */
    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    /**
     * 输出便于教学对照的凭证摘要。
     * @return 渠道、模拟交易号和元金额
     */
    @Override
    public String toString() {
        return "PaymentReceipt{channel=" + channel + ", tradeNo=" + tradeNo
                + ", paidAmount=" + paidAmount + "}";
    }
}
