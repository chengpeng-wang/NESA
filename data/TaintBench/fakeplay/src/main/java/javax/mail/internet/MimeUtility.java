package javax.mail.internet;

import android.support.v4.view.MotionEventCompat;
import com.googleprojects.mm.MMMailContentUtil;
import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;
import com.sun.mail.util.BEncoderStream;
import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.QDecoderStream;
import com.sun.mail.util.QEncoderStream;
import com.sun.mail.util.QPDecoderStream;
import com.sun.mail.util.QPEncoderStream;
import com.sun.mail.util.UUDecoderStream;
import com.sun.mail.util.UUEncoderStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;

public class MimeUtility {
    public static final int ALL = -1;
    static final int ALL_ASCII = 1;
    static final int MOSTLY_ASCII = 2;
    static final int MOSTLY_NONASCII = 3;
    private static boolean decodeStrict;
    private static String defaultJavaCharset;
    private static String defaultMIMECharset;
    private static boolean encodeEolStrict;
    private static boolean foldEncodedWords;
    private static boolean foldText;
    private static Hashtable java2mime = new Hashtable(40);
    private static Hashtable mime2java = new Hashtable(10);

    private MimeUtility() {
    }

    static {
        Throwable th;
        boolean z = false;
        decodeStrict = true;
        encodeEolStrict = false;
        foldEncodedWords = false;
        foldText = true;
        try {
            String s = System.getProperty("mail.mime.decodetext.strict");
            boolean z2 = s == null || !s.equalsIgnoreCase("false");
            decodeStrict = z2;
            s = System.getProperty("mail.mime.encodeeol.strict");
            if (s == null || !s.equalsIgnoreCase("true")) {
                z2 = false;
            } else {
                z2 = true;
            }
            encodeEolStrict = z2;
            s = System.getProperty("mail.mime.foldencodedwords");
            if (s == null || !s.equalsIgnoreCase("true")) {
                z2 = false;
            } else {
                z2 = true;
            }
            foldEncodedWords = z2;
            s = System.getProperty("mail.mime.foldtext");
            if (s == null || !s.equalsIgnoreCase("false")) {
                z = true;
            }
            foldText = z;
        } catch (SecurityException e) {
        }
        try {
            InputStream is = MimeUtility.class.getResourceAsStream("/META-INF/javamail.charset.map");
            if (is != null) {
                try {
                    InputStream is2 = new LineInputStream(is);
                    try {
                        loadMappings((LineInputStream) is2, java2mime);
                        loadMappings((LineInputStream) is2, mime2java);
                        try {
                            is2.close();
                        } catch (Exception e2) {
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        is = is2;
                        try {
                            is.close();
                        } catch (Exception e3) {
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    is.close();
                    throw th;
                }
            }
        } catch (Exception e4) {
        }
        if (java2mime.isEmpty()) {
            java2mime.put("8859_1", "ISO-8859-1");
            java2mime.put("iso8859_1", "ISO-8859-1");
            java2mime.put("iso8859-1", "ISO-8859-1");
            java2mime.put("8859_2", "ISO-8859-2");
            java2mime.put("iso8859_2", "ISO-8859-2");
            java2mime.put("iso8859-2", "ISO-8859-2");
            java2mime.put("8859_3", "ISO-8859-3");
            java2mime.put("iso8859_3", "ISO-8859-3");
            java2mime.put("iso8859-3", "ISO-8859-3");
            java2mime.put("8859_4", "ISO-8859-4");
            java2mime.put("iso8859_4", "ISO-8859-4");
            java2mime.put("iso8859-4", "ISO-8859-4");
            java2mime.put("8859_5", "ISO-8859-5");
            java2mime.put("iso8859_5", "ISO-8859-5");
            java2mime.put("iso8859-5", "ISO-8859-5");
            java2mime.put("8859_6", "ISO-8859-6");
            java2mime.put("iso8859_6", "ISO-8859-6");
            java2mime.put("iso8859-6", "ISO-8859-6");
            java2mime.put("8859_7", "ISO-8859-7");
            java2mime.put("iso8859_7", "ISO-8859-7");
            java2mime.put("iso8859-7", "ISO-8859-7");
            java2mime.put("8859_8", "ISO-8859-8");
            java2mime.put("iso8859_8", "ISO-8859-8");
            java2mime.put("iso8859-8", "ISO-8859-8");
            java2mime.put("8859_9", "ISO-8859-9");
            java2mime.put("iso8859_9", "ISO-8859-9");
            java2mime.put("iso8859-9", "ISO-8859-9");
            java2mime.put("sjis", "Shift_JIS");
            java2mime.put("jis", "ISO-2022-JP");
            java2mime.put("iso2022jp", "ISO-2022-JP");
            java2mime.put("euc_jp", "euc-jp");
            java2mime.put("koi8_r", "koi8-r");
            java2mime.put("euc_cn", "euc-cn");
            java2mime.put("euc_tw", "euc-tw");
            java2mime.put("euc_kr", "euc-kr");
        }
        if (mime2java.isEmpty()) {
            mime2java.put("iso-2022-cn", "ISO2022CN");
            mime2java.put("iso-2022-kr", "ISO2022KR");
            mime2java.put("utf-8", "UTF8");
            mime2java.put("utf8", "UTF8");
            mime2java.put("ja_jp.iso2022-7", "ISO2022JP");
            mime2java.put("ja_jp.eucjp", "EUCJIS");
            mime2java.put("euc-kr", "KSC5601");
            mime2java.put("euckr", "KSC5601");
            mime2java.put("us-ascii", "ISO-8859-1");
            mime2java.put("x-us-ascii", "ISO-8859-1");
        }
    }

    public static String getEncoding(DataSource ds) {
        try {
            ContentType cType = new ContentType(ds.getContentType());
            ContentType contentType;
            try {
                String encoding;
                InputStream is = ds.getInputStream();
                switch (checkAscii(is, -1, !cType.match("text/*"))) {
                    case 1:
                        encoding = "7bit";
                        break;
                    case 2:
                        encoding = "quoted-printable";
                        break;
                    default:
                        encoding = "base64";
                        break;
                }
                try {
                    is.close();
                } catch (IOException e) {
                }
                contentType = cType;
                return encoding;
            } catch (Exception e2) {
                contentType = cType;
                return "base64";
            }
        } catch (Exception e3) {
            return "base64";
        }
    }

    public static String getEncoding(DataHandler dh) {
        if (dh.getName() != null) {
            return getEncoding(dh.getDataSource());
        }
        try {
            String encoding;
            ContentType cType = new ContentType(dh.getContentType());
            AsciiOutputStream aos;
            if (cType.match("text/*")) {
                aos = new AsciiOutputStream(false, false);
                try {
                    dh.writeTo(aos);
                } catch (IOException e) {
                }
                switch (aos.getAscii()) {
                    case 1:
                        encoding = "7bit";
                        break;
                    case 2:
                        encoding = "quoted-printable";
                        break;
                    default:
                        encoding = "base64";
                        break;
                }
            }
            aos = new AsciiOutputStream(true, encodeEolStrict);
            try {
                dh.writeTo(aos);
            } catch (IOException e2) {
            }
            if (aos.getAscii() == 1) {
                encoding = "7bit";
            } else {
                encoding = "base64";
            }
            ContentType contentType = cType;
            return encoding;
        } catch (Exception e3) {
            return "base64";
        }
    }

    public static InputStream decode(InputStream is, String encoding) throws MessagingException {
        if (encoding.equalsIgnoreCase("base64")) {
            return new BASE64DecoderStream(is);
        }
        if (encoding.equalsIgnoreCase("quoted-printable")) {
            return new QPDecoderStream(is);
        }
        if (encoding.equalsIgnoreCase("uuencode") || encoding.equalsIgnoreCase("x-uuencode") || encoding.equalsIgnoreCase("x-uue")) {
            return new UUDecoderStream(is);
        }
        if (encoding.equalsIgnoreCase("binary") || encoding.equalsIgnoreCase("7bit") || encoding.equalsIgnoreCase("8bit")) {
            return is;
        }
        throw new MessagingException("Unknown encoding: " + encoding);
    }

    public static OutputStream encode(OutputStream os, String encoding) throws MessagingException {
        if (encoding == null) {
            return os;
        }
        if (encoding.equalsIgnoreCase("base64")) {
            return new BASE64EncoderStream(os);
        }
        if (encoding.equalsIgnoreCase("quoted-printable")) {
            return new QPEncoderStream(os);
        }
        if (encoding.equalsIgnoreCase("uuencode") || encoding.equalsIgnoreCase("x-uuencode") || encoding.equalsIgnoreCase("x-uue")) {
            return new UUEncoderStream(os);
        }
        if (encoding.equalsIgnoreCase("binary") || encoding.equalsIgnoreCase("7bit") || encoding.equalsIgnoreCase("8bit")) {
            return os;
        }
        throw new MessagingException("Unknown encoding: " + encoding);
    }

    public static OutputStream encode(OutputStream os, String encoding, String filename) throws MessagingException {
        if (encoding == null) {
            return os;
        }
        if (encoding.equalsIgnoreCase("base64")) {
            return new BASE64EncoderStream(os);
        }
        if (encoding.equalsIgnoreCase("quoted-printable")) {
            return new QPEncoderStream(os);
        }
        if (encoding.equalsIgnoreCase("uuencode") || encoding.equalsIgnoreCase("x-uuencode") || encoding.equalsIgnoreCase("x-uue")) {
            return new UUEncoderStream(os, filename);
        }
        if (encoding.equalsIgnoreCase("binary") || encoding.equalsIgnoreCase("7bit") || encoding.equalsIgnoreCase("8bit")) {
            return os;
        }
        throw new MessagingException("Unknown encoding: " + encoding);
    }

    public static String encodeText(String text) throws UnsupportedEncodingException {
        return encodeText(text, null, null);
    }

    public static String encodeText(String text, String charset, String encoding) throws UnsupportedEncodingException {
        return encodeWord(text, charset, encoding, false);
    }

    public static String decodeText(String etext) throws UnsupportedEncodingException {
        String word;
        String lwsp = " \t\n\r";
        if (etext.indexOf("=?") == -1) {
            return etext;
        }
        StringTokenizer st = new StringTokenizer(etext, lwsp, true);
        StringBuffer sb = new StringBuffer();
        StringBuffer wsb = new StringBuffer();
        boolean prevWasEncoded = false;
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            char c = s.charAt(0);
            if (c == ' ' || c == 9 || c == 13 || c == 10) {
                wsb.append(c);
            } else {
                try {
                    word = decodeWord(s);
                    if (!prevWasEncoded && wsb.length() > 0) {
                        sb.append(wsb);
                    }
                    prevWasEncoded = true;
                } catch (ParseException e) {
                    word = s;
                    if (decodeStrict) {
                        if (wsb.length() > 0) {
                            sb.append(wsb);
                        }
                        prevWasEncoded = false;
                    } else {
                        String dword = decodeInnerWords(word);
                        if (dword != word) {
                            if (!(prevWasEncoded && word.startsWith("=?")) && wsb.length() > 0) {
                                sb.append(wsb);
                            }
                            prevWasEncoded = word.endsWith("?=");
                            word = dword;
                        } else {
                            if (wsb.length() > 0) {
                                sb.append(wsb);
                            }
                            prevWasEncoded = false;
                        }
                    }
                }
                sb.append(word);
                wsb.setLength(0);
            }
        }
        sb.append(wsb);
        return sb.toString();
    }

    public static String encodeWord(String word) throws UnsupportedEncodingException {
        return encodeWord(word, null, null);
    }

    public static String encodeWord(String word, String charset, String encoding) throws UnsupportedEncodingException {
        return encodeWord(word, charset, encoding, true);
    }

    private static String encodeWord(String string, String charset, String encoding, boolean encodingWord) throws UnsupportedEncodingException {
        int ascii = checkAscii(string);
        if (ascii == 1) {
            return string;
        }
        String jcharset;
        boolean b64;
        if (charset == null) {
            jcharset = getDefaultJavaCharset();
            charset = getDefaultMIMECharset();
        } else {
            jcharset = javaCharset(charset);
        }
        if (encoding == null) {
            if (ascii != 3) {
                encoding = "Q";
            } else {
                encoding = "B";
            }
        }
        if (encoding.equalsIgnoreCase("B")) {
            b64 = true;
        } else if (encoding.equalsIgnoreCase("Q")) {
            b64 = false;
        } else {
            throw new UnsupportedEncodingException("Unknown transfer encoding: " + encoding);
        }
        StringBuffer outb = new StringBuffer();
        doEncode(string, b64, jcharset, 68 - charset.length(), "=?" + charset + "?" + encoding + "?", true, encodingWord, outb);
        return outb.toString();
    }

    private static void doEncode(String string, boolean b64, String jcharset, int avail, String prefix, boolean first, boolean encodingWord, StringBuffer buf) throws UnsupportedEncodingException {
        int len;
        OutputStream eos;
        byte[] bytes = string.getBytes(jcharset);
        if (b64) {
            len = BEncoderStream.encodedLength(bytes);
        } else {
            len = QEncoderStream.encodedLength(bytes, encodingWord);
        }
        if (len > avail) {
            int size = string.length();
            if (size > 1) {
                doEncode(string.substring(0, size / 2), b64, jcharset, avail, prefix, first, encodingWord, buf);
                doEncode(string.substring(size / 2, size), b64, jcharset, avail, prefix, false, encodingWord, buf);
                return;
            }
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        if (b64) {
            eos = new BEncoderStream(os);
        } else {
            eos = new QEncoderStream(os, encodingWord);
        }
        try {
            eos.write(bytes);
            eos.close();
        } catch (IOException e) {
        }
        byte[] encodedBytes = os.toByteArray();
        if (!first) {
            if (foldEncodedWords) {
                buf.append("\r\n ");
            } else {
                buf.append(" ");
            }
        }
        buf.append(prefix);
        for (byte b : encodedBytes) {
            buf.append((char) b);
        }
        buf.append("?=");
    }

    public static String decodeWord(String eword) throws ParseException, UnsupportedEncodingException {
        if (eword.startsWith("=?")) {
            int pos = eword.indexOf(63, 2);
            if (pos == -1) {
                throw new ParseException("encoded word does not include charset: " + eword);
            }
            String charset = javaCharset(eword.substring(2, pos));
            int start = pos + 1;
            pos = eword.indexOf(63, start);
            if (pos == -1) {
                throw new ParseException("encoded word does not include encoding: " + eword);
            }
            String encoding = eword.substring(start, pos);
            start = pos + 1;
            pos = eword.indexOf("?=", start);
            if (pos == -1) {
                throw new ParseException("encoded word does not end with \"?=\": " + eword);
            }
            String word = eword.substring(start, pos);
            try {
                String decodedWord;
                if (word.length() > 0) {
                    InputStream is;
                    ByteArrayInputStream bis = new ByteArrayInputStream(ASCIIUtility.getBytes(word));
                    if (encoding.equalsIgnoreCase("B")) {
                        is = new BASE64DecoderStream(bis);
                    } else if (encoding.equalsIgnoreCase("Q")) {
                        is = new QDecoderStream(bis);
                    } else {
                        throw new UnsupportedEncodingException("unknown encoding: " + encoding);
                    }
                    int count = bis.available();
                    byte[] bytes = new byte[count];
                    count = is.read(bytes, 0, count);
                    if (count <= 0) {
                        decodedWord = MMMailContentUtil.MM_MESSAGE_SUBJECT;
                    } else {
                        decodedWord = new String(bytes, 0, count, charset);
                    }
                } else {
                    decodedWord = MMMailContentUtil.MM_MESSAGE_SUBJECT;
                }
                if (pos + 2 >= eword.length()) {
                    return decodedWord;
                }
                String rest = eword.substring(pos + 2);
                if (!decodeStrict) {
                    rest = decodeInnerWords(rest);
                }
                return new StringBuilder(String.valueOf(decodedWord)).append(rest).toString();
            } catch (UnsupportedEncodingException uex) {
                throw uex;
            } catch (IOException ioex) {
                throw new ParseException(ioex.toString());
            } catch (IllegalArgumentException e) {
                throw new UnsupportedEncodingException(charset);
            }
        }
        throw new ParseException("encoded word does not start with \"=?\": " + eword);
    }

    private static String decodeInnerWords(String word) throws UnsupportedEncodingException {
        int start = 0;
        StringBuffer buf = new StringBuffer();
        while (true) {
            int i = word.indexOf("=?", start);
            if (i >= 0) {
                buf.append(word.substring(start, i));
                int end = word.indexOf(63, i + 2);
                if (end < 0) {
                    break;
                }
                end = word.indexOf(63, end + 1);
                if (end < 0) {
                    break;
                }
                end = word.indexOf("?=", end + 1);
                if (end < 0) {
                    break;
                }
                String s = word.substring(i, end + 2);
                try {
                    s = decodeWord(s);
                } catch (ParseException e) {
                }
                buf.append(s);
                start = end + 2;
            } else {
                break;
            }
        }
        if (start == 0) {
            return word;
        }
        if (start < word.length()) {
            buf.append(word.substring(start));
        }
        return buf.toString();
    }

    public static String quote(String word, String specials) {
        StringBuffer sb;
        int len = word.length();
        boolean needQuoting = false;
        for (int i = 0; i < len; i++) {
            char c = word.charAt(i);
            if (c == '\"' || c == '\\' || c == 13 || c == 10) {
                sb = new StringBuffer(len + 3);
                sb.append('\"');
                sb.append(word.substring(0, i));
                int lastc = 0;
                for (int j = i; j < len; j++) {
                    char cc = word.charAt(j);
                    if ((cc == '\"' || cc == '\\' || cc == 13 || cc == 10) && !(cc == 10 && lastc == 13)) {
                        sb.append('\\');
                    }
                    sb.append(cc);
                    char lastc2 = cc;
                }
                sb.append('\"');
                return sb.toString();
            }
            if (c < ' ' || c >= 127 || specials.indexOf(c) >= 0) {
                needQuoting = true;
            }
        }
        if (!needQuoting) {
            return word;
        }
        sb = new StringBuffer(len + 2);
        sb.append('\"').append(word).append('\"');
        return sb.toString();
    }

    public static String fold(int used, String s) {
        if (!foldText) {
            return s;
        }
        char c;
        int end = s.length() - 1;
        while (end >= 0) {
            c = s.charAt(end);
            if (c != ' ' && c != 9 && c != 13 && c != 10) {
                break;
            }
            end--;
        }
        if (end != s.length() - 1) {
            s = s.substring(0, end + 1);
        }
        if (s.length() + used <= 76) {
            return s;
        }
        StringBuffer sb = new StringBuffer(s.length() + 4);
        char lastc = 0;
        while (s.length() + used > 76) {
            int lastspace = -1;
            int i = 0;
            while (i < s.length() && (lastspace == -1 || used + i <= 76)) {
                c = s.charAt(i);
                if (!((c != ' ' && c != 9) || lastc == ' ' || lastc == 9)) {
                    lastspace = i;
                }
                lastc = c;
                i++;
            }
            if (lastspace == -1) {
                sb.append(s);
                s = MMMailContentUtil.MM_MESSAGE_SUBJECT;
                break;
            }
            sb.append(s.substring(0, lastspace));
            sb.append("\r\n");
            lastc = s.charAt(lastspace);
            sb.append(lastc);
            s = s.substring(lastspace + 1);
            used = 1;
        }
        sb.append(s);
        return sb.toString();
    }

    public static String unfold(String s) {
        if (!foldText) {
            return s;
        }
        StringBuffer sb = null;
        while (true) {
            int i = indexOfAny(s, "\r\n");
            if (i < 0) {
                break;
            }
            int start = i;
            int l = s.length();
            i++;
            if (i < l && s.charAt(i - 1) == 13 && s.charAt(i) == 10) {
                i++;
            }
            if (start == 0 || s.charAt(start - 1) != '\\') {
                if (i < l) {
                    char c = s.charAt(i);
                    if (c == ' ' || c == 9) {
                        i++;
                        while (i < l) {
                            c = s.charAt(i);
                            if (c != ' ' && c != 9) {
                                break;
                            }
                            i++;
                        }
                        if (sb == null) {
                            sb = new StringBuffer(s.length());
                        }
                        if (start != 0) {
                            sb.append(s.substring(0, start));
                            sb.append(' ');
                        }
                        s = s.substring(i);
                    }
                }
                if (sb == null) {
                    sb = new StringBuffer(s.length());
                }
                sb.append(s.substring(0, i));
                s = s.substring(i);
            } else {
                if (sb == null) {
                    sb = new StringBuffer(s.length());
                }
                sb.append(s.substring(0, start - 1));
                sb.append(s.substring(start, i));
                s = s.substring(i);
            }
        }
        if (sb == null) {
            return s;
        }
        sb.append(s);
        return sb.toString();
    }

    private static int indexOfAny(String s, String any) {
        return indexOfAny(s, any, 0);
    }

    private static int indexOfAny(String s, String any, int start) {
        try {
            int len = s.length();
            for (int i = start; i < len; i++) {
                if (any.indexOf(s.charAt(i)) >= 0) {
                    return i;
                }
            }
            return -1;
        } catch (StringIndexOutOfBoundsException e) {
            return -1;
        }
    }

    public static String javaCharset(String charset) {
        if (mime2java == null || charset == null) {
            return charset;
        }
        String alias = (String) mime2java.get(charset.toLowerCase(Locale.ENGLISH));
        return alias != null ? alias : charset;
    }

    public static String mimeCharset(String charset) {
        if (java2mime == null || charset == null) {
            return charset;
        }
        String alias = (String) java2mime.get(charset.toLowerCase(Locale.ENGLISH));
        return alias != null ? alias : charset;
    }

    public static String getDefaultJavaCharset() {
        if (defaultJavaCharset == null) {
            String mimecs = null;
            try {
                mimecs = System.getProperty("mail.mime.charset");
            } catch (SecurityException e) {
            }
            if (mimecs == null || mimecs.length() <= 0) {
                try {
                    defaultJavaCharset = System.getProperty("file.encoding", "8859_1");
                } catch (SecurityException e2) {
                    defaultJavaCharset = new InputStreamReader(new InputStream() {
                        public int read() {
                            return 0;
                        }
                    }).getEncoding();
                    if (defaultJavaCharset == null) {
                        defaultJavaCharset = "8859_1";
                    }
                }
            } else {
                defaultJavaCharset = javaCharset(mimecs);
                return defaultJavaCharset;
            }
        }
        return defaultJavaCharset;
    }

    static String getDefaultMIMECharset() {
        if (defaultMIMECharset == null) {
            try {
                defaultMIMECharset = System.getProperty("mail.mime.charset");
            } catch (SecurityException e) {
            }
        }
        if (defaultMIMECharset == null) {
            defaultMIMECharset = mimeCharset(getDefaultJavaCharset());
        }
        return defaultMIMECharset;
    }

    private static void loadMappings(LineInputStream is, Hashtable table) {
        while (true) {
            try {
                String currLine = is.readLine();
                if (currLine != null) {
                    if (!currLine.startsWith("--") || !currLine.endsWith("--")) {
                        if (!(currLine.trim().length() == 0 || currLine.startsWith("#"))) {
                            StringTokenizer tk = new StringTokenizer(currLine, " \t");
                            try {
                                String key = tk.nextToken();
                                table.put(key.toLowerCase(Locale.ENGLISH), tk.nextToken());
                            } catch (NoSuchElementException e) {
                            }
                        }
                    } else {
                        return;
                    }
                }
                return;
            } catch (IOException e2) {
                return;
            }
        }
    }

    static int checkAscii(String s) {
        int ascii = 0;
        int non_ascii = 0;
        int l = s.length();
        for (int i = 0; i < l; i++) {
            if (nonascii(s.charAt(i))) {
                non_ascii++;
            } else {
                ascii++;
            }
        }
        if (non_ascii == 0) {
            return 1;
        }
        if (ascii > non_ascii) {
            return 2;
        }
        return 3;
    }

    static int checkAscii(byte[] b) {
        int ascii = 0;
        int non_ascii = 0;
        for (byte b2 : b) {
            if (nonascii(b2 & MotionEventCompat.ACTION_MASK)) {
                non_ascii++;
            } else {
                ascii++;
            }
        }
        if (non_ascii == 0) {
            return 1;
        }
        if (ascii > non_ascii) {
            return 2;
        }
        return 3;
    }

    static int checkAscii(InputStream is, int max, boolean breakOnNonAscii) {
        int ascii = 0;
        int non_ascii = 0;
        int block = 4096;
        int linelen = 0;
        boolean longLine = false;
        boolean badEOL = false;
        boolean checkEOL = encodeEolStrict && breakOnNonAscii;
        byte[] buf = null;
        if (max != 0) {
            block = max == -1 ? 4096 : Math.min(max, 4096);
            buf = new byte[block];
        }
        while (max != 0) {
            try {
                int len = is.read(buf, 0, block);
                if (len == -1) {
                    break;
                }
                int lastb = 0;
                for (int i = 0; i < len; i++) {
                    int b = buf[i] & MotionEventCompat.ACTION_MASK;
                    if (checkEOL && ((lastb == 13 && b != 10) || (lastb != 13 && b == 10))) {
                        badEOL = true;
                    }
                    if (b == 13 || b == 10) {
                        linelen = 0;
                    } else {
                        linelen++;
                        if (linelen > 998) {
                            longLine = true;
                        }
                    }
                    if (!nonascii(b)) {
                        ascii++;
                    } else if (breakOnNonAscii) {
                        return 3;
                    } else {
                        non_ascii++;
                    }
                    lastb = b;
                }
                if (max != -1) {
                    max -= len;
                }
            } catch (IOException e) {
            }
        }
        if (max == 0 && breakOnNonAscii) {
            return 3;
        }
        if (non_ascii == 0) {
            if (badEOL) {
                return 3;
            }
            if (longLine) {
                return 2;
            }
            return 1;
        } else if (ascii > non_ascii) {
            return 2;
        } else {
            return 3;
        }
    }

    static final boolean nonascii(int b) {
        return b >= 127 || !(b >= 32 || b == 13 || b == 10 || b == 9);
    }
}
