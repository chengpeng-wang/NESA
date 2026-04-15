package com.trilead.ssh2.channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class StreamForwarder extends Thread {
    byte[] buffer = new byte[30000];
    Channel c;
    InputStream is;
    String mode;
    OutputStream os;
    Socket s;
    StreamForwarder sibling;

    StreamForwarder(Channel c, StreamForwarder sibling, Socket s, InputStream is, OutputStream os, String mode) throws IOException {
        this.is = is;
        this.os = os;
        this.mode = mode;
        this.c = c;
        this.sibling = sibling;
        this.s = s;
    }

    /* JADX WARNING: Removed duplicated region for block: B:43:0x00ce A:{Splitter:B:0:0x0000, ExcHandler: all (r2_0 'th' java.lang.Throwable)} */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x010a A:{Catch:{ IOException -> 0x012e }} */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00dd A:{LOOP_START, LOOP:3: B:50:0x00dd->B:87:0x00dd} */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00dd A:{LOOP_START, LOOP:3: B:50:0x00dd->B:87:0x00dd} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:20:0x0059, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:22:?, code skipped:
            r7.c.cm.closeChannel(r7.c, "Closed due to exception in StreamForwarder (" + r7.mode + "): " + r0.getMessage(), true);
     */
    /* JADX WARNING: Missing block: B:24:?, code skipped:
            r7.os.close();
     */
    /* JADX WARNING: Missing block: B:26:?, code skipped:
            r7.is.close();
     */
    /* JADX WARNING: Missing block: B:28:0x008f, code skipped:
            if (r7.sibling != null) goto L_0x0091;
     */
    /* JADX WARNING: Missing block: B:30:0x0097, code skipped:
            if (r7.sibling.isAlive() != false) goto L_0x00c6;
     */
    /* JADX WARNING: Missing block: B:32:?, code skipped:
            r7.c.cm.closeChannel(r7.c, "StreamForwarder (" + r7.mode + ") is cleaning up the connection", true);
     */
    /* JADX WARNING: Missing block: B:35:0x00bc, code skipped:
            if (r7.s != null) goto L_0x00be;
     */
    /* JADX WARNING: Missing block: B:36:0x00be, code skipped:
            r7.s.close();
     */
    /* JADX WARNING: Missing block: B:39:?, code skipped:
            r7.sibling.join();
     */
    /* JADX WARNING: Missing block: B:43:0x00ce, code skipped:
            r2 = move-exception;
     */
    /* JADX WARNING: Missing block: B:45:?, code skipped:
            r7.os.close();
     */
    /* JADX WARNING: Missing block: B:51:0x00e3, code skipped:
            if (r7.sibling.isAlive() != false) goto L_0x0110;
     */
    /* JADX WARNING: Missing block: B:53:?, code skipped:
            r7.c.cm.closeChannel(r7.c, "StreamForwarder (" + r7.mode + ") is cleaning up the connection", true);
     */
    /* JADX WARNING: Missing block: B:57:0x010a, code skipped:
            r7.s.close();
     */
    /* JADX WARNING: Missing block: B:60:?, code skipped:
            r7.sibling.join();
     */
    /* JADX WARNING: Missing block: B:93:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:94:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:95:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:96:?, code skipped:
            return;
     */
    public void run() {
        /*
        r7 = this;
    L_0x0000:
        r2 = r7.is;	 Catch:{ IOException -> 0x0059, all -> 0x00ce }
        r3 = r7.buffer;	 Catch:{ IOException -> 0x0059, all -> 0x00ce }
        r1 = r2.read(r3);	 Catch:{ IOException -> 0x0059, all -> 0x00ce }
        if (r1 > 0) goto L_0x004b;
    L_0x000a:
        r2 = r7.os;	 Catch:{ IOException -> 0x012b }
        r2.close();	 Catch:{ IOException -> 0x012b }
    L_0x000f:
        r2 = r7.is;	 Catch:{ IOException -> 0x0128 }
        r2.close();	 Catch:{ IOException -> 0x0128 }
    L_0x0014:
        r2 = r7.sibling;
        if (r2 == 0) goto L_0x004a;
    L_0x0018:
        r2 = r7.sibling;
        r2 = r2.isAlive();
        if (r2 != 0) goto L_0x0118;
    L_0x0020:
        r2 = r7.c;	 Catch:{ IOException -> 0x0125 }
        r2 = r2.cm;	 Catch:{ IOException -> 0x0125 }
        r3 = r7.c;	 Catch:{ IOException -> 0x0125 }
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0125 }
        r5 = "StreamForwarder (";
        r4.<init>(r5);	 Catch:{ IOException -> 0x0125 }
        r5 = r7.mode;	 Catch:{ IOException -> 0x0125 }
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x0125 }
        r5 = ") is cleaning up the connection";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x0125 }
        r4 = r4.toString();	 Catch:{ IOException -> 0x0125 }
        r5 = 1;
        r2.closeChannel(r3, r4, r5);	 Catch:{ IOException -> 0x0125 }
    L_0x0041:
        r2 = r7.s;	 Catch:{ IOException -> 0x0122 }
        if (r2 == 0) goto L_0x004a;
    L_0x0045:
        r2 = r7.s;	 Catch:{ IOException -> 0x0122 }
        r2.close();	 Catch:{ IOException -> 0x0122 }
    L_0x004a:
        return;
    L_0x004b:
        r2 = r7.os;	 Catch:{ IOException -> 0x0059, all -> 0x00ce }
        r3 = r7.buffer;	 Catch:{ IOException -> 0x0059, all -> 0x00ce }
        r4 = 0;
        r2.write(r3, r4, r1);	 Catch:{ IOException -> 0x0059, all -> 0x00ce }
        r2 = r7.os;	 Catch:{ IOException -> 0x0059, all -> 0x00ce }
        r2.flush();	 Catch:{ IOException -> 0x0059, all -> 0x00ce }
        goto L_0x0000;
    L_0x0059:
        r0 = move-exception;
        r2 = r7.c;	 Catch:{ IOException -> 0x013e, all -> 0x00ce }
        r2 = r2.cm;	 Catch:{ IOException -> 0x013e, all -> 0x00ce }
        r3 = r7.c;	 Catch:{ IOException -> 0x013e, all -> 0x00ce }
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x013e, all -> 0x00ce }
        r5 = "Closed due to exception in StreamForwarder (";
        r4.<init>(r5);	 Catch:{ IOException -> 0x013e, all -> 0x00ce }
        r5 = r7.mode;	 Catch:{ IOException -> 0x013e, all -> 0x00ce }
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x013e, all -> 0x00ce }
        r5 = "): ";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x013e, all -> 0x00ce }
        r5 = r0.getMessage();	 Catch:{ IOException -> 0x013e, all -> 0x00ce }
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x013e, all -> 0x00ce }
        r4 = r4.toString();	 Catch:{ IOException -> 0x013e, all -> 0x00ce }
        r5 = 1;
        r2.closeChannel(r3, r4, r5);	 Catch:{ IOException -> 0x013e, all -> 0x00ce }
    L_0x0083:
        r2 = r7.os;	 Catch:{ IOException -> 0x013b }
        r2.close();	 Catch:{ IOException -> 0x013b }
    L_0x0088:
        r2 = r7.is;	 Catch:{ IOException -> 0x0138 }
        r2.close();	 Catch:{ IOException -> 0x0138 }
    L_0x008d:
        r2 = r7.sibling;
        if (r2 == 0) goto L_0x004a;
    L_0x0091:
        r2 = r7.sibling;
        r2 = r2.isAlive();
        if (r2 != 0) goto L_0x00c6;
    L_0x0099:
        r2 = r7.c;	 Catch:{ IOException -> 0x0136 }
        r2 = r2.cm;	 Catch:{ IOException -> 0x0136 }
        r3 = r7.c;	 Catch:{ IOException -> 0x0136 }
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0136 }
        r5 = "StreamForwarder (";
        r4.<init>(r5);	 Catch:{ IOException -> 0x0136 }
        r5 = r7.mode;	 Catch:{ IOException -> 0x0136 }
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x0136 }
        r5 = ") is cleaning up the connection";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x0136 }
        r4 = r4.toString();	 Catch:{ IOException -> 0x0136 }
        r5 = 1;
        r2.closeChannel(r3, r4, r5);	 Catch:{ IOException -> 0x0136 }
    L_0x00ba:
        r2 = r7.s;	 Catch:{ IOException -> 0x00c4 }
        if (r2 == 0) goto L_0x004a;
    L_0x00be:
        r2 = r7.s;	 Catch:{ IOException -> 0x00c4 }
        r2.close();	 Catch:{ IOException -> 0x00c4 }
        goto L_0x004a;
    L_0x00c4:
        r2 = move-exception;
        goto L_0x004a;
    L_0x00c6:
        r2 = r7.sibling;	 Catch:{ InterruptedException -> 0x00cc }
        r2.join();	 Catch:{ InterruptedException -> 0x00cc }
        goto L_0x0091;
    L_0x00cc:
        r2 = move-exception;
        goto L_0x0091;
    L_0x00ce:
        r2 = move-exception;
        r3 = r7.os;	 Catch:{ IOException -> 0x0134 }
        r3.close();	 Catch:{ IOException -> 0x0134 }
    L_0x00d4:
        r3 = r7.is;	 Catch:{ IOException -> 0x0132 }
        r3.close();	 Catch:{ IOException -> 0x0132 }
    L_0x00d9:
        r3 = r7.sibling;
        if (r3 == 0) goto L_0x010f;
    L_0x00dd:
        r3 = r7.sibling;
        r3 = r3.isAlive();
        if (r3 != 0) goto L_0x0110;
    L_0x00e5:
        r3 = r7.c;	 Catch:{ IOException -> 0x0130 }
        r3 = r3.cm;	 Catch:{ IOException -> 0x0130 }
        r4 = r7.c;	 Catch:{ IOException -> 0x0130 }
        r5 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0130 }
        r6 = "StreamForwarder (";
        r5.<init>(r6);	 Catch:{ IOException -> 0x0130 }
        r6 = r7.mode;	 Catch:{ IOException -> 0x0130 }
        r5 = r5.append(r6);	 Catch:{ IOException -> 0x0130 }
        r6 = ") is cleaning up the connection";
        r5 = r5.append(r6);	 Catch:{ IOException -> 0x0130 }
        r5 = r5.toString();	 Catch:{ IOException -> 0x0130 }
        r6 = 1;
        r3.closeChannel(r4, r5, r6);	 Catch:{ IOException -> 0x0130 }
    L_0x0106:
        r3 = r7.s;	 Catch:{ IOException -> 0x012e }
        if (r3 == 0) goto L_0x010f;
    L_0x010a:
        r3 = r7.s;	 Catch:{ IOException -> 0x012e }
        r3.close();	 Catch:{ IOException -> 0x012e }
    L_0x010f:
        throw r2;
    L_0x0110:
        r3 = r7.sibling;	 Catch:{ InterruptedException -> 0x0116 }
        r3.join();	 Catch:{ InterruptedException -> 0x0116 }
        goto L_0x00dd;
    L_0x0116:
        r3 = move-exception;
        goto L_0x00dd;
    L_0x0118:
        r2 = r7.sibling;	 Catch:{ InterruptedException -> 0x011f }
        r2.join();	 Catch:{ InterruptedException -> 0x011f }
        goto L_0x0018;
    L_0x011f:
        r2 = move-exception;
        goto L_0x0018;
    L_0x0122:
        r2 = move-exception;
        goto L_0x004a;
    L_0x0125:
        r2 = move-exception;
        goto L_0x0041;
    L_0x0128:
        r2 = move-exception;
        goto L_0x0014;
    L_0x012b:
        r2 = move-exception;
        goto L_0x000f;
    L_0x012e:
        r3 = move-exception;
        goto L_0x010f;
    L_0x0130:
        r3 = move-exception;
        goto L_0x0106;
    L_0x0132:
        r3 = move-exception;
        goto L_0x00d9;
    L_0x0134:
        r3 = move-exception;
        goto L_0x00d4;
    L_0x0136:
        r2 = move-exception;
        goto L_0x00ba;
    L_0x0138:
        r2 = move-exception;
        goto L_0x008d;
    L_0x013b:
        r2 = move-exception;
        goto L_0x0088;
    L_0x013e:
        r2 = move-exception;
        goto L_0x0083;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trilead.ssh2.channel.StreamForwarder.run():void");
    }
}
