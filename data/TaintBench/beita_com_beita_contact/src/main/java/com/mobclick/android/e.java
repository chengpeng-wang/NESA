package com.mobclick.android;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class e {
    private LocationManager a;
    private Context b;

    public e(Context context) {
        this.b = context;
    }

    public Location a() {
        try {
            Location lastKnownLocation;
            this.a = (LocationManager) this.b.getSystemService("location");
            if (l.a(this.b, "android.permission.ACCESS_FINE_LOCATION")) {
                lastKnownLocation = this.a.getLastKnownLocation("gps");
                if (lastKnownLocation != null) {
                    Log.i(UmengConstants.LOG_TAG, "get location from gps:" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude());
                    return lastKnownLocation;
                }
            }
            if (l.a(this.b, "android.permission.ACCESS_COARSE_LOCATION")) {
                lastKnownLocation = this.a.getLastKnownLocation("network");
                if (lastKnownLocation != null) {
                    Log.i(UmengConstants.LOG_TAG, "get location from network:" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude());
                    return lastKnownLocation;
                }
            }
            Log.i(UmengConstants.LOG_TAG, "Could not get location from GPS or Cell-id, lack ACCESS_COARSE_LOCATION or ACCESS_COARSE_LOCATION permission?");
            return null;
        } catch (Exception e) {
            Log.e(UmengConstants.LOG_TAG, e.getMessage());
            return null;
        }
    }
}
