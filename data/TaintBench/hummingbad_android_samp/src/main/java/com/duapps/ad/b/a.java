package com.duapps.ad.b;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Base64;
import com.duapps.ad.base.f;
import com.mb.bdapp.db.DuAd;
import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: IMData */
public final class a implements Parcelable {
    public static final Creator<a> u = new b();
    public String a;
    public int b;
    public String c;
    public String d;
    public long e;
    public int f;
    public int g;
    public String h;
    public String i;
    public String j;
    public float k;
    public String l;
    public String m;
    public String n;
    public String o;
    public String p;
    public String q;
    public long r;
    public boolean s;
    public boolean t;
    private int v;
    private int w;
    private int x;
    private String y;
    private String z;

    /* compiled from: IMData */
    public enum a {
        None,
        Impression,
        Click
    }

    /* synthetic */ a(Parcel parcel, byte b) {
        this(parcel);
    }

    public a() {
        this.s = false;
        this.t = false;
        this.z = "inmobi";
    }

    public a(String str, int i, String str2, String str3, int i2, JSONObject jSONObject, long j) {
        this.s = false;
        this.t = false;
        this.z = "inmobi";
        this.a = str;
        this.b = i;
        this.d = str2;
        this.c = str3;
        this.e = jSONObject.optLong(MASTNativeAdConstants.ID_STRING);
        this.y = jSONObject.optString(MASTNativeAdConstants.RESPONSE_MEDIATION_SOURCE);
        this.f = jSONObject.optInt("openType", -1);
        this.v = jSONObject.optInt("mType");
        this.w = jSONObject.optInt("label");
        this.g = jSONObject.optInt("preClick");
        this.x = jSONObject.optInt("cacheTime");
        this.r = j;
        JSONObject optJSONObject = jSONObject.optJSONArray(MASTNativeAdConstants.RESPONSE_ADS).optJSONObject(i2);
        if (optJSONObject != null) {
            String optString = optJSONObject.optString("pubContent");
            this.p = optJSONObject.optString("contextCode");
            this.q = optJSONObject.optString("namespace");
            String a = a(optString);
            if (a != null) {
                try {
                    JSONObject jSONObject2 = new JSONObject(a);
                    f.c("IMData", "imAd==" + jSONObject2.toString());
                    this.i = jSONObject2.optString("title");
                    this.j = jSONObject2.optString("description");
                    this.k = (float) jSONObject2.optDouble("rating", 4.5d);
                    this.o = jSONObject2.optString("cta");
                    this.l = jSONObject2.optJSONObject(DuAd.DB_ICON).optString(MASTNativeAdConstants.RESPONSE_URL);
                    this.m = jSONObject2.optJSONObject("screenshots").optString(MASTNativeAdConstants.RESPONSE_URL);
                    this.n = jSONObject2.optString("landingURL");
                    this.h = jSONObject2.optString("package_name");
                } catch (JSONException e) {
                    f.d("IMData", "JSONException:" + e.toString());
                }
            }
        }
    }

    public a(String str, String str2, long j) {
        this.s = false;
        this.t = false;
        this.z = "inmobi";
        this.q = str;
        this.p = str2;
        this.r = j;
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        byte b;
        byte b2 = (byte) 1;
        parcel.writeString(this.a);
        parcel.writeString(this.c);
        parcel.writeInt(this.b);
        parcel.writeString(this.d);
        parcel.writeLong(this.e);
        parcel.writeString(this.y);
        parcel.writeInt(this.f);
        parcel.writeInt(this.v);
        parcel.writeInt(this.w);
        parcel.writeInt(this.g);
        parcel.writeInt(this.x);
        parcel.writeString(this.i);
        parcel.writeString(this.j);
        parcel.writeString(this.o);
        parcel.writeString(this.l);
        parcel.writeString(this.m);
        parcel.writeString(this.n);
        parcel.writeString(this.p);
        parcel.writeString(this.q);
        parcel.writeFloat(this.k);
        parcel.writeLong(this.r);
        if (this.s) {
            b = (byte) 1;
        } else {
            b = (byte) 0;
        }
        parcel.writeByte(b);
        if (!this.t) {
            b2 = (byte) 0;
        }
        parcel.writeByte(b2);
        parcel.writeString(this.h);
    }

    private a(Parcel parcel) {
        boolean z;
        boolean z2 = true;
        this.s = false;
        this.t = false;
        this.z = "inmobi";
        this.a = parcel.readString();
        this.c = parcel.readString();
        this.b = parcel.readInt();
        this.d = parcel.readString();
        this.e = parcel.readLong();
        this.y = parcel.readString();
        this.f = parcel.readInt();
        this.v = parcel.readInt();
        this.w = parcel.readInt();
        this.g = parcel.readInt();
        this.x = parcel.readInt();
        this.i = parcel.readString();
        this.j = parcel.readString();
        this.o = parcel.readString();
        this.l = parcel.readString();
        this.m = parcel.readString();
        this.n = parcel.readString();
        this.p = parcel.readString();
        this.q = parcel.readString();
        this.k = parcel.readFloat();
        this.r = parcel.readLong();
        if (parcel.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.s = z;
        if (parcel.readByte() == (byte) 0) {
            z2 = false;
        }
        this.t = z2;
        this.h = parcel.readString();
    }

    public final boolean a() {
        return System.currentTimeMillis() - this.r <= ((long) ((this.x * 60) * 1000));
    }

    private static String a(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            byte[] bytes = str.getBytes(Defaults.ENCODING_UTF_8);
            return new String(Base64.decode(bytes, 0, bytes.length, 0), Defaults.ENCODING_UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public final String a(a aVar) {
        StringBuffer stringBuffer = new StringBuffer();
        int i = aVar == a.Impression ? 18 : 8;
        stringBuffer.append(this.p);
        stringBuffer.append("<script>");
        stringBuffer.append(this.q);
        stringBuffer.append("recordEvent(" + i + ")");
        stringBuffer.append("</script>");
        return stringBuffer.toString();
    }

    public final String b() {
        return a(a.Impression) + "<script>recordEvent(8)</script>";
    }

    public static a a(JSONObject jSONObject) {
        a aVar = new a();
        aVar.z = jSONObject.optString(com.umeng.analytics.onlineconfig.a.c);
        aVar.a = jSONObject.optString("license");
        aVar.c = jSONObject.optString("logId");
        aVar.b = jSONObject.optInt("sid");
        aVar.d = jSONObject.optString("sType", MASTNativeAdConstants.RESPONSE_NATIVE_STRING);
        aVar.e = jSONObject.optLong(MASTNativeAdConstants.ID_STRING);
        aVar.y = jSONObject.optString(MASTNativeAdConstants.RESPONSE_MEDIATION_SOURCE);
        aVar.w = jSONObject.optInt("label");
        aVar.g = jSONObject.optInt("preClick");
        aVar.f = jSONObject.optInt("opentype");
        aVar.x = jSONObject.optInt("cacheTime");
        aVar.v = jSONObject.optInt("mType");
        aVar.i = jSONObject.optString("title");
        aVar.j = jSONObject.optString("description");
        aVar.o = jSONObject.optString("cta");
        aVar.l = jSONObject.optString(DuAd.DB_ICON);
        aVar.m = jSONObject.optString("screenshots");
        aVar.n = jSONObject.optString("landingURL");
        aVar.k = (float) jSONObject.optLong("rating");
        aVar.h = jSONObject.optString("package_name");
        return aVar;
    }
}
