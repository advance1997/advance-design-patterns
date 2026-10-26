package com.advance.patterns.behavioral.templatemethod.v3.service;

import com.advance.patterns.behavioral.templatemethod.v3.domain.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 验证TemplateHooksTest对应的正常路径、边界条件及异常行为。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
class TemplateHooksTest {
    /** 当前实例使用的new依赖或测试数据。 */
    private final List<String> events = new ArrayList<>();
    /** 当前实例使用的new依赖或测试数据。 */
    private final PaymentCommand command = new PaymentCommand("PAY-002", BigDecimal.ONE);

    /**
     * 验证前后置钩子在固定骨架中的执行顺序，以及入口的 final 约束。
     */
    @Test
    void extensionHooksExecuteInsideFixedSkeleton() throws Exception {
        var template = new AbstractPaymentTemplate(events::add) {
            /**
             * 执行可选前置钩子；默认无操作，子类可追加渠道校验。
             *
             * @param command 待校验或执行的支付命令
             */
            protected void beforePay(PaymentCommand command) { events.add("hook-before"); }
            /**
             * 执行子类提供的渠道支付步骤，并返回确认结果。
             *
             * @param command 待校验或执行的支付命令
             * @return 渠道确认后的统一支付结果
     * @throws RuntimeException 输入校验、渠道调用或扩展步骤失败时向调用方传播
             */
            protected PaymentResult doPay(PaymentCommand command) {
                events.add("channel");
                return new PaymentResult(command.paymentNo(), "TEST", "T-1");
            }
            /**
             * 执行可选后置钩子；失败不代表外部支付已回滚。
             *
             * @param command 待校验或执行的支付命令
             * @param result 渠道确认后的统一支付结果
             */
            protected void afterPay(PaymentCommand command, PaymentResult result) {
                events.add("hook-after");
            }
        };
        template.pay(command);
        assertEquals(List.of("before", "hook-before", "channel", "hook-after", "success", "complete"), events);
        assertTrue(Modifier.isFinal(AbstractPaymentTemplate.class
                .getMethod("pay", PaymentCommand.class).getModifiers()));
    }

    /**
     * 验证前置钩子失败后跳过渠道调用，但仍执行异常和结束步骤。
     */
    @Test
    void failingBeforeHookPreventsChannelCallAndStillCompletes() {
        var failure = new IllegalArgumentException("渠道前置校验失败");
        var template = new AbstractPaymentTemplate(events::add) {
            /**
             * 执行可选前置钩子；默认无操作，子类可追加渠道校验。
             *
             * @param command 待校验或执行的支付命令
             */
            protected void beforePay(PaymentCommand command) { throw failure; }
            /**
             * 执行子类提供的渠道支付步骤，并返回确认结果。
             *
             * @param command 待校验或执行的支付命令
             * @return 渠道确认后的统一支付结果
     * @throws RuntimeException 输入校验、渠道调用或扩展步骤失败时向调用方传播
             */
            protected PaymentResult doPay(PaymentCommand command) {
                fail("前置失败不能调用渠道");
                return null;
            }
        };
        assertSame(failure, assertThrows(IllegalArgumentException.class, () -> template.pay(command)));
        assertEquals(List.of("before", "failure", "complete"), events);
    }

    /**
     * 验证渠道已执行后发生后置异常时不会重复发起支付。
     */
    @Test
    void failingAfterHookDoesNotTriggerPaymentAgain() {
        var failure = new IllegalStateException("后处理失败");
        var template = new AbstractPaymentTemplate(events::add) {
            /**
             * 执行子类提供的渠道支付步骤，并返回确认结果。
             *
             * @param command 待校验或执行的支付命令
             * @return 渠道确认后的统一支付结果
     * @throws RuntimeException 输入校验、渠道调用或扩展步骤失败时向调用方传播
             */
            protected PaymentResult doPay(PaymentCommand command) {
                events.add("channel");
                return new PaymentResult(command.paymentNo(), "TEST", "T-2");
            }
            /**
             * 执行可选后置钩子；失败不代表外部支付已回滚。
             *
             * @param command 待校验或执行的支付命令
             * @param result 渠道确认后的统一支付结果
             */
            protected void afterPay(PaymentCommand command, PaymentResult result) { throw failure; }
        };
        assertSame(failure, assertThrows(IllegalStateException.class, () -> template.pay(command)));
        assertEquals(List.of("before", "channel", "failure", "complete"), events);
    }

    /**
     * 验证空渠道结果会被拒绝，并跳过后置钩子。
     */
    @Test
    void nullResultFailsBeforeAfterHook() {
        var template = new AbstractPaymentTemplate(events::add) {
            /**
             * 执行子类提供的渠道支付步骤，并返回确认结果。
             *
             * @param command 待校验或执行的支付命令
             * @return 渠道确认后的统一支付结果
     * @throws RuntimeException 输入校验、渠道调用或扩展步骤失败时向调用方传播
             */
            protected PaymentResult doPay(PaymentCommand command) { return null; }
            /**
             * 执行可选后置钩子；失败不代表外部支付已回滚。
             *
             * @param command 待校验或执行的支付命令
             * @param result 渠道确认后的统一支付结果
             */
            protected void afterPay(PaymentCommand command, PaymentResult result) { fail("不能后处理空结果"); }
        };
        assertThrows(NullPointerException.class, () -> template.pay(command));
        assertEquals(List.of("before", "failure", "complete"), events);
    }
}
