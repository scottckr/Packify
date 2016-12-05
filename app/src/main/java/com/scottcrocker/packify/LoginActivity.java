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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.scottcrocker.packify.helper.ValidationHelper;

import java.util.ArrayList;
import java.util.List;

import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;
import static com.scottcrocker.packify.MainActivity.db;

public class LoginActivity extends AppCompatActivity {

    EditText inputPasswordEt;
    EditText inputIdEt;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ValidationHelper validationHelper = new ValidationHelper();
    List<Boolean> isValidInput = new ArrayList<>();
    boolean userExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    public void loginValidation(View view) {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        inputIdEt = (EditText) findViewById(R.id.input_login_id);
        String inputId = inputIdEt.getText().toString();
        isValidInput.add(validationHelper.validateInputNumber(inputId, "Användar id" ,this));

        inputPasswordEt = (EditText) findViewById(R.id.input_login_password);
        String inputPassword = inputPasswordEt.getText().toString();
        isValidInput.add(validationHelper.validateInputText(inputPassword, "Lösenord" ,this));
        userExist = false;
        int currentUserId;
        if (validationHelper.isAllTrue(isValidInput)){

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
                    } else {
                        Toast.makeText(this, "Felaktigt lösenord, försök igen", Toast.LENGTH_SHORT).show();
                        editor.putBoolean("isLoggedIn", false);
                        editor.apply();
                        inputPasswordEt.setText("");
                    }
                }

            }if(!userExist){
                Toast.makeText(this, "Felaktigt användarnamn, försök igen", Toast.LENGTH_SHORT).show();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                inputPasswordEt.setText("");
            }

        }
        isValidInput.clear();

    }
}
