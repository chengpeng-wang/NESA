package com.cleanmaster.func.process;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* compiled from: IPhoneMemoryInfo */
final class a implements Creator {
    a() {
    }

    /* renamed from: a */
    public final IPhoneMemoryInfo createFromParcel(Parcel parcel) {
        PhoneMemoryInfo phoneMemoryInfo = new PhoneMemoryInfo();
        phoneMemoryInfo.a = parcel.readLong();
        phoneMemoryInfo.b = parcel.readLong();
        phoneMemoryInfo.c = parcel.readInt();
        phoneMemoryInfo.d = parcel.readInt();
        phoneMemoryInfo.e = parcel.readLong();
        return phoneMemoryInfo;
    }

    /* renamed from: a */
    public final IPhoneMemoryInfo[] newArray(int i) {
        return new PhoneMemoryInfo[i];
    }
}
