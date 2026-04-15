package com.trilead.ssh2.transport;

import com.trilead.ssh2.ConnectionInfo;
import com.trilead.ssh2.DHGexParameters;
import com.trilead.ssh2.ServerHostKeyVerifier;
import com.trilead.ssh2.crypto.CryptoWishList;
import com.trilead.ssh2.crypto.KeyMaterial;
import com.trilead.ssh2.crypto.cipher.BlockCipherFactory;
import com.trilead.ssh2.crypto.digest.MAC;
import com.trilead.ssh2.log.Logger;
import com.trilead.ssh2.packets.PacketKexInit;
import com.trilead.ssh2.packets.PacketNewKeys;
import com.trilead.ssh2.signature.DSAPublicKey;
import com.trilead.ssh2.signature.DSASHA1Verify;
import com.trilead.ssh2.signature.DSASignature;
import com.trilead.ssh2.signature.RSAPublicKey;
import com.trilead.ssh2.signature.RSASHA1Verify;
import com.trilead.ssh2.signature.RSASignature;
import java.io.IOException;
import java.security.SecureRandom;

public class KexManager {
    private static final Logger log = Logger.getLogger(KexManager.class);
    final Object accessLock = new Object();
    boolean connectionClosed = false;
    ClientServerHello csh;
    final String hostname;
    boolean ignore_next_kex_packet = false;
    int kexCount = 0;
    KeyMaterial km;
    KexState kxs;
    ConnectionInfo lastConnInfo = null;
    CryptoWishList nextKEXcryptoWishList;
    DHGexParameters nextKEXdhgexParameters;
    final int port;
    final SecureRandom rnd;
    byte[] sessionId;
    final TransportManager tm;
    ServerHostKeyVerifier verifier;

    public KexManager(TransportManager tm, ClientServerHello csh, CryptoWishList initialCwl, String hostname, int port, ServerHostKeyVerifier keyVerifier, SecureRandom rnd) {
        this.tm = tm;
        this.csh = csh;
        this.nextKEXcryptoWishList = initialCwl;
        this.nextKEXdhgexParameters = new DHGexParameters();
        this.hostname = hostname;
        this.port = port;
        this.verifier = keyVerifier;
        this.rnd = rnd;
    }

