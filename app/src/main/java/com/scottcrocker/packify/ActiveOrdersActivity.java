package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.scottcrocker.packify.controller.OrderViewAdapter;
import com.scottcrocker.packify.helper.ActiveOrdersHelper;
import com.scottcrocker.packify.model.Order;
import com.scottcrocker.packify.model.User;

import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;
import static com.scottcrocker.packify.MainActivity.db;

/**
 * ActiveOrdersActivity, displays a ListView of undelivered Order objects.
 */
public class ActiveOrdersActivity extends AppCompatActivity {

    private static final String TAG = "ActiveOrdersActivity";
    private ListView listView;
    private User user;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private ActiveOrdersHelper activeOrdersHelper = new ActiveOrdersHelper();
    private OrderViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("USERID", -1);
        user = db.getUser(currentUserId);


        activeOrdersHelper.setSeekBarValue(Integer.parseInt(sharedPreferences.getString("seekBarValue", "10")));
        activeOrdersHelper.updateOrdersDisplayed();
        adapter = new OrderViewAdapter(this, Order.getCurrentListedOrders(), R.mipmap.package_undelivered);
        refreshView();

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_active_orders);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        setUpNavigationView();
        View header = navigationView.getHeaderView(0);

        TextView emptyTextTV = (TextView)findViewById(R.id.active_orders_empty);
        TextView currentUserNameTV = (TextView) header.findViewById(R.id.current_user_name);

        listView.setEmptyView(emptyTextTV);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Order selectedOrder = adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), SpecificOrderActivity.class);
                intent.putExtra("ORDERNO", selectedOrder.getOrderNo());
                startActivity(intent);
            }
        });

        try {
            String currentUserNameStr = " " + user.getName();
            currentUserNameTV.setText(currentUserNameStr);
        } catch (Exception e) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityCompat.finishAffinity(ActiveOrdersActivity.this);
            startActivity(intent);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        activeOrdersHelper.updateOrdersDisplayed();
        refreshView();

    }

    @Override
    protected void onStop() {
        super.onStop();
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
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityCompat.finishAffinity(ActiveOrdersActivity.this);
            startActivity(intent);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navDrawer_settings:
                        intent = new Intent(ActiveOrdersActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_admin_userhandler:
                        intent = new Intent(ActiveOrdersActivity.this, UserHandlerActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_admin_orderhandler:
                        intent = new Intent(ActiveOrdersActivity.this, OrderHandlerActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navDrawer_orderhistory:
                        intent = new Intent(ActiveOrdersActivity.this, OrderHistoryActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
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
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.toolbar_update_order) {
            activeOrdersHelper.updateOrdersDisplayedAndDelivered();
            refreshView();
            Toast.makeText(this, getResources().getString(R.string.toast_list_updated), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Refreshes the ListView by checking for undelivered Order objects and sets the adapter once again.
     */
    public void refreshView() {

        listView = (ListView) findViewById(R.id.active_orders_listview);
        listView.setAdapter(adapter);
        Log.d(TAG, "ListView finished");
    }
}