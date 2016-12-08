package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.scottcrocker.packify.controller.NothingSelectedSpinnerAdapter;
import com.scottcrocker.packify.helper.ValidationHelper;
import com.scottcrocker.packify.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;
import static com.scottcrocker.packify.MainActivity.currentUserId;
import static com.scottcrocker.packify.MainActivity.db;

/**
 * UserHandlerActivity, shows a spinner of User objects and some input fields in the database and lets the user choose a User.
 */
public class UserHandlerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner mSpinner;
    private EditText inputNameET;
    private EditText inputPasswordET;
    private EditText inputPhoneNrET;
    private TextView inputUserIdTV;
    private List<Boolean> isValidInput = new ArrayList<>();
    private ArrayAdapter<User> dataAdapter;
    private Switch isAdminSwitch;
    private User user;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private ValidationHelper validationHelper = new ValidationHelper();
    private TextView currentUserNameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_handler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("USERID", -1);
        user = db.getUser(currentUserId);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_user_handler);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        setUpNavigationView();
        View header = navigationView.getHeaderView(0);

        currentUserNameTV = (TextView) header.findViewById(R.id.current_user_name);
        inputNameET = (EditText) findViewById(R.id.input_user_name);
        inputPasswordET = (EditText) findViewById(R.id.input_user_password);
        inputPhoneNrET = (EditText) findViewById(R.id.input_user_phone);
        inputUserIdTV = (TextView) findViewById(R.id.input_user_id);
        inputPasswordET.setTypeface(Typeface.DEFAULT);
        inputPasswordET.setTransformationMethod(new PasswordTransformationMethod());
        isAdminSwitch = (Switch) findViewById(R.id.admin_switch);
        mSpinner = (Spinner) findViewById(R.id.spinner_user_id);

        inputNameET.setEnabled(false);
        inputPasswordET.setEnabled(false);
        inputPhoneNrET.setEnabled(false);
        isAdminSwitch.setEnabled(false);
        mSpinner.setOnItemSelectedListener(this);
        loadSpinnerData();

        try {
            String currentUserNameStr = " " + user.getName();
            currentUserNameTV.setText(currentUserNameStr);
        } catch (Exception e) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityCompat.finishAffinity(UserHandlerActivity.this);
            startActivity(intent);
        }
    }

    private void setUpNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navList);
        // Disabling menu-item for this activity and admin options for non-admin users
        navigationView.getMenu().findItem(R.id.navDrawer_admin_userhandler).setVisible(false);
        try {
            String currentUserNameStr = " " + user.getName();
            currentUserNameTV.setText(currentUserNameStr);
        } catch (Exception e) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityCompat.finishAffinity(UserHandlerActivity.this);
            startActivity(intent);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navDrawer_settings:
                        intent = new Intent(UserHandlerActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_admin_orderhandler:
                        intent = new Intent(UserHandlerActivity.this, OrderHandlerActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_activeorders:
                        intent = new Intent(UserHandlerActivity.this, ActiveOrdersActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_orderhistory:
                        intent = new Intent(UserHandlerActivity.this, OrderHistoryActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || item.getItemId() == R.id.toolbar_update_order
                || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

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
        List<User> users = db.getAllUsers();
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, users);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setPrompt("Välj en användare...");
        mSpinner.setAdapter(new NothingSelectedSpinnerAdapter(dataAdapter, R.layout.spinner_row_nothing_selected, this));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        user = (User) parent.getItemAtPosition(position);
        if (position != 0) {
            inputNameET.setEnabled(true);
            inputPasswordET.setEnabled(true);
            inputPhoneNrET.setEnabled(true);
            isAdminSwitch.setEnabled(true);
            populateInputFields();
        } else {
            inputNameET.setEnabled(false);
            inputPasswordET.setEnabled(false);
            inputPhoneNrET.setEnabled(false);
            isAdminSwitch.setEnabled(false);
        }
    }

    private void populateInputFields() {
        inputNameET.setText(user.getName());
        String inputUserIdStr = "ID: " + String.valueOf(user.getId());
        inputUserIdTV.setText(inputUserIdStr);
        inputPasswordET.setText(user.getPassword());
        inputPhoneNrET.setText(String.valueOf(user.getTelephone()));
        isAdminSwitch.setChecked(user.getIsAdmin());
    }

    /**
     * OnClick method for changing a User in database, validates input fields and saves changes if everything's okay.
     *
     * @param view The view component that is executed by click handler.
     */
    public void saveEditedUser(View view) {
        if (user != mSpinner.getItemAtPosition(0)) {
            if (user.getId() == 0) {
                refreshView();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_userhandler_admin_change), Toast.LENGTH_LONG).show();
            } else {
                String editedName = String.valueOf(inputNameET.getText());
                isValidInput.add(validationHelper.validateInputText(editedName, "Namn", this));

                String editedPassword = String.valueOf(inputPasswordET.getText());
                isValidInput.add(validationHelper.validateInputText(editedPassword, "Lösenord", this));

                String editedPhoneNr = String.valueOf(inputPhoneNrET.getText());
                isValidInput.add(validationHelper.validateInputPhoneNr(editedPhoneNr, "Telefonnummer", this));

                if (ValidationHelper.isAllTrue(isValidInput)) {
                    user.setName(String.valueOf(inputNameET.getText()));
                    user.setPassword(String.valueOf(inputPasswordET.getText()));
                    user.setTelephone(String.valueOf(inputPhoneNrET.getText()));
                    user.setIsAdmin(isAdminSwitch.isChecked());
                    db.editUser(user);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_userhandler_updated), Toast.LENGTH_SHORT).show();
                }
                isValidInput.clear();
            }
        } else if (user == mSpinner.getItemAtPosition(0)) {
            refreshView();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_userhandler_user), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * OnClick method for adding a new User to database.
     *
     * @param view The view component that is executed by click handler.
     */
    public void callPopup(View view) {
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
    }

    /**
     * OnClick method for deleting a User from database.
     *
     * @param view The view component that is executed by click handler.
     */
    public void deleteUser(View view) {
        Log.d("DELETEUSER", "" + user);
        if (user != mSpinner.getItemAtPosition(0)) {
            if (user.getId() == 0 || user.getId() == currentUserId) {
                refreshView();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_userhandler_admin_delete), Toast.LENGTH_LONG).show();
            } else {
                db.deleteUser(user);
                Toast.makeText(this, user.getName() + " " + getResources().getString(R.string.toast_userhandler_deleted), Toast.LENGTH_SHORT).show();
                refreshView();
                loadSpinnerData();
            }
        } else if (user == mSpinner.getItemAtPosition(0)) {
            refreshView();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_userhandler_user), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Refreshes User objects in the dropdown Spinner, also clears all input fields.
     */
    public void refreshView() {
        mSpinner.setAdapter(new NothingSelectedSpinnerAdapter(dataAdapter, R.layout.spinner_row_nothing_selected, this));
        inputNameET.setText("");
        inputPasswordET.setText("");
        inputPhoneNrET.setText("");
        inputUserIdTV.setText("");
        isAdminSwitch.setChecked(false);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // Method has to exist for some reason.
    }
}
