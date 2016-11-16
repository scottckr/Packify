package com.scottcrocker.packify.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.scottcrocker.packify.model.Order;
import com.scottcrocker.packify.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tobiashillen on 2016-11-11.
 */

public class DBHandler extends SQLiteOpenHelper {

    public DBHandler(Context context) {
        super(context, "PackifyDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String orderSql = "CREATE TABLE Orders (_orderNo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "customerNo INTEGER NOT NULL, address TEXT NOT NULL, " +
                "orderSum INTEGER NOT NULL, deliveryDate TEXT NOT NULL, " +
                "isDelivered INTEGER NOT NULL, longitude REAL NOT NULL, latitude REAL NOT NULL);";
        String userSql = "CREATE TABLE Users (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "password TEXT NOT NULL, name TEXT NOT NULL, telephone INTEGER NOT NULL, " +
                "isAdmin INTEGER NOT NULL);";
        db.execSQL(orderSql);
        db.execSQL(userSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String orderSql = "DROP TABLE IF EXISTS Orders;";
        String userSql = "DROP TABLE IF EXISTS Users";
        db.execSQL(orderSql);
        db.execSQL(userSql);
    }

    public void addOrder(int customerNo, String address, int orderSum, String deliveryDate, boolean isDelivered, double longitude, double latitude) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cvs = new ContentValues();
        cvs.put("customerNo", customerNo);
        cvs.put("address", address);
        cvs.put("orderSum", orderSum);
        cvs.put("deliveryDate", deliveryDate);
        cvs.put("isDelivered", isDelivered);
        cvs.put("longitude", longitude);
        cvs.put("latitude", latitude);

        long id = db.insert("Orders", null, cvs);
        Log.d("DATABASE", "Row ID: " + id);

        db.close();
    }

    public void addUser(String password, String name, int telephone, boolean isAdmin) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cvs = new ContentValues();
        cvs.put("password", password);
        cvs.put("name", name);
        cvs.put("telephone", telephone);
        cvs.put("isAdmin", isAdmin);

        long id = db.insert("Users", null, cvs);
        Log.d("DATABASE", "Row ID: " + id);

        db.close();
    }

    public int editOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cvs = new ContentValues();
        cvs.put("customerNo", order.getCustomerNo());
        cvs.put("address", order.getAddress());
        cvs.put("orderSum", order.getOrderSum());
        cvs.put("deliveryDate", order.getDeliveryDate());
        cvs.put("delivered", order.getIsDelivered());
        cvs.put("longitude", order.getLongitude());
        cvs.put("latitude", order.getLatitude());

        return db.update("Orders", cvs, "_orderNo" + " = ?", new String[]{String.valueOf(order.getOrderNo())});
    }

    public int editUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cvs = new ContentValues();
        cvs.put("password", user.getPassword());
        cvs.put("name", user.getName());
        cvs.put("telephone", user.getTelephone());
        cvs.put("isAdmin", user.getIsAdmin());

        return db.update("Users", cvs, "_id" + " = ?", new String[]{String.valueOf(user.getId())});
    }

    public Order getOrder(int orderNo) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Orders", new String[] {"_orderNo", "customerNo", "address",
                "orderSum", "deliveryDate", "isDelivered", "longitude", "latitude"}, "_orderNo =?",
                new String[]{String.valueOf(orderNo)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        boolean isDelivered;
        if (Integer.parseInt(cursor.getString(5)) == 1) {
            isDelivered = true;
        } else {
            isDelivered = false;
        }
        Order order = new Order(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2),
                Integer.parseInt(cursor.getString(3)), cursor.getString(4), isDelivered,
                cursor.getDouble(6), cursor.getDouble(7));

        cursor.close();

        return order;
    }

    public User getUser(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Users", new String[] {"_id", "password", "name",
                        "telephone", "isAdmin"}, "_id =?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        boolean isAdmin;
        if (Integer.parseInt(cursor.getString(4)) == 1) {
            isAdmin = true;
        } else {
            isAdmin = false;
        }
        User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),
                Integer.parseInt(cursor.getString(3)), isAdmin);

        cursor.close();

        return user;
    }

    public List<Order> getAllOrders() {
        List<Order> allOrders = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM Orders";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setOrderNo(Integer.parseInt(cursor.getString(0)));
                order.setCustomerNo(Integer.parseInt(cursor.getString(1)));
                order.setAddress(cursor.getString(2));
                order.setOrderSum(Integer.parseInt(cursor.getString(3)));
                order.setDeliveryDate(cursor.getString(4));
                if (Integer.parseInt(cursor.getString(5)) == 1) {
                    order.setIsDelivered(true);
                } else {
                    order.setIsDelivered(false);
                }
                order.setLongitude(cursor.getDouble(6));
                order.setLatitude(cursor.getDouble(7));

                allOrders.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();

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
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setPassword(cursor.getString(1));
                user.setName(cursor.getString(2));
                user.setTelephone(Integer.parseInt(cursor.getString(3)));
                if (Integer.parseInt(cursor.getString(4)) == 1) {
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
        db.delete("Orders", "_orderNo = ?", new String[]{String.valueOf(order.getOrderNo())});
        db.close();
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Users", "_id = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }
}
