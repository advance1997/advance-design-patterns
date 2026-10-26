package com.advance.patterns.behavioral.templatemethod.v2.service;

import com.advance.patterns.behavioral.templatemethod.v2.domain.*;
import com.advance.patterns.behavioral.templatemethod.v2.gateway.WechatPayClient;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 微信支付实现，处理元到分转换、响应校验与结果转换。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public final class WechatPayment {
    /** 渠道客户端，负责执行外部协议调用。 */
    private final WechatPayClient client;
    /** 公共支付步骤助手。 */
    private final PaymentSteps steps;

    /**
     * 创建 WechatPayment 实例并初始化依赖或业务数据。
     *
     * @param client 当前渠道的客户端实现
     * @param events 教学流程事件记录器，约定不抛出异常
     */
    public WechatPayment(WechatPayClient client, Consumer<String> events) {
        this.client = Objects.requireNonNull(client, "渠道客户端 client 不能为空");
        this.steps = new PaymentSteps(events);
    }

    // 提取了公共步骤，但执行顺序仍由每个渠道自己维护。
    /**
     * 执行支付流程，渠道或步骤异常向调用方传播。
     *
     * @param command 待校验或执行的支付命令
     * @return 渠道确认后的统一支付结果
     * @throws RuntimeException 输入校验、渠道调用或扩展步骤失败时向调用方传播
     */
    public PaymentResult pay(PaymentCommand command) {
        steps.validate(command);
        steps.before();
        try {
            PaymentResult result = invokeChannel(command);
            steps.success();
            return result;
        } catch (RuntimeException failure) {
            steps.failure();
            throw failure;
        } finally {
            steps.complete();
        }
    }

    /**
     * 调用渠道客户端并将已确认的成功响应转换为统一结果。
     *
     * @param command 待校验或执行的支付命令
     * @return 渠道确认后的统一支付结果
     * @throws RuntimeException 输入校验、渠道调用或扩展步骤失败时向调用方传播
     */
    private PaymentResult invokeChannel(PaymentCommand command) {
        System.out.println("[template-method/v2] WechatPayment ---> 调用渠道客户端");
        var response = client.createTransaction(command.paymentNo(),
                command.amount().movePointRight(2).longValueExact());
        if (response == null || !response.success()
                || response.transactionId() == null || response.transactionId().isBlank()) {
            System.out.println("[template-method/v2] WechatPayment ---> 渠道响应校验未通过");
            throw new PaymentException("微信未确认支付成功");
        }
        System.out.println("[template-method/v2] WechatPayment ---> 将渠道响应转换为统一结果");
        return new PaymentResult(command.paymentNo(), "WECHAT_PAY", response.transactionId());
    }
}
