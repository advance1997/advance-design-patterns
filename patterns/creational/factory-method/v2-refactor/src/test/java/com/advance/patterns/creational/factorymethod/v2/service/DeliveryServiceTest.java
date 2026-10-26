package com.advance.patterns.creational.factorymethod.v2.service;

import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryMethod;
import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeliveryServiceTest {

    private final DeliveryService service = new DeliveryService();

    @Test
    void shouldDelegateDeliveryToFactoryProduct() {
        DeliveryReceipt receipt = service.arrange(
                DeliveryMethod.SEA,
                new DeliveryRequest("ORDER-2001", "Singapore")
        );

        assertEquals(new DeliveryReceipt("ORDER-2001", "SHIP", "Singapore"), receipt);
    }
}
