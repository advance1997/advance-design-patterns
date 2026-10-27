package com.advance.patterns.creational.abstractfactory.v1.service;

import com.advance.patterns.creational.abstractfactory.v1.domain.PaymentChannel;
import com.advance.patterns.creational.abstractfactory.v1.domain.PaymentReceipt;
import com.advance.patterns.creational.abstractfactory.v1.product.AlipayPaymentClient;
import com.advance.patterns.creational.abstractfactory.v1.product.AlipayRefundClient;
import com.advance.patterns.creational.abstractfactory.v1.product.WechatPaymentClient;
import com.advance.patterns.creational.abstractfactory.v1.product.WechatRefundClient;
import java.math.BigDecimal;

/**
 * 直接创建版本：支付和退款各维护一次渠道分支，服务依赖四个具体产品。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public final class PaymentService {
    /** 当前服务选择的渠道；两个方法必须人工保持相同映射。 */
    private final PaymentChannel channel;

    /**
     * 选择受支持渠道；具体客户端仍由每个业务方法直接创建。
     * @param channel 服务使用的支付渠道
     * @throws IllegalArgumentException 渠道为空或不支持
     */
    public PaymentService(PaymentChannel channel) {
        if (channel != PaymentChannel.ALIPAY && channel != PaymentChannel.WECHAT_PAY) {
            throw new IllegalArgumentException("不支持的产品渠道：" + channel);
        }
        this.channel = channel;
    }

    /**
     * 发起一次模拟支付，不自动执行退款。
     * @param paymentNo 支付单号
     * @param amount 支付元金额
     * @return 模拟支付凭证
     * @throws IllegalArgumentException 单号或金额非法
     */
    public PaymentReceipt pay(String paymentNo, BigDecimal amount) {
        System.out.println("[abstract-factory/v1] PaymentService.pay ---> 执行支付业务");
        if (channel == PaymentChannel.ALIPAY) {
            System.out.println("[abstract-factory/v1] PaymentService.pay ---> 直接创建 AlipayPaymentClient");
            return new AlipayPaymentClient().pay(paymentNo, amount);
        }
        System.out.println("[abstract-factory/v1] PaymentService.pay ---> 直接创建 WechatPaymentClient");
        return new WechatPaymentClient().pay(paymentNo, amount);
    }

    /**
     * 根据原支付凭证模拟退款，渠道客户端负责拒绝跨渠道调用。
     * @param receipt 原支付凭证
     * @param amount 本次退款元金额
     * @return 模拟退款流水号
     * @throws IllegalArgumentException 凭证为空、渠道不匹配或金额非法
     */
    public String refund(PaymentReceipt receipt, BigDecimal amount) {
        System.out.println("[abstract-factory/v1] PaymentService.refund ---> 执行退款业务");
        if (channel == PaymentChannel.ALIPAY) {
            System.out.println("[abstract-factory/v1] PaymentService.refund ---> 直接创建 AlipayRefundClient");
            return new AlipayRefundClient().refund(receipt, amount);
        }
        System.out.println("[abstract-factory/v1] PaymentService.refund ---> 直接创建 WechatRefundClient");
        return new WechatRefundClient().refund(receipt, amount);
    }
}
