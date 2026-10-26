package com.advance.patterns.creational.factorymethod.v3.creator;

import com.advance.patterns.creational.factorymethod.v3.product.Transport;
import com.advance.patterns.creational.factorymethod.v3.product.TruckTransport;

public final class RoadLogistics extends Logistics {

    @Override
    protected Transport createTransport() {
        System.out.println("[factory-method/v3] RoadLogistics ---> createTransport");
        return new TruckTransport();
    }
}
