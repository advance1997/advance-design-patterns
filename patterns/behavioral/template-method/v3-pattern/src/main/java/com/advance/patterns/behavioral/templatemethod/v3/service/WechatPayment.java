package com.advance.patterns.behavioral.templatemethod.v3.service;

import com.advance.patterns.behavioral.templatemethod.v3.domain.*;
import com.advance.patterns.behavioral.templatemethod.v3.gateway.WechatPayClient;
import java.util.Objects;
import java.util.function.Consumer;

public final class WechatPayment extends AbstractPaymentTemplate {
    private final WechatPayClient client;


    public WechatPayment(WechatPayClient client, Consumer<String> events) {
        super(events);
        this.client = Objects.requireNonNull(client, "client");

    }

    @Override
    protected PaymentResult doPay(PaymentCommand command) {
        System.out.println("[template-method/v3] WechatPayment ---> invoke channel");
        var response = client.createTransaction(command.paymentNo(),
                command.amount().movePointRight(2).longValueExact());
        if (response == null || !response.success()
                || response.transactionId() == null || response.transactionId().isBlank()) {
            System.out.println("[template-method/v3] WechatPayment ---> rejected response");
            throw new PaymentException("微信未确认支付成功");
        }
        System.out.println("[template-method/v3] WechatPayment ---> convert result");
        return new PaymentResult(command.paymentNo(), "WECHAT_PAY", response.transactionId());
    }
}
