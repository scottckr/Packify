package com.scottcrocker.packify.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mavve on 2016-11-11.
 */

public class Order implements Parcelable{
    private String address;
    private String postAddress;
    private int orderSum;
    private int orderNo;
    private boolean isDelivered;
    private String deliveryDate;
    private double latitude;
    private double longitude;
    private int customerNo;
    private int deliveredBy;
    private String customerName;
    List<Order> orderList = new ArrayList<>();
    static List<Order> currentListedOrders = new ArrayList<>();

    public Order() {
    }

    public static List<Order> getCurrentListedOrders() {
        return currentListedOrders;
    }

    public static void setCurrentListedOrders(List<Order> currentListedOrders) {
        Order.currentListedOrders = currentListedOrders;
    }

    public Order(int orderNo, int customerNo, String customerName, String address, String postAddress, int orderSum,
                 String deliveryDate, boolean isDelivered, int deliveredBy, double longitude, double latitude) {
        this.orderNo = orderNo;
        this.address = address;
        this.postAddress = postAddress;
        this.customerName = customerName;
        this.orderSum = orderSum;
        this.isDelivered = isDelivered;
        this.deliveryDate = deliveryDate;
        this.deliveredBy = deliveredBy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.customerNo = customerNo;
    }

    protected Order(Parcel in) {
        address = in.readString();
        postAddress = in.readString();
        orderSum = in.readInt();
        orderNo = in.readInt();
        isDelivered = in.readByte() != 0x00;
        deliveryDate = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        customerNo = in.readInt();
        deliveredBy = in.readInt();
        customerName = in.readString();
    }


    @Override
    public int describeContents(){
        return 0;
    }


    @Override
    public void writeToParcel(Parcel out, int flag){
        out.writeString(address);
        out.writeString(postAddress);
        out.writeInt(orderSum);
        out.writeInt(orderNo);
        out.writeByte((byte) (isDelivered ? 0x01 : 0x00));
        out.writeString(deliveryDate);
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        out.writeInt(customerNo);
        out.writeInt(deliveredBy);
        out.writeString(customerName);
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>(){
        public Order createFromParcel(Parcel in) {
                return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
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

    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress;
    }

    public String getPostAddress() {
        return postAddress;
    }
}
