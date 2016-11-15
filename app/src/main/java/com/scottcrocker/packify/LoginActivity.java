package com.scottcrocker.packify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText inputPasswordEt;
    private EditText inputIdEt;
    public static boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    //TODO : create the login validation for the application and send to proper activity.
    public void loginValidation(View view) {
        inputIdEt = (EditText) findViewById(R.id.input_login_id);
        int inputId = Integer.parseInt(inputIdEt.getText().toString());
        inputPasswordEt = (EditText) findViewById(R.id.input_login_password);
        String inputPassword = inputPasswordEt.getText().toString();
        if (inputId == 1 && inputPassword.equals("admin")) {
            isLoggedIn = true;
        } else {
            isLoggedIn = false;
            Toast.makeText(getApplicationContext(), "Fel input!", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
