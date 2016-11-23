package com.scottcrocker.packify.model;

/**
 * Created by mavve on 2016-11-11.
 */

public class Order {
    private String address;
    private int orderSum;
    private int orderNo;
    private boolean isDelivered;
    private String deliveryDate;
    private double latitude;
    private double longitude;
    private int customerNo;
    private int deliveredBy;
    private String customerName;

    public Order() {
    }

    public Order(int orderNo, int customerNo, String customerName, String address, int orderSum,
                 String deliveryDate, boolean isDelivered, int deliveredBy, double longitude, double latitude) {
        this.orderNo = orderNo;
        this.address = address;
        this.customerName = customerName;
        this.orderSum = orderSum;
        this.isDelivered = isDelivered;
        this.deliveryDate = deliveryDate;
        this.deliveredBy = deliveredBy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.customerNo = customerNo;
    }

    public String toString() {
        return "Order number: " + orderNo + ", name: " + customerName;
    }

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
        this.orderNo = orderNo;
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

    public void setDeliveredBy(int deliveredBy) {
        this.deliveredBy = deliveredBy;
    }

    public int getDeliveredBy() {
        return deliveredBy;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }
}
