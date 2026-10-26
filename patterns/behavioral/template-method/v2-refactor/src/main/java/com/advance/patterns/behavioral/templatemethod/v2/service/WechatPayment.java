package com.advance.patterns.behavioral.templatemethod.v2.service;

import com.advance.patterns.behavioral.templatemethod.v2.domain.*;
import com.advance.patterns.behavioral.templatemethod.v2.gateway.WechatPayClient;
import java.util.Objects;
import java.util.function.Consumer;

public final class WechatPayment {
    private final WechatPayClient client;
    private final PaymentSteps steps;

    public WechatPayment(WechatPayClient client, Consumer<String> events) {
        this.client = Objects.requireNonNull(client, "client");
        this.steps = new PaymentSteps(events);
    }

    // 提取了公共步骤，但执行顺序仍由每个渠道自己维护。
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

    private PaymentResult invokeChannel(PaymentCommand command) {
        var response = client.createTransaction(command.paymentNo(),
                command.amount().movePointRight(2).longValueExact());
        if (response == null || !response.success()
                || response.transactionId() == null || response.transactionId().isBlank()) {
            throw new PaymentException("微信未确认支付成功");
        }
        return new PaymentResult(command.paymentNo(), "WECHAT_PAY", response.transactionId());
    }
}
