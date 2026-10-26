package com.advance.patterns.behavioral.templatemethod.v2.service;

import com.advance.patterns.behavioral.templatemethod.v2.domain.PaymentCommand;
import java.util.Objects;
import java.util.function.Consumer;

/** 公共步骤助手还不是模板方法：它并不拥有完整流程。 */
final class PaymentSteps {
    private final Consumer<String> events;

    PaymentSteps(Consumer<String> events) {
        this.events = Objects.requireNonNull(events, "events");
    }

    void validate(PaymentCommand command) { Objects.requireNonNull(command, "command").validate(); }
    void before() { events.accept("before"); }
    void success() { events.accept("success"); }
    void failure() { events.accept("failure"); }
    void complete() { events.accept("complete"); }
}
