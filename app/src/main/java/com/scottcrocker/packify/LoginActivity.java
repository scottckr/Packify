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

import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;
import static com.scottcrocker.packify.MainActivity.db;

public class LoginActivity extends AppCompatActivity {

    EditText inputPasswordEt;
    EditText inputIdEt;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

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
        int inputId = Integer.parseInt(inputIdEt.getText().toString());
        inputPasswordEt = (EditText) findViewById(R.id.input_login_password);
        String inputPassword = inputPasswordEt.getText().toString();

        int currentUserId;

        for (int i = 0; i < db.getAllUsers().size(); i++) {
            if (db.getAllUsers().get(i).getId() == inputId) {
                if (db.getAllUsers().get(i).getPassword().equals(inputPassword)) {
                    currentUserId = db.getAllUsers().get(i).getId();
                    editor.putInt("USERID", currentUserId);
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();
                } else {
                    Toast.makeText(this, "Lösenordet är felaktigt!", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("isLoggedIn", false);
                    editor.apply();
                }
            }
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
