package com.advance.patterns.behavioral.templatemethod.v2.domain;

/**
 * 不可变同步确认结果，不代表真实平台的异步受理状态。
 *
 * @author advance
 * @since 2026-10-26 23:26:13
 */
public final class PaymentResult {
    /** 支付单号。 */
    private final String paymentNo;

    /** 支付渠道。 */
    private final String channel;

    /** 渠道交易流水号。 */
    private final String tradeNo;

    /**
     * 创建 PaymentResult，保存本次业务数据。
     *
     * @param paymentNo 支付单号
     * @param channel 支付渠道
     * @param tradeNo 渠道交易流水号
     */
    public PaymentResult(String paymentNo, String channel, String tradeNo) {
        this.paymentNo = paymentNo;
        this.channel = channel;
        this.tradeNo = tradeNo;
    }

    /**
     * 获取支付单号。
     *
     * @return 支付单号
     */
    public String getPaymentNo() {
        return paymentNo;
    }

    /**
     * 获取支付渠道。
     *
     * @return 支付渠道
     */
    public String getChannel() {
        return channel;
    }

    /**
     * 获取渠道交易流水号。
     *
     * @return 渠道交易流水号
     */
    public String getTradeNo() {
        return tradeNo;
    }

    /**
     * 按字段值比较，保持数据对象的值语义。
     * @param other 待比较对象
     * @return 全部字段相等时返回 true
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        PaymentResult that = (PaymentResult) other;
        return java.util.Objects.equals(paymentNo, that.paymentNo)
                && java.util.Objects.equals(channel, that.channel)
                && java.util.Objects.equals(tradeNo, that.tradeNo);
    }

    /**
     * 计算与 equals 一致的哈希值。
     * @return 字段哈希值
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(paymentNo, channel, tradeNo);
    }

    /**
     * 生成便于教学观察的数据摘要。
     * @return 包含字段名称和值的文本
     */
    @Override
    public String toString() {
        return "PaymentResult{" + "paymentNo=" + paymentNo + ", channel=" + channel + ", tradeNo=" + tradeNo + "}";
    }

}
