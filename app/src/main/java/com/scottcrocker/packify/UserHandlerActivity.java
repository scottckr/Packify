package com.scottcrocker.packify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class UserHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_handler);
    }

    /**
     * Method to create a new user object, or handle an existing user object which will be sent to DB
     * @param view
     */
    // TO-DO: create new object containing user information, send to database
    public void save_user(View view) {
        Toast toast = new Toast(this);

        Toast.makeText(getApplicationContext(), "Användare sparad", Toast.LENGTH_SHORT).show();
    }

    /**
     * Method to delete user from DB
     * @param view
     */
    // TO-DO: method shall delete user information in database
    public void delete_user(View view) {
        Toast toast = new Toast(this);

        Toast.makeText(getApplicationContext(), "Användare raderad", Toast.LENGTH_SHORT).show();
    }
}
