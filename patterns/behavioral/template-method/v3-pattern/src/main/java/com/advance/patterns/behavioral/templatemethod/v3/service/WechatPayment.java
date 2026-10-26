package com.advance.patterns.behavioral.templatemethod.v3.service;

import com.advance.patterns.behavioral.templatemethod.v3.domain.*;
import com.advance.patterns.behavioral.templatemethod.v3.gateway.WechatPayClient;
import java.util.Objects;

/**
 * 微信支付实现，处理元到分转换、响应校验与结果转换。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public final class WechatPayment extends AbstractPaymentTemplate {
    /** 渠道客户端，负责执行外部协议调用。 */
    private final WechatPayClient client;

    /**
     * 创建 WechatPayment 实例并初始化依赖或业务数据。
     *
     * @param client 当前渠道的客户端实现
     */
    public WechatPayment(WechatPayClient client) {

        this.client = Objects.requireNonNull(client, "渠道客户端 client 不能为空");
    }

    /**
     * 执行子类提供的渠道支付步骤，并返回确认结果。
     *
     * @param command 待校验或执行的支付命令
     * @return 渠道确认后的统一支付结果
     * @throws RuntimeException 输入校验、渠道调用或扩展步骤失败时向调用方传播
     */
    @Override
    protected PaymentResult doPay(PaymentCommand command) {
        System.out.println("[template-method/v3] WechatPayment ---> 调用渠道客户端");
        WechatPayClient.Response response = client.createTransaction(command.getPaymentNo(),
                command.getAmount().movePointRight(2).longValueExact());
        if (response == null || !response.isSuccess()
                || response.getTransactionId() == null || response.getTransactionId().trim().isEmpty()) {
            System.out.println("[template-method/v3] WechatPayment ---> 渠道响应校验未通过");
            throw new PaymentException("微信未确认支付成功");
        }
        System.out.println("[template-method/v3] WechatPayment ---> 将渠道响应转换为统一结果");
        return new PaymentResult(command.getPaymentNo(), "WECHAT_PAY", response.getTransactionId());
    }
}
