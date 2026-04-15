package com.googleprojects.mmsp;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
    public static final String GCM_PROJECT_ID = "1020885815711";
    public static GCMListener mListener = null;

    public GCMIntentService() {
        super(GCM_PROJECT_ID);
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void onError(Context arg0, String arg1) {
    }

    /* access modifiers changed from: protected */
    public void onMessage(Context arg0, Intent arg1) {
        String msg = arg1.getExtras().getString("message");
        if (mListener != null) {
            mListener.GCMListener_MessageReceived(msg);
        }
    }

    /* access modifiers changed from: protected */
    public void onRegistered(Context arg0, String arg1) {
        if (mListener != null) {
            mListener.GCMListener_Registered(arg1);
        }
    }

    /* access modifiers changed from: protected */
    public void onUnregistered(Context arg0, String arg1) {
    }
}
