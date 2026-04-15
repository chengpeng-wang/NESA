package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ByteArray;
import com.sun.mail.iap.ParsingException;
import java.io.ByteArrayInputStream;

public class BODY implements Item {
    static final char[] name = new char[]{'B', 'O', 'D', 'Y'};
    public ByteArray data;
    public int msgno;
    public int origin = 0;
    public String section;

    public BODY(FetchResponse r) throws ParsingException {
        this.msgno = r.getNumber();
        r.skipSpaces();
        int b;
        do {
            b = r.readByte();
            if (b == 93) {
                if (r.readByte() == (byte) 60) {
                    this.origin = r.readNumber();
                    r.skip(1);
                }
                this.data = r.readByteArray();
                return;
            }
        } while (b != 0);
        throw new ParsingException("BODY parse error: missing ``]'' at section end");
    }

    public ByteArray getByteArray() {
        return this.data;
    }

    public ByteArrayInputStream getByteArrayInputStream() {
        if (this.data != null) {
            return this.data.toByteArrayInputStream();
        }
        return null;
    }
}
