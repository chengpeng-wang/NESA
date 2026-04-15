package com.duapps.ad.entity;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* compiled from: AdData */
final class b implements Creator<a> {
    b() {
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        return new a(parcel, (byte) 0);
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new a[i];
    }
}
