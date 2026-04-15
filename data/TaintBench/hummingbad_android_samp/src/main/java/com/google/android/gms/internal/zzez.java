package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import org.json.JSONException;
import org.json.JSONObject;

@zzgk
public class zzez {
    private final boolean zzzI;
    private final boolean zzzJ;
    private final boolean zzzK;
    private final boolean zzzL;
    private final boolean zzzM;

    public static final class zza {
        /* access modifiers changed from: private */
        public boolean zzzI;
        /* access modifiers changed from: private */
        public boolean zzzJ;
        /* access modifiers changed from: private */
        public boolean zzzK;
        /* access modifiers changed from: private */
        public boolean zzzL;
        /* access modifiers changed from: private */
        public boolean zzzM;

        public zzez zzea() {
            return new zzez(this);
        }

        public zza zzo(boolean z) {
            this.zzzI = z;
            return this;
        }

        public zza zzp(boolean z) {
            this.zzzJ = z;
            return this;
        }

        public zza zzq(boolean z) {
            this.zzzK = z;
            return this;
        }

        public zza zzr(boolean z) {
            this.zzzL = z;
            return this;
        }

        public zza zzs(boolean z) {
            this.zzzM = z;
            return this;
        }
    }

    private zzez(zza zza) {
        this.zzzI = zza.zzzI;
        this.zzzJ = zza.zzzJ;
        this.zzzK = zza.zzzK;
        this.zzzL = zza.zzzL;
        this.zzzM = zza.zzzM;
    }

    public JSONObject toJson() {
        try {
            return new JSONObject().put("sms", this.zzzI).put("tel", this.zzzJ).put("calendar", this.zzzK).put("storePicture", this.zzzL).put("inlineVideo", this.zzzM);
        } catch (JSONException e) {
            zzb.zzb("Error occured while obtaining the MRAID capabilities.", e);
            return null;
        }
    }
}
