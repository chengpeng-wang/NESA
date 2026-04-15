package com.sun.mail.dsn;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeUtility;
import myjava.awt.datatransfer.DataFlavor;

public class text_rfc822headers implements DataContentHandler {
    private static ActivationDataFlavor myDF = new ActivationDataFlavor(MessageHeaders.class, "text/rfc822-headers", "RFC822 headers");
    private static ActivationDataFlavor myDFs = new ActivationDataFlavor(String.class, "text/rfc822-headers", "RFC822 headers");

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{myDF, myDFs};
    }

    public Object getTransferData(DataFlavor df, DataSource ds) throws IOException {
        if (myDF.equals(df)) {
            return getContent(ds);
        }
        if (myDFs.equals(df)) {
            return getStringContent(ds);
        }
        return null;
    }

    public Object getContent(DataSource ds) throws IOException {
        try {
            return new MessageHeaders(ds.getInputStream());
        } catch (MessagingException mex) {
            throw new IOException("Exception creating MessageHeaders: " + mex);
        }
    }

    private Object getStringContent(DataSource ds) throws IOException {
        String enc = null;
        try {
            enc = getCharset(ds.getContentType());
            InputStreamReader is = new InputStreamReader(ds.getInputStream(), enc);
            int pos = 0;
            char[] buf = new char[1024];
            while (true) {
                int count = is.read(buf, pos, buf.length - pos);
                if (count == -1) {
                    return new String(buf, 0, pos);
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
        } catch (IllegalArgumentException e) {
            throw new UnsupportedEncodingException(enc);
        }
    }

    public void writeTo(Object obj, String type, OutputStream os) throws IOException {
        if (obj instanceof MessageHeaders) {
            try {
                ((MessageHeaders) obj).writeTo(os);
            } catch (MessagingException mex) {
                Exception ex = mex.getNextException();
                if (ex instanceof IOException) {
                    throw ((IOException) ex);
                }
                throw new IOException("Exception writing headers: " + mex);
            }
        } else if (obj instanceof String) {
            String enc = null;
            try {
                enc = getCharset(type);
                OutputStreamWriter osw = new OutputStreamWriter(os, enc);
                String s = (String) obj;
                osw.write(s, 0, s.length());
                osw.flush();
            } catch (IllegalArgumentException e) {
                throw new UnsupportedEncodingException(enc);
            }
        } else {
            throw new IOException("\"" + myDFs.getMimeType() + "\" DataContentHandler requires String object, " + "was given object of type " + obj.getClass().toString());
        }
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
