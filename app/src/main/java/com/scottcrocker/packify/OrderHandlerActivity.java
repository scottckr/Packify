package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.scottcrocker.packify.helper.ActiveOrdersHelper;
import com.scottcrocker.packify.helper.GPSHelper;
import com.scottcrocker.packify.helper.ValidationHelper;
import com.scottcrocker.packify.model.Order;
import com.scottcrocker.packify.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;
import static com.scottcrocker.packify.MainActivity.db;

/**
 * OrderHandlerActivity lets the user edit and delete orders from the database
 */
public class OrderHandlerActivity extends AppCompatActivity {

    private EditText orderNoET;
    private EditText customerIdET;
    private EditText customerNameET;
    private EditText orderSumET;
    private EditText addressET;
    private EditText postAddressET;
    private Switch isDeliveredSwitch;
    private List<Boolean> isValidInput = new ArrayList<>();
    private User user;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private ValidationHelper validationHelper = new ValidationHelper();
    private GPSHelper gps;
    private ActiveOrdersHelper activeOrdersHelper = new ActiveOrdersHelper();

    private String orderNo;
    private String customerId;
    private String customerName;
    private String orderSum;
    private String address;
    private String postAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_handler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("USERID", -1);
        gps = new GPSHelper(this);
        user = db.getUser(currentUserId);

        orderNoET = (EditText) findViewById(R.id.input_order_number);
        customerIdET = (EditText) findViewById(R.id.input_customer_id);
        customerNameET = (EditText) findViewById(R.id.input_customer_name);
        orderSumET = (EditText) findViewById(R.id.input_order_sum);
        addressET = (EditText) findViewById(R.id.input_order_address);
        postAddressET = (EditText) findViewById(R.id.input_order_post_address);
        isDeliveredSwitch = (Switch) findViewById(R.id.is_delivered_switch);

        customerIdET.setEnabled(false);
        customerNameET.setEnabled(false);
        orderSumET.setEnabled(false);
        addressET.setEnabled(false);
        postAddressET.setEnabled(false);
        isDeliveredSwitch.setEnabled(false);

