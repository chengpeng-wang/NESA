package com.cleanmaster.func.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.picksinit.c;
import java.util.ArrayList;
import java.util.List;

/* compiled from: PackageManagerWrapper */
public class a {
    private static a a = new a();
    private Context b = c.getInstance().getContext();
    private PackageManager c = this.b.getPackageManager();
    private List d;

    public static synchronized a a() {
        a aVar;
        synchronized (a.class) {
            aVar = a;
        }
        return aVar;
    }

    public List b() {
        ArrayList arrayList;
        synchronized (this) {
            List<PackageInfo> a = a(0);
            arrayList = new ArrayList();
            if (a != null && a.size() > 0) {
                for (PackageInfo packageInfo : a) {
                    if (com.cleanmaster.common.a.a(packageInfo.applicationInfo)) {
                        arrayList.add(packageInfo);
                    }
                }
            }
        }
        return arrayList;
    }

    public List c() {
        List<PackageInfo> a = a(0);
        ArrayList arrayList = new ArrayList();
        if (a != null && a.size() > 0) {
            for (PackageInfo packageInfo : a) {
                if (com.cleanmaster.common.a.a(packageInfo.applicationInfo)) {
                    arrayList.add(packageInfo.packageName);
                }
            }
        }
        return arrayList;
    }

    private List a(int i) {
        try {
            if (this.d == null) {
                this.d = this.c.getInstalledPackages(i);
            }
        } catch (Exception e) {
        }
        return this.d;
    }

    public void a(String str) {
        if (this.d != null) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= this.d.size()) {
                    return;
                }
                if (((PackageInfo) this.d.get(i2)).packageName.equals(str)) {
                    this.d.remove(i2);
                    return;
                }
                i = i2 + 1;
            }
        }
    }

    public void a(String str, Context context) {
        int i = 0;
        try {
            if (this.d != null) {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 0);
                while (true) {
                    int i2 = i;
                    if (i2 >= this.d.size()) {
                        break;
                    } else if (((PackageInfo) this.d.get(i2)).packageName.equals(str)) {
                        this.d.remove(i2);
                        break;
                    } else {
                        i = i2 + 1;
                    }
                }
                this.d.add(packageInfo);
            }
        } catch (Exception e) {
        }
    }
}
