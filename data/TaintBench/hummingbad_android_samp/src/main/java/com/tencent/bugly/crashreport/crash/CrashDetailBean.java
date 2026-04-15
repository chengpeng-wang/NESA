package com.tencent.bugly.crashreport.crash;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.tencent.bugly.crashreport.common.info.PlugInBean;
import com.tencent.bugly.proguard.ad;
import java.util.Map;
import java.util.UUID;

/* compiled from: BUGLY */
public class CrashDetailBean implements Parcelable, Comparable<CrashDetailBean> {
    public static final Creator<CrashDetailBean> CREATOR = new Creator<CrashDetailBean>() {
        /* renamed from: a */
        public CrashDetailBean createFromParcel(Parcel parcel) {
            return new CrashDetailBean(parcel);
        }

        /* renamed from: a */
        public CrashDetailBean[] newArray(int i) {
            return new CrashDetailBean[i];
        }
    };
    public String A = "";
    public long B = -1;
    public long C = -1;
    public long D = -1;
    public long E = -1;
    public long F = -1;
    public long G = -1;
    public String H = "";
    public String I = "";
    public String J = "";
    public String K = "";
    public String L = "";
    public long M = -1;
    public boolean N = false;
    public Map<String, String> O = null;
    public int P = -1;
    public int Q = -1;
    public Map<String, String> R = null;
    public Map<String, String> S = null;
    public byte[] T = null;
    public long a = -1;
    public int b = 0;
    public String c = UUID.randomUUID().toString();
    public boolean d = false;
    public String e = "";
    public String f = "";
    public String g = "";
    public Map<String, PlugInBean> h = null;
    public Map<String, PlugInBean> i = null;
    public boolean j = false;
    public boolean k = false;
    public int l = 0;
    public String m = "";
    public String n = "";
    public String o = "";
    public String p = "";
    public String q = "";
    public long r = -1;
    public String s = null;
    public int t = 0;
    public String u = "";
    public String v = "";
    public String w = null;
    public byte[] x = null;
    public Map<String, String> y = null;
    public String z = "";

    public CrashDetailBean(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.b = in.readInt();
        this.c = in.readString();
        this.d = in.readByte() == (byte) 1;
        this.e = in.readString();
        this.f = in.readString();
        this.g = in.readString();
        if (in.readByte() == (byte) 1) {
            z = true;
        } else {
            z = false;
        }
        this.j = z;
        if (in.readByte() == (byte) 1) {
            z = true;
        } else {
            z = false;
        }
        this.k = z;
        this.l = in.readInt();
        this.m = in.readString();
        this.n = in.readString();
        this.o = in.readString();
        this.p = in.readString();
        this.q = in.readString();
        this.r = in.readLong();
        this.s = in.readString();
        this.t = in.readInt();
        this.u = in.readString();
        this.v = in.readString();
        this.w = in.readString();
        this.y = ad.b(in);
        this.z = in.readString();
        this.A = in.readString();
        this.B = in.readLong();
        this.C = in.readLong();
        this.D = in.readLong();
        this.E = in.readLong();
        this.F = in.readLong();
        this.G = in.readLong();
        this.H = in.readString();
        this.I = in.readString();
        this.J = in.readString();
        this.K = in.readString();
        this.L = in.readString();
        this.M = in.readLong();
        if (in.readByte() != (byte) 1) {
            z2 = false;
        }
        this.N = z2;
        this.O = ad.b(in);
        this.h = ad.a(in);
        this.i = ad.a(in);
        this.P = in.readInt();
        this.Q = in.readInt();
        this.R = ad.b(in);
        this.S = ad.b(in);
        this.T = in.createByteArray();
        this.x = in.createByteArray();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeInt(this.b);
        dest.writeString(this.c);
        dest.writeByte((byte) (this.d ? 1 : 0));
        dest.writeString(this.e);
        dest.writeString(this.f);
        dest.writeString(this.g);
        if (this.j) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (this.k) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        dest.writeInt(this.l);
        dest.writeString(this.m);
        dest.writeString(this.n);
        dest.writeString(this.o);
        dest.writeString(this.p);
        dest.writeString(this.q);
        dest.writeLong(this.r);
        dest.writeString(this.s);
        dest.writeInt(this.t);
        dest.writeString(this.u);
        dest.writeString(this.v);
        dest.writeString(this.w);
        ad.b(dest, this.y);
        dest.writeString(this.z);
        dest.writeString(this.A);
        dest.writeLong(this.B);
        dest.writeLong(this.C);
        dest.writeLong(this.D);
        dest.writeLong(this.E);
        dest.writeLong(this.F);
        dest.writeLong(this.G);
        dest.writeString(this.H);
        dest.writeString(this.I);
        dest.writeString(this.J);
        dest.writeString(this.K);
        dest.writeString(this.L);
        dest.writeLong(this.M);
        if (!this.N) {
            i2 = 0;
        }
        dest.writeByte((byte) i2);
        ad.b(dest, this.O);
        ad.a(dest, this.h);
        ad.a(dest, this.i);
        dest.writeInt(this.P);
        dest.writeInt(this.Q);
        ad.b(dest, this.R);
        ad.b(dest, this.S);
        dest.writeByteArray(this.T);
        dest.writeByteArray(this.x);
    }

    /* renamed from: a */
    public int compareTo(CrashDetailBean crashDetailBean) {
        if (crashDetailBean == null) {
            return 1;
        }
        long j = this.r - crashDetailBean.r;
        if (j > 0) {
            return 1;
        }
        if (j < 0) {
            return -1;
        }
        return 0;
    }
}
