package com.mopub.common;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import com.mopub.common.logging.MoPubLog;
import java.math.BigDecimal;

public class LocationService {

    public enum LocationAwareness {
        NORMAL,
        TRUNCATED,
        DISABLED;

        @Deprecated
        public final com.mopub.common.MoPub.LocationAwareness getNewLocationAwareness() {
            if (this == TRUNCATED) {
                return com.mopub.common.MoPub.LocationAwareness.TRUNCATED;
            }
            if (this == DISABLED) {
                return com.mopub.common.MoPub.LocationAwareness.DISABLED;
            }
            return com.mopub.common.MoPub.LocationAwareness.NORMAL;
        }

        @Deprecated
        public static LocationAwareness fromMoPubLocationAwareness(com.mopub.common.MoPub.LocationAwareness awareness) {
            if (awareness == com.mopub.common.MoPub.LocationAwareness.DISABLED) {
                return DISABLED;
            }
            if (awareness == com.mopub.common.MoPub.LocationAwareness.TRUNCATED) {
                return TRUNCATED;
            }
            return NORMAL;
        }
    }

    @Nullable
    public static Location getLastKnownLocation(Context context, int locationPrecision, com.mopub.common.MoPub.LocationAwareness locationLocationAwareness) {
        if (locationLocationAwareness == com.mopub.common.MoPub.LocationAwareness.DISABLED) {
            return null;
        }
        Location lastKnownLocation;
        Location lastKnownLocation2;
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        try {
            lastKnownLocation = locationManager.getLastKnownLocation("gps");
        } catch (SecurityException e) {
            MoPubLog.d("Failed to retrieve GPS location: access appears to be disabled.");
            lastKnownLocation = null;
        } catch (IllegalArgumentException e2) {
            MoPubLog.d("Failed to retrieve GPS location: device has no GPS provider.");
            lastKnownLocation = null;
        } catch (NullPointerException e3) {
            MoPubLog.d("Failed to retrieve GPS location: device has no GPS provider.");
            lastKnownLocation = null;
        }
        try {
            lastKnownLocation2 = locationManager.getLastKnownLocation("network");
        } catch (SecurityException e4) {
            MoPubLog.d("Failed to retrieve network location: access appears to be disabled.");
            lastKnownLocation2 = null;
        } catch (IllegalArgumentException e5) {
            MoPubLog.d("Failed to retrieve network location: device has no network provider.");
            lastKnownLocation2 = null;
        } catch (NullPointerException e6) {
            MoPubLog.d("Failed to retrieve GPS location: device has no network provider.");
            lastKnownLocation2 = null;
        }
        if (lastKnownLocation == null && lastKnownLocation2 == null) {
            return null;
        }
        if (lastKnownLocation == null || lastKnownLocation2 == null) {
            if (lastKnownLocation == null) {
                lastKnownLocation = lastKnownLocation2;
            }
        } else if (lastKnownLocation.getTime() <= lastKnownLocation2.getTime()) {
            lastKnownLocation = lastKnownLocation2;
        }
        if (locationLocationAwareness == com.mopub.common.MoPub.LocationAwareness.TRUNCATED) {
            lastKnownLocation.setLatitude(BigDecimal.valueOf(lastKnownLocation.getLatitude()).setScale(locationPrecision, 5).doubleValue());
            lastKnownLocation.setLongitude(BigDecimal.valueOf(lastKnownLocation.getLongitude()).setScale(locationPrecision, 5).doubleValue());
        }
        return lastKnownLocation;
    }
}