    public ConnectionInfo getOrWaitForConnectionInfo(int minKexCount) throws IOException {
        ConnectionInfo connectionInfo;
        synchronized (this.accessLock) {
            while (true) {
                if (this.lastConnInfo != null && this.lastConnInfo.keyExchangeCounter >= minKexCount) {
                    connectionInfo = this.lastConnInfo;
                } else if (this.connectionClosed) {
                    throw ((IOException) new IOException("Key exchange was not finished, connection is closed.").initCause(this.tm.getReasonClosedCause()));
                } else {
                    try {
                        this.accessLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
        return connectionInfo;
    }

    private String getFirstMatch(String[] client, String[] server) throws NegotiateException {
        if (client == null || server == null) {
            throw new IllegalArgumentException();
        } else if (client.length == 0) {
            return null;
        } else {
            for (int i = 0; i < client.length; i++) {
                for (Object equals : server) {
                    if (client[i].equals(equals)) {
                        return client[i];
                    }
                }
            }
            throw new NegotiateException();
        }
    }

    private boolean compareFirstOfNameList(String[] a, String[] b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException();
        } else if (a.length == 0 && b.length == 0) {
            return true;
        } else {
            if (a.length == 0 || b.length == 0) {
                return false;
            }
            return a[0].equals(b[0]);
        }
    }

    private boolean isGuessOK(KexParameters cpar, KexParameters spar) {
        if (cpar == null || spar == null) {
            throw new IllegalArgumentException();
        } else if (compareFirstOfNameList(cpar.kex_algorithms, spar.kex_algorithms) && compareFirstOfNameList(cpar.server_host_key_algorithms, spar.server_host_key_algorithms)) {
            return true;
        } else {
            return false;
        }
    }

    private NegotiatedParameters mergeKexParameters(KexParameters client, KexParameters server) {
        NegotiatedParameters np = new NegotiatedParameters();
        try {
            np.kex_algo = getFirstMatch(client.kex_algorithms, server.kex_algorithms);
            log.log(20, "kex_algo=" + np.kex_algo);
            np.server_host_key_algo = getFirstMatch(client.server_host_key_algorithms, server.server_host_key_algorithms);
            log.log(20, "server_host_key_algo=" + np.server_host_key_algo);
            np.enc_algo_client_to_server = getFirstMatch(client.encryption_algorithms_client_to_server, server.encryption_algorithms_client_to_server);
            np.enc_algo_server_to_client = getFirstMatch(client.encryption_algorithms_server_to_client, server.encryption_algorithms_server_to_client);
            log.log(20, "enc_algo_client_to_server=" + np.enc_algo_client_to_server);
            log.log(20, "enc_algo_server_to_client=" + np.enc_algo_server_to_client);
            np.mac_algo_client_to_server = getFirstMatch(client.mac_algorithms_client_to_server, server.mac_algorithms_client_to_server);
            np.mac_algo_server_to_client = getFirstMatch(client.mac_algorithms_server_to_client, server.mac_algorithms_server_to_client);
            log.log(20, "mac_algo_client_to_server=" + np.mac_algo_client_to_server);
            log.log(20, "mac_algo_server_to_client=" + np.mac_algo_server_to_client);
            np.comp_algo_client_to_server = getFirstMatch(client.compression_algorithms_client_to_server, server.compression_algorithms_client_to_server);
            np.comp_algo_server_to_client = getFirstMatch(client.compression_algorithms_server_to_client, server.compression_algorithms_server_to_client);
            log.log(20, "comp_algo_client_to_server=" + np.comp_algo_client_to_server);
            log.log(20, "comp_algo_server_to_client=" + np.comp_algo_server_to_client);
            try {
                np.lang_client_to_server = getFirstMatch(client.languages_client_to_server, server.languages_client_to_server);
            } catch (NegotiateException e) {
                np.lang_client_to_server = null;
            }
            try {
                np.lang_server_to_client = getFirstMatch(client.languages_server_to_client, server.languages_server_to_client);
            } catch (NegotiateException e2) {
                np.lang_server_to_client = null;
            }
            if (!isGuessOK(client, server)) {
                return np;
            }
            np.guessOK = true;
            return np;
        } catch (NegotiateException e3) {
            return null;
        }
    }

    public synchronized void initiateKEX(CryptoWishList cwl, DHGexParameters dhgex) throws IOException {
        this.nextKEXcryptoWishList = cwl;
        this.nextKEXdhgexParameters = dhgex;
        if (this.kxs == null) {
            this.kxs = new KexState();
            this.kxs.dhgexParameters = this.nextKEXdhgexParameters;
            PacketKexInit kp = new PacketKexInit(this.nextKEXcryptoWishList, this.rnd);
            this.kxs.localKEX = kp;
            this.tm.sendKexMessage(kp.getPayload());
        }
    }

    private boolean establishKeyMaterial() {
        try {
            int mac_cs_key_len = MAC.getKeyLen(this.kxs.np.mac_algo_client_to_server);
            int enc_cs_key_len = BlockCipherFactory.getKeySize(this.kxs.np.enc_algo_client_to_server);
            int enc_cs_block_len = BlockCipherFactory.getBlockSize(this.kxs.np.enc_algo_client_to_server);
            int mac_sc_key_len = MAC.getKeyLen(this.kxs.np.mac_algo_server_to_client);
            this.km = KeyMaterial.create("SHA1", this.kxs.H, this.kxs.K, this.sessionId, enc_cs_key_len, enc_cs_block_len, mac_cs_key_len, BlockCipherFactory.getKeySize(this.kxs.np.enc_algo_server_to_client), BlockCipherFactory.getBlockSize(this.kxs.np.enc_algo_server_to_client), mac_sc_key_len);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void finishKex() throws IOException {
        if (this.sessionId == null) {
            this.sessionId = this.kxs.H;
        }
        establishKeyMaterial();
        this.tm.sendKexMessage(new PacketNewKeys().getPayload());
        try {
            this.tm.changeSendCipher(BlockCipherFactory.createCipher(this.kxs.np.enc_algo_client_to_server, true, this.km.enc_key_client_to_server, this.km.initial_iv_client_to_server), new MAC(this.kxs.np.mac_algo_client_to_server, this.km.integrity_key_client_to_server));
            this.tm.kexFinished();
        } catch (IllegalArgumentException e) {
            throw new IOException("Fatal error during MAC startup!");
        }
    }

    public static final String[] getDefaultServerHostkeyAlgorithmList() {
        return new String[]{"ssh-rsa", "ssh-dss"};
    }

    public static final void checkServerHostkeyAlgorithmsList(String[] algos) {
        int i = 0;
        while (i < algos.length) {
            if ("ssh-rsa".equals(algos[i]) || "ssh-dss".equals(algos[i])) {
                i++;
            } else {
                throw new IllegalArgumentException("Unknown server host key algorithm '" + algos[i] + "'");
            }
        }
    }

    public static final String[] getDefaultKexAlgorithmList() {
        return new String[]{"diffie-hellman-group-exchange-sha1", "diffie-hellman-group14-sha1", "diffie-hellman-group1-sha1"};
    }

    public static final void checkKexAlgorithmList(String[] algos) {
        int i = 0;
        while (i < algos.length) {
            if ("diffie-hellman-group-exchange-sha1".equals(algos[i]) || "diffie-hellman-group14-sha1".equals(algos[i]) || "diffie-hellman-group1-sha1".equals(algos[i])) {
                i++;
            } else {
                throw new IllegalArgumentException("Unknown kex algorithm '" + algos[i] + "'");
            }
        }
    }

    private boolean verifySignature(byte[] sig, byte[] hostkey) throws IOException {
        if (this.kxs.np.server_host_key_algo.equals("ssh-rsa")) {
            RSASignature rs = RSASHA1Verify.decodeSSHRSASignature(sig);
            RSAPublicKey rpk = RSASHA1Verify.decodeSSHRSAPublicKey(hostkey);
            log.log(50, "Verifying ssh-rsa signature");
            return RSASHA1Verify.verifySignature(this.kxs.H, rs, rpk);
        } else if (this.kxs.np.server_host_key_algo.equals("ssh-dss")) {
            DSASignature ds = DSASHA1Verify.decodeSSHDSASignature(sig);
            DSAPublicKey dpk = DSASHA1Verify.decodeSSHDSAPublicKey(hostkey);
            log.log(50, "Verifying ssh-dss signature");
            return DSASHA1Verify.verifySignature(this.kxs.H, ds, dpk);
        } else {
            throw new IOException("Unknown server host key algorithm '" + this.kxs.np.server_host_key_algo + "'");
        }
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:73:0x01f8, B:108:0x035f, B:118:0x03a5, B:140:0x047b, B:150:0x04c1] */
    /* JADX WARNING: Missing block: B:86:0x02ba, code skipped:
            throw new java.io.IOException("Fatal error during MAC startup!");
     */
    /* JADX WARNING: Missing block: B:114:0x0387, code skipped:
            r16 = move-exception;
     */
    /* JADX WARNING: Missing block: B:116:0x0397, code skipped:
            throw ((java.io.IOException) new java.io.IOException("The server hostkey was not accepted by the verifier callback.").initCause(r16));
     */
    /* JADX WARNING: Missing block: B:125:0x0401, code skipped:
            r16 = move-exception;
     */
    /* JADX WARNING: Missing block: B:127:0x0411, code skipped:
            throw ((java.io.IOException) new java.io.IOException("KEX error.").initCause(r16));
     */
    /* JADX WARNING: Missing block: B:146:0x04a3, code skipped:
            r16 = move-exception;
     */
    /* JADX WARNING: Missing block: B:148:0x04b3, code skipped:
            throw ((java.io.IOException) new java.io.IOException("The server hostkey was not accepted by the verifier callback.").initCause(r16));
     */
    /* JADX WARNING: Missing block: B:157:0x0513, code skipped:
            r16 = move-exception;
     */
    /* JADX WARNING: Missing block: B:159:0x0523, code skipped:
            throw ((java.io.IOException) new java.io.IOException("KEX error.").initCause(r16));
     */
    public synchronized void handleMessage(byte[] r26, int r27) throws java.io.IOException {
        /*
        r25 = this;
        monitor-enter(r25);
        if (r26 != 0) goto L_0x001d;
    L_0x0003:
        r0 = r25;
        r4 = r0.accessLock;	 Catch:{ all -> 0x001a }
        monitor-enter(r4);	 Catch:{ all -> 0x001a }
        r3 = 1;
        r0 = r25;
        r0.connectionClosed = r3;	 Catch:{ all -> 0x0017 }
        r0 = r25;
        r3 = r0.accessLock;	 Catch:{ all -> 0x0017 }
        r3.notifyAll();	 Catch:{ all -> 0x0017 }
        monitor-exit(r4);	 Catch:{ all -> 0x0017 }
    L_0x0015:
        monitor-exit(r25);
        return;
    L_0x0017:
        r3 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0017 }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x001a:
        r3 = move-exception;
        monitor-exit(r25);
        throw r3;
    L_0x001d:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        if (r3 != 0) goto L_0x0048;
    L_0x0023:
        r3 = 0;
        r3 = r26[r3];	 Catch:{ all -> 0x001a }
        r4 = 20;
        if (r3 == r4) goto L_0x0048;
    L_0x002a:
        r3 = new java.io.IOException;	 Catch:{ all -> 0x001a }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x001a }
        r5 = "Unexpected KEX message (type ";
        r4.<init>(r5);	 Catch:{ all -> 0x001a }
        r5 = 0;
        r5 = r26[r5];	 Catch:{ all -> 0x001a }
        r4 = r4.append(r5);	 Catch:{ all -> 0x001a }
        r5 = ")";
        r4 = r4.append(r5);	 Catch:{ all -> 0x001a }
        r4 = r4.toString();	 Catch:{ all -> 0x001a }
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x0048:
        r0 = r25;
        r3 = r0.ignore_next_kex_packet;	 Catch:{ all -> 0x001a }
        if (r3 == 0) goto L_0x0054;
    L_0x004e:
        r3 = 0;
        r0 = r25;
        r0.ignore_next_kex_packet = r3;	 Catch:{ all -> 0x001a }
        goto L_0x0015;
    L_0x0054:
        r3 = 0;
        r3 = r26[r3];	 Catch:{ all -> 0x001a }
        r4 = 20;
        if (r3 != r4) goto L_0x01e3;
    L_0x005b:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        if (r3 == 0) goto L_0x0071;
    L_0x0061:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.state;	 Catch:{ all -> 0x001a }
        if (r3 == 0) goto L_0x0071;
    L_0x0069:
        r3 = new java.io.IOException;	 Catch:{ all -> 0x001a }
        r4 = "Unexpected SSH_MSG_KEXINIT message during on-going kex exchange!";
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x0071:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        if (r3 != 0) goto L_0x00ac;
    L_0x0077:
        r3 = new com.trilead.ssh2.transport.KexState;	 Catch:{ all -> 0x001a }
        r3.m137init();	 Catch:{ all -> 0x001a }
        r0 = r25;
        r0.kxs = r3;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r4 = r0.nextKEXdhgexParameters;	 Catch:{ all -> 0x001a }
        r3.dhgexParameters = r4;	 Catch:{ all -> 0x001a }
        r18 = new com.trilead.ssh2.packets.PacketKexInit;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.nextKEXcryptoWishList;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r4 = r0.rnd;	 Catch:{ all -> 0x001a }
        r0 = r18;
        r0.m79init(r3, r4);	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r0 = r18;
        r3.localKEX = r0;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.tm;	 Catch:{ all -> 0x001a }
        r4 = r18.getPayload();	 Catch:{ all -> 0x001a }
        r3.sendKexMessage(r4);	 Catch:{ all -> 0x001a }
    L_0x00ac:
        r18 = new com.trilead.ssh2.packets.PacketKexInit;	 Catch:{ all -> 0x001a }
        r3 = 0;
        r0 = r18;
        r1 = r26;
        r2 = r27;
        r0.m80init(r1, r3, r2);	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r0 = r18;
        r3.remoteKEX = r0;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r4 = r0.kxs;	 Catch:{ all -> 0x001a }
        r4 = r4.localKEX;	 Catch:{ all -> 0x001a }
        r4 = r4.getKexParameters();	 Catch:{ all -> 0x001a }
        r0 = r25;
        r5 = r0.kxs;	 Catch:{ all -> 0x001a }
        r5 = r5.remoteKEX;	 Catch:{ all -> 0x001a }
        r5 = r5.getKexParameters();	 Catch:{ all -> 0x001a }
        r0 = r25;
        r4 = r0.mergeKexParameters(r4, r5);	 Catch:{ all -> 0x001a }
        r3.np = r4;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.np;	 Catch:{ all -> 0x001a }
        if (r3 != 0) goto L_0x00f0;
    L_0x00e8:
        r3 = new java.io.IOException;	 Catch:{ all -> 0x001a }
        r4 = "Cannot negotiate, proposals do not match.";
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x00f0:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.remoteKEX;	 Catch:{ all -> 0x001a }
        r3 = r3.isFirst_kex_packet_follows();	 Catch:{ all -> 0x001a }
        if (r3 == 0) goto L_0x010b;
    L_0x00fc:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.np;	 Catch:{ all -> 0x001a }
        r3 = r3.guessOK;	 Catch:{ all -> 0x001a }
        if (r3 != 0) goto L_0x010b;
    L_0x0106:
        r3 = 1;
        r0 = r25;
        r0.ignore_next_kex_packet = r3;	 Catch:{ all -> 0x001a }
    L_0x010b:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.np;	 Catch:{ all -> 0x001a }
        r3 = r3.kex_algo;	 Catch:{ all -> 0x001a }
        r4 = "diffie-hellman-group-exchange-sha1";
        r3 = r3.equals(r4);	 Catch:{ all -> 0x001a }
        if (r3 == 0) goto L_0x015d;
    L_0x011b:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.dhgexParameters;	 Catch:{ all -> 0x001a }
        r3 = r3.getMin_group_len();	 Catch:{ all -> 0x001a }
        if (r3 != 0) goto L_0x0146;
    L_0x0127:
        r13 = new com.trilead.ssh2.packets.PacketKexDhGexRequestOld;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.dhgexParameters;	 Catch:{ all -> 0x001a }
        r13.m78init(r3);	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.tm;	 Catch:{ all -> 0x001a }
        r4 = r13.getPayload();	 Catch:{ all -> 0x001a }
        r3.sendKexMessage(r4);	 Catch:{ all -> 0x001a }
    L_0x013d:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r4 = 1;
        r3.state = r4;	 Catch:{ all -> 0x001a }
        goto L_0x0015;
    L_0x0146:
        r13 = new com.trilead.ssh2.packets.PacketKexDhGexRequest;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.dhgexParameters;	 Catch:{ all -> 0x001a }
        r13.m77init(r3);	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.tm;	 Catch:{ all -> 0x001a }
        r4 = r13.getPayload();	 Catch:{ all -> 0x001a }
        r3.sendKexMessage(r4);	 Catch:{ all -> 0x001a }
        goto L_0x013d;
    L_0x015d:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.np;	 Catch:{ all -> 0x001a }
        r3 = r3.kex_algo;	 Catch:{ all -> 0x001a }
        r4 = "diffie-hellman-group1-sha1";
        r3 = r3.equals(r4);	 Catch:{ all -> 0x001a }
        if (r3 != 0) goto L_0x017d;
    L_0x016d:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.np;	 Catch:{ all -> 0x001a }
        r3 = r3.kex_algo;	 Catch:{ all -> 0x001a }
        r4 = "diffie-hellman-group14-sha1";
        r3 = r3.equals(r4);	 Catch:{ all -> 0x001a }
        if (r3 == 0) goto L_0x01db;
    L_0x017d:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r4 = new com.trilead.ssh2.crypto.dh.DhExchange;	 Catch:{ all -> 0x001a }
        r4.m51init();	 Catch:{ all -> 0x001a }
        r3.dhx = r4;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.np;	 Catch:{ all -> 0x001a }
        r3 = r3.kex_algo;	 Catch:{ all -> 0x001a }
        r4 = "diffie-hellman-group1-sha1";
        r3 = r3.equals(r4);	 Catch:{ all -> 0x001a }
        if (r3 == 0) goto L_0x01cb;
    L_0x0198:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.dhx;	 Catch:{ all -> 0x001a }
        r4 = 1;
        r0 = r25;
        r5 = r0.rnd;	 Catch:{ all -> 0x001a }
        r3.init(r4, r5);	 Catch:{ all -> 0x001a }
    L_0x01a6:
        r19 = new com.trilead.ssh2.packets.PacketKexDHInit;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.dhx;	 Catch:{ all -> 0x001a }
        r3 = r3.getE();	 Catch:{ all -> 0x001a }
        r0 = r19;
        r0.m72init(r3);	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.tm;	 Catch:{ all -> 0x001a }
        r4 = r19.getPayload();	 Catch:{ all -> 0x001a }
        r3.sendKexMessage(r4);	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r4 = 1;
        r3.state = r4;	 Catch:{ all -> 0x001a }
        goto L_0x0015;
    L_0x01cb:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.dhx;	 Catch:{ all -> 0x001a }
        r4 = 14;
        r0 = r25;
        r5 = r0.rnd;	 Catch:{ all -> 0x001a }
        r3.init(r4, r5);	 Catch:{ all -> 0x001a }
        goto L_0x01a6;
    L_0x01db:
        r3 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x001a }
        r4 = "Unkown KEX method!";
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x01e3:
        r3 = 0;
        r3 = r26[r3];	 Catch:{ all -> 0x001a }
        r4 = 21;
        if (r3 != r4) goto L_0x02be;
    L_0x01ea:
        r0 = r25;
        r3 = r0.km;	 Catch:{ all -> 0x001a }
        if (r3 != 0) goto L_0x01f8;
    L_0x01f0:
        r3 = new java.io.IOException;	 Catch:{ all -> 0x001a }
        r4 = "Peer sent SSH_MSG_NEWKEYS, but I have no key material ready!";
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x01f8:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ IllegalArgumentException -> 0x02b2 }
        r3 = r3.np;	 Catch:{ IllegalArgumentException -> 0x02b2 }
        r3 = r3.enc_algo_server_to_client;	 Catch:{ IllegalArgumentException -> 0x02b2 }
        r4 = 0;
        r0 = r25;
        r5 = r0.km;	 Catch:{ IllegalArgumentException -> 0x02b2 }
        r5 = r5.enc_key_server_to_client;	 Catch:{ IllegalArgumentException -> 0x02b2 }
        r0 = r25;
        r6 = r0.km;	 Catch:{ IllegalArgumentException -> 0x02b2 }
        r6 = r6.initial_iv_server_to_client;	 Catch:{ IllegalArgumentException -> 0x02b2 }
        r10 = com.trilead.ssh2.crypto.cipher.BlockCipherFactory.createCipher(r3, r4, r5, r6);	 Catch:{ IllegalArgumentException -> 0x02b2 }
        r20 = new com.trilead.ssh2.crypto.digest.MAC;	 Catch:{ IllegalArgumentException -> 0x02b2 }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ IllegalArgumentException -> 0x02b2 }
        r3 = r3.np;	 Catch:{ IllegalArgumentException -> 0x02b2 }
        r3 = r3.mac_algo_server_to_client;	 Catch:{ IllegalArgumentException -> 0x02b2 }
        r0 = r25;
        r4 = r0.km;	 Catch:{ IllegalArgumentException -> 0x02b2 }
        r4 = r4.integrity_key_server_to_client;	 Catch:{ IllegalArgumentException -> 0x02b2 }
        r0 = r20;
        r0.m55init(r3, r4);	 Catch:{ IllegalArgumentException -> 0x02b2 }
        r0 = r25;
        r3 = r0.tm;	 Catch:{ all -> 0x001a }
        r0 = r20;
        r3.changeRecvCipher(r10, r0);	 Catch:{ all -> 0x001a }
        r22 = new com.trilead.ssh2.ConnectionInfo;	 Catch:{ all -> 0x001a }
        r22.m4init();	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kexCount;	 Catch:{ all -> 0x001a }
        r3 = r3 + 1;
        r0 = r25;
        r0.kexCount = r3;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.np;	 Catch:{ all -> 0x001a }
        r3 = r3.kex_algo;	 Catch:{ all -> 0x001a }
        r0 = r22;
        r0.keyExchangeAlgorithm = r3;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kexCount;	 Catch:{ all -> 0x001a }
        r0 = r22;
        r0.keyExchangeCounter = r3;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.np;	 Catch:{ all -> 0x001a }
        r3 = r3.enc_algo_client_to_server;	 Catch:{ all -> 0x001a }
        r0 = r22;
        r0.clientToServerCryptoAlgorithm = r3;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.np;	 Catch:{ all -> 0x001a }
        r3 = r3.enc_algo_server_to_client;	 Catch:{ all -> 0x001a }
        r0 = r22;
        r0.serverToClientCryptoAlgorithm = r3;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.np;	 Catch:{ all -> 0x001a }
        r3 = r3.mac_algo_client_to_server;	 Catch:{ all -> 0x001a }
        r0 = r22;
        r0.clientToServerMACAlgorithm = r3;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.np;	 Catch:{ all -> 0x001a }
        r3 = r3.mac_algo_server_to_client;	 Catch:{ all -> 0x001a }
        r0 = r22;
        r0.serverToClientMACAlgorithm = r3;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.np;	 Catch:{ all -> 0x001a }
        r3 = r3.server_host_key_algo;	 Catch:{ all -> 0x001a }
        r0 = r22;
        r0.serverHostKeyAlgorithm = r3;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.hostkey;	 Catch:{ all -> 0x001a }
        r0 = r22;
        r0.serverHostKey = r3;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r4 = r0.accessLock;	 Catch:{ all -> 0x001a }
        monitor-enter(r4);	 Catch:{ all -> 0x001a }
        r0 = r22;
        r1 = r25;
        r1.lastConnInfo = r0;	 Catch:{ all -> 0x02bb }
        r0 = r25;
        r3 = r0.accessLock;	 Catch:{ all -> 0x02bb }
        r3.notifyAll();	 Catch:{ all -> 0x02bb }
        monitor-exit(r4);	 Catch:{ all -> 0x02bb }
        r3 = 0;
        r0 = r25;
        r0.kxs = r3;	 Catch:{ all -> 0x001a }
        goto L_0x0015;
    L_0x02b2:
        r17 = move-exception;
        r3 = new java.io.IOException;	 Catch:{ all -> 0x001a }
        r4 = "Fatal error during MAC startup!";
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x02bb:
        r3 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x02bb }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x02be:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        if (r3 == 0) goto L_0x02cc;
    L_0x02c4:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.state;	 Catch:{ all -> 0x001a }
        if (r3 != 0) goto L_0x02d4;
    L_0x02cc:
        r3 = new java.io.IOException;	 Catch:{ all -> 0x001a }
        r4 = "Unexpected Kex submessage!";
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x02d4:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.np;	 Catch:{ all -> 0x001a }
        r3 = r3.kex_algo;	 Catch:{ all -> 0x001a }
        r4 = "diffie-hellman-group-exchange-sha1";
        r3 = r3.equals(r4);	 Catch:{ all -> 0x001a }
        if (r3 == 0) goto L_0x0436;
    L_0x02e4:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.state;	 Catch:{ all -> 0x001a }
        r4 = 1;
        if (r3 != r4) goto L_0x033a;
    L_0x02ed:
        r11 = new com.trilead.ssh2.packets.PacketKexDhGexGroup;	 Catch:{ all -> 0x001a }
        r3 = 0;
        r0 = r26;
        r1 = r27;
        r11.m74init(r0, r3, r1);	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r4 = new com.trilead.ssh2.crypto.dh.DhGroupExchange;	 Catch:{ all -> 0x001a }
        r5 = r11.getP();	 Catch:{ all -> 0x001a }
        r6 = r11.getG();	 Catch:{ all -> 0x001a }
        r4.m52init(r5, r6);	 Catch:{ all -> 0x001a }
        r3.dhgx = r4;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.dhgx;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r4 = r0.rnd;	 Catch:{ all -> 0x001a }
        r3.init(r4);	 Catch:{ all -> 0x001a }
        r12 = new com.trilead.ssh2.packets.PacketKexDhGexInit;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.dhgx;	 Catch:{ all -> 0x001a }
        r3 = r3.getE();	 Catch:{ all -> 0x001a }
        r12.m75init(r3);	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.tm;	 Catch:{ all -> 0x001a }
        r4 = r12.getPayload();	 Catch:{ all -> 0x001a }
        r3.sendKexMessage(r4);	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r4 = 2;
        r3.state = r4;	 Catch:{ all -> 0x001a }
        goto L_0x0015;
    L_0x033a:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.state;	 Catch:{ all -> 0x001a }
        r4 = 2;
        if (r3 != r4) goto L_0x042e;
    L_0x0343:
        r14 = new com.trilead.ssh2.packets.PacketKexDhGexReply;	 Catch:{ all -> 0x001a }
        r3 = 0;
        r0 = r26;
        r1 = r27;
        r14.m76init(r0, r3, r1);	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r4 = r14.getHostKey();	 Catch:{ all -> 0x001a }
        r3.hostkey = r4;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.verifier;	 Catch:{ all -> 0x001a }
        if (r3 == 0) goto L_0x0398;
    L_0x035d:
        r23 = 0;
        r0 = r25;
        r3 = r0.verifier;	 Catch:{ Exception -> 0x0387 }
        r0 = r25;
        r4 = r0.hostname;	 Catch:{ Exception -> 0x0387 }
        r0 = r25;
        r5 = r0.port;	 Catch:{ Exception -> 0x0387 }
        r0 = r25;
        r6 = r0.kxs;	 Catch:{ Exception -> 0x0387 }
        r6 = r6.np;	 Catch:{ Exception -> 0x0387 }
        r6 = r6.server_host_key_algo;	 Catch:{ Exception -> 0x0387 }
        r0 = r25;
        r7 = r0.kxs;	 Catch:{ Exception -> 0x0387 }
        r7 = r7.hostkey;	 Catch:{ Exception -> 0x0387 }
        r23 = r3.verifyServerHostKey(r4, r5, r6, r7);	 Catch:{ Exception -> 0x0387 }
        if (r23 != 0) goto L_0x0398;
    L_0x037f:
        r3 = new java.io.IOException;	 Catch:{ all -> 0x001a }
        r4 = "The server hostkey was not accepted by the verifier callback";
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x0387:
        r16 = move-exception;
        r3 = new java.io.IOException;	 Catch:{ all -> 0x001a }
        r4 = "The server hostkey was not accepted by the verifier callback.";
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        r0 = r16;
        r3 = r3.initCause(r0);	 Catch:{ all -> 0x001a }
        r3 = (java.io.IOException) r3;	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x0398:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.dhgx;	 Catch:{ all -> 0x001a }
        r4 = r14.getF();	 Catch:{ all -> 0x001a }
        r3.setF(r4);	 Catch:{ all -> 0x001a }
        r0 = r25;
        r0 = r0.kxs;	 Catch:{ IllegalArgumentException -> 0x0401 }
        r24 = r0;
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ IllegalArgumentException -> 0x0401 }
        r3 = r3.dhgx;	 Catch:{ IllegalArgumentException -> 0x0401 }
        r0 = r25;
        r4 = r0.csh;	 Catch:{ IllegalArgumentException -> 0x0401 }
        r4 = r4.getClientString();	 Catch:{ IllegalArgumentException -> 0x0401 }
        r0 = r25;
        r5 = r0.csh;	 Catch:{ IllegalArgumentException -> 0x0401 }
        r5 = r5.getServerString();	 Catch:{ IllegalArgumentException -> 0x0401 }
        r0 = r25;
        r6 = r0.kxs;	 Catch:{ IllegalArgumentException -> 0x0401 }
        r6 = r6.localKEX;	 Catch:{ IllegalArgumentException -> 0x0401 }
        r6 = r6.getPayload();	 Catch:{ IllegalArgumentException -> 0x0401 }
        r0 = r25;
        r7 = r0.kxs;	 Catch:{ IllegalArgumentException -> 0x0401 }
        r7 = r7.remoteKEX;	 Catch:{ IllegalArgumentException -> 0x0401 }
        r7 = r7.getPayload();	 Catch:{ IllegalArgumentException -> 0x0401 }
        r8 = r14.getHostKey();	 Catch:{ IllegalArgumentException -> 0x0401 }
        r0 = r25;
        r9 = r0.kxs;	 Catch:{ IllegalArgumentException -> 0x0401 }
        r9 = r9.dhgexParameters;	 Catch:{ IllegalArgumentException -> 0x0401 }
        r3 = r3.calculateH(r4, r5, r6, r7, r8, r9);	 Catch:{ IllegalArgumentException -> 0x0401 }
        r0 = r24;
        r0.H = r3;	 Catch:{ IllegalArgumentException -> 0x0401 }
        r3 = r14.getSignature();	 Catch:{ all -> 0x001a }
        r0 = r25;
        r4 = r0.kxs;	 Catch:{ all -> 0x001a }
        r4 = r4.hostkey;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r21 = r0.verifySignature(r3, r4);	 Catch:{ all -> 0x001a }
        if (r21 != 0) goto L_0x0412;
    L_0x03f9:
        r3 = new java.io.IOException;	 Catch:{ all -> 0x001a }
        r4 = "Hostkey signature sent by remote is wrong!";
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x0401:
        r16 = move-exception;
        r3 = new java.io.IOException;	 Catch:{ all -> 0x001a }
        r4 = "KEX error.";
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        r0 = r16;
        r3 = r3.initCause(r0);	 Catch:{ all -> 0x001a }
        r3 = (java.io.IOException) r3;	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x0412:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r4 = r0.kxs;	 Catch:{ all -> 0x001a }
        r4 = r4.dhgx;	 Catch:{ all -> 0x001a }
        r4 = r4.getK();	 Catch:{ all -> 0x001a }
        r3.K = r4;	 Catch:{ all -> 0x001a }
        r25.finishKex();	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r4 = -1;
        r3.state = r4;	 Catch:{ all -> 0x001a }
        goto L_0x0015;
    L_0x042e:
        r3 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x001a }
        r4 = "Illegal State in KEX Exchange!";
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x0436:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.np;	 Catch:{ all -> 0x001a }
        r3 = r3.kex_algo;	 Catch:{ all -> 0x001a }
        r4 = "diffie-hellman-group1-sha1";
        r3 = r3.equals(r4);	 Catch:{ all -> 0x001a }
        if (r3 != 0) goto L_0x0456;
    L_0x0446:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.np;	 Catch:{ all -> 0x001a }
        r3 = r3.kex_algo;	 Catch:{ all -> 0x001a }
        r4 = "diffie-hellman-group14-sha1";
        r3 = r3.equals(r4);	 Catch:{ all -> 0x001a }
        if (r3 == 0) goto L_0x0540;
    L_0x0456:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.state;	 Catch:{ all -> 0x001a }
        r4 = 1;
        if (r3 != r4) goto L_0x0540;
    L_0x045f:
        r15 = new com.trilead.ssh2.packets.PacketKexDHReply;	 Catch:{ all -> 0x001a }
        r3 = 0;
        r0 = r26;
        r1 = r27;
        r15.m73init(r0, r3, r1);	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r4 = r15.getHostKey();	 Catch:{ all -> 0x001a }
        r3.hostkey = r4;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.verifier;	 Catch:{ all -> 0x001a }
        if (r3 == 0) goto L_0x04b4;
    L_0x0479:
        r23 = 0;
        r0 = r25;
        r3 = r0.verifier;	 Catch:{ Exception -> 0x04a3 }
        r0 = r25;
        r4 = r0.hostname;	 Catch:{ Exception -> 0x04a3 }
        r0 = r25;
        r5 = r0.port;	 Catch:{ Exception -> 0x04a3 }
        r0 = r25;
        r6 = r0.kxs;	 Catch:{ Exception -> 0x04a3 }
        r6 = r6.np;	 Catch:{ Exception -> 0x04a3 }
        r6 = r6.server_host_key_algo;	 Catch:{ Exception -> 0x04a3 }
        r0 = r25;
        r7 = r0.kxs;	 Catch:{ Exception -> 0x04a3 }
        r7 = r7.hostkey;	 Catch:{ Exception -> 0x04a3 }
        r23 = r3.verifyServerHostKey(r4, r5, r6, r7);	 Catch:{ Exception -> 0x04a3 }
        if (r23 != 0) goto L_0x04b4;
    L_0x049b:
        r3 = new java.io.IOException;	 Catch:{ all -> 0x001a }
        r4 = "The server hostkey was not accepted by the verifier callback";
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x04a3:
        r16 = move-exception;
        r3 = new java.io.IOException;	 Catch:{ all -> 0x001a }
        r4 = "The server hostkey was not accepted by the verifier callback.";
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        r0 = r16;
        r3 = r3.initCause(r0);	 Catch:{ all -> 0x001a }
        r3 = (java.io.IOException) r3;	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x04b4:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r3 = r3.dhx;	 Catch:{ all -> 0x001a }
        r4 = r15.getF();	 Catch:{ all -> 0x001a }
        r3.setF(r4);	 Catch:{ all -> 0x001a }
        r0 = r25;
        r9 = r0.kxs;	 Catch:{ IllegalArgumentException -> 0x0513 }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ IllegalArgumentException -> 0x0513 }
        r3 = r3.dhx;	 Catch:{ IllegalArgumentException -> 0x0513 }
        r0 = r25;
        r4 = r0.csh;	 Catch:{ IllegalArgumentException -> 0x0513 }
        r4 = r4.getClientString();	 Catch:{ IllegalArgumentException -> 0x0513 }
        r0 = r25;
        r5 = r0.csh;	 Catch:{ IllegalArgumentException -> 0x0513 }
        r5 = r5.getServerString();	 Catch:{ IllegalArgumentException -> 0x0513 }
        r0 = r25;
        r6 = r0.kxs;	 Catch:{ IllegalArgumentException -> 0x0513 }
        r6 = r6.localKEX;	 Catch:{ IllegalArgumentException -> 0x0513 }
        r6 = r6.getPayload();	 Catch:{ IllegalArgumentException -> 0x0513 }
        r0 = r25;
        r7 = r0.kxs;	 Catch:{ IllegalArgumentException -> 0x0513 }
        r7 = r7.remoteKEX;	 Catch:{ IllegalArgumentException -> 0x0513 }
        r7 = r7.getPayload();	 Catch:{ IllegalArgumentException -> 0x0513 }
        r8 = r15.getHostKey();	 Catch:{ IllegalArgumentException -> 0x0513 }
        r3 = r3.calculateH(r4, r5, r6, r7, r8);	 Catch:{ IllegalArgumentException -> 0x0513 }
        r9.H = r3;	 Catch:{ IllegalArgumentException -> 0x0513 }
        r3 = r15.getSignature();	 Catch:{ all -> 0x001a }
        r0 = r25;
        r4 = r0.kxs;	 Catch:{ all -> 0x001a }
        r4 = r4.hostkey;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r21 = r0.verifySignature(r3, r4);	 Catch:{ all -> 0x001a }
        if (r21 != 0) goto L_0x0524;
    L_0x050b:
        r3 = new java.io.IOException;	 Catch:{ all -> 0x001a }
        r4 = "Hostkey signature sent by remote is wrong!";
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x0513:
        r16 = move-exception;
        r3 = new java.io.IOException;	 Catch:{ all -> 0x001a }
        r4 = "KEX error.";
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        r0 = r16;
        r3 = r3.initCause(r0);	 Catch:{ all -> 0x001a }
        r3 = (java.io.IOException) r3;	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
    L_0x0524:
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r0 = r25;
        r4 = r0.kxs;	 Catch:{ all -> 0x001a }
        r4 = r4.dhx;	 Catch:{ all -> 0x001a }
        r4 = r4.getK();	 Catch:{ all -> 0x001a }
        r3.K = r4;	 Catch:{ all -> 0x001a }
        r25.finishKex();	 Catch:{ all -> 0x001a }
        r0 = r25;
        r3 = r0.kxs;	 Catch:{ all -> 0x001a }
        r4 = -1;
        r3.state = r4;	 Catch:{ all -> 0x001a }
        goto L_0x0015;
    L_0x0540:
        r3 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x001a }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x001a }
        r5 = "Unkown KEX method! (";
        r4.<init>(r5);	 Catch:{ all -> 0x001a }
        r0 = r25;
        r5 = r0.kxs;	 Catch:{ all -> 0x001a }
        r5 = r5.np;	 Catch:{ all -> 0x001a }
        r5 = r5.kex_algo;	 Catch:{ all -> 0x001a }
        r4 = r4.append(r5);	 Catch:{ all -> 0x001a }
        r5 = ")";
        r4 = r4.append(r5);	 Catch:{ all -> 0x001a }
        r4 = r4.toString();	 Catch:{ all -> 0x001a }
        r3.<init>(r4);	 Catch:{ all -> 0x001a }
        throw r3;	 Catch:{ all -> 0x001a }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trilead.ssh2.transport.KexManager.handleMessage(byte[], int):void");
    }
}
