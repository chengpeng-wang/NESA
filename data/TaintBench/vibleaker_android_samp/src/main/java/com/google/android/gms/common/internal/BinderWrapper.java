package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.annotation.KeepName;

@KeepName
public final class BinderWrapper implements Parcelable {
    public static final Creator<BinderWrapper> CREATOR = new Creator<BinderWrapper>() {
        /* renamed from: zzan */
        public BinderWrapper createFromParcel(Parcel parcel) {
            return new BinderWrapper(parcel, null);
        }

        /* renamed from: zzbQ */
        public BinderWrapper[] newArray(int i) {
            return new BinderWrapper[i];
        }
    };
    private IBinder zzakD;

    public BinderWrapper() {
        this.zzakD = null;
    }

    public BinderWrapper(IBinder binder) {
        this.zzakD = null;
        this.zzakD = binder;
    }

    private BinderWrapper(Parcel in) {
        this.zzakD = null;
        this.zzakD = in.readStrongBinder();
    }

    /* synthetic */ BinderWrapper(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(this.zzakD);
    }
}
