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
        System.out.println("[template-method/v1] WechatPayment ---> before");
        events.accept("before");
        try {
            PaymentResult result = invokeChannel(command);
            System.out.println("[template-method/v1] WechatPayment ---> success");
            events.accept("success");
            return result;
        } catch (RuntimeException failure) {
            System.out.println("[template-method/v1] WechatPayment ---> failure");
            events.accept("failure");
            throw failure;
        } finally {
            System.out.println("[template-method/v1] WechatPayment ---> complete");
            events.accept("complete");
        }
    }

    private PaymentResult invokeChannel(PaymentCommand command) {
        System.out.println("[template-method/v1] WechatPayment ---> invoke channel");
        var response = client.createTransaction(command.paymentNo(),
                command.amount().movePointRight(2).longValueExact());
        if (response == null || !response.success()
                || response.transactionId() == null || response.transactionId().isBlank()) {
            System.out.println("[template-method/v1] WechatPayment ---> rejected response");
            throw new PaymentException("微信未确认支付成功");
        }
        System.out.println("[template-method/v1] WechatPayment ---> convert result");
        return new PaymentResult(command.paymentNo(), "WECHAT_PAY", response.transactionId());
    }
}
