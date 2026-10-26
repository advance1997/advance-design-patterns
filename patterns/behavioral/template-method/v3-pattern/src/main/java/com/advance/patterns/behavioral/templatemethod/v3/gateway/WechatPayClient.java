package com.advance.patterns.behavioral.templatemethod.v3.gateway;

/**
 * 微信支付教学网关接口，按分金额调用模拟交易能力。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public interface WechatPayClient {
    /**
     * 执行 createTransaction 对应的业务步骤。
     *
     * @param paymentNo 支付单号
     * @param amountInCents 以分计价的支付金额
     * @return 当前步骤的处理结果
     */
    Response createTransaction(String paymentNo, long amountInCents);
    /**
     * 渠道响应，保留原始协议字段。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    public final class Response {
        /** 渠道确认成功标志。 */
        private final boolean success;

        /** 微信交易流水号。 */
        private final String transactionId;

        /**
         * 创建 Response，保存本次业务数据。
         *
         * @param success 渠道确认成功标志
         * @param transactionId 微信交易流水号
         */
        public Response(boolean success, String transactionId) {
            this.success = success;
            this.transactionId = transactionId;
        }

        /**
         * 获取渠道确认成功标志。
         *
         * @return 渠道确认成功标志
         */
        public boolean isSuccess() {
            return success;
        }

        /**
         * 获取微信交易流水号。
         *
         * @return 微信交易流水号
         */
        public String getTransactionId() {
            return transactionId;
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
            Response that = (Response) other;
            return java.util.Objects.equals(success, that.success)
                    && java.util.Objects.equals(transactionId, that.transactionId);
        }

        /**
         * 计算与 equals 一致的哈希值。
         * @return 字段哈希值
         */
        @Override
        public int hashCode() {
            return java.util.Objects.hash(success, transactionId);
        }

        /**
         * 生成便于教学观察的数据摘要。
         * @return 包含字段名称和值的文本
         */
        @Override
        public String toString() {
            return "Response{" + "success=" + success + ", transactionId=" + transactionId + "}";
        }
    }
}
