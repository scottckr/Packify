package com.scottcrocker.packify.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.scottcrocker.packify.MainActivity;
import com.scottcrocker.packify.model.Order;
import com.scottcrocker.packify.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tobiashillen on 2016-11-11.
 */

public class DBHandler extends SQLiteOpenHelper {

    public DBHandler(Context context) {
        super(context, "PackifyDB", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String orderSql = "CREATE TABLE Orders (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "orderNo INTEGER NOT NULL, customerNo INTEGER NOT NULL, address TEXT NOT NULL, " +
                "orderSum INTEGER NOT NULL, deliveryDate TEXT NOT NULL, " +
                "isDelivered INTEGER NOT NULL, deliveredBy INTEGER NOT NULL, " +
                "longitude REAL NOT NULL, latitude REAL NOT NULL);";
        String userSql = "CREATE TABLE Users (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER NOT NULL, password TEXT NOT NULL, name TEXT NOT NULL, " +
                "telephone INTEGER NOT NULL, isAdmin INTEGER NOT NULL);";
        db.execSQL(orderSql);
        db.execSQL(userSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String orderSql = "DROP TABLE IF EXISTS Orders;";
        String userSql = "DROP TABLE IF EXISTS Users;";
        db.execSQL(orderSql);
        db.execSQL(userSql);
        onCreate(db);
        Log.d("DATABASE", "Database updated!");
    }

    public void addOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cvs = new ContentValues();
        cvs.put("orderNo", order.getOrderNo());
        cvs.put("customerNo", order.getCustomerNo());
        cvs.put("address", order.getAddress());
        cvs.put("orderSum", order.getOrderSum());
        cvs.put("deliveryDate", order.getDeliveryDate());
        int isDeliveredInt;
        if (order.getIsDelivered()) {
            isDeliveredInt = 1;
        } else {
            isDeliveredInt = 0;
        }
        cvs.put("isDelivered", isDeliveredInt);
        cvs.put("deliveredBy", order.getDeliveredBy());
        cvs.put("longitude", order.getLongitude());
        cvs.put("latitude", order.getLatitude());

        long id = db.insert("Orders", null, cvs);
        db.close();
        Log.d("DATABASE", "Added order: " + id);
    }

    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cvs = new ContentValues();
        cvs.put("userId", user.getId());
        cvs.put("password", user.getPassword());
        cvs.put("name", user.getName());
        cvs.put("telephone", user.getTelephone());
        int isAdminInt;
        if (user.getIsAdmin()) {
            isAdminInt = 1;
        } else {
            isAdminInt = 0;
        }
        cvs.put("isAdmin", isAdminInt);

        long id = db.insert("Users", null, cvs);
        db.close();
        Log.d("DATABASE", "Added user: " + id);
    }

    public int editOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cvs = new ContentValues();
        cvs.put("orderNo", order.getOrderNo());
        cvs.put("customerNo", order.getCustomerNo());
        cvs.put("address", order.getAddress());
        cvs.put("orderSum", order.getOrderSum());
        cvs.put("deliveryDate", order.getDeliveryDate());
        cvs.put("isDelivered", order.getIsDelivered());
        cvs.put("deliveredBy", order.getDeliveredBy());
        cvs.put("longitude", order.getLongitude());
        cvs.put("latitude", order.getLatitude());

        return db.update("Orders", cvs, "id" + " = ?", new String[]{String.valueOf(order.getOrderNo())});
    }

    public int editUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cvs = new ContentValues();
        cvs.put("userId", user.getId());
        cvs.put("password", user.getPassword());
        cvs.put("name", user.getName());
        cvs.put("telephone", user.getTelephone());
        cvs.put("isAdmin", user.getIsAdmin());

        return db.update("Users", cvs, "id" + " = ?", new String[]{String.valueOf(user.getId())});
    }

    public Order getOrder(int orderNo) {
        String query = "SELECT * FROM " + "Orders" + " WHERE " + "orderNo" + " = \"" + orderNo + "\"";

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Order order = new Order();
        boolean isDeliveredBool;

        if (cursor.moveToFirst()) {
            order.setOrderNo(cursor.getInt(1));
            order.setCustomerNo(cursor.getInt(2));
            order.setAddress(cursor.getString(3));
            order.setOrderSum(cursor.getInt(4));
            order.setDeliveryDate(cursor.getString(5));
            if (cursor.getInt(6) == 1) {
                isDeliveredBool = true;
            } else {
                isDeliveredBool = false;
            }
            order.setIsDelivered(isDeliveredBool);
            order.setDeliveredBy(cursor.getInt(7));
            order.setLongitude(cursor.getDouble(8));
            order.setLatitude(cursor.getDouble(9));
            cursor.close();
        } else {
            order = null;
        }
        db.close();
        return order;
    }

    public User getUser(int id) {
        String query = "SELECT * FROM " + "Users" + " WHERE " + "userId" + " = \"" + id + "\"";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        User user = new User();
        boolean isAdminBool;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user.setId(cursor.getInt(1));
            user.setPassword(cursor.getString(2));
            user.setName(cursor.getString(3));
            user.setTelephone(cursor.getInt(4));
            if (cursor.getInt(5) == 1) {
                isAdminBool = true;
            } else {
                isAdminBool = false;
            }
            user.setIsAdmin(isAdminBool);
            cursor.close();
        } else {
            user = null;
        }
        db.close();
        return user;
    }

    public List<Order> getAllOrders() {
        SQLiteDatabase db = getReadableDatabase();
        List<Order> allOrders = new ArrayList<>();

        String selectQuery = "SELECT * FROM Orders";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setOrderNo(cursor.getInt(1));
                order.setCustomerNo(cursor.getInt(2));
                order.setAddress(cursor.getString(3));
                order.setOrderSum(cursor.getInt(4));
                order.setDeliveryDate(cursor.getString(5));
                if (cursor.getInt(6) == 1) {
                    order.setIsDelivered(true);
                } else {
                    order.setIsDelivered(false);
                }
                order.setDeliveredBy(cursor.getInt(7));
                order.setLongitude(cursor.getDouble(8));
                order.setLatitude(cursor.getDouble(9));

                allOrders.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();

        Log.d("DATABASE", "Got orders: " + allOrders);

        return allOrders;
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM Users";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(1));
                user.setPassword(cursor.getString(2));
                user.setName(cursor.getString(3));
                user.setTelephone(cursor.getInt(4));
                if (cursor.getInt(5) == 1) {
                    user.setIsAdmin(true);
                } else {
                    user.setIsAdmin(false);
                }

                allUsers.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return allUsers;
    }

    public void deleteOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Orders", "orderNo = ?", new String[]{String.valueOf(order.getOrderNo())});
        db.close();
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Users", "userId = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }
}