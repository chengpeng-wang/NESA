package com.tencent.bugly.crashreport.common.strategy;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.tencent.bugly.proguard.ad;
import java.util.Date;
import java.util.Map;

/* compiled from: BUGLY */
public class StrategyBean implements Parcelable {
    public static final Creator<StrategyBean> CREATOR = new Creator<StrategyBean>() {
        /* renamed from: a */
        public StrategyBean createFromParcel(Parcel parcel) {
            return new StrategyBean(parcel);
        }

        /* renamed from: a */
        public StrategyBean[] newArray(int i) {
            return new StrategyBean[i];
        }
    };
    public static String a;
    public long b;
    public long c;
    public boolean d;
    public boolean e;
    public boolean f;
    public boolean g;
    public boolean h;
    public boolean i;
    public boolean j;
    public boolean k;
    public long l;
    public long m;
    public String n;
    public String o;
    public String p;
    public Map<String, String> q;

    public StrategyBean() {
        this.b = -1;
        this.c = -1;
        this.d = true;
        this.e = true;
        this.f = true;
        this.g = true;
        this.h = true;
        this.i = true;
        this.j = true;
        this.k = true;
        this.m = 30000;
        this.n = "http://rqd.uu.qq.com/rqd/sync";
        this.o = "http://rqd.uu.qq.com/rqd/sync";
        this.c = new Date().getTime();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("S(").append("@L@L").append("@)");
        a = stringBuilder.toString();
        stringBuilder.setLength(0);
        stringBuilder.append("*^").append("@K#K").append("@!");
        this.p = stringBuilder.toString();
    }

    public StrategyBean(Parcel in) {
        boolean z = true;
        this.b = -1;
        this.c = -1;
        this.d = true;
        this.e = true;
        this.f = true;
        this.g = true;
        this.h = true;
        this.i = true;
        this.j = true;
        this.k = true;
        this.m = 30000;
        this.n = "http://rqd.uu.qq.com/rqd/sync";
        this.o = "http://rqd.uu.qq.com/rqd/sync";
        try {
            boolean z2;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("S(").append("@L@L").append("@)");
            a = stringBuilder.toString();
            this.c = in.readLong();
            this.d = in.readByte() == (byte) 1;
            if (in.readByte() == (byte) 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.e = z2;
            if (in.readByte() == (byte) 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.f = z2;
            this.n = in.readString();
            this.o = in.readString();
            this.p = in.readString();
            this.q = ad.b(in);
            if (in.readByte() == (byte) 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.g = z2;
            if (in.readByte() == (byte) 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.j = z2;
            if (in.readByte() == (byte) 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.k = z2;
            this.m = in.readLong();
            if (in.readByte() == (byte) 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.h = z2;
            if (in.readByte() != (byte) 1) {
                z = false;
            }
            this.i = z;
            this.l = in.readLong();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeLong(this.c);
        dest.writeByte((byte) (this.d ? 1 : 0));
        if (this.e) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (this.f) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        dest.writeString(this.n);
        dest.writeString(this.o);
        dest.writeString(this.p);
        ad.b(dest, this.q);
        if (this.g) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
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
        dest.writeLong(this.m);
        if (this.h) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (!this.i) {
            i2 = 0;
        }
        dest.writeByte((byte) i2);
        dest.writeLong(this.l);
    }
}
