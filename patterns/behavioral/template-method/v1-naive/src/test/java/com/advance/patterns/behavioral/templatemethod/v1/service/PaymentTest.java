package com.advance.patterns.behavioral.templatemethod.v1.service;

import com.advance.patterns.behavioral.templatemethod.v1.domain.*;
import com.advance.patterns.behavioral.templatemethod.v1.gateway.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 验证PaymentTest对应的正常路径、边界条件及异常行为。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
class PaymentTest {
    /** 当前实例使用的new依赖或测试数据。 */
    private final List<String> events = new ArrayList<>();
    /** 当前实例使用的new依赖或测试数据。 */
    private final PaymentCommand command = new PaymentCommand("PAY-001", new BigDecimal("12.34"));

    /**
     * 验证支付宝接收元金额、转换交易号，并按前置、渠道、成功、结束顺序执行。
     */
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

    /**
     * 验证微信金额精确转换为分，使用响应交易号构造统一结果。
     */
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

    /**
     * 验证渠道拒绝时进入失败和结束步骤，不进入成功步骤。
     *
     * @param alipay 是否使用支付宝分支，false 表示微信分支
     */
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void channelRejectionDoesNotReachSuccess(boolean alipay) {
        Function<PaymentCommand, PaymentResult> pay = alipay
                ? new AlipayPayment((no, amount) -> new AlipayClient.Response("40004", null), events::add)::pay
                : new WechatPayment((no, cents) -> new WechatPayClient.Response(false, null), events::add)::pay;
        assertThrows(PaymentException.class, () -> pay.apply(command));
        assertEquals(List.of("before", "failure", "complete"), events);
    }

    /**
     * 验证客户端超时原样传出且不会触发自动重试。
     *
     * @param alipay 是否使用支付宝分支，false 表示微信分支
     */
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

    /**
     * 验证成功响应缺少有效交易号时仍被拒绝。
     *
     * @param alipay 是否使用支付宝分支，false 表示微信分支
     */
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void malformedSuccessResponseIsRejected(boolean alipay) {
        Function<PaymentCommand, PaymentResult> pay = alipay
                ? new AlipayPayment((no, amount) -> new AlipayClient.Response("10000", " "), events::add)::pay
                : new WechatPayment((no, cents) -> new WechatPayClient.Response(true, ""), events::add)::pay;
        assertThrows(PaymentException.class, () -> pay.apply(command));
        assertEquals(List.of("before", "failure", "complete"), events);
    }

    /**
     * 验证非法单号或金额在前置校验阶段被拒绝，渠道不会被调用。
     *
     * @param alipay 是否使用支付宝分支，false 表示微信分支
     */
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
                /**
                 * 验证 PaymentCommand 场景下的执行结果与异常约束。
                 *
                 * @param "PAY-001" 本步骤使用的"PAY-001"参数
                 * @param BigDecimal("1.001" 本步骤使用的BigDecimal("1.001"参数
                 * @return 当前步骤的处理结果
                 */
                new PaymentCommand("PAY-001", new BigDecimal("1.001")))) {
            assertThrows(IllegalArgumentException.class, () -> pay.apply(invalid));
        }
        assertThrows(NullPointerException.class, () -> pay.apply(null));
        assertTrue(events.isEmpty());
    }
}
