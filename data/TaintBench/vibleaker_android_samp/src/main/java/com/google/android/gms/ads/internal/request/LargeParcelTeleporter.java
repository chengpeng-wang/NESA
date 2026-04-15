package com.google.android.gms.ads.internal.request;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseInputStream;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzna;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;

@zzhb
public final class LargeParcelTeleporter implements SafeParcelable {
    public static final Creator<LargeParcelTeleporter> CREATOR = new zzl();
    final int mVersionCode;
    ParcelFileDescriptor zzIq;
    private Parcelable zzIr;
    private boolean zzIs;

    LargeParcelTeleporter(int versionCode, ParcelFileDescriptor parcelFileDescriptor) {
        this.mVersionCode = versionCode;
        this.zzIq = parcelFileDescriptor;
        this.zzIr = null;
        this.zzIs = true;
    }

    public LargeParcelTeleporter(SafeParcelable teleportee) {
        this.mVersionCode = 1;
        this.zzIq = null;
        this.zzIr = teleportee;
        this.zzIs = false;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.zzIq == null) {
            Parcel obtain = Parcel.obtain();
            try {
                this.zzIr.writeToParcel(obtain, 0);
                byte[] marshall = obtain.marshall();
                this.zzIq = zzf(marshall);
            } finally {
                obtain.recycle();
            }
        }
        zzl.zza(this, dest, flags);
    }

    public <T extends SafeParcelable> T zza(Creator<T> creator) {
        if (this.zzIs) {
            if (this.zzIq == null) {
                zzb.e("File descriptor is empty, returning null.");
                return null;
            }
            DataInputStream dataInputStream = new DataInputStream(new AutoCloseInputStream(this.zzIq));
            try {
                byte[] bArr = new byte[dataInputStream.readInt()];
                dataInputStream.readFully(bArr, 0, bArr.length);
                zzna.zzb(dataInputStream);
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.unmarshall(bArr, 0, bArr.length);
                    obtain.setDataPosition(0);
                    this.zzIr = (SafeParcelable) creator.createFromParcel(obtain);
                    this.zzIs = false;
                } finally {
                    obtain.recycle();
                }
            } catch (IOException e) {
                throw new IllegalStateException("Could not read from parcel file descriptor", e);
            } catch (Throwable th) {
                zzna.zzb(dataInputStream);
            }
        }
        return (SafeParcelable) this.zzIr;
    }

    /* access modifiers changed from: protected */
    public <T> ParcelFileDescriptor zzf(final byte[] bArr) {
        Throwable e;
        ParcelFileDescriptor parcelFileDescriptor = null;
        final Closeable autoCloseOutputStream;
        try {
            ParcelFileDescriptor[] createPipe = ParcelFileDescriptor.createPipe();
            autoCloseOutputStream = new AutoCloseOutputStream(createPipe[1]);
            try {
                new Thread(new Runnable() {
                    /* JADX WARNING: Removed duplicated region for block: B:14:0x0036  */
                    /* JADX WARNING: Removed duplicated region for block: B:13:0x0030  */
                    /* JADX WARNING: Removed duplicated region for block: B:20:0x0044  */
                    /* JADX WARNING: Removed duplicated region for block: B:18:0x003e  */
                    public void run() {
                        /*
                        r4 = this;
                        r2 = 0;
                        r1 = new java.io.DataOutputStream;	 Catch:{ IOException -> 0x001f, all -> 0x003a }
                        r0 = r2;	 Catch:{ IOException -> 0x001f, all -> 0x003a }
                        r1.<init>(r0);	 Catch:{ IOException -> 0x001f, all -> 0x003a }
                        r0 = r7;	 Catch:{ IOException -> 0x004a }
                        r0 = r0.length;	 Catch:{ IOException -> 0x004a }
                        r1.writeInt(r0);	 Catch:{ IOException -> 0x004a }
                        r0 = r7;	 Catch:{ IOException -> 0x004a }
                        r1.write(r0);	 Catch:{ IOException -> 0x004a }
                        if (r1 != 0) goto L_0x001b;
                    L_0x0015:
                        r0 = r2;
                        com.google.android.gms.internal.zzna.zzb(r0);
                    L_0x001a:
                        return;
                    L_0x001b:
                        com.google.android.gms.internal.zzna.zzb(r1);
                        goto L_0x001a;
                    L_0x001f:
                        r0 = move-exception;
                        r1 = r2;
                    L_0x0021:
                        r2 = "Error transporting the ad response";
                        com.google.android.gms.ads.internal.util.client.zzb.zzb(r2, r0);	 Catch:{ all -> 0x0048 }
                        r2 = com.google.android.gms.ads.internal.zzr.zzbF();	 Catch:{ all -> 0x0048 }
                        r3 = 1;
                        r2.zzb(r0, r3);	 Catch:{ all -> 0x0048 }
                        if (r1 != 0) goto L_0x0036;
                    L_0x0030:
                        r0 = r2;
                        com.google.android.gms.internal.zzna.zzb(r0);
                        goto L_0x001a;
                    L_0x0036:
                        com.google.android.gms.internal.zzna.zzb(r1);
                        goto L_0x001a;
                    L_0x003a:
                        r0 = move-exception;
                        r1 = r2;
                    L_0x003c:
                        if (r1 != 0) goto L_0x0044;
                    L_0x003e:
                        r1 = r2;
                        com.google.android.gms.internal.zzna.zzb(r1);
                    L_0x0043:
                        throw r0;
                    L_0x0044:
                        com.google.android.gms.internal.zzna.zzb(r1);
                        goto L_0x0043;
                    L_0x0048:
                        r0 = move-exception;
                        goto L_0x003c;
                    L_0x004a:
                        r0 = move-exception;
                        goto L_0x0021;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.ads.internal.request.LargeParcelTeleporter$AnonymousClass1.run():void");
                    }
                }).start();
                return createPipe[0];
            } catch (IOException e2) {
                e = e2;
            }
        } catch (IOException e3) {
            e = e3;
            autoCloseOutputStream = parcelFileDescriptor;
            zzb.zzb("Error transporting the ad response", e);
            zzr.zzbF().zzb(e, true);
            zzna.zzb(autoCloseOutputStream);
            return parcelFileDescriptor;
        }
    }
}
