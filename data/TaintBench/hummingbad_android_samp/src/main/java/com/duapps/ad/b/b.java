package com.duapps.ad.b;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* compiled from: IMData */
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
