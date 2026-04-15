package com.trilead.ssh2;

import com.trilead.ssh2.crypto.Base64;
import com.trilead.ssh2.crypto.digest.Digest;
import com.trilead.ssh2.crypto.digest.HMAC;
import com.trilead.ssh2.crypto.digest.MD5;
import com.trilead.ssh2.crypto.digest.SHA1;
import com.trilead.ssh2.signature.DSAPublicKey;
import com.trilead.ssh2.signature.DSASHA1Verify;
import com.trilead.ssh2.signature.RSAPublicKey;
import com.trilead.ssh2.signature.RSASHA1Verify;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

public class KnownHosts {
    public static final int HOSTKEY_HAS_CHANGED = 2;
    public static final int HOSTKEY_IS_NEW = 1;
    public static final int HOSTKEY_IS_OK = 0;
    private LinkedList publicKeys = new LinkedList();

    private class KnownHostsEntry {
        Object key;
        String[] patterns;

        KnownHostsEntry(String[] patterns, Object key) {
            this.patterns = patterns;
            this.key = key;
        }
    }

    public KnownHosts(char[] knownHostsData) throws IOException {
        initialize(knownHostsData);
    }

    public KnownHosts(File knownHosts) throws IOException {
        initialize(knownHosts);
    }

    public void addHostkey(String[] hostnames, String serverHostKeyAlgorithm, byte[] serverHostKey) throws IOException {
        if (hostnames == null) {
            throw new IllegalArgumentException("hostnames may not be null");
        } else if ("ssh-rsa".equals(serverHostKeyAlgorithm)) {
            RSAPublicKey rpk = RSASHA1Verify.decodeSSHRSAPublicKey(serverHostKey);
            synchronized (this.publicKeys) {
                this.publicKeys.add(new KnownHostsEntry(hostnames, rpk));
            }
        } else if ("ssh-dss".equals(serverHostKeyAlgorithm)) {
            DSAPublicKey dpk = DSASHA1Verify.decodeSSHDSAPublicKey(serverHostKey);
            synchronized (this.publicKeys) {
                this.publicKeys.add(new KnownHostsEntry(hostnames, dpk));
            }
        } else {
            throw new IOException("Unknwon host key type (" + serverHostKeyAlgorithm + ")");
        }
    }

    public void addHostkeys(char[] knownHostsData) throws IOException {
        initialize(knownHostsData);
    }

    public void addHostkeys(File knownHosts) throws IOException {
        initialize(knownHosts);
    }

    public static final String createHashedHostname(String hostname) {
        byte[] salt = new byte[new SHA1().getDigestLength()];
        new SecureRandom().nextBytes(salt);
        byte[] hash = hmacSha1Hash(salt, hostname);
        String base64_salt = new String(Base64.encode(salt));
        return new String("|1|" + base64_salt + "|" + new String(Base64.encode(hash)));
    }

