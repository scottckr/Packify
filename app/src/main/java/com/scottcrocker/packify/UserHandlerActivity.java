package com.scottcrocker.packify;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.scottcrocker.packify.model.User;

import java.util.List;

public class UserHandlerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner mSpinner;
    EditText inputName;
    EditText inputPassword;
    EditText inputPhoneNr;
    EditText inputUserId;

    Button addUserBtn;
    Button deleteUserBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_handler);

        inputName = (EditText) findViewById(R.id.input_user_name);
        inputPassword = (EditText) findViewById(R.id.input_user_password);
        inputPhoneNr = (EditText) findViewById(R.id.input_user_phone);
        inputUserId = (EditText) findViewById(R.id.input_user_id);

        addUserBtn = (Button) findViewById(R.id.btn_submit_user);
        deleteUserBtn = (Button) findViewById(R.id.btn_delete_user);

        mSpinner = (Spinner) findViewById(R.id.spinner_user_id);
        mSpinner.setOnItemSelectedListener(this);
        loadSpinnerData();
    }

    private void loadSpinnerData() {

        List<User> users = MainActivity.db.getAllUsers();

        ArrayAdapter<User> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, users);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mSpinner.setAdapter(dataAdapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + item,
                Toast.LENGTH_LONG).show();

    }


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * Method to create a new user object, or handle an existing user object which will be sent to DB
     * @param view
     */
    // TO-DO: create new object containing user information, send to database
    public void addUser(View view) {

        String newUsername = String.valueOf(inputName.getText());
        String newUserPass = String.valueOf(inputPassword.getText());
        int newUserPhoneNr = Integer.parseInt(inputPhoneNr.getText().toString());
        int newUserId = Integer.parseInt(inputUserId.getText().toString());

        Switch toggle = (Switch) findViewById(R.id.admin_switch);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    // The toggle is enabled
                } else {

                }
            }
        });

        User user = new User(newUserId, newUserPass, newUsername, newUserPhoneNr,toggle.isChecked());

        MainActivity.db.addUser(user);

        Toast.makeText(getApplicationContext(), "Användare sparad", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());
    }

    /**
     * Method to delete user from DB
     * @param view
     */
    // TO-DO: method shall delete user information in database
    public void deleteUser(View view) {
        Toast toast = new Toast(this);

        Toast.makeText(getApplicationContext(), "Användare raderad", Toast.LENGTH_SHORT).show();
    }
}
