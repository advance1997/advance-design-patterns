package com.advance.patterns.creational.abstractfactory.v2.product;

import com.advance.patterns.creational.abstractfactory.v2.domain.PaymentChannel;
import com.advance.patterns.creational.abstractfactory.v2.domain.PaymentReceipt;
import java.math.BigDecimal;

/**
 * 退款产品抽象，与支付产品是两种不同职责，而不是两种支付算法。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public interface RefundClient {
    /**
     * 声明所属产品族，实例生命周期内保持稳定。
     * @return 非空渠道标识
     */
    PaymentChannel getChannel();

    /**
     * 对指定原交易执行一次模拟退款，不改变原凭证。
     * @param receipt 原支付凭证
     * @param amount 本次退款元金额
     * @return 模拟退款流水号
     * @throws IllegalArgumentException 凭证、渠道或金额不符合约束
     */
    String refund(PaymentReceipt receipt, BigDecimal amount);
}
