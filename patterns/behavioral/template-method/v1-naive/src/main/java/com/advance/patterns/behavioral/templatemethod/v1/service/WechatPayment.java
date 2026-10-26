package com.advance.patterns.behavioral.templatemethod.v1.service;

import com.advance.patterns.behavioral.templatemethod.v1.domain.*;
import com.advance.patterns.behavioral.templatemethod.v1.gateway.WechatPayClient;
import java.util.Objects;
import java.util.function.Consumer;

public final class WechatPayment {
    private final WechatPayClient client;
    private final Consumer<String> events;

    public WechatPayment(WechatPayClient client, Consumer<String> events) {
        this.client = Objects.requireNonNull(client, "client");
        this.events = Objects.requireNonNull(events, "events");
    }

    // 两个渠道重复同一套流程：增加步骤时必须修改两处。
    public PaymentResult pay(PaymentCommand command) {
        Objects.requireNonNull(command, "command").validate();
        events.accept("before");
        try {
            PaymentResult result = invokeChannel(command);
            events.accept("success");
            return result;
        } catch (RuntimeException failure) {
            events.accept("failure");
            throw failure;
        } finally {
            events.accept("complete");
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
