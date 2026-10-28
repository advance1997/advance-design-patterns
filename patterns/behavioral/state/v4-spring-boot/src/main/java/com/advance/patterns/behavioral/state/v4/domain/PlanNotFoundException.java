package com.advance.patterns.behavioral.state.v4.domain;

/**
 * 请求的虚构方案不存在，由 Web 层转为明确的业务错误响应。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
public class PlanNotFoundException extends RuntimeException {

    /** Java 异常序列化标识。 */
    private static final long serialVersionUID = 1L;

    /**
     * 保留可读的拒绝原因。
     *
     * @param message 中文错误说明
     */
    public PlanNotFoundException(String message) {
        super(message);
    }
}
