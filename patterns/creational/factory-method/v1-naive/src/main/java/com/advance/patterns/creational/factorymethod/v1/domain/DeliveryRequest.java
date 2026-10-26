package com.advance.patterns.creational.factorymethod.v1.domain;

public record DeliveryRequest(String orderId, String destination) {

    public DeliveryRequest {
        orderId = requireText(orderId, "orderId");
        destination = requireText(destination, "destination");
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }
}
