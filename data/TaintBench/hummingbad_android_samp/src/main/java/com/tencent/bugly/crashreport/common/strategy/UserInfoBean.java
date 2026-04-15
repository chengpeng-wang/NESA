package com.tencent.bugly.crashreport.common.strategy;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.tencent.bugly.proguard.ad;
import java.util.Map;

/* compiled from: BUGLY */
public class UserInfoBean implements Parcelable {
    public static final Creator<UserInfoBean> CREATOR = new Creator<UserInfoBean>() {
        /* renamed from: a */
        public UserInfoBean createFromParcel(Parcel parcel) {
            return new UserInfoBean(parcel);
        }

        /* renamed from: a */
        public UserInfoBean[] newArray(int i) {
            return new UserInfoBean[i];
        }
    };
    public long a;
    public int b;
    public String c;
    public String d;
    public long e;
    public long f;
    public boolean g = false;
    public String h;
    public int i;
    public int j = -1;
    public int k = -1;
    public Map<String, String> l = null;
    public Map<String, String> m = null;

    public UserInfoBean(Parcel in) {
        boolean z = true;
        this.b = in.readInt();
        this.c = in.readString();
        this.d = in.readString();
        this.e = in.readLong();
        this.f = in.readLong();
        if (in.readByte() != (byte) 1) {
            z = false;
        }
        this.g = z;
        this.j = in.readInt();
        this.k = in.readInt();
        this.l = ad.b(in);
        this.m = ad.b(in);
        this.h = in.readString();
        this.i = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.b);
        dest.writeString(this.c);
        dest.writeString(this.d);
        dest.writeLong(this.e);
        dest.writeLong(this.f);
        dest.writeByte((byte) (this.g ? 1 : 0));
        dest.writeInt(this.j);
        dest.writeInt(this.k);
        ad.b(dest, this.l);
        ad.b(dest, this.m);
        dest.writeString(this.h);
        dest.writeInt(this.i);
    }
}
