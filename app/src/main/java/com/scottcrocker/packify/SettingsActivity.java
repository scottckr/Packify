package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.scottcrocker.packify.helper.ActiveOrdersHelper;
import com.scottcrocker.packify.helper.ValidationHelper;
import com.scottcrocker.packify.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.scottcrocker.packify.MainActivity.db;
import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;

/**
 * SettingsActivity contains seekbar, logout-button and is where phone number for shared preferences is set.
 */
public class SettingsActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private int seekBarMin = 5;
    private int seekBarStep = 1;
    private TextView valueOfSeekBarTV;
    private EditText phoneNumberET;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private User user;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private ValidationHelper validationHelper = new ValidationHelper();
    private List<Boolean> isValidInput = new ArrayList<>();
    private ActiveOrdersHelper activeOrdersHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("USERID", -1);
        user = db.getUser(currentUserId);

        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        valueOfSeekBarTV = (TextView) findViewById(R.id.number_of_orders);
        phoneNumberET = (EditText) findViewById(R.id.sms_number);

        loadSavedSettings();
        int seekBarMax = 30;
        seekBar.setMax((seekBarMax - seekBarMin) / seekBarStep);
        onSeekBarChanges();

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_settings);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }        getSupportActionBar().setHomeButtonEnabled(true);
        setUpNavigationView();
        View header = navigationView.getHeaderView(0);

        TextView currentUserNameTV = (TextView) header.findViewById(R.id.current_user_name);

        try {
            String currentUserNameStr = " " + user.getName();
            currentUserNameTV.setText(currentUserNameStr);
        } catch (Exception e) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityCompat.finishAffinity(SettingsActivity.this);
            startActivity(intent);
        }
    }

    private void setUpNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navList);

        // Disabling menu-item for this activity and admin options for non-admin users
        navigationView.getMenu().findItem(R.id.navDrawer_settings).setVisible(false);
        try {
            if (!user.getIsAdmin()) {
                navigationView.getMenu().findItem(R.id.navDrawer_admin_orderhandler).setVisible(false);
                navigationView.getMenu().findItem(R.id.navDrawer_admin_userhandler).setVisible(false);
            }
        } catch (Exception e) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityCompat.finishAffinity(SettingsActivity.this);
            startActivity(intent);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navDrawer_activeorders:
                        intent = new Intent(SettingsActivity.this, ActiveOrdersActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_admin_userhandler:
                        intent = new Intent(SettingsActivity.this, UserHandlerActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_admin_orderhandler:
                        intent = new Intent(SettingsActivity.this, OrderHandlerActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_orderhistory:
                        intent = new Intent(SettingsActivity.this, OrderHistoryActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || item.getItemId() == R.id.toolbar_update_order || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void onSeekBarChanges() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = seekBarMin + (progress * seekBarStep);
                valueOfSeekBarTV.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * Saves the settings made by the user in shared preferences called PackifySharedPreferences.
     * If the user input is valid a confirm message is displayed.
     *
     * @param view The view component that is executed by click handler.
     */
    public void onSaveSettings(View view) {
        isValidInput.clear();
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String savedPhoneNumber = phoneNumberET.getText().toString();
        String savedSeekBarValue = valueOfSeekBarTV.getText().toString();
        isValidInput.add(validationHelper.validateInputPhoneNr(savedPhoneNumber, "Telefonnummer", this));

        if (ValidationHelper.isAllTrue(isValidInput)) {
            editor.putString("seekBarValue", savedSeekBarValue);
            editor.putString("number", savedPhoneNumber);
            editor.apply();
            Toast.makeText(this, getResources().getString(R.string.toast_settings_saved), Toast.LENGTH_SHORT).show();
            ActiveOrdersHelper.setSeekBarValue(Integer.parseInt(savedSeekBarValue));
            finish();
        }
    }

    /**
     * Gathers the users most recent settings and updates the fields.
     */
    public void loadSavedSettings() {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String sharedSeekBarValue = sharedPreferences.getString("seekBarValue", "10");
        String sharedPhoneNumber = sharedPreferences.getString("number", "");

        phoneNumberET.setText(sharedPhoneNumber);
        seekBar.setProgress((Integer.parseInt(sharedSeekBarValue) - seekBarMin));
        valueOfSeekBarTV.setText((sharedSeekBarValue));
    }

    /**
     * Sends the user back to the loginActivity.
     * It also clears the user id saved in shared preferences.
     *
     * @param view The view component that is executed by click handler.
     */
    public void logout(View view) {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.remove("USERID");
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        // Kills all activities in backstack
        ActivityCompat.finishAffinity(SettingsActivity.this);
        finish();
    }
}