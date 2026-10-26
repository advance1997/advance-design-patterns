package com.advance.patterns.creational.factorymethod.v3.product;

import com.advance.patterns.creational.factorymethod.v3.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v3.domain.DeliveryRequest;

public final class TruckTransport implements Transport {

    @Override
    public DeliveryReceipt deliver(DeliveryRequest request) {
        return new DeliveryReceipt(request.orderId(), "TRUCK", request.destination());
    }
}
