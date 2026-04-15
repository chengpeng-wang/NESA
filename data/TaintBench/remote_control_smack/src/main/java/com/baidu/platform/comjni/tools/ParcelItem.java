package com.baidu.platform.comjni.tools;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ParcelItem implements Parcelable {
    public static final Creator<ParcelItem> a = new b();
    private Bundle b;

    public int describeContents() {
        return 0;
    }

    public Bundle getBundle() {
        return this.b;
    }

    public void setBundle(Bundle bundle) {
        this.b = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.b);
    }
}
