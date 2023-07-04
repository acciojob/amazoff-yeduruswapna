package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.*;

@Repository
public class OrderRepository {
    HashMap<String, Order> orders;
    HashMap<String, DeliveryPartner> deliveryPartners;
    HashMap<String, List<String>> ordersOfPartnerMap;
    HashSet<String> assingedOrders;

    public OrderRepository() {
        orders = new HashMap<>();
        deliveryPartners = new HashMap<>();
        ordersOfPartnerMap = new HashMap<>();
        assingedOrders = new HashSet<>();

    }

    public void addOrder(Order order) {
        orders.put(order.getId(), order);
        assingedOrders.add(order.getId());
    }

    public void addPartner(String partnerId) {
        DeliveryPartner d1 = new DeliveryPartner(partnerId);
        deliveryPartners.put(partnerId, d1);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        List<String> list = ordersOfPartnerMap.getOrDefault(partnerId, new ArrayList<>());
        list.add(orderId);
        ordersOfPartnerMap.put(partnerId, list);
        deliveryPartners.get(partnerId).setNumberOfOrders(deliveryPartners.get(partnerId).getNumberOfOrders() + 1);

        assingedOrders.remove(orderId);
    }

    public Order getOrderById(String orderId) {
        return orders.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartners.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return ordersOfPartnerMap.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return ordersOfPartnerMap.getOrDefault(partnerId, new ArrayList<>());
    }

    public List<String> getAllOrders() {
        return new ArrayList<>(orders.keySet());
    }

    public Integer getCountOfUnassignedOrders() {
        return assingedOrders.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        Integer count = 0;
        // converting given string time to integer
        String[] arr = time.split(":"); // 12:45
        int hr = Integer.parseInt(arr[0]);
        int min = Integer.parseInt(arr[1]);

        int total = (hr * 60 + min);

        List<String> list = ordersOfPartnerMap.getOrDefault(partnerId, new ArrayList<>());
        if (list.size() == 0)
            return 0; // no order assigned to partnerId

        for (String s : list) {
            Order currentOrder = orders.get(s);
            if (currentOrder.getDeliveryTime() > total) {
                count++;
            }
        }
        return count;

    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        // return in HH:MM format
        String str = "00:00";
        int max = 0;

        List<String> list = ordersOfPartnerMap.getOrDefault(partnerId, new ArrayList<>());
        if (list.size() == 0)
            return str;
        for (String s : list) {
            Order currentOrder = orders.get(s);
            max = Math.max(max, currentOrder.getDeliveryTime());
        }
        // convert int to string (140-> 02:20)
        int hr = max / 60;
        int min = max % 60;

        if (hr < 10) {
            str = "0" + hr + ":";
        } else {
            str = hr + ":";
        }

        if (min < 10) {
            str += "0" + min;
        } else {
            str += min;
        }
        return str;
    }

    public void deletePartnerById(String partnerId) {
        if (!ordersOfPartnerMap.isEmpty()) {
            assingedOrders.addAll(ordersOfPartnerMap.get(partnerId));
        }
        deliveryPartners.remove(partnerId);
        ordersOfPartnerMap.remove(partnerId);

    }

    public void deleteOrderById(String orderId) {
        if (orders.containsKey(orderId)) {
            if (assingedOrders.contains(orderId)) {
                assingedOrders.remove(orderId);
            } else {
                for (String str : ordersOfPartnerMap.keySet()) {
                    List<String> list = ordersOfPartnerMap.get(str);
                    list.remove(orderId);
                }
            }
        }

    }
}