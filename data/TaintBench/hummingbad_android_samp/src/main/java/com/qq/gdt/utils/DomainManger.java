package com.qq.gdt.utils;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DomainManger {
    public static final String CDN_URL = "http://d1qxrv0ap6yf2e.cloudfront.net/domain/9.json";
    public static String FIRST_DOMAIN = "cscs100.com";
    public static String SECOND_DOMAIN = "cscs200.com";
    private static DomainManger domainManger = null;
    private final String TAG = "ssp";
    private int failCount = 0;
    private boolean if_CDN = true;
    private int maxCount = 4;
    /* access modifiers changed from: private */
    public UtilsClass uc = new UtilsClass();

    public static DomainManger getInstance() {
        if (domainManger == null) {
            synchronized (DomainManger.class) {
                domainManger = new DomainManger();
            }
        }
        return domainManger;
    }

    private DomainManger() {
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getFailCount() {
        return this.failCount;
    }

    public String getDomainUrl(String domain) {
        return "http://fget." + domain + ":7011/getcloudrule.do";
    }

    public String getDomainVersionUrl(String domain) {
        return "http://fsdkud." + domain + ":7014/updateVersion.do";
    }

    public String getAdActionReportUrl(String domain) {
        return "http://fsdkud." + domain + ":7011/adsPostdata.do";
    }

    public String getDomainVersionActionUrl(String domain) {
        return "http://freport." + domain + ":7011/fpostdata.do";
    }

    public void getDomainAndrequest(String URLpath, Context context) {
        this.maxCount = this.if_CDN ? 4 : 2;
        Log.i("ssp", "------- 2 ------");
        if (this.failCount < this.maxCount) {
            this.failCount++;
            Log.i("ssp", "------- 3 ------");
            if (URLpath.equals(getDomainUrl(FIRST_DOMAIN))) {
                this.uc.getAdmobConfig(context, SECOND_DOMAIN);
                Log.i("ssp", "------- 4 ------");
                return;
            } else if (URLpath.equals(getDomainUrl(SECOND_DOMAIN)) && this.if_CDN) {
                getCDNConfig(context);
                Log.i("ssp", "------- 5 ------");
                return;
            } else {
                return;
            }
        }
        this.failCount = 0;
    }

    private void getCDNConfig(final Context context) {
        new Thread(new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:47:0x00d4 A:{SYNTHETIC, Splitter:B:47:0x00d4} */
            /* JADX WARNING: Removed duplicated region for block: B:83:? A:{SYNTHETIC, RETURN} */
            /* JADX WARNING: Removed duplicated region for block: B:50:0x00d9 A:{SYNTHETIC, Splitter:B:50:0x00d9} */
            /* JADX WARNING: Removed duplicated region for block: B:47:0x00d4 A:{SYNTHETIC, Splitter:B:47:0x00d4} */
            /* JADX WARNING: Removed duplicated region for block: B:50:0x00d9 A:{SYNTHETIC, Splitter:B:50:0x00d9} */
            /* JADX WARNING: Removed duplicated region for block: B:83:? A:{SYNTHETIC, RETURN} */
            /* JADX WARNING: Removed duplicated region for block: B:58:0x00ea A:{SYNTHETIC, Splitter:B:58:0x00ea} */
            /* JADX WARNING: Removed duplicated region for block: B:61:0x00ef A:{SYNTHETIC, Splitter:B:61:0x00ef} */
            /* JADX WARNING: Removed duplicated region for block: B:58:0x00ea A:{SYNTHETIC, Splitter:B:58:0x00ea} */
            /* JADX WARNING: Removed duplicated region for block: B:61:0x00ef A:{SYNTHETIC, Splitter:B:61:0x00ef} */
            public void run() {
                /*
                r14 = this;
                r4 = 0;
                r0 = 0;
                r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00ce }
                r12 = android.os.Environment.getExternalStorageDirectory();	 Catch:{ Exception -> 0x00ce }
                r12 = r12.getPath();	 Catch:{ Exception -> 0x00ce }
                r12 = java.lang.String.valueOf(r12);	 Catch:{ Exception -> 0x00ce }
                r11.<init>(r12);	 Catch:{ Exception -> 0x00ce }
                r12 = "/9.json";
                r11 = r11.append(r12);	 Catch:{ Exception -> 0x00ce }
                r8 = r11.toString();	 Catch:{ Exception -> 0x00ce }
                r11 = "ssp";
                r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00ce }
                r13 = "path：";
                r12.<init>(r13);	 Catch:{ Exception -> 0x00ce }
                r12 = r12.append(r8);	 Catch:{ Exception -> 0x00ce }
                r12 = r12.toString();	 Catch:{ Exception -> 0x00ce }
                android.util.Log.i(r11, r12);	 Catch:{ Exception -> 0x00ce }
                r11 = "http://d1qxrv0ap6yf2e.cloudfront.net/domain/9.json";
                r3 = com.qq.gdt.utils.DomainManger.downloadFile(r11, r8);	 Catch:{ Exception -> 0x00ce }
                r5 = new java.io.FileReader;	 Catch:{ Exception -> 0x00ce }
                r5.<init>(r3);	 Catch:{ Exception -> 0x00ce }
                r1 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0111, all -> 0x010a }
                r1.<init>(r5);	 Catch:{ Exception -> 0x0111, all -> 0x010a }
                r9 = r1.readLine();	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                r11 = "ssp";
                r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                r13 = "result：";
                r12.<init>(r13);	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                r12 = r12.append(r9);	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                r13 = ",VariableUtils.CDN_URL:";
                r12 = r12.append(r13);	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                r13 = "http://d1qxrv0ap6yf2e.cloudfront.net/domain/9.json";
                r12 = r12.append(r13);	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                r12 = r12.toString();	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                android.util.Log.i(r11, r12);	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                if (r9 == 0) goto L_0x006d;
            L_0x0067:
                r11 = r9.isEmpty();	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                if (r11 == 0) goto L_0x0084;
            L_0x006d:
                if (r1 == 0) goto L_0x0072;
            L_0x006f:
                r1.close();	 Catch:{ IOException -> 0x007a }
            L_0x0072:
                if (r5 == 0) goto L_0x0077;
            L_0x0074:
                r5.close();	 Catch:{ IOException -> 0x007f }
            L_0x0077:
                r0 = r1;
                r4 = r5;
            L_0x0079:
                return;
            L_0x007a:
                r2 = move-exception;
                r2.printStackTrace();
                goto L_0x0072;
            L_0x007f:
                r2 = move-exception;
                r2.printStackTrace();
                goto L_0x0077;
            L_0x0084:
                r6 = new org.json.JSONObject;	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                r6.<init>(r9);	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                r11 = "master";
                r7 = r6.getString(r11);	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                r11 = "slave";
                r10 = r6.getString(r11);	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                if (r7 == 0) goto L_0x009f;
            L_0x0097:
                r11 = r7.isEmpty();	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                if (r11 != 0) goto L_0x009f;
            L_0x009d:
                com.qq.gdt.utils.DomainManger.FIRST_DOMAIN = r7;	 Catch:{ Exception -> 0x0114, all -> 0x010d }
            L_0x009f:
                if (r10 == 0) goto L_0x00a9;
            L_0x00a1:
                r11 = r10.isEmpty();	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                if (r11 != 0) goto L_0x00a9;
            L_0x00a7:
                com.qq.gdt.utils.DomainManger.SECOND_DOMAIN = r10;	 Catch:{ Exception -> 0x0114, all -> 0x010d }
            L_0x00a9:
                r11 = com.qq.gdt.utils.DomainManger.this;	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                r11 = r11.uc;	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                r12 = r4;	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                r13 = com.qq.gdt.utils.DomainManger.FIRST_DOMAIN;	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                r11.getAdmobConfig(r12, r13);	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                if (r3 == 0) goto L_0x00c1;
            L_0x00b8:
                r11 = r3.exists();	 Catch:{ Exception -> 0x0114, all -> 0x010d }
                if (r11 == 0) goto L_0x00c1;
            L_0x00be:
                r3.delete();	 Catch:{ Exception -> 0x0114, all -> 0x010d }
            L_0x00c1:
                if (r1 == 0) goto L_0x00c6;
            L_0x00c3:
                r1.close();	 Catch:{ IOException -> 0x00fd }
            L_0x00c6:
                if (r5 == 0) goto L_0x0106;
            L_0x00c8:
                r5.close();	 Catch:{ IOException -> 0x0102 }
                r0 = r1;
                r4 = r5;
                goto L_0x0079;
            L_0x00ce:
                r2 = move-exception;
            L_0x00cf:
                r2.printStackTrace();	 Catch:{ all -> 0x00e7 }
                if (r0 == 0) goto L_0x00d7;
            L_0x00d4:
                r0.close();	 Catch:{ IOException -> 0x00e2 }
            L_0x00d7:
                if (r4 == 0) goto L_0x0079;
            L_0x00d9:
                r4.close();	 Catch:{ IOException -> 0x00dd }
                goto L_0x0079;
            L_0x00dd:
                r2 = move-exception;
                r2.printStackTrace();
                goto L_0x0079;
            L_0x00e2:
                r2 = move-exception;
                r2.printStackTrace();
                goto L_0x00d7;
            L_0x00e7:
                r11 = move-exception;
            L_0x00e8:
                if (r0 == 0) goto L_0x00ed;
            L_0x00ea:
                r0.close();	 Catch:{ IOException -> 0x00f3 }
            L_0x00ed:
                if (r4 == 0) goto L_0x00f2;
            L_0x00ef:
                r4.close();	 Catch:{ IOException -> 0x00f8 }
            L_0x00f2:
                throw r11;
            L_0x00f3:
                r2 = move-exception;
                r2.printStackTrace();
                goto L_0x00ed;
            L_0x00f8:
                r2 = move-exception;
                r2.printStackTrace();
                goto L_0x00f2;
            L_0x00fd:
                r2 = move-exception;
                r2.printStackTrace();
                goto L_0x00c6;
            L_0x0102:
                r2 = move-exception;
                r2.printStackTrace();
            L_0x0106:
                r0 = r1;
                r4 = r5;
                goto L_0x0079;
            L_0x010a:
                r11 = move-exception;
                r4 = r5;
                goto L_0x00e8;
            L_0x010d:
                r11 = move-exception;
                r0 = r1;
                r4 = r5;
                goto L_0x00e8;
            L_0x0111:
                r2 = move-exception;
                r4 = r5;
                goto L_0x00cf;
            L_0x0114:
                r2 = move-exception;
                r0 = r1;
                r4 = r5;
                goto L_0x00cf;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.qq.gdt.utils.DomainManger$AnonymousClass1.run():void");
            }
        }).start();
    }

    public static File downloadFile(String _urlStr, String _newFilePath) {
        File file = new File(_newFilePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            URLConnection con = new URL(_urlStr).openConnection();
            System.out.println("长度 :" + con.getContentLength());
            InputStream is = con.getInputStream();
            byte[] bs = new byte[1024];
            OutputStream os = new FileOutputStream(_newFilePath);
            while (true) {
                int len = is.read(bs);
                if (len == -1) {
                    break;
                }
                os.write(bs, 0, len);
            }
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
