package com.advance.patterns.behavioral.templatemethod.v3.service;

import com.advance.patterns.behavioral.templatemethod.v3.domain.*;
import com.advance.patterns.behavioral.templatemethod.v3.gateway.AlipayClient;
import java.util.Objects;
import java.util.function.Consumer;

public final class AlipayPayment extends AbstractPaymentTemplate {
    private final AlipayClient client;


    public AlipayPayment(AlipayClient client, Consumer<String> events) {
        super(events);
        this.client = Objects.requireNonNull(client, "client");

    }

    @Override
    protected PaymentResult doPay(PaymentCommand command) {
        var response = client.pay(command.paymentNo(), command.amount());
        if (response == null || !"10000".equals(response.code())
                || response.tradeNo() == null || response.tradeNo().isBlank()) {
            throw new PaymentException("支付宝未确认支付成功");
        }
        return new PaymentResult(command.paymentNo(), "ALIPAY", response.tradeNo());
    }
}
