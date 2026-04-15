package com.picksinit;

import com.cleanmaster.util.k;
import java.io.File;

/* compiled from: PicksConfig */
class b implements Runnable {
    final /* synthetic */ PicksConfig a;

    b(PicksConfig picksConfig) {
        this.a = picksConfig;
    }

    public void run() {
        try {
            File file = new File(this.a.file_path);
            if (file.exists()) {
                com.cleanmaster.ui.app.market.b.d(new k(file).a("PicksConfig", "offerid").toString());
            }
        } catch (Exception e) {
        }
    }
}
