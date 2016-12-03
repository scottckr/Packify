package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.scottcrocker.packify.helper.ValidationHelper;
import com.scottcrocker.packify.model.Order;
import com.scottcrocker.packify.model.User;

import java.util.ArrayList;
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
    Switch isDeliveredSwitch;
    Button addOrderBtn;
    Button editOrderBtn;
    List<Boolean> isValidInput = new ArrayList<>();
    SharedPreferences sharedPreferences;
    int currentUserId;
    User user;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    TextView currentUserName;
    NavigationView navigationView;
    ValidationHelper validationHelper = new ValidationHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_handler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = db.getUser(currentUserId);

        addOrderBtn = (Button) findViewById(R.id.btn_submit_order);
        editOrderBtn = (Button) findViewById(R.id.btn_edit_order);
        editOrderBtn.setVisibility(View.INVISIBLE);
        orderNoET = (EditText) findViewById(R.id.input_order_number);
        customerIdET = (EditText) findViewById(R.id.input_customer_id);
        customerNameET = (EditText) findViewById(R.id.input_customer_name);
        orderSumET = (EditText) findViewById(R.id.input_order_sum);
        addressET = (EditText) findViewById(R.id.input_order_address);
        postAddressET = (EditText) findViewById(R.id.input_order_post_address);
        isDeliveredSwitch = (Switch) findViewById(R.id.is_delivered_switch);
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

                    if (db.getOrder(Integer.parseInt(orderNoET.getText().toString())).getIsDelivered()) {
                        isDeliveredSwitch.setChecked(true);
                    }
                } else {
                    isDeliveredSwitch.setChecked(false);
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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_order_handler);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setUpNavigationView();
        View header = navigationView.getHeaderView(0);
        currentUserName = (TextView) header.findViewById(R.id.current_user_name);
        currentUserName.setText(user.getName());
    }

    private void setUpNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navList);

        // Disabling menu-item for this activity and admin options for non-admin users
        navigationView.getMenu().findItem(R.id.navDrawer_admin_orderhandler).setVisible(false);
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
                        intent = new Intent(OrderHandlerActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.navDrawer_admin_userhandler:
                        intent = new Intent(OrderHandlerActivity.this, UserHandlerActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.navDrawer_activeorders:
                        intent = new Intent(OrderHandlerActivity.this, ActiveOrdersActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.navDrawer_orderhistory:
                        intent = new Intent(OrderHandlerActivity.this, OrderHistoryActivity.class);
                        startActivity(intent);
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

    //What's this? What's this? Whaaaaaat iiiiiiiis thiiiiiiis?
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    /**
     * Method to create a new order object, or handle an existing order object which will be sent to DB
     *
     * @param view
     */
    // TO-DO: create new object containing order information, send to database
    public void addOrder(View view) {

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("USERID", -1);


        orderNoET = (EditText) findViewById(R.id.input_order_number);
        String orderNo = orderNoET.getText().toString();
        isValidInput.add(validationHelper.validateInputNumber(orderNo, "Ordernummer", this));

        customerIdET = (EditText) findViewById(R.id.input_customer_id);
        String customerId = customerIdET.getText().toString();
        isValidInput.add(validationHelper.validateInputNumber(customerId, "Kundnummer", this));

        customerNameET = (EditText) findViewById(R.id.input_customer_name);
        String customerName = customerNameET.getText().toString();
        isValidInput.add(validationHelper.validateInputText(customerName, "Namn", this));

        orderSumET = (EditText) findViewById(R.id.input_order_sum);
        String orderSum = orderSumET.getText().toString();
        isValidInput.add(validationHelper.validateInputNumber(orderSum, "Ordersumma", this));

        addressET = (EditText) findViewById(R.id.input_order_address);
        String address = addressET.getText().toString();
        isValidInput.add(validationHelper.validateInputText(address, "Address", this));

        postAddressET = (EditText) findViewById(R.id.input_order_post_address);
        String postAddress = postAddressET.getText().toString();
        isValidInput.add(validationHelper.validateInputText(address, "Postadress", this));

        isDeliveredSwitch = (Switch) findViewById(R.id.is_delivered_switch);

        if (validationHelper.isAllTrue(isValidInput) && !MainActivity.db.doesFieldExist("Orders", "orderNo", orderNo)) {
            Order order = new Order(Integer.parseInt(orderNo), Integer.parseInt(customerId),
                    customerName, address, postAddress, Integer.parseInt(orderSum), "---",
                    isDeliveredSwitch.isChecked(), MainActivity.db.getUser(currentUserId).getId(), MainActivity.gps.getLongitude(address),
                    MainActivity.gps.getLatitude(address), null);

            MainActivity.db.addOrder(order);
            Toast.makeText(getApplicationContext(), "Order sparad", Toast.LENGTH_SHORT).show();
        } else if (MainActivity.db.doesFieldExist("Orders", "orderNo", orderNo)) {
            Toast.makeText(getApplicationContext(), "Ordernumret finns redan!", Toast.LENGTH_LONG).show();
        }
        refreshView();
        isValidInput.clear();
    }

    /**
     * Method to edit order from DB
     *
     * @param view
     */
    public void editOrder(View view) {
        Order order = db.getOrder(Integer.parseInt(orderNoET.getText().toString()));
        isDeliveredSwitch = (Switch) findViewById(R.id.is_delivered_switch);
        Order editedOrder = new Order(order.getOrderNo(), Integer.parseInt(customerIdET.getText().toString()),
                customerNameET.getText().toString(), addressET.getText().toString(), postAddressET.getText().toString(),
                Integer.parseInt(orderSumET.getText().toString()), order.getDeliveryDate(), isDeliveredSwitch.isChecked(),
                order.getDeliveredBy(), order.getLongitude(), order.getLatitude(), order.getSignature());
        db.editOrder(editedOrder);
        refreshView();
    }

    /**
     * Method to delete order from DB
     *
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

}

