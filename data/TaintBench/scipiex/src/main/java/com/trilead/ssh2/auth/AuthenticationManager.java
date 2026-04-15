package com.trilead.ssh2.auth;

import com.trilead.ssh2.crypto.PEMDecoder;
import com.trilead.ssh2.packets.PacketServiceAccept;
import com.trilead.ssh2.packets.PacketServiceRequest;
import com.trilead.ssh2.packets.PacketUserauthBanner;
import com.trilead.ssh2.packets.PacketUserauthFailure;
import com.trilead.ssh2.packets.PacketUserauthRequestNone;
import com.trilead.ssh2.packets.PacketUserauthRequestPassword;
import com.trilead.ssh2.packets.PacketUserauthRequestPublicKey;
import com.trilead.ssh2.packets.TypesWriter;
import com.trilead.ssh2.signature.DSAPrivateKey;
import com.trilead.ssh2.signature.DSASHA1Verify;
import com.trilead.ssh2.signature.RSAPrivateKey;
import com.trilead.ssh2.signature.RSASHA1Verify;
import com.trilead.ssh2.transport.MessageHandler;
import com.trilead.ssh2.transport.TransportManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Vector;

public class AuthenticationManager implements MessageHandler {
    boolean authenticated = false;
    String banner;
    boolean connectionClosed = false;
    boolean initDone = false;
    boolean isPartialSuccess = false;
    Vector packets = new Vector();
    String[] remainingMethods = new String[0];
    TransportManager tm;

    public AuthenticationManager(TransportManager tm) {
        this.tm = tm;
    }

