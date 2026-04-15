package com.baidu.android.pushservice.message;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class h implements Creator {
    h() {
    }

    /* renamed from: a */
    public PublicMsg createFromParcel(Parcel parcel) {
        return new PublicMsg(parcel);
    }

    /* renamed from: a */
    public PublicMsg[] newArray(int i) {
        return new PublicMsg[i];
    }
}
