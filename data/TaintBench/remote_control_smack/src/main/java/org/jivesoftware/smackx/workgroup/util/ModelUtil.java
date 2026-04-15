package org.jivesoftware.smackx.workgroup.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public final class ModelUtil {
    private ModelUtil() {
    }

    public static final boolean areEqual(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }

    public static final boolean areBooleansEqual(Boolean b1, Boolean b2) {
        return (b1 == Boolean.TRUE && b2 == Boolean.TRUE) || !(b1 == Boolean.TRUE || b2 == Boolean.TRUE);
    }

    public static final boolean areDifferent(Object o1, Object o2) {
        return !areEqual(o1, o2);
    }

    public static final boolean areBooleansDifferent(Boolean b1, Boolean b2) {
        return !areBooleansEqual(b1, b2);
    }

    public static final boolean hasNonNullElement(Object[] array) {
        if (array != null) {
            for (Object obj : array) {
                if (obj != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static final String concat(String[] strs) {
        return concat(strs, " ");
    }

    public static final String concat(String[] strs, String delim) {
        if (strs == null) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        for (String str : strs) {
            if (str != null) {
                buf.append(str).append(delim);
            }
        }
        int length = buf.length();
        if (length > 0) {
            buf.setLength(length - 1);
        }
        return buf.toString();
    }

    public static final boolean hasLength(String s) {
        return s != null && s.length() > 0;
    }

    public static final String nullifyIfEmpty(String s) {
        return hasLength(s) ? s : null;
    }

    public static final String nullifyingToString(Object o) {
        return o != null ? nullifyIfEmpty(o.toString()) : null;
    }

    public static boolean hasStringChanged(String oldString, String newString) {
        if (oldString == null && newString == null) {
            return false;
        }
        if (oldString == null && newString != null) {
            return true;
        }
        if ((oldString == null || newString != null) && oldString.equals(newString)) {
            return false;
        }
        return true;
    }

    public static String getTimeFromLong(long diff) {
        String HOURS = "h";
        String MINUTES = "min";
        String SECONDS = "sec";
        Date currentTime = new Date();
        long numDays = diff / 86400000;
        diff %= 86400000;
        long numHours = diff / 3600000;
        diff %= 3600000;
        long numMinutes = diff / 60000;
        diff %= 60000;
        long numSeconds = diff / 1000;
        long numMilliseconds = diff % 1000;
        StringBuilder buf = new StringBuilder();
        if (numHours > 0) {
            buf.append(numHours + " " + "h" + ", ");
        }
        if (numMinutes > 0) {
            buf.append(numMinutes + " " + "min" + ", ");
        }
        buf.append(numSeconds + " " + "sec");
        return buf.toString();
    }

    public static List iteratorAsList(Iterator i) {
        ArrayList list = new ArrayList(10);
        while (i.hasNext()) {
            list.add(i.next());
        }
        return list;
    }

    public static Iterator reverseListIterator(ListIterator i) {
        return new ReverseListIterator(i);
    }
}
