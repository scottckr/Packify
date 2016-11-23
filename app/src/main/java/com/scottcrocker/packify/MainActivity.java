package com.scottcrocker.packify;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import com.scottcrocker.packify.controller.DBHandler;
import com.scottcrocker.packify.helper.GPSHelper;
import com.scottcrocker.packify.model.User;





public class MainActivity extends AppCompatActivity {
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
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);


        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            Intent intent = new Intent(this, ActiveOrdersActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
