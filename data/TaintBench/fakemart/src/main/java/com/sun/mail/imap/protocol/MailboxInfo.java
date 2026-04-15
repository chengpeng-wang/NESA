package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ParsingException;
import com.sun.mail.iap.Response;
import javax.mail.Flags;

public class MailboxInfo {
    public Flags availableFlags = null;
    public int first = -1;
    public int mode;
    public Flags permanentFlags = null;
    public int recent = -1;
    public int total = -1;
    public long uidnext = -1;
    public long uidvalidity = -1;

    public MailboxInfo(Response[] r) throws ParsingException {
        int i = 0;
        while (i < r.length) {
            if (r[i] != null && (r[i] instanceof IMAPResponse)) {
                IMAPResponse ir = r[i];
                if (ir.keyEquals("EXISTS")) {
                    this.total = ir.getNumber();
                    r[i] = null;
                } else if (ir.keyEquals("RECENT")) {
                    this.recent = ir.getNumber();
                    r[i] = null;
                } else if (ir.keyEquals("FLAGS")) {
                    this.availableFlags = new FLAGS(ir);
                    r[i] = null;
                } else if (ir.isUnTagged() && ir.isOK()) {
                    ir.skipSpaces();
                    if (ir.readByte() != (byte) 91) {
                        ir.reset();
                    } else {
                        boolean handled = true;
                        String s = ir.readAtom();
                        if (s.equalsIgnoreCase("UNSEEN")) {
                            this.first = ir.readNumber();
                        } else if (s.equalsIgnoreCase("UIDVALIDITY")) {
                            this.uidvalidity = ir.readLong();
                        } else if (s.equalsIgnoreCase("PERMANENTFLAGS")) {
                            this.permanentFlags = new FLAGS(ir);
                        } else if (s.equalsIgnoreCase("UIDNEXT")) {
                            this.uidnext = ir.readLong();
                        } else {
                            handled = false;
                        }
                        if (handled) {
                            r[i] = null;
                        } else {
                            ir.reset();
                        }
                    }
                }
            }
            i++;
        }
        if (this.permanentFlags != null) {
            return;
        }
        if (this.availableFlags != null) {
            this.permanentFlags = new Flags(this.availableFlags);
        } else {
            this.permanentFlags = new Flags();
        }
    }
}
