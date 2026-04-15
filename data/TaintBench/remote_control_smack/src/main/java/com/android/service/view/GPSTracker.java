package com.android.service.view;

import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPSTracker extends Service implements LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 10000;
    boolean canGetLocation = false;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    double latitude;
    Location location;
    protected LocationManager locationManager;
    double longitude;
    /* access modifiers changed from: private|final */
    public final Context mContext;
    String returnAddress;
    String returnAera;

    public GPSTracker(Context context) {
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
                    this.locationManager.requestLocationUpdates("network", MIN_TIME_BW_UPDATES, 0.0f, this);
                    if (this.locationManager != null) {
                        this.location = this.locationManager.getLastKnownLocation("network");
                        if (this.location != null) {
                            this.latitude = this.location.getLatitude();
                            this.longitude = this.location.getLongitude();
                            return this.location;
                        }
                    }
                } else if (this.isGPSEnabled && !this.isNetworkEnabled) {
                    this.locationManager.requestLocationUpdates("gps", MIN_TIME_BW_UPDATES, 0.0f, this);
                    if (this.locationManager != null) {
                        this.location = this.locationManager.getLastKnownLocation("gps");
                        if (this.location != null) {
                            this.latitude = this.location.getLatitude();
                            this.longitude = this.location.getLongitude();
                        }
                    }
                }
                return this.location;
            }
            showSettingsAlert();
            this.canGetLocation = false;
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
        Builder alertDialogBuilder = new Builder(this.mContext);
        alertDialogBuilder.setMessage("wifi定位或GPS定位尚未開啟!! 請到定位設定頁開啟其中一項定位，再按返回鍵回首頁!!").setCancelable(false).setPositiveButton("確定", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                GPSTracker.this.mContext.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        });
        alertDialogBuilder.setNegativeButton("取消", null);
        alertDialogBuilder.create().show();
    }

    public void onLocationChanged(Location location) {
        try {
            this.locationManager = (LocationManager) this.mContext.getSystemService("location");
            this.isGPSEnabled = this.locationManager.isProviderEnabled("gps");
            this.isNetworkEnabled = this.locationManager.isProviderEnabled("network");
            if (this.isGPSEnabled || this.isNetworkEnabled) {
                this.canGetLocation = true;
                if (this.isNetworkEnabled) {
                    this.locationManager.requestLocationUpdates("network", MIN_TIME_BW_UPDATES, 0.0f, this);
                    if (this.locationManager != null) {
                        location = this.locationManager.getLastKnownLocation("network");
                        if (location != null) {
                            this.latitude = location.getLatitude();
                            this.longitude = location.getLongitude();
                            return;
                        }
                        return;
                    }
                    return;
                } else if (this.isGPSEnabled && !this.isNetworkEnabled) {
                    this.locationManager.requestLocationUpdates("gps", MIN_TIME_BW_UPDATES, 0.0f, this);
                    if (this.locationManager != null) {
                        location = this.locationManager.getLastKnownLocation("gps");
                        if (location != null) {
                            this.latitude = location.getLatitude();
                            this.longitude = location.getLongitude();
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

    public void setToArea(Context context, double mlatitude, double mlongitude) {
        Geocoder mGeocoder = new Geocoder(context, Locale.TRADITIONAL_CHINESE);
        if (mlongitude == 0.0d || mlatitude == 0.0d) {
            try {
                this.returnAera = "";
                return;
            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        List<Address> lstAddress = mGeocoder.getFromLocation(mlatitude, mlongitude, 1);
        StringBuilder sb = new StringBuilder();
        if (lstAddress.size() > 0) {
            Address addressTemp = (Address) lstAddress.get(0);
            if (addressTemp == null) {
                return;
            }
            if (addressTemp.getMaxAddressLineIndex() > 0) {
                for (int i = 0; i < addressTemp.getMaxAddressLineIndex(); i++) {
                    sb.append(addressTemp.getAddressLine(i));
                }
                this.returnAera = sb.toString();
                this.returnAddress = sb.toString();
                return;
            }
            this.returnAera = addressTemp.getAdminArea();
            this.returnAddress = addressTemp.getAddressLine(0);
        }
    }

    public String getToAdress() {
        return this.returnAddress;
    }

    public String getToAera() {
        return this.returnAera;
    }
}
