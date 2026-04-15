package com.flurry.sdk;

import android.text.TextUtils;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.mobileads.VastIconXmlManager;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

final class go {
    private static dx a(String str) {
        dx dxVar = dx.a;
        try {
            if (!TextUtils.isEmpty(str)) {
                return (dx) Enum.valueOf(dx.class, str);
            }
        } catch (Exception e) {
        }
        return dxVar;
    }

    static List a(JSONObject jSONObject) {
        List<JSONObject> a = lv.a(jSONObject.getJSONArray("adUnits"));
        ArrayList arrayList = new ArrayList();
        for (JSONObject jSONObject2 : a) {
            dv dvVar = new dv();
            dvVar.a = a(jSONObject2.getString("adViewType"));
            dvVar.b = jSONObject2.getString("adSpace");
            dvVar.c = jSONObject2.getLong("expiration");
            dvVar.g = jSONObject2.getString("groupId");
            dvVar.d = c(jSONObject2);
            dvVar.e = e(jSONObject2);
            dvVar.f = jSONObject2.getInt("combinable");
            dvVar.h = jSONObject2.getLong("price");
            dvVar.i = jSONObject2.getString("adomain");
            dvVar.j = jSONObject2.getLong("closableTimeMillis15SecOrLess");
            dvVar.k = jSONObject2.getLong("closableTimeMillisLongerThan15Sec");
            dvVar.l = jSONObject2.getLong("viewabilityDurationMillis");
            dvVar.m = jSONObject2.getInt("viewabilityPercentVisible");
            dvVar.n = jSONObject2.getBoolean("rewardable");
            dvVar.o = jSONObject2.getLong("preRenderTimeoutMillis");
            dvVar.p = jSONObject2.getInt("preCacheAdSkippableTimeLimitMillis");
            dvVar.q = jSONObject2.getBoolean("videoAutoPlay");
            dvVar.r = jSONObject2.getBoolean("supportMRAID");
            dvVar.s = jSONObject2.getBoolean("preRender");
            dvVar.t = jSONObject2.getBoolean("renderTime");
            dvVar.u = lv.a(jSONObject2.getJSONObject("clientSideRtbPayload"));
            dvVar.v = c(jSONObject2.getString("screenOrientation"));
            dvVar.w = d(jSONObject2);
            dvVar.x = jSONObject2.getInt("videoPctCompletionForMoreInfo");
            dvVar.y = jSONObject2.getInt("videoPctCompletionForReward");
            dvVar.z = jSONObject2.getInt("videoTimeMillisForViewBeacon");
            arrayList.add(dvVar);
        }
        return arrayList;
    }

    private static ed b(String str) {
        ed edVar = ed.ADSPACE;
        try {
            if (!TextUtils.isEmpty(str)) {
                return (ed) Enum.valueOf(ed.class, str);
            }
        } catch (Exception e) {
        }
        return edVar;
    }

    static List b(JSONObject jSONObject) {
        List<JSONObject> a = lv.a(jSONObject.getJSONArray("frequencyCapResponseInfoList"));
        ArrayList arrayList = new ArrayList();
        for (JSONObject jSONObject2 : a) {
            ec ecVar = new ec();
            ecVar.a = b(jSONObject2.getString("capType"));
            ecVar.b = jSONObject2.getString(MASTNativeAdConstants.ID_STRING);
            ecVar.c = jSONObject2.getLong("serveTime");
            ecVar.d = jSONObject2.getLong("expirationTime");
            ecVar.e = jSONObject2.getLong("streamCapDurationMillis");
            ecVar.f = jSONObject2.getInt("capRemaining");
            ecVar.g = jSONObject2.getInt("totalCap");
            ecVar.h = jSONObject2.getInt("capDurationType");
            arrayList.add(ecVar);
        }
        return arrayList;
    }

    private static ej c(String str) {
        ej ejVar = ej.PORTRAIT;
        try {
            if (!TextUtils.isEmpty(str)) {
                return (ej) Enum.valueOf(ej.class, str);
            }
        } catch (Exception e) {
        }
        return ejVar;
    }

