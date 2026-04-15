package com.duapps.ad.b;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* compiled from: IMDataModel */
final class d implements Creator<c> {
    d() {
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        return new c(parcel, (byte) 0);
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new c[i];
    }
}
