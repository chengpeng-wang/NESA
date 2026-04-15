package org.apache.james.mime4j.util;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.james.mime4j.field.ContentTypeField;

public final class MimeUtil {
    public static final String ENC_7BIT = "7bit";
    public static final String ENC_8BIT = "8bit";
    public static final String ENC_BASE64 = "base64";
    public static final String ENC_BINARY = "binary";
    public static final String ENC_QUOTED_PRINTABLE = "quoted-printable";
    public static final String MIME_HEADER_CONTENT_DESCRIPTION = "content-description";
    public static final String MIME_HEADER_CONTENT_DISPOSITION = "content-disposition";
    public static final String MIME_HEADER_CONTENT_ID = "content-id";
    public static final String MIME_HEADER_LANGAUGE = "content-language";
    public static final String MIME_HEADER_LOCATION = "content-location";
    public static final String MIME_HEADER_MD5 = "content-md5";
    public static final String MIME_HEADER_MIME_VERSION = "mime-version";
    public static final String PARAM_CREATION_DATE = "creation-date";
    public static final String PARAM_FILENAME = "filename";
    public static final String PARAM_MODIFICATION_DATE = "modification-date";
    public static final String PARAM_READ_DATE = "read-date";
    public static final String PARAM_SIZE = "size";
    private static final ThreadLocal<DateFormat> RFC822_DATE_FORMAT = new ThreadLocal<DateFormat>() {
        /* access modifiers changed from: protected */
        public DateFormat initialValue() {
            return new Rfc822DateFormat();
        }
    };
    private static int counter = 0;
    private static final Log log = LogFactory.getLog(MimeUtil.class);
    private static final Random random = new Random();

    private static final class Rfc822DateFormat extends SimpleDateFormat {
        private static final long serialVersionUID = 1;

        public Rfc822DateFormat() {
            super("EEE, d MMM yyyy HH:mm:ss ", Locale.US);
        }

