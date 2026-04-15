package com.google.android.gms.internal;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzr;
import java.io.BufferedOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public class zzdg implements zzdf {
    private final Context mContext;
    private final VersionInfoParcel zzpT;

    @zzhb
    static class zza {
        private final String mValue;
        private final String zzvs;

        public zza(String str, String str2) {
            this.zzvs = str;
            this.mValue = str2;
        }

        public String getKey() {
            return this.zzvs;
        }

        public String getValue() {
            return this.mValue;
        }
    }

    @zzhb
    static class zzb {
        private final String zzzp;
        private final URL zzzq;
        private final ArrayList<zza> zzzr;
        private final String zzzs;

        public zzb(String str, URL url, ArrayList<zza> arrayList, String str2) {
            this.zzzp = str;
            this.zzzq = url;
            if (arrayList == null) {
                this.zzzr = new ArrayList();
            } else {
                this.zzzr = arrayList;
            }
            this.zzzs = str2;
        }

        public String zzdU() {
            return this.zzzp;
        }

        public URL zzdV() {
            return this.zzzq;
        }

        public ArrayList<zza> zzdW() {
            return this.zzzr;
        }

        public String zzdX() {
            return this.zzzs;
        }
    }

    @zzhb
    class zzc {
        private final zzd zzzt;
        private final boolean zzzu;
        private final String zzzv;

        public zzc(boolean z, zzd zzd, String str) {
            this.zzzu = z;
            this.zzzt = zzd;
            this.zzzv = str;
        }

        public String getReason() {
            return this.zzzv;
        }

        public boolean isSuccess() {
            return this.zzzu;
        }

        public zzd zzdY() {
            return this.zzzt;
        }
    }

    @zzhb
    static class zzd {
        private final String zzxY;
        private final String zzzp;
        private final int zzzw;
        private final List<zza> zzzx;

        public zzd(String str, int i, List<zza> list, String str2) {
            this.zzzp = str;
            this.zzzw = i;
            if (list == null) {
                this.zzzx = new ArrayList();
            } else {
                this.zzzx = list;
            }
            this.zzxY = str2;
        }

        public String getBody() {
            return this.zzxY;
        }

        public int getResponseCode() {
            return this.zzzw;
        }

        public String zzdU() {
            return this.zzzp;
        }

        public Iterable<zza> zzdZ() {
            return this.zzzx;
        }
    }

    public zzdg(Context context, VersionInfoParcel versionInfoParcel) {
        this.mContext = context;
        this.zzpT = versionInfoParcel;
    }

    public JSONObject zzQ(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONObject jSONObject2 = new JSONObject();
            Object obj = "";
            try {
                obj = jSONObject.optString("http_request_id");
                zzc zza = zza(zzc(jSONObject));
                if (zza.isSuccess()) {
                    jSONObject2.put("response", zza(zza.zzdY()));
                    jSONObject2.put("success", true);
                    return jSONObject2;
                }
                jSONObject2.put("response", new JSONObject().put("http_request_id", obj));
                jSONObject2.put("success", false);
                jSONObject2.put("reason", zza.getReason());
                return jSONObject2;
            } catch (Exception e) {
                try {
                    jSONObject2.put("response", new JSONObject().put("http_request_id", obj));
                    jSONObject2.put("success", false);
                    jSONObject2.put("reason", e.toString());
                    return jSONObject2;
                } catch (JSONException e2) {
                    return jSONObject2;
                }
            }
        } catch (JSONException e3) {
            com.google.android.gms.ads.internal.util.client.zzb.e("The request is not a valid JSON.");
            try {
                return new JSONObject().put("success", false);
            } catch (JSONException e4) {
                return new JSONObject();
            }
        }
    }

    /* access modifiers changed from: protected */
    public zzc zza(zzb zzb) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) zzb.zzdV().openConnection();
            zzr.zzbC().zza(this.mContext, this.zzpT.afmaVersion, false, httpURLConnection);
            Iterator it = zzb.zzdW().iterator();
            while (it.hasNext()) {
                zza zza = (zza) it.next();
                httpURLConnection.addRequestProperty(zza.getKey(), zza.getValue());
            }
            if (!TextUtils.isEmpty(zzb.zzdX())) {
                httpURLConnection.setDoOutput(true);
                byte[] bytes = zzb.zzdX().getBytes();
                httpURLConnection.setFixedLengthStreamingMode(bytes.length);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
                bufferedOutputStream.write(bytes);
                bufferedOutputStream.close();
            }
            ArrayList arrayList = new ArrayList();
            if (httpURLConnection.getHeaderFields() != null) {
                for (Entry entry : httpURLConnection.getHeaderFields().entrySet()) {
                    for (String zza2 : (List) entry.getValue()) {
                        arrayList.add(new zza((String) entry.getKey(), zza2));
                    }
                }
            }
            return new zzc(true, new zzd(zzb.zzdU(), httpURLConnection.getResponseCode(), arrayList, zzr.zzbC().zza(new InputStreamReader(httpURLConnection.getInputStream()))), null);
        } catch (Exception e) {
            return new zzc(false, null, e.toString());
        }
    }

    /* access modifiers changed from: protected */
    public JSONObject zza(zzd zzd) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("http_request_id", zzd.zzdU());
            if (zzd.getBody() != null) {
                jSONObject.put("body", zzd.getBody());
            }
            JSONArray jSONArray = new JSONArray();
            for (zza zza : zzd.zzdZ()) {
                jSONArray.put(new JSONObject().put("key", zza.getKey()).put("value", zza.getValue()));
            }
            jSONObject.put("headers", jSONArray);
            jSONObject.put("response_code", zzd.getResponseCode());
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error constructing JSON for http response.", e);
        }
        return jSONObject;
    }

    public void zza(final zzjp zzjp, final Map<String, String> map) {
        zziq.zza(new Runnable() {
            public void run() {
                com.google.android.gms.ads.internal.util.client.zzb.zzaI("Received Http request.");
                final JSONObject zzQ = zzdg.this.zzQ((String) map.get("http_request"));
                if (zzQ == null) {
                    com.google.android.gms.ads.internal.util.client.zzb.e("Response should not be null.");
                } else {
                    zzir.zzMc.post(new Runnable() {
                        public void run() {
                            zzjp.zzb("fetchHttpRequestCompleted", zzQ);
                            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Dispatched http response.");
                        }
                    });
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public zzb zzc(JSONObject jSONObject) {
        URL url;
        String optString = jSONObject.optString("http_request_id");
        String optString2 = jSONObject.optString("url");
        String optString3 = jSONObject.optString("post_body", null);
        try {
            url = new URL(optString2);
        } catch (MalformedURLException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error constructing http request.", e);
            url = null;
        }
        ArrayList arrayList = new ArrayList();
        JSONArray optJSONArray = jSONObject.optJSONArray("headers");
        if (optJSONArray == null) {
            optJSONArray = new JSONArray();
        }
        for (int i = 0; i < optJSONArray.length(); i++) {
            JSONObject optJSONObject = optJSONArray.optJSONObject(i);
            if (optJSONObject != null) {
                arrayList.add(new zza(optJSONObject.optString("key"), optJSONObject.optString("value")));
            }
        }
        return new zzb(optString, url, arrayList, optString3);
    }
}
