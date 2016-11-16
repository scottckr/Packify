package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView valueOfSeekBar;
    private EditText phoneNumber;
    public static final String SHARED_PREFERENCES = "PackifySharedPreferences";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        seekBar = (SeekBar)findViewById(R.id.seekBar);
        valueOfSeekBar = (TextView)findViewById(R.id.number_of_orders);
        phoneNumber = (EditText)findViewById(R.id.sms_number);

        loadSavedSettings();
        onSeekBarChanges();
    }

    /**
     * onSeekBarChanges senses when you change the value and displays it in textview field above the bar.
     */
    public void onSeekBarChanges(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valueOfSeekBar.setText(String.valueOf(progress));
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
     * OnSubmitChanges saves the settings made by the user in shared preferences called PackifySharedPreferences.
     * If the user input is valid it shows a confirm message. Else it shows or a warning message.
     * @param view
     */
    public void onSubmitChanges(View view){
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String savedPhoneNumber = phoneNumber.getText().toString();
        String savedSeekBarValue = valueOfSeekBar.getText().toString();

        if(savedPhoneNumber.matches("[0-9]{10}")){
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
        String sharedSeekBarValue = sharedPreferences.getString("seekBarValue", "0");
        String sharedPhoneNumber = sharedPreferences.getString("number", "");

        phoneNumber.setText(sharedPhoneNumber);
        seekBar.setProgress(Integer.parseInt(sharedSeekBarValue));
        valueOfSeekBar.setText(sharedSeekBarValue.toString());

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
