package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.scottcrocker.packify.controller.DBHandler;
import static com.scottcrocker.packify.SettingsActivity.SHARED_PREFERENCES;

import static com.scottcrocker.packify.SettingsActivity.SHARED_PREFERENCES;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    public static DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBHandler(this);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);

        if (sharedPreferences.getBoolean("isLoggedIn", false) == true) {
            Intent intent = new Intent(this, ActiveOrdersActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
