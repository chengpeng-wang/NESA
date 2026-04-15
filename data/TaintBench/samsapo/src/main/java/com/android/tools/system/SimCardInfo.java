package com.android.tools.system;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SimCardInfo {
    private Context context;

    public SimCardInfo(Context context) {
        this.context = context;
    }

    public String getProvider() {
        return ((TelephonyManager) this.context.getSystemService("phone")).getNetworkOperatorName();
    }
}
