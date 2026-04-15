package com.google.android.gms.ads.internal.formats;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.RemoteException;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzcj.zza;
import com.google.android.gms.internal.zzgk;

@zzgk
public class zzc extends zza {
    private final Uri mUri;
    private final Drawable zzvJ;

    public zzc(Drawable drawable, Uri uri) {
        this.zzvJ = drawable;
        this.mUri = uri;
    }

    public Uri getUri() throws RemoteException {
        return this.mUri;
    }

    public zzd zzdr() throws RemoteException {
        return zze.zzx(this.zzvJ);
    }
}
