package com.advance.patterns.behavioral.strategy.v1.service;

import com.advance.patterns.behavioral.strategy.v1.domain.PaymentChannel;
import com.advance.patterns.behavioral.strategy.v1.domain.PaymentCommand;
import com.advance.patterns.behavioral.strategy.v1.domain.PaymentException;
import com.advance.patterns.behavioral.strategy.v1.domain.PaymentResult;
import com.advance.patterns.behavioral.strategy.v1.gateway.AlipayClient;
import com.advance.patterns.behavioral.strategy.v1.gateway.WechatPayClient;

/**
 * 原始支付服务：渠道选择、协议调用和结果转换集中在条件分支中。
 *
 * @author advance
 * @since 2026-10-27 18:35:37
 */
public final class PaymentService {
    /** 支付宝网关。 */
    private final AlipayClient alipayClient;
    /** 微信网关。 */
    private final WechatPayClient wechatPayClient;

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
        this.alipayClient = alipayClient;
        this.wechatPayClient = wechatPayClient;
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
        System.out.println("[strategy/v1] PaymentService.pay ---> 开始支付，使用条件分支选择渠道");
        if (command == null) {
            throw new IllegalArgumentException("command 不能为空");
        }
        command.validate();
        if (command.getChannel() == PaymentChannel.ALIPAY) {
            System.out.println("[strategy/v1] PaymentService.pay ---> 命中 ALIPAY 分支");
            System.out.println("[strategy/v1] PaymentService.pay ---> 调用 AlipayClient.pay，amount 单位为元");
            AlipayClient.Response response = alipayClient.pay(command.getPaymentNo(), command.getAmount());
            if (response == null || !"10000".equals(response.getCode())) {
                throw new PaymentException("支付宝响应为空或 code 非 10000，不能确认支付成功");
            }
            System.out.println("[strategy/v1] PaymentService.pay ---> 读取 tradeNo，转换统一支付结果");
            return new PaymentResult(PaymentChannel.ALIPAY, response.getTradeNo());
        } else if (command.getChannel() == PaymentChannel.WECHAT_PAY) {
            System.out.println("[strategy/v1] PaymentService.pay ---> 命中 WECHAT_PAY 分支");
            long amountInCents;
            try {
                amountInCents = command.getAmount().movePointRight(2).longValueExact();
            } catch (ArithmeticException exception) {
                throw new IllegalArgumentException("amount 转换为分后超出 long 范围或无法精确表示", exception);
            }
            System.out.println("[strategy/v1] PaymentService.pay ---> 元转分，amountInCents=" + amountInCents);
            WechatPayClient.Response response =
                    wechatPayClient.createTransaction(command.getPaymentNo(), amountInCents);
            if (response == null || !response.isSuccess()) {
                throw new PaymentException("微信响应为空或 success 为 false，不能确认支付成功");
            }
            System.out.println("[strategy/v1] PaymentService.pay ---> 读取 transactionId，转换统一支付结果");
            return new PaymentResult(PaymentChannel.WECHAT_PAY, response.getTransactionId());
        }
        throw new IllegalArgumentException("不支持支付渠道：" + command.getChannel());
    }
}
