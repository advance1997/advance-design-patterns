package com.advance.patterns.behavioral.templatemethod.v2.service;

import com.advance.patterns.behavioral.templatemethod.v2.domain.*;
import com.advance.patterns.behavioral.templatemethod.v2.gateway.AlipayClient;
import java.util.Objects;
import java.util.function.Consumer;

public final class AlipayPayment {
    private final AlipayClient client;
    private final PaymentSteps steps;

    public AlipayPayment(AlipayClient client, Consumer<String> events) {
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
        var response = client.pay(command.paymentNo(), command.amount());
        if (response == null || !"10000".equals(response.code())
                || response.tradeNo() == null || response.tradeNo().isBlank()) {
            throw new PaymentException("支付宝未确认支付成功");
        }
        return new PaymentResult(command.paymentNo(), "ALIPAY", response.tradeNo());
    }
}
