package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.scottcrocker.packify.helper.ValidationHelper;
import com.scottcrocker.packify.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.scottcrocker.packify.MainActivity.db;
import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    SeekBar seekBar;
    private int seekBarMax = 30;
    private int seekBarMin = 5;
    private int seekBarStep = 1;
    TextView valueOfSeekBar;
    EditText phoneNumber;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    User user;
    int currentUserId;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    TextView currentUserName;
    NavigationView navigationView;
    ValidationHelper validationHelper = new ValidationHelper();
    List<Boolean> isValidInput = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("USERID", -1);
        user = db.getUser(currentUserId);

        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        valueOfSeekBar = (TextView) findViewById(R.id.number_of_orders);
        phoneNumber = (EditText) findViewById(R.id.sms_number);

        loadSavedSettings();
        seekBar.setMax((seekBarMax - seekBarMin) / seekBarStep);
        onSeekBarChanges();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_settings);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setUpNavigationView();
        View header = navigationView.getHeaderView(0);
        currentUserName = (TextView) header.findViewById(R.id.current_user_name);
        currentUserName.setText(user.getName());
    }

    private void setUpNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navList);

        // Disabling menu-item for this activity and admin options for non-admin users
        navigationView.getMenu().findItem(R.id.navDrawer_settings).setVisible(false);
        if (!user.getIsAdmin()) {
            navigationView.getMenu().findItem(R.id.navDrawer_admin_orderhandler).setVisible(false);
            navigationView.getMenu().findItem(R.id.navDrawer_admin_userhandler).setVisible(false);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent;
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navDrawer_activeorders:
                        intent = new Intent(SettingsActivity.this, ActiveOrdersActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.navDrawer_admin_userhandler:
                        intent = new Intent(SettingsActivity.this, UserHandlerActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.navDrawer_admin_orderhandler:
                        intent = new Intent(SettingsActivity.this, OrderHandlerActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.navDrawer_orderhistory:
                        intent = new Intent(SettingsActivity.this, OrderHistoryActivity.class);
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

    /**
     * onSeekBarChanges senses when you change the value and displays it in textview field above the bar.
     */
    private void onSeekBarChanges() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = seekBarMin + (progress * seekBarStep);
                valueOfSeekBar.setText(String.valueOf(value));
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
     * onSaveSettings saves the settings made by the user in shared preferences called PackifySharedPreferences.
     * If the user input is valid it shows a confirm message. Else it shows or a warning message.
     *
     * @param view
     */
    public void onSaveSettings(View view) {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String savedPhoneNumber = phoneNumber.getText().toString();
        String savedSeekBarValue = valueOfSeekBar.getText().toString();
        isValidInput.add(validationHelper.validateInputPhoneNr(savedPhoneNumber, "Telefonnummer" ,this));

        if (validationHelper.isAllTrue(isValidInput)) {
            editor.putString("seekBarValue", savedSeekBarValue);
            editor.putString("number", savedPhoneNumber);
            editor.apply();
            Toast.makeText(this, "Dina inställningar är sparade", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * loadSavedSettings gather the users most recent settings and updates the fields.
     */
    public void loadSavedSettings() {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String sharedSeekBarValue = sharedPreferences.getString("seekBarValue", "10");
        String sharedPhoneNumber = sharedPreferences.getString("number", "");

        phoneNumber.setText(sharedPhoneNumber);
        seekBar.setProgress((Integer.parseInt(sharedSeekBarValue) - seekBarMin));
        valueOfSeekBar.setText((sharedSeekBarValue).toString());
    }

    /**
     * logout sends the user back to the loginActivity.
     * It also clears the username and password saved in shared preferences.
     *
     * @param view
     */
    public void logout(View view) {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}