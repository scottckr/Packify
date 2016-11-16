package com.scottcrocker.packify;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scottcrocker.packify.model.Order;

public class SpecificOrderActivity extends AppCompatActivity {

    Order specificOrder;
    int orderNumber;
    TextView orderNumTv;
    String orderNumStr;
    TextView customerIdTv;
    String customerIdStr;
    TextView orderSumTv;
    String orderSumStr;
    TextView addressTv;
    String addressStr;
    TextView deliveryDateTv;
    String deliveryDateStr;
    Button btnDeliverOrder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_order);
        Bundle extras = this.getIntent().getExtras();
        orderNumber = extras.getInt("id");
        specificOrder = MainActivity.db.getOrder(orderNumber);
        refreshView();

    }

    private void deliverOrder(View view) {
        //TODO Send delivery-information to database and reset view
        refreshView();

    }

    private void refreshView() {
        orderNumTv = (TextView) findViewById(R.id.order_number);
        orderNumStr = getString(R.string.order_number) + " " + specificOrder.getOrderNo();
        orderNumTv.setText(orderNumStr);

        customerIdTv = (TextView) findViewById(R.id.customer_id);
        customerIdStr = getString(R.string.customer_id) + " " + specificOrder.getCustomerNo();
        customerIdTv.setText(customerIdStr);

        orderSumTv = (TextView) findViewById(R.id.order_sum);
        orderSumStr = getString(R.string.order_sum) + " " + specificOrder.getOrderSum();
        orderSumTv.setText(orderSumStr);

        addressTv = (TextView) findViewById(R.id.address);
        addressStr = getString(R.string.address) + " " + specificOrder.getAddress();
        addressTv.setText(addressStr);

        if (specificOrder.getIsDelivered()) {
            deliveryDateTv = (TextView) findViewById(R.id.delivery_date);
            deliveryDateStr = getString(R.string.delivery_date) + " " + specificOrder.getDeliveryDate();
            deliveryDateTv.setText(deliveryDateStr);

            btnDeliverOrder = (Button) findViewById(R.id.btn_deliver_order);
            btnDeliverOrder.setEnabled(false);
        }
    }

    private void openNavigation(View view) {
        //TODO Open google maps with coordinates. Not Amphitheater Parkway!

        //RIKTIG KOD - Databas
        String uri = "geo:" + specificOrder.getLatitude() + "," + specificOrder.getLongitude() +
                "?q=" + specificOrder.getLatitude() + "," + specificOrder.getLongitude() +
                "(" + specificOrder.getAddress() + ")";


        //TEST KOD
        //String uri = "geo:57.71780,11.94467?q=57.71780,11.94467(Myntgatan 28)";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
}
