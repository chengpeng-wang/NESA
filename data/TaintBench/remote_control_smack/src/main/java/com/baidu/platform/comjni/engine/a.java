package com.baidu.platform.comjni.engine;

import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.List;

public class a {
    private static SparseArray<List<Handler>> a = new SparseArray();

    public static void a() {
        int size = a.size();
        for (int i = 0; i < size; i++) {
            List list = (List) a.get(a.keyAt(i));
            if (list != null) {
                list.clear();
            }
        }
        a.clear();
        a = null;
    }

    public static void a(int i, int i2, int i3) {
        List<Handler> list = (List) a.get(i);
        if (list != null && !list.isEmpty()) {
            for (Handler obtain : list) {
                Message.obtain(obtain, i, i2, i3, null).sendToTarget();
            }
        }
    }

    public static void a(int i, Handler handler) {
        if (a == null) {
            a = new SparseArray();
        }
        List list = (List) a.get(i);
        if (list == null) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(handler);
            a.put(i, arrayList);
        } else if (!list.contains(handler)) {
            list.add(handler);
        }
    }

    public static void b(int i, Handler handler) {
        handler.removeCallbacksAndMessages(null);
        List list = (List) a.get(i);
        if (list != null) {
            list.remove(handler);
        }
    }
}
