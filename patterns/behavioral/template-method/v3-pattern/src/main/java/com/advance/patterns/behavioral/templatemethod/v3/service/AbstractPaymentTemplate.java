package com.advance.patterns.behavioral.templatemethod.v3.service;

import com.advance.patterns.behavioral.templatemethod.v3.domain.*;
import java.util.Objects;
import java.util.function.Consumer;

/** 固定支付骨架；每次请求仅使用局部变量，不在模板实例中存放请求状态。 */
public abstract class AbstractPaymentTemplate {
    private final Consumer<String> events;

    protected AbstractPaymentTemplate(Consumer<String> events) {
        this.events = Objects.requireNonNull(events, "events");
    }

    public final PaymentResult pay(PaymentCommand command) {
        Objects.requireNonNull(command, "command").validate();
        events.accept("before");
        try {
            beforePay(command);
            PaymentResult result = Objects.requireNonNull(doPay(command), "渠道结果不能为空");
            afterPay(command, result);
            events.accept("success");
            return result;
        } catch (RuntimeException failure) {
            events.accept("failure");
            throw failure;
        } finally {
            events.accept("complete");
        }
    }

    /** 必须实现的变化步骤：调用渠道、校验响应、转换结果。 */
    protected abstract PaymentResult doPay(PaymentCommand command);

    /** 可选前置钩子，例如渠道特有校验；默认不做任何事。 */
    protected void beforePay(PaymentCommand command) {}

    /** 可选后置钩子；此时外部渠道可能已经成功，失败不可直接重试支付。 */
    protected void afterPay(PaymentCommand command, PaymentResult result) {}
}
