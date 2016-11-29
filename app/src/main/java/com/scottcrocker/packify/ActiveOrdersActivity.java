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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.scottcrocker.packify.helper.RandomHelper;
import com.scottcrocker.packify.model.Order;
import com.scottcrocker.packify.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;


public class ActiveOrdersActivity extends AppCompatActivity{

    SharedPreferences sharedPreferences;
    private static final String TAG = "ActiveOrdersActivity";
    ListView listView;
    User user;
    int currentUserId;
    int amountOfOrdersToDisplay;
    int amountOfOrders;
    int amountOfNewOrdersNeeded;
    int amountOfOrdersToRemove;
    List<Order> undeliveredOrders = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("USERID", -1);
        amountOfOrders = Integer.parseInt(sharedPreferences.getString("seekBarValue", "30"));
        user = MainActivity.db.getUser(currentUserId);

        int undeliveredOrderAmount = filterUndeliveredOrders().size();
        if (amountOfOrders > undeliveredOrderAmount) {
            amountOfOrdersToDisplay = undeliveredOrderAmount;
        } else {
            amountOfOrdersToDisplay = amountOfOrders;
        }

        cleanCurrentOrders();
        refreshOrders();

        final ArrayAdapter<Order> adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.list_item_label, Order.getCurrentListedOrders());

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


        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_active_orders);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setUpNavigationView();

    }

    private void setUpNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navList);

        // Disabling menu-item for this activity and admin options for non-admin users
        navigationView.getMenu().findItem(R.id.navDrawer_activeorders).setVisible(false);
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
                        intent = new Intent(ActiveOrdersActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.navDrawer_admin_userhandler:
                        intent = new Intent(ActiveOrdersActivity.this, UserHandlerActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.navDrawer_admin_orderhandler:
                        intent = new Intent(ActiveOrdersActivity.this, OrderHandlerActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.navDrawer_orderhistory:
                        intent = new Intent(ActiveOrdersActivity.this, OrderHistoryActivity.class);
                        startActivity(intent);
                        return true;


                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.getItem(2).setVisible(false);
        // TODO: Delete items from toolbar_menu.xml after implementing the navDrawer on all activities
        menu.getItem(1).setVisible(false);
        menu.getItem(3).setVisible(false);
        menu.getItem(4).setVisible(false);
        menu.getItem(5).setVisible(false);
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


    //What's this? What's this? Whaaaaaat iiiiiiiis thiiiiiiis?
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Refreshes the view with the right amount of delivered orders.
     */
    public void refreshView() {
        cleanCurrentOrders();
        refreshOrders();

        final ArrayAdapter<Order> adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.list_item_label, Order.getCurrentListedOrders());
        listView = (ListView) findViewById(R.id.active_orders_listview);
        listView.setAdapter(adapter);
        Log.d(TAG, "ListView refreshed");
    }

    /**
     * Updates the orderlist so it only contains undelivered orders and sets a value of how many new orders that is needed.
     */
    public void cleanCurrentOrders(){
        List<Order> tempOrders = new ArrayList<>();
        for (int i = 0; i < Order.getCurrentListedOrders().size(); i++){
            if(!Order.getCurrentListedOrders().get(i).getIsDelivered()){
                boolean x = Order.getCurrentListedOrders().get(i).getIsDelivered();
                tempOrders.add(Order.getCurrentListedOrders().get(i));
            }
        }
        Order.getCurrentListedOrders().clear();
        for (int i = 0; i < tempOrders.size(); i++){
            Order.getCurrentListedOrders().add(tempOrders.get(i));
        }
    }

    public void refreshOrders(){
        if (amountOfOrdersToDisplay > Order.getCurrentListedOrders().size()){
            //utöka med random
            filterUndeliveredOrders();
            amountOfNewOrdersNeeded = amountOfOrdersToDisplay - Order.getCurrentListedOrders().size();
            addRandomOrders();
        }else if(amountOfOrdersToDisplay < Order.getCurrentListedOrders().size()){
            //ta bort överflödiga orders.
            amountOfOrdersToRemove = Order.getCurrentListedOrders().size() - amountOfOrdersToDisplay;
            removeOrders();
        }else{
            //no change.
        }

    }
    public void removeOrders(){
        int amountToSave = Order.getCurrentListedOrders().size()-amountOfOrdersToRemove;
        List<Order> tempOrders = new ArrayList<>();

        for(int i = 0; i < amountToSave; i++){
            tempOrders.add(Order.getCurrentListedOrders().get(i));
        }
        Order.getCurrentListedOrders().clear();
        for (int i = 0; i < tempOrders.size(); i++){
            Order.getCurrentListedOrders().add(tempOrders.get(i));
        }
    }

    //
    public void addRandomOrders(){
        RandomHelper rnd = new RandomHelper();
        int rndOrder;
        boolean orderDuplicateCheck = false;
        for(int i = 0; i < amountOfNewOrdersNeeded; i++){//kör antalet gånger som behövs
            rndOrder = rnd.randomNrGenerator(undeliveredOrders.size());//hittar en ny random order
            for(int y = 0; y < Order.getCurrentListedOrders().size(); y++){//kollar igenom alla Orders och ser om någon redan finns.
                if(undeliveredOrders.get(rndOrder).getOrderNo() == Order.getCurrentListedOrders().get(y).getOrderNo()){
                    orderDuplicateCheck = true;
                }
            }
            if (orderDuplicateCheck){
                i--;
                orderDuplicateCheck = false;
            }else{
                if (Order.getCurrentListedOrders().size() < amountOfOrdersToDisplay){
                    Order.getCurrentListedOrders().add(undeliveredOrders.get(rndOrder));
                }
            }
        }
    }

    public List<Order> filterUndeliveredOrders(){

        for (int i = 0; i < MainActivity.db.getAllOrders().size(); i++) {
            if (!MainActivity.db.getAllOrders().get(i).getIsDelivered()) {
                undeliveredOrders.add(MainActivity.db.getAllOrders().get(i));
            }
        }
        return undeliveredOrders;
    }

}
