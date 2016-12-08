package com.scottcrocker.packify.helper;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * GPSHelper class, has methods for getting longitude and latitude from an address.
 */

public class GPSHelper extends AppCompatActivity {
    Context context;

    /**
     * Constructor for GPSHelper class.
     *
     * @param context Takes a context to use the GPSHelper in.
     */
    public GPSHelper(Context context) {
        this.context = context;
    }

    private static final String TAG = "GPSHelper";

    /**
     * This method takes an address and returns a double with the longitude coordinate of the address.
     *
     * @param address
     * @return longitude
     */
    public double getLongitude(String address) {
        Geocoder gc = new Geocoder(context);
        List<Address> addressList;
        double longitude = 0;
        try {
            addressList = gc.getFromLocationName(address, 1);
        } catch (IOException e) {
            Log.d(TAG, "Could not get longitude!");
            return longitude;
        }

        if (addressList.size() > 0) {
            longitude = addressList.get(0).getLongitude();
        }
        Log.d(TAG, "Longitude: " + longitude);
        return longitude;
    }

    /**
     * This method takes an address and returns a double with the latitude coordinate of the address.
     *
     * @param address
     * @return latitude
     */
    public double getLatitude(String address) {
        Geocoder gc = new Geocoder(context);
        List<Address> addressList;
        double latitude = 0;
        try {
            addressList = gc.getFromLocationName(address, 1);
        } catch (IOException e) {
            Log.d(TAG, "Could not get latitude!");
            return latitude;
        }

        if (addressList.size() > 0) {
            latitude = addressList.get(0).getLatitude();
        }
        Log.d(TAG, "Latitude: " + latitude);
        return latitude;
    }

}
