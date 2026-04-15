package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ParsingException;
import javax.mail.Flags;
import javax.mail.Flags.Flag;

public class FLAGS extends Flags implements Item {
    static final char[] name = new char[]{'F', 'L', 'A', 'G', 'S'};
    private static final long serialVersionUID = 439049847053756670L;
    public int msgno;

    public FLAGS(IMAPResponse r) throws ParsingException {
        this.msgno = r.getNumber();
        r.skipSpaces();
        String[] flags = r.readSimpleList();
        if (flags != null) {
            for (String s : flags) {
                if (s.length() >= 2 && s.charAt(0) == '\\') {
                    switch (Character.toUpperCase(s.charAt(1))) {
                        case '*':
                            add(Flag.USER);
                            break;
                        case 'A':
                            add(Flag.ANSWERED);
                            break;
                        case 'D':
                            if (s.length() < 3) {
                                add(s);
                                break;
                            }
                            char c = s.charAt(2);
                            if (c != 'e' && c != 'E') {
                                if (c != 'r' && c != 'R') {
                                    break;
                                }
                                add(Flag.DRAFT);
                                break;
                            }
                            add(Flag.DELETED);
                            break;
                            break;
                        case 'F':
                            add(Flag.FLAGGED);
                            break;
                        case 'R':
                            add(Flag.RECENT);
                            break;
                        case 'S':
                            add(Flag.SEEN);
                            break;
                        default:
                            add(s);
                            break;
                    }
                }
                add(s);
            }
        }
    }
}