    /* access modifiers changed from: 0000 */
    public boolean methodPossible(String methName) {
        if (this.remainingMethods == null) {
            return false;
        }
        for (String compareTo : this.remainingMethods) {
            if (compareTo.compareTo(methName) == 0) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public byte[] deQueue() throws IOException {
        byte[] res;
        synchronized (this.packets) {
            while (this.packets.size() == 0) {
                if (this.connectionClosed) {
                    throw ((IOException) new IOException("The connection is closed.").initCause(this.tm.getReasonClosedCause()));
                }
                try {
                    this.packets.wait();
                } catch (InterruptedException e) {
                }
            }
            res = (byte[]) this.packets.firstElement();
            this.packets.removeElementAt(0);
        }
        return res;
    }

    /* access modifiers changed from: 0000 */
    public byte[] getNextMessage() throws IOException {
        while (true) {
            byte[] msg = deQueue();
            if (msg[0] != (byte) 53) {
                return msg;
            }
            this.banner = new PacketUserauthBanner(msg, 0, msg.length).getBanner();
        }
    }

    public String[] getRemainingMethods(String user) throws IOException {
        initialize(user);
        return this.remainingMethods;
    }

    public boolean getPartialSuccess() {
        return this.isPartialSuccess;
    }

    private boolean initialize(String user) throws IOException {
        if (this.initDone) {
            return this.authenticated;
        }
        this.tm.registerMessageHandler(this, 0, 255);
        this.tm.sendMessage(new PacketServiceRequest("ssh-userauth").getPayload());
        this.tm.sendMessage(new PacketUserauthRequestNone("ssh-connection", user).getPayload());
        byte[] msg = getNextMessage();
        PacketServiceAccept packetServiceAccept = new PacketServiceAccept(msg, 0, msg.length);
        msg = getNextMessage();
        this.initDone = true;
        if (msg[0] == (byte) 52) {
            this.authenticated = true;
            this.tm.removeMessageHandler(this, 0, 255);
            return true;
        } else if (msg[0] == (byte) 51) {
            PacketUserauthFailure puf = new PacketUserauthFailure(msg, 0, msg.length);
            this.remainingMethods = puf.getAuthThatCanContinue();
            this.isPartialSuccess = puf.isPartialSuccess();
            return false;
        } else {
            throw new IOException("Unexpected SSH message (type " + msg[0] + ")");
        }
    }

    public boolean authenticatePublicKey(String user, char[] PEMPrivateKey, String password, SecureRandom rnd) throws IOException {
        try {
            initialize(user);
            if (methodPossible("publickey")) {
                Object key = PEMDecoder.decode(PEMPrivateKey, password);
                byte[] pk_enc;
                TypesWriter tw;
                byte[] H;
                if (key instanceof DSAPrivateKey) {
                    DSAPrivateKey pk = (DSAPrivateKey) key;
                    pk_enc = DSASHA1Verify.encodeSSHDSAPublicKey(pk.getPublicKey());
                    tw = new TypesWriter();
                    H = this.tm.getSessionIdentifier();
                    tw.writeString(H, 0, H.length);
                    tw.writeByte(50);
                    tw.writeString(user);
                    tw.writeString("ssh-connection");
                    tw.writeString("publickey");
                    tw.writeBoolean(true);
                    tw.writeString("ssh-dss");
                    tw.writeString(pk_enc, 0, pk_enc.length);
                    String str = user;
                    this.tm.sendMessage(new PacketUserauthRequestPublicKey("ssh-connection", str, "ssh-dss", pk_enc, DSASHA1Verify.encodeSSHDSASignature(DSASHA1Verify.generateSignature(tw.getBytes(), pk, rnd))).getPayload());
                } else if (key instanceof RSAPrivateKey) {
                    RSAPrivateKey pk2 = (RSAPrivateKey) key;
                    pk_enc = RSASHA1Verify.encodeSSHRSAPublicKey(pk2.getPublicKey());
                    tw = new TypesWriter();
                    H = this.tm.getSessionIdentifier();
                    tw.writeString(H, 0, H.length);
                    tw.writeByte(50);
                    tw.writeString(user);
                    tw.writeString("ssh-connection");
                    tw.writeString("publickey");
                    tw.writeBoolean(true);
                    tw.writeString("ssh-rsa");
                    tw.writeString(pk_enc, 0, pk_enc.length);
                    String str2 = user;
                    this.tm.sendMessage(new PacketUserauthRequestPublicKey("ssh-connection", str2, "ssh-rsa", pk_enc, RSASHA1Verify.encodeSSHRSASignature(RSASHA1Verify.generateSignature(tw.getBytes(), pk2))).getPayload());
                } else {
                    throw new IOException("Unknown private key type returned by the PEM decoder.");
                }
                byte[] ar = getNextMessage();
                if (ar[0] == (byte) 52) {
                    this.authenticated = true;
                    this.tm.removeMessageHandler(this, 0, 255);
                    return true;
                } else if (ar[0] == (byte) 51) {
                    PacketUserauthFailure packetUserauthFailure = new PacketUserauthFailure(ar, 0, ar.length);
                    this.remainingMethods = packetUserauthFailure.getAuthThatCanContinue();
                    this.isPartialSuccess = packetUserauthFailure.isPartialSuccess();
                    return false;
                } else {
                    throw new IOException("Unexpected SSH message (type " + ar[0] + ")");
                }
            }
            throw new IOException("Authentication method publickey not supported by the server at this stage.");
        } catch (IOException e) {
            this.tm.close(e, false);
            throw ((IOException) new IOException("Publickey authentication failed.").initCause(e));
        }
    }

    public boolean authenticateNone(String user) throws IOException {
        try {
            initialize(user);
            return this.authenticated;
        } catch (IOException e) {
            this.tm.close(e, false);
            throw ((IOException) new IOException("None authentication failed.").initCause(e));
        }
    }

    public boolean authenticatePassword(String user, String pass) throws IOException {
        try {
            initialize(user);
            if (methodPossible("password")) {
                this.tm.sendMessage(new PacketUserauthRequestPassword("ssh-connection", user, pass).getPayload());
                byte[] ar = getNextMessage();
                if (ar[0] == (byte) 52) {
                    this.authenticated = true;
                    this.tm.removeMessageHandler(this, 0, 255);
                    return true;
                } else if (ar[0] == (byte) 51) {
                    PacketUserauthFailure puf = new PacketUserauthFailure(ar, 0, ar.length);
                    this.remainingMethods = puf.getAuthThatCanContinue();
                    this.isPartialSuccess = puf.isPartialSuccess();
                    return false;
                } else {
                    throw new IOException("Unexpected SSH message (type " + ar[0] + ")");
                }
            }
            throw new IOException("Authentication method password not supported by the server at this stage.");
        } catch (IOException e) {
            this.tm.close(e, false);
            throw ((IOException) new IOException("Password authentication failed.").initCause(e));
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    public boolean authenticateInteractive(java.lang.String r14, java.lang.String[] r15, com.trilead.ssh2.InteractiveCallback r16) throws java.io.IOException {
        /*
        r13 = this;
        r13.initialize(r14);	 Catch:{ IOException -> 0x0013 }
        r0 = "keyboard-interactive";
        r0 = r13.methodPossible(r0);	 Catch:{ IOException -> 0x0013 }
        if (r0 != 0) goto L_0x0028;
    L_0x000b:
        r0 = new java.io.IOException;	 Catch:{ IOException -> 0x0013 }
        r1 = "Authentication method keyboard-interactive not supported by the server at this stage.";
        r0.<init>(r1);	 Catch:{ IOException -> 0x0013 }
        throw r0;	 Catch:{ IOException -> 0x0013 }
    L_0x0013:
        r7 = move-exception;
        r0 = r13.tm;
        r1 = 0;
        r0.close(r7, r1);
        r0 = new java.io.IOException;
        r1 = "Keyboard-interactive authentication failed.";
        r0.<init>(r1);
        r0 = r0.initCause(r7);
        r0 = (java.io.IOException) r0;
        throw r0;
    L_0x0028:
        if (r15 != 0) goto L_0x002d;
    L_0x002a:
        r0 = 0;
        r15 = new java.lang.String[r0];	 Catch:{ IOException -> 0x0013 }
    L_0x002d:
        r12 = new com.trilead.ssh2.packets.PacketUserauthRequestInteractive;	 Catch:{ IOException -> 0x0013 }
        r0 = "ssh-connection";
        r12.m101init(r0, r14, r15);	 Catch:{ IOException -> 0x0013 }
        r0 = r13.tm;	 Catch:{ IOException -> 0x0013 }
        r1 = r12.getPayload();	 Catch:{ IOException -> 0x0013 }
        r0.sendMessage(r1);	 Catch:{ IOException -> 0x0013 }
    L_0x003d:
        r6 = r13.getNextMessage();	 Catch:{ IOException -> 0x0013 }
        r0 = 0;
        r0 = r6[r0];	 Catch:{ IOException -> 0x0013 }
        r1 = 52;
        if (r0 != r1) goto L_0x0055;
    L_0x0048:
        r0 = 1;
        r13.authenticated = r0;	 Catch:{ IOException -> 0x0013 }
        r0 = r13.tm;	 Catch:{ IOException -> 0x0013 }
        r1 = 0;
        r2 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r0.removeMessageHandler(r13, r1, r2);	 Catch:{ IOException -> 0x0013 }
        r0 = 1;
    L_0x0054:
        return r0;
    L_0x0055:
        r0 = 0;
        r0 = r6[r0];	 Catch:{ IOException -> 0x0013 }
        r1 = 51;
        if (r0 != r1) goto L_0x0071;
    L_0x005c:
        r8 = new com.trilead.ssh2.packets.PacketUserauthFailure;	 Catch:{ IOException -> 0x0013 }
        r0 = 0;
        r1 = r6.length;	 Catch:{ IOException -> 0x0013 }
        r8.m97init(r6, r0, r1);	 Catch:{ IOException -> 0x0013 }
        r0 = r8.getAuthThatCanContinue();	 Catch:{ IOException -> 0x0013 }
        r13.remainingMethods = r0;	 Catch:{ IOException -> 0x0013 }
        r0 = r8.isPartialSuccess();	 Catch:{ IOException -> 0x0013 }
        r13.isPartialSuccess = r0;	 Catch:{ IOException -> 0x0013 }
        r0 = 0;
        goto L_0x0054;
    L_0x0071:
        r0 = 0;
        r0 = r6[r0];	 Catch:{ IOException -> 0x0013 }
        r1 = 60;
        if (r0 != r1) goto L_0x00c2;
    L_0x0078:
        r9 = new com.trilead.ssh2.packets.PacketUserauthInfoRequest;	 Catch:{ IOException -> 0x0013 }
        r0 = 0;
        r1 = r6.length;	 Catch:{ IOException -> 0x0013 }
        r9.m99init(r6, r0, r1);	 Catch:{ IOException -> 0x0013 }
        r1 = r9.getName();	 Catch:{ Exception -> 0x00a3 }
        r2 = r9.getInstruction();	 Catch:{ Exception -> 0x00a3 }
        r3 = r9.getNumPrompts();	 Catch:{ Exception -> 0x00a3 }
        r4 = r9.getPrompt();	 Catch:{ Exception -> 0x00a3 }
        r5 = r9.getEcho();	 Catch:{ Exception -> 0x00a3 }
        r0 = r16;
        r11 = r0.replyToChallenge(r1, r2, r3, r4, r5);	 Catch:{ Exception -> 0x00a3 }
        if (r11 != 0) goto L_0x00b2;
    L_0x009b:
        r0 = new java.io.IOException;	 Catch:{ IOException -> 0x0013 }
        r1 = "Your callback may not return NULL!";
        r0.<init>(r1);	 Catch:{ IOException -> 0x0013 }
        throw r0;	 Catch:{ IOException -> 0x0013 }
    L_0x00a3:
        r7 = move-exception;
        r0 = new java.io.IOException;	 Catch:{ IOException -> 0x0013 }
        r1 = "Exception in callback.";
        r0.<init>(r1);	 Catch:{ IOException -> 0x0013 }
        r0 = r0.initCause(r7);	 Catch:{ IOException -> 0x0013 }
        r0 = (java.io.IOException) r0;	 Catch:{ IOException -> 0x0013 }
        throw r0;	 Catch:{ IOException -> 0x0013 }
    L_0x00b2:
        r10 = new com.trilead.ssh2.packets.PacketUserauthInfoResponse;	 Catch:{ IOException -> 0x0013 }
        r10.m100init(r11);	 Catch:{ IOException -> 0x0013 }
        r0 = r13.tm;	 Catch:{ IOException -> 0x0013 }
        r1 = r10.getPayload();	 Catch:{ IOException -> 0x0013 }
        r0.sendMessage(r1);	 Catch:{ IOException -> 0x0013 }
        goto L_0x003d;
    L_0x00c2:
        r0 = new java.io.IOException;	 Catch:{ IOException -> 0x0013 }
        r1 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0013 }
        r2 = "Unexpected SSH message (type ";
        r1.<init>(r2);	 Catch:{ IOException -> 0x0013 }
        r2 = 0;
        r2 = r6[r2];	 Catch:{ IOException -> 0x0013 }
        r1 = r1.append(r2);	 Catch:{ IOException -> 0x0013 }
        r2 = ")";
        r1 = r1.append(r2);	 Catch:{ IOException -> 0x0013 }
        r1 = r1.toString();	 Catch:{ IOException -> 0x0013 }
        r0.<init>(r1);	 Catch:{ IOException -> 0x0013 }
        throw r0;	 Catch:{ IOException -> 0x0013 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trilead.ssh2.auth.AuthenticationManager.authenticateInteractive(java.lang.String, java.lang.String[], com.trilead.ssh2.InteractiveCallback):boolean");
    }

    public void handleMessage(byte[] msg, int msglen) throws IOException {
        synchronized (this.packets) {
            if (msg == null) {
                this.connectionClosed = true;
            } else {
                byte[] tmp = new byte[msglen];
                System.arraycopy(msg, 0, tmp, 0, msglen);
                this.packets.addElement(tmp);
            }
            this.packets.notifyAll();
            if (this.packets.size() > 5) {
                this.connectionClosed = true;
                throw new IOException("Error, peer is flooding us with authentication packets.");
            }
        }
    }
}
