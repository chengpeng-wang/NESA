package com.baidu.inf.iis.bcs.utils;

import com.baidu.inf.iis.bcs.http.BCSHttpRequest;
import com.baidu.inf.iis.bcs.model.BCSClientException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.spi.LocationInfo;

public class ServiceUtils {
    protected static final DateUtils dateUtils = new DateUtils();
    private static final Log log = LogFactory.getLog(ServiceUtils.class);

    public static byte[] computeMD5Hash(byte[] bArr) throws NoSuchAlgorithmException, IOException {
        return computeMD5Hash(new ByteArrayInputStream(bArr));
    }

    public static byte[] computeMD5Hash(InputStream inputStream) throws NoSuchAlgorithmException, IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            byte[] bArr = new byte[16384];
            while (true) {
                int read = bufferedInputStream.read(bArr, 0, bArr.length);
                if (read == -1) {
                    break;
                }
                instance.update(bArr, 0, read);
            }
            byte[] digest = instance.digest();
            return digest;
        } finally {
            try {
                bufferedInputStream.close();
            } catch (Exception e) {
                log.warn("Unable to close input stream of hash candidate: " + e);
            }
        }
    }

    public static URL convertRequestToUrl(BCSHttpRequest bCSHttpRequest) {
        String str = bCSHttpRequest.getEndpoint() + "/" + bCSHttpRequest.getResourcePath();
        String str2 = str;
        Object obj = 1;
        for (String str3 : bCSHttpRequest.getParameters().keySet()) {
            Object obj2;
            String str4;
            if (obj != null) {
                obj2 = null;
                str4 = str2 + LocationInfo.NA;
            } else {
                str4 = str2 + "&";
                obj2 = obj;
            }
            obj = obj2;
            str2 = str4 + str3 + "=" + urlEncode((String) bCSHttpRequest.getParameters().get(str3));
        }
        try {
            return new URL(str2);
        } catch (MalformedURLException e) {
            throw new BCSClientException("Unable to convert request to well formed URL: " + e.getMessage(), e);
        }
    }

    public static String formatIso8601Date(Date date) {
        return dateUtils.formatIso8601Date(date);
    }

    public static String formatRfc822Date(Date date) {
        return dateUtils.formatRfc822Date(date);
    }

    public static byte[] fromBase64(String str) {
        try {
            return Base64.decodeBase64(str.getBytes(Constants.DEFAULT_ENCODING));
        } catch (UnsupportedEncodingException e) {
            log.warn("Tried to Base64-decode a String with the wrong encoding: ", e);
            return Base64.decodeBase64(str.getBytes());
        }
    }

    public static byte[] fromHex(String str) {
        int i = 0;
        byte[] bArr = new byte[((str.length() + 1) / 2)];
        int i2 = 0;
        while (i2 < str.length()) {
            String substring = str.substring(i2, i2 + 2);
            int i3 = i2 + 2;
            i2 = i + 1;
            bArr[i] = (byte) Integer.parseInt(substring, 16);
            i = i2;
            i2 = i3;
        }
        return bArr;
    }

    public static String join(List<String> list) {
        String str = "";
        Object obj = 1;
        Iterator it = list.iterator();
        while (true) {
            Object obj2 = obj;
            if (!it.hasNext()) {
                return str;
            }
            String str2 = (String) it.next();
            if (obj2 == null) {
                str = str + ", ";
            }
            str = str + str2;
            obj = null;
        }
    }

    public static Date parseIso8601Date(String str) throws ParseException {
        return dateUtils.parseIso8601Date(str);
    }

    public static Date parseRfc822Date(String str) throws ParseException {
        return dateUtils.parseRfc822Date(str);
    }

    public static String removeQuotes(String str) {
        if (str == null) {
            return null;
        }
        String trim = str.trim();
        if (trim.startsWith("\"")) {
            trim = trim.substring(1);
        }
        if (trim.endsWith("\"")) {
            return trim.substring(0, trim.length() - 1);
        }
        return trim;
    }

    public static String toBase64(byte[] bArr) {
        return new String(Base64.encodeBase64(bArr));
    }

    public static byte[] toByteArray(String str) {
        try {
            return str.getBytes(Constants.DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            log.warn("Encoding " + Constants.DEFAULT_ENCODING + " is not supported", e);
            return str.getBytes();
        }
    }

    public static String toHex(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder(bArr.length * 2);
        for (byte toHexString : bArr) {
            String toHexString2 = Integer.toHexString(toHexString);
            if (toHexString2.length() == 1) {
                stringBuilder.append("0");
            } else if (toHexString2.length() == 8) {
                toHexString2 = toHexString2.substring(6);
            }
            stringBuilder.append(toHexString2);
        }
        return stringBuilder.toString().toLowerCase(Locale.getDefault());
    }

    public static String urlEncode(String str) {
        if (str == null) {
            return null;
        }
        try {
            return URLEncoder.encode(str, Constants.DEFAULT_ENCODING).replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new BCSClientException("Unable to encode path: " + str, e);
        }
    }
}
