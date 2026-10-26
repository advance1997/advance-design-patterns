package com.advance.patterns.creational.factorymethod.v3.creator;

import com.advance.patterns.creational.factorymethod.v3.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v3.domain.DeliveryRequest;
import com.advance.patterns.creational.factorymethod.v3.product.Transport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LogisticsTest {

    @Test
    void roadCreatorShouldUseTruckProduct() {
        Logistics logistics = new RoadLogistics();

        DeliveryReceipt receipt = logistics.planDelivery(
                new DeliveryRequest("ORDER-3001", "Beijing")
        );

        assertEquals(new DeliveryReceipt("ORDER-3001", "TRUCK", "Beijing"), receipt);
    }

    @Test
    void seaCreatorShouldUseShipProduct() {
        Logistics logistics = new SeaLogistics();

        DeliveryReceipt receipt = logistics.planDelivery(
                new DeliveryRequest("ORDER-3002", "Hamburg")
        );

        assertEquals(new DeliveryReceipt("ORDER-3002", "SHIP", "Hamburg"), receipt);
    }

    @Test
    void newTransportCanBeAddedWithoutChangingExistingCreators() {
        Logistics airLogistics = new Logistics() {
            @Override
            protected Transport createTransport() {
                return request -> new DeliveryReceipt(
                        request.orderId(),
                        "AIRPLANE",
                        request.destination()
                );
            }
        };

        DeliveryReceipt receipt = airLogistics.planDelivery(
                new DeliveryRequest("ORDER-3003", "New York")
        );

        assertEquals(new DeliveryReceipt("ORDER-3003", "AIRPLANE", "New York"), receipt);
    }

    @Test
    void stableProcessShouldRejectMissingRequest() {
        Logistics logistics = new RoadLogistics();

        assertThrows(NullPointerException.class, () -> logistics.planDelivery(null));
    }
}
