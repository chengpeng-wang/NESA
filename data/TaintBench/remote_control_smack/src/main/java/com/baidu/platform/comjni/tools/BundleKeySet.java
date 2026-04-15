package com.baidu.platform.comjni.tools;

import android.os.Bundle;
import java.util.Iterator;

public class BundleKeySet {
    public String[] getBundleKeys(Bundle bundle) {
        if (bundle == null || bundle.isEmpty()) {
            return null;
        }
        String[] strArr = new String[bundle.size()];
        int i = 0;
        Iterator it = bundle.keySet().iterator();
        while (true) {
            int i2 = i;
            if (!it.hasNext()) {
                return strArr;
            }
            strArr[i2] = ((String) it.next()).toString();
            i = i2 + 1;
        }
    }
}
