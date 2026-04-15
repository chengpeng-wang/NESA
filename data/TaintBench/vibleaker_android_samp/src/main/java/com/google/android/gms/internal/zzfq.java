package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public class zzfq {
    private final boolean zzDu;
    private final boolean zzDv;
    private final boolean zzDw;
    private final boolean zzDx;
    private final boolean zzDy;

    public static final class zza {
        /* access modifiers changed from: private */
        public boolean zzDu;
        /* access modifiers changed from: private */
        public boolean zzDv;
        /* access modifiers changed from: private */
        public boolean zzDw;
        /* access modifiers changed from: private */
        public boolean zzDx;
        /* access modifiers changed from: private */
        public boolean zzDy;

        public zzfq zzeP() {
            return new zzfq(this);
        }

        public zza zzq(boolean z) {
            this.zzDu = z;
            return this;
        }

        public zza zzr(boolean z) {
            this.zzDv = z;
            return this;
        }

        public zza zzs(boolean z) {
            this.zzDw = z;
            return this;
        }

        public zza zzt(boolean z) {
            this.zzDx = z;
            return this;
        }

        public zza zzu(boolean z) {
            this.zzDy = z;
            return this;
        }
    }

    private zzfq(zza zza) {
        this.zzDu = zza.zzDu;
        this.zzDv = zza.zzDv;
        this.zzDw = zza.zzDw;
        this.zzDx = zza.zzDx;
        this.zzDy = zza.zzDy;
    }

    public JSONObject toJson() {
        try {
            return new JSONObject().put("sms", this.zzDu).put("tel", this.zzDv).put("calendar", this.zzDw).put("storePicture", this.zzDx).put("inlineVideo", this.zzDy);
        } catch (JSONException e) {
            zzb.zzb("Error occured while obtaining the MRAID capabilities.", e);
            return null;
        }
    }
}
