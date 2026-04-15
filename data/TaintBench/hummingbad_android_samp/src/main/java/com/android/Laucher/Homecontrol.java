package com.android.Laucher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class Homecontrol {
    private Context mContext;
    private IntentFilter mFilter = new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS");
    /* access modifiers changed from: private */
    public OnHomePressedListener mListener;
    private InnerRecevier mRecevier = new InnerRecevier();

    class InnerRecevier extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        InnerRecevier() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.CLOSE_SYSTEM_DIALOGS")) {
                String reason = intent.getStringExtra("reason");
                if (reason != null && Homecontrol.this.mListener != null) {
                    if (reason.equals("homekey")) {
                        Homecontrol.this.mListener.onHomePressed();
                    } else if (reason.equals("recentapps")) {
                        Homecontrol.this.mListener.onHomeLongPressed();
                    }
                }
            }
        }
    }

    public interface OnHomePressedListener {
        void onHomeLongPressed();

        void onHomePressed();
    }

    public Homecontrol(Context context) {
        this.mContext = context;
    }

    public void setOnHomePressedListener(OnHomePressedListener listener) {
        this.mListener = listener;
    }

    public void startWatch() {
        if (this.mRecevier != null) {
            this.mContext.registerReceiver(this.mRecevier, this.mFilter);
        }
    }

    public void stopWatch() {
        if (this.mRecevier != null) {
            this.mContext.unregisterReceiver(this.mRecevier);
        }
    }
}
