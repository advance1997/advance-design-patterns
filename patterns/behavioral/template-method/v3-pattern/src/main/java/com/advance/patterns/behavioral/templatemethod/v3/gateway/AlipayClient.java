package com.advance.patterns.behavioral.templatemethod.v3.gateway;

import java.math.BigDecimal;

/**
 * 支付宝教学网关接口，按元金额调用模拟支付能力。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public interface AlipayClient {
    /**
     * 执行支付流程，渠道或步骤异常向调用方传播。
     *
     * @param paymentNo 支付单号
     * @param amount 以元计价的支付金额
     * @return 当前步骤的处理结果
     */
    Response pay(String paymentNo, BigDecimal amount);
    /**
     * 渠道响应，保留原始协议字段。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    public final class Response {
        /** 渠道返回码。 */
        private final String code;

        /** 渠道交易流水号。 */
        private final String tradeNo;

        /**
         * 创建 Response，保存本次业务数据。
         *
         * @param code 渠道返回码
         * @param tradeNo 渠道交易流水号
         */
        public Response(String code, String tradeNo) {
            this.code = code;
            this.tradeNo = tradeNo;
        }

        /**
         * 获取渠道返回码。
         *
         * @return 渠道返回码
         */
        public String getCode() {
            return code;
        }

        /**
         * 获取渠道交易流水号。
         *
         * @return 渠道交易流水号
         */
        public String getTradeNo() {
            return tradeNo;
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
            return java.util.Objects.equals(code, that.code)
                    && java.util.Objects.equals(tradeNo, that.tradeNo);
        }

        /**
         * 计算与 equals 一致的哈希值。
         * @return 字段哈希值
         */
        @Override
        public int hashCode() {
            return java.util.Objects.hash(code, tradeNo);
        }

        /**
         * 生成便于教学观察的数据摘要。
         * @return 包含字段名称和值的文本
         */
        @Override
        public String toString() {
            return "Response{" + "code=" + code + ", tradeNo=" + tradeNo + "}";
        }
    }
}
