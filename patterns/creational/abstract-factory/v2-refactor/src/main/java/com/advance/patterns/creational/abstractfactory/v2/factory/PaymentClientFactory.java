package com.advance.patterns.creational.abstractfactory.v2.factory;

import com.advance.patterns.creational.abstractfactory.v2.domain.PaymentChannel;
import com.advance.patterns.creational.abstractfactory.v2.product.PaymentClient;
import com.advance.patterns.creational.abstractfactory.v2.product.AlipayPaymentClient;
import com.advance.patterns.creational.abstractfactory.v2.product.WechatPaymentClient;

/**
 * 独立的支付简单工厂；无法约束调用方使用另一个工厂时选择相同渠道。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public final class PaymentClientFactory {
    /**
     * 工具型工厂不需要实例化。
     */
    private PaymentClientFactory() {
    }

    /**
     * 按渠道创建单一产品，不能保证一组产品的渠道一致。
     * @param channel 产品所属渠道
     * @return 新建的支付客户端
     * @throws IllegalArgumentException 渠道为空或不支持
     */
    public static PaymentClient create(PaymentChannel channel) {
        System.out.println("[abstract-factory/v2] PaymentClientFactory.create ---> 创建单个产品，channel=" + channel);
        if (channel == PaymentChannel.ALIPAY) {
            return new AlipayPaymentClient();
        }
        if (channel == PaymentChannel.WECHAT_PAY) {
            return new WechatPaymentClient();
        }
        throw new IllegalArgumentException("不支持的产品渠道：" + channel);
    }
}
