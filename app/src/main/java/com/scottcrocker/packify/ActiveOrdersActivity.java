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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.scottcrocker.packify.controller.OrderViewAdapter;
import com.scottcrocker.packify.helper.OrderHandlerHelper;
import com.scottcrocker.packify.helper.RandomHelper;
import com.scottcrocker.packify.model.Order;
import com.scottcrocker.packify.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;
import static com.scottcrocker.packify.MainActivity.db;


public class ActiveOrdersActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String TAG = "ActiveOrdersActivity";
    ListView listView;
    User user;
    int currentUserId;
    int amountOfOrders;
    List<Order> allOrders;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    TextView currentUserName;
    NavigationView navigationView;
    OrderHandlerHelper orderHandlerHelper = new OrderHandlerHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("USERID", -1);
        amountOfOrders = Integer.parseInt(sharedPreferences.getString("seekBarValue", "30"));
        user = db.getUser(currentUserId);
        allOrders = db.getAllOrders();

        final OrderViewAdapter adapter = refreshView();

        TextView emptyText = (TextView)findViewById(R.id.active_orders_empty);
        listView.setEmptyView(emptyText);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Order selectedOrder = adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), SpecificOrderActivity.class);
                intent.putExtra("ORDERNO", selectedOrder.getOrderNo());
                startActivity(intent);
            }
        });


        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_active_orders);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setUpNavigationView();
        View header = navigationView.getHeaderView(0);
        currentUserName = (TextView) header.findViewById(R.id.current_user_name);
        try {
            currentUserName.setText(user.getName());
        } catch (Exception e) {
            sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityCompat.finishAffinity(ActiveOrdersActivity.this);
            startActivity(intent);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        refreshView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setUpNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navList);

        // Disabling menu-item for this activity and admin options for non-admin users
        navigationView.getMenu().findItem(R.id.navDrawer_activeorders).setVisible(false);
        try {
            if (!user.getIsAdmin()) {
                navigationView.getMenu().findItem(R.id.navDrawer_admin_orderhandler).setVisible(false);
                navigationView.getMenu().findItem(R.id.navDrawer_admin_userhandler).setVisible(false);
            }
        } catch (Exception e) {
            sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityCompat.finishAffinity(ActiveOrdersActivity.this);
            startActivity(intent);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent;
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navDrawer_settings:
                        intent = new Intent(ActiveOrdersActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_admin_userhandler:
                        intent = new Intent(ActiveOrdersActivity.this, UserHandlerActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_admin_orderhandler:
                        intent = new Intent(ActiveOrdersActivity.this, OrderHandlerActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_orderhistory:
                        intent = new Intent(ActiveOrdersActivity.this, OrderHistoryActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                }
                return false;
            }
        });
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
            orderHandlerHelper.updateOrdersDisplayedAndDelivered();
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

    /**
     * Refreshes the view with the right amount of delivered orders.
     */
    public OrderViewAdapter refreshView() {
        final OrderViewAdapter adapter = new OrderViewAdapter(this, Order.getCurrentListedOrders(), R.mipmap.package_undelivered);
        listView = (ListView) findViewById(R.id.active_orders_listview);
        listView.setAdapter(adapter);
        Log.d(TAG, "ListView finished");
        return adapter;
    }

    /*
                    for (int y = 0; y < Order.getCurrentListedOrders().size(); y++) {//kollar igenom alla Orders och ser om någon redan finns.
                    if (undeliveredOrders.get(rndOrder).getOrderNo() == Order.getCurrentListedOrders().get(y).getOrderNo()) {
                        orderDuplicateCheck = true;
                    }
                }
                if (orderDuplicateCheck) {
                    i--;
                    orderDuplicateCheck = false;
                } else {
                    if (Order.getCurrentListedOrders().size() < amountOfOrdersToDisplay) {
                        Order.getCurrentListedOrders().add(undeliveredOrders.get(rndOrder));
                    }
                }
     */

}