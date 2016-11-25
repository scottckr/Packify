package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.scottcrocker.packify.model.User;

import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private SeekBar seekBar;
    private int seekBarMax = 30;
    private int seekBarMin = 5;
    private int seekBarStep = 1;
    private TextView valueOfSeekBar;
    private EditText phoneNumber;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    User user;
    int currentUserId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("USERID", -1);
        user = MainActivity.db.getUser(currentUserId);

        seekBar = (SeekBar)findViewById(R.id.seekBar);
        valueOfSeekBar = (TextView)findViewById(R.id.number_of_orders);
        phoneNumber = (EditText)findViewById(R.id.sms_number);

        loadSavedSettings();
        seekBar.setMax((seekBarMax-seekBarMin)/seekBarStep);
        onSeekBarChanges();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);

        /*Log.d(TAG, "Current user id: " + currentUserId + " // User is admin: " + user.getIsAdmin());
        if (user.getIsAdmin()) {
            Log.d(TAG, "Showing admin choices in toolbar menu");
        } else {
            Log.d(TAG, "Disabling admin choices in toolbar menu");
            menu.getItem(4).setVisible(false);
            menu.getItem(5).setVisible(false);
        }*/

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.toolbar_admin_userhandler:
                intent = new Intent(this, UserHandlerActivity.class);
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
     * onSeekBarChanges senses when you change the value and displays it in textview field above the bar.
     */
    private void onSeekBarChanges(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

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
     * @param view
     */
    public void onSaveSettings(View view){
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String savedPhoneNumber = phoneNumber.getText().toString();
        String savedSeekBarValue = valueOfSeekBar.getText().toString();

        if(savedPhoneNumber.matches("[0-9]{9,10}")){
            editor.putString("seekBarValue", savedSeekBarValue);
            editor.putString("number", savedPhoneNumber);
            editor.commit();
            Toast.makeText(this, "Dina inställningar är sparade", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Telefonnummret måste vara 10 siffror 0-9", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * loadSavedSettings gather the users most recent settings and updates the fields.
     */
    public void loadSavedSettings(){
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        String sharedSeekBarValue = sharedPreferences.getString("seekBarValue", "10");
        String sharedPhoneNumber = sharedPreferences.getString("number", "");

        phoneNumber.setText(sharedPhoneNumber);
        seekBar.setProgress((Integer.parseInt(sharedSeekBarValue)-seekBarMin));
        valueOfSeekBar.setText((sharedSeekBarValue).toString());


    }

    /**
     * logout sends the user back to the loginActivity.
     * It also clears the username and password saved in shared preferences.
     * @param view
     */
    public void logout(View view){
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

}
