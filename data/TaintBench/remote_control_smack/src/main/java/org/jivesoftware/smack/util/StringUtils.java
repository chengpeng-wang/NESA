package org.jivesoftware.smack.util;

import android.support.v4.view.MotionEventCompat;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.cloud.GeoSearchManager;
import com.baidu.mapapi.map.MapView.LayoutParams;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class StringUtils {
    private static final char[] AMP_ENCODE = "&amp;".toCharArray();
    private static final char[] GT_ENCODE = "&gt;".toCharArray();
    private static final char[] LT_ENCODE = "&lt;".toCharArray();
    private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();
    private static MessageDigest digest = null;
    private static char[] numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static Random randGen = new Random();

    public static String parseName(String XMPPAddress) {
        if (XMPPAddress == null) {
            return null;
        }
        int atIndex = XMPPAddress.lastIndexOf("@");
        if (atIndex <= 0) {
            return "";
        }
        return XMPPAddress.substring(0, atIndex);
    }

    public static String parseServer(String XMPPAddress) {
        if (XMPPAddress == null) {
            return null;
        }
        int atIndex = XMPPAddress.lastIndexOf("@");
        if (atIndex + 1 > XMPPAddress.length()) {
            return "";
        }
        int slashIndex = XMPPAddress.indexOf("/");
        if (slashIndex <= 0 || slashIndex <= atIndex) {
            return XMPPAddress.substring(atIndex + 1);
        }
        return XMPPAddress.substring(atIndex + 1, slashIndex);
    }

    public static String parseResource(String XMPPAddress) {
        if (XMPPAddress == null) {
            return null;
        }
        int slashIndex = XMPPAddress.indexOf("/");
        if (slashIndex + 1 > XMPPAddress.length() || slashIndex < 0) {
            return "";
        }
        return XMPPAddress.substring(slashIndex + 1);
    }

    public static String parseBareAddress(String XMPPAddress) {
        if (XMPPAddress == null) {
            return null;
        }
        int slashIndex = XMPPAddress.indexOf("/");
        if (slashIndex < 0) {
            return XMPPAddress;
        }
        if (slashIndex == 0) {
            return "";
        }
        return XMPPAddress.substring(0, slashIndex);
    }

    public static String escapeNode(String node) {
        if (node == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(node.length() + 8);
        int n = node.length();
        for (int i = 0; i < n; i++) {
            char c = node.charAt(i);
            switch (c) {
                case '\"':
                    buf.append("\\22");
                    break;
                case '&':
                    buf.append("\\26");
                    break;
                case '\'':
                    buf.append("\\27");
                    break;
                case '/':
                    buf.append("\\2f");
                    break;
                case ':':
                    buf.append("\\3a");
                    break;
                case '<':
                    buf.append("\\3c");
                    break;
                case BDLocation.TypeCriteriaException /*62*/:
                    buf.append("\\3e");
                    break;
                case '@':
                    buf.append("\\40");
                    break;
                case '\\':
                    buf.append("\\5c");
                    break;
                default:
                    if (!Character.isWhitespace(c)) {
                        buf.append(c);
                        break;
                    }
                    buf.append("\\20");
                    break;
            }
        }
        return buf.toString();
    }

    public static String unescapeNode(String node) {
        if (node == null) {
            return null;
        }
        char[] nodeChars = node.toCharArray();
        StringBuilder buf = new StringBuilder(nodeChars.length);
        int i = 0;
        int n = nodeChars.length;
        while (i < n) {
            char c = node.charAt(i);
            if (c == '\\' && i + 2 < n) {
                char c2 = nodeChars[i + 1];
                char c3 = nodeChars[i + 2];
                if (c2 == '2') {
                    switch (c3) {
                        case LayoutParams.TOP /*48*/:
                            buf.append(' ');
                            i += 2;
                            continue;
                        case GeoSearchManager.GEO_SEARCH /*50*/:
                            buf.append('\"');
                            i += 2;
                            continue;
                        case '6':
                            buf.append('&');
                            i += 2;
                            continue;
                        case '7':
                            buf.append('\'');
                            i += 2;
                            continue;
                        case 'f':
                            buf.append('/');
                            i += 2;
                            continue;
                    }
                } else if (c2 == '3') {
                    switch (c3) {
                        case 'a':
                            buf.append(':');
                            i += 2;
                            continue;
                        case 'c':
                            buf.append('<');
                            i += 2;
                            continue;
                        case 'e':
                            buf.append('>');
                            i += 2;
                            continue;
                    }
                } else if (c2 == '4') {
                    if (c3 == '0') {
                        buf.append("@");
                        i += 2;
                        i++;
                    }
                } else if (c2 == '5' && c3 == 'c') {
                    buf.append("\\");
                    i += 2;
                    i++;
                }
            }
            buf.append(c);
            i++;
        }
        return buf.toString();
    }

    public static String escapeForXML(String string) {
        if (string == null) {
            return null;
        }
        int i = 0;
        int last = 0;
        char[] input = string.toCharArray();
        int len = input.length;
        StringBuilder out = new StringBuilder((int) (((double) len) * 1.3d));
        while (i < len) {
            char ch = input[i];
            if (ch <= '>') {
                if (ch == '<') {
                    if (i > last) {
                        out.append(input, last, i - last);
                    }
                    last = i + 1;
                    out.append(LT_ENCODE);
                } else if (ch == '>') {
                    if (i > last) {
                        out.append(input, last, i - last);
                    }
                    last = i + 1;
                    out.append(GT_ENCODE);
                } else if (ch == '&') {
                    if (i > last) {
                        out.append(input, last, i - last);
                    }
                    if (len <= i + 5 || input[i + 1] != '#' || !Character.isDigit(input[i + 2]) || !Character.isDigit(input[i + 3]) || !Character.isDigit(input[i + 4]) || input[i + 5] != ';') {
                        last = i + 1;
                        out.append(AMP_ENCODE);
                    }
                } else if (ch == '\"') {
                    if (i > last) {
                        out.append(input, last, i - last);
                    }
                    last = i + 1;
                    out.append(QUOTE_ENCODE);
                }
            }
            i++;
        }
        if (last == 0) {
            return string;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
    }

    public static synchronized String hash(String data) {
        String encodeHex;
        synchronized (StringUtils.class) {
            if (digest == null) {
                try {
                    digest = MessageDigest.getInstance("SHA-1");
                } catch (NoSuchAlgorithmException e) {
                    System.err.println("Failed to load the SHA-1 MessageDigest. Jive will be unable to function normally.");
                }
            }
            try {
                digest.update(data.getBytes(StringEncodings.UTF8));
            } catch (UnsupportedEncodingException e2) {
                System.err.println(e2);
            }
            encodeHex = encodeHex(digest.digest());
        }
        return encodeHex;
    }

    public static String encodeHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            if ((aByte & MotionEventCompat.ACTION_MASK) < 16) {
                hex.append("0");
            }
            hex.append(Integer.toString(aByte & MotionEventCompat.ACTION_MASK, 16));
        }
        return hex.toString();
    }

    public static String encodeBase64(String data) {
        byte[] bytes = null;
        try {
            bytes = data.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }
        return encodeBase64(bytes);
    }

    public static String encodeBase64(byte[] data) {
        return encodeBase64(data, false);
    }

    public static String encodeBase64(byte[] data, boolean lineBreaks) {
        return encodeBase64(data, 0, data.length, lineBreaks);
    }

    public static String encodeBase64(byte[] data, int offset, int len, boolean lineBreaks) {
        return Base64.encodeBytes(data, offset, len, lineBreaks ? 0 : 8);
    }

    public static byte[] decodeBase64(String data) {
        return Base64.decode(data);
    }

    public static String randomString(int length) {
        if (length < 1) {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }

    private StringUtils() {
    }
}
