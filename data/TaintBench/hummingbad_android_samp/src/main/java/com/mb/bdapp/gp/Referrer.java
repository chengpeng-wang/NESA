package com.mb.bdapp.gp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.mb.bdapp.db.DuAd;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Referrer {
    public static final String DEFAULT_USER_AGENT_PC = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:38.0) Gecko/20100101 Firefox/38.0";
    private static String DEFAULT_USER_AGENT_PHONE = null;
    static String[] OS_VERSION = new String[]{"4.2.2", "4.4.2", "4.4.3", "5.0.1", "5.1.1"};
    private static final String TAG = "Referrer";
    private Context mContext;
    private AtomicInteger mStackDeep = new AtomicInteger(25);
    /* access modifiers changed from: private */
    public String murl;

    static {
        DEFAULT_USER_AGENT_PHONE = "Mozilla/5.0 (Linux; Android %s; %s Build/LRX22C) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36";
        DEFAULT_USER_AGENT_PHONE = String.format(DEFAULT_USER_AGENT_PHONE, new Object[]{randomOsVersion(), Build.MODEL});
    }

    public static String randomOsVersion() {
        return OS_VERSION[new Random().nextInt(OS_VERSION.length)];
    }

    public Referrer(Context context, String url) {
        this.mContext = context;
        this.murl = url;
    }

    public static boolean isGoogleStore(String url) {
        boolean isGooglePlayUrl = !TextUtils.isEmpty(url) && (url.contains("https://play.google.com/store/apps/details?") || url.startsWith("market://details?") || url.contains("http://play.google.com/store/apps/details?"));
        Log.e("HDJ", "广告链接  isGoogleStore 【" + isGooglePlayUrl + "】：" + url);
        return isGooglePlayUrl;
    }

    private boolean needRedirect(int code) {
        return code == 301 || code == 302 || code == 303 || code == 307;
    }

    public String getReferrerBySourceUrl() {
        return getReferrerBySourceUrl(this.murl);
    }

    public void vistSourceUrl() {
        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                Log.d("hyp", "Referer:access source url");
                for (int i = 0; i < 2; i++) {
                    Referrer.this.getReferrerBySourceUrl(Referrer.this.murl);
                }
            }
        }).start();
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:64:0x01a4=Splitter:B:64:0x01a4, B:69:0x01c4=Splitter:B:69:0x01c4} */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x01e6  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x01f3 A:{PHI: r2 , ExcHandler: MalformedURLException (e java.net.MalformedURLException), Splitter:B:8:0x0054} */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x01f0 A:{PHI: r2 , ExcHandler: IOException (e java.io.IOException), Splitter:B:8:0x0054} */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x01ea A:{PHI: r2 , ExcHandler: all (th java.lang.Throwable), Splitter:B:8:0x0054} */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x019e  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x01be  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x01de  */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:62:0x019e, code skipped:
            r2.disconnect();
     */
    /* JADX WARNING: Missing block: B:67:0x01be, code skipped:
            r2.disconnect();
     */
    /* JADX WARNING: Missing block: B:72:0x01de, code skipped:
            r2.disconnect();
     */
    /* JADX WARNING: Missing block: B:77:0x01ea, code skipped:
            r14 = th;
     */
    /* JADX WARNING: Missing block: B:78:0x01eb, code skipped:
            r12 = r13;
     */
    /* JADX WARNING: Missing block: B:79:0x01ed, code skipped:
            r5 = e;
     */
    /* JADX WARNING: Missing block: B:80:0x01ee, code skipped:
            r12 = r13;
     */
    /* JADX WARNING: Missing block: B:81:0x01f0, code skipped:
            r5 = e;
     */
    /* JADX WARNING: Missing block: B:82:0x01f1, code skipped:
            r12 = r13;
     */
    /* JADX WARNING: Missing block: B:84:0x01f4, code skipped:
            r12 = r13;
     */
    public java.lang.String getReferrerBySourceUrl(java.lang.String r18) {
        /*
        r17 = this;
        r14 = "HDJ -- Tracking Url -- ";
        r0 = r18;
        android.util.Log.e(r14, r0);
        r14 = "android_id=";
        r0 = r18;
        r14 = r0.contains(r14);
        if (r14 != 0) goto L_0x0039;
    L_0x0011:
        r14 = new java.lang.StringBuilder;
        r15 = "&android_id=";
        r14.<init>(r15);
        r0 = r17;
        r15 = r0.mContext;
        r15 = com.mb.bdapp.util.AppInfoUtils.getAndroidId(r15);
        r14 = r14.append(r15);
        r8 = r14.toString();
        r14 = new java.lang.StringBuilder;
        r15 = java.lang.String.valueOf(r18);
        r14.<init>(r15);
        r14 = r14.append(r8);
        r18 = r14.toString();
    L_0x0039:
        r14 = "HDJ -- New Url -- ";
        r0 = r18;
        android.util.Log.e(r14, r0);
        r10 = 0;
        r0 = r17;
        r14 = r0.mStackDeep;
        r14 = r14.getAndDecrement();
        if (r14 <= 0) goto L_0x0097;
    L_0x004b:
        r12 = 0;
        r2 = 0;
        r13 = new java.net.URL;	 Catch:{ MalformedURLException -> 0x019b, IOException -> 0x01a3, Exception -> 0x01c3 }
        r0 = r18;
        r13.<init>(r0);	 Catch:{ MalformedURLException -> 0x019b, IOException -> 0x01a3, Exception -> 0x01c3 }
        r14 = r13.openConnection();	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r0 = r14;
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r2 = r0;
        r14 = "User-Agent";
        r15 = DEFAULT_USER_AGENT_PHONE;	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r2.setRequestProperty(r14, r15);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r14 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r2.setConnectTimeout(r14);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r14 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r2.setReadTimeout(r14);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r14 = 0;
        r2.setInstanceFollowRedirects(r14);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r1 = r2.getResponseCode();	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r0 = r17;
        r14 = r0.needRedirect(r1);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        if (r14 == 0) goto L_0x0117;
    L_0x007d:
        r14 = "Location";
        r6 = r2.getHeaderField(r14);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        if (r6 != 0) goto L_0x008b;
    L_0x0085:
        r14 = "location";
        r6 = r2.getHeaderField(r14);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
    L_0x008b:
        r14 = isGoogleStore(r6);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        if (r14 == 0) goto L_0x0099;
    L_0x0091:
        r10 = r6;
    L_0x0092:
        if (r2 == 0) goto L_0x0097;
    L_0x0094:
        r2.disconnect();
    L_0x0097:
        r3 = r10;
    L_0x0098:
        return r3;
    L_0x0099:
        r14 = "http://";
        r14 = r6.startsWith(r14);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        if (r14 != 0) goto L_0x00d9;
    L_0x00a1:
        r14 = "https://";
        r14 = r6.startsWith(r14);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        if (r14 != 0) goto L_0x00d9;
    L_0x00a9:
        r7 = new java.net.URL;	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r0 = r18;
        r7.<init>(r0);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r9 = r7.getPort();	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        if (r9 >= 0) goto L_0x00e5;
    L_0x00b6:
        r14 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r15 = r7.getProtocol();	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r15 = java.lang.String.valueOf(r15);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r14.<init>(r15);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r15 = "://";
        r14 = r14.append(r15);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r15 = r7.getHost();	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r14 = r14.append(r15);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r14 = r14.append(r6);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r6 = r14.toString();	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
    L_0x00d9:
        r0 = r17;
        r3 = r0.getReferrerBySourceUrl(r6);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        if (r2 == 0) goto L_0x0098;
    L_0x00e1:
        r2.disconnect();
        goto L_0x0098;
    L_0x00e5:
        r14 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r15 = r7.getProtocol();	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r15 = java.lang.String.valueOf(r15);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r14.<init>(r15);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r15 = "://";
        r14 = r14.append(r15);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r15 = r7.getHost();	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r14 = r14.append(r15);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r15 = ":";
        r14 = r14.append(r15);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r15 = java.lang.String.valueOf(r9);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r14 = r14.append(r15);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r14 = r14.append(r6);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r6 = r14.toString();	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        goto L_0x00d9;
    L_0x0117:
        r11 = r2.getInputStream();	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        r4 = "";
        if (r11 == 0) goto L_0x0123;
    L_0x011f:
        r4 = readStringFromStream(r11);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
    L_0x0123:
        r14 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r1 != r14) goto L_0x0197;
    L_0x0127:
        r14 = android.text.TextUtils.isEmpty(r4);	 Catch:{ MalformedURLException -> 0x01f3, IOException -> 0x01f0, Exception -> 0x01ed, all -> 0x01ea }
        if (r14 != 0) goto L_0x0197;
    L_0x012d:
        r0 = r17;
        r3 = r0.httpEquivFilter(r4);	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        if (r3 == 0) goto L_0x0150;
    L_0x0135:
        r14 = "Referrer";
        r15 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        r16 = "httpEquivFilter:";
        r15.<init>(r16);	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        r15 = r15.append(r3);	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        r15 = r15.toString();	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        com.mb.bdapp.util.LogUtil.w(r14, r15);	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        if (r2 == 0) goto L_0x0098;
    L_0x014b:
        r2.disconnect();
        goto L_0x0098;
    L_0x0150:
        r0 = r17;
        r3 = r0.documentLocationFilter(r4);	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        if (r3 == 0) goto L_0x0173;
    L_0x0158:
        r14 = "Referrer";
        r15 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        r16 = "documentLocationFilter:";
        r15.<init>(r16);	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        r15 = r15.append(r3);	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        r15 = r15.toString();	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        com.mb.bdapp.util.LogUtil.w(r14, r15);	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        if (r2 == 0) goto L_0x0098;
    L_0x016e:
        r2.disconnect();
        goto L_0x0098;
    L_0x0173:
        r0 = r17;
        r3 = r0.windowLocationFillter(r4);	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        if (r3 == 0) goto L_0x0197;
    L_0x017b:
        r14 = "Referrer";
        r15 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        r16 = "windowLocationFillter:";
        r15.<init>(r16);	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        r15 = r15.append(r3);	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        r15 = r15.toString();	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        com.mb.bdapp.util.LogUtil.w(r14, r15);	 Catch:{ Exception -> 0x0196, MalformedURLException -> 0x01f3, IOException -> 0x01f0, all -> 0x01ea }
        if (r2 == 0) goto L_0x0098;
    L_0x0191:
        r2.disconnect();
        goto L_0x0098;
    L_0x0196:
        r14 = move-exception;
    L_0x0197:
        r10 = r18;
        goto L_0x0092;
    L_0x019b:
        r14 = move-exception;
    L_0x019c:
        if (r2 == 0) goto L_0x0097;
    L_0x019e:
        r2.disconnect();
        goto L_0x0097;
    L_0x01a3:
        r5 = move-exception;
    L_0x01a4:
        r14 = "Referrer";
        r15 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01e3 }
        r16 = "getMessage";
        r15.<init>(r16);	 Catch:{ all -> 0x01e3 }
        r16 = r5.getMessage();	 Catch:{ all -> 0x01e3 }
        r15 = r15.append(r16);	 Catch:{ all -> 0x01e3 }
        r15 = r15.toString();	 Catch:{ all -> 0x01e3 }
        com.mb.bdapp.util.LogUtil.e(r14, r15);	 Catch:{ all -> 0x01e3 }
        if (r2 == 0) goto L_0x0097;
    L_0x01be:
        r2.disconnect();
        goto L_0x0097;
    L_0x01c3:
        r5 = move-exception;
    L_0x01c4:
        r14 = "Referrer";
        r15 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01e3 }
        r16 = "getMessage";
        r15.<init>(r16);	 Catch:{ all -> 0x01e3 }
        r16 = r5.getMessage();	 Catch:{ all -> 0x01e3 }
        r15 = r15.append(r16);	 Catch:{ all -> 0x01e3 }
        r15 = r15.toString();	 Catch:{ all -> 0x01e3 }
        com.mb.bdapp.util.LogUtil.e(r14, r15);	 Catch:{ all -> 0x01e3 }
        if (r2 == 0) goto L_0x0097;
    L_0x01de:
        r2.disconnect();
        goto L_0x0097;
    L_0x01e3:
        r14 = move-exception;
    L_0x01e4:
        if (r2 == 0) goto L_0x01e9;
    L_0x01e6:
        r2.disconnect();
    L_0x01e9:
        throw r14;
    L_0x01ea:
        r14 = move-exception;
        r12 = r13;
        goto L_0x01e4;
    L_0x01ed:
        r5 = move-exception;
        r12 = r13;
        goto L_0x01c4;
    L_0x01f0:
        r5 = move-exception;
        r12 = r13;
        goto L_0x01a4;
    L_0x01f3:
        r14 = move-exception;
        r12 = r13;
        goto L_0x019c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mb.bdapp.gp.Referrer.getReferrerBySourceUrl(java.lang.String):java.lang.String");
    }

    public static void sendReferrer(Context context, String referrer, String pkgName) {
        if (!TextUtils.isEmpty(referrer)) {
            Intent localIntent = new Intent("com.android.vending.INSTALL_REFERRER");
            localIntent.putExtra(DuAd.DB_REFERRER, referrer);
            localIntent.addFlags(32);
            localIntent.setPackage(pkgName);
            context.sendBroadcast(localIntent);
            Log.d("hyp", "send Referrer broadcast success!");
        }
    }

    private String httpEquivFilter(String document) {
        Matcher matcher = Pattern.compile("(?i)URL=.*((http|https|market):.*)'*").matcher(document);
        if (matcher.find()) {
            String url = getReferrerBySourceUrl(matcher.group(1));
            if (isGoogleStore(url)) {
                String reffer = url;
                return url;
            }
        }
        return null;
    }

    private String documentLocationFilter(String document) {
        Matcher matcher = Pattern.compile("(?i)document.location.replace.*((http|market|https):\\/\\/[^']*)").matcher(document);
        if (!matcher.find()) {
            return null;
        }
        String onloadUrl = matcher.group(1);
        if (matcher.group(2).equals("market")) {
            return onloadUrl;
        }
        return getReferrerBySourceUrl(onloadUrl);
    }

    private String windowLocationFillter(String document) {
        Matcher matcher = Pattern.compile("(?i)window.location.*((http|https|market):\\/\\/[^']*)").matcher(document);
        if (!matcher.find()) {
            return null;
        }
        String jsLocation = matcher.group(1);
        if (matcher.group(2).equals("market")) {
            return jsLocation;
        }
        return getReferrerBySourceUrl(jsLocation);
    }

    public static String readStringFromStream(InputStream stream) {
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    try {
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    sb.append(line);
                }
            }
            stream.close();
        } catch (IOException e2) {
            try {
                stream.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        } catch (Exception e4) {
            try {
                stream.close();
            } catch (IOException e32) {
                e32.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                stream.close();
            } catch (IOException e322) {
                e322.printStackTrace();
            }
            throw th;
        }
        return sb.toString();
    }
}
