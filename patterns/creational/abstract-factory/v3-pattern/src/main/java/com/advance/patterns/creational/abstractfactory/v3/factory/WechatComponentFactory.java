package com.advance.patterns.creational.abstractfactory.v3.factory;

import com.advance.patterns.creational.abstractfactory.v3.domain.PaymentChannel;
import com.advance.patterns.creational.abstractfactory.v3.product.PaymentClient;
import com.advance.patterns.creational.abstractfactory.v3.product.RefundClient;
import com.advance.patterns.creational.abstractfactory.v3.product.WechatPaymentClient;
import com.advance.patterns.creational.abstractfactory.v3.product.WechatRefundClient;

/**
 * 微信具体工厂，只创建本渠道的两种匹配产品。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public final class WechatComponentFactory implements PaymentComponentFactory {
    /**
     * 声明本工厂产品族。
     * @return WECHAT_PAY
     */
    @Override
    public PaymentChannel getChannel() {
        return PaymentChannel.WECHAT_PAY;
    }

    /**
     * 创建本产品族的支付客户端。
     * @return 新的 WechatPaymentClient
     */
    @Override
    public PaymentClient createPaymentClient() {
        System.out.println("[abstract-factory/v3] WechatComponentFactory.createPaymentClient ---> 创建同渠道支付产品");
        return new WechatPaymentClient();
    }

    /**
     * 创建本产品族的退款客户端。
     * @return 新的 WechatRefundClient
     */
    @Override
    public RefundClient createRefundClient() {
        System.out.println("[abstract-factory/v3] WechatComponentFactory.createRefundClient ---> 创建同渠道退款产品");
        return new WechatRefundClient();
    }
}
