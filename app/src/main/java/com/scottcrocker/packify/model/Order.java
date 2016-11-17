package com.scottcrocker.packify.model;

import java.util.Date;

/**
 * Created by mavve on 2016-11-11.
 */

public class Order {
    public Order() {

    }

    public Order(int customerNo, String address, int orderSum, String deliveryDate, boolean isDelivered, double longitude, double latitude) {
        this.address = address;
        this.orderSum = orderSum;
        this.isDelivered = isDelivered;
        this.deliveryDate = deliveryDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.customerNo = customerNo;
    }

    private String address;
    private int orderSum;
    private static int orderNo;
    private boolean isDelivered;
    private String deliveryDate;
    private double latitude;
    private double longitude;
    private int customerNo;

    public int getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(int customerNo) {
        this.customerNo = customerNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(int orderSum) {
        this.orderSum = orderSum;
    }

    public void setOrderNo(int orderNo) {
        Order.orderNo = orderNo;
    }

    public boolean getIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(boolean isDelivered) {
        this.isDelivered = isDelivered;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getOrderNo() {
        return orderNo;
    }
}
