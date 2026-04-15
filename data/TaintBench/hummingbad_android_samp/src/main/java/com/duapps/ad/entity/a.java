package com.duapps.ad.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.cmcm.adsdk.requestconfig.data.PosBean.Colums;
import com.mb.bdapp.db.DuAd;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.HashSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: AdData */
public final class a implements Parcelable {
    public static final Creator<a> v = new b();
    private static final HashSet<String> w;
    private long A;
    private int B;
    private float C;
    private int D;
    private String E;
    private long F;
    private long G;
    private String H;
    public long a;
    public String b;
    public String c;
    public String d;
    public String e;
    public String f;
    public String g;
    public float h;
    public int i;
    public int j;
    public String k;
    public String l;
    public String m;
    public String n;
    public int o;
    public String p;
    public int q;
    public String r;
    public String[] s;
    public String[] t;
    public int u;
    private int x;
    private boolean y;
    private boolean z;

    /* synthetic */ a(Parcel parcel, byte b) {
        this(parcel);
    }

    static {
        HashSet hashSet = new HashSet();
        w = hashSet;
        hashSet.add("sites");
        w.add("yeahmobi");
        w.add("matomy");
        w.add("kissmyads");
        w.add("applift");
        w.add("glispa");
        w.add("appflood");
        w.add("efun");
        w.add("motiveinteractive");
        w.add("apploop");
        w.add("performence");
        w.add("admobix");
    }

    protected a() {
        this.x = -1;
        this.j = -1;
        this.H = "download";
    }

    public a(String str, int i, String str2, String str3, JSONObject jSONObject, long j) {
        this(str, i, str2, str3, jSONObject);
        this.F = j;
    }

    private a(String str, int i, String str2, String str3, JSONObject jSONObject) {
        this.x = -1;
        this.j = -1;
        this.H = "download";
        this.n = str;
        this.o = i;
        this.p = str2;
        this.m = str3;
        this.a = jSONObject.optLong(MASTNativeAdConstants.ID_STRING);
        this.b = jSONObject.optString("title");
        this.k = jSONObject.optString(MASTNativeAdConstants.RESPONSE_MEDIATION_SOURCE);
        this.g = jSONObject.optString("adUrl");
        this.c = jSONObject.optString("pkg");
        this.e = jSONObject.optString("shortDesc");
        this.d = jSONObject.optString("description");
        this.j = jSONObject.optInt("openType", -1);
        this.i = jSONObject.optInt("integral");
        this.h = (float) jSONObject.optDouble("pts", 4.5d);
        this.C = (float) jSONObject.optDouble("contentRating", 0.0d);
        this.D = jSONObject.optInt("label", 0);
        this.E = jSONObject.optString("cate");
        this.q = jSONObject.optInt("preClick");
        this.u = jSONObject.optInt("pp", 0);
        this.f = b(jSONObject.optJSONArray("images"));
        this.l = b(jSONObject.optJSONArray("bigImages"));
        this.r = jSONObject.optString("buttonDes");
        this.G = jSONObject.optLong("cacheTime", 120);
        this.s = a(jSONObject.optJSONArray("impUrls"));
        this.t = a(jSONObject.optJSONArray("cUrls"));
    }

