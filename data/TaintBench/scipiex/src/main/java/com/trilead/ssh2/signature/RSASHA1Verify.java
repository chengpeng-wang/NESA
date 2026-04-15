package com.trilead.ssh2.signature;

import com.trilead.ssh2.crypto.SimpleDERReader;
import com.trilead.ssh2.crypto.digest.SHA1;
import com.trilead.ssh2.log.Logger;
import com.trilead.ssh2.packets.TypesReader;
import com.trilead.ssh2.packets.TypesWriter;
import java.io.IOException;
import java.math.BigInteger;

public class RSASHA1Verify {
    private static final Logger log = Logger.getLogger(RSASHA1Verify.class);

    public static RSAPublicKey decodeSSHRSAPublicKey(byte[] key) throws IOException {
        TypesReader tr = new TypesReader(key);
        if (tr.readString().equals("ssh-rsa")) {
            BigInteger e = tr.readMPINT();
            BigInteger n = tr.readMPINT();
            if (tr.remain() == 0) {
                return new RSAPublicKey(e, n);
            }
            throw new IOException("Padding in RSA public key!");
        }
        throw new IllegalArgumentException("This is not a ssh-rsa public key");
    }

    public static byte[] encodeSSHRSAPublicKey(RSAPublicKey pk) throws IOException {
        TypesWriter tw = new TypesWriter();
        tw.writeString("ssh-rsa");
        tw.writeMPInt(pk.getE());
        tw.writeMPInt(pk.getN());
        return tw.getBytes();
    }

    public static RSASignature decodeSSHRSASignature(byte[] sig) throws IOException {
        TypesReader tr = new TypesReader(sig);
        if (tr.readString().equals("ssh-rsa")) {
            byte[] s = tr.readByteString();
            if (s.length == 0) {
                throw new IOException("Error in RSA signature, S is empty.");
            }
            if (log.isEnabled()) {
                log.log(80, "Decoding ssh-rsa signature string (length: " + s.length + ")");
            }
            if (tr.remain() == 0) {
                return new RSASignature(new BigInteger(1, s));
            }
            throw new IOException("Padding in RSA signature!");
        }
        throw new IOException("Peer sent wrong signature format");
    }

    public static byte[] encodeSSHRSASignature(RSASignature sig) throws IOException {
        TypesWriter tw = new TypesWriter();
        tw.writeString("ssh-rsa");
        byte[] s = sig.getS().toByteArray();
        if (s.length <= 1 || s[0] != (byte) 0) {
            tw.writeString(s, 0, s.length);
        } else {
            tw.writeString(s, 1, s.length - 1);
        }
        return tw.getBytes();
    }

    public static RSASignature generateSignature(byte[] message, RSAPrivateKey pk) throws IOException {
        SHA1 md = new SHA1();
        md.update(message);
        byte[] sha_message = new byte[md.getDigestLength()];
        md.digest(sha_message);
        byte[] der_header = new byte[15];
        der_header[0] = (byte) 48;
        der_header[1] = (byte) 33;
        der_header[2] = (byte) 48;
        der_header[3] = (byte) 9;
        der_header[4] = (byte) 6;
        der_header[5] = (byte) 5;
        der_header[6] = (byte) 43;
        der_header[7] = (byte) 14;
        der_header[8] = (byte) 3;
        der_header[9] = (byte) 2;
        der_header[10] = (byte) 26;
        der_header[11] = (byte) 5;
        der_header[13] = (byte) 4;
        der_header[14] = (byte) 20;
        int num_pad = (((pk.getN().bitLength() + 7) / 8) - ((der_header.length + 2) + sha_message.length)) - 1;
        if (num_pad < 8) {
            throw new IOException("Cannot sign with RSA, message too long");
        }
        byte[] sig = new byte[(((der_header.length + sha_message.length) + 2) + num_pad)];
        sig[0] = (byte) 1;
        for (int i = 0; i < num_pad; i++) {
            sig[i + 1] = (byte) -1;
        }
        sig[num_pad + 1] = (byte) 0;
        System.arraycopy(der_header, 0, sig, num_pad + 2, der_header.length);
        System.arraycopy(sha_message, 0, sig, (num_pad + 2) + der_header.length, sha_message.length);
        return new RSASignature(new BigInteger(1, sig).modPow(pk.getD(), pk.getN()));
    }

