package com.advance.patterns.creational.factorymethod.v1.product;

import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryRequest;

public final class TruckTransport implements Transport {

    @Override
    public DeliveryReceipt deliver(DeliveryRequest request) {
        return new DeliveryReceipt(request.orderId(), "TRUCK", request.destination());
    }
}
