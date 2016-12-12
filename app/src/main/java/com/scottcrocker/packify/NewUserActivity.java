package com.scottcrocker.packify;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.scottcrocker.packify.helper.ValidationHelper;
import com.scottcrocker.packify.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.scottcrocker.packify.MainActivity.db;

/**
 * NewUserActivity lets the user add new user to the database
 */
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
        isValidInput.clear();

        String newUserId = inputNewUserId.getText().toString();
        if(!db.doesFieldExist("Users", "userId", newUserId)){

            isValidInput.add(validationHelper.validateInputNumber(newUserId, "Användar-ID" ,this));

            String newUsername = String.valueOf(inputNewUserName.getText());
            isValidInput.add(validationHelper.validateInputText(newUsername, "Namn" ,this));

            String newUserPass = String.valueOf(inputNewUserPass.getText());
            isValidInput.add(validationHelper.validateInputText(newUserPass, "Lösenord" ,this));

            String newUserPhoneNr = inputNewUserPhoneNr.getText().toString();
            isValidInput.add(validationHelper.validateInputPhoneNr(newUserPhoneNr, "Telefonnummer" ,this));

            if (ValidationHelper.isAllTrue(isValidInput)) {
                User user = new User(Integer.parseInt(newUserId), newUserPass, newUsername, newUserPhoneNr, toggle.isChecked());
                db.addUser(user);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_newuser_saved), Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(this, UserHandlerActivity.class);
                startActivity(intent);
            } else {
                //IsValidInput is false;
            }
        } else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_newuser_userid), Toast.LENGTH_LONG).show();
        }
    }
}
