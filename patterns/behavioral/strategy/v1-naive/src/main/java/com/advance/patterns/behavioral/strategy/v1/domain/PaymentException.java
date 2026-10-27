package com.advance.patterns.behavioral.strategy.v1.domain;

/**
 * 支付业务异常；表示渠道拒绝或返回数据不完整，不代表外部资金操作已回滚。
 *
 * @author advance
 * @since 2026-10-27 18:35:37
 */
public class PaymentException extends RuntimeException {
    /** 序列化版本号。 */
    private static final long serialVersionUID = 1L;

    /**
     * 保存失败原因，不包含密钥或完整渠道请求。
     * @param message 中文错误说明
     */
    public PaymentException(String message) {
        super(message);
    }
}
