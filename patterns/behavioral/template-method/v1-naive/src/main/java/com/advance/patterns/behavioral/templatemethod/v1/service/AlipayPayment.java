package com.advance.patterns.behavioral.templatemethod.v1.service;

import com.advance.patterns.behavioral.templatemethod.v1.domain.*;
import com.advance.patterns.behavioral.templatemethod.v1.gateway.AlipayClient;
import java.util.Objects;
import java.util.function.Consumer;

public final class AlipayPayment {
    private final AlipayClient client;
    private final Consumer<String> events;

    public AlipayPayment(AlipayClient client, Consumer<String> events) {
        this.client = Objects.requireNonNull(client, "client");
        this.events = Objects.requireNonNull(events, "events");
    }

    // 两个渠道重复同一套流程：增加步骤时必须修改两处。
    public PaymentResult pay(PaymentCommand command) {
        Objects.requireNonNull(command, "command").validate();
        System.out.println("[template-method/v1] AlipayPayment ---> before");
        events.accept("before");
        try {
            PaymentResult result = invokeChannel(command);
            System.out.println("[template-method/v1] AlipayPayment ---> success");
            events.accept("success");
            return result;
        } catch (RuntimeException failure) {
            System.out.println("[template-method/v1] AlipayPayment ---> failure");
            events.accept("failure");
            throw failure;
        } finally {
            System.out.println("[template-method/v1] AlipayPayment ---> complete");
            events.accept("complete");
        }
    }

    private PaymentResult invokeChannel(PaymentCommand command) {
        System.out.println("[template-method/v1] AlipayPayment ---> invoke channel");
        var response = client.pay(command.paymentNo(), command.amount());
        if (response == null || !"10000".equals(response.code())
                || response.tradeNo() == null || response.tradeNo().isBlank()) {
            System.out.println("[template-method/v1] AlipayPayment ---> rejected response");
            throw new PaymentException("支付宝未确认支付成功");
        }
        System.out.println("[template-method/v1] AlipayPayment ---> convert result");
        return new PaymentResult(command.paymentNo(), "ALIPAY", response.tradeNo());
    }
}
