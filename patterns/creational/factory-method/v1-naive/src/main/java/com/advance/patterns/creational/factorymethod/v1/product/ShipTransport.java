package com.advance.patterns.creational.factorymethod.v1.product;

import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryRequest;

public final class ShipTransport implements Transport {

    @Override
    public DeliveryReceipt deliver(DeliveryRequest request) {
        System.out.println("[factory-method/v1] SHIP ---> deliver ---> start");
        try {
            java.util.Objects.requireNonNull(request, "request must not be null");
            return new DeliveryReceipt(request.orderId(), "SHIP", request.destination());
        } catch (RuntimeException failure) {
            System.out.println("[factory-method/v1] SHIP ---> deliver ---> failure: " + failure.getClass().getSimpleName()
                    + " | " + failure.getMessage());
            throw failure;
        }
    }
}
