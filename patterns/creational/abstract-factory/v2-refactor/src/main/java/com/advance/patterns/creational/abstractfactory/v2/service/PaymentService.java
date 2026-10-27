package com.advance.patterns.creational.abstractfactory.v2.service;

import com.advance.patterns.creational.abstractfactory.v2.domain.PaymentReceipt;
import com.advance.patterns.creational.abstractfactory.v2.product.PaymentClient;
import com.advance.patterns.creational.abstractfactory.v2.product.RefundClient;
import java.math.BigDecimal;

/**
 * 过渡版本：服务依赖产品接口，但两个产品可以由调用方独立选择，仍可能错配。
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
     * 注入两个独立创建的产品；本版本刻意未在装配时检查渠道一致性。
     * @param paymentClient 支付客户端
     * @param refundClient 退款客户端
     * @throws IllegalArgumentException 任一客户端为空
     */
    public PaymentService(PaymentClient paymentClient, RefundClient refundClient) {
        if (paymentClient == null || refundClient == null) {
            throw new IllegalArgumentException("paymentClient 和 refundClient 均不能为空");
        }
        this.paymentClient = paymentClient;
        this.refundClient = refundClient;
        System.out.println("[abstract-factory/v2] PaymentService ---> 注入两个独立产品，未统一约束产品族");
    }

    /**
     * 发起一次模拟支付，不自动执行退款。
     * @param paymentNo 支付单号
     * @param amount 支付元金额
     * @return 模拟支付凭证
     * @throws IllegalArgumentException 单号或金额非法
     */
    public PaymentReceipt pay(String paymentNo, BigDecimal amount) {
        System.out.println("[abstract-factory/v2] PaymentService.pay ---> 执行支付业务");
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
        System.out.println("[abstract-factory/v2] PaymentService.refund ---> 执行退款业务");
        return refundClient.refund(receipt, amount);
    }
}
