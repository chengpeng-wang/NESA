package android.support.v4.util;

import android.util.Log;
import java.io.Writer;

public class LogWriter extends Writer {
    private StringBuilder mBuilder;
    private final String mTag;

    public LogWriter(String str) {
        String str2 = str;
        StringBuilder stringBuilder = r6;
        StringBuilder stringBuilder2 = new StringBuilder(128);
        this.mBuilder = stringBuilder;
        this.mTag = str2;
    }

    public void close() {
        flushBuilder();
    }

    public void flush() {
        flushBuilder();
    }

    public void write(char[] cArr, int i, int i2) {
        char[] cArr2 = cArr;
        int i3 = i;
        int i4 = i2;
        for (int i5 = 0; i5 < i4; i5++) {
            char c = cArr2[i3 + i5];
            if (c == 10) {
                flushBuilder();
            } else {
                StringBuilder append = this.mBuilder.append(c);
            }
        }
    }

    private void flushBuilder() {
        if (this.mBuilder.length() > 0) {
            int d = Log.d(this.mTag, this.mBuilder.toString());
            StringBuilder delete = this.mBuilder.delete(0, this.mBuilder.length());
        }
    }
}
