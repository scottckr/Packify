package com.scottcrocker.packify;

import android.content.Intent;
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

import java.util.List;

public class OrderHandlerActivity extends AppCompatActivity {

    EditText orderNoET;
    EditText customerIdET;
    EditText orderSumET;
    EditText addressET;

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
        orderNoET = (EditText) findViewById(R.id.input_order_number);
        int orderNo = Integer.parseInt(orderNoET.getText().toString());
        customerIdET = (EditText) findViewById(R.id.input_customer_id);
        int customerId = Integer.parseInt(customerIdET.getText().toString());
        orderSumET = (EditText) findViewById(R.id.input_order_sum);
        int orderSum = Integer.parseInt(orderSumET.getText().toString());
        addressET = (EditText) findViewById(R.id.input_order_address);
        String address = addressET.getText().toString();

        Order order = new Order(orderNo, customerId, address, orderSum, "---", false, 0,
                MainActivity.gps.getLongitude(address), MainActivity.gps.getLatitude(address));

        MainActivity.db.addOrder(order);

        //Log.d("DATABASE", "Order: " + MainActivity.db.getOrder(order.getOrderNo()).getDeliveryDate());

        Toast.makeText(getApplicationContext(), "Order sparad", Toast.LENGTH_SHORT).show();
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
}
