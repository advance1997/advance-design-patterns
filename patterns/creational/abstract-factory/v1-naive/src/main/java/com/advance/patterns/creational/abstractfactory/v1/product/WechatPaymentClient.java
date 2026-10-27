package com.advance.patterns.creational.abstractfactory.v1.product;

import com.advance.patterns.creational.abstractfactory.v1.domain.PaymentChannel;
import com.advance.patterns.creational.abstractfactory.v1.domain.PaymentReceipt;
import com.advance.patterns.creational.abstractfactory.v1.domain.PaymentValidation;
import java.math.BigDecimal;

/**
 * 微信产品族的支付客户端；只打印模拟调用，不产生真实资金操作。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public final class WechatPaymentClient {
    /**
     * 声明当前客户端所属产品族。
     * @return WECHAT_PAY
     */
    public PaymentChannel getChannel() {
        return PaymentChannel.WECHAT_PAY;
    }

    /**
     * 校验请求后返回确定性的模拟支付凭证。
     * @param paymentNo 支付单号
     * @param amount 支付元金额
     * @return 模拟支付凭证
     * @throws IllegalArgumentException 单号或金额不符合约束
     */
    public PaymentReceipt pay(String paymentNo, BigDecimal amount) {
        System.out.println("[abstract-factory/v1] WechatPaymentClient.pay ---> 进入微信支付产品");
        PaymentValidation.validatePayment(paymentNo, amount);
        long amountInCents = PaymentValidation.toCents(amount);
        System.out.println("[abstract-factory/v1] WechatPaymentClient.pay ---> 模拟渠道调用，amountInCents="
                + amountInCents + " 分");
        return new PaymentReceipt(getChannel(), "WX-" + paymentNo, amount);
    }
}
