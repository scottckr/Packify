package com.scottcrocker.packify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_orders);

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
}
