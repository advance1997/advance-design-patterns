package com.advance.patterns.creational.abstractfactory.v2.domain;

import java.math.BigDecimal;

/**
 * 本版本独有的支付参数校验；不承担产品创建或产品族选择职责。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public final class PaymentValidation {
    /** 元转分时小数点移动位数。 */
    private static final int CENT_SCALE = 2;

    /**
     * 禁止实例化无状态工具类。
     */
    private PaymentValidation() {
    }

    /**
     * 校验元金额，允许尾随零，不允许隐式舍入。
     * @param amount 元金额
     * @throws IllegalArgumentException 金额为空、非正数或超过两位有效小数
     */
    public static void validateAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0
                || amount.stripTrailingZeros().scale() > CENT_SCALE) {
            throw new IllegalArgumentException("amount 必须为正数元金额且最多两位有效小数");
        }
    }

    /**
     * 校验支付单号和金额，确保模拟调用前拒绝错误输入。
     * @param paymentNo 非空支付单号
     * @param amount 元金额
     * @throws IllegalArgumentException 单号或金额非法
     */
    public static void validatePayment(String paymentNo, BigDecimal amount) {
        System.out.println("[abstract-factory/v2] PaymentValidation.validatePayment ---> 校验支付单号与元金额");
        if (paymentNo == null || paymentNo.trim().isEmpty()) {
            throw new IllegalArgumentException("paymentNo 不能为空");
        }
        validateAmount(amount);
    }

    /**
     * 检查原交易归属和单次退款金额，不进行持久化或累计退款控制。
     * @param receipt 原支付凭证
     * @param amount 本次退款元金额
     * @param expectedChannel 当前退款客户端所属渠道
     * @throws IllegalArgumentException 凭证为空、渠道不匹配或退款金额非法
     */
    public static void validateRefund(PaymentReceipt receipt, BigDecimal amount,
            PaymentChannel expectedChannel) {
        System.out.println("[abstract-factory/v2] PaymentValidation.validateRefund ---> 核对原支付渠道和退款金额");
        if (receipt == null) {
            throw new IllegalArgumentException("receipt 不能为空");
        }
        if (expectedChannel == null || receipt.getChannel() != expectedChannel) {
            throw new IllegalArgumentException("退款客户端渠道与原支付渠道不一致");
        }
        validateAmount(amount);
        if (amount.compareTo(receipt.getPaidAmount()) > 0) {
            throw new IllegalArgumentException("单次退款金额不能超过原支付金额");
        }
    }

    /**
     * 将元精确转换为 long 类型的分，不做截断或舍入。
     * @param amount 合法元金额
     * @return 分金额
     * @throws IllegalArgumentException 金额非法或转换后超出 long 范围
     */
    public static long toCents(BigDecimal amount) {
        validateAmount(amount);
        try {
            return amount.movePointRight(CENT_SCALE).longValueExact();
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException("amount 转换为分后超出 long 范围", exception);
        }
    }
}
