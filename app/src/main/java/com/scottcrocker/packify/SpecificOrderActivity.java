package com.scottcrocker.packify;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.scottcrocker.packify.model.Order;
import com.scottcrocker.packify.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;


public class SpecificOrderActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String TAG = "SpecificOrderActivity";
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
    TextView deliveredByTv;
    String deliveredByStr;
    User user;
    int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.SEND_SMS},1);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("USERID", -1);
        user = MainActivity.db.getUser(currentUserId);
        orderNumber = getIntent().getIntExtra("ORDERNO", 0);
        specificOrder = MainActivity.db.getOrder(orderNumber);
        refreshView();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        Log.d(TAG, "Current user id: " + currentUserId + " // User is admin: " + user.getIsAdmin());
        if (user.getIsAdmin()) {
            Log.d(TAG, "Showing admin choices in toolbar menu");
        } else {
            Log.d(TAG, "Disabling admin choices in toolbar menu");
            menu.getItem(4).setVisible(false);
            menu.getItem(5).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }


        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.toolbar_update_order:
                refreshView();
                return true;

            case R.id.toolbar_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.toolbar_admin_userhandler:
                intent = new Intent(this, UserHandlerActivity.class);
                startActivity(intent);
                return true;

            case R.id.toolbar_admin_orderhandler:
                intent = new Intent(this, OrderHandlerActivity.class);
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

    public void deliverOrder(View view) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String currentDate = df.format(Calendar.getInstance().getTime());

        specificOrder.setIsDelivered(true);
        specificOrder.setDeliveryDate(currentDate);
        specificOrder.setDeliveredBy(currentUserId);

        MainActivity.db.editOrder(specificOrder);
        sendSms();
        refreshView();
    }

    public void sendSms() {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        String sharedPhoneNumber = sharedPreferences.getString("number", "");
        String messageSms = "Order " + specificOrder.getOrderNo() + " levererad";
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(sharedPhoneNumber, null, messageSms, null, null);
    }


    public void refreshView() {
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
            deliveryDateTv.setVisibility(View.VISIBLE);
            deliveryDateTv.setText(deliveryDateStr);

            deliveredByTv = (TextView) findViewById(R.id.delivered_by);
            deliveredByTv.setVisibility(View.VISIBLE);

            btnDeliverOrder = (Button) findViewById(R.id.btn_deliver_order);
            btnDeliverOrder.setEnabled(false);
        } else {
            deliveredByTv = (TextView) findViewById(R.id.delivered_by);
            //Waiting for method to be done at dbhandler....
            deliveredByStr = getString(R.string.delivered_by) + " " + specificOrder.getDeliveredBy();

            deliveryDateTv = (TextView) findViewById(R.id.delivery_date);
            deliveryDateTv.setVisibility(View.INVISIBLE);

            deliveredByTv = (TextView) findViewById(R.id.delivered_by);
            deliveredByTv.setVisibility(View.INVISIBLE);
        }
        Log.d(TAG, "View refreshed");
    }

    public void openNavigation(View view) {
        String uri = "geo:" + specificOrder.getLatitude() + "," + specificOrder.getLongitude() +
                "?q=" + specificOrder.getAddress();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
}
