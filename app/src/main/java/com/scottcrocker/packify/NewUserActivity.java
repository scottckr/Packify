package com.scottcrocker.packify;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.Toast;

import com.scottcrocker.packify.helper.ValidationHelper;
import com.scottcrocker.packify.model.User;

import java.util.ArrayList;
import java.util.List;

public class NewUserActivity extends AppCompatActivity {

    EditText inputNewUserName;
    EditText inputNewUserPass;
    EditText inputNewUserId;
    EditText inputNewUserPhoneNr;

    Switch toggle;
    ValidationHelper validationHelper = new ValidationHelper();
    List<Boolean> isValidInput = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        inputNewUserName = (EditText) findViewById(R.id.input_new_user_name);
        inputNewUserPass = (EditText) findViewById(R.id.input_new_user_pass);
        inputNewUserId = (EditText) findViewById(R.id.input_new_user_id);
        inputNewUserPhoneNr = (EditText) findViewById(R.id.input_new_user_phonenr);
        inputNewUserPass.setTypeface(Typeface.DEFAULT);
        inputNewUserPass.setTransformationMethod(new PasswordTransformationMethod());

        toggle = (Switch) findViewById(R.id.is_new_admin);

    }

    public void cancelBtn(View view) {
        finish();
    }

    public void addNewUser(View view) {

        String newUserId = inputNewUserId.getText().toString();
        isValidInput.add(validationHelper.validateInputNumber(newUserId, "Användar id" ,this));

        String newUsername = String.valueOf(inputNewUserName.getText());
        isValidInput.add(validationHelper.validateInputText(newUsername, "Namn" ,this));

        String newUserPass = String.valueOf(inputNewUserPass.getText());
        isValidInput.add(validationHelper.validateInputText(newUserPass, "Lösenord" ,this));

        String newUserPhoneNr = inputNewUserPhoneNr.getText().toString();
        isValidInput.add(validationHelper.validateInputPhoneNr(newUserPhoneNr, "Telefonnummer" ,this));

        if (ValidationHelper.isAllTrue(isValidInput) && !MainActivity.db.doesFieldExist("Users", "userId", newUserId)) {
            User user = new User(Integer.parseInt(newUserId), newUserPass, newUsername, newUserPhoneNr, toggle.isChecked());
            MainActivity.db.addUser(user);
            Toast.makeText(getApplicationContext(), "Användare tillagd", Toast.LENGTH_SHORT).show();
            finish();
            Intent intent = new Intent(this, UserHandlerActivity.class);
            startActivity(intent);
        } else if (MainActivity.db.doesFieldExist("Users", "userId", newUserId)){
            Toast.makeText(getApplicationContext(), "Användar-ID finns redan!", Toast.LENGTH_LONG).show();
        } else {
            //IsValidInput is false;
        }
        isValidInput.clear();

    }
}/////////////////////////////////////////////////////
