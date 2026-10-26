package com.advance.patterns.creational.factorymethod.v1.domain;

/**
 * 配送请求，保存订单标识与目的地，并在构造时检查必填字段。
 *
 * @param orderId 订单标识，不允许为空白
 * @param destination 配送目的地，不允许为空白
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public record DeliveryRequest(String orderId, String destination) {

    /**
     * 在构造时校验请求必填字段，保留原始文本。
     */
    public DeliveryRequest {
        orderId = requireText(orderId, "orderId");
        destination = requireText(destination, "destination");
    }

    /**
     * 检查文本字段是否非空白，校验失败时抛出参数异常。
     *
     * @param value 待校验的输入值
     * @param fieldName 参与校验的字段名称
     * @return 校验通过的原始文本，不执行裁剪
     * @throws IllegalArgumentException 文本为 null 或空白时抛出
     */
    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            System.out.println("[factory-method/v1] DeliveryRequest ---> 参数校验失败，字段：" + fieldName);
            throw new IllegalArgumentException((fieldName.equals("orderId") ? "订单标识 " : "目的地 ") + fieldName + " 不能为空白");
        }
        return value;
    }
}
