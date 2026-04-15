package com.sun.mail.smtp;

import android.support.v4.view.MotionEventCompat;
import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StreamTokenizer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.mail.internet.HeaderTokenizer.Token;

public class DigestMD5 {
    private static char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private String clientResponse;
    private PrintStream debugout;
    private MessageDigest md5;
    private String uri;

    public DigestMD5(PrintStream debugout) {
        this.debugout = debugout;
        if (debugout != null) {
            debugout.println("DEBUG DIGEST-MD5: Loaded");
        }
    }

    public byte[] authClient(String host, String user, String passwd, String realm, String serverChallenge) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream b64os = new BASE64EncoderStream(bos, Integer.MAX_VALUE);
        try {
            SecureRandom random = new SecureRandom();
            this.md5 = MessageDigest.getInstance("MD5");
            StringBuffer result = new StringBuffer();
            this.uri = "smtp/" + host;
            String nc = "00000001";
            String qop = "auth";
            byte[] bytes = new byte[32];
            if (this.debugout != null) {
                this.debugout.println("DEBUG DIGEST-MD5: Begin authentication ...");
            }
            Hashtable map = tokenize(serverChallenge);
            if (realm == null) {
                String text = (String) map.get("realm");
                if (text != null) {
                    realm = new StringTokenizer(text, ",").nextToken();
                } else {
                    realm = host;
                }
            }
            String nonce = (String) map.get("nonce");
            random.nextBytes(bytes);
            b64os.write(bytes);
            b64os.flush();
            String cnonce = bos.toString();
            bos.reset();
            this.md5.update(this.md5.digest(ASCIIUtility.getBytes(new StringBuilder(String.valueOf(user)).append(":").append(realm).append(":").append(passwd).toString())));
            this.md5.update(ASCIIUtility.getBytes(":" + nonce + ":" + cnonce));
            this.clientResponse = new StringBuilder(String.valueOf(toHex(this.md5.digest()))).append(":").append(nonce).append(":").append(nc).append(":").append(cnonce).append(":").append(qop).append(":").toString();
            this.md5.update(ASCIIUtility.getBytes("AUTHENTICATE:" + this.uri));
            this.md5.update(ASCIIUtility.getBytes(this.clientResponse + toHex(this.md5.digest())));
            result.append("username=\"" + user + "\"");
            result.append(",realm=\"" + realm + "\"");
            result.append(",qop=" + qop);
            result.append(",nc=" + nc);
            result.append(",nonce=\"" + nonce + "\"");
            result.append(",cnonce=\"" + cnonce + "\"");
            result.append(",digest-uri=\"" + this.uri + "\"");
            result.append(",response=" + toHex(this.md5.digest()));
            if (this.debugout != null) {
                this.debugout.println("DEBUG DIGEST-MD5: Response => " + result.toString());
            }
            b64os.write(ASCIIUtility.getBytes(result.toString()));
            b64os.flush();
            return bos.toByteArray();
        } catch (NoSuchAlgorithmException ex) {
            if (this.debugout != null) {
                this.debugout.println("DEBUG DIGEST-MD5: " + ex);
            }
            throw new IOException(ex.toString());
        }
    }

    public boolean authServer(String serverResponse) throws IOException {
        Hashtable map = tokenize(serverResponse);
        this.md5.update(ASCIIUtility.getBytes(":" + this.uri));
        this.md5.update(ASCIIUtility.getBytes(this.clientResponse + toHex(this.md5.digest())));
        String text = toHex(this.md5.digest());
        if (text.equals((String) map.get("rspauth"))) {
            return true;
        }
        if (this.debugout != null) {
            this.debugout.println("DEBUG DIGEST-MD5: Expected => rspauth=" + text);
        }
        return false;
    }

    private Hashtable tokenize(String serverResponse) throws IOException {
        Hashtable map = new Hashtable();
        byte[] bytes = serverResponse.getBytes();
        String key = null;
        StreamTokenizer tokens = new StreamTokenizer(new InputStreamReader(new BASE64DecoderStream(new ByteArrayInputStream(bytes, 4, bytes.length - 4))));
        tokens.ordinaryChars(48, 57);
        tokens.wordChars(48, 57);
        while (true) {
            int ttype = tokens.nextToken();
            if (ttype != -1) {
                switch (ttype) {
                    case Token.COMMENT /*-3*/:
                        if (key == null) {
                            key = tokens.sval;
                            break;
                        }
                    case 34:
                        if (this.debugout != null) {
                            this.debugout.println("DEBUG DIGEST-MD5: Received => " + key + "='" + tokens.sval + "'");
                        }
                        if (map.containsKey(key)) {
                            map.put(key, map.get(key) + "," + tokens.sval);
                        } else {
                            map.put(key, tokens.sval);
                        }
                        key = null;
                        break;
                    default:
                        break;
                }
            }
            return map;
        }
    }

    private static String toHex(byte[] bytes) {
        char[] result = new char[(bytes.length * 2)];
        int i = 0;
        for (byte b : bytes) {
            int temp = b & MotionEventCompat.ACTION_MASK;
            int i2 = i + 1;
            result[i] = digits[temp >> 4];
            i = i2 + 1;
            result[i2] = digits[temp & 15];
        }
        return new String(result);
    }
}
