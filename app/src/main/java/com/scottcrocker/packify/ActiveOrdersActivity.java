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
import android.widget.TextView;
import android.widget.Toast;


import com.scottcrocker.packify.controller.OrderViewAdapter;
import com.scottcrocker.packify.helper.RandomHelper;
import com.scottcrocker.packify.model.Order;
import com.scottcrocker.packify.model.User;

import java.util.ArrayList;
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
    List<Order> allOrders = new ArrayList<>();
    List<Order> undeliveredOrders = new ArrayList<>();
    List<Order> deliveredOrders = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    TextView currentUserName;
    NavigationView navigationView;

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
        allOrders = MainActivity.db.getAllOrders();

        //orderAmountToShow();
        //cleanCurrentOrders();
        refreshOrders();

        final OrderViewAdapter adapter = new OrderViewAdapter(this, Order.getCurrentListedOrders(), R.mipmap.package_undelivered);

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
        View header = navigationView.getHeaderView(0);
        currentUserName = (TextView) header.findViewById(R.id.current_user_name);
        currentUserName.setText(user.getName());

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        amountOfOrders = Integer.parseInt(sharedPreferences.getString("seekBarValue", "30"));
        user = MainActivity.db.getUser(currentUserId);
        allOrders = MainActivity.db.getAllOrders();

        //orderAmountToShow();
        //cleanCurrentOrders();
        refreshOrders();

        final OrderViewAdapter adapter = new OrderViewAdapter(this, Order.getCurrentListedOrders(), R.mipmap.package_undelivered);

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
        View header = navigationView.getHeaderView(0);
        currentUserName = (TextView) header.findViewById(R.id.current_user_name);
        String currentUserNameStr = " " + user.getName();
        currentUserName.setText(currentUserNameStr);
    }

    private void setUpNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navList);

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


    //What's this? What's this? Whaaaaaat iiiiiiiis thiiiiiiis? QUE?
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Refreshes the view with the right amount of delivered orders.
     */
    public void refreshView() {

        clearUndeliveredOrders();
        cleanCurrentOrders();
        refreshOrders();

        final OrderViewAdapter adapter = new OrderViewAdapter(this, Order.getCurrentListedOrders(), R.mipmap.package_undelivered);

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

    public void orderAmountToShow(){
        int undeliveredOrderAmount = filterUndeliveredOrders().size();
        if (amountOfOrders > undeliveredOrderAmount) {
            amountOfOrdersToDisplay = undeliveredOrderAmount;
        } else {
            amountOfOrdersToDisplay = amountOfOrders;
        }
    }

    public List<Order> filterUndeliveredOrders(){
        allOrders = MainActivity.db.getAllOrders();
        Log.d(TAG, "Number of orders in database: " + allOrders.size());
        for (int i = 0; i < allOrders.size(); i++) {
            if (!allOrders.get(i).getIsDelivered()) {
                undeliveredOrders.add(allOrders.get(i));
            }
            else{
                deliveredOrders.add(allOrders.get(i));
            }
        }
        return undeliveredOrders;
    }

    public void clearUndeliveredOrders(){
        filterUndeliveredOrders();
        List<Order> tempOrder = new ArrayList<>();
        boolean deliveredOrder = false;
        int amountRemoved;

        for(int i =0; i < Order.getCurrentListedOrders().size(); i++){
            for(int y = 0; y < deliveredOrders.size(); y++){
                if(Order.getCurrentListedOrders().get(i).getOrderNo() == deliveredOrders.get(y).getOrderNo()){
                    deliveredOrder = true;
                }
            }
            if (!deliveredOrder){
                tempOrder.add(Order.getCurrentListedOrders().get(i));
            }else{
                deliveredOrder = false;
            }
        }

        Order.getCurrentListedOrders().clear();
        for(int i =0; i < tempOrder.size(); i++){
            Order.getCurrentListedOrders().add(tempOrder.get(i));
        }
        amountRemoved = amountOfOrdersToDisplay-tempOrder.size();
        if(amountRemoved == 1){
            Toast.makeText(this, amountRemoved + " levererad order togs bort", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, amountRemoved + " levererade ordrar togs bort", Toast.LENGTH_SHORT).show();
        }



    }

}
