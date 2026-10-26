package com.advance.patterns.creational.factorymethod.v1.domain;

/**
 * 不可变配送请求，在构造时校验必填字段。
 *
 * @author advance
 * @since 2026-10-26 23:26:13
 */
public final class DeliveryRequest {
    /** 订单标识。 */
    private final String orderId;

    /** 配送目的地。 */
    private final String destination;

    /**
     * 创建 DeliveryRequest，保存本次业务数据。
     *
     * @param orderId 订单标识
     * @param destination 配送目的地
     */
    public DeliveryRequest(String orderId, String destination) {
        requireText(orderId, "订单标识 orderId");
        requireText(destination, "目的地 destination");
        this.orderId = orderId;
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
        DeliveryRequest that = (DeliveryRequest) other;
        return java.util.Objects.equals(orderId, that.orderId)
                && java.util.Objects.equals(destination, that.destination);
    }

    /**
     * 计算与 equals 一致的哈希值。
     * @return 字段哈希值
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(orderId, destination);
    }

    /**
     * 生成便于教学观察的数据摘要。
     * @return 包含字段名称和值的文本
     */
    @Override
    public String toString() {
        return "DeliveryRequest{" + "orderId=" + orderId + ", destination=" + destination + "}";
    }

    /**
     * 检查必填文本，使用 Java 8 的字符判断保留空白校验语义。
     * @param value 待校验文本
     * @param field 字段说明
     * @throws IllegalArgumentException 字段为空或全部为空白时抛出
     */
    private static void requireText(String value, String field) {
        if (value != null) {
            for (int i = 0; i < value.length(); i++) {
                if (!Character.isWhitespace(value.charAt(i))) {
                    return;
                }
            }
        }
        System.out.println("[factory-method/v1] DeliveryRequest.requireText ---> 参数校验失败：" + field);
        throw new IllegalArgumentException(field + " 不能为空白");
    }
}
