package com.advance.patterns.creational.factorymethod.v2.product;

import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryRequest;

public final class TruckTransport implements Transport {

    @Override
    public DeliveryReceipt deliver(DeliveryRequest request) {
        System.out.println("[factory-method/v2] TRUCK ---> deliver ---> start");
        try {
            java.util.Objects.requireNonNull(request, "request must not be null");
            return new DeliveryReceipt(request.orderId(), "TRUCK", request.destination());
        } catch (RuntimeException failure) {
            System.out.println("[factory-method/v2] TRUCK ---> deliver ---> failure: " + failure.getClass().getSimpleName()
                    + " | " + failure.getMessage());
            throw failure;
        }
    }
}
