package com.scottcrocker.packify;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scottcrocker.packify.model.Order;
import com.scottcrocker.packify.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;
import static com.scottcrocker.packify.MainActivity.db;


public class SpecificOrderActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String TAG = "SpecificOrderActivity";
    Order specificOrder;
    int orderNumber;
    TextView orderNumTv;
    String orderNumStr;
    TextView customerIdTv;
    String customerIdStr;
    TextView customerNameTv;
    String customerNameStr;
    TextView orderSumTv;
    String orderSumStr;
    TextView addressTv;
    String addressStr;
    TextView deliveryDateTv;
    String deliveryDateStr;
    Button btnDeliverOrder;
    TextView deliveredByTv;
    String deliveredByStr;
    TextView postAddressTv;
    String postAddressStr;
    User user;
    int currentUserId;
    ImageView signatureIv;
    Bitmap signature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("USERID", -1);

        user = db.getUser(currentUserId);
        orderNumber = getIntent().getIntExtra("ORDERNO", 0);
        specificOrder = db.getOrder(orderNumber);
        refreshView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(requestCode == Activity.RESULT_OK) {
                signature = BitmapFactory.decodeByteArray(data.getByteArrayExtra("SIGNATURE"), 0,
                        data.getByteArrayExtra("SIGNATURE").length);
                signatureIv = (ImageView) findViewById(R.id.signature_imageview);
                signatureIv.setImageBitmap(signature);
            }
        }
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
                specificOrder = db.getOrder(orderNumber);
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
        Intent intent = new Intent(this, SignatureActivity.class);
        startActivityForResult(intent, 1);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String currentDate = df.format(Calendar.getInstance().getTime());

        specificOrder.setIsDelivered(true);
        specificOrder.setDeliveryDate(currentDate);
        specificOrder.setDeliveredBy(currentUserId);

        db.editOrder(specificOrder);

        sendSms();
        refreshView();
    }

    public void sendSms() {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String sharedPhoneNumber = sharedPreferences.getString("number", "");
        String messageSms = "Order " + specificOrder.getOrderNo() + " levererad.";
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

        customerNameTv = (TextView) findViewById(R.id.customer_name);
        customerNameStr = getString(R.string.customer_name) + " " + specificOrder.getCustomerName();
        customerNameTv.setText(customerNameStr);

        orderSumTv = (TextView) findViewById(R.id.order_sum);
        orderSumStr = getString(R.string.order_sum) + " " + specificOrder.getOrderSum() + " SEK";
        orderSumTv.setText(orderSumStr);

        addressTv = (TextView) findViewById(R.id.address);
        addressStr = getString(R.string.address) + " " + specificOrder.getAddress();
        addressTv.setText(addressStr);

        postAddressTv = (TextView) findViewById(R.id.post_address);
        postAddressStr = getString(R.string.post_address) + " " + specificOrder.getPostAddress();
        postAddressTv.setText(postAddressStr);

        if (specificOrder.getIsDelivered()) {
            deliveryDateTv = (TextView) findViewById(R.id.delivery_date);
            deliveryDateStr = getString(R.string.delivery_date) + " " + specificOrder.getDeliveryDate();
            deliveryDateTv.setText(deliveryDateStr);
            deliveryDateTv.setVisibility(View.VISIBLE);

            deliveredByTv = (TextView) findViewById(R.id.delivered_by);
            deliveredByStr = getString(R.string.delivered_by) + " " + specificOrder.getDeliveredBy() + ", " + db.getUser(specificOrder.getDeliveredBy()).getName();
            deliveredByTv.setText(deliveredByStr);
            deliveredByTv.setVisibility(View.VISIBLE);

            btnDeliverOrder = (Button) findViewById(R.id.btn_deliver_order);
            btnDeliverOrder.setEnabled(false);
        } else {
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
