package com.cleanmaster.func.process;

import android.os.Parcel;

public class PhoneMemoryInfo implements IPhoneMemoryInfo {
    long a;
    long b;
    int c;
    int d;
    long e;

    PhoneMemoryInfo() {
    }

    public PhoneMemoryInfo(long available, long total) {
        a(available, total);
    }

    public void a(long j, long j2) {
        this.e = j;
        this.a = j2;
        this.d = 1;
        this.b = j;
        if (0 < this.a) {
            this.c = (int) ((((float) (this.a - this.b)) * 100.0f) / ((float) this.a));
        } else {
            this.c = 85;
        }
    }

    public int describeContents() {
        return 0;
    }

    public long a() {
        return this.a;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.a);
        dest.writeLong(this.b);
        dest.writeInt(this.c);
        dest.writeInt(this.d);
        dest.writeLong(this.e);
    }
}
