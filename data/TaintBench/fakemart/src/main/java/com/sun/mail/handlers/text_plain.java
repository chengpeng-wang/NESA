package com.sun.mail.handlers;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeUtility;
import myjava.awt.datatransfer.DataFlavor;

public class text_plain implements DataContentHandler {
    private static ActivationDataFlavor myDF = new ActivationDataFlavor(String.class, "text/plain", "Text String");

    /* access modifiers changed from: protected */
    public ActivationDataFlavor getDF() {
        return myDF;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{getDF()};
    }

    public Object getTransferData(DataFlavor df, DataSource ds) throws IOException {
        if (getDF().equals(df)) {
            return getContent(ds);
        }
        return null;
    }

    public Object getContent(DataSource ds) throws IOException {
        String enc = null;
        try {
            enc = getCharset(ds.getContentType());
            InputStreamReader is = new InputStreamReader(ds.getInputStream(), enc);
            int pos = 0;
            try {
                char[] buf = new char[1024];
                while (true) {
                    int count = is.read(buf, pos, buf.length - pos);
                    if (count == -1) {
                        break;
                    }
                    pos += count;
                    if (pos >= buf.length) {
                        int size = buf.length;
                        if (size < AccessibilityEventCompat.TYPE_GESTURE_DETECTION_START) {
                            size += size;
                        } else {
                            size += AccessibilityEventCompat.TYPE_GESTURE_DETECTION_START;
                        }
                        char[] tbuf = new char[size];
                        System.arraycopy(buf, 0, tbuf, 0, pos);
                        buf = tbuf;
                    }
                }
                Object str = new String(buf, 0, pos);
                return str;
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        } catch (IllegalArgumentException e2) {
            throw new UnsupportedEncodingException(enc);
        }
    }

    public void writeTo(Object obj, String type, OutputStream os) throws IOException {
        if (obj instanceof String) {
            String enc = null;
            try {
                enc = getCharset(type);
                OutputStreamWriter osw = new OutputStreamWriter(os, enc);
                String s = (String) obj;
                osw.write(s, 0, s.length());
                osw.flush();
                return;
            } catch (IllegalArgumentException e) {
                throw new UnsupportedEncodingException(enc);
            }
        }
        throw new IOException("\"" + getDF().getMimeType() + "\" DataContentHandler requires String object, " + "was given object of type " + obj.getClass().toString());
    }

    private String getCharset(String type) {
        try {
            String charset = new ContentType(type).getParameter("charset");
            if (charset == null) {
                charset = "us-ascii";
            }
            return MimeUtility.javaCharset(charset);
        } catch (Exception e) {
            return null;
        }
    }
}
