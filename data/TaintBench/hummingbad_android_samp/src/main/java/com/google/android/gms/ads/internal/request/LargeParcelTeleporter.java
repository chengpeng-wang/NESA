package com.google.android.gms.ads.internal.request;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseInputStream;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzlr;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class LargeParcelTeleporter implements SafeParcelable {
    public static final Creator<LargeParcelTeleporter> CREATOR = new zzl();
    final int mVersionCode;
    ParcelFileDescriptor zzEo;
    private Parcelable zzEp;
    private boolean zzEq;

    LargeParcelTeleporter(int versionCode, ParcelFileDescriptor parcelFileDescriptor) {
        this.mVersionCode = versionCode;
        this.zzEo = parcelFileDescriptor;
        this.zzEp = null;
        this.zzEq = true;
    }

    public LargeParcelTeleporter(SafeParcelable teleportee) {
        this.mVersionCode = 1;
        this.zzEo = null;
        this.zzEp = teleportee;
        this.zzEq = false;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.zzEo == null) {
            Parcel obtain = Parcel.obtain();
            try {
                this.zzEp.writeToParcel(obtain, 0);
                byte[] marshall = obtain.marshall();
                this.zzEo = zzf(marshall);
            } finally {
                obtain.recycle();
            }
        }
        zzl.zza(this, dest, flags);
    }

    public <T extends SafeParcelable> T zza(Creator<T> creator) {
        if (this.zzEq) {
            if (this.zzEo == null) {
                zzb.e("File descriptor is empty, returning null.");
                return null;
            }
            DataInputStream dataInputStream = new DataInputStream(new AutoCloseInputStream(this.zzEo));
            try {
                byte[] bArr = new byte[dataInputStream.readInt()];
                dataInputStream.readFully(bArr, 0, bArr.length);
                zzlr.zzb(dataInputStream);
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.unmarshall(bArr, 0, bArr.length);
                    obtain.setDataPosition(0);
                    this.zzEp = (SafeParcelable) creator.createFromParcel(obtain);
                    this.zzEq = false;
                } finally {
                    obtain.recycle();
                }
            } catch (IOException e) {
                throw new IllegalStateException("Could not read from parcel file descriptor", e);
            } catch (Throwable th) {
                zzlr.zzb(dataInputStream);
            }
        }
        return (SafeParcelable) this.zzEp;
    }

    /* access modifiers changed from: protected */
    public <T> ParcelFileDescriptor zzf(final byte[] bArr) {
        try {
            ParcelFileDescriptor[] createPipe = ParcelFileDescriptor.createPipe();
            final AutoCloseOutputStream autoCloseOutputStream = new AutoCloseOutputStream(createPipe[1]);
            new Thread(new Runnable() {
                public void run() {
                    DataOutputStream dataOutputStream = new DataOutputStream(autoCloseOutputStream);
                    try {
                        dataOutputStream.writeInt(bArr.length);
                        dataOutputStream.write(bArr);
                    } catch (IOException e) {
                        zzb.zzb("Error transporting the ad response", e);
                        zzp.zzbA().zzc(e, true);
                    } finally {
                        zzlr.zzb(dataOutputStream);
                    }
                }
            }).start();
            return createPipe[0];
        } catch (IOException e) {
            zzb.zzb("Error transporting the ad response", e);
            zzp.zzbA().zzc(e, true);
            return null;
        }
    }
}
