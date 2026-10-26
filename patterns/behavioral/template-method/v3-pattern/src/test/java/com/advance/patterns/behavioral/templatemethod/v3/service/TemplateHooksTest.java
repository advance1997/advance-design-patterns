package com.advance.patterns.behavioral.templatemethod.v3.service;

import com.advance.patterns.behavioral.templatemethod.v3.domain.PaymentCommand;
import com.advance.patterns.behavioral.templatemethod.v3.domain.PaymentResult;
import com.advance.patterns.behavioral.templatemethod.v3.gateway.AlipayClient;
import com.advance.patterns.behavioral.templatemethod.v3.gateway.WechatPayClient;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/**
 * TemplateHooksTest 打印演示；每个场景独立准备数据，人工对照期望值。
 *
 * @author advance
 * @since 2026-10-26 23:26:13
 */
public class TemplateHooksTest {
    /**
     * 演示 testHookOrder 对应的支付场景。
     * <p>期望结果：依次输出前置步骤、beforePay、doPay、afterPay、成功、结束。</p>
     */
    @Test
    public void testHookOrder() {
        System.out.println("\ntestHookOrder ---> 期望：依次输出前置步骤、beforePay、doPay、afterPay、成功、结束");
        AbstractPaymentTemplate template = new HookOrderTemplate();
        PaymentResult result = template.pay(new PaymentCommand("PAY-002", BigDecimal.ONE));
        System.out.println("实际支付结果：" + result);
    }

    /**
     * 演示 testBeforeFailure 对应的支付场景。
     * <p>期望结果：IllegalArgumentException，前置钩子失败，不执行 doPay 和 afterPay。</p>
     */
    @Test
    public void testBeforeFailure() {
        System.out.println("\ntestBeforeFailure ---> 期望：IllegalArgumentException，前置钩子失败，不执行 doPay 和 afterPay");
        try {
            AbstractPaymentTemplate template = new BeforeFailureTemplate();
            PaymentResult result = template.pay(new PaymentCommand("PAY-002", BigDecimal.ONE));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testAfterFailure 对应的支付场景。
     * <p>期望结果：IllegalStateException，渠道执行一次，后置失败不再次支付。</p>
     */
    @Test
    public void testAfterFailure() {
        System.out.println("\ntestAfterFailure ---> 期望：IllegalStateException，渠道执行一次，后置失败不再次支付");
        try {
            AbstractPaymentTemplate template = new AfterFailureTemplate();
            PaymentResult result = template.pay(new PaymentCommand("PAY-002", BigDecimal.ONE));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testNullResult 对应的支付场景。
     * <p>期望结果：NullPointerException，空渠道结果被拒绝，不执行 afterPay。</p>
     */
    @Test
    public void testNullResult() {
        System.out.println("\ntestNullResult ---> 期望：NullPointerException，空渠道结果被拒绝，不执行 afterPay");
        try {
            AbstractPaymentTemplate template = new NullResultTemplate();
            PaymentResult result = template.pay(new PaymentCommand("PAY-002", BigDecimal.ONE));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 依次输出前置步骤、beforePay、doPay、afterPay、成功、结束。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class HookOrderTemplate extends AbstractPaymentTemplate {
        /** @param command 本次支付命令 */
        @Override
        protected void beforePay(PaymentCommand command) {
            System.out.println("HookOrderTemplate.beforePay ---> 自定义前置钩子");
        }

        /**
         * 执行模拟渠道步骤。
         * @param command 本次支付命令
         * @return 模拟渠道结果
         */
        @Override
        protected PaymentResult doPay(PaymentCommand command) {
            System.out.println("HookOrderTemplate.doPay ---> 执行一次渠道支付");
            return new PaymentResult(command.getPaymentNo(), "TEST", "T-001");
        }

        /**
         * 执行本场景的后置钩子。
         * @param command 本次支付命令
         * @param result 渠道结果
         */
        @Override
        protected void afterPay(PaymentCommand command, PaymentResult result) {
            System.out.println("HookOrderTemplate.afterPay ---> 自定义后置钩子");
        }
    }

    /**
     * IllegalArgumentException，前置钩子失败，不执行 doPay 和 afterPay。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class BeforeFailureTemplate extends AbstractPaymentTemplate {
        /** @param command 本次支付命令 */
        @Override
        protected void beforePay(PaymentCommand command) {
            throw new IllegalArgumentException("渠道前置校验失败");
        }

        /**
         * 执行模拟渠道步骤。
         * @param command 本次支付命令
         * @return 模拟渠道结果
         */
        @Override
        protected PaymentResult doPay(PaymentCommand command) {
            System.out.println("BeforeFailureTemplate.doPay ---> 执行一次渠道支付");
            return new PaymentResult(command.getPaymentNo(), "TEST", "T-001");
        }

        /**
         * 执行本场景的后置钩子。
         * @param command 本次支付命令
         * @param result 渠道结果
         */
        @Override
        protected void afterPay(PaymentCommand command, PaymentResult result) {
            System.out.println("BeforeFailureTemplate.afterPay ---> 不应出现此输出");
        }
    }

    /**
     * IllegalStateException，渠道执行一次，后置失败不再次支付。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class AfterFailureTemplate extends AbstractPaymentTemplate {
        /** @param command 本次支付命令 */
        @Override
        protected void beforePay(PaymentCommand command) {
            System.out.println("AfterFailureTemplate.beforePay ---> 前置钩子");
        }

        /**
         * 执行模拟渠道步骤。
         * @param command 本次支付命令
         * @return 模拟渠道结果
         */
        @Override
        protected PaymentResult doPay(PaymentCommand command) {
            System.out.println("AfterFailureTemplate.doPay ---> 执行一次渠道支付");
            return new PaymentResult(command.getPaymentNo(), "TEST", "T-001");
        }

        /**
         * 执行本场景的后置钩子。
         * @param command 本次支付命令
         * @param result 渠道结果
         */
        @Override
        protected void afterPay(PaymentCommand command, PaymentResult result) {
            throw new IllegalStateException("后处理失败");
        }
    }

    /**
     * NullPointerException，空渠道结果被拒绝，不执行 afterPay。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class NullResultTemplate extends AbstractPaymentTemplate {
        /** @param command 本次支付命令 */
        @Override
        protected void beforePay(PaymentCommand command) {
            System.out.println("NullResultTemplate.beforePay ---> 前置钩子");
        }

        /**
         * 执行模拟渠道步骤。
         * @param command 本次支付命令
         * @return 模拟渠道结果
         */
        @Override
        protected PaymentResult doPay(PaymentCommand command) {
            System.out.println("NullResultTemplate.doPay ---> 执行一次渠道支付");
            return null;
        }

        /**
         * 执行本场景的后置钩子。
         * @param command 本次支付命令
         * @param result 渠道结果
         */
        @Override
        protected void afterPay(PaymentCommand command, PaymentResult result) {
            System.out.println("NullResultTemplate.afterPay ---> 不应出现此输出");
        }
    }

}
