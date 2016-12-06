package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.scottcrocker.packify.controller.DBHandler;
import com.scottcrocker.packify.helper.GPSHelper;

/**
 * Main activity, it's sole purpose is to declare and assign public static variables and redirect the user based on login status.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    SharedPreferences sharedPreferences;

    /**
     * DBHandler variable, is used in a lot of places to query and edit the database.
     */
    public static DBHandler db;
    /**
     * GPSHelper variable, is used in a few places to get longitude and latitude based on addresses.
     */
    public static GPSHelper gps;
    /**
     * SHARED_PREFERENCES constant, is used for SharedPreferences all over the app.
     */
    public static final String SHARED_PREFERENCES = "PackifySharedPreferences";
    /**
     * An int variable for storing the currently logged in user for usage everywhere, is assigned in LoginActivity.
     */
    public static int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBHandler(this);
        gps = new GPSHelper(this);
        Log.d(TAG, "Started");
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            Intent intent = new Intent(this, ActiveOrdersActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
