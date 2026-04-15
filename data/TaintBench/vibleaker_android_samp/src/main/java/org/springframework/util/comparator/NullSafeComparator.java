package org.springframework.util.comparator;

import java.util.Comparator;
import org.springframework.util.Assert;

public class NullSafeComparator<T> implements Comparator<T> {
    public static final NullSafeComparator NULLS_HIGH = new NullSafeComparator(false);
    public static final NullSafeComparator NULLS_LOW = new NullSafeComparator(true);
    private final Comparator<T> nonNullComparator;
    private final boolean nullsLow;

    private NullSafeComparator(boolean nullsLow) {
        this.nonNullComparator = new ComparableComparator();
        this.nullsLow = nullsLow;
    }

    public NullSafeComparator(Comparator<T> comparator, boolean nullsLow) {
        Assert.notNull(comparator, "The non-null comparator is required");
        this.nonNullComparator = comparator;
        this.nullsLow = nullsLow;
    }

    public int compare(T o1, T o2) {
        int i = 1;
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            if (this.nullsLow) {
                return -1;
            }
            return 1;
        } else if (o2 != null) {
            return this.nonNullComparator.compare(o1, o2);
        } else {
            if (!this.nullsLow) {
                i = -1;
            }
            return i;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NullSafeComparator)) {
            return false;
        }
        NullSafeComparator<T> other = (NullSafeComparator) obj;
        if (this.nonNullComparator.equals(other.nonNullComparator) && this.nullsLow == other.nullsLow) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (this.nullsLow ? -1 : 1) * this.nonNullComparator.hashCode();
    }

    public String toString() {
        return "NullSafeComparator: non-null comparator [" + this.nonNullComparator + "]; " + (this.nullsLow ? "nulls low" : "nulls high");
    }
}
