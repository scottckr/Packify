package com.scottcrocker.packify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.scottcrocker.packify.controller.DBHandler;
import com.scottcrocker.packify.helper.GPSHelper;

public class MainActivity extends AppCompatActivity {

    public static DBHandler db;
    public static GPSHelper gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBHandler(getApplicationContext());
        if (LoginActivity.isLoggedIn) {
            Intent intent = new Intent(this, ActiveOrdersActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
