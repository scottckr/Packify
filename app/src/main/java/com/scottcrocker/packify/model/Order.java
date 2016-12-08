package com.scottcrocker.packify.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Order model class.
 */
public class Order {

    private String address;
    private String postAddress;
    private int orderSum;
    private int orderNo;
    private boolean isDelivered;
    private String deliveryDate;
    private double latitude;
    private double longitude;
    private int customerNo;
    private String deliveredBy;
    private String customerName;
    private byte[] signature;
    private static List<Order> currentListedOrders = new ArrayList<>();

    /**
     * Empty constructor for Order class.
     */
    public Order() {
    }

    /**
     * Constructor for Order class which takes a lot of arguments.
     *
     * @param orderNo      An order number to put into the orderNo variable.
     * @param customerNo   A customer number to put into the customerNo variable.
     * @param customerName A customer name to put into the customerName variable.
     * @param address      An address to put into the address variable.
     * @param postAddress  A post address to put into the postAddress variable.
     * @param orderSum     An order sum to put into the orderSum variable.
     * @param deliveryDate A delivery date to put into the deliveryDate variable.
     * @param isDelivered  A boolean for determining if an Order is delivered or not, put into the isDelivered variable.
     * @param deliveredBy  A String for determining which User delivered the Order, put into the deliveredBy variable.
     * @param longitude    A longitude to put into the longitude variable.
     * @param latitude     A latitude to put into the latitude variable.
     * @param signature    A byte array for a signature bitmap to put into the signature variable.
     */
    public Order(int orderNo, int customerNo, String customerName, String address, String postAddress, int orderSum,
                 String deliveryDate, boolean isDelivered, String deliveredBy, double longitude, double latitude, byte[] signature) {
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
        this.signature = signature;
    }

    /**
     * A List of currently listed orders in ActiveOrdersActivity.
     *
     * @return Returns a List of Order objects currently in the ListView in ActiveOrdersActivity.
     */
    public static List<Order> getCurrentListedOrders() {
        return currentListedOrders;
    }

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

    public void setDeliveredBy(String deliveredBy) {
        this.deliveredBy = deliveredBy;
    }

    public String getDeliveredBy() {
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

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public byte[] getSignature() {
        return signature;
    }
}
