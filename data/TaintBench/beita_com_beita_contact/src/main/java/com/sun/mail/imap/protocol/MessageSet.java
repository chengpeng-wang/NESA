package com.sun.mail.imap.protocol;

import java.util.Vector;

public class MessageSet {
    public int end;
    public int start;

    public MessageSet(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int size() {
        return (this.end - this.start) + 1;
    }

    public static MessageSet[] createMessageSets(int[] msgs) {
        Vector v = new Vector();
        int j;
        for (int i = 0; i < msgs.length; i = (j - 1) + 1) {
            MessageSet ms = new MessageSet();
            ms.start = msgs[i];
            j = i + 1;
            while (j < msgs.length && msgs[j] == msgs[j - 1] + 1) {
                j++;
            }
            ms.end = msgs[j - 1];
            v.addElement(ms);
        }
        MessageSet[] msgsets = new MessageSet[v.size()];
        v.copyInto(msgsets);
        return msgsets;
    }

    public static String toString(MessageSet[] msgsets) {
        if (msgsets == null || msgsets.length == 0) {
            return null;
        }
        int i = 0;
        StringBuffer s = new StringBuffer();
        int size = msgsets.length;
        while (true) {
            int start = msgsets[i].start;
            int end = msgsets[i].end;
            if (end > start) {
                s.append(start).append(':').append(end);
            } else {
                s.append(start);
            }
            i++;
            if (i >= size) {
                return s.toString();
            }
            s.append(',');
        }
    }

    public static int size(MessageSet[] msgsets) {
        int count = 0;
        if (msgsets == null) {
            return 0;
        }
        for (MessageSet size : msgsets) {
            count += size.size();
        }
        return count;
    }
}
