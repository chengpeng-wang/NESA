package com.example.chathook.utils;

import android.app.IThumbnailReceiver;
import android.util.Log;
import java.util.List;

public class MIActivityManager {
    public static List getTasks(Object instance, int maxNum, int flags) {
        List object = null;
        try {
            return (List) Class.forName("android.app.IActivityManager").getDeclaredMethod("getTasks", new Class[]{Integer.TYPE, Integer.TYPE, IThumbnailReceiver.class}).invoke(instance, new Object[]{Integer.valueOf(maxNum), Integer.valueOf(flags), null});
        } catch (Exception e) {
            Log.e("yincc", " IActivityManager exc = " + e.getMessage());
            e.printStackTrace();
            return object;
        }
    }
}
