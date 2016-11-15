package com.scottcrocker.packify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText inputPasswordEt;
    private EditText inputIdEt;
    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    //TODO : create the login validation for the application and send to proper activity.
    public void loginValidation(View view) {
        inputIdEt = (EditText) findViewById(R.id.input_login_id);
        int inputId = Integer.parseInt(inputIdEt.getText().toString());
        inputPasswordEt = (EditText) findViewById(R.id.input_login_password);
        String inputPassword = inputPasswordEt.getText().toString();
        if (inputId == 1 && inputPassword.equals("admin")) {
            Intent intent = new Intent(this, ActiveOrdersActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Fel input!", Toast.LENGTH_SHORT).show();
        }
    }
}
