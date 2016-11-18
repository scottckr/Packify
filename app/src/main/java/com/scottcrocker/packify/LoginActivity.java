package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scottcrocker.packify.model.User;

import java.util.List;

import static com.scottcrocker.packify.SettingsActivity.SHARED_PREFERENCES;

public class LoginActivity extends AppCompatActivity {

    private EditText inputPasswordEt;
    private EditText inputIdEt;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.toolbar_update_order:
                //TODO Update view.
                return true;

            case R.id.toolbar_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.toolbar_admin_settings:
                intent = new Intent(this, UserHandlerActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //TODO : create the login validation for the application and send to proper activity.
    public void loginValidation(View view) {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        List<User> users = MainActivity.db.getAllUsers();

        inputIdEt = (EditText) findViewById(R.id.input_login_id);
        int inputId = Integer.parseInt(inputIdEt.getText().toString());
        inputPasswordEt = (EditText) findViewById(R.id.input_login_password);
        String inputPassword = inputPasswordEt.getText().toString();

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == inputId) {
                if (users.get(i).getPassword().equals(inputPassword)) {
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();
                } else {
                    Toast.makeText(getApplicationContext(), "Lösenordet är felaktigt!", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("isLoggedIn", false);
                    editor.apply();
                }
            }
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
