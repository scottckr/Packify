package com.scottcrocker.packify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.scottcrocker.packify.model.Order;

import java.util.ArrayList;
import java.util.List;

public class ActiveOrdersActivity extends AppCompatActivity {
    private ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_orders);

        List<String> listViewArray = new ArrayList<>();

        listViewArray.add("Order 1");
        listViewArray.add("Order 2");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listViewArray);

        listView = (ListView)findViewById(R.id.active_orders_listview);
        listView.setAdapter(adapter);
    }
}
