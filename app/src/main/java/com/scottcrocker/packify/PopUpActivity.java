package com.scottcrocker.packify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.Toast;

import com.scottcrocker.packify.model.User;

public class PopUpActivity extends AppCompatActivity {

    EditText inputNewUserName;
    EditText inputNewUserPass;
    EditText inputNewUserId;
    EditText inputNewUserPhoneNr;

    PopupWindow pw;

    Switch toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        inputNewUserName = (EditText) findViewById(R.id.input_new_user_name);
        inputNewUserPass = (EditText) findViewById(R.id.input_new_user_pass);
        inputNewUserId = (EditText) findViewById(R.id.input_new_user_id);
        inputNewUserPhoneNr = (EditText) findViewById(R.id.input_new_user_phonenr);

        toggle = (Switch) findViewById(R.id.is_new_admin);

    }

    public void cancel_Btn(View view) {
        finish();
    }

    public void addNewUser(View view) {

        //isValidInput = true;

        String newUsername = String.valueOf(inputNewUserName.getText());
       // validateInput(newUsername, "Namn");

        String newUserPass = String.valueOf(inputNewUserPass.getText());
       // validateInput(newUserPass, "Lösenord");

        String newUserPhoneNr = inputNewUserPhoneNr.getText().toString();
       // validateInput(newUserPhoneNr, "Telefonnummer");

        String newUserId = inputNewUserId.getText().toString();
       // validateInput(newUserId, "Användar ID");

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

       // if (isValidInput) {
            User user = new User(Integer.parseInt(newUserId), newUserPass, newUsername, Integer.parseInt(newUserPhoneNr), toggle.isChecked());
            MainActivity.db.addUser(user);
            Toast.makeText(getApplicationContext(), "Användare tillagd", Toast.LENGTH_SHORT).show();
            finish();
            Intent intent = new Intent(this, UserHandlerActivity.class);
            startActivity(intent);
       // } else if (MainActivity.db.doesFieldExist("Users", "userId", newUserId)) {
        //    Toast.makeText(getApplicationContext(), "Användar-ID finns redan!", Toast.LENGTH_LONG).show();
       // }
    }
}
