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

import static com.scottcrocker.packify.MainActivity.db;

/**
 * Custom adapter for displaying Orders in ListViews.
 *
 * @author Scott Crocker
 */
public class OrderViewAdapter extends BaseAdapter {

    private int iconId;
    private List<Order> dataSource;
    private LayoutInflater inflater;

    /**
     * Constructor for OrderViewAdapter.
     *
     * @param activity Which activity the adapter is used.
     * @param orders   What items to use in the adapter.
     * @param iconId   What icon to display on the items.
     */
    public OrderViewAdapter(Activity activity, List<Order> orders, int iconId) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.iconId = iconId;
        dataSource = orders;
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Order getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = inflater.inflate(R.layout.list_item, parent, false);
        } else {
            v = convertView;
        }
        ImageView icon = (ImageView) v.findViewById(R.id.list_item_icon);
        icon.setImageResource(iconId);
        TextView orderLabelTv = (TextView) v.findViewById(R.id.list_item_label);
        String orderLabelOne = "Ordernummer: " + dataSource.get(pos).getOrderNo();
        orderLabelTv.setText(orderLabelOne);
        orderLabelTv = (TextView) v.findViewById(R.id.list_item_label2);
        String orderLabelTwo = "Adress: " + db.getOrder(dataSource.get(pos).getOrderNo()).getAddress() + ", " + db.getOrder(dataSource.get(pos).getOrderNo()).getPostAddress();
        orderLabelTv.setText(orderLabelTwo);
        return v;
    }
    public void clearAdapter(){
        dataSource.clear();
        notifyDataSetChanged();
        dataSource.addAll(Order.getCurrentListedOrders());
    }
}
