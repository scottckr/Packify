package com.scottcrocker.packify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.scottcrocker.packify.controller.OrderViewAdapter;
import com.scottcrocker.packify.model.Order;
import com.scottcrocker.packify.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.scottcrocker.packify.MainActivity.SHARED_PREFERENCES;

public class OrderHistoryActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String TAG = "OrderHistoryActivity";
    private ListView historyListView;
    User user;
    int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("USERID", -1);
        user = MainActivity.db.getUser(currentUserId);

        List<Order> deliveredOrders = new ArrayList<>();

        for (int i = 0; i < MainActivity.db.getAllOrders().size(); i++) {
            if (MainActivity.db.getAllOrders().get(i).getIsDelivered()) {
                deliveredOrders.add(MainActivity.db.getAllOrders().get(i));
            }
        }

        final OrderViewAdapter adapter = new OrderViewAdapter(this, deliveredOrders, R.drawable.parcel_delivered);

        historyListView = (ListView) findViewById(R.id.order_history_listview);
        historyListView.setAdapter(adapter);

        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Order selectedOrder = adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), SpecificOrderActivity.class);
                intent.putExtra("ORDERNO", selectedOrder.getOrderNo());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.getItem(3).setVisible(false);

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

            case R.id.toolbar_activeorders:
                intent = new Intent(this, ActiveOrdersActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //TODO Check if method works
    public void refreshView() {
        List<Order> deliveredOrders = new ArrayList<>();

        for (int i = 0; i < MainActivity.db.getAllOrders().size(); i++) {
            if (MainActivity.db.getAllOrders().get(i).getIsDelivered()) {
                deliveredOrders.add(MainActivity.db.getAllOrders().get(i));
            }
        }

        final OrderViewAdapter adapter = new OrderViewAdapter(this, deliveredOrders, R.drawable.parcel_delivered);

        historyListView = (ListView) findViewById(R.id.order_history_listview);
        historyListView.setAdapter(adapter);
        Log.d(TAG, "ListView refreshed");
    }
}
