package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.scottcrocker.packify.model.Order;
import com.scottcrocker.packify.model.User;


import java.util.List;

import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;

public class OrderHandlerActivity extends AppCompatActivity {

    private static final String TAG = "OrderHandlerActivity";
    EditText orderNoET;
    EditText customerIdET;
    EditText customerNameET;
    EditText orderSumET;
    EditText addressET;
    EditText postAddressET;
    boolean isValidInput;
    User user;
    SharedPreferences sharedPreferences;
    int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_handler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(5).setVisible(false);
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

            case R.id.toolbar_admin_userhandler:
                intent = new Intent(this, UserHandlerActivity.class);
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
     * Method to create a new order object, or handle an existing order object which will be sent to DB
     * @param view
     */
    // TO-DO: create new object containing order information, send to database

    public void addOrder(View view) {

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("USERID", -1);

        isValidInput = true;
        orderNoET = (EditText) findViewById(R.id.input_order_number);
        String orderNo = orderNoET.getText().toString();
        validateInput(orderNo, "Ordernummer");

        customerIdET = (EditText) findViewById(R.id.input_customer_id);
        String customerId = customerIdET.getText().toString();
        validateInput(customerId, "Kundnummer");

        customerNameET = (EditText) findViewById(R.id.input_customer_name);
        String customerName = customerNameET.getText().toString();

        orderSumET = (EditText) findViewById(R.id.input_order_sum);
        String orderSum = orderSumET.getText().toString();
        validateInput(orderSum, "Ordersumma");

        addressET = (EditText) findViewById(R.id.input_order_address);
        postAddressET = (EditText) findViewById(R.id.input_order_post_address);
        String address = addressET.getText().toString() + ", " + postAddressET.getText().toString();

        if(isValidInput) {
            Order order = new Order(Integer.parseInt(orderNo), Integer.parseInt(customerId), customerName, address, Integer.parseInt(orderSum), "---", false, MainActivity.db.getUser(currentUserId).getId(),MainActivity.gps.getLongitude(address), MainActivity.gps.getLatitude(address));

            MainActivity.db.addOrder(order);
            Toast.makeText(getApplicationContext(), "Order sparad", Toast.LENGTH_SHORT).show();
        }
        //Log.d("DATABASE", "Order: " + MainActivity.db.getOrder(order.getOrderNo()).getDeliveryDate());

    }

    /**
     * Method to edit order from DB
     * @param view
     */
    public void editOrder(View view) {

    }

    /**
     * Method to delete order from DB
     * @param view
     */
    // TO-DO: method shall delete order information in database
    public void deleteOrder(View view) {
        Toast.makeText(getApplicationContext(), "Order raderad", Toast.LENGTH_SHORT).show();
    }

    /**
     * Validates the input from user. Makes sure its only digits and not empty.
     * @param input - Users input value.
     * @param fieldName - The name of current field.
     */
    public void validateInput(String input, String fieldName){
        if (input.matches("\\d*")){
            Log.d(TAG, "Input for "+fieldName+" is valid");
        }else if(input.equals("")){
            isValidInput = false;
            Toast.makeText(getApplicationContext(), fieldName+" är tom", Toast.LENGTH_SHORT).show();
        }else{
            isValidInput = false;
            Toast.makeText(getApplicationContext(), fieldName+" måste bestå av siffror!", Toast.LENGTH_SHORT).show();
        }
    }
}
