package com.duapps.ad.b;

import android.os.Parcel;
import android.os.Parcelable;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: IMDataModel */
public final class c implements Parcelable {
    public final List<a> a;
    private String b;
    private int c;
    private String d;
    private String e;
    private long f;

    /* synthetic */ c(Parcel parcel, byte b) {
        this(parcel);
    }

    public c() {
        this.a = new ArrayList();
    }

    public c(String str, int i, String str2, JSONObject jSONObject, long j) {
        this.a = new ArrayList();
        this.b = str;
        this.c = i;
        this.e = str2;
        this.f = j;
        if (jSONObject != null) {
            if (i == jSONObject.optInt("sId")) {
                this.d = jSONObject.optString("logId");
                JSONArray optJSONArray = jSONObject.optJSONArray("list");
                if (optJSONArray.length() != 0) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(0);
                    if (optJSONObject != null) {
                        JSONArray optJSONArray2 = optJSONObject.optJSONArray(MASTNativeAdConstants.RESPONSE_ADS);
                        for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                            this.a.add(new a(str, i, str2, this.d, i2, optJSONObject, j));
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
        parcel.writeString(this.d);
        parcel.writeInt(this.c);
        parcel.writeString(this.e);
        parcel.writeLong(this.f);
        parcel.writeTypedList(this.a);
    }

    private c(Parcel parcel) {
        this.a = new ArrayList();
        this.b = parcel.readString();
        this.d = parcel.readString();
        this.c = parcel.readInt();
        this.e = parcel.readString();
        this.f = parcel.readLong();
        parcel.readTypedList(this.a, a.u);
    }

    static {
        d dVar = new d();
    }
}
