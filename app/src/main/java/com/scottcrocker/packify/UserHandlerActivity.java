package com.scottcrocker.packify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

public class UserHandlerActivity extends AppCompatActivity {

    Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_handler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSpinner = (Spinner) findViewById(R.id.spinner_user_id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.toolbar_update_order:
                //TODO Update view.
                return true;

            case R.id.toolbar_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.toolbar_admin_settings:
                intent = new Intent(this, UserHandlerActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Method to create a new user object, or handle an existing user object which will be sent to DB
     * @param view
     */
    // TODO: create new object containing user information, send to database
    public void save_user(View view) {
        Toast toast = new Toast(this);

        Toast.makeText(getApplicationContext(), "Användare sparad", Toast.LENGTH_SHORT).show();
    }

    /**
     * Method to delete user from DB
     * @param view
     */
    // TODO: method shall delete user information in database
    public void delete_user(View view) {
        Toast toast = new Toast(this);

        Toast.makeText(getApplicationContext(), "Användare raderad", Toast.LENGTH_SHORT).show();
    }
}
