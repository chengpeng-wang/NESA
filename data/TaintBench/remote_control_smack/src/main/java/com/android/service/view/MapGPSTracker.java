package com.android.service.view;

import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MapGPSTracker extends Service implements LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 10000;
    boolean canGetLocation = false;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    double latitude = 0.0d;
    Location location;
    protected LocationManager locationManager;
    double longitude = 0.0d;
    /* access modifiers changed from: private|final */
    public final Context mContext;

    public MapGPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            this.locationManager = (LocationManager) this.mContext.getSystemService("location");
            this.isGPSEnabled = this.locationManager.isProviderEnabled("gps");
            this.isNetworkEnabled = this.locationManager.isProviderEnabled("network");
            if (this.isGPSEnabled || this.isNetworkEnabled) {
                this.canGetLocation = true;
                if (this.isNetworkEnabled) {
                    this.locationManager.requestLocationUpdates("network", MIN_TIME_BW_UPDATES, 10.0f, this);
                    Log.d("Network", "Network");
                    if (this.locationManager != null) {
                        this.location = this.locationManager.getLastKnownLocation("network");
                        if (this.location != null) {
                            this.latitude = this.location.getLatitude();
                            this.longitude = this.location.getLongitude();
                            System.out.println("-------Using Network-------");
                        }
                    }
                }
                if (this.isGPSEnabled && this.location == null) {
                    this.locationManager.requestLocationUpdates("gps", MIN_TIME_BW_UPDATES, 10.0f, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (this.locationManager != null) {
                        this.location = this.locationManager.getLastKnownLocation("gps");
                        if (this.location != null) {
                            this.latitude = this.location.getLatitude();
                            this.longitude = this.location.getLongitude();
                            System.out.println("-------Using GPS-------");
                        }
                    }
                }
                return this.location;
            }
            System.out.println("no network provider is enabled");
            return this.location;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopUsingGPS() {
        if (this.locationManager != null) {
            this.locationManager.removeUpdates(this);
        }
    }

    public double getLatitude() {
        if (this.location != null) {
            this.latitude = this.location.getLatitude();
        }
        return this.latitude;
    }

    public double getLongitude() {
        if (this.location != null) {
            this.longitude = this.location.getLongitude();
        }
        return this.longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        Builder alertDialog = new Builder(this.mContext);
        alertDialog.setTitle("定位設定");
        alertDialog.setMessage("wifi定位或GPS定位尚未開啟!! 請到定位設定頁開啟其中一項定位，再按返回鍵回首頁!!");
        alertDialog.setPositiveButton("確定", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MapGPSTracker.this.mContext.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        });
        alertDialog.show();
    }

    public void onLocationChanged(Location location) {
        try {
            this.locationManager = (LocationManager) this.mContext.getSystemService("location");
            this.isGPSEnabled = this.locationManager.isProviderEnabled("gps");
            this.isNetworkEnabled = this.locationManager.isProviderEnabled("network");
            if (this.isGPSEnabled || this.isNetworkEnabled) {
                this.canGetLocation = true;
                if (this.isNetworkEnabled) {
                    this.locationManager.requestLocationUpdates("network", MIN_TIME_BW_UPDATES, 10.0f, this);
                    if (this.locationManager != null) {
                        location = this.locationManager.getLastKnownLocation("network");
                        if (location != null) {
                            this.latitude = location.getLatitude();
                            this.longitude = location.getLongitude();
                            System.out.println("-------Using Network-------");
                            return;
                        }
                        return;
                    }
                    return;
                } else if (this.isGPSEnabled && !this.isNetworkEnabled) {
                    this.locationManager.requestLocationUpdates("gps", MIN_TIME_BW_UPDATES, 10.0f, this);
                    if (this.locationManager != null) {
                        location = this.locationManager.getLastKnownLocation("gps");
                        if (location != null) {
                            this.latitude = location.getLatitude();
                            this.longitude = location.getLongitude();
                            System.out.println("-------Using GPS-------");
                            return;
                        }
                        return;
                    }
                    return;
                } else {
                    return;
                }
            }
            System.out.println("no network provider is enabled");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}
