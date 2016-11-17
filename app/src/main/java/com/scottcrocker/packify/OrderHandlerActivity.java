package com.scottcrocker.packify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.scottcrocker.packify.model.Order;

public class OrderHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_handler);
    }

    /**
     * Method to create a new order object, or handle an existing order object which will be sent to DB
     * @param view
     */
    // TO-DO: create new object containing order information, send to database
    public void saveOrder(View view) {
        EditText orderNoET = (EditText) findViewById(R.id.input_order_number);
        int orderNo = Integer.parseInt(orderNoET.getText().toString());
        EditText customerIdET = (EditText) findViewById(R.id.input_customer_id);
        int customerId = Integer.parseInt(customerIdET.getText().toString());
        EditText orderSumET = (EditText) findViewById(R.id.input_order_sum);
        int orderSum = Integer.parseInt(orderSumET.getText().toString());
        EditText addressET = (EditText) findViewById(R.id.input_order_address);
        String address = addressET.getText().toString();
        Order order = new Order(orderNo, customerId, address, orderSum, "Leeeif", false, 0.0, 0.0);
        MainActivity.db.addOrder(order);

        Log.d("DATABASE", "Order: " + MainActivity.db.getOrder(order.getOrderNo()).getDeliveryDate());

        Toast.makeText(getApplicationContext(), "Order sparad", Toast.LENGTH_SHORT).show();
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
