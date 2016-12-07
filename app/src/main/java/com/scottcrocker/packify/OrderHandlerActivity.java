package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.scottcrocker.packify.helper.GPSHelper;
import com.scottcrocker.packify.helper.ValidationHelper;
import com.scottcrocker.packify.model.Order;
import com.scottcrocker.packify.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    TextView currentUserName;
    NavigationView navigationView;
    ValidationHelper validationHelper = new ValidationHelper();

    String orderNo;
    String customerId;
    String customerName;
    String orderSum;
    String address;
    String postAddress;

    GPSHelper gps = new GPSHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_handler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = db.getUser(currentUserId);

        addOrderBtn = (Button) findViewById(R.id.btn_submit_order);
        editOrderBtn = (Button) findViewById(R.id.btn_edit_order);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setUpNavigationView();
        View header = navigationView.getHeaderView(0);
        currentUserName = (TextView) header.findViewById(R.id.current_user_name);
        String currentUserNameStr = " " + user.getName();
        currentUserName.setText(currentUserNameStr);
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
     * Method to create a new order object, or handle an existing order object which will be sent to DB
     * @param view
     */
    public void addOrder(View view) {

        Intent intent = new Intent(this, NewOrderActivity.class);
        startActivity(intent);
    }

    /**
     * Method to edit order from DB
     * @param view
     */
    public void editOrder(View view) {

        orderNo = orderNoET.getText().toString();
        if (!validationHelper.validateInputText(orderNo, "Ordernummer", this)) {
            return;
        }
        if (validationHelper.orderExist(this, orderNo)) {
            orderInputValidation();
            Order order = db.getOrder(Integer.parseInt(orderNo));
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String currentDate = df.format(Calendar.getInstance().getTime());
            order.setDeliveryDate(currentDate);
            if(validationHelper.isAllTrue(isValidInput)){
                isDeliveredSwitch = (Switch) findViewById(R.id.is_delivered_switch);
                Order editedOrder = new Order(Integer.parseInt(orderNo), Integer.parseInt(customerId),
                        customerName, address, postAddress,Integer.parseInt(orderSum), order.getDeliveryDate(), isDeliveredSwitch.isChecked(),
                        order.getDeliveredBy(), gps.getLongitude(address + ", "+ postAddress), gps.getLatitude(address + ", "+ postAddress), order.getSignature());
                db.editOrder(editedOrder);
                Toast.makeText(getApplicationContext(), "Order ändrad", Toast.LENGTH_SHORT).show();
            }
            isValidInput.clear();
        }
    }

    /**
     * Method to delete order from DB
     * @param view
     */
    public void deleteOrder(View view) {
        if (!orderNoET.getText().toString().equals("")) {
            orderNo = orderNoET.getText().toString();
            Order order = db.getOrder(Integer.parseInt(orderNo));
            isValidInput.add(validationHelper.validateInputNumber(orderNo, "Ordernummer", this));

            if(validationHelper.isAllTrue(isValidInput) && validationHelper.orderExist(this, orderNo)){
                db.deleteOrder(order);
                Toast.makeText(getApplicationContext(), "Order raderad", Toast.LENGTH_SHORT).show();
                cleanAllFields();
            }
            isValidInput.clear();
        } else {
            Toast.makeText(this, "Du måste fylla i ett ordernummer!", Toast.LENGTH_LONG).show();
        }
    }

    public void cleanAllFields() {
        orderNoET.setText("");
        customerIdET.setText("");
        customerNameET.setText("");
        orderSumET.setText("");
        addressET.setText("");
        postAddressET.setText("");
    }

    public void orderInputValidation(){
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
