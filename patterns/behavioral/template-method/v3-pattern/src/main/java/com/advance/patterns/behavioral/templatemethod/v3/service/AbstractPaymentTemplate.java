package com.advance.patterns.behavioral.templatemethod.v3.service;

import com.advance.patterns.behavioral.templatemethod.v3.domain.*;
import java.util.Objects;

/**
 * 支付流程模板，以 final 入口固定顺序并开放渠道步骤和前后置钩子。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public abstract class AbstractPaymentTemplate {

    /**
     * 创建 AbstractPaymentTemplate 实例并初始化依赖或业务数据。
     *
     */
    protected AbstractPaymentTemplate() {
    }

    /**
     * 执行支付流程，渠道或步骤异常向调用方传播。
     *
     * @param command 待校验或执行的支付命令
     * @return 渠道确认后的统一支付结果
     * @throws RuntimeException 输入校验、渠道调用或扩展步骤失败时向调用方传播
     */
    public final PaymentResult pay(PaymentCommand command) {
        Objects.requireNonNull(command, "支付命令 command 不能为空").validate();
        System.out.println("[template-method/v3] AbstractPaymentTemplate ---> 开始执行前置步骤");
        try {
            System.out.println("[template-method/v3] AbstractPaymentTemplate ---> 进入 beforePay 前置扩展钩子");
            beforePay(command);
            PaymentResult result = Objects.requireNonNull(doPay(command), "渠道结果不能为空");
            System.out.println("[template-method/v3] AbstractPaymentTemplate ---> 进入 afterPay 后置扩展钩子");
            afterPay(command, result);
            System.out.println("[template-method/v3] AbstractPaymentTemplate ---> 业务步骤执行成功");
            return result;
        } catch (RuntimeException failure) {
            System.out.println("[template-method/v3] AbstractPaymentTemplate ---> 业务步骤执行失败");
            throw failure;
        } finally {
            System.out.println("[template-method/v3] AbstractPaymentTemplate ---> 流程执行结束");
        }
    }

    /**
     * 执行子类提供的渠道支付步骤，并返回确认结果。
     *
     * @param command 待校验或执行的支付命令
     * @return 渠道确认后的统一支付结果
     * @throws RuntimeException 输入校验、渠道调用或扩展步骤失败时向调用方传播
     */
    protected abstract PaymentResult doPay(PaymentCommand command);

    /**
     * 执行可选前置钩子；默认无操作，子类可追加渠道校验。
     *
     * @param command 待校验或执行的支付命令
     */
    protected void beforePay(PaymentCommand command) {}

    /**
     * 执行可选后置钩子；失败不代表外部支付已回滚。
     *
     * @param command 待校验或执行的支付命令
     * @param result 渠道确认后的统一支付结果
     */
    protected void afterPay(PaymentCommand command, PaymentResult result) {}
}