    public static boolean verifySignature(byte[] message, RSASignature ds, RSAPublicKey dpk) throws IOException {
        SHA1 md = new SHA1();
        md.update(message);
        byte[] sha_message = new byte[md.getDigestLength()];
        md.digest(sha_message);
        BigInteger n = dpk.getN();
        BigInteger e = dpk.getE();
        BigInteger s = ds.getS();
        if (n.compareTo(s) <= 0) {
            log.log(20, "ssh-rsa signature: n.compareTo(s) <= 0");
            return false;
        }
        int rsa_block_len = (n.bitLength() + 7) / 8;
        if (rsa_block_len < 1) {
            log.log(20, "ssh-rsa signature: rsa_block_len < 1");
            return false;
        }
        byte[] v = s.modPow(e, n).toByteArray();
        int startpos = 0;
        if (v.length > 0 && v[0] == (byte) 0) {
            startpos = 0 + 1;
        }
        if (v.length - startpos != rsa_block_len - 1) {
            log.log(20, "ssh-rsa signature: (v.length - startpos) != (rsa_block_len - 1)");
            return false;
        } else if (v[startpos] != (byte) 1) {
            log.log(20, "ssh-rsa signature: v[startpos] != 0x01");
            return false;
        } else {
            int pos = startpos + 1;
            while (pos < v.length) {
                if (v[pos] == (byte) 0) {
                    if (pos - (startpos + 1) < 8) {
                        log.log(20, "ssh-rsa signature: num_pad < 8");
                        return false;
                    }
                    pos++;
                    if (pos >= v.length) {
                        log.log(20, "ssh-rsa signature: pos >= v.length");
                        return false;
                    }
                    SimpleDERReader dr = new SimpleDERReader(v, pos, v.length - pos);
                    byte[] seq = dr.readSequenceAsByteArray();
                    if (dr.available() != 0) {
                        log.log(20, "ssh-rsa signature: dr.available() != 0");
                        return false;
                    }
                    dr.resetInput(seq);
                    byte[] digestAlgorithm = dr.readSequenceAsByteArray();
                    if (digestAlgorithm.length < 8 || digestAlgorithm.length > 9) {
                        log.log(20, "ssh-rsa signature: (digestAlgorithm.length < 8) || (digestAlgorithm.length > 9)");
                        return false;
                    }
                    int i;
                    byte[] digestAlgorithm_sha1 = new byte[9];
                    digestAlgorithm_sha1[0] = (byte) 6;
                    digestAlgorithm_sha1[1] = (byte) 5;
                    digestAlgorithm_sha1[2] = (byte) 43;
                    digestAlgorithm_sha1[3] = (byte) 14;
                    digestAlgorithm_sha1[4] = (byte) 3;
                    digestAlgorithm_sha1[5] = (byte) 2;
                    digestAlgorithm_sha1[6] = (byte) 26;
                    digestAlgorithm_sha1[7] = (byte) 5;
                    for (i = 0; i < digestAlgorithm.length; i++) {
                        if (digestAlgorithm[i] != digestAlgorithm_sha1[i]) {
                            log.log(20, "ssh-rsa signature: digestAlgorithm[i] != digestAlgorithm_sha1[i]");
                            return false;
                        }
                    }
                    byte[] digest = dr.readOctetString();
                    if (dr.available() != 0) {
                        log.log(20, "ssh-rsa signature: dr.available() != 0 (II)");
                        return false;
                    }
                    if (digest.length != sha_message.length) {
                        log.log(20, "ssh-rsa signature: digest.length != sha_message.length");
                        return false;
                    }
                    for (i = 0; i < sha_message.length; i++) {
                        if (sha_message[i] != digest[i]) {
                            log.log(20, "ssh-rsa signature: sha_message[i] != digest[i]");
                            return false;
                        }
                    }
                    return true;
                } else if (v[pos] != (byte) -1) {
                    log.log(20, "ssh-rsa signature: v[pos] != (byte) 0xff");
                    return false;
                } else {
                    pos++;
                }
            }
            log.log(20, "ssh-rsa signature: pos >= v.length");
            return false;
        }
    }
}
