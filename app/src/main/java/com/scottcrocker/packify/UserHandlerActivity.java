package com.scottcrocker.packify;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import static com.scottcrocker.packify.MainActivity.currentUserId;
import static com.scottcrocker.packify.MainActivity.db;

public class UserHandlerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "UserHandlerActivity";
    Spinner mSpinner;
    EditText inputName;
    EditText inputPassword;
    EditText inputPhoneNr;
    EditText inputUserId;
    boolean isValidInput;

    Switch toggle;

    Button addUserBtn;
    Button deleteUserBtn;
    List<User> users;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_handler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputName = (EditText) findViewById(R.id.input_user_name);
        inputPassword = (EditText) findViewById(R.id.input_user_password);
        inputPhoneNr = (EditText) findViewById(R.id.input_user_phone);
        inputUserId = (EditText) findViewById(R.id.input_user_id);

        toggle = (Switch) findViewById(R.id.admin_switch);

        addUserBtn = (Button) findViewById(R.id.btn_submit_user);
        deleteUserBtn = (Button) findViewById(R.id.btn_delete_user);

        mSpinner = (Spinner) findViewById(R.id.spinner_user_id);
        mSpinner.setOnItemSelectedListener(this);
        loadSpinnerData();
    }

    private void loadSpinnerData() {

        users = db.getAllUsers();

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

        user = (User) parent.getItemAtPosition(position);
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + user,
                Toast.LENGTH_LONG).show();

        populateInputFields();

    }

    private void populateInputFields() {

        inputName.setText(user.getName());
        inputUserId.setText(String.valueOf(user.getId()));
        inputPassword.setText(user.getPassword());
        inputPhoneNr.setText(String.valueOf(user.getTelephone()));

        toggle.setChecked(user.getIsAdmin());

    }


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(4).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.toolbar_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.toolbar_admin_orderhandler:
                intent = new Intent(this, OrderHandlerActivity.class);
                startActivity(intent);
                return true;

            case R.id.toolbar_activeorders:
                intent = new Intent(this, ActiveOrdersActivity.class);
                startActivity(intent);
                return true;

            case R.id.toolbar_orderhistory:
                intent = new Intent(this, OrderHistoryActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Method to create a new user object, or handle an existing user object which will be sent to DB
     *
     * @param view
     */
    // TODO: create new object containing user information, send to database
    public void addUser(View view) {
        isValidInput = true;
        String newUsername = String.valueOf(inputName.getText());
        validateInput(newUsername, "Namn");

        String newUserPass = String.valueOf(inputPassword.getText());
        validateInput(newUserPass, "Lösenord");

        String newUserPhoneNr = inputPhoneNr.getText().toString();
        validateInput(newUserPhoneNr, "Telefonnummer");

        String newUserId = inputUserId.getText().toString();
        validateInput(newUserId, "Användar ID");


        if (isValidInput && !MainActivity.db.doesFieldExist("Users", "userId", newUserId)) {
            User user = new User(Integer.parseInt(newUserId), newUserPass, newUsername, Integer.parseInt(newUserPhoneNr), toggle.isChecked());
            MainActivity.db.addUser(user);
            Toast.makeText(getApplicationContext(), "Användare sparad", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        } else if (MainActivity.db.doesFieldExist("Users", "userId", newUserId)) {
            Toast.makeText(getApplicationContext(), "Användar-ID finns redan!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method to delete user from DB
     *
     * @param view
     */
    // TODO: method shall delete user information in database
    public void deleteUser(View view) {
        Log.d("DELETEUSER", "" + user);
        if (user.getId() != currentUserId || user.getId() != 0) {
            db.deleteUser(user);
            Toast.makeText(this, user.getName() + " deleted.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "You cannot delete yourself or the main admin account!", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Validates the input from user. Sends a toast if userinput is not valid.
     *
     * @param input     - Users input value.
     * @param fieldName - The name of current field.
     */
    public void validateInput(String input, String fieldName) {

        switch (fieldName) {
            case "Användar ID":
                if (input.matches("^\\d{1,9}$")){
                    Log.d(TAG, "Input for "+fieldName+" is valid");
                }else if(input.equals("")){
                    isValidInput = false;
                    Toast.makeText(getApplicationContext(), fieldName + " är tom", Toast.LENGTH_SHORT).show();
                } else {
                    isValidInput = false;
                    Toast.makeText(getApplicationContext(), fieldName + " måste bestå av siffror!", Toast.LENGTH_SHORT).show();
                }
                break;

            case "Namn":
                if (input.equals("")) {
                    isValidInput = false;
                    Toast.makeText(getApplicationContext(), fieldName + " är tom", Toast.LENGTH_SHORT).show();
                } else {
                    //intended to accept numbers as user input. Maybe a name can be "Kenny212".
                    Log.d(TAG, "Input for " + fieldName + " is valid");
                }
                break;

            case "Lösenord":
                break;

            case "Telefonnummer":
                if (input.matches("[0-9]{9,10}")) {
                    Log.d(TAG, "Input for " + fieldName + " is valid");
                } else if (input.equals("")) {
                    isValidInput = false;
                    Toast.makeText(getApplicationContext(), fieldName + " är tom", Toast.LENGTH_SHORT).show();
                } else {
                    isValidInput = false;
                    Toast.makeText(getApplicationContext(), fieldName + " måste bestå 9-10 av siffror!", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
