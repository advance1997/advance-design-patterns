package com.advance.patterns.creational.abstractfactory.v3.service;

import com.advance.patterns.creational.abstractfactory.v3.domain.PaymentChannel;
import com.advance.patterns.creational.abstractfactory.v3.domain.PaymentReceipt;
import com.advance.patterns.creational.abstractfactory.v3.product.PaymentClient;
import com.advance.patterns.creational.abstractfactory.v3.product.RefundClient;
import com.advance.patterns.creational.abstractfactory.v3.factory.PaymentComponentFactory;
import java.math.BigDecimal;

/**
 * 抽象工厂客户端：一次装配同渠道产品族，业务方法只依赖抽象产品。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public final class PaymentService {
    /** 支付产品，只保存客户端依赖，不保存单次请求。 */
    private final PaymentClient paymentClient;
    /** 退款产品，与支付产品是不同的产品类型。 */
    private final RefundClient refundClient;

    /**
     * 从同一个工厂各创建一次产品，并在业务调用前校验产品族一致性。
     * @param factory 产品族工厂
     * @throws IllegalArgumentException 工厂为空
     * @throws IllegalStateException 工厂渠道为空、产品为空或产品不属于该工厂渠道
     */
    public PaymentService(PaymentComponentFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("factory 不能为空");
        }
        PaymentChannel channel = factory.getChannel();
        if (channel == null) {
            throw new IllegalStateException("工厂必须声明非空产品族渠道");
        }
        PaymentClient createdPaymentClient = factory.createPaymentClient();
        RefundClient createdRefundClient = factory.createRefundClient();
        if (createdPaymentClient == null || createdRefundClient == null) {
            throw new IllegalStateException("工厂不能返回空产品");
        }
        if (createdPaymentClient.getChannel() != channel || createdRefundClient.getChannel() != channel) {
            throw new IllegalStateException("工厂创建的支付产品与退款产品必须属于声明的同一渠道");
        }
        this.paymentClient = createdPaymentClient;
        this.refundClient = createdRefundClient;
        System.out.println("[abstract-factory/v3] PaymentService ---> 产品族校验通过，channel=" + channel);
    }

    /**
     * 发起一次模拟支付，不自动执行退款。
     * @param paymentNo 支付单号
     * @param amount 支付元金额
     * @return 模拟支付凭证
     * @throws IllegalArgumentException 单号或金额非法
     */
    public PaymentReceipt pay(String paymentNo, BigDecimal amount) {
        System.out.println("[abstract-factory/v3] PaymentService.pay ---> 执行支付业务");
        return paymentClient.pay(paymentNo, amount);
    }

    /**
     * 根据原支付凭证模拟退款，渠道客户端负责拒绝跨渠道调用。
     * @param receipt 原支付凭证
     * @param amount 本次退款元金额
     * @return 模拟退款流水号
     * @throws IllegalArgumentException 凭证为空、渠道不匹配或金额非法
     */
    public String refund(PaymentReceipt receipt, BigDecimal amount) {
        System.out.println("[abstract-factory/v3] PaymentService.refund ---> 执行退款业务");
        return refundClient.refund(receipt, amount);
    }
}
