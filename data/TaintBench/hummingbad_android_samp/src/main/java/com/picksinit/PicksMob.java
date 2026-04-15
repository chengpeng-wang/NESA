package com.picksinit;

import android.os.Build.VERSION;
import android.text.TextUtils;
import com.cleanmaster.ui.app.market.Ad;
import com.cleanmaster.ui.app.market.c;
import com.cleanmaster.util.a;
import java.net.URLEncoder;
import java.util.HashMap;

public class PicksMob extends c {
    private a mAesUtils;
    private HashMap mParse302UrlMaps;

    public void preLoad302Ad(Ad ad, int posid) {
        if (ad != null && VERSION.SDK_INT >= 11 && ad.isPreloadUrl()) {
            com.cleanmaster.ui.app.utils.a.a().a(posid, ad.getPkgUrl(), ad.getPkg(), "", null);
        }
    }

    public synchronized void clearParse302Url(int posid) {
        if (this.mParse302UrlMaps != null && this.mParse302UrlMaps.containsKey(Integer.valueOf(posid))) {
            int intValue = ((Integer) this.mParse302UrlMaps.remove(Integer.valueOf(posid))).intValue();
            if (intValue > 0) {
                com.cleanmaster.ui.app.utils.a.a().a(intValue);
            }
        }
    }

    public void downloadSuccessReport(String posid, Ad ad, String rf) {
        c.c(posid, ad, rf);
    }

    public void doFaceBookViewReport(String placementId, String rawJson, String pkgName, String posId, int res) {
        String str = "";
        try {
            if (!TextUtils.isEmpty(rawJson)) {
                if (this.mAesUtils == null) {
                    this.mAesUtils = new a();
                }
                str = URLEncoder.encode(this.mAesUtils.a(rawJson), "utf-8");
            }
            c.a(placementId, str, pkgName, posId, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doFaceBookClickReport(String placementId, String rawJson, String pkgName, String posId, int res) {
        String str = "";
        try {
            if (!TextUtils.isEmpty(rawJson)) {
                if (this.mAesUtils == null) {
                    this.mAesUtils = new a();
                }
                str = URLEncoder.encode(this.mAesUtils.a(rawJson), "utf-8");
            }
            c.b(placementId, str, pkgName, posId, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public com.cleanmaster.down.a getExtraDownLoader() {
        return null;
    }

    public void reportDowned(String pkg) {
    }

    public void reportInstall(String pkg) {
    }

    public boolean ishaveInternalDown() {
        return false;
    }

    public void setDownLoader(com.cleanmaster.down.a downLoader) {
    }
}
