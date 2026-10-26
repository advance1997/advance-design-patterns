package com.advance.patterns.creational.factorymethod.v2.domain;

/**
 * 配送结果，记录订单标识、运输工具和目的地。
 *
 * @param orderId 订单标识，不允许为空白
 * @param transport 运输工具标识
 * @param destination 配送目的地，不允许为空白
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public record DeliveryReceipt(String orderId, String transport, String destination) {
}
