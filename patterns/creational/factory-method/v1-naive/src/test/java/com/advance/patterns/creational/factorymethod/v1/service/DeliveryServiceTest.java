package com.advance.patterns.creational.factorymethod.v1.service;

import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryMethod;
import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 验证DeliveryServiceTest对应的正常路径、边界条件及异常行为。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
class DeliveryServiceTest {

    /** 当前实例使用的new依赖或测试数据。 */
    private final DeliveryService service = new DeliveryService();

    /**
     * 验证公路配送选择卡车，并保留请求中的订单与目的地。
     */
    @Test
    void shouldArrangeRoadDeliveryWithTruck() {
        DeliveryRequest request = new DeliveryRequest("ORDER-1001", "Shanghai");

        DeliveryReceipt receipt = service.arrange(DeliveryMethod.ROAD, request);

        assertEquals(new DeliveryReceipt("ORDER-1001", "TRUCK", "Shanghai"), receipt);
    }

    /**
     * 验证海运配送选择船舶，并保留请求中的订单与目的地。
     */
    @Test
    void shouldArrangeSeaDeliveryWithShip() {
        DeliveryRequest request = new DeliveryRequest("ORDER-1002", "Rotterdam");

        DeliveryReceipt receipt = service.arrange(DeliveryMethod.SEA, request);

        assertEquals(new DeliveryReceipt("ORDER-1002", "SHIP", "Rotterdam"), receipt);
    }

    /**
     * 验证空白订单号在请求构造时被拒绝。
     */
    @Test
    void shouldRejectBlankOrderId() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryRequest(" ", "Shanghai"));
    }
}
