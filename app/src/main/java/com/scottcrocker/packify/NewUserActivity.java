package com.scottcrocker.packify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.scottcrocker.packify.helper.ValidationHelper;
import com.scottcrocker.packify.model.User;

public class NewUserActivity extends AppCompatActivity {

    EditText inputNewUserName;
    EditText inputNewUserPass;
    EditText inputNewUserId;
    EditText inputNewUserPhoneNr;

    Switch toggle;

        ValidationHelper validationHelper = new ValidationHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        inputNewUserName = (EditText) findViewById(R.id.input_new_user_name);
        inputNewUserPass = (EditText) findViewById(R.id.input_new_user_pass);
        inputNewUserId = (EditText) findViewById(R.id.input_new_user_id);
        inputNewUserPhoneNr = (EditText) findViewById(R.id.input_new_user_phonenr);

        toggle = (Switch) findViewById(R.id.is_new_admin);

    }

    public void cancelBtn(View view) {
        finish();
    }

    public void addNewUser(View view) {

        String newUsername = String.valueOf(inputNewUserName.getText());
        validationHelper.validateInputName(newUsername,this);


        String newUserPass = String.valueOf(inputNewUserPass.getText());
        validationHelper.validateInputPassWord(newUserPass,this);

        String newUserPhoneNr = inputNewUserPhoneNr.getText().toString();
        validationHelper.validateInputPhoneNr(newUserPhoneNr,this);

        String newUserId = inputNewUserId.getText().toString();
        validationHelper.validateInputUserId(newUserId,this);



        if (validationHelper.validateNewUserFields() == true && !MainActivity.db.doesFieldExist("Users", "userId", newUserId)) {
            User user = new User(Integer.parseInt(newUserId), newUserPass, newUsername, Integer.parseInt(newUserPhoneNr), toggle.isChecked());
            MainActivity.db.addUser(user);
            Toast.makeText(getApplicationContext(), "Användare tillagd", Toast.LENGTH_SHORT).show();
            finish();
            Intent intent = new Intent(this, UserHandlerActivity.class);
            startActivity(intent);
        } else if (MainActivity.db.doesFieldExist("Users", "userId", newUserId)){
            Toast.makeText(getApplicationContext(), "Användar-ID finns redan!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Något fält är felaktigt.", Toast.LENGTH_LONG).show();
        }
    }
}
