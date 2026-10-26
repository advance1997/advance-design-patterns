package com.advance.patterns.behavioral.templatemethod.v3.service;

import com.advance.patterns.behavioral.templatemethod.v3.domain.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TemplateHooksTest {
    private final List<String> events = new ArrayList<>();
    private final PaymentCommand command = new PaymentCommand("PAY-002", BigDecimal.ONE);

    @Test
    void extensionHooksExecuteInsideFixedSkeleton() throws Exception {
        var template = new AbstractPaymentTemplate(events::add) {
            protected void beforePay(PaymentCommand command) { events.add("hook-before"); }
            protected PaymentResult doPay(PaymentCommand command) {
                events.add("channel");
                return new PaymentResult(command.paymentNo(), "TEST", "T-1");
            }
            protected void afterPay(PaymentCommand command, PaymentResult result) {
                events.add("hook-after");
            }
        };
        template.pay(command);
        assertEquals(List.of("before", "hook-before", "channel", "hook-after", "success", "complete"), events);
        assertTrue(Modifier.isFinal(AbstractPaymentTemplate.class
                .getMethod("pay", PaymentCommand.class).getModifiers()));
    }

    @Test
    void failingBeforeHookPreventsChannelCallAndStillCompletes() {
        var failure = new IllegalArgumentException("渠道前置校验失败");
        var template = new AbstractPaymentTemplate(events::add) {
            protected void beforePay(PaymentCommand command) { throw failure; }
            protected PaymentResult doPay(PaymentCommand command) {
                fail("前置失败不能调用渠道");
                return null;
            }
        };
        assertSame(failure, assertThrows(IllegalArgumentException.class, () -> template.pay(command)));
        assertEquals(List.of("before", "failure", "complete"), events);
    }

    @Test
    void failingAfterHookDoesNotTriggerPaymentAgain() {
        var failure = new IllegalStateException("后处理失败");
        var template = new AbstractPaymentTemplate(events::add) {
            protected PaymentResult doPay(PaymentCommand command) {
                events.add("channel");
                return new PaymentResult(command.paymentNo(), "TEST", "T-2");
            }
            protected void afterPay(PaymentCommand command, PaymentResult result) { throw failure; }
        };
        assertSame(failure, assertThrows(IllegalStateException.class, () -> template.pay(command)));
        assertEquals(List.of("before", "channel", "failure", "complete"), events);
    }

    @Test
    void nullResultFailsBeforeAfterHook() {
        var template = new AbstractPaymentTemplate(events::add) {
            protected PaymentResult doPay(PaymentCommand command) { return null; }
            protected void afterPay(PaymentCommand command, PaymentResult result) { fail("不能后处理空结果"); }
        };
        assertThrows(NullPointerException.class, () -> template.pay(command));
        assertEquals(List.of("before", "failure", "complete"), events);
    }
}
