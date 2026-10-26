package com.advance.patterns.behavioral.templatemethod.v3.service;

import com.advance.patterns.behavioral.templatemethod.v3.domain.*;
import com.advance.patterns.behavioral.templatemethod.v3.gateway.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private final List<String> events = new ArrayList<>();
    private final PaymentCommand command = new PaymentCommand("PAY-001", new BigDecimal("12.34"));

    @Test
    void alipayConvertsResponseAndPreservesStepOrder() {
        var payment = new AlipayPayment((no, amount) -> {
            assertEquals("PAY-001", no);
            assertEquals(new BigDecimal("12.34"), amount);
            events.add("channel");
            return new AlipayClient.Response("10000", "ALI-001");
        }, events::add);
        assertEquals(new PaymentResult("PAY-001", "ALIPAY", "ALI-001"), payment.pay(command));
        assertEquals(List.of("before", "channel", "success", "complete"), events);
    }

    @Test
    void wechatConvertsYuanToCentsAndUsesTransactionId() {
        var payment = new WechatPayment((no, cents) -> {
            assertEquals("PAY-001", no);
            assertEquals(1234L, cents);
            events.add("channel");
            return new WechatPayClient.Response(true, "WX-001");
        }, events::add);
        assertEquals(new PaymentResult("PAY-001", "WECHAT_PAY", "WX-001"), payment.pay(command));
        assertEquals(List.of("before", "channel", "success", "complete"), events);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void channelRejectionDoesNotReachSuccess(boolean alipay) {
        Function<PaymentCommand, PaymentResult> pay = alipay
                ? new AlipayPayment((no, amount) -> new AlipayClient.Response("40004", null), events::add)::pay
                : new WechatPayment((no, cents) -> new WechatPayClient.Response(false, null), events::add)::pay;
        assertThrows(PaymentException.class, () -> pay.apply(command));
        assertEquals(List.of("before", "failure", "complete"), events);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void transportFailureIsPropagatedWithoutAutomaticRetry(boolean alipay) {
        var timeout = new IllegalStateException("模拟网关超时");
        Function<PaymentCommand, PaymentResult> pay = alipay
                ? new AlipayPayment((no, amount) -> { events.add("channel"); throw timeout; }, events::add)::pay
                : new WechatPayment((no, cents) -> { events.add("channel"); throw timeout; }, events::add)::pay;
        assertSame(timeout, assertThrows(IllegalStateException.class, () -> pay.apply(command)));
        assertEquals(List.of("before", "channel", "failure", "complete"), events);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void malformedSuccessResponseIsRejected(boolean alipay) {
        Function<PaymentCommand, PaymentResult> pay = alipay
                ? new AlipayPayment((no, amount) -> new AlipayClient.Response("10000", " "), events::add)::pay
                : new WechatPayment((no, cents) -> new WechatPayClient.Response(true, ""), events::add)::pay;
        assertThrows(PaymentException.class, () -> pay.apply(command));
        assertEquals(List.of("before", "failure", "complete"), events);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void invalidInputStopsBeforeAnyChannelCall(boolean alipay) {
        Function<PaymentCommand, PaymentResult> pay = alipay
                ? new AlipayPayment((no, amount) -> { fail("不应调用渠道"); return null; }, events::add)::pay
                : new WechatPayment((no, cents) -> { fail("不应调用渠道"); return null; }, events::add)::pay;
        for (var invalid : List.of(
                new PaymentCommand("", BigDecimal.ONE),
                new PaymentCommand(null, BigDecimal.ONE),
                new PaymentCommand("PAY-001", null),
                new PaymentCommand("PAY-001", BigDecimal.ZERO),
                new PaymentCommand("PAY-001", new BigDecimal("-1")),
                new PaymentCommand("PAY-001", new BigDecimal("1.001")))) {
            assertThrows(IllegalArgumentException.class, () -> pay.apply(invalid));
        }
        assertThrows(NullPointerException.class, () -> pay.apply(null));
        assertTrue(events.isEmpty());
    }
}
