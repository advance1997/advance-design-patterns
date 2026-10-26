package com.advance.patterns.creational.factorymethod.v1.service;

import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryMethod;
import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeliveryServiceTest {

    private final DeliveryService service = new DeliveryService();

    @Test
    void shouldArrangeRoadDeliveryWithTruck() {
        DeliveryRequest request = new DeliveryRequest("ORDER-1001", "Shanghai");

        DeliveryReceipt receipt = service.arrange(DeliveryMethod.ROAD, request);

        assertEquals(new DeliveryReceipt("ORDER-1001", "TRUCK", "Shanghai"), receipt);
    }

    @Test
    void shouldArrangeSeaDeliveryWithShip() {
        DeliveryRequest request = new DeliveryRequest("ORDER-1002", "Rotterdam");

        DeliveryReceipt receipt = service.arrange(DeliveryMethod.SEA, request);

        assertEquals(new DeliveryReceipt("ORDER-1002", "SHIP", "Rotterdam"), receipt);
    }

    @Test
    void shouldRejectBlankOrderId() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryRequest(" ", "Shanghai"));
    }
}