        public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
            StringBuffer sb = super.format(date, toAppendTo, pos);
            int minutes = ((this.calendar.get(15) + this.calendar.get(16)) / 1000) / 60;
            if (minutes < 0) {
                sb.append('-');
                minutes = -minutes;
            } else {
                sb.append('+');
            }
            sb.append(String.format("%02d%02d", new Object[]{Integer.valueOf(minutes / 60), Integer.valueOf(minutes % 60)}));
            return sb;
        }
    }

    private MimeUtil() {
    }

    public static boolean isSameMimeType(String pType1, String pType2) {
        return (pType1 == null || pType2 == null || !pType1.equalsIgnoreCase(pType2)) ? false : true;
    }

    public static boolean isMessage(String pMimeType) {
        return pMimeType != null && pMimeType.equalsIgnoreCase(ContentTypeField.TYPE_MESSAGE_RFC822);
    }

    public static boolean isMultipart(String pMimeType) {
        return pMimeType != null && pMimeType.toLowerCase().startsWith(ContentTypeField.TYPE_MULTIPART_PREFIX);
    }

    public static boolean isBase64Encoding(String pTransferEncoding) {
        return ENC_BASE64.equalsIgnoreCase(pTransferEncoding);
    }

    public static boolean isQuotedPrintableEncoded(String pTransferEncoding) {
        return ENC_QUOTED_PRINTABLE.equalsIgnoreCase(pTransferEncoding);
    }

    public static java.util.Map<java.lang.String, java.lang.String> getHeaderParams(java.lang.String r25) {
        /*
        r25 = r25.trim();
        r25 = unfold(r25);
        r21 = new java.util.HashMap;
        r21.<init>();
        r23 = ";";
        r0 = r25;
        r1 = r23;
        r23 = r0.indexOf(r1);
        r24 = -1;
        r0 = r23;
        r1 = r24;
        if (r0 != r1) goto L_0x0066;
    L_0x001f:
        r17 = r25;
        r20 = 0;
    L_0x0023:
        r23 = "";
        r0 = r21;
        r1 = r23;
        r2 = r17;
        r0.put(r1, r2);
        if (r20 == 0) goto L_0x0187;
    L_0x0030:
        r12 = r20.toCharArray();
        r18 = new java.lang.StringBuilder;
        r23 = 64;
        r0 = r18;
        r1 = r23;
        r0.<init>(r1);
        r19 = new java.lang.StringBuilder;
        r23 = 64;
        r0 = r19;
        r1 = r23;
        r0.<init>(r1);
        r7 = 0;
        r4 = 1;
        r8 = 2;
        r6 = 3;
        r5 = 4;
        r9 = 5;
        r3 = 99;
        r22 = 0;
        r13 = 0;
        r10 = r12;
        r0 = r10.length;
        r16 = r0;
        r15 = 0;
    L_0x005a:
        r0 = r16;
        if (r15 >= r0) goto L_0x0162;
    L_0x005e:
        r11 = r10[r15];
        switch(r22) {
            case 0: goto L_0x0094;
            case 1: goto L_0x00b8;
            case 2: goto L_0x00d0;
            case 3: goto L_0x00d9;
            case 4: goto L_0x0113;
            case 5: goto L_0x00e4;
            case 99: goto L_0x008b;
            default: goto L_0x0063;
        };
    L_0x0063:
        r15 = r15 + 1;
        goto L_0x005a;
    L_0x0066:
        r23 = 0;
        r24 = ";";
        r0 = r25;
        r1 = r24;
        r24 = r0.indexOf(r1);
        r0 = r25;
        r1 = r23;
        r2 = r24;
        r17 = r0.substring(r1, r2);
        r23 = r17.length();
        r23 = r23 + 1;
        r0 = r25;
        r1 = r23;
        r20 = r0.substring(r1);
        goto L_0x0023;
    L_0x008b:
        r23 = 59;
        r0 = r23;
        if (r11 != r0) goto L_0x0063;
    L_0x0091:
        r22 = 0;
        goto L_0x0063;
    L_0x0094:
        r23 = 61;
        r0 = r23;
        if (r11 != r0) goto L_0x00a4;
    L_0x009a:
        r23 = log;
        r24 = "Expected header param name, got '='";
        r23.error(r24);
        r22 = 99;
        goto L_0x0063;
    L_0x00a4:
        r23 = 0;
        r0 = r18;
        r1 = r23;
        r0.setLength(r1);
        r23 = 0;
        r0 = r19;
        r1 = r23;
        r0.setLength(r1);
        r22 = 1;
    L_0x00b8:
        r23 = 61;
        r0 = r23;
        if (r11 != r0) goto L_0x00ca;
    L_0x00be:
        r23 = r18.length();
        if (r23 != 0) goto L_0x00c7;
    L_0x00c4:
        r22 = 99;
        goto L_0x0063;
    L_0x00c7:
        r22 = 2;
        goto L_0x0063;
    L_0x00ca:
        r0 = r18;
        r0.append(r11);
        goto L_0x0063;
    L_0x00d0:
        r14 = 0;
        switch(r11) {
            case 9: goto L_0x00d7;
            case 32: goto L_0x00d7;
            case 34: goto L_0x00eb;
            default: goto L_0x00d4;
        };
    L_0x00d4:
        r22 = 3;
        r14 = 1;
    L_0x00d7:
        if (r14 == 0) goto L_0x0063;
    L_0x00d9:
        r14 = 0;
        switch(r11) {
            case 9: goto L_0x00ee;
            case 32: goto L_0x00ee;
            case 59: goto L_0x00ee;
            default: goto L_0x00dd;
        };
    L_0x00dd:
        r0 = r19;
        r0.append(r11);
    L_0x00e2:
        if (r14 == 0) goto L_0x0063;
    L_0x00e4:
        switch(r11) {
            case 9: goto L_0x0063;
            case 32: goto L_0x0063;
            case 59: goto L_0x010f;
            default: goto L_0x00e7;
        };
    L_0x00e7:
        r22 = 99;
        goto L_0x0063;
    L_0x00eb:
        r22 = 4;
        goto L_0x00d7;
    L_0x00ee:
        r23 = r18.toString();
        r23 = r23.trim();
        r23 = r23.toLowerCase();
        r24 = r19.toString();
        r24 = r24.trim();
        r0 = r21;
        r1 = r23;
        r2 = r24;
        r0.put(r1, r2);
        r22 = 5;
        r14 = 1;
        goto L_0x00e2;
    L_0x010f:
        r22 = 0;
        goto L_0x0063;
    L_0x0113:
        switch(r11) {
            case 34: goto L_0x0129;
            case 92: goto L_0x0150;
            default: goto L_0x0116;
        };
    L_0x0116:
        if (r13 == 0) goto L_0x0121;
    L_0x0118:
        r23 = 92;
        r0 = r19;
        r1 = r23;
        r0.append(r1);
    L_0x0121:
        r13 = 0;
        r0 = r19;
        r0.append(r11);
        goto L_0x0063;
    L_0x0129:
        if (r13 != 0) goto L_0x0148;
    L_0x012b:
        r23 = r18.toString();
        r23 = r23.trim();
        r23 = r23.toLowerCase();
        r24 = r19.toString();
        r0 = r21;
        r1 = r23;
        r2 = r24;
        r0.put(r1, r2);
        r22 = 5;
        goto L_0x0063;
    L_0x0148:
        r13 = 0;
        r0 = r19;
        r0.append(r11);
        goto L_0x0063;
    L_0x0150:
        if (r13 == 0) goto L_0x015b;
    L_0x0152:
        r23 = 92;
        r0 = r19;
        r1 = r23;
        r0.append(r1);
    L_0x015b:
        if (r13 != 0) goto L_0x0160;
    L_0x015d:
        r13 = 1;
    L_0x015e:
        goto L_0x0063;
    L_0x0160:
        r13 = 0;
        goto L_0x015e;
    L_0x0162:
        r23 = 3;
        r0 = r22;
        r1 = r23;
        if (r0 != r1) goto L_0x0187;
    L_0x016a:
        r23 = r18.toString();
        r23 = r23.trim();
        r23 = r23.toLowerCase();
        r24 = r19.toString();
        r24 = r24.trim();
        r0 = r21;
        r1 = r23;
        r2 = r24;
        r0.put(r1, r2);
    L_0x0187:
        return r21;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.james.mime4j.util.MimeUtil.getHeaderParams(java.lang.String):java.util.Map");
    }

    public static String createUniqueBoundary() {
        StringBuilder sb = new StringBuilder();
        sb.append("-=Part.");
        sb.append(Integer.toHexString(nextCounterValue()));
        sb.append('.');
        sb.append(Long.toHexString(random.nextLong()));
        sb.append('.');
        sb.append(Long.toHexString(System.currentTimeMillis()));
        sb.append('.');
        sb.append(Long.toHexString(random.nextLong()));
        sb.append("=-");
        return sb.toString();
    }

    public static String createUniqueMessageId(String hostName) {
        StringBuilder sb = new StringBuilder("<Mime4j.");
        sb.append(Integer.toHexString(nextCounterValue()));
        sb.append('.');
        sb.append(Long.toHexString(random.nextLong()));
        sb.append('.');
        sb.append(Long.toHexString(System.currentTimeMillis()));
        if (hostName != null) {
            sb.append('@');
            sb.append(hostName);
        }
        sb.append('>');
        return sb.toString();
    }

    public static String formatDate(Date date, TimeZone zone) {
        DateFormat df = (DateFormat) RFC822_DATE_FORMAT.get();
        if (zone == null) {
            df.setTimeZone(TimeZone.getDefault());
        } else {
            df.setTimeZone(zone);
        }
        return df.format(date);
    }

    public static String fold(String s, int usedCharacters) {
        int length = s.length();
        if (usedCharacters + length <= 76) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        int lastLineBreak = -usedCharacters;
        int wspIdx = indexOfWsp(s, 0);
        while (wspIdx != length) {
            int nextWspIdx = indexOfWsp(s, wspIdx + 1);
            if (nextWspIdx - lastLineBreak > 76) {
                sb.append(s.substring(Math.max(0, lastLineBreak), wspIdx));
                sb.append(CharsetUtil.CRLF);
                lastLineBreak = wspIdx;
            }
            wspIdx = nextWspIdx;
        }
        sb.append(s.substring(Math.max(0, lastLineBreak)));
        return sb.toString();
    }

    public static String unfold(String s) {
        int length = s.length();
        for (int idx = 0; idx < length; idx++) {
            char c = s.charAt(idx);
            if (c == 13 || c == 10) {
                return unfold0(s, idx);
            }
        }
        return s;
    }

    private static String unfold0(String s, int crlfIdx) {
        int length = s.length();
        StringBuilder sb = new StringBuilder(length);
        if (crlfIdx > 0) {
            sb.append(s.substring(0, crlfIdx));
        }
        for (int idx = crlfIdx + 1; idx < length; idx++) {
            char c = s.charAt(idx);
            if (!(c == 13 || c == 10)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static int indexOfWsp(String s, int fromIndex) {
        int len = s.length();
        int index = fromIndex;
        while (index < len) {
            char c = s.charAt(index);
            if (c == ' ' || c == 9) {
                return index;
            }
            index++;
        }
        return len;
    }

    private static synchronized int nextCounterValue() {
        int i;
        synchronized (MimeUtil.class) {
            i = counter;
            counter = i + 1;
        }
        return i;
    }
}
