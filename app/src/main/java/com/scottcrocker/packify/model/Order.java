package com.scottcrocker.packify.model;

import java.util.Date;

/**
 * Created by mavve on 2016-11-11.
 */

public class Order {
    private int customerNo;
    private String address;
    private int orderSum;
    private static int orderNo;
    private boolean delivered;
    private Date deliveryDate;
    private double latitude;
    private double longitude;

    public static int getOrderNo() {
        return orderNo;
    }
}
