package com.advance.patterns.behavioral.templatemethod.v2.domain;

/**
 * 支付业务异常，用于表示渠道响应未通过成功校验。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public final class PaymentException extends RuntimeException {
    /**
     * 创建 PaymentException 实例并初始化依赖或业务数据。
     *
     * @param message 异常说明
     */
    public PaymentException(String message) {
        super(message);
    }
}
