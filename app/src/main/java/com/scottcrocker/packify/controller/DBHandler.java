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

    public void addOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cvs = new ContentValues();
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
        cvs.put("longitude", order.getLongitude());
        cvs.put("latitude", order.getLatitude());

        long id = db.insert("Orders", null, cvs);
        db.close();
        Log.d("DATABASE", "Added order: " + id);
    }

    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cvs = new ContentValues();
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

    public Order getOrder(Order orderToGet) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM Orders WHERE _orderNo=?";

        Cursor cursor = db.rawQuery(selectQuery, null);

        Order order = new Order();

        boolean isDeliveredBool;


        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            order.setCustomerNo(Integer.parseInt(cursor.getString(1)));
            order.setAddress(cursor.getString(2));
            order.setOrderSum(Integer.parseInt(cursor.getString(3)));
            order.setDeliveryDate(cursor.getString(4));
            if (cursor.getInt(5) == 1) {
                isDeliveredBool = true;
            } else {
                isDeliveredBool = false;
            }
            order.setIsDelivered(isDeliveredBool);
            order.setLongitude(0.0);
            order.setLatitude(0.0);
        } else {
            order = null;
        }

        db.close();
        return order;
    }

    public User getUser(User userToGet) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT _id, password, name, telephone, isAdmin " +
                "FROM Orders WHERE _id =?";

        User user = new User();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userToGet.getId())});

        if (cursor.moveToFirst()) {
            do {
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                user.setName(cursor.getString(cursor.getColumnIndex("name")));
                user.setTelephone(cursor.getInt(cursor.getColumnIndex("telephone")));
                boolean isAdminBool;
                if (cursor.getInt(cursor.getColumnIndex("isAdmin")) == 1) {
                    isAdminBool = true;
                } else {
                    isAdminBool = false;
                }
                user.setIsAdmin(isAdminBool);
            } while (cursor.moveToNext());
        }
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
