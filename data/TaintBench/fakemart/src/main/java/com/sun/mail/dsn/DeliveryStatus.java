package com.sun.mail.dsn;

import com.sun.mail.util.LineOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;

public class DeliveryStatus {
    private static boolean debug;
    protected InternetHeaders messageDSN;
    protected InternetHeaders[] recipientDSN;

    static {
        boolean z = false;
        debug = false;
        try {
            String s = System.getProperty("mail.dsn.debug");
            if (!(s == null || s.equalsIgnoreCase("false"))) {
                z = true;
            }
            debug = z;
        } catch (SecurityException e) {
        }
    }

    public DeliveryStatus() throws MessagingException {
        this.messageDSN = new InternetHeaders();
        this.recipientDSN = new InternetHeaders[0];
    }

    public DeliveryStatus(InputStream is) throws MessagingException, IOException {
        this.messageDSN = new InternetHeaders(is);
        if (debug) {
            System.out.println("DSN: got messageDSN");
        }
        Vector v = new Vector();
        while (is.available() > 0) {
            try {
                InternetHeaders h = new InternetHeaders(is);
                if (debug) {
                    System.out.println("DSN: got recipientDSN");
                }
                v.addElement(h);
            } catch (EOFException e) {
                if (debug) {
                    System.out.println("DSN: got EOFException");
                }
            }
        }
        if (debug) {
            System.out.println("DSN: recipientDSN size " + v.size());
        }
        this.recipientDSN = new InternetHeaders[v.size()];
        v.copyInto(this.recipientDSN);
    }

    public InternetHeaders getMessageDSN() {
        return this.messageDSN;
    }

    public void setMessageDSN(InternetHeaders messageDSN) {
        this.messageDSN = messageDSN;
    }

    public int getRecipientDSNCount() {
        return this.recipientDSN.length;
    }

    public InternetHeaders getRecipientDSN(int n) {
        return this.recipientDSN[n];
    }

    public void addRecipientDSN(InternetHeaders h) {
        InternetHeaders[] rh = new InternetHeaders[(this.recipientDSN.length + 1)];
        System.arraycopy(this.recipientDSN, 0, rh, 0, this.recipientDSN.length);
        this.recipientDSN = rh;
        this.recipientDSN[this.recipientDSN.length - 1] = h;
    }

    public void writeTo(OutputStream os) throws IOException, MessagingException {
        LineOutputStream los;
        if (os instanceof LineOutputStream) {
            los = (LineOutputStream) os;
        } else {
            los = new LineOutputStream(os);
        }
        writeInternetHeaders(this.messageDSN, los);
        los.writeln();
        for (InternetHeaders writeInternetHeaders : this.recipientDSN) {
            writeInternetHeaders(writeInternetHeaders, los);
            los.writeln();
        }
    }

    private static void writeInternetHeaders(InternetHeaders h, LineOutputStream los) throws IOException {
        Enumeration e = h.getAllHeaderLines();
        while (e.hasMoreElements()) {
            try {
                los.writeln((String) e.nextElement());
            } catch (MessagingException mex) {
                Exception ex = mex.getNextException();
                if (ex instanceof IOException) {
                    throw ((IOException) ex);
                }
                throw new IOException("Exception writing headers: " + mex);
            }
        }
    }

    public String toString() {
        return "DeliveryStatus: Reporting-MTA=" + this.messageDSN.getHeader("Reporting-MTA", null) + ", #Recipients=" + this.recipientDSN.length;
    }
}
