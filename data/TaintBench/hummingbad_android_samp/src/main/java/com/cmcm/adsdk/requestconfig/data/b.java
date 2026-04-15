package com.cmcm.adsdk.requestconfig.data;

import android.text.TextUtils;
import com.cmcm.adsdk.requestconfig.data.PosBean.Colums;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: ConfigResponse */
public final class b {
    private final Map<String, a> a = new HashMap();
    private ConfigResponseHeader b = new ConfigResponseHeader();
    private List<PosBean> c = new ArrayList();

    /* compiled from: ConfigResponse */
    public static class a {
        public String a;
        public String b;
        public List<PosBean> c = new ArrayList();
    }

    public final Map<String, a> a() {
        return this.a;
    }

    public final List<PosBean> b() {
        return this.c;
    }

    public static b a(String str) {
        Exception e;
        b bVar = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            b bVar2 = new b();
            try {
                JSONArray jSONArray = new JSONObject(str).getJSONArray("poslist");
                if (jSONArray != null) {
                    for (int i = 0; i < jSONArray.length(); i++) {
                        a aVar = new a();
                        JSONObject jSONObject = jSONArray.getJSONObject(i);
                        aVar.a = jSONObject.getString(Colums.REQUEST_ADTYPE_COLUMN);
                        aVar.b = jSONObject.getString(Colums.REQUEST_PLACEID_COLUMN);
                        JSONArray jSONArray2 = jSONObject.getJSONArray("info");
                        if (jSONArray2 != null) {
                            for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                                PosBean posBean = (PosBean) new PosBean().fromJSONObject(jSONObject);
                                InfoBean infoBean = (InfoBean) new InfoBean().fromJSONObject(jSONArray2.getJSONObject(i2));
                                posBean.name = infoBean.a;
                                posBean.parameter = infoBean.b;
                                posBean.weight = Integer.valueOf(infoBean.c);
                                if (posBean.weight.intValue() > 0) {
                                    aVar.c.add(posBean);
                                }
                            }
                        }
                        Collections.sort(aVar.c);
                        bVar2.a.put(aVar.b, aVar);
                        bVar2.c.addAll(aVar.c);
                    }
                }
                return bVar2;
            } catch (Exception e2) {
                e = e2;
                bVar = bVar2;
                com.cmcm.adsdk.requestconfig.log.a.d("ConfigResponse", "ConfigResponse create error..." + e.getMessage());
                return bVar;
            }
        } catch (Exception e3) {
            e = e3;
            com.cmcm.adsdk.requestconfig.log.a.d("ConfigResponse", "ConfigResponse create error..." + e.getMessage());
            return bVar;
        }
    }

    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.b.toString());
        stringBuilder.append(":poslist{");
        for (PosBean posBean : this.c) {
            stringBuilder.append(posBean.toString());
            stringBuilder.append(",");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
