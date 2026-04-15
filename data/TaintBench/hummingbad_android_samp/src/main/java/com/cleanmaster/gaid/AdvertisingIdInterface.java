package com.cleanmaster.gaid;

import android.os.IInterface;

public interface AdvertisingIdInterface extends IInterface {
    String getId();

    boolean isLimitAdTrackingEnabled(boolean z);
}
