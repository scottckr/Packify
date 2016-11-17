package com.scottcrocker.packify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.scottcrocker.packify.model.Order;

import java.util.ArrayList;
import java.util.List;

public class ActiveOrdersActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<Order> allOrders = MainActivity.db.getAllOrders();

        List<Order> undeliveredOrders = new ArrayList<>();

        for (int i = 0; i < allOrders.size(); i++) {
            if (!allOrders.get(i).getIsDelivered()) {
                undeliveredOrders.add(allOrders.get(i));
            }
        }

        final ArrayAdapter<Order> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, undeliveredOrders);

        listView = (ListView)findViewById(R.id.active_orders_listview);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.toolbar_update_order:
                //TODO Update view.
                return true;

            case R.id.toolbar_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.toolbar_admin_settings:
                intent = new Intent(this, UserHandlerActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