    private static final byte[] hmacSha1Hash(byte[] salt, String hostname) {
        SHA1 sha1 = new SHA1();
        if (salt.length != sha1.getDigestLength()) {
            throw new IllegalArgumentException("Salt has wrong length (" + salt.length + ")");
        }
        HMAC hmac = new HMAC(sha1, salt, salt.length);
        try {
            hmac.update(hostname.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            hmac.update(hostname.getBytes());
        }
        byte[] dig = new byte[hmac.getDigestLength()];
        hmac.digest(dig);
        return dig;
    }

    private final boolean checkHashed(String entry, String hostname) {
        if (!entry.startsWith("|1|")) {
            return false;
        }
        int delim_idx = entry.indexOf(124, 3);
        if (delim_idx == -1) {
            return false;
        }
        String salt_base64 = entry.substring(3, delim_idx);
        String hash_base64 = entry.substring(delim_idx + 1);
        byte[] salt = null;
        byte[] hash = null;
        try {
            salt = Base64.decode(salt_base64.toCharArray());
            hash = Base64.decode(hash_base64.toCharArray());
            if (salt.length != new SHA1().getDigestLength()) {
                return false;
            }
            byte[] dig = hmacSha1Hash(salt, hostname);
            for (int i = 0; i < dig.length; i++) {
                if (dig[i] != hash[i]) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private int checkKey(String remoteHostname, Object remoteKey) {
        int result = 1;
        synchronized (this.publicKeys) {
            Iterator i = this.publicKeys.iterator();
            while (i.hasNext()) {
                KnownHostsEntry ke = (KnownHostsEntry) i.next();
                if (hostnameMatches(ke.patterns, remoteHostname)) {
                    if (matchKeys(ke.key, remoteKey)) {
                        return 0;
                    }
                    result = 2;
                }
            }
            return result;
        }
    }

    private Vector getAllKeys(String hostname) {
        Vector keys = new Vector();
        synchronized (this.publicKeys) {
            Iterator i = this.publicKeys.iterator();
            while (i.hasNext()) {
                KnownHostsEntry ke = (KnownHostsEntry) i.next();
                if (hostnameMatches(ke.patterns, hostname)) {
                    keys.addElement(ke.key);
                }
            }
        }
        return keys;
    }

    public String[] getPreferredServerHostkeyAlgorithmOrder(String hostname) {
        String[] algos = recommendHostkeyAlgorithms(hostname);
        if (algos != null) {
            return algos;
        }
        InetAddress[] inetAddressArr = (InetAddress[]) null;
        try {
            inetAddressArr = InetAddress.getAllByName(hostname);
            for (InetAddress hostAddress : inetAddressArr) {
                algos = recommendHostkeyAlgorithms(hostAddress.getHostAddress());
                if (algos != null) {
                    return algos;
                }
            }
            return null;
        } catch (UnknownHostException e) {
            return null;
        }
    }

    private final boolean hostnameMatches(String[] hostpatterns, String hostname) {
        boolean isMatch = false;
        hostname = hostname.toLowerCase();
        int k = 0;
        while (k < hostpatterns.length) {
            if (hostpatterns[k] != null) {
                String pattern;
                boolean negate;
                if (hostpatterns[k].length() <= 0 || hostpatterns[k].charAt(0) != '!') {
                    pattern = hostpatterns[k];
                    negate = false;
                } else {
                    pattern = hostpatterns[k].substring(1);
                    negate = true;
                }
                if (!isMatch || negate) {
                    if (pattern.charAt(0) != '|') {
                        pattern = pattern.toLowerCase();
                        if (pattern.indexOf(63) == -1 && pattern.indexOf(42) == -1) {
                            if (pattern.compareTo(hostname) != 0) {
                                continue;
                            } else if (negate) {
                                return false;
                            } else {
                                isMatch = true;
                            }
                        } else if (!pseudoRegex(pattern.toCharArray(), 0, hostname.toCharArray(), 0)) {
                            continue;
                        } else if (negate) {
                            return false;
                        } else {
                            isMatch = true;
                        }
                    } else if (!checkHashed(pattern, hostname)) {
                        continue;
                    } else if (negate) {
                        return false;
                    } else {
                        isMatch = true;
                    }
                }
            }
            k++;
        }
        return isMatch;
    }

    private void initialize(char[] knownHostsData) throws IOException {
        BufferedReader br = new BufferedReader(new CharArrayReader(knownHostsData));
        while (true) {
            String line = br.readLine();
            if (line != null) {
                line = line.trim();
                if (!line.startsWith("#")) {
                    String[] arr = line.split(" ");
                    if (arr.length >= 3 && (arr[1].compareTo("ssh-rsa") == 0 || arr[1].compareTo("ssh-dss") == 0)) {
                        addHostkey(arr[0].split(","), arr[1], Base64.decode(arr[2].toCharArray()));
                    }
                }
            } else {
                return;
            }
        }
    }

    private void initialize(File knownHosts) throws IOException {
        char[] buff = new char[512];
        CharArrayWriter cw = new CharArrayWriter();
        knownHosts.createNewFile();
        FileReader fr = new FileReader(knownHosts);
        while (true) {
            int len = fr.read(buff);
            if (len < 0) {
                fr.close();
                initialize(cw.toCharArray());
                return;
            }
            cw.write(buff, 0, len);
        }
    }

    private final boolean matchKeys(Object key1, Object key2) {
        if ((key1 instanceof RSAPublicKey) && (key2 instanceof RSAPublicKey)) {
            RSAPublicKey savedRSAKey = (RSAPublicKey) key1;
            RSAPublicKey remoteRSAKey = (RSAPublicKey) key2;
            if (savedRSAKey.getE().equals(remoteRSAKey.getE()) && savedRSAKey.getN().equals(remoteRSAKey.getN())) {
                return true;
            }
            return false;
        } else if (!(key1 instanceof DSAPublicKey) || !(key2 instanceof DSAPublicKey)) {
            return false;
        } else {
            DSAPublicKey savedDSAKey = (DSAPublicKey) key1;
            DSAPublicKey remoteDSAKey = (DSAPublicKey) key2;
            if (savedDSAKey.getG().equals(remoteDSAKey.getG()) && savedDSAKey.getP().equals(remoteDSAKey.getP()) && savedDSAKey.getQ().equals(remoteDSAKey.getQ()) && savedDSAKey.getY().equals(remoteDSAKey.getY())) {
                return true;
            }
            return false;
        }
    }

    private final boolean pseudoRegex(char[] pattern, int i, char[] match, int j) {
        while (pattern.length != i) {
            if (pattern[i] == '*') {
                i++;
                if (pattern.length == i) {
                    return true;
                }
                if (pattern[i] == '*' || pattern[i] == '?') {
                    while (!pseudoRegex(pattern, i, match, j)) {
                        j++;
                        if (match.length == j) {
                            return false;
                        }
                    }
                    return true;
                }
                do {
                    if (pattern[i] == match[j] && pseudoRegex(pattern, i + 1, match, j + 1)) {
                        return true;
                    }
                    j++;
                } while (match.length != j);
                return false;
            } else if (match.length == j) {
                return false;
            } else {
                if (pattern[i] != '?' && pattern[i] != match[j]) {
                    return false;
                }
                i++;
                j++;
            }
        }
        if (match.length == j) {
            return true;
        }
        return false;
    }

    private String[] recommendHostkeyAlgorithms(String hostname) {
        String preferredAlgo = null;
        Vector keys = getAllKeys(hostname);
        for (int i = 0; i < keys.size(); i++) {
            String thisAlgo;
            if (keys.elementAt(i) instanceof RSAPublicKey) {
                thisAlgo = "ssh-rsa";
            } else if (keys.elementAt(i) instanceof DSAPublicKey) {
                thisAlgo = "ssh-dss";
            } else {
                continue;
            }
            if (!(preferredAlgo == null || preferredAlgo.compareTo(thisAlgo) == 0)) {
                return null;
            }
        }
        if (preferredAlgo == null) {
            return null;
        }
        if (preferredAlgo.equals("ssh-rsa")) {
            return new String[]{"ssh-rsa", "ssh-dss"};
        }
        return new String[]{"ssh-dss", "ssh-rsa"};
    }

    public int verifyHostkey(String hostname, String serverHostKeyAlgorithm, byte[] serverHostKey) throws IOException {
        Object remoteKey;
        if ("ssh-rsa".equals(serverHostKeyAlgorithm)) {
            remoteKey = RSASHA1Verify.decodeSSHRSAPublicKey(serverHostKey);
        } else if ("ssh-dss".equals(serverHostKeyAlgorithm)) {
            remoteKey = DSASHA1Verify.decodeSSHDSAPublicKey(serverHostKey);
        } else {
            throw new IllegalArgumentException("Unknown hostkey type " + serverHostKeyAlgorithm);
        }
        int result = checkKey(hostname, remoteKey);
        if (result == 0) {
            return result;
        }
        InetAddress[] inetAddressArr = (InetAddress[]) null;
        try {
            inetAddressArr = InetAddress.getAllByName(hostname);
            for (InetAddress hostAddress : inetAddressArr) {
                int newresult = checkKey(hostAddress.getHostAddress(), remoteKey);
                if (newresult == 0) {
                    return newresult;
                }
                if (newresult == 2) {
                    result = 2;
                }
            }
            return result;
        } catch (UnknownHostException e) {
            return result;
        }
    }

    public static final void addHostkeyToFile(File knownHosts, String[] hostnames, String serverHostKeyAlgorithm, byte[] serverHostKey) throws IOException {
        if (hostnames == null || hostnames.length == 0) {
            throw new IllegalArgumentException("Need at least one hostname specification");
        } else if (serverHostKeyAlgorithm == null || serverHostKey == null) {
            throw new IllegalArgumentException();
        } else {
            CharArrayWriter writer = new CharArrayWriter();
            for (int i = 0; i < hostnames.length; i++) {
                if (i != 0) {
                    writer.write(44);
                }
                writer.write(hostnames[i]);
            }
            writer.write(32);
            writer.write(serverHostKeyAlgorithm);
            writer.write(32);
            writer.write(Base64.encode(serverHostKey));
            writer.write("\n");
            char[] entry = writer.toCharArray();
            RandomAccessFile raf = new RandomAccessFile(knownHosts, "rw");
            long len = raf.length();
            if (len > 0) {
                raf.seek(len - 1);
                if (raf.read() != 10) {
                    raf.write(10);
                }
            }
            raf.write(new String(entry).getBytes("ISO-8859-1"));
            raf.close();
        }
    }

    private static final byte[] rawFingerPrint(String type, String keyType, byte[] hostkey) {
        Digest dig;
        if ("md5".equals(type)) {
            dig = new MD5();
        } else if ("sha1".equals(type)) {
            dig = new SHA1();
        } else {
            throw new IllegalArgumentException("Unknown hash type " + type);
        }
        if (!"ssh-rsa".equals(keyType) && !"ssh-dss".equals(keyType)) {
            throw new IllegalArgumentException("Unknown key type " + keyType);
        } else if (hostkey == null) {
            throw new IllegalArgumentException("hostkey is null");
        } else {
            dig.update(hostkey);
            byte[] res = new byte[dig.getDigestLength()];
            dig.digest(res);
            return res;
        }
    }

    private static final String rawToHexFingerprint(byte[] fingerprint) {
        char[] alpha = "0123456789abcdef".toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < fingerprint.length; i++) {
            if (i != 0) {
                sb.append(':');
            }
            int b = fingerprint[i] & 255;
            sb.append(alpha[b >> 4]);
            sb.append(alpha[b & 15]);
        }
        return sb.toString();
    }

    private static final String rawToBubblebabbleFingerprint(byte[] raw) {
        char[] v = "aeiouy".toCharArray();
        char[] c = "bcdfghklmnprstvzx".toCharArray();
        StringBuffer sb = new StringBuffer();
        int seed = 1;
        int rounds = (raw.length / 2) + 1;
        sb.append('x');
        for (int i = 0; i < rounds; i++) {
            if (i + 1 < rounds || raw.length % 2 != 0) {
                sb.append(v[(((raw[i * 2] >> 6) & 3) + seed) % 6]);
                sb.append(c[(raw[i * 2] >> 2) & 15]);
                sb.append(v[((raw[i * 2] & 3) + (seed / 6)) % 6]);
                if (i + 1 < rounds) {
                    sb.append(c[(raw[(i * 2) + 1] >> 4) & 15]);
                    sb.append('-');
                    sb.append(c[raw[(i * 2) + 1] & 15]);
                    seed = ((seed * 5) + (((raw[i * 2] & 255) * 7) + (raw[(i * 2) + 1] & 255))) % 36;
                }
            } else {
                sb.append(v[seed % 6]);
                sb.append('x');
                sb.append(v[seed / 6]);
            }
        }
        sb.append('x');
        return sb.toString();
    }

    public static final String createHexFingerprint(String keytype, byte[] publickey) {
        return rawToHexFingerprint(rawFingerPrint("md5", keytype, publickey));
    }

    public static final String createBubblebabbleFingerprint(String keytype, byte[] publickey) {
        return rawToBubblebabbleFingerprint(rawFingerPrint("sha1", keytype, publickey));
    }
}
