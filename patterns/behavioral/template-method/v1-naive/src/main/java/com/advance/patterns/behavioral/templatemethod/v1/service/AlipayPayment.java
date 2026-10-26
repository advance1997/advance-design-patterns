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
        var response = client.pay(command.paymentNo(), command.amount());
        if (response == null || !"10000".equals(response.code())
                || response.tradeNo() == null || response.tradeNo().isBlank()) {
            throw new PaymentException("支付宝未确认支付成功");
        }
        return new PaymentResult(command.paymentNo(), "ALIPAY", response.tradeNo());
    }
}
