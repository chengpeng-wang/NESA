package com.duapps.ad;

import android.content.Context;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PullRequestController {
    private static PullRequestController c;
    private Map<Integer, IDuAdController> a = Collections.synchronizedMap(new HashMap());
    private Context b;

    private PullRequestController(Context context) {
        this.b = context;
    }

    public static PullRequestController getInstance(Context context) {
        synchronized (PullRequestController.class) {
            if (c == null) {
                c = new PullRequestController(context.getApplicationContext());
            }
        }
        return c;
    }

    public IDuAdController getPullController(int i, int i2) {
        IDuAdController iDuAdController;
        synchronized (this.a) {
            if (this.a.containsKey(Integer.valueOf(i))) {
                iDuAdController = (IDuAdController) this.a.get(Integer.valueOf(i));
            } else {
                iDuAdController = new a(this.b, i, i2);
                this.a.put(Integer.valueOf(i), iDuAdController);
            }
        }
        return iDuAdController;
    }

    public void clearCache() {
        synchronized (this.a) {
            for (Entry value : this.a.entrySet()) {
                ((IDuAdController) value.getValue()).destroy();
            }
            this.a.clear();
        }
    }

    public void remove(int i) {
        synchronized (this.a) {
            this.a.remove(Integer.valueOf(i));
        }
    }
}
