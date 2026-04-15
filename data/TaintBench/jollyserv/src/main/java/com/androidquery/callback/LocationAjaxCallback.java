package com.androidquery.callback;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import com.androidquery.util.AQUtility;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class LocationAjaxCallback extends AbstractAjaxCallback<Location, LocationAjaxCallback> {
    private float accuracy = 1000.0f;
    private boolean gpsEnabled = false;
    private Listener gpsListener;
    private long initTime;
    private long interval = 1000;
    private int iteration = 3;
    /* access modifiers changed from: private */
    public LocationManager lm;
    private int n = 0;
    private boolean networkEnabled = false;
    private Listener networkListener;
    private long timeout = 30000;
    private float tolerance = 10.0f;

    private class Listener extends TimerTask implements LocationListener {
        private Listener() {
        }

        /* synthetic */ Listener(LocationAjaxCallback locationAjaxCallback, Listener listener) {
            this();
        }

        public void onLocationChanged(Location location) {
            AQUtility.debug("changed", location);
            LocationAjaxCallback.this.check(location);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            AQUtility.debug((Object) "onStatusChanged");
        }

        public void onProviderEnabled(String provider) {
            AQUtility.debug((Object) "onProviderEnabled");
            LocationAjaxCallback.this.check(LocationAjaxCallback.this.getBestLocation());
            LocationAjaxCallback.this.lm.removeUpdates(this);
        }

        public void onProviderDisabled(String provider) {
            AQUtility.debug((Object) "onProviderDisabled");
        }

        public void run() {
            LocationAjaxCallback.this.failure();
        }
    }

    public LocationAjaxCallback() {
        ((LocationAjaxCallback) type(Location.class)).url("device");
    }

    public void async(Context context) {
        this.lm = (LocationManager) context.getSystemService("location");
        this.gpsEnabled = this.lm.isProviderEnabled("gps");
        this.networkEnabled = this.lm.isProviderEnabled("network");
        work();
    }

    public LocationAjaxCallback timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public LocationAjaxCallback accuracy(float accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    public LocationAjaxCallback tolerance(float tolerance) {
        this.tolerance = tolerance;
        return this;
    }

    public LocationAjaxCallback iteration(int iteration) {
        this.iteration = iteration;
        return this;
    }

    /* access modifiers changed from: private */
    public void check(Location loc) {
        if (loc != null && isBetter(loc)) {
            boolean best;
            this.n++;
            boolean last = this.n >= this.iteration;
            boolean accurate = isAccurate(loc);
            boolean diff = isDiff(loc);
            if (!this.gpsEnabled || "gps".equals(loc.getProvider())) {
                best = true;
            } else {
                best = false;
            }
            AQUtility.debug(Integer.valueOf(this.n), Integer.valueOf(this.iteration));
            AQUtility.debug("acc", Boolean.valueOf(accurate));
            AQUtility.debug("best", Boolean.valueOf(best));
            if (!diff) {
                return;
            }
            if (!last) {
                if (accurate && best) {
                    stop();
                }
                callback(loc);
            } else if (accurate && best) {
                stop();
                callback(loc);
            }
        }
    }

    private void callback(Location loc) {
        this.result = loc;
        status(loc, 200);
        callback();
    }

    private void status(Location loc, int code) {
        if (this.status == null) {
            this.status = new AjaxStatus();
        }
        if (loc != null) {
            this.status.time(new Date(loc.getTime()));
        }
        this.status.code(code).done().source(5);
    }

    private boolean isAccurate(Location loc) {
        return loc.getAccuracy() < this.accuracy;
    }

    private boolean isDiff(Location loc) {
        if (this.result == null) {
            return true;
        }
        if (distFrom(((Location) this.result).getLatitude(), ((Location) this.result).getLongitude(), loc.getLatitude(), loc.getLongitude()) >= this.tolerance) {
            return true;
        }
        AQUtility.debug((Object) "duplicate location");
        return false;
    }

    private boolean isBetter(Location loc) {
        if (this.result == null) {
            return true;
        }
        if (((Location) this.result).getTime() <= this.initTime || !((Location) this.result).getProvider().equals("gps") || !loc.getProvider().equals("network")) {
            return true;
        }
        AQUtility.debug((Object) "inferior location");
        return false;
    }

    /* access modifiers changed from: private */
    public void failure() {
        if (this.gpsListener != null || this.networkListener != null) {
            AQUtility.debug((Object) "fail");
            this.result = null;
            status(null, AjaxStatus.TRANSFORM_ERROR);
            stop();
            callback();
        }
    }

    public void stop() {
        AQUtility.debug((Object) "stop");
        Listener gListener = this.gpsListener;
        if (gListener != null) {
            this.lm.removeUpdates(gListener);
            gListener.cancel();
        }
        Listener nListener = this.networkListener;
        if (nListener != null) {
            this.lm.removeUpdates(nListener);
            nListener.cancel();
        }
        this.gpsListener = null;
        this.networkListener = null;
    }

    private void work() {
        Location loc = getBestLocation();
        Timer timer = new Timer(false);
        if (this.networkEnabled) {
            AQUtility.debug((Object) "register net");
            this.networkListener = new Listener(this, null);
            this.lm.requestLocationUpdates("network", this.interval, 0.0f, this.networkListener, Looper.getMainLooper());
            timer.schedule(this.networkListener, this.timeout);
        }
        if (this.gpsEnabled) {
            AQUtility.debug((Object) "register gps");
            this.gpsListener = new Listener(this, null);
            this.lm.requestLocationUpdates("gps", this.interval, 0.0f, this.gpsListener, Looper.getMainLooper());
            timer.schedule(this.gpsListener, this.timeout);
        }
        if (this.iteration > 1 && loc != null) {
            this.n++;
            callback(loc);
        }
        this.initTime = System.currentTimeMillis();
    }

    /* access modifiers changed from: private */
    public Location getBestLocation() {
        Location loc1 = this.lm.getLastKnownLocation("gps");
        Location loc2 = this.lm.getLastKnownLocation("network");
        if (loc2 == null) {
            return loc1;
        }
        if (loc1 == null) {
            return loc2;
        }
        if (loc1.getTime() <= loc2.getTime()) {
            return loc2;
        }
        return loc1;
    }

    private static float distFrom(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = (Math.sin(dLat / 2.0d) * Math.sin(dLat / 2.0d)) + (((Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))) * Math.sin(dLng / 2.0d)) * Math.sin(dLng / 2.0d));
        return ((float) (3958.75d * (2.0d * Math.atan2(Math.sqrt(a), Math.sqrt(1.0d - a))))) * ((float) 1609);
    }
}
