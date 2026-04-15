package com.trilead.ssh2.crypto;

import com.trilead.ssh2.crypto.cipher.AES;
import com.trilead.ssh2.crypto.cipher.BlockCipher;
import com.trilead.ssh2.crypto.cipher.CBCMode;
import com.trilead.ssh2.crypto.cipher.DES;
import com.trilead.ssh2.crypto.cipher.DESede;
import com.trilead.ssh2.crypto.digest.MD5;
import com.trilead.ssh2.signature.DSAPrivateKey;
import com.trilead.ssh2.signature.RSAPrivateKey;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.math.BigInteger;

public class PEMDecoder {
    private static final int PEM_DSA_PRIVATE_KEY = 2;
    private static final int PEM_RSA_PRIVATE_KEY = 1;

    private static final int hexToInt(char c) {
        if (c >= 'a' && c <= 'f') {
            return (c - 97) + 10;
        }
        if (c >= 'A' && c <= 'F') {
            return (c - 65) + 10;
        }
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        throw new IllegalArgumentException("Need hex char");
    }

    private static byte[] hexToByteArray(String hex) {
        if (hex == null) {
            throw new IllegalArgumentException("null argument");
        } else if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Uneven string length in hex encoding.");
        } else {
            byte[] decoded = new byte[(hex.length() / 2)];
            for (int i = 0; i < decoded.length; i++) {
                int hi = hexToInt(hex.charAt(i * 2));
                decoded[i] = (byte) ((hi * 16) + hexToInt(hex.charAt((i * 2) + 1)));
            }
            return decoded;
        }
    }

    private static byte[] generateKeyFromPasswordSaltWithMD5(byte[] password, byte[] salt, int keyLen) throws IOException {
        if (salt.length < 8) {
            throw new IllegalArgumentException("Salt needs to be at least 8 bytes for key generation.");
        }
        MD5 md5 = new MD5();
        byte[] key = new byte[keyLen];
        byte[] tmp = new byte[md5.getDigestLength()];
        while (true) {
            md5.update(password, 0, password.length);
            md5.update(salt, 0, 8);
            int copy = keyLen < tmp.length ? keyLen : tmp.length;
            md5.digest(tmp, 0);
            System.arraycopy(tmp, 0, key, key.length - keyLen, copy);
            keyLen -= copy;
            if (keyLen == 0) {
                return key;
            }
            md5.update(tmp, 0, tmp.length);
        }
    }

    private static byte[] removePadding(byte[] buff, int blockSize) throws IOException {
        byte rfc_1423_padding = buff[buff.length - 1] & 255;
        if (rfc_1423_padding < (byte) 1 || rfc_1423_padding > blockSize) {
            throw new IOException("Decrypted PEM has wrong padding, did you specify the correct password?");
        }
        for (byte i = (byte) 2; i <= rfc_1423_padding; i++) {
            if (buff[buff.length - i] != rfc_1423_padding) {
                throw new IOException("Decrypted PEM has wrong padding, did you specify the correct password?");
            }
        }
        byte[] tmp = new byte[(buff.length - rfc_1423_padding)];
        System.arraycopy(buff, 0, tmp, 0, buff.length - rfc_1423_padding);
        return tmp;
    }

    private static final PEMStructure parsePEM(char[] pem) throws IOException {
        String line;
        String endLine;
        PEMStructure ps = new PEMStructure();
        BufferedReader br = new BufferedReader(new CharArrayReader(pem));
        do {
            line = br.readLine();
            if (line == null) {
                throw new IOException("Invalid PEM structure, '-----BEGIN...' missing");
            }
            line = line.trim();
            if (line.startsWith("-----BEGIN DSA PRIVATE KEY-----")) {
                endLine = "-----END DSA PRIVATE KEY-----";
                ps.pemType = 2;
                break;
            }
        } while (!line.startsWith("-----BEGIN RSA PRIVATE KEY-----"));
        endLine = "-----END RSA PRIVATE KEY-----";
        ps.pemType = 1;
        while (true) {
            line = br.readLine();
            if (line == null) {
                throw new IOException("Invalid PEM structure, " + endLine + " missing");
            }
            line = line.trim();
            int sem_idx = line.indexOf(58);
            if (sem_idx == -1) {
                StringBuffer keyData = new StringBuffer();
                while (line != null) {
                    line = line.trim();
                    if (line.startsWith(endLine)) {
                        char[] pem_chars = new char[keyData.length()];
                        keyData.getChars(0, pem_chars.length, pem_chars, 0);
                        ps.data = Base64.decode(pem_chars);
                        if (ps.data.length != 0) {
                            return ps;
                        }
                        throw new IOException("Invalid PEM structure, no data available");
                    }
                    keyData.append(line);
                    line = br.readLine();
                }
                throw new IOException("Invalid PEM structure, " + endLine + " missing");
            }
            String name = line.substring(0, sem_idx + 1);
            String[] values = line.substring(sem_idx + 1).split(",");
            for (int i = 0; i < values.length; i++) {
                values[i] = values[i].trim();
            }
            if ("Proc-Type:".equals(name)) {
                ps.procType = values;
            } else if ("DEK-Info:".equals(name)) {
                ps.dekInfo = values;
            }
        }
    }

    private static final void decryptPEM(PEMStructure ps, byte[] pw) throws IOException {
        if (ps.dekInfo == null) {
            throw new IOException("Broken PEM, no mode and salt given, but encryption enabled");
        } else if (ps.dekInfo.length != 2) {
            throw new IOException("Broken PEM, DEK-Info is incomplete!");
        } else {
            BlockCipher bc;
            String algo = ps.dekInfo[0];
            byte[] salt = hexToByteArray(ps.dekInfo[1]);
            AES aes;
            if (algo.equals("DES-EDE3-CBC")) {
                DESede des3 = new DESede();
                des3.init(false, generateKeyFromPasswordSaltWithMD5(pw, salt, 24));
                bc = new CBCMode(des3, salt, false);
            } else if (algo.equals("DES-CBC")) {
                DES des = new DES();
                des.init(false, generateKeyFromPasswordSaltWithMD5(pw, salt, 8));
                bc = new CBCMode(des, salt, false);
            } else if (algo.equals("AES-128-CBC")) {
                aes = new AES();
                aes.init(false, generateKeyFromPasswordSaltWithMD5(pw, salt, 16));
                bc = new CBCMode(aes, salt, false);
            } else if (algo.equals("AES-192-CBC")) {
                aes = new AES();
                aes.init(false, generateKeyFromPasswordSaltWithMD5(pw, salt, 24));
                bc = new CBCMode(aes, salt, false);
            } else if (algo.equals("AES-256-CBC")) {
                aes = new AES();
                aes.init(false, generateKeyFromPasswordSaltWithMD5(pw, salt, 32));
                bc = new CBCMode(aes, salt, false);
            } else {
                throw new IOException("Cannot decrypt PEM structure, unknown cipher " + algo);
            }
            if (ps.data.length % bc.getBlockSize() != 0) {
                throw new IOException("Invalid PEM structure, size of encrypted block is not a multiple of " + bc.getBlockSize());
            }
            byte[] dz = new byte[ps.data.length];
            for (int i = 0; i < ps.data.length / bc.getBlockSize(); i++) {
                bc.transformBlock(ps.data, bc.getBlockSize() * i, dz, bc.getBlockSize() * i);
            }
            ps.data = removePadding(dz, bc.getBlockSize());
            ps.dekInfo = null;
            ps.procType = null;
        }
    }

    public static final boolean isPEMEncrypted(PEMStructure ps) throws IOException {
        if (ps.procType == null) {
            return false;
        }
        if (ps.procType.length != 2) {
            throw new IOException("Unknown Proc-Type field.");
        } else if (!"4".equals(ps.procType[0])) {
            throw new IOException("Unknown Proc-Type field (" + ps.procType[0] + ")");
        } else if ("ENCRYPTED".equals(ps.procType[1])) {
            return true;
        } else {
            return false;
        }
    }

    public static Object decode(char[] pem, String password) throws IOException {
        PEMStructure ps = parsePEM(pem);
        if (isPEMEncrypted(ps)) {
            if (password == null) {
                throw new IOException("PEM is encrypted, but no password was specified");
            }
            decryptPEM(ps, password.getBytes("ISO-8859-1"));
        }
        SimpleDERReader dr;
        byte[] seq;
        BigInteger version;
        if (ps.pemType == 2) {
            dr = new SimpleDERReader(ps.data);
            seq = dr.readSequenceAsByteArray();
            if (dr.available() != 0) {
                throw new IOException("Padding in DSA PRIVATE KEY DER stream.");
            }
            dr.resetInput(seq);
            version = dr.readInt();
            if (version.compareTo(BigInteger.ZERO) != 0) {
                throw new IOException("Wrong version (" + version + ") in DSA PRIVATE KEY DER stream.");
            }
            BigInteger p = dr.readInt();
            BigInteger q = dr.readInt();
            BigInteger g = dr.readInt();
            BigInteger y = dr.readInt();
            BigInteger x = dr.readInt();
            if (dr.available() == 0) {
                return new DSAPrivateKey(p, q, g, y, x);
            }
            throw new IOException("Padding in DSA PRIVATE KEY DER stream.");
        } else if (ps.pemType == 1) {
            dr = new SimpleDERReader(ps.data);
            seq = dr.readSequenceAsByteArray();
            if (dr.available() != 0) {
                throw new IOException("Padding in RSA PRIVATE KEY DER stream.");
            }
            dr.resetInput(seq);
            version = dr.readInt();
            if (version.compareTo(BigInteger.ZERO) == 0 || version.compareTo(BigInteger.ONE) == 0) {
                BigInteger n = dr.readInt();
                return new RSAPrivateKey(dr.readInt(), dr.readInt(), n);
            }
            throw new IOException("Wrong version (" + version + ") in RSA PRIVATE KEY DER stream.");
        } else {
            throw new IOException("PEM problem: it is of unknown type");
        }
    }
}
