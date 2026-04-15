package com.google.android.gms.internal;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.overlay.AdLauncherIntentInfoParcel;
import com.google.android.gms.ads.internal.zze;
import com.google.android.gms.ads.internal.zzr;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@zzhb
public final class zzdm implements zzdf {
    private final zze zzzA;
    private final zzfn zzzB;
    private final zzdh zzzD;

    public static class zza extends zzim {
        private final String zzF;
        private final zzjp zzpD;
        private final String zzzE = "play.google.com";
        private final String zzzF = "market";
        private final int zzzG = 10;

        public zza(zzjp zzjp, String str) {
            this.zzpD = zzjp;
            this.zzF = str;
        }

        public void onStop() {
        }

        public Intent zzT(String str) {
            Uri parse = Uri.parse(str);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(268435456);
            intent.setData(parse);
            return intent;
        }

        /* JADX WARNING: Removed duplicated region for block: B:37:0x00aa A:{SYNTHETIC, Splitter:B:37:0x00aa} */
        /* JADX WARNING: Removed duplicated region for block: B:62:0x008f A:{SYNTHETIC} */
        public void zzbr() {
            /*
            r8 = this;
            r0 = 0;
            r2 = r8.zzF;
        L_0x0003:
            r1 = 10;
            if (r0 >= r1) goto L_0x0119;
        L_0x0007:
            r4 = r0 + 1;
            r0 = new java.net.URL;	 Catch:{ IndexOutOfBoundsException -> 0x00b6, IOException -> 0x00d1, RuntimeException -> 0x00ec }
            r0.<init>(r2);	 Catch:{ IndexOutOfBoundsException -> 0x00b6, IOException -> 0x00d1, RuntimeException -> 0x00ec }
            r1 = "play.google.com";
            r3 = r0.getHost();	 Catch:{ IndexOutOfBoundsException -> 0x00b6, IOException -> 0x00d1, RuntimeException -> 0x00ec }
            r1 = r1.equalsIgnoreCase(r3);	 Catch:{ IndexOutOfBoundsException -> 0x00b6, IOException -> 0x00d1, RuntimeException -> 0x00ec }
            if (r1 == 0) goto L_0x002d;
        L_0x001a:
            r0 = r2;
        L_0x001b:
            r0 = r8.zzT(r0);
            r1 = com.google.android.gms.ads.internal.zzr.zzbC();
            r2 = r8.zzpD;
            r2 = r2.getContext();
            r1.zzb(r2, r0);
            return;
        L_0x002d:
            r1 = "market";
            r3 = r0.getProtocol();	 Catch:{ IndexOutOfBoundsException -> 0x00b6, IOException -> 0x00d1, RuntimeException -> 0x00ec }
            r1 = r1.equalsIgnoreCase(r3);	 Catch:{ IndexOutOfBoundsException -> 0x00b6, IOException -> 0x00d1, RuntimeException -> 0x00ec }
            if (r1 == 0) goto L_0x003b;
        L_0x0039:
            r0 = r2;
            goto L_0x001b;
        L_0x003b:
            r0 = r0.openConnection();	 Catch:{ IndexOutOfBoundsException -> 0x00b6, IOException -> 0x00d1, RuntimeException -> 0x00ec }
            r0 = (java.net.HttpURLConnection) r0;	 Catch:{ IndexOutOfBoundsException -> 0x00b6, IOException -> 0x00d1, RuntimeException -> 0x00ec }
            r1 = com.google.android.gms.ads.internal.zzr.zzbC();	 Catch:{ all -> 0x00b1 }
            r3 = r8.zzpD;	 Catch:{ all -> 0x00b1 }
            r3 = r3.getContext();	 Catch:{ all -> 0x00b1 }
            r5 = r8.zzpD;	 Catch:{ all -> 0x00b1 }
            r5 = r5.zzhX();	 Catch:{ all -> 0x00b1 }
            r5 = r5.afmaVersion;	 Catch:{ all -> 0x00b1 }
            r6 = 0;
            r1.zza(r3, r5, r6, r0);	 Catch:{ all -> 0x00b1 }
            r1 = r0.getResponseCode();	 Catch:{ all -> 0x00b1 }
            r5 = r0.getHeaderFields();	 Catch:{ all -> 0x00b1 }
            r3 = "";
            r6 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
            if (r1 < r6) goto L_0x0116;
        L_0x0065:
            r6 = 399; // 0x18f float:5.59E-43 double:1.97E-321;
            if (r1 > r6) goto L_0x0116;
        L_0x0069:
            r1 = 0;
            r6 = "Location";
            r6 = r5.containsKey(r6);	 Catch:{ all -> 0x00b1 }
            if (r6 == 0) goto L_0x0099;
        L_0x0072:
            r1 = "Location";
            r1 = r5.get(r1);	 Catch:{ all -> 0x00b1 }
            r1 = (java.util.List) r1;	 Catch:{ all -> 0x00b1 }
        L_0x007a:
            if (r1 == 0) goto L_0x0116;
        L_0x007c:
            r5 = r1.size();	 Catch:{ all -> 0x00b1 }
            if (r5 <= 0) goto L_0x0116;
        L_0x0082:
            r3 = 0;
            r1 = r1.get(r3);	 Catch:{ all -> 0x00b1 }
            r1 = (java.lang.String) r1;	 Catch:{ all -> 0x00b1 }
        L_0x0089:
            r3 = android.text.TextUtils.isEmpty(r1);	 Catch:{ all -> 0x00b1 }
            if (r3 == 0) goto L_0x00aa;
        L_0x008f:
            r1 = "Arrived at landing page, this ideally should not happen. Will open it in browser.";
            com.google.android.gms.ads.internal.util.client.zzb.zzaK(r1);	 Catch:{ all -> 0x00b1 }
            r0.disconnect();	 Catch:{ IndexOutOfBoundsException -> 0x00b6, IOException -> 0x00d1, RuntimeException -> 0x00ec }
            r0 = r2;
            goto L_0x001b;
        L_0x0099:
            r6 = "location";
            r6 = r5.containsKey(r6);	 Catch:{ all -> 0x00b1 }
            if (r6 == 0) goto L_0x007a;
        L_0x00a1:
            r1 = "location";
            r1 = r5.get(r1);	 Catch:{ all -> 0x00b1 }
            r1 = (java.util.List) r1;	 Catch:{ all -> 0x00b1 }
            goto L_0x007a;
        L_0x00aa:
            r0.disconnect();	 Catch:{ IndexOutOfBoundsException -> 0x0111, IOException -> 0x010c, RuntimeException -> 0x0107 }
            r0 = r4;
            r2 = r1;
            goto L_0x0003;
        L_0x00b1:
            r1 = move-exception;
            r0.disconnect();	 Catch:{ IndexOutOfBoundsException -> 0x00b6, IOException -> 0x00d1, RuntimeException -> 0x00ec }
            throw r1;	 Catch:{ IndexOutOfBoundsException -> 0x00b6, IOException -> 0x00d1, RuntimeException -> 0x00ec }
        L_0x00b6:
            r0 = move-exception;
            r1 = r0;
            r0 = r2;
        L_0x00b9:
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "Error while parsing ping URL: ";
            r2 = r2.append(r3);
            r2 = r2.append(r0);
            r2 = r2.toString();
            com.google.android.gms.ads.internal.util.client.zzb.zzd(r2, r1);
            goto L_0x001b;
        L_0x00d1:
            r0 = move-exception;
            r1 = r0;
            r0 = r2;
        L_0x00d4:
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "Error while pinging URL: ";
            r2 = r2.append(r3);
            r2 = r2.append(r0);
            r2 = r2.toString();
            com.google.android.gms.ads.internal.util.client.zzb.zzd(r2, r1);
            goto L_0x001b;
        L_0x00ec:
            r0 = move-exception;
            r1 = r0;
            r0 = r2;
        L_0x00ef:
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "Error while pinging URL: ";
            r2 = r2.append(r3);
            r2 = r2.append(r0);
            r2 = r2.toString();
            com.google.android.gms.ads.internal.util.client.zzb.zzd(r2, r1);
            goto L_0x001b;
        L_0x0107:
            r0 = move-exception;
            r7 = r0;
            r0 = r1;
            r1 = r7;
            goto L_0x00ef;
        L_0x010c:
            r0 = move-exception;
            r7 = r0;
            r0 = r1;
            r1 = r7;
            goto L_0x00d4;
        L_0x0111:
            r0 = move-exception;
            r7 = r0;
            r0 = r1;
            r1 = r7;
            goto L_0x00b9;
        L_0x0116:
            r1 = r3;
            goto L_0x0089;
        L_0x0119:
            r0 = r2;
            goto L_0x001b;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzdm$zza.zzbr():void");
        }
    }

    public static class zzb {
        private final zzjp zzpD;

        public zzb(zzjp zzjp) {
            this.zzpD = zzjp;
        }

        public Intent zza(Context context, Map<String, String> map) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            String str = (String) map.get("u");
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            if (this.zzpD != null) {
                str = zzr.zzbC().zza(this.zzpD, str);
            }
            Uri parse = Uri.parse(str);
            boolean parseBoolean = Boolean.parseBoolean((String) map.get("use_first_package"));
            boolean parseBoolean2 = Boolean.parseBoolean((String) map.get("use_running_process"));
            Uri build = "http".equalsIgnoreCase(parse.getScheme()) ? parse.buildUpon().scheme("https").build() : "https".equalsIgnoreCase(parse.getScheme()) ? parse.buildUpon().scheme("http").build() : null;
            ArrayList arrayList = new ArrayList();
            Intent zzd = zzd(parse);
            Intent zzd2 = zzd(build);
            ResolveInfo zza = zza(context, zzd, arrayList);
            if (zza != null) {
                return zza(zzd, zza);
            }
            if (zzd2 != null) {
                ResolveInfo zza2 = zza(context, zzd2);
                if (zza2 != null) {
                    Intent zza3 = zza(zzd, zza2);
                    if (zza(context, zza3) != null) {
                        return zza3;
                    }
                }
            }
            if (arrayList.size() == 0) {
                return zzd;
            }
            if (parseBoolean2 && activityManager != null) {
                List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
                if (runningAppProcesses != null) {
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        ResolveInfo resolveInfo = (ResolveInfo) it.next();
                        for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                            if (runningAppProcessInfo.processName.equals(resolveInfo.activityInfo.packageName)) {
                                return zza(zzd, resolveInfo);
                            }
                        }
                    }
                }
            }
            return parseBoolean ? zza(zzd, (ResolveInfo) arrayList.get(0)) : zzd;
        }

        public Intent zza(Intent intent, ResolveInfo resolveInfo) {
            Intent intent2 = new Intent(intent);
            intent2.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
            return intent2;
        }

        public ResolveInfo zza(Context context, Intent intent) {
            return zza(context, intent, new ArrayList());
        }

        public ResolveInfo zza(Context context, Intent intent, ArrayList<ResolveInfo> arrayList) {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return null;
            }
            ResolveInfo resolveInfo;
            List queryIntentActivities = packageManager.queryIntentActivities(intent, 65536);
            ResolveInfo resolveActivity = packageManager.resolveActivity(intent, 65536);
            if (queryIntentActivities != null && resolveActivity != null) {
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 >= queryIntentActivities.size()) {
                        break;
                    }
                    resolveInfo = (ResolveInfo) queryIntentActivities.get(i2);
                    if (resolveActivity != null && resolveActivity.activityInfo.name.equals(resolveInfo.activityInfo.name)) {
                        resolveInfo = resolveActivity;
                        break;
                    }
                    i = i2 + 1;
                }
            }
            resolveInfo = null;
            arrayList.addAll(queryIntentActivities);
            return resolveInfo;
        }

        public Intent zzd(Uri uri) {
            if (uri == null) {
                return null;
            }
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(268435456);
            intent.setData(uri);
            intent.setAction("android.intent.action.VIEW");
            return intent;
        }
    }

    public zzdm(zzdh zzdh, zze zze, zzfn zzfn) {
        this.zzzD = zzdh;
        this.zzzA = zze;
        this.zzzB = zzfn;
    }

    private static boolean zzc(Map<String, String> map) {
        return "1".equals(map.get("custom_close"));
    }

    private static int zzd(Map<String, String> map) {
        String str = (String) map.get("o");
        if (str != null) {
            if ("p".equalsIgnoreCase(str)) {
                return zzr.zzbE().zzhw();
            }
            if ("l".equalsIgnoreCase(str)) {
                return zzr.zzbE().zzhv();
            }
            if ("c".equalsIgnoreCase(str)) {
                return zzr.zzbE().zzhx();
            }
        }
        return -1;
    }

    private static void zze(zzjp zzjp, Map<String, String> map) {
        String str = (String) map.get("u");
        if (TextUtils.isEmpty(str)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Destination url cannot be empty.");
        } else {
            new zza(zzjp, str).zzgd();
        }
    }

    private static void zzf(zzjp zzjp, Map<String, String> map) {
        Context context = zzjp.getContext();
        if (TextUtils.isEmpty((String) map.get("u"))) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Destination url cannot be empty.");
            return;
        }
        try {
            zzjp.zzhU().zza(new AdLauncherIntentInfoParcel(new zzb(zzjp).zza(context, (Map) map)));
        } catch (ActivityNotFoundException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK(e.getMessage());
        }
    }

    private void zzo(boolean z) {
        if (this.zzzB != null) {
            this.zzzB.zzp(z);
        }
    }

    public void zza(zzjp zzjp, Map<String, String> map) {
        String str = (String) map.get("a");
        if (str == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Action missing from an open GMSG.");
        } else if (this.zzzA == null || this.zzzA.zzbh()) {
            zzjq zzhU = zzjp.zzhU();
            if ("expand".equalsIgnoreCase(str)) {
                if (zzjp.zzhY()) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaK("Cannot expand WebView that is already expanded.");
                    return;
                }
                zzo(false);
                zzhU.zza(zzc(map), zzd(map));
            } else if ("webapp".equalsIgnoreCase(str)) {
                str = (String) map.get("u");
                zzo(false);
                if (str != null) {
                    zzhU.zza(zzc(map), zzd(map), str);
                } else {
                    zzhU.zza(zzc(map), zzd(map), (String) map.get("html"), (String) map.get("baseurl"));
                }
            } else if ("in_app_purchase".equalsIgnoreCase(str)) {
                str = (String) map.get("product_id");
                String str2 = (String) map.get("report_urls");
                if (this.zzzD == null) {
                    return;
                }
                if (str2 == null || str2.isEmpty()) {
                    this.zzzD.zza(str, new ArrayList());
                } else {
                    this.zzzD.zza(str, new ArrayList(Arrays.asList(str2.split(" "))));
                }
            } else if ("app".equalsIgnoreCase(str) && "true".equalsIgnoreCase((String) map.get("play_store"))) {
                zze(zzjp, map);
            } else if ("app".equalsIgnoreCase(str) && "true".equalsIgnoreCase((String) map.get("system_browser"))) {
                zzo(true);
                zzf(zzjp, map);
            } else {
                zzo(true);
                str = (String) map.get("u");
                zzhU.zza(new AdLauncherIntentInfoParcel((String) map.get("i"), !TextUtils.isEmpty(str) ? zzr.zzbC().zza(zzjp, str) : str, (String) map.get("m"), (String) map.get("p"), (String) map.get("c"), (String) map.get("f"), (String) map.get("e")));
            }
        } else {
            this.zzzA.zzq((String) map.get("u"));
        }
    }
}
