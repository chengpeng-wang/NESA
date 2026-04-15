package com.google.android.gms.internal;

import com.google.android.gms.internal.zzb.zza;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.Map;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

public class zzx {
    public static String zza(Map<String, String> map) {
        return zzb(map, "ISO-8859-1");
    }

    public static zza zzb(zzi zzi) {
        long j;
        long currentTimeMillis = System.currentTimeMillis();
        Map map = zzi.zzA;
        long j2 = 0;
        long j3 = 0;
        long j4 = 0;
        long j5 = 0;
        long j6 = 0;
        Object obj = null;
        Object obj2 = null;
        String str = (String) map.get("Date");
        if (str != null) {
            j2 = zzg(str);
        }
        str = (String) map.get("Cache-Control");
        if (str != null) {
            obj = 1;
            String[] split = str.split(",");
            for (String trim : split) {
                String trim2 = trim2.trim();
                if (trim2.equals("no-cache") || trim2.equals("no-store")) {
                    return null;
                }
                if (trim2.startsWith("max-age=")) {
                    try {
                        j5 = Long.parseLong(trim2.substring(8));
                    } catch (Exception e) {
                    }
                } else if (trim2.startsWith("stale-while-revalidate=")) {
                    try {
                        j6 = Long.parseLong(trim2.substring(23));
                    } catch (Exception e2) {
                    }
                } else if (trim2.equals("must-revalidate") || trim2.equals("proxy-revalidate")) {
                    obj2 = 1;
                }
            }
        }
        long j7 = j6;
        Object obj3 = obj;
        long j8 = j5;
        long j9 = j7;
        str = (String) map.get("Expires");
        if (str != null) {
            j4 = zzg(str);
        }
        str = (String) map.get("Last-Modified");
        if (str != null) {
            j3 = zzg(str);
        }
        str = (String) map.get("ETag");
        if (obj3 != null) {
            j8 = (j8 * 1000) + currentTimeMillis;
            j = obj2 != null ? j8 : (1000 * j9) + j8;
        } else if (j2 <= 0 || j4 < j2) {
            j = 0;
            j8 = 0;
        } else {
            j = (j4 - j2) + currentTimeMillis;
            j8 = j;
        }
        zza zza = new zza();
        zza.data = zzi.data;
        zza.zzb = str;
        zza.zzf = j8;
        zza.zze = j;
        zza.zzc = j2;
        zza.zzd = j3;
        zza.zzg = map;
        return zza;
    }

    public static String zzb(Map<String, String> map, String str) {
        String str2 = (String) map.get(MASTNativeAdConstants.REQUEST_HEADER_CONTENT_TYPE);
        if (str2 == null) {
            return str;
        }
        String[] split = str2.split(";");
        for (int i = 1; i < split.length; i++) {
            String[] split2 = split[i].trim().split(MASTNativeAdConstants.EQUAL);
            if (split2.length == 2 && split2[0].equals("charset")) {
                return split2[1];
            }
        }
        return str;
    }

    public static long zzg(String str) {
        try {
            return DateUtils.parseDate(str).getTime();
        } catch (DateParseException e) {
            return 0;
        }
    }
}
