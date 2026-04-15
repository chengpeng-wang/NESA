package com.sun.mail.imap.protocol;

import java.util.Vector;

public class UIDSet {
    public long end;
    public long start;

    public UIDSet(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long size() {
        return (this.end - this.start) + 1;
    }

    public static UIDSet[] createUIDSets(long[] msgs) {
        Vector v = new Vector();
        int j;
        for (int i = 0; i < msgs.length; i = (j - 1) + 1) {
            UIDSet ms = new UIDSet();
            ms.start = msgs[i];
            j = i + 1;
            while (j < msgs.length && msgs[j] == msgs[j - 1] + 1) {
                j++;
            }
            ms.end = msgs[j - 1];
            v.addElement(ms);
        }
        UIDSet[] msgsets = new UIDSet[v.size()];
        v.copyInto(msgsets);
        return msgsets;
    }

    public static String toString(UIDSet[] msgsets) {
        if (msgsets == null || msgsets.length == 0) {
            return null;
        }
        int i = 0;
        StringBuffer s = new StringBuffer();
        int size = msgsets.length;
        while (true) {
            long start = msgsets[i].start;
            long end = msgsets[i].end;
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

    public static long size(UIDSet[] msgsets) {
        long count = 0;
        if (msgsets == null) {
            return 0;
        }
        for (UIDSet size : msgsets) {
            count += size.size();
        }
        return count;
    }
}
