package com.scottcrocker.packify.model;

import java.util.Date;

/**
 * Created by mavve on 2016-11-11.
 */

public class Order {

    public Order(String address, int orderSum, boolean delivered, Date deliveryDate, double latitude, double longitude, int customerNo) {
        this.address = address;
        this.orderSum = orderSum;
        this.delivered = delivered;
        this.deliveryDate = deliveryDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.customerNo = customerNo;
    }

    private String address;
    private int orderSum;
    private static int orderNo;
    private boolean delivered;
    private Date deliveryDate;
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

    public static void setOrderNo(int orderNo) {
        Order.orderNo = orderNo;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
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


    public static int getOrderNo() {
        return orderNo;
    }
}
