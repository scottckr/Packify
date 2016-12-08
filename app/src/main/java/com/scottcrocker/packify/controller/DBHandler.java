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
 * Database class for handling all SQLite queries.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = "DATABASE";

    /**
     * Constructor for DBHandler, creates a database called PackifyDB.
     *
     * @param context Context of the app.
     */
    public DBHandler(Context context) {
        super(context, "PackifyDB", null, 13);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String orderSql = "CREATE TABLE Orders (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "orderNo INTEGER NOT NULL, customerNo INTEGER NOT NULL, customerName TEXT NOT NULL, " +
                "address TEXT NOT NULL, postAddress TEXT NOT NULL, orderSum INTEGER NOT NULL, " +
                "deliveryDate TEXT NOT NULL, isDelivered INTEGER NOT NULL, deliveredBy TEXT NOT NULL, " +
                "longitude REAL NOT NULL, latitude REAL NOT NULL, signature BLOB);";
        String userSql = "CREATE TABLE Users (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER NOT NULL, password TEXT NOT NULL, name TEXT NOT NULL, " +
                "telephone TEXT NOT NULL, isAdmin INTEGER NOT NULL);";
        String createAdminSql = "INSERT INTO Users VALUES (1, 0, 'admin', 'ADMIN', '0', 1)";
        db.execSQL(orderSql);
        db.execSQL(userSql);
        db.execSQL(createAdminSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String orderSql = "DROP TABLE IF EXISTS Orders;";
        String userSql = "DROP TABLE IF EXISTS Users;";
        db.execSQL(orderSql);
        db.execSQL(userSql);
        onCreate(db);
        Log.d(TAG, "Database updated!");
    }

    /**
     * Adds an order to the database.
     * Takes an Order object and extracts its variables and puts them into the Orders table.
     *
     * @param order Order object to add to the database.
     */
    public void addOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cvs = new ContentValues();
        cvs.put("orderNo", order.getOrderNo());
        cvs.put("customerNo", order.getCustomerNo());
        cvs.put("customerName", order.getCustomerName());
        cvs.put("address", order.getAddress());
        cvs.put("postAddress", order.getPostAddress());
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
        cvs.put("signature", order.getSignature());

        long id = db.insert("Orders", null, cvs);
        db.close();
        Log.d(TAG, "Added order: " + id);
    }

    /**
     * Adds a User to the database.
     * Takes a User object and extracts its variables and puts them into the Users table.
     *
     * @param user User object to add to the database.
     */
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
        Log.d(TAG, "Added user: " + id);
    }

    /**
     * Edits an Order already in the database.
     * Takes an Order object, extracts its variables and updates the Order object which corresponds to the 'orderNo' variable.
     *
     * @param order Takes an Order object to edit.
     */
    public void editOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cvs = new ContentValues();
        cvs.put("orderNo", order.getOrderNo());
        cvs.put("customerNo", order.getCustomerNo());
        cvs.put("customerName", order.getCustomerName());
        cvs.put("address", order.getAddress());
        cvs.put("postAddress", order.getPostAddress());
        cvs.put("orderSum", order.getOrderSum());
        cvs.put("deliveryDate", order.getDeliveryDate());
        cvs.put("isDelivered", order.getIsDelivered());
        cvs.put("deliveredBy", order.getDeliveredBy());
        cvs.put("longitude", order.getLongitude());
        cvs.put("latitude", order.getLatitude());
        cvs.put("signature", order.getSignature());

        Log.d(TAG, "Order updated: " + cvs);

        db.update("Orders", cvs, "orderNo" + " = ?", new String[]{String.valueOf(order.getOrderNo())});
    }

    /**
     * Edits a User already in the database.
     * Takes a User object, extracts its variables and updates the User object which corresponds to the 'userId' variable.
     *
     * @param user Takes a User object to edit.
     */
    public void editUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cvs = new ContentValues();
        cvs.put("userId", user.getId());
        cvs.put("password", user.getPassword());
        cvs.put("name", user.getName());
        cvs.put("telephone", user.getTelephone());
        cvs.put("isAdmin", user.getIsAdmin());

        db.update("Users", cvs, "userId" + " = ?", new String[]{String.valueOf(user.getId())});
    }

    /**
     * Gets an Order object from the database.
     * Queries the Orders table for a row where the 'orderNo' is equal to the 'orderNo' input.
     *
     * @param orderNo Selects which 'orderNo' to use when querying the database.
     * @return Returns the Order which has an 'orderNo' equal to the 'orderNo' input.
     */
    public Order getOrder(int orderNo) {
        String query = "SELECT * FROM " + "Orders" + " WHERE " + "orderNo" + " = \"" + orderNo + "\"";

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Order order = new Order();
        boolean isDeliveredBool;

        if (cursor.moveToFirst()) {
            order.setOrderNo(cursor.getInt(1));
            order.setCustomerNo(cursor.getInt(2));
            order.setCustomerName(cursor.getString(3));
            order.setAddress(cursor.getString(4));
            order.setPostAddress(cursor.getString(5));
            order.setOrderSum(cursor.getInt(6));
            order.setDeliveryDate(cursor.getString(7));
            isDeliveredBool = cursor.getInt(8) == 1;
            order.setIsDelivered(isDeliveredBool);
            order.setDeliveredBy(cursor.getString(9));
            order.setLongitude(cursor.getDouble(10));
            order.setLatitude(cursor.getDouble(11));
            order.setSignature(cursor.getBlob(12));
            cursor.close();
        } else {
            order = null;
        }
        db.close();
        return order;
    }

    /**
     * Gets a User object from the database.
     * Queries the Users table for a row where the 'userId' is equal to the 'userId' input.
     *
     * @param userId Selects which 'userId' to use when querying the database.
     * @return Returns the User which has a 'userId' equal to the 'userId' input.
     */
    public User getUser(int userId) {
        String query = "SELECT * FROM " + "Users" + " WHERE " + "userId" + " = \"" + userId + "\"";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        User user = new User();
        boolean isAdminBool;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user.setId(cursor.getInt(1));
            user.setPassword(cursor.getString(2));
            user.setName(cursor.getString(3));
            user.setTelephone(cursor.getString(4));
            isAdminBool = cursor.getInt(5) == 1;
            user.setIsAdmin(isAdminBool);
            cursor.close();
        } else {
            user = null;
        }
        db.close();
        return user;
    }

    /**
     * Gets all orders from the Orders table in the database.
     *
     * @return Returns a List of all Order objects in the Orders table.
     */
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
                order.setCustomerName(cursor.getString(3));
                order.setAddress(cursor.getString(4));
                order.setPostAddress(cursor.getString(5));
                order.setOrderSum(cursor.getInt(6));
                order.setDeliveryDate(cursor.getString(7));
                if (cursor.getInt(8) == 1) {
                    order.setIsDelivered(true);
                } else {
                    order.setIsDelivered(false);
                }
                order.setDeliveredBy(cursor.getString(9));
                order.setLongitude(cursor.getDouble(10));
                order.setLatitude(cursor.getDouble(11));
                order.setSignature(cursor.getBlob(12));

                allOrders.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();

        Log.d(TAG, "Got orders: " + allOrders);

        return allOrders;
    }

    /**
     * Gets all users from the Users table in the database.
     *
     * @return Returns a List of all User objects in the Users table.
     */
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
                user.setTelephone(cursor.getString(4));
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

    /**
     * Deletes an Order from the database.
     *
     * @param order Takes an Order object and deletes the row in the Orders table which has an 'orderNo' equal to the 'orderNo' of the Order input.
     */
    public void deleteOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Orders", "orderNo = ?", new String[]{String.valueOf(order.getOrderNo())});
        db.close();
    }

    /**
     * Deletes a User from the database.
     *
     * @param user Takes a User object and deletes the row in the Users table which has a 'userId' equal to the 'userId' of the User input.
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Users", "userId = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * Checks if someone is trying to add something to the database which already exists.
     *
     * @param tableName Which table to add to the query.
     * @param fieldName Which column to look at in the query.
     * @param input     What value to look for in the 'fieldName'.
     * @return Returns true if 'input' already exists in 'fieldName'.
     */
    public boolean doesFieldExist(String tableName, String fieldName, String input) {
        SQLiteDatabase db = getReadableDatabase();
        if (!input.equals("")) {
            String query = "SELECT * FROM " + tableName + " WHERE " + fieldName + " = " + input;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }
            cursor.close();
            return true;
        } else {
            return false;
        }
    }
}
