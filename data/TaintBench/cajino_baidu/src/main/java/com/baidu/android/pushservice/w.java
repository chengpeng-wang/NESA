package com.baidu.android.pushservice;

public final class w {
    public static final String a = i;
    public static final String b = j;
    public static final int c = k;
    public static final String d = l;
    public static final String e = (a + "/rest/2.0/channel/channel");
    public static final String f = (a + "/rest/2.0/channel/");
    public static final String g = (d + "/searchbox?action=publicsrv&type=issuedcode");
    public static String h = "http://lbsonline.pushct.baidu.com/lbsupload";
    private static String i = "http://channel.api.duapp.com";
    private static String j = "agentchannel.api.duapp.com";
    private static int k = 5287;
    private static String l = "http://m.baidu.com";

    static {
        a();
    }

    /* JADX WARNING: Removed duplicated region for block: B:45:0x008e A:{SYNTHETIC, Splitter:B:45:0x008e} */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0076 A:{Catch:{ all -> 0x0097 }} */
    /* JADX WARNING: Removed duplicated region for block: B:56:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0081 A:{SYNTHETIC, Splitter:B:38:0x0081} */
    private static void a() {
        /*
        r0 = new java.io.File;
        r1 = android.os.Environment.getExternalStorageDirectory();
        r2 = "pushservice.cfg";
        r0.<init>(r1, r2);
        r1 = r0.exists();
        if (r1 == 0) goto L_0x0068;
    L_0x0011:
        r3 = new java.util.Properties;
        r3.<init>();
        r2 = 0;
        r1 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x006e, all -> 0x008a }
        r1.<init>(r0);	 Catch:{ Exception -> 0x006e, all -> 0x008a }
        r3.load(r1);	 Catch:{ Exception -> 0x0099 }
        r0 = "http_server";
        r0 = r3.getProperty(r0);	 Catch:{ Exception -> 0x0099 }
        if (r0 == 0) goto L_0x002f;
    L_0x0027:
        r2 = r0.length();	 Catch:{ Exception -> 0x0099 }
        if (r2 <= 0) goto L_0x002f;
    L_0x002d:
        i = r0;	 Catch:{ Exception -> 0x0099 }
    L_0x002f:
        r0 = "socket_server";
        r0 = r3.getProperty(r0);	 Catch:{ Exception -> 0x0099 }
        if (r0 == 0) goto L_0x003f;
    L_0x0037:
        r2 = r0.length();	 Catch:{ Exception -> 0x0099 }
        if (r2 <= 0) goto L_0x003f;
    L_0x003d:
        j = r0;	 Catch:{ Exception -> 0x0099 }
    L_0x003f:
        r0 = "socket_server_port";
        r0 = r3.getProperty(r0);	 Catch:{ Exception -> 0x0099 }
        if (r0 == 0) goto L_0x0053;
    L_0x0047:
        r2 = r0.length();	 Catch:{ Exception -> 0x0099 }
        if (r2 <= 0) goto L_0x0053;
    L_0x004d:
        r0 = java.lang.Integer.parseInt(r0);	 Catch:{ Exception -> 0x0099 }
        k = r0;	 Catch:{ Exception -> 0x0099 }
    L_0x0053:
        r0 = "config_server";
        r0 = r3.getProperty(r0);	 Catch:{ Exception -> 0x0099 }
        if (r0 == 0) goto L_0x0063;
    L_0x005b:
        r2 = r0.length();	 Catch:{ Exception -> 0x0099 }
        if (r2 <= 0) goto L_0x0063;
    L_0x0061:
        l = r0;	 Catch:{ Exception -> 0x0099 }
    L_0x0063:
        if (r1 == 0) goto L_0x0068;
    L_0x0065:
        r1.close();	 Catch:{ IOException -> 0x0069 }
    L_0x0068:
        return;
    L_0x0069:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0068;
    L_0x006e:
        r0 = move-exception;
        r1 = r2;
    L_0x0070:
        r2 = com.baidu.android.pushservice.b.a();	 Catch:{ all -> 0x0097 }
        if (r2 == 0) goto L_0x007f;
    L_0x0076:
        r2 = java.lang.System.out;	 Catch:{ all -> 0x0097 }
        r0 = r0.getMessage();	 Catch:{ all -> 0x0097 }
        r2.println(r0);	 Catch:{ all -> 0x0097 }
    L_0x007f:
        if (r1 == 0) goto L_0x0068;
    L_0x0081:
        r1.close();	 Catch:{ IOException -> 0x0085 }
        goto L_0x0068;
    L_0x0085:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0068;
    L_0x008a:
        r0 = move-exception;
        r1 = r2;
    L_0x008c:
        if (r1 == 0) goto L_0x0091;
    L_0x008e:
        r1.close();	 Catch:{ IOException -> 0x0092 }
    L_0x0091:
        throw r0;
    L_0x0092:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0091;
    L_0x0097:
        r0 = move-exception;
        goto L_0x008c;
    L_0x0099:
        r0 = move-exception;
        goto L_0x0070;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.pushservice.w.a():void");
    }
}
