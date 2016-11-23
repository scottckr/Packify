package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.Toast;

import com.scottcrocker.packify.model.Order;
import com.scottcrocker.packify.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;


public class ActiveOrdersActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String TAG = "ActiveOrdersActivity";
    ListView listView;
    User user;
    int currentUserId;
    int amountOfOrdersDisplayed;

    //Navigation Drawers variables
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("USERID", -1);
        int amountOfOrders = Integer.parseInt(sharedPreferences.getString("seekBarValue", "30"));
        user = MainActivity.db.getUser(currentUserId);

        List<Order> undeliveredOrders = new ArrayList<>();

        if (amountOfOrders > MainActivity.db.getAllOrders().size()) {
            amountOfOrdersDisplayed = MainActivity.db.getAllOrders().size();
        } else {
            amountOfOrdersDisplayed = amountOfOrders;
        }

        for (int i = 0; i < amountOfOrdersDisplayed; i++) {
            if (!MainActivity.db.getAllOrders().get(i).getIsDelivered()) {
                undeliveredOrders.add(MainActivity.db.getAllOrders().get(i));
            }
        }


        final ArrayAdapter<Order> adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.list_item_label, undeliveredOrders);

        listView = (ListView) findViewById(R.id.active_orders_listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Order selectedOrder = adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), SpecificOrderActivity.class);
                intent.putExtra("ORDERNO", selectedOrder.getOrderNo());
                startActivity(intent);
            }
        });

        // Navigation Drawer stuff
        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        setupDrawer();
        //

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.getItem(2).setVisible(false);

       /*Log.d(TAG, "Current user id: " + currentUserId + " // User is admin: " + user.getIsAdmin());
        if (user.getIsAdmin()) {
            Log.d(TAG, "Showing admin choices in toolbar menu");

        } else {
            Log.d(TAG, "Disabling admin choices in toolbar menu");
            menu.getItem(4).setVisible(false);
            menu.getItem(5).setVisible(false);
        }*/

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

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

            case R.id.toolbar_orderhistory:
                intent = new Intent(this, OrderHistoryActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    private void addDrawerItems() {
        String[] itemList = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemList);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ActiveOrdersActivity.this, "You clicked something" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupDrawer() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Meny");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    //TODO Check if method works
    public void refreshView() {
        List<Order> allOrders = MainActivity.db.getAllOrders();
        List<Order> undeliveredOrders = new ArrayList<>();

        for (int i = 0; i < allOrders.size(); i++) {
            if (!allOrders.get(i).getIsDelivered()) {
                undeliveredOrders.add(allOrders.get(i));
            }
        }

        final ArrayAdapter<Order> adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.list_item_label, undeliveredOrders);

        listView = (ListView) findViewById(R.id.active_orders_listview);
        listView.setAdapter(adapter);
        Log.d(TAG, "ListView refreshed");
    }
}
