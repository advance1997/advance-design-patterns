package com.advance.patterns.creational.factorymethod.v2.domain;

/**
 * 不可变配送结果。
 *
 * @author advance
 * @since 2026-10-26 23:26:13
 */
public final class DeliveryReceipt {
    /** 订单标识。 */
    private final String orderId;

    /** 运输工具标识。 */
    private final String transport;

    /** 配送目的地。 */
    private final String destination;

    /**
     * 创建 DeliveryReceipt，保存本次业务数据。
     *
     * @param orderId 订单标识
     * @param transport 运输工具标识
     * @param destination 配送目的地
     */
    public DeliveryReceipt(String orderId, String transport, String destination) {
        this.orderId = orderId;
        this.transport = transport;
        this.destination = destination;
    }

    /**
     * 获取订单标识。
     *
     * @return 订单标识
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 获取运输工具标识。
     *
     * @return 运输工具标识
     */
    public String getTransport() {
        return transport;
    }

    /**
     * 获取配送目的地。
     *
     * @return 配送目的地
     */
    public String getDestination() {
        return destination;
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
        DeliveryReceipt that = (DeliveryReceipt) other;
        return java.util.Objects.equals(orderId, that.orderId)
                && java.util.Objects.equals(transport, that.transport)
                && java.util.Objects.equals(destination, that.destination);
    }

    /**
     * 计算与 equals 一致的哈希值。
     * @return 字段哈希值
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(orderId, transport, destination);
    }

    /**
     * 生成便于教学观察的数据摘要。
     * @return 包含字段名称和值的文本
     */
    @Override
    public String toString() {
        return "DeliveryReceipt{" + "orderId=" + orderId + ", transport=" + transport + ", destination=" + destination + "}";
    }

}
