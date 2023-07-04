package com.driver;

public class OrderNotFound extends RuntimeException{
    public OrderNotFound(String id) {
        super("Order not found: " + id);
    }
}