    private static String[] a(JSONArray jSONArray) {
        if (jSONArray != null) {
            int length = jSONArray.length();
            String[] strArr = new String[length];
            int i = 0;
            while (i < length) {
                try {
                    strArr[i] = jSONArray.getString(i);
                    i++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return strArr;
        }
        return null;
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        byte b = (byte) 1;
        parcel.writeLong(this.a);
        parcel.writeString(this.b);
        parcel.writeString(this.c);
        parcel.writeString(this.d);
        parcel.writeString(this.e);
        parcel.writeInt(this.x);
        parcel.writeString(this.f);
        parcel.writeString(this.g);
        parcel.writeByte(this.y ? (byte) 1 : (byte) 0);
        if (!this.z) {
            b = (byte) 0;
        }
        parcel.writeByte(b);
        parcel.writeFloat(this.h);
        parcel.writeInt(this.i);
        parcel.writeInt(this.j);
        parcel.writeString(this.k);
        parcel.writeLong(this.A);
        parcel.writeInt(this.B);
        parcel.writeFloat(this.C);
        parcel.writeInt(this.D);
        parcel.writeString(this.E);
        parcel.writeInt(this.q);
        parcel.writeInt(this.u);
        parcel.writeString(this.l);
        parcel.writeString(this.m);
        parcel.writeString(this.n);
        parcel.writeInt(this.o);
        parcel.writeString(this.p);
        parcel.writeString(this.r);
        parcel.writeLong(this.G);
        parcel.writeLong(this.F);
        parcel.writeStringArray(this.s);
        parcel.writeStringArray(this.t);
        parcel.writeInt(this.u);
    }

    private a(Parcel parcel) {
        boolean z = true;
        this.x = -1;
        this.j = -1;
        this.H = "download";
        this.a = parcel.readLong();
        this.b = parcel.readString();
        this.c = parcel.readString();
        this.d = parcel.readString();
        this.e = parcel.readString();
        this.x = parcel.readInt();
        this.f = parcel.readString();
        this.g = parcel.readString();
        this.y = parcel.readByte() != (byte) 0;
        if (parcel.readByte() == (byte) 0) {
            z = false;
        }
        this.z = z;
        this.h = parcel.readFloat();
        this.i = parcel.readInt();
        this.j = parcel.readInt();
        this.k = parcel.readString();
        this.A = parcel.readLong();
        this.B = parcel.readInt();
        this.C = parcel.readFloat();
        this.D = parcel.readInt();
        this.E = parcel.readString();
        this.q = parcel.readInt();
        this.u = parcel.readInt();
        this.l = parcel.readString();
        this.m = parcel.readString();
        this.n = parcel.readString();
        this.o = parcel.readInt();
        this.p = parcel.readString();
        this.r = parcel.readString();
        this.G = parcel.readLong();
        this.F = parcel.readLong();
        this.s = parcel.createStringArray();
        this.t = parcel.createStringArray();
        this.u = parcel.readInt();
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.c == null ? 0 : this.c.hashCode()) + 31) * 31;
        if (this.g != null) {
            i = this.g.hashCode();
        }
        return hashCode + i;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        a aVar = (a) obj;
        if (this.c == null) {
            if (aVar.c != null) {
                return false;
            }
        } else if (!this.c.equals(aVar.c)) {
            return false;
        }
        if (this.g == null) {
            if (aVar.g != null) {
                return false;
            }
            return true;
        } else if (this.g.equals(aVar.g)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean a(a aVar) {
        if (aVar.q > 0) {
            return true;
        }
        return false;
    }

    public final boolean a() {
        return System.currentTimeMillis() - this.F <= (this.G * 60) * 1000;
    }

    public static a a(JSONObject jSONObject) {
        a aVar = new a();
        aVar.H = jSONObject.optString(com.umeng.analytics.onlineconfig.a.c);
        aVar.a = jSONObject.optLong(MASTNativeAdConstants.ID_STRING);
        aVar.b = jSONObject.optString("name");
        aVar.c = jSONObject.optString("pkg");
        aVar.d = jSONObject.optString("desc");
        aVar.e = jSONObject.optString("sdesc");
        aVar.x = jSONObject.optInt("pos");
        aVar.j = jSONObject.optInt("opentype");
        aVar.k = jSONObject.optString("urlsource");
        aVar.f = jSONObject.optString(DuAd.DB_ICON);
        aVar.g = jSONObject.optString("playurl");
        aVar.h = (float) jSONObject.optDouble("pts");
        aVar.i = jSONObject.optInt("points");
        aVar.A = jSONObject.optLong("down");
        aVar.B = jSONObject.optInt(Colums.REQUEST_ADTYPE_COLUMN);
        aVar.C = (float) jSONObject.optDouble("rating");
        aVar.m = jSONObject.optString("logId");
        aVar.n = jSONObject.optString("license");
        aVar.o = jSONObject.optInt("sid");
        aVar.p = jSONObject.optString("sType", MASTNativeAdConstants.RESPONSE_NATIVE_STRING);
        aVar.D = jSONObject.optInt("label");
        aVar.q = jSONObject.optInt("preClick");
        aVar.u = jSONObject.optInt("pp", 0);
        aVar.E = jSONObject.optString("cate");
        aVar.s = a(jSONObject.optJSONArray("impUrls"));
        aVar.t = a(jSONObject.optJSONArray("cUrls"));
        aVar.u = jSONObject.optInt("pp", 0);
        return aVar;
    }

    public static JSONObject b(a aVar) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(com.umeng.analytics.onlineconfig.a.c, aVar.H);
        jSONObject.put(MASTNativeAdConstants.ID_STRING, aVar.a);
        jSONObject.put("name", aVar.b);
        jSONObject.put("pkg", aVar.c);
        jSONObject.put("desc", aVar.d);
        jSONObject.put("sdesc", aVar.e);
        jSONObject.put("pos", aVar.x);
        jSONObject.put("opentype", aVar.j);
        jSONObject.put("urlsource", aVar.k);
        jSONObject.put(DuAd.DB_ICON, aVar.f);
        jSONObject.put("playurl", aVar.g);
        jSONObject.put("pts", (double) aVar.h);
        jSONObject.put("points", aVar.i);
        jSONObject.put("down", aVar.A);
        jSONObject.put(Colums.REQUEST_ADTYPE_COLUMN, aVar.B);
        jSONObject.put("rating", (double) aVar.C);
        jSONObject.put("logId", aVar.m);
        jSONObject.put("license", aVar.n);
        jSONObject.put("sid", aVar.o);
        jSONObject.put("sType", aVar.p);
        jSONObject.put("label", aVar.D);
        jSONObject.put("preClick", aVar.q);
        jSONObject.put("pp", aVar.u);
        jSONObject.put("cate", aVar.E);
        jSONObject.put("impUrls", aVar.s);
        jSONObject.put("cUrls", aVar.t);
        jSONObject.put("pp", aVar.u);
        return jSONObject;
    }

    private static String b(JSONArray jSONArray) {
        if (jSONArray == null) {
            return "";
        }
        int length = jSONArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject optJSONObject = jSONArray.optJSONObject(i);
            if (optJSONObject != null) {
                return optJSONObject.optString(MASTNativeAdConstants.RESPONSE_URL, "");
            }
        }
        return "";
    }
}
