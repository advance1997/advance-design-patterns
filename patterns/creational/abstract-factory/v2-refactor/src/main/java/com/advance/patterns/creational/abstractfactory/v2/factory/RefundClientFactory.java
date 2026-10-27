package com.advance.patterns.creational.abstractfactory.v2.factory;

import com.advance.patterns.creational.abstractfactory.v2.domain.PaymentChannel;
import com.advance.patterns.creational.abstractfactory.v2.product.RefundClient;
import com.advance.patterns.creational.abstractfactory.v2.product.AlipayRefundClient;
import com.advance.patterns.creational.abstractfactory.v2.product.WechatRefundClient;

/**
 * 独立的退款简单工厂；无法约束调用方使用另一个工厂时选择相同渠道。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public final class RefundClientFactory {
    /**
     * 工具型工厂不需要实例化。
     */
    private RefundClientFactory() {
    }

    /**
     * 按渠道创建单一产品，不能保证一组产品的渠道一致。
     * @param channel 产品所属渠道
     * @return 新建的退款客户端
     * @throws IllegalArgumentException 渠道为空或不支持
     */
    public static RefundClient create(PaymentChannel channel) {
        System.out.println("[abstract-factory/v2] RefundClientFactory.create ---> 创建单个产品，channel=" + channel);
        if (channel == PaymentChannel.ALIPAY) {
            return new AlipayRefundClient();
        }
        if (channel == PaymentChannel.WECHAT_PAY) {
            return new WechatRefundClient();
        }
        throw new IllegalArgumentException("不支持的产品渠道：" + channel);
    }
}
