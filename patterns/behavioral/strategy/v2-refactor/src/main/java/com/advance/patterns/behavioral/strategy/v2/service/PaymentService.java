package com.advance.patterns.behavioral.strategy.v2.service;

import com.advance.patterns.behavioral.strategy.v2.domain.PaymentChannel;
import com.advance.patterns.behavioral.strategy.v2.domain.PaymentCommand;
import com.advance.patterns.behavioral.strategy.v2.domain.PaymentException;
import com.advance.patterns.behavioral.strategy.v2.domain.PaymentResult;
import com.advance.patterns.behavioral.strategy.v2.gateway.AlipayClient;
import com.advance.patterns.behavioral.strategy.v2.gateway.WechatPayClient;
import com.advance.patterns.behavioral.strategy.v2.payment.AlipayPayment;
import com.advance.patterns.behavioral.strategy.v2.payment.WechatPayment;

/**
 * 过渡支付服务：协议细节已提取，但选择逻辑仍依赖具体渠道类。
 *
 * @author advance
 * @since 2026-10-27 18:35:37
 */
public final class PaymentService {
    /** 支付宝具体实现。 */
    private final AlipayPayment alipayPayment;
    /** 微信具体实现。 */
    private final WechatPayment wechatPayment;

    /**
     * 装配固定的两个渠道，增加渠道仍需要修改本类。
     * @param alipayClient 支付宝网关
     * @param wechatPayClient 微信网关
     * @throws IllegalArgumentException 任一网关为空
     */
    public PaymentService(AlipayClient alipayClient, WechatPayClient wechatPayClient) {
        if (alipayClient == null || wechatPayClient == null) {
            throw new IllegalArgumentException("alipayClient 和 wechatPayClient 均不能为空");
        }
        this.alipayPayment = new AlipayPayment(alipayClient);
        this.wechatPayment = new WechatPayment(wechatPayClient);
    }

    /**
     * 校验输入后使用分支选择渠道；不做默认降级或自动重试。
     * @param command 支付请求
     * @return 统一的成功结果
     * @throws IllegalArgumentException 输入非法、渠道不支持或金额无法转换
     * @throws PaymentException 渠道拒绝或响应不完整
     * @throws IllegalStateException 网关超时，结果未知
     */
    public PaymentResult pay(PaymentCommand command) {
        System.out.println("[strategy/v2] PaymentService.pay ---> 开始支付，使用条件分支选择渠道");
        if (command == null) {
            throw new IllegalArgumentException("command 不能为空");
        }
        command.validate();
        if (command.getChannel() == PaymentChannel.ALIPAY) {
            System.out.println("[strategy/v2] PaymentService.pay ---> 命中 ALIPAY 分支");
            return alipayPayment.pay(command);
        } else if (command.getChannel() == PaymentChannel.WECHAT_PAY) {
            System.out.println("[strategy/v2] PaymentService.pay ---> 命中 WECHAT_PAY 分支");
            return wechatPayment.pay(command);
        }
        throw new IllegalArgumentException("不支持支付渠道：" + command.getChannel());
    }
}
