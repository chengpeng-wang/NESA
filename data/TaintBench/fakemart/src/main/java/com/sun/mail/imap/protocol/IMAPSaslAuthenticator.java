package com.sun.mail.imap.protocol;

import java.io.PrintStream;
import java.util.Properties;

public class IMAPSaslAuthenticator implements SaslAuthenticator {
    /* access modifiers changed from: private */
    public boolean debug;
    private String host;
    private String name;
    /* access modifiers changed from: private */
    public PrintStream out;
    private IMAPProtocol pr;
    private Properties props;

    public IMAPSaslAuthenticator(IMAPProtocol pr, String name, Properties props, boolean debug, PrintStream out, String host) {
        this.pr = pr;
        this.name = name;
        this.props = props;
        this.debug = debug;
        this.out = out;
        this.host = host;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* JADX WARNING: Missing block: B:41:0x0124, code skipped:
            if (r20.equalsIgnoreCase("auth-conf") != false) goto L_0x0126;
     */
    /* JADX WARNING: Missing block: B:118:?, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:120:?, code skipped:
            return false;
     */
    public boolean authenticate(java.lang.String[] r31, java.lang.String r32, java.lang.String r33, java.lang.String r34, java.lang.String r35) throws com.sun.mail.iap.ProtocolException {
        /*
        r30 = this;
        r0 = r30;
        r0 = r0.pr;
        r29 = r0;
        monitor-enter(r29);
        r28 = new java.util.Vector;	 Catch:{ all -> 0x022b }
        r28.<init>();	 Catch:{ all -> 0x022b }
        r26 = 0;
        r21 = 0;
        r13 = 0;
        r0 = r30;
        r4 = r0.debug;	 Catch:{ all -> 0x022b }
        if (r4 == 0) goto L_0x002d;
    L_0x0017:
        r0 = r30;
        r4 = r0.out;	 Catch:{ all -> 0x022b }
        r5 = "IMAP SASL DEBUG: Mechanisms:";
        r4.print(r5);	 Catch:{ all -> 0x022b }
        r15 = 0;
    L_0x0021:
        r0 = r31;
        r4 = r0.length;	 Catch:{ all -> 0x022b }
        if (r15 < r4) goto L_0x0068;
    L_0x0026:
        r0 = r30;
        r4 = r0.out;	 Catch:{ all -> 0x022b }
        r4.println();	 Catch:{ all -> 0x022b }
    L_0x002d:
        r22 = r32;
        r27 = r34;
        r19 = r35;
        r9 = new com.sun.mail.imap.protocol.IMAPSaslAuthenticator$1;	 Catch:{ all -> 0x022b }
        r0 = r30;
        r1 = r27;
        r2 = r19;
        r3 = r22;
        r9.m315init(r1, r2, r3);	 Catch:{ all -> 0x022b }
        r0 = r30;
        r6 = r0.name;	 Catch:{ SaslException -> 0x0083 }
        r0 = r30;
        r7 = r0.host;	 Catch:{ SaslException -> 0x0083 }
        r0 = r30;
        r8 = r0.props;	 Catch:{ SaslException -> 0x0083 }
        r4 = r31;
        r5 = r33;
        r24 = javax.security.sasl.Sasl.createSaslClient(r4, r5, r6, r7, r8, r9);	 Catch:{ SaslException -> 0x0083 }
        if (r24 != 0) goto L_0x00a5;
    L_0x0056:
        r0 = r30;
        r4 = r0.debug;	 Catch:{ all -> 0x022b }
        if (r4 == 0) goto L_0x0065;
    L_0x005c:
        r0 = r30;
        r4 = r0.out;	 Catch:{ all -> 0x022b }
        r5 = "IMAP SASL DEBUG: No SASL support";
        r4.println(r5);	 Catch:{ all -> 0x022b }
    L_0x0065:
        monitor-exit(r29);	 Catch:{ all -> 0x022b }
        r4 = 0;
    L_0x0067:
        return r4;
    L_0x0068:
        r0 = r30;
        r4 = r0.out;	 Catch:{ all -> 0x022b }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x022b }
        r6 = " ";
        r5.<init>(r6);	 Catch:{ all -> 0x022b }
        r6 = r31[r15];	 Catch:{ all -> 0x022b }
        r5 = r5.append(r6);	 Catch:{ all -> 0x022b }
        r5 = r5.toString();	 Catch:{ all -> 0x022b }
        r4.print(r5);	 Catch:{ all -> 0x022b }
        r15 = r15 + 1;
        goto L_0x0021;
    L_0x0083:
        r25 = move-exception;
        r0 = r30;
        r4 = r0.debug;	 Catch:{ all -> 0x022b }
        if (r4 == 0) goto L_0x00a2;
    L_0x008a:
        r0 = r30;
        r4 = r0.out;	 Catch:{ all -> 0x022b }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x022b }
        r6 = "IMAP SASL DEBUG: Failed to create SASL client: ";
        r5.<init>(r6);	 Catch:{ all -> 0x022b }
        r0 = r25;
        r5 = r5.append(r0);	 Catch:{ all -> 0x022b }
        r5 = r5.toString();	 Catch:{ all -> 0x022b }
        r4.println(r5);	 Catch:{ all -> 0x022b }
    L_0x00a2:
        monitor-exit(r29);	 Catch:{ all -> 0x022b }
        r4 = 0;
        goto L_0x0067;
    L_0x00a5:
        r0 = r30;
        r4 = r0.debug;	 Catch:{ all -> 0x022b }
        if (r4 == 0) goto L_0x00c5;
    L_0x00ab:
        r0 = r30;
        r4 = r0.out;	 Catch:{ all -> 0x022b }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x022b }
        r6 = "IMAP SASL DEBUG: SASL client ";
        r5.<init>(r6);	 Catch:{ all -> 0x022b }
        r6 = r24.getMechanismName();	 Catch:{ all -> 0x022b }
        r5 = r5.append(r6);	 Catch:{ all -> 0x022b }
        r5 = r5.toString();	 Catch:{ all -> 0x022b }
        r4.println(r5);	 Catch:{ all -> 0x022b }
    L_0x00c5:
        r0 = r30;
        r4 = r0.pr;	 Catch:{ Exception -> 0x0139 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0139 }
        r6 = "AUTHENTICATE ";
        r5.<init>(r6);	 Catch:{ Exception -> 0x0139 }
        r6 = r24.getMechanismName();	 Catch:{ Exception -> 0x0139 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0139 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0139 }
        r6 = 0;
        r26 = r4.writeCommand(r5, r6);	 Catch:{ Exception -> 0x0139 }
        r0 = r30;
        r4 = r0.pr;	 Catch:{ all -> 0x022b }
        r18 = r4.getIMAPOutputStream();	 Catch:{ all -> 0x022b }
        r12 = new java.io.ByteArrayOutputStream;	 Catch:{ all -> 0x022b }
        r12.<init>();	 Catch:{ all -> 0x022b }
        r4 = 2;
        r10 = new byte[r4];	 Catch:{ all -> 0x022b }
        r10 = {13, 10};	 Catch:{ all -> 0x022b }
        r4 = r24.getMechanismName();	 Catch:{ all -> 0x022b }
        r5 = "XGWTRUSTEDAPP";
        r17 = r4.equals(r5);	 Catch:{ all -> 0x022b }
    L_0x00fe:
        if (r13 == 0) goto L_0x015a;
    L_0x0100:
        r4 = r24.isComplete();	 Catch:{ all -> 0x022b }
        if (r4 == 0) goto L_0x0255;
    L_0x0106:
        r4 = "javax.security.sasl.qop";
        r0 = r24;
        r20 = r0.getNegotiatedProperty(r4);	 Catch:{ all -> 0x022b }
        r20 = (java.lang.String) r20;	 Catch:{ all -> 0x022b }
        if (r20 == 0) goto L_0x0255;
    L_0x0112:
        r4 = "auth-int";
        r0 = r20;
        r4 = r0.equalsIgnoreCase(r4);	 Catch:{ all -> 0x022b }
        if (r4 != 0) goto L_0x0126;
    L_0x011c:
        r4 = "auth-conf";
        r0 = r20;
        r4 = r0.equalsIgnoreCase(r4);	 Catch:{ all -> 0x022b }
        if (r4 == 0) goto L_0x0255;
    L_0x0126:
        r0 = r30;
        r4 = r0.debug;	 Catch:{ all -> 0x022b }
        if (r4 == 0) goto L_0x0135;
    L_0x012c:
        r0 = r30;
        r4 = r0.out;	 Catch:{ all -> 0x022b }
        r5 = "IMAP SASL DEBUG: Mechanism requires integrity or confidentiality";
        r4.println(r5);	 Catch:{ all -> 0x022b }
    L_0x0135:
        monitor-exit(r29);	 Catch:{ all -> 0x022b }
        r4 = 0;
        goto L_0x0067;
    L_0x0139:
        r14 = move-exception;
        r0 = r30;
        r4 = r0.debug;	 Catch:{ all -> 0x022b }
        if (r4 == 0) goto L_0x0156;
    L_0x0140:
        r0 = r30;
        r4 = r0.out;	 Catch:{ all -> 0x022b }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x022b }
        r6 = "IMAP SASL DEBUG: AUTHENTICATE Exception: ";
        r5.<init>(r6);	 Catch:{ all -> 0x022b }
        r5 = r5.append(r14);	 Catch:{ all -> 0x022b }
        r5 = r5.toString();	 Catch:{ all -> 0x022b }
        r4.println(r5);	 Catch:{ all -> 0x022b }
    L_0x0156:
        monitor-exit(r29);	 Catch:{ all -> 0x022b }
        r4 = 0;
        goto L_0x0067;
    L_0x015a:
        r0 = r30;
        r4 = r0.pr;	 Catch:{ Exception -> 0x01cc }
        r21 = r4.readResponse();	 Catch:{ Exception -> 0x01cc }
        r4 = r21.isContinuation();	 Catch:{ Exception -> 0x01cc }
        if (r4 == 0) goto L_0x022e;
    L_0x0168:
        r11 = 0;
        r11 = (byte[]) r11;	 Catch:{ Exception -> 0x01cc }
        r4 = r24.isComplete();	 Catch:{ Exception -> 0x01cc }
        if (r4 != 0) goto L_0x01ae;
    L_0x0171:
        r4 = r21.readByteArray();	 Catch:{ Exception -> 0x01cc }
        r11 = r4.getNewBytes();	 Catch:{ Exception -> 0x01cc }
        r4 = r11.length;	 Catch:{ Exception -> 0x01cc }
        if (r4 <= 0) goto L_0x0180;
    L_0x017c:
        r11 = com.sun.mail.util.BASE64DecoderStream.decode(r11);	 Catch:{ Exception -> 0x01cc }
    L_0x0180:
        r0 = r30;
        r4 = r0.debug;	 Catch:{ Exception -> 0x01cc }
        if (r4 == 0) goto L_0x01a8;
    L_0x0186:
        r0 = r30;
        r4 = r0.out;	 Catch:{ Exception -> 0x01cc }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01cc }
        r6 = "IMAP SASL DEBUG: challenge: ";
        r5.<init>(r6);	 Catch:{ Exception -> 0x01cc }
        r6 = 0;
        r7 = r11.length;	 Catch:{ Exception -> 0x01cc }
        r6 = com.sun.mail.util.ASCIIUtility.toString(r11, r6, r7);	 Catch:{ Exception -> 0x01cc }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x01cc }
        r6 = " :";
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x01cc }
        r5 = r5.toString();	 Catch:{ Exception -> 0x01cc }
        r4.println(r5);	 Catch:{ Exception -> 0x01cc }
    L_0x01a8:
        r0 = r24;
        r11 = r0.evaluateChallenge(r11);	 Catch:{ Exception -> 0x01cc }
    L_0x01ae:
        if (r11 != 0) goto L_0x01dd;
    L_0x01b0:
        r0 = r30;
        r4 = r0.debug;	 Catch:{ Exception -> 0x01cc }
        if (r4 == 0) goto L_0x01bf;
    L_0x01b6:
        r0 = r30;
        r4 = r0.out;	 Catch:{ Exception -> 0x01cc }
        r5 = "IMAP SASL DEBUG: no response";
        r4.println(r5);	 Catch:{ Exception -> 0x01cc }
    L_0x01bf:
        r0 = r18;
        r0.write(r10);	 Catch:{ Exception -> 0x01cc }
        r18.flush();	 Catch:{ Exception -> 0x01cc }
        r12.reset();	 Catch:{ Exception -> 0x01cc }
        goto L_0x00fe;
    L_0x01cc:
        r16 = move-exception;
        r0 = r30;
        r4 = r0.debug;	 Catch:{ all -> 0x022b }
        if (r4 == 0) goto L_0x01d6;
    L_0x01d3:
        r16.printStackTrace();	 Catch:{ all -> 0x022b }
    L_0x01d6:
        r21 = com.sun.mail.iap.Response.byeResponse(r16);	 Catch:{ all -> 0x022b }
        r13 = 1;
        goto L_0x00fe;
    L_0x01dd:
        r0 = r30;
        r4 = r0.debug;	 Catch:{ Exception -> 0x01cc }
        if (r4 == 0) goto L_0x0205;
    L_0x01e3:
        r0 = r30;
        r4 = r0.out;	 Catch:{ Exception -> 0x01cc }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01cc }
        r6 = "IMAP SASL DEBUG: response: ";
        r5.<init>(r6);	 Catch:{ Exception -> 0x01cc }
        r6 = 0;
        r7 = r11.length;	 Catch:{ Exception -> 0x01cc }
        r6 = com.sun.mail.util.ASCIIUtility.toString(r11, r6, r7);	 Catch:{ Exception -> 0x01cc }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x01cc }
        r6 = " :";
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x01cc }
        r5 = r5.toString();	 Catch:{ Exception -> 0x01cc }
        r4.println(r5);	 Catch:{ Exception -> 0x01cc }
    L_0x0205:
        r11 = com.sun.mail.util.BASE64EncoderStream.encode(r11);	 Catch:{ Exception -> 0x01cc }
        if (r17 == 0) goto L_0x0214;
    L_0x020b:
        r4 = "XGWTRUSTEDAPP ";
        r4 = r4.getBytes();	 Catch:{ Exception -> 0x01cc }
        r12.write(r4);	 Catch:{ Exception -> 0x01cc }
    L_0x0214:
        r12.write(r11);	 Catch:{ Exception -> 0x01cc }
        r12.write(r10);	 Catch:{ Exception -> 0x01cc }
        r4 = r12.toByteArray();	 Catch:{ Exception -> 0x01cc }
        r0 = r18;
        r0.write(r4);	 Catch:{ Exception -> 0x01cc }
        r18.flush();	 Catch:{ Exception -> 0x01cc }
        r12.reset();	 Catch:{ Exception -> 0x01cc }
        goto L_0x00fe;
    L_0x022b:
        r4 = move-exception;
        monitor-exit(r29);	 Catch:{ all -> 0x022b }
        throw r4;
    L_0x022e:
        r4 = r21.isTagged();	 Catch:{ Exception -> 0x01cc }
        if (r4 == 0) goto L_0x0243;
    L_0x0234:
        r4 = r21.getTag();	 Catch:{ Exception -> 0x01cc }
        r0 = r26;
        r4 = r4.equals(r0);	 Catch:{ Exception -> 0x01cc }
        if (r4 == 0) goto L_0x0243;
    L_0x0240:
        r13 = 1;
        goto L_0x00fe;
    L_0x0243:
        r4 = r21.isBYE();	 Catch:{ Exception -> 0x01cc }
        if (r4 == 0) goto L_0x024c;
    L_0x0249:
        r13 = 1;
        goto L_0x00fe;
    L_0x024c:
        r0 = r28;
        r1 = r21;
        r0.addElement(r1);	 Catch:{ Exception -> 0x01cc }
        goto L_0x00fe;
    L_0x0255:
        r4 = r28.size();	 Catch:{ all -> 0x022b }
        r0 = new com.sun.mail.iap.Response[r4];	 Catch:{ all -> 0x022b }
        r23 = r0;
        r0 = r28;
        r1 = r23;
        r0.copyInto(r1);	 Catch:{ all -> 0x022b }
        r0 = r30;
        r4 = r0.pr;	 Catch:{ all -> 0x022b }
        r0 = r23;
        r4.notifyResponseHandlers(r0);	 Catch:{ all -> 0x022b }
        r0 = r30;
        r4 = r0.pr;	 Catch:{ all -> 0x022b }
        r0 = r21;
        r4.handleResult(r0);	 Catch:{ all -> 0x022b }
        r0 = r30;
        r4 = r0.pr;	 Catch:{ all -> 0x022b }
        r0 = r21;
        r4.setCapabilities(r0);	 Catch:{ all -> 0x022b }
        monitor-exit(r29);	 Catch:{ all -> 0x022b }
        r4 = 1;
        goto L_0x0067;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.protocol.IMAPSaslAuthenticator.authenticate(java.lang.String[], java.lang.String, java.lang.String, java.lang.String, java.lang.String):boolean");
    }
}
