package org.apache.log4j.helpers;

import org.apache.log4j.spi.LoggingEvent;

public class BoundedFIFO {
    LoggingEvent[] buf;
    int first = 0;
    int maxSize;
    int next = 0;
    int numElements = 0;

    public BoundedFIFO(int maxSize) {
        if (maxSize < 1) {
            throw new IllegalArgumentException(new StringBuffer().append("The maxSize argument (").append(maxSize).append(") is not a positive integer.").toString());
        }
        this.maxSize = maxSize;
        this.buf = new LoggingEvent[maxSize];
    }

    public LoggingEvent get() {
        if (this.numElements == 0) {
            return null;
        }
        LoggingEvent r = this.buf[this.first];
        this.buf[this.first] = null;
        int i = this.first + 1;
        this.first = i;
        if (i == this.maxSize) {
            this.first = 0;
        }
        this.numElements--;
        return r;
    }

    public void put(LoggingEvent o) {
        if (this.numElements != this.maxSize) {
            this.buf[this.next] = o;
            int i = this.next + 1;
            this.next = i;
            if (i == this.maxSize) {
                this.next = 0;
            }
            this.numElements++;
        }
    }

    public int getMaxSize() {
        return this.maxSize;
    }

    public boolean isFull() {
        return this.numElements == this.maxSize;
    }

    public int length() {
        return this.numElements;
    }

    /* access modifiers changed from: 0000 */
    public int min(int a, int b) {
        return a < b ? a : b;
    }

    public synchronized void resize(int newSize) {
        if (newSize != this.maxSize) {
            LoggingEvent[] tmp = new LoggingEvent[newSize];
            int len1 = min(min(this.maxSize - this.first, newSize), this.numElements);
            System.arraycopy(this.buf, this.first, tmp, 0, len1);
            int len2 = 0;
            if (len1 < this.numElements && len1 < newSize) {
                len2 = min(this.numElements - len1, newSize - len1);
                System.arraycopy(this.buf, 0, tmp, len1, len2);
            }
            this.buf = tmp;
            this.maxSize = newSize;
            this.first = 0;
            this.numElements = len1 + len2;
            this.next = this.numElements;
            if (this.next == this.maxSize) {
                this.next = 0;
            }
        }
    }

    public boolean wasEmpty() {
        return this.numElements == 1;
    }

    public boolean wasFull() {
        return this.numElements + 1 == this.maxSize;
    }
}
