package com.address.core;

import android.telephony.PhoneStateListener;

public class PhoneCallListener extends PhoneStateListener {
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
    }
}
