package com.advance.patterns.creational.abstractfactory.v3.factory;

import com.advance.patterns.creational.abstractfactory.v3.domain.PaymentChannel;

/**
 * 装配入口的简单选择器；只选择产品族工厂，不在业务服务中分别选择两种产品。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public final class PaymentFactories {
    /**
     * 禁止实例化选择工具。
     */
    private PaymentFactories() {
    }

    /**
     * 按渠道选择一个具体产品族工厂。
     * @param channel 所需产品族渠道
     * @return 新的具体工厂
     * @throws IllegalArgumentException 渠道为空或不支持
     */
    public static PaymentComponentFactory create(PaymentChannel channel) {
        System.out.println("[abstract-factory/v3] PaymentFactories.create ---> 选择整个产品族，channel=" + channel);
        if (channel == PaymentChannel.ALIPAY) {
            return new AlipayComponentFactory();
        }
        if (channel == PaymentChannel.WECHAT_PAY) {
            return new WechatComponentFactory();
        }
        throw new IllegalArgumentException("不支持的产品渠道：" + channel);
    }
}
