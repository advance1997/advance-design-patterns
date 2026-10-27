package com.advance.patterns.behavioral.strategy.v2.gateway;

/**
 * 微信教学网关，分金额与 success/transactionId 字段模拟渠道差异。
 *
 * @author advance
 * @since 2026-10-27 18:35:37
 */
public interface WechatPayClient {
    /**
     * 发起一次模拟支付，不包含真实网络连接或重试。
     * @param paymentNo 支付单号
     * @param amountInCents 分金额
     * @return 渠道原始响应；调用方必须处理空响应
     * @throws IllegalStateException 模拟客户端超时
     */
    Response createTransaction(String paymentNo, long amountInCents);

    /**
     * 保留渠道字段的原始响应，允许构造异常数据以验证边界。
     *
     * @author advance
     * @since 2026-10-27 18:35:37
     */
    final class Response {
        /** 渠道是否明确成功。 */
        private final boolean success;
        /** 渠道交易流水号。 */
        private final String transactionId;

        /**
         * 保存原始响应，不在网关层替调用方判断成功。
         * @param success 是否成功
         * @param transactionId 渠道交易号
         */
        public Response(boolean success, String transactionId) {
            this.success = success;
            this.transactionId = transactionId;
        }

        /**
         * 读取原始成功标识。
         * @return 是否成功
         */
        public boolean isSuccess() {
            return success;
        }

        /**
         * 读取交易号。
         * @return 原始交易号，可能为空
         */
        public String getTransactionId() {
            return transactionId;
        }
    }
}
