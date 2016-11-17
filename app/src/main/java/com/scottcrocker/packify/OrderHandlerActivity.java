package com.scottcrocker.packify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
    public void createOrderFromInput(View view) {
        Toast toast = new Toast(this);

        Toast.makeText(getApplicationContext(), "Order sparad", Toast.LENGTH_SHORT).show();
    }

    /**
     * Method to delete order from DB
     * @param view
     */
    // TO-DO: method shall delete order information in database
    public void deleteOrder(View view) {
        Toast toast = new Toast(this);

        Toast.makeText(getApplicationContext(), "Order raderad", Toast.LENGTH_SHORT).show();
    }
}