    private static List c(JSONObject jSONObject) {
        List<JSONObject> a = lv.a(jSONObject.getJSONArray("adFrames"));
        ArrayList arrayList = new ArrayList();
        for (JSONObject jSONObject2 : a) {
            du duVar;
            dq dqVar = new dq();
            dqVar.a = jSONObject2.getInt("binding");
            dqVar.b = jSONObject2.getString("display");
            dqVar.c = jSONObject2.getString("content");
            JSONObject jSONObject3 = jSONObject2.getJSONObject("adSpaceLayout");
            if (jSONObject3 != null) {
                duVar = new du();
                duVar.a = jSONObject3.getInt(MillennialBanner.AD_WIDTH_KEY);
                duVar.b = jSONObject3.getInt(MillennialBanner.AD_HEIGHT_KEY);
                duVar.c = jSONObject3.getString("fix");
                duVar.d = jSONObject3.getString("format");
                duVar.e = jSONObject3.getString("alignment");
            } else {
                duVar = null;
            }
            dqVar.d = duVar;
            dqVar.e = f(jSONObject2);
            dqVar.f = jSONObject2.getString("adGuid");
            dqVar.g = jSONObject2.getInt("cachingEnum");
            dqVar.h = jSONObject2.getLong("assetExpirationTimestampUTCMillis");
            dqVar.i = lv.b(jSONObject2.getJSONArray("cacheWhitelistedAssets"));
            dqVar.j = lv.b(jSONObject2.getJSONArray("cacheBlacklistedAssets"));
            arrayList.add(dqVar);
        }
        return arrayList;
    }

    private static eg d(JSONObject jSONObject) {
        JSONObject jSONObject2 = jSONObject.getJSONObject("nativeAdInfo");
        eg egVar = new eg();
        if (jSONObject2 != null) {
            egVar.a = jSONObject2.getInt("style");
            egVar.c = jSONObject2.getString("template");
            List<JSONObject> a = lv.a(jSONObject2.getJSONArray(MASTNativeAdConstants.NATIVE_ASSETS_STRING));
            ArrayList arrayList = new ArrayList();
            for (JSONObject jSONObject22 : a) {
                eh ehVar = new eh();
                ehVar.a = jSONObject22.getString("name");
                ehVar.c = jSONObject22.getString(MASTNativeAdConstants.RESPONSE_VALUE);
                ehVar.d = jSONObject22.getInt(VastIconXmlManager.WIDTH);
                ehVar.e = jSONObject22.getInt(VastIconXmlManager.HEIGHT);
                ehVar.b = d(jSONObject22.getString("type"));
                ehVar.f = lv.a(jSONObject22.getJSONObject("params"));
                arrayList.add(ehVar);
            }
            egVar.b = arrayList;
        }
        return egVar;
    }

    private static ei d(String str) {
        ei eiVar = ei.IMAGE;
        try {
            if (!TextUtils.isEmpty(str)) {
                return (ei) Enum.valueOf(ei.class, str);
            }
        } catch (Exception e) {
        }
        return eiVar;
    }

    private static List e(JSONObject jSONObject) {
        List<JSONObject> a = lv.a(jSONObject.getJSONArray("frequencyCapResponseInfoList"));
        ArrayList arrayList = new ArrayList();
        for (JSONObject jSONObject2 : a) {
            if (jSONObject2 != null) {
                ec ecVar = new ec();
                ecVar.a = b(jSONObject2.getString("capType"));
                ecVar.b = jSONObject2.getString(MASTNativeAdConstants.ID_STRING);
                ecVar.c = jSONObject2.getLong("serveTime");
                ecVar.d = jSONObject2.getLong("expirationTime");
                ecVar.e = jSONObject2.getLong("streamCapDurationMillis");
                ecVar.f = jSONObject2.getInt("capRemaining");
                ecVar.g = jSONObject2.getInt("totalCap");
                ecVar.h = jSONObject2.getInt("capDurationType");
                arrayList.add(ecVar);
            }
        }
        return arrayList;
    }

    private static List f(JSONObject jSONObject) {
        List<JSONObject> a = lv.a(jSONObject.getJSONArray("callbacks"));
        ArrayList arrayList = new ArrayList();
        for (JSONObject jSONObject2 : a) {
            dy dyVar = new dy();
            dyVar.a = jSONObject2.getString("event");
            dyVar.b = lv.b(jSONObject2.getJSONArray("actions"));
            arrayList.add(dyVar);
        }
        return arrayList;
    }
}
