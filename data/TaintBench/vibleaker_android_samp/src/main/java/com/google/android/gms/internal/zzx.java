package com.google.android.gms.internal;

import com.google.android.gms.internal.zzb.zza;
import java.util.Map;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.springframework.http.HttpHeaders;

public class zzx {
    public static String zza(Map<String, String> map) {
        return zzb(map, "ISO-8859-1");
    }

    public static zza zzb(zzi zzi) {
        Object obj;
        long j;
        Object obj2;
        long currentTimeMillis = System.currentTimeMillis();
        Map map = zzi.zzA;
        long j2 = 0;
        long j3 = 0;
        long j4 = 0;
        String str = (String) map.get(HttpHeaders.DATE);
        if (str != null) {
            j2 = zzg(str);
        }
        str = (String) map.get(HttpHeaders.CACHE_CONTROL);
        if (str != null) {
            String[] split = str.split(",");
            obj = null;
            j = 0;
            j4 = 0;
            for (String trim : split) {
                String trim2 = trim2.trim();
                if (trim2.equals("no-cache") || trim2.equals("no-store")) {
                    return null;
                }
                if (trim2.startsWith("max-age=")) {
                    try {
                        j4 = Long.parseLong(trim2.substring(8));
                    } catch (Exception e) {
                    }
                } else if (trim2.startsWith("stale-while-revalidate=")) {
                    try {
                        j = Long.parseLong(trim2.substring(23));
                    } catch (Exception e2) {
                    }
                } else if (trim2.equals("must-revalidate") || trim2.equals("proxy-revalidate")) {
                    obj = 1;
                }
            }
            j3 = j4;
            j4 = j;
            obj2 = 1;
        } else {
            obj = null;
            obj2 = null;
        }
        str = (String) map.get(HttpHeaders.EXPIRES);
        long zzg = str != null ? zzg(str) : 0;
        str = (String) map.get(HttpHeaders.LAST_MODIFIED);
        long zzg2 = str != null ? zzg(str) : 0;
        str = (String) map.get(HttpHeaders.ETAG);
        if (obj2 != null) {
            j3 = currentTimeMillis + (1000 * j3);
            j = obj != null ? j3 : (1000 * j4) + j3;
        } else if (j2 <= 0 || zzg < j2) {
            j = 0;
            j3 = 0;
        } else {
            j = (zzg - j2) + currentTimeMillis;
            j3 = j;
        }
        zza zza = new zza();
        zza.data = zzi.data;
        zza.zzb = str;
        zza.zzf = j3;
        zza.zze = j;
        zza.zzc = j2;
        zza.zzd = zzg2;
        zza.zzg = map;
        return zza;
    }

    public static String zzb(Map<String, String> map, String str) {
        String str2 = (String) map.get(HttpHeaders.CONTENT_TYPE);
        if (str2 == null) {
            return str;
        }
        String[] split = str2.split(";");
        for (int i = 1; i < split.length; i++) {
            String[] split2 = split[i].trim().split("=");
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
