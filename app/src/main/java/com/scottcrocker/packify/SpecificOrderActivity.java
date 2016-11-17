package com.scottcrocker.packify;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scottcrocker.packify.model.Order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        orderNumber = getIntent().getIntExtra("ORDERNO", 0);
        specificOrder = MainActivity.db.getOrder(orderNumber);
        refreshView();
    }

    public void deliverOrder(View view) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String currentDate = df.format(Calendar.getInstance().getTime());

        specificOrder.setIsDelivered(true);
        specificOrder.setDeliveryDate(currentDate);

        MainActivity.db.editOrder(specificOrder);
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

    public void openNavigation(View view) {
        String uri = "geo:" + specificOrder.getLatitude() + "," + specificOrder.getLongitude() +
                "?q=" + specificOrder.getAddress();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
}
