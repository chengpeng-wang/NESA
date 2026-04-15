package com.qc.access;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import com.qc.base.QCCache;
import com.qc.common.Constant;
import com.qc.common.ThreadPool;
import com.qc.entity.AdsInfo;
import com.qc.model.AdsTask;
import java.util.List;

public class BaseOsService extends Service {
    BroadcastReceiver broadcast = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.ADS_TASK_FINISHED)) {
                BaseOsService.this.service_flag = false;
                BaseOsService.this.stopSelf();
            }
        }
    };
    private Context context;
    /* access modifiers changed from: private */
    public boolean service_flag = false;

    public void onCreate() {
        this.context = this;
        this.service_flag = true;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ADS_TASK_FINISHED);
        registerReceiver(this.broadcast, filter);
        super.onCreate();
    }

    public void onStart(Intent intent, int startId) {
        if (Constant.ADSMAIN_ACTION.equals(intent.getAction())) {
            List<AdsInfo> adsList = (List) QCCache.getInstance().getValue("adsList");
            QCCache.getInstance().clearQueue();
            if (adsList != null && adsList.size() > 0) {
                int index = 0;
                for (AdsInfo adsInfo : adsList) {
                    index++;
                    QCCache.getInstance().offer(adsInfo);
                    ThreadPool pool = ThreadPool.getInstance();
                    pool.setDebug(false);
                    pool.start(new AdsTask(this.context, adsInfo, index, adsList.size()), 2);
                }
            }
        }
        super.onStart(intent, startId);
    }

    public void onDestroy() {
        try {
            unregisterReceiver(this.broadcast);
        } catch (Exception e) {
        }
        if (this.service_flag) {
            startService(new Intent(this, BaseOsService.class));
        }
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
