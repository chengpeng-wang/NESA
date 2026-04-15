package com.flurry.sdk;

import java.util.ArrayList;
import java.util.List;

final class kb extends lz {
    final /* synthetic */ jx a;

    kb(jx jxVar) {
        this.a = jxVar;
    }

    public final void safeRun() {
        jx jxVar = this.a;
        if (ht.a().a) {
            ArrayList<String> arrayList = new ArrayList(jxVar.d.b.keySet());
            if (arrayList.isEmpty()) {
                iw.a(4, jxVar.b, "No more reports to send.");
                return;
            }
            for (String str : arrayList) {
                if (!jxVar.c()) {
                    List<String> a = jxVar.d.a(str);
                    iw.a(4, jxVar.b, "Number of not sent blocks = " + a.size());
                    for (String str2 : a) {
                        if (!jxVar.c.contains(str2)) {
                            if (jxVar.c()) {
                                break;
                            }
                            kg kgVar = (kg) new ii(hz.a.b.getFileStreamPath(kg.a(str2)), ".yflurrydatasenderblock.", 1, new kd(jxVar)).a();
                            if (kgVar == null) {
                                iw.a(6, jxVar.b, "Internal ERROR! Cannot read!");
                                jxVar.d.a(str2, str);
                            } else {
                                byte[] bArr = kgVar.b;
                                if (bArr == null || bArr.length == 0) {
                                    iw.a(6, jxVar.b, "Internal ERROR! Report is empty!");
                                    jxVar.d.a(str2, str);
                                } else {
                                    iw.a(5, jxVar.b, "Reading block info " + str2);
                                    jxVar.c.add(str2);
                                    jxVar.a(bArr, str2, str);
                                }
                            }
                        }
                    }
                } else {
                    return;
                }
            }
            return;
        }
        iw.a(5, jxVar.b, "Reports were not sent! No Internet connection!");
    }
}
