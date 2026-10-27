package com.advance.patterns.creational.abstractfactory.v1.product;

import com.advance.patterns.creational.abstractfactory.v1.domain.PaymentChannel;
import com.advance.patterns.creational.abstractfactory.v1.domain.PaymentReceipt;
import com.advance.patterns.creational.abstractfactory.v1.domain.PaymentValidation;
import java.math.BigDecimal;

/**
 * 支付宝产品族的退款客户端；只打印模拟调用，不产生真实资金操作。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public final class AlipayRefundClient {
    /**
     * 声明当前客户端所属产品族。
     * @return ALIPAY
     */
    public PaymentChannel getChannel() {
        return PaymentChannel.ALIPAY;
    }

    /**
     * 先校验原交易归属，再执行模拟退款。
     * @param receipt 原支付凭证
     * @param amount 本次退款元金额
     * @return 模拟退款流水号
     * @throws IllegalArgumentException 凭证、渠道或金额不符合约束
     */
    public String refund(PaymentReceipt receipt, BigDecimal amount) {
        System.out.println("[abstract-factory/v1] AlipayRefundClient.refund ---> 进入支付宝退款产品");
        PaymentValidation.validateRefund(receipt, amount, getChannel());
        System.out.println("[abstract-factory/v1] AlipayRefundClient.refund ---> 模拟渠道调用，amount=" + amount + " 元");
        return "ALI-REFUND-" + receipt.getTradeNo();
    }
}
