package com.qc.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class QuietInstallEngine {
    /* JADX WARNING: Removed duplicated region for block: B:59:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:61:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0083  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x009e  */
    public static java.lang.String install(java.lang.String r14) {
        /*
        r13 = -1;
        r11 = 4;
        r0 = new java.lang.String[r11];
        r11 = 0;
        r12 = "pm";
        r0[r11] = r12;
        r11 = 1;
        r12 = "install";
        r0[r11] = r12;
        r11 = 2;
        r12 = "-r";
        r0[r11] = r12;
        r11 = 3;
        r0[r11] = r14;
        r9 = "";
        r7 = new java.lang.ProcessBuilder;
        r7.<init>(r0);
        r6 = 0;
        r4 = 0;
        r5 = 0;
        r1 = new java.io.ByteArrayOutputStream;	 Catch:{ IOException -> 0x0061, Exception -> 0x0076, all -> 0x0091 }
        r1.<init>();	 Catch:{ IOException -> 0x0061, Exception -> 0x0076, all -> 0x0091 }
        r8 = -1;
        r6 = r7.start();	 Catch:{ IOException -> 0x0061, Exception -> 0x0076, all -> 0x0091 }
        r4 = r6.getErrorStream();	 Catch:{ IOException -> 0x0061, Exception -> 0x0076, all -> 0x0091 }
    L_0x002e:
        r8 = r4.read();	 Catch:{ IOException -> 0x0061, Exception -> 0x0076, all -> 0x0091 }
        if (r8 != r13) goto L_0x005d;
    L_0x0034:
        r11 = 10;
        r1.write(r11);	 Catch:{ IOException -> 0x0061, Exception -> 0x0076, all -> 0x0091 }
        r5 = r6.getInputStream();	 Catch:{ IOException -> 0x0061, Exception -> 0x0076, all -> 0x0091 }
    L_0x003d:
        r8 = r5.read();	 Catch:{ IOException -> 0x0061, Exception -> 0x0076, all -> 0x0091 }
        if (r8 != r13) goto L_0x0072;
    L_0x0043:
        r2 = r1.toByteArray();	 Catch:{ IOException -> 0x0061, Exception -> 0x0076, all -> 0x0091 }
        r10 = new java.lang.String;	 Catch:{ IOException -> 0x0061, Exception -> 0x0076, all -> 0x0091 }
        r10.<init>(r2);	 Catch:{ IOException -> 0x0061, Exception -> 0x0076, all -> 0x0091 }
        if (r4 == 0) goto L_0x0051;
    L_0x004e:
        r4.close();	 Catch:{ IOException -> 0x00a7 }
    L_0x0051:
        if (r5 == 0) goto L_0x0056;
    L_0x0053:
        r5.close();	 Catch:{ IOException -> 0x00a7 }
    L_0x0056:
        if (r6 == 0) goto L_0x005b;
    L_0x0058:
        r6.destroy();
    L_0x005b:
        r9 = r10;
    L_0x005c:
        return r9;
    L_0x005d:
        r1.write(r8);	 Catch:{ IOException -> 0x0061, Exception -> 0x0076, all -> 0x0091 }
        goto L_0x002e;
    L_0x0061:
        r11 = move-exception;
        if (r4 == 0) goto L_0x0067;
    L_0x0064:
        r4.close();	 Catch:{ IOException -> 0x0087 }
    L_0x0067:
        if (r5 == 0) goto L_0x006c;
    L_0x0069:
        r5.close();	 Catch:{ IOException -> 0x0087 }
    L_0x006c:
        if (r6 == 0) goto L_0x005c;
    L_0x006e:
        r6.destroy();
        goto L_0x005c;
    L_0x0072:
        r1.write(r8);	 Catch:{ IOException -> 0x0061, Exception -> 0x0076, all -> 0x0091 }
        goto L_0x003d;
    L_0x0076:
        r11 = move-exception;
        if (r4 == 0) goto L_0x007c;
    L_0x0079:
        r4.close();	 Catch:{ IOException -> 0x008c }
    L_0x007c:
        if (r5 == 0) goto L_0x0081;
    L_0x007e:
        r5.close();	 Catch:{ IOException -> 0x008c }
    L_0x0081:
        if (r6 == 0) goto L_0x005c;
    L_0x0083:
        r6.destroy();
        goto L_0x005c;
    L_0x0087:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x006c;
    L_0x008c:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0081;
    L_0x0091:
        r11 = move-exception;
        if (r4 == 0) goto L_0x0097;
    L_0x0094:
        r4.close();	 Catch:{ IOException -> 0x00a2 }
    L_0x0097:
        if (r5 == 0) goto L_0x009c;
    L_0x0099:
        r5.close();	 Catch:{ IOException -> 0x00a2 }
    L_0x009c:
        if (r6 == 0) goto L_0x00a1;
    L_0x009e:
        r6.destroy();
    L_0x00a1:
        throw r11;
    L_0x00a2:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x009c;
    L_0x00a7:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0056;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.qc.common.QuietInstallEngine.install(java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:59:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:61:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0082  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x009d  */
    public static java.lang.String installInSDCard(int r14) {
        /*
        r13 = -1;
        r11 = 3;
        r0 = new java.lang.String[r11];
        r11 = 0;
        r12 = "pm";
        r0[r11] = r12;
        r11 = 1;
        r12 = "setInstallLocation";
        r0[r11] = r12;
        r11 = 2;
        r12 = java.lang.String.valueOf(r14);
        r0[r11] = r12;
        r9 = "";
        r7 = new java.lang.ProcessBuilder;
        r7.<init>(r0);
        r6 = 0;
        r4 = 0;
        r5 = 0;
        r1 = new java.io.ByteArrayOutputStream;	 Catch:{ IOException -> 0x0060, Exception -> 0x0075, all -> 0x0090 }
        r1.<init>();	 Catch:{ IOException -> 0x0060, Exception -> 0x0075, all -> 0x0090 }
        r8 = -1;
        r6 = r7.start();	 Catch:{ IOException -> 0x0060, Exception -> 0x0075, all -> 0x0090 }
        r4 = r6.getErrorStream();	 Catch:{ IOException -> 0x0060, Exception -> 0x0075, all -> 0x0090 }
    L_0x002d:
        r8 = r4.read();	 Catch:{ IOException -> 0x0060, Exception -> 0x0075, all -> 0x0090 }
        if (r8 != r13) goto L_0x005c;
    L_0x0033:
        r11 = 10;
        r1.write(r11);	 Catch:{ IOException -> 0x0060, Exception -> 0x0075, all -> 0x0090 }
        r5 = r6.getInputStream();	 Catch:{ IOException -> 0x0060, Exception -> 0x0075, all -> 0x0090 }
    L_0x003c:
        r8 = r5.read();	 Catch:{ IOException -> 0x0060, Exception -> 0x0075, all -> 0x0090 }
        if (r8 != r13) goto L_0x0071;
    L_0x0042:
        r2 = r1.toByteArray();	 Catch:{ IOException -> 0x0060, Exception -> 0x0075, all -> 0x0090 }
        r10 = new java.lang.String;	 Catch:{ IOException -> 0x0060, Exception -> 0x0075, all -> 0x0090 }
        r10.<init>(r2);	 Catch:{ IOException -> 0x0060, Exception -> 0x0075, all -> 0x0090 }
        if (r4 == 0) goto L_0x0050;
    L_0x004d:
        r4.close();	 Catch:{ IOException -> 0x00a6 }
    L_0x0050:
        if (r5 == 0) goto L_0x0055;
    L_0x0052:
        r5.close();	 Catch:{ IOException -> 0x00a6 }
    L_0x0055:
        if (r6 == 0) goto L_0x005a;
    L_0x0057:
        r6.destroy();
    L_0x005a:
        r9 = r10;
    L_0x005b:
        return r9;
    L_0x005c:
        r1.write(r8);	 Catch:{ IOException -> 0x0060, Exception -> 0x0075, all -> 0x0090 }
        goto L_0x002d;
    L_0x0060:
        r11 = move-exception;
        if (r4 == 0) goto L_0x0066;
    L_0x0063:
        r4.close();	 Catch:{ IOException -> 0x0086 }
    L_0x0066:
        if (r5 == 0) goto L_0x006b;
    L_0x0068:
        r5.close();	 Catch:{ IOException -> 0x0086 }
    L_0x006b:
        if (r6 == 0) goto L_0x005b;
    L_0x006d:
        r6.destroy();
        goto L_0x005b;
    L_0x0071:
        r1.write(r8);	 Catch:{ IOException -> 0x0060, Exception -> 0x0075, all -> 0x0090 }
        goto L_0x003c;
    L_0x0075:
        r11 = move-exception;
        if (r4 == 0) goto L_0x007b;
    L_0x0078:
        r4.close();	 Catch:{ IOException -> 0x008b }
    L_0x007b:
        if (r5 == 0) goto L_0x0080;
    L_0x007d:
        r5.close();	 Catch:{ IOException -> 0x008b }
    L_0x0080:
        if (r6 == 0) goto L_0x005b;
    L_0x0082:
        r6.destroy();
        goto L_0x005b;
    L_0x0086:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x006b;
    L_0x008b:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0080;
    L_0x0090:
        r11 = move-exception;
        if (r4 == 0) goto L_0x0096;
    L_0x0093:
        r4.close();	 Catch:{ IOException -> 0x00a1 }
    L_0x0096:
        if (r5 == 0) goto L_0x009b;
    L_0x0098:
        r5.close();	 Catch:{ IOException -> 0x00a1 }
    L_0x009b:
        if (r6 == 0) goto L_0x00a0;
    L_0x009d:
        r6.destroy();
    L_0x00a0:
        throw r11;
    L_0x00a1:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x009b;
    L_0x00a6:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0055;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.qc.common.QuietInstallEngine.installInSDCard(int):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:63:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x006c  */
    /* JADX WARNING: Removed duplicated region for block: B:65:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0084  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x009f  */
    public static java.lang.String unInstall(java.lang.String r14) {
        /*
        r13 = -1;
        r11 = 3;
        r0 = new java.lang.String[r11];
        r11 = 0;
        r12 = "pm";
        r0[r11] = r12;
        r11 = 1;
        r12 = "uninstall";
        r0[r11] = r12;
        r11 = 2;
        r0[r11] = r14;
        r9 = "";
        r7 = new java.lang.ProcessBuilder;
        r7.<init>(r0);
        r6 = 0;
        r4 = 0;
        r5 = 0;
        r1 = new java.io.ByteArrayOutputStream;	 Catch:{ IOException -> 0x005c, Exception -> 0x0074 }
        r1.<init>();	 Catch:{ IOException -> 0x005c, Exception -> 0x0074 }
        r8 = -1;
        r6 = r7.start();	 Catch:{ IOException -> 0x005c, Exception -> 0x0074 }
        r4 = r6.getErrorStream();	 Catch:{ IOException -> 0x005c, Exception -> 0x0074 }
    L_0x0029:
        r8 = r4.read();	 Catch:{ IOException -> 0x005c, Exception -> 0x0074 }
        if (r8 != r13) goto L_0x0058;
    L_0x002f:
        r11 = 10;
        r1.write(r11);	 Catch:{ IOException -> 0x005c, Exception -> 0x0074 }
        r5 = r6.getInputStream();	 Catch:{ IOException -> 0x005c, Exception -> 0x0074 }
    L_0x0038:
        r8 = r5.read();	 Catch:{ IOException -> 0x005c, Exception -> 0x0074 }
        if (r8 != r13) goto L_0x0070;
    L_0x003e:
        r2 = r1.toByteArray();	 Catch:{ IOException -> 0x005c, Exception -> 0x0074 }
        r10 = new java.lang.String;	 Catch:{ IOException -> 0x005c, Exception -> 0x0074 }
        r10.<init>(r2);	 Catch:{ IOException -> 0x005c, Exception -> 0x0074 }
        if (r4 == 0) goto L_0x004c;
    L_0x0049:
        r4.close();	 Catch:{ IOException -> 0x00a8 }
    L_0x004c:
        if (r5 == 0) goto L_0x0051;
    L_0x004e:
        r5.close();	 Catch:{ IOException -> 0x00a8 }
    L_0x0051:
        if (r6 == 0) goto L_0x0056;
    L_0x0053:
        r6.destroy();
    L_0x0056:
        r9 = r10;
    L_0x0057:
        return r9;
    L_0x0058:
        r1.write(r8);	 Catch:{ IOException -> 0x005c, Exception -> 0x0074 }
        goto L_0x0029;
    L_0x005c:
        r3 = move-exception;
        r3.printStackTrace();	 Catch:{ all -> 0x0092 }
        if (r4 == 0) goto L_0x0065;
    L_0x0062:
        r4.close();	 Catch:{ IOException -> 0x0088 }
    L_0x0065:
        if (r5 == 0) goto L_0x006a;
    L_0x0067:
        r5.close();	 Catch:{ IOException -> 0x0088 }
    L_0x006a:
        if (r6 == 0) goto L_0x0057;
    L_0x006c:
        r6.destroy();
        goto L_0x0057;
    L_0x0070:
        r1.write(r8);	 Catch:{ IOException -> 0x005c, Exception -> 0x0074 }
        goto L_0x0038;
    L_0x0074:
        r3 = move-exception;
        r3.printStackTrace();	 Catch:{ all -> 0x0092 }
        if (r4 == 0) goto L_0x007d;
    L_0x007a:
        r4.close();	 Catch:{ IOException -> 0x008d }
    L_0x007d:
        if (r5 == 0) goto L_0x0082;
    L_0x007f:
        r5.close();	 Catch:{ IOException -> 0x008d }
    L_0x0082:
        if (r6 == 0) goto L_0x0057;
    L_0x0084:
        r6.destroy();
        goto L_0x0057;
    L_0x0088:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x006a;
    L_0x008d:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0082;
    L_0x0092:
        r11 = move-exception;
        if (r4 == 0) goto L_0x0098;
    L_0x0095:
        r4.close();	 Catch:{ IOException -> 0x00a3 }
    L_0x0098:
        if (r5 == 0) goto L_0x009d;
    L_0x009a:
        r5.close();	 Catch:{ IOException -> 0x00a3 }
    L_0x009d:
        if (r6 == 0) goto L_0x00a2;
    L_0x009f:
        r6.destroy();
    L_0x00a2:
        throw r11;
    L_0x00a3:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x009d;
    L_0x00a8:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0051;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.qc.common.QuietInstallEngine.unInstall(java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:54:0x00a4  */
    /* JADX WARNING: Removed duplicated region for block: B:65:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0089  */
    /* JADX WARNING: Removed duplicated region for block: B:63:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0071  */
    public static java.lang.String unInstall_saveData(java.lang.String r14) {
        /*
        r13 = -1;
        r11 = 4;
        r0 = new java.lang.String[r11];
        r11 = 0;
        r12 = "pm";
        r0[r11] = r12;
        r11 = 1;
        r12 = "uninstall";
        r0[r11] = r12;
        r11 = 2;
        r12 = "-k";
        r0[r11] = r12;
        r11 = 3;
        r0[r11] = r14;
        r9 = "";
        r7 = new java.lang.ProcessBuilder;
        r7.<init>(r0);
        r6 = 0;
        r4 = 0;
        r5 = 0;
        r1 = new java.io.ByteArrayOutputStream;	 Catch:{ IOException -> 0x0061, Exception -> 0x0079 }
        r1.<init>();	 Catch:{ IOException -> 0x0061, Exception -> 0x0079 }
        r8 = -1;
        r6 = r7.start();	 Catch:{ IOException -> 0x0061, Exception -> 0x0079 }
        r4 = r6.getErrorStream();	 Catch:{ IOException -> 0x0061, Exception -> 0x0079 }
    L_0x002e:
        r8 = r4.read();	 Catch:{ IOException -> 0x0061, Exception -> 0x0079 }
        if (r8 != r13) goto L_0x005d;
    L_0x0034:
        r11 = 10;
        r1.write(r11);	 Catch:{ IOException -> 0x0061, Exception -> 0x0079 }
        r5 = r6.getInputStream();	 Catch:{ IOException -> 0x0061, Exception -> 0x0079 }
    L_0x003d:
        r8 = r5.read();	 Catch:{ IOException -> 0x0061, Exception -> 0x0079 }
        if (r8 != r13) goto L_0x0075;
    L_0x0043:
        r2 = r1.toByteArray();	 Catch:{ IOException -> 0x0061, Exception -> 0x0079 }
        r10 = new java.lang.String;	 Catch:{ IOException -> 0x0061, Exception -> 0x0079 }
        r10.<init>(r2);	 Catch:{ IOException -> 0x0061, Exception -> 0x0079 }
        if (r4 == 0) goto L_0x0051;
    L_0x004e:
        r4.close();	 Catch:{ IOException -> 0x00ad }
    L_0x0051:
        if (r5 == 0) goto L_0x0056;
    L_0x0053:
        r5.close();	 Catch:{ IOException -> 0x00ad }
    L_0x0056:
        if (r6 == 0) goto L_0x005b;
    L_0x0058:
        r6.destroy();
    L_0x005b:
        r9 = r10;
    L_0x005c:
        return r9;
    L_0x005d:
        r1.write(r8);	 Catch:{ IOException -> 0x0061, Exception -> 0x0079 }
        goto L_0x002e;
    L_0x0061:
        r3 = move-exception;
        r3.printStackTrace();	 Catch:{ all -> 0x0097 }
        if (r4 == 0) goto L_0x006a;
    L_0x0067:
        r4.close();	 Catch:{ IOException -> 0x008d }
    L_0x006a:
        if (r5 == 0) goto L_0x006f;
    L_0x006c:
        r5.close();	 Catch:{ IOException -> 0x008d }
    L_0x006f:
        if (r6 == 0) goto L_0x005c;
    L_0x0071:
        r6.destroy();
        goto L_0x005c;
    L_0x0075:
        r1.write(r8);	 Catch:{ IOException -> 0x0061, Exception -> 0x0079 }
        goto L_0x003d;
    L_0x0079:
        r3 = move-exception;
        r3.printStackTrace();	 Catch:{ all -> 0x0097 }
        if (r4 == 0) goto L_0x0082;
    L_0x007f:
        r4.close();	 Catch:{ IOException -> 0x0092 }
    L_0x0082:
        if (r5 == 0) goto L_0x0087;
    L_0x0084:
        r5.close();	 Catch:{ IOException -> 0x0092 }
    L_0x0087:
        if (r6 == 0) goto L_0x005c;
    L_0x0089:
        r6.destroy();
        goto L_0x005c;
    L_0x008d:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x006f;
    L_0x0092:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0087;
    L_0x0097:
        r11 = move-exception;
        if (r4 == 0) goto L_0x009d;
    L_0x009a:
        r4.close();	 Catch:{ IOException -> 0x00a8 }
    L_0x009d:
        if (r5 == 0) goto L_0x00a2;
    L_0x009f:
        r5.close();	 Catch:{ IOException -> 0x00a8 }
    L_0x00a2:
        if (r6 == 0) goto L_0x00a7;
    L_0x00a4:
        r6.destroy();
    L_0x00a7:
        throw r11;
    L_0x00a8:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x00a2;
    L_0x00ad:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0056;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.qc.common.QuietInstallEngine.unInstall_saveData(java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Failed to extract finally block: empty outs */
    public static void execCommand(java.lang.String... r4) {
        /*
        r1 = 0;
        r2 = new java.lang.ProcessBuilder;	 Catch:{ IOException -> 0x0017 }
        r3 = 0;
        r3 = new java.lang.String[r3];	 Catch:{ IOException -> 0x0017 }
        r2.<init>(r3);	 Catch:{ IOException -> 0x0017 }
        r2 = r2.command(r4);	 Catch:{ IOException -> 0x0017 }
        r1 = r2.start();	 Catch:{ IOException -> 0x0017 }
        if (r1 == 0) goto L_0x0016;
    L_0x0013:
        r1.destroy();
    L_0x0016:
        return;
    L_0x0017:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0021 }
        if (r1 == 0) goto L_0x0016;
    L_0x001d:
        r1.destroy();
        goto L_0x0016;
    L_0x0021:
        r2 = move-exception;
        if (r1 == 0) goto L_0x0027;
    L_0x0024:
        r1.destroy();
    L_0x0027:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.qc.common.QuietInstallEngine.execCommand(java.lang.String[]):void");
    }

    public static void installByRoot(String currentTempFilePath) {
        OutputStream out = null;
        InputStream in = null;
        try {
            Process process = Runtime.getRuntime().exec("su");
            out = process.getOutputStream();
            out.write(("pm install -r " + currentTempFilePath + "\n").getBytes());
            in = process.getInputStream();
            byte[] bs = new byte[256];
            while (true) {
                int len = in.read(bs);
                if (-1 == len) {
                    break;
                }
                new String(bs, 0, len).equals("Success\n");
            }
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e22) {
                    e22.printStackTrace();
                    return;
                }
            }
            if (in != null) {
                in.close();
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e222) {
                    e222.printStackTrace();
                    return;
                }
            }
            if (in != null) {
                in.close();
            }
        } catch (Throwable th) {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e2222) {
                    e2222.printStackTrace();
                }
            }
            if (in != null) {
                in.close();
            }
        }
    }

    public static String run(String[] cmd, String workdirectory) throws IOException {
        String result = "";
        try {
            ProcessBuilder builder = new ProcessBuilder(cmd);
            InputStream in = null;
            if (workdirectory != null) {
                builder.directory(new File(workdirectory));
                builder.redirectErrorStream(true);
                in = builder.start().getInputStream();
                byte[] re = new byte[1024];
                while (in.read(re) != -1) {
                    result = new StringBuilder(String.valueOf(result)).append(new String(re)).toString();
                }
            }
            if (in != null) {
                in.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String installCMD(String apkAbsolutePath) {
        String result = null;
        try {
            return run(new String[]{"/system/bin/pm", "install", "-r", apkAbsolutePath}, "/system/bin/");
        } catch (IOException e) {
            return result;
        }
    }

    public static String unInstallCMD(String packageName) {
        String result = null;
        try {
            return run(new String[]{"/system/bin/pm", "uninstall", packageName}, "/system/bin/");
        } catch (IOException e) {
            return result;
        }
    }

    public static String ec(String command) throws InterruptedException {
        String returnString = "";
        try {
            Process pro = Runtime.getRuntime().exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
            while (true) {
                String line = input.readLine();
                if (line == null) {
                    break;
                }
                returnString = new StringBuilder(String.valueOf(returnString)).append(line).append("\n").toString();
            }
            input.close();
            output.close();
            pro.destroy();
        } catch (IOException e) {
        }
        return returnString;
    }

    public static String fileAddOpter(String filePath) throws InterruptedException {
        String result = null;
        try {
            return run(new String[]{"/system/bin/chmod", "666", filePath}, "/system/bin/");
        } catch (IOException e) {
            return result;
        }
    }
}
