package com.advance.patterns.creational.abstractfactory.v3.product;

import com.advance.patterns.creational.abstractfactory.v3.domain.PaymentChannel;
import com.advance.patterns.creational.abstractfactory.v3.domain.PaymentReceipt;
import java.math.BigDecimal;

/**
 * 支付产品抽象；只描述支付能力，不负责选择或创建退款客户端。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public interface PaymentClient {
    /**
     * 声明所属产品族，实例生命周期内保持稳定。
     * @return 非空渠道标识
     */
    PaymentChannel getChannel();

    /**
     * 发起一次模拟支付，实际产品实现不连接真实平台。
     * @param paymentNo 支付单号
     * @param amount 支付元金额
     * @return 包含渠道、交易号和金额的模拟支付凭证
     * @throws IllegalArgumentException 单号或金额不符合约束
     */
    PaymentReceipt pay(String paymentNo, BigDecimal amount);
}
