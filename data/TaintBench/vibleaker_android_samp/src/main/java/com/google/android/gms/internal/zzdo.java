package com.google.android.gms.internal;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MotionEvent;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.overlay.zzk;
import com.google.android.gms.ads.internal.util.client.zzb;
import java.util.Map;
import java.util.WeakHashMap;
import org.json.JSONObject;

@zzhb
public final class zzdo implements zzdf {
    private final Map<zzjp, Integer> zzzI = new WeakHashMap();

    private static int zza(Context context, Map<String, String> map, String str, int i) {
        String str2 = (String) map.get(str);
        if (str2 == null) {
            return i;
        }
        try {
            return zzn.zzcS().zzb(context, Integer.parseInt(str2));
        } catch (NumberFormatException e) {
            zzb.zzaK("Could not parse " + str + " in a video GMSG: " + str2);
            return i;
        }
    }

    public void zza(zzjp zzjp, Map<String, String> map) {
        String str = (String) map.get("action");
        if (str == null) {
            zzb.zzaK("Action missing from video GMSG.");
            return;
        }
        if (zzb.zzQ(3)) {
            JSONObject jSONObject = new JSONObject(map);
            jSONObject.remove("google.afma.Notify_dt");
            zzb.zzaI("Video GMSG: " + str + " " + jSONObject.toString());
        }
        String str2;
        if ("background".equals(str)) {
            str2 = (String) map.get("color");
            if (TextUtils.isEmpty(str2)) {
                zzb.zzaK("Color parameter missing from color video GMSG.");
                return;
            }
            try {
                int parseColor = Color.parseColor(str2);
                zzjo zzia = zzjp.zzia();
                if (zzia != null) {
                    zzk zzhM = zzia.zzhM();
                    if (zzhM != null) {
                        zzhM.setBackgroundColor(parseColor);
                        return;
                    }
                }
                this.zzzI.put(zzjp, Integer.valueOf(parseColor));
                return;
            } catch (IllegalArgumentException e) {
                zzb.zzaK("Invalid color parameter in video GMSG.");
                return;
            }
        }
        zzjo zzia2 = zzjp.zzia();
        if (zzia2 == null) {
            zzb.zzaK("Could not get underlay container for a video GMSG.");
            return;
        }
        boolean equals = "new".equals(str);
        boolean equals2 = "position".equals(str);
        int zza;
        int zza2;
        if (equals || equals2) {
            int parseInt;
            Context context = zzjp.getContext();
            int zza3 = zza(context, map, "x", 0);
            zza = zza(context, map, "y", 0);
            zza2 = zza(context, map, "w", -1);
            int zza4 = zza(context, map, "h", -1);
            try {
                parseInt = Integer.parseInt((String) map.get("player"));
            } catch (NumberFormatException e2) {
                parseInt = 0;
            }
            if (equals && zzia2.zzhM() == null) {
                zzia2.zza(zza3, zza, zza2, zza4, parseInt);
                if (this.zzzI.containsKey(zzjp)) {
                    zza3 = ((Integer) this.zzzI.get(zzjp)).intValue();
                    zzk zzhM2 = zzia2.zzhM();
                    zzhM2.setBackgroundColor(zza3);
                    zzhM2.zzfE();
                    return;
                }
                return;
            }
            zzia2.zze(zza3, zza, zza2, zza4);
            return;
        }
        zzk zzhM3 = zzia2.zzhM();
        if (zzhM3 == null) {
            zzk.zzg(zzjp);
        } else if ("click".equals(str)) {
            Context context2 = zzjp.getContext();
            zza = zza(context2, map, "x", 0);
            zza2 = zza(context2, map, "y", 0);
            long uptimeMillis = SystemClock.uptimeMillis();
            MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, (float) zza, (float) zza2, 0);
            zzhM3.zzd(obtain);
            obtain.recycle();
        } else if ("currentTime".equals(str)) {
            str2 = (String) map.get("time");
            if (str2 == null) {
                zzb.zzaK("Time parameter missing from currentTime video GMSG.");
                return;
            }
            try {
                zzhM3.seekTo((int) (Float.parseFloat(str2) * 1000.0f));
            } catch (NumberFormatException e3) {
                zzb.zzaK("Could not parse time parameter from currentTime video GMSG: " + str2);
            }
        } else if ("hide".equals(str)) {
            zzhM3.setVisibility(4);
        } else if ("load".equals(str)) {
            zzhM3.zzfD();
        } else if ("mimetype".equals(str)) {
            zzhM3.setMimeType((String) map.get("mimetype"));
        } else if ("muted".equals(str)) {
            if (Boolean.parseBoolean((String) map.get("muted"))) {
                zzhM3.zzff();
            } else {
                zzhM3.zzfg();
            }
        } else if ("pause".equals(str)) {
            zzhM3.pause();
        } else if ("play".equals(str)) {
            zzhM3.play();
        } else if ("show".equals(str)) {
            zzhM3.setVisibility(0);
        } else if ("src".equals(str)) {
            zzhM3.zzap((String) map.get("src"));
        } else if ("volume".equals(str)) {
            str2 = (String) map.get("volume");
            if (str2 == null) {
                zzb.zzaK("Level parameter missing from volume video GMSG.");
                return;
            }
            try {
                zzhM3.zza(Float.parseFloat(str2));
            } catch (NumberFormatException e4) {
                zzb.zzaK("Could not parse volume parameter from volume video GMSG: " + str2);
            }
        } else if ("watermark".equals(str)) {
            zzhM3.zzfE();
        } else {
            zzb.zzaK("Unknown video action: " + str);
        }
    }
}