        orderNoET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (db.doesFieldExist("Orders", "orderNo", editable.toString())) {

                    String customerIdStr = String.valueOf(db.getOrder(Integer.parseInt(editable.toString())).getCustomerNo());
                    customerIdET.setText(customerIdStr);
                    customerIdET.setEnabled(true);

                    String customerNameStr = String.valueOf(db.getOrder(Integer.parseInt(editable.toString())).getCustomerName());
                    customerNameET.setText(customerNameStr);
                    customerNameET.setEnabled(true);

                    String orderSumStr = String.valueOf(db.getOrder(Integer.parseInt(editable.toString())).getOrderSum());
                    orderSumET.setText(orderSumStr);
                    orderSumET.setEnabled(true);

                    String addressStr = String.valueOf(db.getOrder(Integer.parseInt(editable.toString())).getAddress());
                    addressET.setText(addressStr);
                    addressET.setEnabled(true);

                    String postAddressStr = String.valueOf(db.getOrder(Integer.parseInt(editable.toString())).getPostAddress());
                    postAddressET.setText(postAddressStr);
                    postAddressET.setEnabled(true);

                    isDeliveredSwitch.setEnabled(true);

                    if (db.getOrder(Integer.parseInt(orderNoET.getText().toString())).getIsDelivered()) {
                        isDeliveredSwitch.setChecked(true);
                    }
                } else {
                    customerIdET.setEnabled(false);
                    customerNameET.setEnabled(false);
                    orderSumET.setEnabled(false);
                    addressET.setEnabled(false);
                    postAddressET.setEnabled(false);
                    isDeliveredSwitch.setEnabled(false);

                    isDeliveredSwitch.setChecked(false);

                    customerIdET.setText("");
                    customerNameET.setText("");
                    orderSumET.setText("");
                    addressET.setText("");
                    postAddressET.setText("");
                }
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_order_handler);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        setUpNavigationView();
        View header = navigationView.getHeaderView(0);
        TextView currentUserNameTV = (TextView) header.findViewById(R.id.current_user_name);
        try {
            String currentUserNameStr = " " + user.getName();
            currentUserNameTV.setText(currentUserNameStr);
        } catch (Exception e) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityCompat.finishAffinity(OrderHandlerActivity.this);
            startActivity(intent);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        orderNoET.setText("");
        customerIdET.setText("");
        customerNameET.setText("");
        orderSumET.setText("");
        addressET.setText("");
        postAddressET.setText("");
        super.onResume();
    }

    private void setUpNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navList);
        // Disabling menu-item for this activity and admin options for non-admin users
        navigationView.getMenu().findItem(R.id.navDrawer_admin_orderhandler).setVisible(false);
        try {
            if (!user.getIsAdmin()) {
                navigationView.getMenu().findItem(R.id.navDrawer_admin_orderhandler).setVisible(false);
                navigationView.getMenu().findItem(R.id.navDrawer_admin_userhandler).setVisible(false);
            }
        } catch (Exception e) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityCompat.finishAffinity(OrderHandlerActivity.this);
            startActivity(intent);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent;
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navDrawer_settings:
                        intent = new Intent(OrderHandlerActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_admin_userhandler:
                        intent = new Intent(OrderHandlerActivity.this, UserHandlerActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_activeorders:
                        intent = new Intent(OrderHandlerActivity.this, ActiveOrdersActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_orderhistory:
                        intent = new Intent(OrderHandlerActivity.this, OrderHistoryActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;


                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.toolbar_update_order) {
            cleanAllFields();
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

    /**
     * This method opens NewOrderActivity where the user can add new orders.
     * @param view The view component that is executed by click handler.
     */
    public void addOrder(View view) {
        Intent intent = new Intent(this, NewOrderActivity.class);
        startActivity(intent);
    }

    /**
     * This method edits an order in the database from according to user input.
     * @param view The view component that is executed by click handler.
     */
    public void editOrder(View view) {
        if (!orderNoET.getText().toString().equals("") && db.doesFieldExist("Orders", "orderNo", orderNoET.getText().toString())) {
            orderInputValidation();
            Order order = db.getOrder(Integer.parseInt(orderNo));
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String currentDate = df.format(Calendar.getInstance().getTime());
            order.setDeliveryDate(currentDate);
            if (validationHelper.isAllTrue(isValidInput)) {
                isDeliveredSwitch = (Switch) findViewById(R.id.is_delivered_switch);
                Order editedOrder = new Order(Integer.parseInt(orderNo), Integer.parseInt(customerId),
                        customerName, address, postAddress, Integer.parseInt(orderSum), order.getDeliveryDate(), isDeliveredSwitch.isChecked(),
                        order.getDeliveredBy(), gps.getLongitude(address + ", " + postAddress), gps.getLatitude(address + ", " + postAddress), order.getSignature());
                db.editOrder(editedOrder);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_orderhandler_updated), Toast.LENGTH_SHORT).show();
            }
            isValidInput.clear();
        } else if (orderNoET.getText().toString().equals("")) {
            Toast.makeText(this, "Ordernummer är tomt!", Toast.LENGTH_LONG).show();
        } else if (!db.doesFieldExist("Orders", "orderNo", orderNoET.getText().toString()) && !orderNoET.getText().toString().equals("")) {
            Toast.makeText(this, "Ordernumret finns inte!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method deletes an order from database selected by user input.
     * @param view The view component that is executed by click handler.
     */
    public void deleteOrder(View view) {
        if (!orderNoET.getText().toString().equals("")) {
            isValidInput.clear();
            orderNo = orderNoET.getText().toString();
            Order order = db.getOrder(Integer.parseInt(orderNo));
            isValidInput.add(validationHelper.validateInputNumber(orderNo, "Ordernummer", this));

            if (validationHelper.isAllTrue(isValidInput) && db.doesFieldExist("Orders", "orderNo", orderNo)) {
                db.deleteOrder(order);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_orderhandler_deleted), Toast.LENGTH_SHORT).show();
                cleanAllFields();
            }
            activeOrdersHelper.updateOrdersDisplayed();
        } else {
            Toast.makeText(this, getResources().getString(R.string.toast_orderhandler_orderno), Toast.LENGTH_LONG).show();
        }
    }

    private void cleanAllFields() {
        orderNoET.setText("");
        customerIdET.setText("");
        customerNameET.setText("");
        orderSumET.setText("");
        addressET.setText("");
        postAddressET.setText("");
    }

    private void orderInputValidation(){
        orderNoET = (EditText) findViewById(R.id.input_order_number);
        orderNo = orderNoET.getText().toString();
        isValidInput.add(validationHelper.validateInputNumber(orderNo, "Ordernummer", this));

        customerIdET = (EditText) findViewById(R.id.input_customer_id);
        customerId = customerIdET.getText().toString();
        isValidInput.add(validationHelper.validateInputNumber(customerId, "Kundnummer", this));

        customerNameET = (EditText) findViewById(R.id.input_customer_name);
        customerName = customerNameET.getText().toString();
        isValidInput.add(validationHelper.validateInputText(customerName, "Namn", this));

        orderSumET = (EditText) findViewById(R.id.input_order_sum);
        orderSum = orderSumET.getText().toString();
        isValidInput.add(validationHelper.validateInputNumber(orderSum, "Ordersumma", this));

        addressET = (EditText) findViewById(R.id.input_order_address);
        address = addressET.getText().toString();
        isValidInput.add(validationHelper.validateInputText(address, "Address", this));

        postAddressET = (EditText) findViewById(R.id.input_order_post_address);
        postAddress = postAddressET.getText().toString();
        isValidInput.add(validationHelper.validateInputText(postAddress, "Postadress", this));

        isDeliveredSwitch = (Switch) findViewById(R.id.is_delivered_switch);
    }
}
