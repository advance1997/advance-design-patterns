package com.advance.patterns.creational.factorymethod.v2.factory;

import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryMethod;
import com.advance.patterns.creational.factorymethod.v2.product.ShipTransport;
import com.advance.patterns.creational.factorymethod.v2.product.Transport;
import com.advance.patterns.creational.factorymethod.v2.product.TruckTransport;

import java.util.Objects;

public final class TransportFactory {

    private TransportFactory() {
    }

    public static Transport create(DeliveryMethod method) {
        Objects.requireNonNull(method, "method must not be null");
        return switch (method) {
            case ROAD -> new TruckTransport();
            case SEA -> new ShipTransport();
        };
    }
}
