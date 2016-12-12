package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.scottcrocker.packify.controller.DBHandler;
import com.scottcrocker.packify.helper.ValidationHelper;

/**
 * MainActivity, its sole purpose is to declare and assign public static variables and redirect the user based on login status.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    SharedPreferences sharedPreferences;

    /**
     * ValidationHelper variable, is used to validate input fields in several activities.
     */
    public static ValidationHelper validationHelper;

    /**
     * DBHandler variable, is used in a lot of places to query and edit the database.
     */
    public static DBHandler db;

    /**
     * SHARED_PREFERENCES constant, is used for SharedPreferences all over the app.
     */
    public static final String SHARED_PREFERENCES = "PackifySharedPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBHandler(this);
        validationHelper = new ValidationHelper();
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
