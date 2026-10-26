package com.advance.patterns.creational.factorymethod.v3.creator;

import com.advance.patterns.creational.factorymethod.v3.product.ShipTransport;
import com.advance.patterns.creational.factorymethod.v3.product.Transport;

public final class SeaLogistics extends Logistics {

    @Override
    protected Transport createTransport() {
        System.out.println("[factory-method/v3] SeaLogistics ---> createTransport");
        return new ShipTransport();
    }
}
