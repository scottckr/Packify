package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.scottcrocker.packify.helper.ValidationHelper;

import java.util.ArrayList;
import java.util.List;

import static com.scottcrocker.packify.MainActivity.validationHelper;
import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;
import static com.scottcrocker.packify.MainActivity.db;

/**
 * LoginActivity, lets the user sign in to the app.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText inputPasswordEt;
    private List<Boolean> isValidInput = new ArrayList<>();
    private boolean userExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userExist = false;

        inputPasswordEt = (EditText) findViewById(R.id.input_login_password);
        inputPasswordEt.setTypeface(Typeface.DEFAULT);
        inputPasswordEt.setTransformationMethod(new PasswordTransformationMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method validates the user input in the login fields. If valid, the user is logged in and
     * ActiveOrdersActivity is started via Main.
     *
     * @param view The view component that is executed by click handler.
     */
    public void loginValidation(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        EditText inputIdEt = (EditText) findViewById(R.id.input_login_id);
        String inputId = inputIdEt.getText().toString();
        isValidInput.add(validationHelper.validateInputNumber(inputId, "Användar-ID", this));

        inputPasswordEt = (EditText) findViewById(R.id.input_login_password);
        String inputPassword = inputPasswordEt.getText().toString();
        isValidInput.add(validationHelper.validateInputText(inputPassword, "Lösenord", this));
        userExist = false;
        int currentUserId;
        if (ValidationHelper.isAllTrue(isValidInput)) {

            for (int i = 0; i < db.getAllUsers().size(); i++) {
                if (db.getAllUsers().get(i).getId() == Integer.parseInt(inputId)) {
                    userExist = true;
                    if (db.getAllUsers().get(i).getPassword().equals(inputPassword)) {
                        currentUserId = db.getAllUsers().get(i).getId();
                        editor.putInt("USERID", currentUserId);
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        isValidInput.clear();
                        finish();
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.toast_login_password), Toast.LENGTH_SHORT).show();
                        editor.putBoolean("isLoggedIn", false);
                        editor.apply();
                        inputPasswordEt.setText("");
                    }
                }
            }
            if (!userExist) {
                Toast.makeText(this, getResources().getString(R.string.toast_login_username), Toast.LENGTH_SHORT).show();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                inputPasswordEt.setText("");
            }
        }
        isValidInput.clear();
    }
}
