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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scottcrocker.packify.model.Order;
import com.scottcrocker.packify.model.User;


import java.util.List;

import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;
import static com.scottcrocker.packify.MainActivity.db;

public class OrderHandlerActivity extends AppCompatActivity {

    private static final String TAG = "OrderHandlerActivity";
    EditText orderNoET;
    EditText customerIdET;
    EditText customerNameET;
    EditText orderSumET;
    EditText addressET;
    EditText postAddressET;
    Button addOrderBtn;
    Button editOrderBtn;
    boolean isValidInput;
    SharedPreferences sharedPreferences;
    int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_handler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addOrderBtn = (Button) findViewById(R.id.btn_submit_order);
        editOrderBtn = (Button) findViewById(R.id.btn_edit_order);
        editOrderBtn.setVisibility(View.INVISIBLE);
        orderNoET = (EditText) findViewById(R.id.input_order_number);
        customerIdET = (EditText) findViewById(R.id.input_customer_id);
        customerNameET = (EditText) findViewById(R.id.input_customer_name);
        orderSumET = (EditText) findViewById(R.id.input_order_sum);
        addressET = (EditText) findViewById(R.id.input_order_address);
        postAddressET = (EditText) findViewById(R.id.input_order_post_address);
        orderNoET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (MainActivity.db.doesFieldExist("Orders", "orderNo", editable.toString())) {
                    editOrderBtn.setVisibility(View.VISIBLE);
                    addOrderBtn.setVisibility(View.INVISIBLE);

                    String customerIdStr = String.valueOf(MainActivity.db.getOrder(Integer.parseInt(editable.toString())).getCustomerNo());
                    customerIdET.setText(customerIdStr);

                    String customerNameStr = String.valueOf(MainActivity.db.getOrder(Integer.parseInt(editable.toString())).getCustomerName());
                    customerNameET.setText(customerNameStr);

                    String orderSumStr = String.valueOf(MainActivity.db.getOrder(Integer.parseInt(editable.toString())).getOrderSum());
                    orderSumET.setText(orderSumStr);

                    String addressStr = String.valueOf(MainActivity.db.getOrder(Integer.parseInt(editable.toString())).getAddress());
                    addressET.setText(addressStr);

                    String postAddressStr = String.valueOf(MainActivity.db.getOrder(Integer.parseInt(editable.toString())).getPostAddress());
                    postAddressET.setText(postAddressStr);
                } else {
                    editOrderBtn.setVisibility(View.INVISIBLE);
                    addOrderBtn.setVisibility(View.VISIBLE);

                    customerIdET.setText("");
                    customerNameET.setText("");
                    orderSumET.setText("");
                    addressET.setText("");
                    postAddressET.setText("");
                }
            }
        });
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
        String address = addressET.getText().toString();

        postAddressET = (EditText) findViewById(R.id.input_order_post_address);
        String postAddress = postAddressET.getText().toString();

        if(isValidInput && !MainActivity.db.doesFieldExist("Orders", "orderNo", orderNo)) {
            Order order = new Order(Integer.parseInt(orderNo), Integer.parseInt(customerId), customerName, address, postAddress, Integer.parseInt(orderSum), "---", false, MainActivity.db.getUser(currentUserId).getId(),MainActivity.gps.getLongitude(address), MainActivity.gps.getLatitude(address));

            MainActivity.db.addOrder(order);
            Toast.makeText(getApplicationContext(), "Order sparad", Toast.LENGTH_SHORT).show();
        } else if(MainActivity.db.doesFieldExist("Orders", "orderNo", orderNo)) {
            Toast.makeText(getApplicationContext(), "Ordernumret finns redan!", Toast.LENGTH_LONG).show();
        }
        //Log.d("DATABASE", "Order: " + MainActivity.db.getOrder(order.getOrderNo()).getDeliveryDate());

        refreshView();
    }

    /**
     * Method to edit order from DB
     * @param view
     */
    public void editOrder(View view) {
        Order order = db.getOrder(Integer.parseInt(orderNoET.getText().toString()));
        Order editedOrder = new Order(order.getOrderNo(), Integer.parseInt(customerIdET.getText().toString()),
                customerNameET.getText().toString(), addressET.getText().toString(), postAddressET.getText().toString(),
                Integer.parseInt(orderSumET.getText().toString()), order.getDeliveryDate(), order.getIsDelivered(),
                order.getDeliveredBy(), order.getLongitude(), order.getLatitude());
        db.editOrder(editedOrder);
        refreshView();
    }

    /**
     * Method to delete order from DB
     * @param view
     */
    // TO-DO: method shall delete order information in database
    public void deleteOrder(View view) {
        Order order = db.getOrder(Integer.parseInt(orderNoET.getText().toString()));
        db.deleteOrder(order);
        Toast.makeText(getApplicationContext(), "Order raderad", Toast.LENGTH_SHORT).show();
        refreshView();
    }

    public void refreshView() {
        orderNoET.setText("");
        customerIdET.setText("");
        customerNameET.setText("");
        orderSumET.setText("");
        addressET.setText("");
        postAddressET.setText("");
    }

    /**
     * Validates the input from user. Makes sure its only digits and not empty.
     * @param input - Users input value.
     * @param fieldName - The name of current field.
     */
    public void validateInput(String input, String fieldName){
        if (input.matches("^\\d{1,9}$")){
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
