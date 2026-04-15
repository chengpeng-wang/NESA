package com.sun.mail.imap;

import com.sun.mail.imap.protocol.MessageSet;
import com.sun.mail.imap.protocol.UIDSet;
import java.util.Vector;
import javax.mail.Message;

public final class Utility {

    public interface Condition {
        boolean test(IMAPMessage iMAPMessage);
    }

    private Utility() {
    }

    public static MessageSet[] toMessageSet(Message[] msgs, Condition cond) {
        Vector v = new Vector(1);
        int i = 0;
        while (i < msgs.length) {
            IMAPMessage msg = msgs[i];
            if (!msg.isExpunged()) {
                int current = msg.getSequenceNumber();
                if (cond == null || cond.test(msg)) {
                    MessageSet set = new MessageSet();
                    set.start = current;
                    while (true) {
                        i++;
                        if (i >= msgs.length) {
                            break;
                        }
                        msg = msgs[i];
                        if (!msg.isExpunged()) {
                            int next = msg.getSequenceNumber();
                            if (cond == null || cond.test(msg)) {
                                if (next != current + 1) {
                                    i--;
                                    break;
                                }
                                current = next;
                            }
                        }
                    }
                    set.end = current;
                    v.addElement(set);
                }
            }
            i++;
        }
        if (v.isEmpty()) {
            return null;
        }
        MessageSet[] sets = new MessageSet[v.size()];
        v.copyInto(sets);
        return sets;
    }

    public static UIDSet[] toUIDSet(Message[] msgs) {
        Vector v = new Vector(1);
        int i = 0;
        while (i < msgs.length) {
            IMAPMessage msg = msgs[i];
            if (!msg.isExpunged()) {
                long current = msg.getUID();
                UIDSet set = new UIDSet();
                set.start = current;
                while (true) {
                    i++;
                    if (i < msgs.length) {
                        msg = msgs[i];
                        if (!msg.isExpunged()) {
                            long next = msg.getUID();
                            if (next != 1 + current) {
                                i--;
                                break;
                            }
                            current = next;
                        }
                    } else {
                        break;
                    }
                }
                set.end = current;
                v.addElement(set);
            }
            i++;
        }
        if (v.isEmpty()) {
            return null;
        }
        UIDSet[] sets = new UIDSet[v.size()];
        v.copyInto(sets);
        return sets;
    }
}
