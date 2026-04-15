package org.springframework.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

public final class BooleanComparator implements Comparator<Boolean>, Serializable {
    public static final BooleanComparator TRUE_HIGH = new BooleanComparator(false);
    public static final BooleanComparator TRUE_LOW = new BooleanComparator(true);
    private final boolean trueLow;

    public BooleanComparator(boolean trueLow) {
        this.trueLow = trueLow;
    }

    public int compare(Boolean v1, Boolean v2) {
        if ((v1.booleanValue() ^ v2.booleanValue()) != 0) {
            return (v1.booleanValue() ^ this.trueLow) != 0 ? 1 : -1;
        } else {
            return 0;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BooleanComparator)) {
            return false;
        }
        if (this.trueLow != ((BooleanComparator) obj).trueLow) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (this.trueLow ? -1 : 1) * getClass().hashCode();
    }

    public String toString() {
        return "BooleanComparator: " + (this.trueLow ? "true low" : "true high");
    }
}
