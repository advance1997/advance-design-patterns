package com.advance.patterns.behavioral.templatemethod.v3.domain;

import java.math.BigDecimal;

/**
 * 不可变支付命令，校验由支付流程入口调用。
 *
 * @author advance
 * @since 2026-10-26 23:26:13
 */
public final class PaymentCommand {
    /** 支付单号。 */
    private final String paymentNo;

    /** 以元计价的金额。 */
    private final BigDecimal amount;

    /**
     * 创建 PaymentCommand，保存本次业务数据。
     *
     * @param paymentNo 支付单号
     * @param amount 以元计价的金额
     */
    public PaymentCommand(String paymentNo, BigDecimal amount) {
        this.paymentNo = paymentNo;
        this.amount = amount;
    }

    /**
     * 获取支付单号。
     *
     * @return 支付单号
     */
    public String getPaymentNo() {
        return paymentNo;
    }

    /**
     * 获取以元计价的金额。
     *
     * @return 以元计价的金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 按字段值比较，保持数据对象的值语义。
     * @param other 待比较对象
     * @return 全部字段相等时返回 true
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        PaymentCommand that = (PaymentCommand) other;
        return java.util.Objects.equals(paymentNo, that.paymentNo)
                && java.util.Objects.equals(amount, that.amount);
    }

    /**
     * 计算与 equals 一致的哈希值。
     * @return 字段哈希值
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(paymentNo, amount);
    }

    /**
     * 生成便于教学观察的数据摘要。
     * @return 包含字段名称和值的文本
     */
    @Override
    public String toString() {
        return "PaymentCommand{" + "paymentNo=" + paymentNo + ", amount=" + amount + "}";
    }

    /**
     * 校验单号与金额；金额必须为正数且最多两位小数。
     * @throws IllegalArgumentException 输入不满足约束时抛出
     */
    public void validate() {
        System.out.println("[template-method/v3] PaymentCommand.validate ---> 校验支付单号和元金额");
        boolean blank = true;
        if (paymentNo != null) {
            for (int i = 0; i < paymentNo.length(); i++) {
                if (!Character.isWhitespace(paymentNo.charAt(i))) {
                    blank = false;
                    break;
                }
            }
        }
        if (blank) {
            throw new IllegalArgumentException("支付单号不能为空");
        }
        if (amount == null || amount.signum() <= 0 || amount.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException("金额必须为正数且最多两位小数");
        }
    }
}
