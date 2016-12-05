package com.scottcrocker.packify;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.scottcrocker.packify.helper.ValidationHelper;
import com.scottcrocker.packify.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.scottcrocker.packify.MainActivity.currentUserId;
import static com.scottcrocker.packify.MainActivity.db;

public class UserHandlerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "UserHandlerActivity";
    Spinner mSpinner;
    EditText inputName;
    EditText inputPassword;
    EditText inputPhoneNr;
    TextView inputUserId;
    List<Boolean> isValidInput = new ArrayList<>();

    Switch toggle;

    Button saveEditedUserBtn;
    Button deleteUserBtn;
    List<User> users;
    User user;
    //Variable "currentUser" is only called that because of "user" being used later in code - must be changed (NavDrawer)
    User currentUser;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    TextView currentUserName;
    NavigationView navigationView;
    ValidationHelper validationHelper = new ValidationHelper();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_handler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentUser = db.getUser(currentUserId);

        inputName = (EditText) findViewById(R.id.input_user_name);
        inputPassword = (EditText) findViewById(R.id.input_user_password);
        inputPhoneNr = (EditText) findViewById(R.id.input_user_phone);
        inputUserId = (TextView) findViewById(R.id.input_user_id);
        inputPassword.setTypeface(Typeface.DEFAULT);
        inputPassword.setTransformationMethod(new PasswordTransformationMethod());

        toggle = (Switch) findViewById(R.id.admin_switch);

        saveEditedUserBtn = (Button) findViewById(R.id.btn_save_existing_user);
        deleteUserBtn = (Button) findViewById(R.id.btn_delete_user);

        mSpinner = (Spinner) findViewById(R.id.spinner_user_id);
        mSpinner.setOnItemSelectedListener(this);
        loadSpinnerData();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_user_handler);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setUpNavigationView();
        View header = navigationView.getHeaderView(0);
        currentUserName = (TextView) header.findViewById(R.id.current_user_name);
        String currentUserNameStr = " " + currentUser.getName();
        currentUserName.setText(currentUserNameStr);
    }

    private void setUpNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navList);

        // Disabling menu-item for this activity and admin options for non-admin users
        navigationView.getMenu().findItem(R.id.navDrawer_admin_userhandler).setVisible(false);
        if (!currentUser.getIsAdmin()) {
            navigationView.getMenu().findItem(R.id.navDrawer_admin_orderhandler).setVisible(false);
            navigationView.getMenu().findItem(R.id.navDrawer_admin_userhandler).setVisible(false);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent;
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navDrawer_settings:
                        intent = new Intent(UserHandlerActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.navDrawer_admin_orderhandler:
                        intent = new Intent(UserHandlerActivity.this, OrderHandlerActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.navDrawer_activeorders:
                        intent = new Intent(UserHandlerActivity.this, ActiveOrdersActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.navDrawer_orderhistory:
                        intent = new Intent(UserHandlerActivity.this, OrderHistoryActivity.class);
                        startActivity(intent);
                        return true;


                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.toolbar_update_order) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    //What's this? What's this? Whaaaaaat iiiiiiiis thiiiiiiis?
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        inputUserId.setText("ID:" + String.valueOf(user.getId()));
        inputPassword.setText(user.getPassword());
        inputPhoneNr.setText(String.valueOf(user.getTelephone()));
        toggle.setChecked(user.getIsAdmin());

    }

    public void saveEditedUser(View view) {
        if (user.getId() != 0) {
            String editedName = String.valueOf(inputName.getText());
            isValidInput.add(validationHelper.validateInputText(editedName, "Namn" ,this));

            String editedPassword = String.valueOf(inputPassword.getText());
            isValidInput.add(validationHelper.validateInputText(editedPassword, "Lösenord" ,this));

            String editedPhoneNr = String.valueOf(inputPhoneNr.getText());
            isValidInput.add(validationHelper.validateInputPhoneNr(editedPhoneNr, "Telefonnummer" ,this));

            if(validationHelper.isAllTrue(isValidInput)){
                user.setName(String.valueOf(inputName.getText()));
                user.setPassword(String.valueOf(inputPassword.getText()));
                user.setTelephone(String.valueOf(inputPhoneNr.getText()));
                user.setIsAdmin(toggle.isChecked());
                db.editUser(user);
                Toast.makeText(getApplicationContext(), "Användaruppgifter uppdaterade", Toast.LENGTH_SHORT).show();
                refreshView();
            }
            isValidInput.clear();
        } else {
            refreshView();
            Toast.makeText(getApplicationContext(), "Du kan inte redigera huvudadminkontot!", Toast.LENGTH_LONG).show();
        }
    }

    public void callPopup(View view) {
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
    }


    /**
     * Method to delete user from DB
     *
     * @param view
     */
    // TODO: method shall delete user information in database
    public void deleteUser(View view) {
        Log.d("DELETEUSER", "" + user);
        if (user.getId() != currentUserId || user.getId() != 0 ) {
            db.deleteUser(user);
            Toast.makeText(this, user.getName() + " deleted.", Toast.LENGTH_SHORT).show();
            refreshView();
        } else {
            Toast.makeText(this, "You cannot delete yourself or the main admin account!", Toast.LENGTH_LONG).show();
        }
    }


    public void refreshView() {
        inputName.setText("");
        inputPassword.setText("");
        inputPhoneNr.setText("");
        inputUserId.setText("");
    }


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
