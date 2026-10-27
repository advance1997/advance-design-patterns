package com.advance.patterns.creational.abstractfactory.v3.product;

import com.advance.patterns.creational.abstractfactory.v3.domain.PaymentChannel;
import com.advance.patterns.creational.abstractfactory.v3.domain.PaymentReceipt;
import com.advance.patterns.creational.abstractfactory.v3.domain.PaymentValidation;
import java.math.BigDecimal;

/**
 * 支付宝产品族的支付客户端；只打印模拟调用，不产生真实资金操作。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public final class AlipayPaymentClient implements PaymentClient {
    /**
     * 声明当前客户端所属产品族。
     * @return ALIPAY
     */
    @Override
    public PaymentChannel getChannel() {
        return PaymentChannel.ALIPAY;
    }

    /**
     * 校验请求后返回确定性的模拟支付凭证。
     * @param paymentNo 支付单号
     * @param amount 支付元金额
     * @return 模拟支付凭证
     * @throws IllegalArgumentException 单号或金额不符合约束
     */
    @Override
    public PaymentReceipt pay(String paymentNo, BigDecimal amount) {
        System.out.println("[abstract-factory/v3] AlipayPaymentClient.pay ---> 进入支付宝支付产品");
        PaymentValidation.validatePayment(paymentNo, amount);
        System.out.println("[abstract-factory/v3] AlipayPaymentClient.pay ---> 模拟渠道调用，amount=" + amount + " 元");
        return new PaymentReceipt(getChannel(), "ALI-" + paymentNo, amount);
    }
}
