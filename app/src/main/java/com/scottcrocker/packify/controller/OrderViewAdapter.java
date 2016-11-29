package com.scottcrocker.packify.controller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scottcrocker.packify.R;
import com.scottcrocker.packify.model.Order;

import java.util.List;

/**
 * Created by Scott on 2016-11-28.
 */

public class OrderViewAdapter extends BaseAdapter {
    public OrderViewAdapter(Activity activity, List<Order> orders, int iconId) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.iconId = iconId;
        dataSource = orders;
    }

    private int iconId;
    private ImageView icon;
    private List<Order> dataSource;
    private LayoutInflater inflater;

    public int getCount() {
        return dataSource.size();
    }

    public Order getItem(int position) {
        return dataSource.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int pos, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = inflater.inflate(R.layout.list_item, parent, false);
        } else {
            v = convertView;
        }
        icon = (ImageView) v.findViewById(R.id.list_item_icon);
        icon.setImageResource(iconId);
        TextView orderLabelTv = (TextView) v.findViewById(R.id.list_item_label);
        String orderLabelOne = "Order: " + dataSource.get(pos).getOrderNo();
        orderLabelTv.setText(orderLabelOne);
        orderLabelTv = (TextView) v.findViewById(R.id.list_item_label2);
        String orderLabelTwo = "Adress: " + dataSource.get(pos).getAddress() + ", " + dataSource.get(pos).getPostAddress();
        orderLabelTv.setText(orderLabelTwo);
        return v;
    }
}
