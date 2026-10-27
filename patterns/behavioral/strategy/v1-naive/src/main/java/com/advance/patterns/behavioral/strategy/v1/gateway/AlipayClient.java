package com.advance.patterns.behavioral.strategy.v1.gateway;

import java.math.BigDecimal;

/**
 * 支付宝教学网关，元金额与 code/tradeNo 字段模拟渠道差异。
 *
 * @author advance
 * @since 2026-10-27 18:35:37
 */
public interface AlipayClient {
    /**
     * 发起一次模拟支付，不包含真实网络连接或重试。
     * @param paymentNo 支付单号
     * @param amount 元金额
     * @return 渠道原始响应；调用方必须处理空响应
     * @throws IllegalStateException 模拟客户端超时
     */
    Response pay(String paymentNo, BigDecimal amount);

    /**
     * 保留渠道字段的原始响应，允许构造异常数据以验证边界。
     *
     * @author advance
     * @since 2026-10-27 18:35:37
     */
    final class Response {
        /** 渠道结果码，10000 表示模拟成功。 */
        private final String code;
        /** 渠道交易流水号。 */
        private final String tradeNo;

        /**
         * 保存原始响应，不在网关层替调用方判断成功。
         * @param code 原始结果码
         * @param tradeNo 渠道交易号
         */
        public Response(String code, String tradeNo) {
            this.code = code;
            this.tradeNo = tradeNo;
        }

        /**
         * 读取原始成功标识。
         * @return 原始结果码
         */
        public String getCode() {
            return code;
        }

        /**
         * 读取交易号。
         * @return 原始交易号，可能为空
         */
        public String getTradeNo() {
            return tradeNo;
        }
    }
}
