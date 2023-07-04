package com.driver;

public class DeliveryPartnerNotFound extends RuntimeException {
    public DeliveryPartnerNotFound(String id) {
        super("Delivery Partner not found: " + id);
    }
}