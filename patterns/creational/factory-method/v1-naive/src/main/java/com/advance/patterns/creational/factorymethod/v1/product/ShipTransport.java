package com.advance.patterns.creational.factorymethod.v1.product;

import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryRequest;

public final class ShipTransport implements Transport {

    @Override
    public DeliveryReceipt deliver(DeliveryRequest request) {
        return new DeliveryReceipt(request.orderId(), "SHIP", request.destination());
    }
}
