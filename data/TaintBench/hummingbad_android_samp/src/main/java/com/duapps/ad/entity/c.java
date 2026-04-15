package com.duapps.ad.entity;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: AdModel */
public final class c implements Parcelable {
    public final List<a> a;
    private String b;
    private String c;
    private int d;
    private String e;
    private int f;
    private int g;
    private int h;
    private long i;
    private String j;
    private String k;
    private String l;
    private String m;

    /* synthetic */ c(Parcel parcel, byte b) {
        this(parcel);
    }

    public c() {
        this.a = new ArrayList();
    }

    public c(String str, int i, String str2, JSONObject jSONObject, long j) {
        this.a = new ArrayList();
        this.b = str;
        this.d = i;
        this.e = str2;
        if (jSONObject != null) {
            if (i == jSONObject.optInt("sId")) {
                this.f = jSONObject.optInt("pn");
                this.g = jSONObject.optInt("ps");
                this.h = jSONObject.optInt("total");
                this.c = jSONObject.optString("logId");
                this.j = jSONObject.optString("ext");
                this.k = jSONObject.optString("title");
                this.l = jSONObject.optString("shortdesc");
                this.m = jSONObject.optString("description");
                JSONArray optJSONArray = jSONObject.optJSONArray("list");
                if (optJSONArray != null) {
                    int length = optJSONArray.length();
                    for (int i2 = 0; i2 < length; i2++) {
                        JSONObject optJSONObject = optJSONArray.optJSONObject(i2);
                        if (optJSONObject != null) {
                            this.a.add(new a(str, i, str2, this.c, optJSONObject, j));
                        }
                    }
                }
            }
        }
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.b);
        parcel.writeString(this.c);
        parcel.writeInt(this.d);
        parcel.writeString(this.e);
        parcel.writeInt(this.f);
        parcel.writeInt(this.g);
        parcel.writeInt(this.h);
        parcel.writeTypedList(this.a);
        parcel.writeString(this.j);
        parcel.writeString(this.k);
        parcel.writeString(this.l);
        parcel.writeString(this.m);
        parcel.writeLong(this.i);
    }

    private c(Parcel parcel) {
        this.a = new ArrayList();
        this.b = parcel.readString();
        this.c = parcel.readString();
        this.d = parcel.readInt();
        this.e = parcel.readString();
        this.f = parcel.readInt();
        this.g = parcel.readInt();
        this.h = parcel.readInt();
        parcel.readTypedList(this.a, a.v);
        this.j = parcel.readString();
        this.k = parcel.readString();
        this.l = parcel.readString();
        this.m = parcel.readString();
        this.i = parcel.readLong();
    }

    static {
        d dVar = new d();
    }
}
