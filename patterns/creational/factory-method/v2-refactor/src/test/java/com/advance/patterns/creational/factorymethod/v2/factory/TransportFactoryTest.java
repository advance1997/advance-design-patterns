package com.advance.patterns.creational.factorymethod.v2.factory;

import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryMethod;
import com.advance.patterns.creational.factorymethod.v2.product.ShipTransport;
import com.advance.patterns.creational.factorymethod.v2.product.TruckTransport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransportFactoryTest {

    @Test
    void shouldCreateProductForEachDeliveryMethod() {
        assertInstanceOf(TruckTransport.class, TransportFactory.create(DeliveryMethod.ROAD));
        assertInstanceOf(ShipTransport.class, TransportFactory.create(DeliveryMethod.SEA));
    }

    @Test
    void shouldRejectMissingDeliveryMethod() {
        assertThrows(NullPointerException.class, () -> TransportFactory.create(null));
    }
}
