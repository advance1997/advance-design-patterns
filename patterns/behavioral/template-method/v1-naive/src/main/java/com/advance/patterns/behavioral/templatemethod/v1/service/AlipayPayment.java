package com.advance.patterns.behavioral.templatemethod.v1.service;

import com.advance.patterns.behavioral.templatemethod.v1.domain.*;
import com.advance.patterns.behavioral.templatemethod.v1.gateway.AlipayClient;
import java.util.Objects;

/**
 * 支付宝支付实现，处理渠道调用、成功码校验与结果转换。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public final class AlipayPayment {
    /** 渠道客户端，负责执行外部协议调用。 */
    private final AlipayClient client;

    /**
     * 创建 AlipayPayment 实例并初始化依赖或业务数据。
     *
     * @param client 当前渠道的客户端实现
     */
    public AlipayPayment(AlipayClient client) {
        this.client = Objects.requireNonNull(client, "渠道客户端 client 不能为空");
    }

    // 两个渠道重复同一套流程：增加步骤时必须修改两处。
    /**
     * 执行支付流程，渠道或步骤异常向调用方传播。
     *
     * @param command 待校验或执行的支付命令
     * @return 渠道确认后的统一支付结果
     * @throws RuntimeException 输入校验、渠道调用或扩展步骤失败时向调用方传播
     */
    public PaymentResult pay(PaymentCommand command) {
        Objects.requireNonNull(command, "支付命令 command 不能为空").validate();
        System.out.println("[template-method/v1] AlipayPayment ---> 开始执行前置步骤");
        try {
            PaymentResult result = invokeChannel(command);
            System.out.println("[template-method/v1] AlipayPayment ---> 业务步骤执行成功");
            return result;
        } catch (RuntimeException failure) {
            System.out.println("[template-method/v1] AlipayPayment ---> 业务步骤执行失败");
            throw failure;
        } finally {
            System.out.println("[template-method/v1] AlipayPayment ---> 流程执行结束");
        }
    }

    /**
     * 调用渠道客户端并将已确认的成功响应转换为统一结果。
     *
     * @param command 待校验或执行的支付命令
     * @return 渠道确认后的统一支付结果
     * @throws RuntimeException 输入校验、渠道调用或扩展步骤失败时向调用方传播
     */
    private PaymentResult invokeChannel(PaymentCommand command) {
        System.out.println("[template-method/v1] AlipayPayment ---> 调用渠道客户端");
        AlipayClient.Response response = client.pay(command.getPaymentNo(), command.getAmount());
        if (response == null || !"10000".equals(response.getCode())
                || response.getTradeNo() == null || response.getTradeNo().trim().isEmpty()) {
            System.out.println("[template-method/v1] AlipayPayment ---> 渠道响应校验未通过");
            throw new PaymentException("支付宝未确认支付成功");
        }
        System.out.println("[template-method/v1] AlipayPayment ---> 将渠道响应转换为统一结果");
        return new PaymentResult(command.getPaymentNo(), "ALIPAY", response.getTradeNo());
    }
}
