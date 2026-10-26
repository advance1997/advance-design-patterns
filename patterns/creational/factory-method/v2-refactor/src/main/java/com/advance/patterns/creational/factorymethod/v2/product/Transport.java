package com.advance.patterns.creational.factorymethod.v2.product;

import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryRequest;

public interface Transport {

    DeliveryReceipt deliver(DeliveryRequest request);
}
