package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.scottcrocker.packify.controller.DBHandler;
import com.scottcrocker.packify.helper.GPSHelper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    SharedPreferences sharedPreferences;

    public static DBHandler db;
    public static GPSHelper gps;
    public static final String SHARED_PREFERENCES = "PackifySharedPreferences";
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
