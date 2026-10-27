package com.advance.patterns.creational.abstractfactory.v3.factory;

import com.advance.patterns.creational.abstractfactory.v3.domain.PaymentChannel;
import com.advance.patterns.creational.abstractfactory.v3.product.PaymentClient;
import com.advance.patterns.creational.abstractfactory.v3.product.RefundClient;

/**
 * 抽象工厂：统一创建同一渠道的支付产品与退款产品，表达产品族约束。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public interface PaymentComponentFactory {
    /**
     * 声明整个产品族的渠道，返回值在工厂生命周期内保持稳定。
     * @return 非空渠道
     */
    PaymentChannel getChannel();

    /**
     * 创建本产品族的支付客户端。
     * @return 非空客户端，其 getChannel 必须等于工厂渠道
     */
    PaymentClient createPaymentClient();

    /**
     * 创建本产品族的退款客户端。
     * @return 非空客户端，其 getChannel 必须等于工厂渠道
     */
    RefundClient createRefundClient();
}
