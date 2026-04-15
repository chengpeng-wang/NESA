package com.google.android.gms.ads.internal.purchase;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.ads.purchase.InAppPurchaseActivity;
import com.google.android.gms.common.zze;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzih;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public class zzi {
    public void zza(Context context, boolean z, GInAppPurchaseManagerInfoParcel gInAppPurchaseManagerInfoParcel) {
        Intent intent = new Intent();
        intent.setClassName(context, InAppPurchaseActivity.CLASS_NAME);
        intent.putExtra("com.google.android.gms.ads.internal.purchase.useClientJar", z);
        GInAppPurchaseManagerInfoParcel.zza(intent, gInAppPurchaseManagerInfoParcel);
        zzr.zzbC().zzb(context, intent);
    }

    public String zzaq(String str) {
        String str2 = null;
        if (str == null) {
            return str2;
        }
        try {
            return new JSONObject(str).getString("developerPayload");
        } catch (JSONException e) {
            zzb.zzaK("Fail to parse purchase data");
            return str2;
        }
    }

    public String zzar(String str) {
        String str2 = null;
        if (str == null) {
            return str2;
        }
        try {
            return new JSONObject(str).getString("purchaseToken");
        } catch (JSONException e) {
            zzb.zzaK("Fail to parse purchase data");
            return str2;
        }
    }

    public int zzd(Intent intent) {
        if (intent == null) {
            return 5;
        }
        Object obj = intent.getExtras().get("RESPONSE_CODE");
        if (obj == null) {
            zzb.zzaK("Intent with no response code, assuming OK (known issue)");
            return 0;
        } else if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        } else {
            if (obj instanceof Long) {
                return (int) ((Long) obj).longValue();
            }
            zzb.zzaK("Unexpected type for intent response code. " + obj.getClass().getName());
            return 5;
        }
    }

    public int zzd(Bundle bundle) {
        Object obj = bundle.get("RESPONSE_CODE");
        if (obj == null) {
            zzb.zzaK("Bundle with null response code, assuming OK (known issue)");
            return 0;
        } else if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        } else {
            if (obj instanceof Long) {
                return (int) ((Long) obj).longValue();
            }
            zzb.zzaK("Unexpected type for intent response code. " + obj.getClass().getName());
            return 5;
        }
    }

    public String zze(Intent intent) {
        return intent == null ? null : intent.getStringExtra("INAPP_PURCHASE_DATA");
    }

    public String zzf(Intent intent) {
        return intent == null ? null : intent.getStringExtra("INAPP_DATA_SIGNATURE");
    }

    public void zzz(final Context context) {
        AnonymousClass1 anonymousClass1 = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder service) {
                boolean z = false;
                zzb zzb = new zzb(context.getApplicationContext(), false);
                zzb.zzN(service);
                int zzb2 = zzb.zzb(3, context.getPackageName(), "inapp");
                zzih zzbF = zzr.zzbF();
                if (zzb2 == 0) {
                    z = true;
                }
                zzbF.zzC(z);
                context.unbindService(this);
                zzb.destroy();
            }

            public void onServiceDisconnected(ComponentName name) {
            }
        };
        Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        intent.setPackage(zze.GOOGLE_PLAY_STORE_PACKAGE);
        context.bindService(intent, anonymousClass1, 1);
    }
}
