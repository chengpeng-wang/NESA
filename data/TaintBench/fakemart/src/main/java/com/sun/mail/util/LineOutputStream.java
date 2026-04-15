package com.sun.mail.util;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import javax.mail.MessagingException;

public class LineOutputStream extends FilterOutputStream {
    private static byte[] newline = new byte[2];

    static {
        newline[0] = (byte) 13;
        newline[1] = (byte) 10;
    }

    public LineOutputStream(OutputStream out) {
        super(out);
    }

    public void writeln(String s) throws MessagingException {
        try {
            this.out.write(ASCIIUtility.getBytes(s));
            this.out.write(newline);
        } catch (Exception ex) {
            throw new MessagingException("IOException", ex);
        }
    }

    public void writeln() throws MessagingException {
        try {
            this.out.write(newline);
        } catch (Exception ex) {
            throw new MessagingException("IOException", ex);
        }
    }
}
