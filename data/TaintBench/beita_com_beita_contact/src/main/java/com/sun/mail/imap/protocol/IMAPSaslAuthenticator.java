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
    /* JADX WARNING: Missing block: B:119:?, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:121:?, code skipped:
            return false;
     */
    public boolean authenticate(java.lang.String[] r32, java.lang.String r33, java.lang.String r34, java.lang.String r35, java.lang.String r36) throws com.sun.mail.iap.ProtocolException {
        /*
        r31 = this;
        r0 = r31;
        r0 = r0.pr;
        r30 = r0;
        monitor-enter(r30);
        r29 = new java.util.Vector;	 Catch:{ all -> 0x0258 }
        r29.<init>();	 Catch:{ all -> 0x0258 }
        r27 = 0;
        r22 = 0;
        r14 = 0;
        r0 = r31;
        r0 = r0.debug;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        if (r5 == 0) goto L_0x0035;
    L_0x0018:
        r0 = r31;
        r0 = r0.out;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        r6 = "IMAP SASL DEBUG: Mechanisms:";
        r5.print(r6);	 Catch:{ all -> 0x0258 }
        r16 = 0;
    L_0x0024:
        r0 = r32;
        r0 = r0.length;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        r0 = r16;
        r1 = r5;
        if (r0 < r1) goto L_0x0076;
    L_0x002d:
        r0 = r31;
        r0 = r0.out;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        r5.println();	 Catch:{ all -> 0x0258 }
    L_0x0035:
        r23 = r33;
        r28 = r35;
        r20 = r36;
        r10 = new com.sun.mail.imap.protocol.IMAPSaslAuthenticator$1;	 Catch:{ all -> 0x0258 }
        r0 = r10;
        r1 = r31;
        r2 = r28;
        r3 = r20;
        r4 = r23;
        r0.m277init(r2, r3, r4);	 Catch:{ all -> 0x0258 }
        r0 = r31;
        r0 = r0.name;	 Catch:{ SaslException -> 0x0092 }
        r7 = r0;
        r0 = r31;
        r0 = r0.host;	 Catch:{ SaslException -> 0x0092 }
        r8 = r0;
        r0 = r31;
        r0 = r0.props;	 Catch:{ SaslException -> 0x0092 }
        r9 = r0;
        r5 = r32;
        r6 = r34;
        r25 = javax.security.sasl.Sasl.createSaslClient(r5, r6, r7, r8, r9, r10);	 Catch:{ SaslException -> 0x0092 }
        if (r25 != 0) goto L_0x00b9;
    L_0x0062:
        r0 = r31;
        r0 = r0.debug;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        if (r5 == 0) goto L_0x0073;
    L_0x0069:
        r0 = r31;
        r0 = r0.out;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        r6 = "IMAP SASL DEBUG: No SASL support";
        r5.println(r6);	 Catch:{ all -> 0x0258 }
    L_0x0073:
        monitor-exit(r30);	 Catch:{ all -> 0x0258 }
        r5 = 0;
    L_0x0075:
        return r5;
    L_0x0076:
        r0 = r31;
        r0 = r0.out;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0258 }
        r7 = " ";
        r6.<init>(r7);	 Catch:{ all -> 0x0258 }
        r7 = r32[r16];	 Catch:{ all -> 0x0258 }
        r6 = r6.append(r7);	 Catch:{ all -> 0x0258 }
        r6 = r6.toString();	 Catch:{ all -> 0x0258 }
        r5.print(r6);	 Catch:{ all -> 0x0258 }
        r16 = r16 + 1;
        goto L_0x0024;
    L_0x0092:
        r5 = move-exception;
        r26 = r5;
        r0 = r31;
        r0 = r0.debug;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        if (r5 == 0) goto L_0x00b6;
    L_0x009c:
        r0 = r31;
        r0 = r0.out;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0258 }
        r7 = "IMAP SASL DEBUG: Failed to create SASL client: ";
        r6.<init>(r7);	 Catch:{ all -> 0x0258 }
        r0 = r6;
        r1 = r26;
        r6 = r0.append(r1);	 Catch:{ all -> 0x0258 }
        r6 = r6.toString();	 Catch:{ all -> 0x0258 }
        r5.println(r6);	 Catch:{ all -> 0x0258 }
    L_0x00b6:
        monitor-exit(r30);	 Catch:{ all -> 0x0258 }
        r5 = 0;
        goto L_0x0075;
    L_0x00b9:
        r0 = r31;
        r0 = r0.debug;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        if (r5 == 0) goto L_0x00db;
    L_0x00c0:
        r0 = r31;
        r0 = r0.out;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0258 }
        r7 = "IMAP SASL DEBUG: SASL client ";
        r6.<init>(r7);	 Catch:{ all -> 0x0258 }
        r7 = r25.getMechanismName();	 Catch:{ all -> 0x0258 }
        r6 = r6.append(r7);	 Catch:{ all -> 0x0258 }
        r6 = r6.toString();	 Catch:{ all -> 0x0258 }
        r5.println(r6);	 Catch:{ all -> 0x0258 }
    L_0x00db:
        r0 = r31;
        r0 = r0.pr;	 Catch:{ Exception -> 0x0156 }
        r5 = r0;
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0156 }
        r7 = "AUTHENTICATE ";
        r6.<init>(r7);	 Catch:{ Exception -> 0x0156 }
        r7 = r25.getMechanismName();	 Catch:{ Exception -> 0x0156 }
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x0156 }
        r6 = r6.toString();	 Catch:{ Exception -> 0x0156 }
        r7 = 0;
        r27 = r5.writeCommand(r6, r7);	 Catch:{ Exception -> 0x0156 }
        r0 = r31;
        r0 = r0.pr;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        r19 = r5.getIMAPOutputStream();	 Catch:{ all -> 0x0258 }
        r13 = new java.io.ByteArrayOutputStream;	 Catch:{ all -> 0x0258 }
        r13.<init>();	 Catch:{ all -> 0x0258 }
        r5 = 2;
        r11 = new byte[r5];	 Catch:{ all -> 0x0258 }
        r11 = {13, 10};	 Catch:{ all -> 0x0258 }
        r5 = r25.getMechanismName();	 Catch:{ all -> 0x0258 }
        r6 = "XGWTRUSTEDAPP";
        r18 = r5.equals(r6);	 Catch:{ all -> 0x0258 }
    L_0x0116:
        if (r14 == 0) goto L_0x017a;
    L_0x0118:
        r5 = r25.isComplete();	 Catch:{ all -> 0x0258 }
        if (r5 == 0) goto L_0x0283;
    L_0x011e:
        r5 = "javax.security.sasl.qop";
        r0 = r25;
        r1 = r5;
        r21 = r0.getNegotiatedProperty(r1);	 Catch:{ all -> 0x0258 }
        r21 = (java.lang.String) r21;	 Catch:{ all -> 0x0258 }
        if (r21 == 0) goto L_0x0283;
    L_0x012b:
        r5 = "auth-int";
        r0 = r21;
        r1 = r5;
        r5 = r0.equalsIgnoreCase(r1);	 Catch:{ all -> 0x0258 }
        if (r5 != 0) goto L_0x0141;
    L_0x0136:
        r5 = "auth-conf";
        r0 = r21;
        r1 = r5;
        r5 = r0.equalsIgnoreCase(r1);	 Catch:{ all -> 0x0258 }
        if (r5 == 0) goto L_0x0283;
    L_0x0141:
        r0 = r31;
        r0 = r0.debug;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        if (r5 == 0) goto L_0x0152;
    L_0x0148:
        r0 = r31;
        r0 = r0.out;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        r6 = "IMAP SASL DEBUG: Mechanism requires integrity or confidentiality";
        r5.println(r6);	 Catch:{ all -> 0x0258 }
    L_0x0152:
        monitor-exit(r30);	 Catch:{ all -> 0x0258 }
        r5 = 0;
        goto L_0x0075;
    L_0x0156:
        r5 = move-exception;
        r15 = r5;
        r0 = r31;
        r0 = r0.debug;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        if (r5 == 0) goto L_0x0176;
    L_0x015f:
        r0 = r31;
        r0 = r0.out;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0258 }
        r7 = "IMAP SASL DEBUG: AUTHENTICATE Exception: ";
        r6.<init>(r7);	 Catch:{ all -> 0x0258 }
        r6 = r6.append(r15);	 Catch:{ all -> 0x0258 }
        r6 = r6.toString();	 Catch:{ all -> 0x0258 }
        r5.println(r6);	 Catch:{ all -> 0x0258 }
    L_0x0176:
        monitor-exit(r30);	 Catch:{ all -> 0x0258 }
        r5 = 0;
        goto L_0x0075;
    L_0x017a:
        r0 = r31;
        r0 = r0.pr;	 Catch:{ Exception -> 0x01f3 }
        r5 = r0;
        r22 = r5.readResponse();	 Catch:{ Exception -> 0x01f3 }
        r5 = r22.isContinuation();	 Catch:{ Exception -> 0x01f3 }
        if (r5 == 0) goto L_0x025b;
    L_0x0189:
        r12 = 0;
        r12 = (byte[]) r12;	 Catch:{ Exception -> 0x01f3 }
        r5 = r25.isComplete();	 Catch:{ Exception -> 0x01f3 }
        if (r5 != 0) goto L_0x01d2;
    L_0x0192:
        r5 = r22.readByteArray();	 Catch:{ Exception -> 0x01f3 }
        r12 = r5.getNewBytes();	 Catch:{ Exception -> 0x01f3 }
        r5 = r12.length;	 Catch:{ Exception -> 0x01f3 }
        if (r5 <= 0) goto L_0x01a1;
    L_0x019d:
        r12 = com.sun.mail.util.BASE64DecoderStream.decode(r12);	 Catch:{ Exception -> 0x01f3 }
    L_0x01a1:
        r0 = r31;
        r0 = r0.debug;	 Catch:{ Exception -> 0x01f3 }
        r5 = r0;
        if (r5 == 0) goto L_0x01cb;
    L_0x01a8:
        r0 = r31;
        r0 = r0.out;	 Catch:{ Exception -> 0x01f3 }
        r5 = r0;
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01f3 }
        r7 = "IMAP SASL DEBUG: challenge: ";
        r6.<init>(r7);	 Catch:{ Exception -> 0x01f3 }
        r7 = 0;
        r8 = r12.length;	 Catch:{ Exception -> 0x01f3 }
        r7 = com.sun.mail.util.ASCIIUtility.toString(r12, r7, r8);	 Catch:{ Exception -> 0x01f3 }
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x01f3 }
        r7 = " :";
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x01f3 }
        r6 = r6.toString();	 Catch:{ Exception -> 0x01f3 }
        r5.println(r6);	 Catch:{ Exception -> 0x01f3 }
    L_0x01cb:
        r0 = r25;
        r1 = r12;
        r12 = r0.evaluateChallenge(r1);	 Catch:{ Exception -> 0x01f3 }
    L_0x01d2:
        if (r12 != 0) goto L_0x0207;
    L_0x01d4:
        r0 = r31;
        r0 = r0.debug;	 Catch:{ Exception -> 0x01f3 }
        r5 = r0;
        if (r5 == 0) goto L_0x01e5;
    L_0x01db:
        r0 = r31;
        r0 = r0.out;	 Catch:{ Exception -> 0x01f3 }
        r5 = r0;
        r6 = "IMAP SASL DEBUG: no response";
        r5.println(r6);	 Catch:{ Exception -> 0x01f3 }
    L_0x01e5:
        r0 = r19;
        r1 = r11;
        r0.write(r1);	 Catch:{ Exception -> 0x01f3 }
        r19.flush();	 Catch:{ Exception -> 0x01f3 }
        r13.reset();	 Catch:{ Exception -> 0x01f3 }
        goto L_0x0116;
    L_0x01f3:
        r5 = move-exception;
        r17 = r5;
        r0 = r31;
        r0 = r0.debug;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        if (r5 == 0) goto L_0x0200;
    L_0x01fd:
        r17.printStackTrace();	 Catch:{ all -> 0x0258 }
    L_0x0200:
        r22 = com.sun.mail.iap.Response.byeResponse(r17);	 Catch:{ all -> 0x0258 }
        r14 = 1;
        goto L_0x0116;
    L_0x0207:
        r0 = r31;
        r0 = r0.debug;	 Catch:{ Exception -> 0x01f3 }
        r5 = r0;
        if (r5 == 0) goto L_0x0231;
    L_0x020e:
        r0 = r31;
        r0 = r0.out;	 Catch:{ Exception -> 0x01f3 }
        r5 = r0;
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01f3 }
        r7 = "IMAP SASL DEBUG: response: ";
        r6.<init>(r7);	 Catch:{ Exception -> 0x01f3 }
        r7 = 0;
        r8 = r12.length;	 Catch:{ Exception -> 0x01f3 }
        r7 = com.sun.mail.util.ASCIIUtility.toString(r12, r7, r8);	 Catch:{ Exception -> 0x01f3 }
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x01f3 }
        r7 = " :";
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x01f3 }
        r6 = r6.toString();	 Catch:{ Exception -> 0x01f3 }
        r5.println(r6);	 Catch:{ Exception -> 0x01f3 }
    L_0x0231:
        r12 = com.sun.mail.util.BASE64EncoderStream.encode(r12);	 Catch:{ Exception -> 0x01f3 }
        if (r18 == 0) goto L_0x0240;
    L_0x0237:
        r5 = "XGWTRUSTEDAPP ";
        r5 = r5.getBytes();	 Catch:{ Exception -> 0x01f3 }
        r13.write(r5);	 Catch:{ Exception -> 0x01f3 }
    L_0x0240:
        r13.write(r12);	 Catch:{ Exception -> 0x01f3 }
        r13.write(r11);	 Catch:{ Exception -> 0x01f3 }
        r5 = r13.toByteArray();	 Catch:{ Exception -> 0x01f3 }
        r0 = r19;
        r1 = r5;
        r0.write(r1);	 Catch:{ Exception -> 0x01f3 }
        r19.flush();	 Catch:{ Exception -> 0x01f3 }
        r13.reset();	 Catch:{ Exception -> 0x01f3 }
        goto L_0x0116;
    L_0x0258:
        r5 = move-exception;
        monitor-exit(r30);	 Catch:{ all -> 0x0258 }
        throw r5;
    L_0x025b:
        r5 = r22.isTagged();	 Catch:{ Exception -> 0x01f3 }
        if (r5 == 0) goto L_0x0271;
    L_0x0261:
        r5 = r22.getTag();	 Catch:{ Exception -> 0x01f3 }
        r0 = r5;
        r1 = r27;
        r5 = r0.equals(r1);	 Catch:{ Exception -> 0x01f3 }
        if (r5 == 0) goto L_0x0271;
    L_0x026e:
        r14 = 1;
        goto L_0x0116;
    L_0x0271:
        r5 = r22.isBYE();	 Catch:{ Exception -> 0x01f3 }
        if (r5 == 0) goto L_0x027a;
    L_0x0277:
        r14 = 1;
        goto L_0x0116;
    L_0x027a:
        r0 = r29;
        r1 = r22;
        r0.addElement(r1);	 Catch:{ Exception -> 0x01f3 }
        goto L_0x0116;
    L_0x0283:
        r5 = r29.size();	 Catch:{ all -> 0x0258 }
        r0 = r5;
        r0 = new com.sun.mail.iap.Response[r0];	 Catch:{ all -> 0x0258 }
        r24 = r0;
        r0 = r29;
        r1 = r24;
        r0.copyInto(r1);	 Catch:{ all -> 0x0258 }
        r0 = r31;
        r0 = r0.pr;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        r0 = r5;
        r1 = r24;
        r0.notifyResponseHandlers(r1);	 Catch:{ all -> 0x0258 }
        r0 = r31;
        r0 = r0.pr;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        r0 = r5;
        r1 = r22;
        r0.handleResult(r1);	 Catch:{ all -> 0x0258 }
        r0 = r31;
        r0 = r0.pr;	 Catch:{ all -> 0x0258 }
        r5 = r0;
        r0 = r5;
        r1 = r22;
        r0.setCapabilities(r1);	 Catch:{ all -> 0x0258 }
        monitor-exit(r30);	 Catch:{ all -> 0x0258 }
        r5 = 1;
        goto L_0x0075;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.protocol.IMAPSaslAuthenticator.authenticate(java.lang.String[], java.lang.String, java.lang.String, java.lang.String, java.lang.String):boolean");
    }
}
