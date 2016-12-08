package com.scottcrocker.packify;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.scottcrocker.packify.model.Order;
import com.scottcrocker.packify.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;
import static com.scottcrocker.packify.MainActivity.db;


public class SpecificOrderActivity extends AppCompatActivity implements OnMapReadyCallback{

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
    TextView receivedByTv;
    User user;
    int currentUserId;
    ImageView signatureIv;
    Bitmap signature;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    TextView currentUserName;
    NavigationView navigationView;
    SupportMapFragment mapFragment;
    GoogleMap mMap;

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


        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_specific_order);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setUpNavigationView();
        View header = navigationView.getHeaderView(0);
        currentUserName = (TextView) header.findViewById(R.id.current_user_name);
        String currentUserNameStr = " " + user.getName();
        currentUserName.setText(currentUserNameStr);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (specificOrder.getIsDelivered()) {
            mapFragment.getView().setVisibility(View.GONE);
        }

        refreshView();
    }

    private void setUpNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navList);

        // Disabling menu-item for this activity and admin options for non-admin users
        if (!user.getIsAdmin()) {
            navigationView.getMenu().findItem(R.id.navDrawer_admin_orderhandler).setVisible(false);
            navigationView.getMenu().findItem(R.id.navDrawer_admin_userhandler).setVisible(false);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent;
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navDrawer_settings:
                        intent = new Intent(SpecificOrderActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_admin_userhandler:
                        intent = new Intent(SpecificOrderActivity.this, UserHandlerActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_admin_orderhandler:
                        intent = new Intent(SpecificOrderActivity.this, OrderHandlerActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_activeorders:
                        intent = new Intent(SpecificOrderActivity.this, ActiveOrdersActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_orderhistory:
                        intent = new Intent(SpecificOrderActivity.this, OrderHistoryActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;


                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                /*signature = BitmapFactory.decodeByteArray(data.getByteArrayExtra("SIGNATURE"), 0,
                        data.getByteArrayExtra("SIGNATURE").length);
                signatureIv = (ImageView) findViewById(R.id.signature_imageview);
                signatureIv.setImageBitmap(signature);*/

                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String currentDate = df.format(Calendar.getInstance().getTime());
                specificOrder.setIsDelivered(true);
                specificOrder.setDeliveryDate(currentDate);
                specificOrder.setDeliveredBy("" + db.getUser(currentUserId).getId() + ", " + db.getUser(currentUserId).getName());
                specificOrder.setSignature(data.getByteArrayExtra("SIGNATURE"));

                db.editOrder(specificOrder);

                sendSms();

                refreshView();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.toolbar_update_order) {
            refreshView();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            LatLng pos = new LatLng(specificOrder.getLatitude(), specificOrder.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 14));
            mMap.addMarker(new MarkerOptions().position(pos).title(specificOrder.getAddress()));
            mMap.getUiSettings().setAllGesturesEnabled(false);
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    openMaps();
                }
            });
    }

    public void openMaps() {
        String uri = "geo:" + specificOrder.getLatitude() + "," + specificOrder.getLongitude() +
                "?q=" + specificOrder.getAddress() + ", " + specificOrder.getPostAddress();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    public void deliverOrder(View view) {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String sharedPhoneNumber = sharedPreferences.getString("number", "");
        if (sharedPhoneNumber.equals("")) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            Toast.makeText(this, getResources().getString(R.string.toast_specificorder_phoneno), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, SignatureActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    public void sendSms() {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String sharedPhoneNumber = sharedPreferences.getString("number", "");
        String messageSms = "Order " + specificOrder.getOrderNo() + " levererad.";
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(sharedPhoneNumber, null, messageSms, null, null);
    }

    public void refreshView() {
        specificOrder = db.getOrder(orderNumber);
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
            deliveredByStr = getString(R.string.delivered_by) + " " + specificOrder.getDeliveredBy();
            deliveredByTv.setText(deliveredByStr);
            deliveredByTv.setVisibility(View.VISIBLE);

            btnDeliverOrder = (Button) findViewById(R.id.btn_deliver_order);
            btnDeliverOrder.setEnabled(false);

            receivedByTv = (TextView) findViewById(R.id.received_by);
            receivedByTv.setVisibility(View.VISIBLE);

            signatureIv = (ImageView) findViewById(R.id.signature_imageview);
            if (specificOrder.getSignature() != null) {
                signature = BitmapFactory.decodeByteArray(specificOrder.getSignature(), 0, specificOrder.getSignature().length);
            } else {
                signature = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.no_signature);
            }
            signatureIv.setImageBitmap(signature);
            signatureIv.setVisibility(View.VISIBLE);
        } else {
            deliveryDateTv = (TextView) findViewById(R.id.delivery_date);
            deliveryDateTv.setVisibility(View.GONE);

            receivedByTv = (TextView) findViewById(R.id.received_by);
            receivedByTv.setVisibility(View.GONE);

            deliveredByTv = (TextView) findViewById(R.id.delivered_by);
            deliveredByTv.setVisibility(View.GONE);

            signatureIv = (ImageView) findViewById(R.id.signature_imageview);
            signatureIv.setVisibility(View.GONE);
        }
        Log.d(TAG, "View refreshed");
    }

    public void openNavigation(View view) {
        openMaps();
    }
}