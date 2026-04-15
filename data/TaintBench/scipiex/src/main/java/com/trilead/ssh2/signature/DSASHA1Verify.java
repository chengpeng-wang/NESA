package com.trilead.ssh2.signature;

import com.trilead.ssh2.crypto.digest.SHA1;
import com.trilead.ssh2.log.Logger;
import com.trilead.ssh2.packets.TypesReader;
import com.trilead.ssh2.packets.TypesWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

public class DSASHA1Verify {
    private static final Logger log = Logger.getLogger(DSASHA1Verify.class);

    public static DSAPublicKey decodeSSHDSAPublicKey(byte[] key) throws IOException {
        TypesReader tr = new TypesReader(key);
        if (tr.readString().equals("ssh-dss")) {
            BigInteger p = tr.readMPINT();
            BigInteger q = tr.readMPINT();
            BigInteger g = tr.readMPINT();
            BigInteger y = tr.readMPINT();
            if (tr.remain() == 0) {
                return new DSAPublicKey(p, q, g, y);
            }
            throw new IOException("Padding in DSA public key!");
        }
        throw new IllegalArgumentException("This is not a ssh-dss public key!");
    }

    public static byte[] encodeSSHDSAPublicKey(DSAPublicKey pk) throws IOException {
        TypesWriter tw = new TypesWriter();
        tw.writeString("ssh-dss");
        tw.writeMPInt(pk.getP());
        tw.writeMPInt(pk.getQ());
        tw.writeMPInt(pk.getG());
        tw.writeMPInt(pk.getY());
        return tw.getBytes();
    }

    public static byte[] encodeSSHDSASignature(DSASignature ds) {
        int r_copylen;
        int s_copylen;
        TypesWriter tw = new TypesWriter();
        tw.writeString("ssh-dss");
        byte[] r = ds.getR().toByteArray();
        byte[] s = ds.getS().toByteArray();
        byte[] a40 = new byte[40];
        if (r.length < 20) {
            r_copylen = r.length;
        } else {
            r_copylen = 20;
        }
        if (s.length < 20) {
            s_copylen = s.length;
        } else {
            s_copylen = 20;
        }
        System.arraycopy(r, r.length - r_copylen, a40, 20 - r_copylen, r_copylen);
        System.arraycopy(s, s.length - s_copylen, a40, 40 - s_copylen, s_copylen);
        tw.writeString(a40, 0, 40);
        return tw.getBytes();
    }

    public static DSASignature decodeSSHDSASignature(byte[] sig) throws IOException {
        byte[] rsArray = null;
        if (sig.length == 40) {
            rsArray = sig;
        } else {
            TypesReader tr = new TypesReader(sig);
            if (tr.readString().equals("ssh-dss")) {
                rsArray = tr.readByteString();
                if (rsArray.length != 40) {
                    throw new IOException("Peer sent corrupt signature");
                } else if (tr.remain() != 0) {
                    throw new IOException("Padding in DSA signature!");
                }
            }
            throw new IOException("Peer sent wrong signature format");
        }
        byte[] tmp = new byte[20];
        System.arraycopy(rsArray, 0, tmp, 0, 20);
        BigInteger r = new BigInteger(1, tmp);
        System.arraycopy(rsArray, 20, tmp, 0, 20);
        BigInteger s = new BigInteger(1, tmp);
        if (log.isEnabled()) {
            log.log(30, "decoded ssh-dss signature: first bytes r(" + (rsArray[0] & 255) + "), s(" + (rsArray[20] & 255) + ")");
        }
        return new DSASignature(r, s);
    }

    public static boolean verifySignature(byte[] message, DSASignature ds, DSAPublicKey dpk) throws IOException {
        SHA1 md = new SHA1();
        md.update(message);
        byte[] sha_message = new byte[md.getDigestLength()];
        md.digest(sha_message);
        BigInteger m = new BigInteger(1, sha_message);
        BigInteger r = ds.getR();
        BigInteger s = ds.getS();
        BigInteger g = dpk.getG();
        BigInteger p = dpk.getP();
        BigInteger q = dpk.getQ();
        BigInteger y = dpk.getY();
        BigInteger zero = BigInteger.ZERO;
        if (log.isEnabled()) {
            log.log(60, "ssh-dss signature: m: " + m.toString(16));
            log.log(60, "ssh-dss signature: r: " + r.toString(16));
            log.log(60, "ssh-dss signature: s: " + s.toString(16));
            log.log(60, "ssh-dss signature: g: " + g.toString(16));
            log.log(60, "ssh-dss signature: p: " + p.toString(16));
            log.log(60, "ssh-dss signature: q: " + q.toString(16));
            log.log(60, "ssh-dss signature: y: " + y.toString(16));
        }
        if (zero.compareTo(r) >= 0 || q.compareTo(r) <= 0) {
            log.log(20, "ssh-dss signature: zero.compareTo(r) >= 0 || q.compareTo(r) <= 0");
            return false;
        } else if (zero.compareTo(s) >= 0 || q.compareTo(s) <= 0) {
            log.log(20, "ssh-dss signature: zero.compareTo(s) >= 0 || q.compareTo(s) <= 0");
            return false;
        } else {
            BigInteger w = s.modInverse(q);
            return g.modPow(m.multiply(w).mod(q), p).multiply(y.modPow(r.multiply(w).mod(q), p)).mod(p).mod(q).equals(r);
        }
    }

    public static DSASignature generateSignature(byte[] message, DSAPrivateKey pk, SecureRandom rnd) {
        BigInteger k;
        SHA1 md = new SHA1();
        md.update(message);
        byte[] sha_message = new byte[md.getDigestLength()];
        md.digest(sha_message);
        BigInteger m = new BigInteger(1, sha_message);
        int qBitLength = pk.getQ().bitLength();
        do {
            k = new BigInteger(qBitLength, rnd);
        } while (k.compareTo(pk.getQ()) >= 0);
        BigInteger r = pk.getG().modPow(k, pk.getP()).mod(pk.getQ());
        return new DSASignature(r, k.modInverse(pk.getQ()).multiply(m.add(pk.getX().multiply(r))).mod(pk.getQ()));
    }
}
