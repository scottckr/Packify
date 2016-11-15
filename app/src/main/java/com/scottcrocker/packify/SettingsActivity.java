package com.scottcrocker.packify;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView valueOfSeekBar;
    private EditText phoneNumber;
    public static final String SHARED_PREFERENCES = "PackifySharedPreferences";
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        seekBar = (SeekBar)findViewById(R.id.seekBar);
        valueOfSeekBar = (TextView)findViewById(R.id.number_of_orders);
        phoneNumber = (EditText)findViewById(R.id.sms_number);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        phoneNumber.setText(sharedPreferences.getString("number", ""));


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

    public void onSubmitChanges(){
        int value = Integer.parseInt(valueOfSeekBar.getText().toString());
        int number = Integer.parseInt(valueOfSeekBar.getText().toString());
        editor = (SharedPreferences.Editor) getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        editor.putInt("seekBarValue", value);
        editor.putInt("number", number);
        editor.commit();
    }









}
