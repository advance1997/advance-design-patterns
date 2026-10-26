package com.advance.patterns.creational.factorymethod.v3.product;

import com.advance.patterns.creational.factorymethod.v3.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v3.domain.DeliveryRequest;

public final class ShipTransport implements Transport {

    @Override
    public DeliveryReceipt deliver(DeliveryRequest request) {
        return new DeliveryReceipt(request.orderId(), "SHIP", request.destination());
    }
}
