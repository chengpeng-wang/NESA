package com.flurry.sdk;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

final class hr implements LocationListener {
    final /* synthetic */ hp a;

    public hr(hp hpVar) {
        this.a = hpVar;
    }

    public final void onLocationChanged(Location location) {
        if (location != null) {
            this.a.d = location;
        }
        if (hp.c(this.a) >= 3) {
            iw.a(4, hp.a, "Max location reports reached, stopping");
            hp.b(this.a);
        }
    }

    public final void onProviderDisabled(String str) {
    }

    public final void onProviderEnabled(String str) {
    }

    public final void onStatusChanged(String str, int i, Bundle bundle) {
    }
}
