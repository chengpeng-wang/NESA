package com.tencent.bugly.crashreport.common.info;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/* compiled from: BUGLY */
public class PlugInBean implements Parcelable {
    public static final Creator<PlugInBean> CREATOR = new Creator<PlugInBean>() {
        /* renamed from: a */
        public PlugInBean createFromParcel(Parcel parcel) {
            return new PlugInBean(parcel);
        }

        /* renamed from: a */
        public PlugInBean[] newArray(int i) {
            return new PlugInBean[i];
        }
    };
    public final String a;
    public final String b;
    public final String c;

    public String toString() {
        return "plid:" + this.a + " plV:" + this.b + " plUUID:" + this.c;
    }

    public PlugInBean(Parcel in) {
        this.a = in.readString();
        this.b = in.readString();
        this.c = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.a);
        dest.writeString(this.b);
        dest.writeString(this.c);
    }
}
