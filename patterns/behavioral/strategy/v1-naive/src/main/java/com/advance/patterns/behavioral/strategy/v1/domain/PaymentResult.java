package com.advance.patterns.behavioral.strategy.v1.domain;

/**
 * 统一支付结果；仅在模拟渠道明确成功且交易号有效时创建。
 *
 * @author advance
 * @since 2026-10-27 18:35:37
 */
public final class PaymentResult {
    /** 成功渠道。 */
    private final PaymentChannel channel;
    /** 渠道交易流水号。 */
    private final String tradeNo;

    /**
     * 创建成功结果，避免将空交易号当作支付成功。
     * @param channel 成功渠道
     * @param tradeNo 非空渠道交易号
     * @throws IllegalArgumentException 渠道为空
     * @throws PaymentException 交易号为空
     */
    public PaymentResult(PaymentChannel channel, String tradeNo) {
        if (channel == null) {
            throw new IllegalArgumentException("channel 不能为空");
        }
        if (tradeNo == null || tradeNo.trim().isEmpty()) {
            throw new PaymentException("渠道 " + channel + " 返回的交易号为空，不能确认支付成功");
        }
        this.channel = channel;
        this.tradeNo = tradeNo;
    }

    /**
     * 获取成功渠道。
     * @return 成功渠道
     */
    public PaymentChannel getChannel() {
        return channel;
    }

    /**
     * 获取交易号。
     * @return 渠道交易流水号
     */
    public String getTradeNo() {
        return tradeNo;
    }

    /**
     * 返回便于人工对照的结果摘要。
     * @return 渠道和交易号
     */
    @Override
    public String toString() {
        return "PaymentResult{channel=" + channel + ", tradeNo=" + tradeNo + "}";
    }
}
