package com.scottcrocker.packify;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.scottcrocker.packify.helper.GPSHelper;

public class SpecificOrderActivity extends AppCompatActivity {

    int orderId;
    GPSHelper gpsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_order);
        gpsHelper = new GPSHelper();
    }

    public void deliverOrder(View view) {
        //TODO Send delivery-information to database and reset view
    }

    public void openNavigation(View view) {
        //TODO Open google maps with coordinates. Not Amphitheater Parkway!
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway%2C+CA"));
        startActivity(intent);
    }
}
