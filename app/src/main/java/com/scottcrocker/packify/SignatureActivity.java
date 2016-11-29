package com.scottcrocker.packify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class SignatureActivity extends AppCompatActivity {

    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        saveButton = (Button) findViewById(R.id.save_signature_button);
    }
}
