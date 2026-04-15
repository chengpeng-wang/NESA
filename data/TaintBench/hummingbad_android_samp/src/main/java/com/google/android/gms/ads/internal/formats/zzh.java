package com.google.android.gms.ads.internal.formats;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import com.cmcm.adsdk.Const;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzn;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzan;
import com.google.android.gms.internal.zzbb;
import com.google.android.gms.internal.zzdg;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzip;
import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONException;
import org.json.JSONObject;

@zzgk
public class zzh {
    private final Context mContext;
    /* access modifiers changed from: private */
    public zzip zzoL;
    private final VersionInfoParcel zzpa;
    private final Object zzpc = new Object();
    private final zzn zzwb;
    private final JSONObject zzwe;
    /* access modifiers changed from: private|final */
    public final zzbb zzwf;
    private final zza zzwg;
    private final zzan zzwh;
    private boolean zzwi;
    /* access modifiers changed from: private */
    public String zzwj;

    public interface zza {
        String getCustomTemplateId();

        void zza(zzh zzh);

        String zzdu();

        zza zzdv();
    }

    public zzh(Context context, zzn zzn, zzbb zzbb, zzan zzan, JSONObject jSONObject, zza zza, VersionInfoParcel versionInfoParcel) {
        this.mContext = context;
        this.zzwb = zzn;
        this.zzwf = zzbb;
        this.zzwh = zzan;
        this.zzwe = jSONObject;
        this.zzwg = zza;
        this.zzpa = versionInfoParcel;
    }

    public void performClick(String assetId) {
        zzx.zzch("performClick must be called on the main UI thread.");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("asset", assetId);
            jSONObject.put("template", this.zzwg.zzdu());
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(Const.KEY_JUHE, this.zzwe);
            jSONObject2.put("click", jSONObject);
            jSONObject2.put("has_custom_click_handler", this.zzwb.zzr(this.zzwg.getCustomTemplateId()) != null);
            this.zzwf.zza("google.afma.nativeAds.handleClickGmsg", jSONObject2);
        } catch (JSONException e) {
            zzb.zzb("Unable to create click JSON.", e);
        }
    }

    public void recordImpression() {
        zzx.zzch("recordImpression must be called on the main UI thread.");
        zzl(true);
        this.zzwb.zzaP();
    }

    public zzb zza(OnClickListener onClickListener) {
        zza zzdv = this.zzwg.zzdv();
        if (zzdv == null) {
            return null;
        }
        zzb zzb = new zzb(this.mContext, zzdv);
        zzb.setLayoutParams(new LayoutParams(-1, -1));
        zzb.zzdq().setOnClickListener(onClickListener);
        zzb.zzdq().setContentDescription("Ad attribution icon");
        return zzb;
    }

    public void zzb(MotionEvent motionEvent) {
        this.zzwh.zza(motionEvent);
    }

    public void zzb(View view, Map<String, WeakReference<View>> map) {
        zzx.zzch("performClick must be called on the main UI thread.");
        for (Entry entry : map.entrySet()) {
            if (view.equals((View) ((WeakReference) entry.getValue()).get())) {
                performClick((String) entry.getKey());
                return;
            }
        }
    }

    public zzip zzdy() {
        this.zzoL = zzdz();
        this.zzoL.getWebView().setVisibility(8);
        this.zzwf.zza("/loadHtml", (zzdg) new zzdg() {
            public void zza(zzip zzip, final Map<String, String> map) {
                zzh.this.zzoL.zzgS().zza(new com.google.android.gms.internal.zziq.zza() {
                    public void zza(zzip zzip, boolean z) {
                        zzh.this.zzwj = (String) map.get(MASTNativeAdConstants.ID_STRING);
                        JSONObject jSONObject = new JSONObject();
                        try {
                            jSONObject.put("messageType", "htmlLoaded");
                            jSONObject.put(MASTNativeAdConstants.ID_STRING, zzh.this.zzwj);
                            zzh.this.zzwf.zzb("sendMessageToNativeJs", jSONObject);
                        } catch (JSONException e) {
                            zzb.zzb("Unable to dispatch sendMessageToNativeJsevent", e);
                        }
                    }
                });
                zzh.this.zzoL.loadData((String) map.get("overlayHtml"), "text/html", Defaults.ENCODING_UTF_8);
            }
        });
        this.zzwf.zza("/showOverlay", (zzdg) new zzdg() {
            public void zza(zzip zzip, Map<String, String> map) {
                zzh.this.zzoL.getWebView().setVisibility(0);
            }
        });
        this.zzwf.zza("/hideOverlay", (zzdg) new zzdg() {
            public void zza(zzip zzip, Map<String, String> map) {
                zzh.this.zzoL.getWebView().setVisibility(8);
            }
        });
        this.zzoL.zzgS().zza("/hideOverlay", new zzdg() {
            public void zza(zzip zzip, Map<String, String> map) {
                zzh.this.zzoL.getWebView().setVisibility(8);
            }
        });
        this.zzoL.zzgS().zza("/sendMessageToSdk", new zzdg() {
            public void zza(zzip zzip, Map<String, String> map) {
                JSONObject jSONObject = new JSONObject();
                try {
                    for (String str : map.keySet()) {
                        jSONObject.put(str, map.get(str));
                    }
                    jSONObject.put(MASTNativeAdConstants.ID_STRING, zzh.this.zzwj);
                    zzh.this.zzwf.zzb("sendMessageToNativeJs", jSONObject);
                } catch (JSONException e) {
                    zzb.zzb("Unable to dispatch sendMessageToNativeJs event", e);
                }
            }
        });
        return this.zzoL;
    }

    /* access modifiers changed from: 0000 */
    public zzip zzdz() {
        return zzp.zzby().zza(this.mContext, AdSizeParcel.zzs(this.mContext), false, false, this.zzwh, this.zzpa);
    }

    public void zzh(View view) {
    }

    public void zzi(View view) {
        synchronized (this.zzpc) {
            if (this.zzwi) {
            } else if (!view.isShown()) {
            } else if (view.getGlobalVisibleRect(new Rect(), null)) {
                recordImpression();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void zzl(boolean z) {
        this.zzwi = z;
    }
}
