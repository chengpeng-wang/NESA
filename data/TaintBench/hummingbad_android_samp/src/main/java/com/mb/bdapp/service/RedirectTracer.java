package com.mb.bdapp.service;

import android.os.Build;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RedirectTracer {
    private static String DEFAULT_USER_AGENT_PHONE = null;
    private static final int MAX_STACK_DEEP = 25;
    private static String[] OS_VERSION = new String[]{"4.2.2", "4.4.2", "4.4.3", "5.0.1", "5.1.1"};
    private static final String TAG = RedirectTracer.class.getSimpleName();
    private static AtomicInteger mStackDeep = new AtomicInteger(25);
    private AtomicInteger mTraceTimes = new AtomicInteger(1);

    public interface TracerCallback {
        void callback(boolean z, String str);
    }

    static {
        DEFAULT_USER_AGENT_PHONE = "Mozilla/5.0 (Linux; Android %s; %s Build/LRX22C) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36";
        DEFAULT_USER_AGENT_PHONE = String.format(DEFAULT_USER_AGENT_PHONE, new Object[]{randomOsVersion(), Build.MODEL});
    }

    public static String randomOsVersion() {
        return OS_VERSION[new Random().nextInt(OS_VERSION.length)];
    }

    public int getAndDecreamentTraceTimes() {
        int traceTimes = this.mTraceTimes.getAndDecrement();
        mStackDeep.set(25);
        return traceTimes;
    }

    public int getTraceTimes() {
        return this.mTraceTimes.get();
    }

    /* JADX WARNING: Removed duplicated region for block: B:53:0x00a6  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00b4  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00c1 A:{PHI: r2 , ExcHandler: MalformedURLException (e java.net.MalformedURLException), Splitter:B:5:0x0010} */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00be A:{PHI: r2 , ExcHandler: IOException (e java.io.IOException), Splitter:B:5:0x0010} */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00b8 A:{PHI: r2 , ExcHandler: all (th java.lang.Throwable), Splitter:B:5:0x0010} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:61:0x00b8, code skipped:
            r10 = th;
     */
    /* JADX WARNING: Missing block: B:62:0x00b9, code skipped:
            r8 = r9;
     */
    /* JADX WARNING: Missing block: B:64:0x00bc, code skipped:
            r8 = r9;
     */
    /* JADX WARNING: Missing block: B:66:0x00bf, code skipped:
            r8 = r9;
     */
    /* JADX WARNING: Missing block: B:68:0x00c2, code skipped:
            r8 = r9;
     */
    public static java.lang.String recursiveTracePath(java.lang.String r12, java.lang.String r13) {
        /*
        r6 = 0;
        r10 = mStackDeep;
        r10 = r10.getAndDecrement();
        if (r10 <= 0) goto L_0x0047;
    L_0x0009:
        r8 = 0;
        r2 = 0;
        r9 = new java.net.URL;	 Catch:{ MalformedURLException -> 0x009c, IOException -> 0x00a3, Exception -> 0x00aa, all -> 0x00b1 }
        r9.<init>(r12);	 Catch:{ MalformedURLException -> 0x009c, IOException -> 0x00a3, Exception -> 0x00aa, all -> 0x00b1 }
        r10 = r9.openConnection();	 Catch:{ MalformedURLException -> 0x00c1, IOException -> 0x00be, Exception -> 0x00bb, all -> 0x00b8 }
        r0 = r10;
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ MalformedURLException -> 0x00c1, IOException -> 0x00be, Exception -> 0x00bb, all -> 0x00b8 }
        r2 = r0;
        r10 = "User-Agent";
        r11 = DEFAULT_USER_AGENT_PHONE;	 Catch:{ MalformedURLException -> 0x00c1, IOException -> 0x00be, Exception -> 0x00bb, all -> 0x00b8 }
        r2.setRequestProperty(r10, r11);	 Catch:{ MalformedURLException -> 0x00c1, IOException -> 0x00be, Exception -> 0x00bb, all -> 0x00b8 }
        r10 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r2.setConnectTimeout(r10);	 Catch:{ MalformedURLException -> 0x00c1, IOException -> 0x00be, Exception -> 0x00bb, all -> 0x00b8 }
        r10 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r2.setReadTimeout(r10);	 Catch:{ MalformedURLException -> 0x00c1, IOException -> 0x00be, Exception -> 0x00bb, all -> 0x00b8 }
        r10 = 0;
        r2.setInstanceFollowRedirects(r10);	 Catch:{ MalformedURLException -> 0x00c1, IOException -> 0x00be, Exception -> 0x00bb, all -> 0x00b8 }
        r1 = r2.getResponseCode();	 Catch:{ MalformedURLException -> 0x00c1, IOException -> 0x00be, Exception -> 0x00bb, all -> 0x00b8 }
        r10 = needRedirect(r1);	 Catch:{ MalformedURLException -> 0x00c1, IOException -> 0x00be, Exception -> 0x00bb, all -> 0x00b8 }
        if (r10 == 0) goto L_0x0053;
    L_0x0037:
        r5 = getLocation(r2);	 Catch:{ MalformedURLException -> 0x00c1, IOException -> 0x00be, Exception -> 0x00bb, all -> 0x00b8 }
        r10 = isGoogleStore(r5);	 Catch:{ MalformedURLException -> 0x00c1, IOException -> 0x00be, Exception -> 0x00bb, all -> 0x00b8 }
        if (r10 == 0) goto L_0x0049;
    L_0x0041:
        r6 = r5;
    L_0x0042:
        if (r2 == 0) goto L_0x0047;
    L_0x0044:
        r2.disconnect();
    L_0x0047:
        r3 = r6;
    L_0x0048:
        return r3;
    L_0x0049:
        r3 = recursiveTracePath(r5, r12);	 Catch:{ MalformedURLException -> 0x00c1, IOException -> 0x00be, Exception -> 0x00bb, all -> 0x00b8 }
        if (r2 == 0) goto L_0x0048;
    L_0x004f:
        r2.disconnect();
        goto L_0x0048;
    L_0x0053:
        r7 = r2.getInputStream();	 Catch:{ MalformedURLException -> 0x00c1, IOException -> 0x00be, Exception -> 0x00bb, all -> 0x00b8 }
        r4 = "";
        if (r7 == 0) goto L_0x005f;
    L_0x005b:
        r4 = readStringFromStream(r7);	 Catch:{ MalformedURLException -> 0x00c1, IOException -> 0x00be, Exception -> 0x00bb, all -> 0x00b8 }
    L_0x005f:
        r10 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r1 != r10) goto L_0x009a;
    L_0x0063:
        r10 = android.text.TextUtils.isEmpty(r4);	 Catch:{ MalformedURLException -> 0x00c1, IOException -> 0x00be, Exception -> 0x00bb, all -> 0x00b8 }
        if (r10 != 0) goto L_0x009a;
    L_0x0069:
        r3 = httpEquivFilter(r4);	 Catch:{ Exception -> 0x0099, MalformedURLException -> 0x00c1, IOException -> 0x00be, all -> 0x00b8 }
        if (r3 == 0) goto L_0x0075;
    L_0x006f:
        if (r2 == 0) goto L_0x0048;
    L_0x0071:
        r2.disconnect();
        goto L_0x0048;
    L_0x0075:
        r3 = documentLocationFilter(r4);	 Catch:{ Exception -> 0x0099, MalformedURLException -> 0x00c1, IOException -> 0x00be, all -> 0x00b8 }
        if (r3 == 0) goto L_0x0081;
    L_0x007b:
        if (r2 == 0) goto L_0x0048;
    L_0x007d:
        r2.disconnect();
        goto L_0x0048;
    L_0x0081:
        r3 = windowLocationFillter(r4);	 Catch:{ Exception -> 0x0099, MalformedURLException -> 0x00c1, IOException -> 0x00be, all -> 0x00b8 }
        if (r3 == 0) goto L_0x008d;
    L_0x0087:
        if (r2 == 0) goto L_0x0048;
    L_0x0089:
        r2.disconnect();
        goto L_0x0048;
    L_0x008d:
        r3 = windowSelfLocationFillter(r4);	 Catch:{ Exception -> 0x0099, MalformedURLException -> 0x00c1, IOException -> 0x00be, all -> 0x00b8 }
        if (r3 == 0) goto L_0x009a;
    L_0x0093:
        if (r2 == 0) goto L_0x0048;
    L_0x0095:
        r2.disconnect();
        goto L_0x0048;
    L_0x0099:
        r10 = move-exception;
    L_0x009a:
        r6 = r12;
        goto L_0x0042;
    L_0x009c:
        r10 = move-exception;
    L_0x009d:
        if (r2 == 0) goto L_0x0047;
    L_0x009f:
        r2.disconnect();
        goto L_0x0047;
    L_0x00a3:
        r10 = move-exception;
    L_0x00a4:
        if (r2 == 0) goto L_0x0047;
    L_0x00a6:
        r2.disconnect();
        goto L_0x0047;
    L_0x00aa:
        r10 = move-exception;
    L_0x00ab:
        if (r2 == 0) goto L_0x0047;
    L_0x00ad:
        r2.disconnect();
        goto L_0x0047;
    L_0x00b1:
        r10 = move-exception;
    L_0x00b2:
        if (r2 == 0) goto L_0x00b7;
    L_0x00b4:
        r2.disconnect();
    L_0x00b7:
        throw r10;
    L_0x00b8:
        r10 = move-exception;
        r8 = r9;
        goto L_0x00b2;
    L_0x00bb:
        r10 = move-exception;
        r8 = r9;
        goto L_0x00ab;
    L_0x00be:
        r10 = move-exception;
        r8 = r9;
        goto L_0x00a4;
    L_0x00c1:
        r10 = move-exception;
        r8 = r9;
        goto L_0x009d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mb.bdapp.service.RedirectTracer.recursiveTracePath(java.lang.String, java.lang.String):java.lang.String");
    }

    public static String getLocation(HttpURLConnection conn) {
        String location = conn.getHeaderField("location");
        if (TextUtils.isEmpty(location)) {
            return conn.getHeaderField("Location");
        }
        return location;
    }

    public static String readStringFromStream(InputStream stream) {
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    sb.append(line);
                }
                try {
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (OutOfMemoryError e2) {
            try {
                Runtime.getRuntime().gc();
                System.runFinalization();
            } catch (IOException e3) {
                try {
                    stream.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            } catch (Exception e5) {
                try {
                    stream.close();
                } catch (IOException e42) {
                    e42.printStackTrace();
                }
            } catch (Throwable th) {
                try {
                    stream.close();
                } catch (IOException e422) {
                    e422.printStackTrace();
                }
            }
        }
        stream.close();
        return sb.toString();
    }

    private static String httpEquivFilter(String document) {
        Matcher matcher = Pattern.compile("(?i)(http-equiv\\W+refresh.+url\\W+(.+?)[\"|'])").matcher(document);
        if (matcher.find()) {
            String refreshUrl = matcher.group(2);
            String url = recursiveTracePath(refreshUrl, refreshUrl);
            if (isGoogleStore(url)) {
                String reffer = url;
                return url;
            }
        }
        return null;
    }

    private static String documentLocationFilter(String document) {
        Matcher matcher = Pattern.compile("(?i)document.location.replace.*((http|market|https):\\/\\/[^'|\"]*)").matcher(document);
        if (!matcher.find()) {
            return null;
        }
        String onloadUrl = matcher.group(1);
        if (matcher.group(2).equals("market")) {
            return onloadUrl;
        }
        return recursiveTracePath(onloadUrl, onloadUrl);
    }

    private static String windowSelfLocationFillter(String document) {
        Matcher matcher = Pattern.compile("(?i)window.self.location.*((http|https|market):\\/\\/[^'|\"]*)").matcher(document);
        if (!matcher.find()) {
            return null;
        }
        String jsLocation = matcher.group(1);
        if (matcher.group(2).equals("market")) {
            return jsLocation;
        }
        return recursiveTracePath(jsLocation, jsLocation);
    }

    private static String windowLocationFillter(String document) {
        Matcher matcher = Pattern.compile("(?i)window.location.*((http|https|market):\\/\\/[^'|\"]*)").matcher(document);
        if (!matcher.find()) {
            return null;
        }
        String jsLocation = matcher.group(1);
        if (matcher.group(2).equals("market")) {
            return jsLocation;
        }
        return recursiveTracePath(jsLocation, jsLocation);
    }

    private static boolean needRedirect(int code) {
        return code == 301 || code == 302 || code == 303 || code == 307;
    }

    public static boolean isGoogleStore(String url) {
        return !TextUtils.isEmpty(url) && (url.contains("https://play.google.com/store/apps/details?") || url.startsWith("market://details?") || url.contains("http://play.google.com/store/apps/details?"));
    }

    public static boolean containReferrer(String url) {
        url = URLDecoder.decode(url);
        return !TextUtils.isEmpty(url) && url.contains("referrer=");
    }
}
