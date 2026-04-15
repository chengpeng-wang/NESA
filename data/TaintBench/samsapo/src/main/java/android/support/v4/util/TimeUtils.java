package android.support.v4.util;

import java.io.PrintWriter;

public class TimeUtils {
    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static char[] sFormatStr = new char[24];
    private static final Object sFormatSync;

    public TimeUtils() {
    }

    static {
        Object obj = r2;
        Object obj2 = new Object();
        sFormatSync = obj;
    }

    private static int accumField(int i, int i2, boolean z, int i3) {
        int i4 = i;
        int i5 = i2;
        boolean z2 = z;
        int i6 = i3;
        if (i4 > 99 || (z2 && i6 >= 3)) {
            return 3 + i5;
        }
        if (i4 > 9 || (z2 && i6 >= 2)) {
            return 2 + i5;
        }
        if (z2 || i4 > 0) {
            return 1 + i5;
        }
        return 0;
    }

    private static int printField(char[] cArr, int i, char c, int i2, boolean z, int i3) {
        char[] cArr2 = cArr;
        int i4 = i;
        char c2 = c;
        int i5 = i2;
        boolean z2 = z;
        int i6 = i3;
        if (z2 || i4 > 0) {
            int i7;
            int i8 = i5;
            if ((z2 && i6 >= 3) || i4 > 99) {
                i7 = i4 / 100;
                cArr2[i5] = (char) (i7 + 48);
                i5++;
                i4 -= i7 * 100;
            }
            if ((z2 && i6 >= 2) || i4 > 9 || i8 != i5) {
                i7 = i4 / 10;
                cArr2[i5] = (char) (i7 + 48);
                i5++;
                i4 -= i7 * 10;
            }
            cArr2[i5] = (char) (i4 + 48);
            i5++;
            cArr2[i5] = c2;
            i5++;
        }
        return i5;
    }

    private static int formatDurationLocked(long j, int i) {
        long j2 = j;
        int i2 = i;
        if (sFormatStr.length < i2) {
            sFormatStr = new char[i2];
        }
        char[] cArr = sFormatStr;
        if (j2 == 0) {
            int i3 = 0;
            i2--;
            while (i3 < i2) {
                cArr[i3] = ' ';
            }
            cArr[i3] = '0';
            return i3 + 1;
        }
        char c;
        int accumField;
        if (j2 > 0) {
            c = '+';
        } else {
            c = '-';
            j2 = -j2;
        }
        int i4 = (int) (j2 % 1000);
        int floor = (int) Math.floor((double) (j2 / 1000));
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        if (floor > SECONDS_PER_DAY) {
            i5 = floor / SECONDS_PER_DAY;
            floor -= i5 * SECONDS_PER_DAY;
        }
        if (floor > SECONDS_PER_HOUR) {
            i6 = floor / SECONDS_PER_HOUR;
            floor -= i6 * SECONDS_PER_HOUR;
        }
        if (floor > SECONDS_PER_MINUTE) {
            i7 = floor / SECONDS_PER_MINUTE;
            floor -= i7 * SECONDS_PER_MINUTE;
        }
        int i8 = 0;
        if (i2 != 0) {
            accumField = accumField(i5, 1, false, 0);
            accumField += accumField(i6, 1, accumField > 0, 2);
            accumField += accumField(i7, 1, accumField > 0, 2);
            accumField += accumField(floor, 1, accumField > 0, 2);
            for (accumField += accumField(i4, 2, true, accumField > 0 ? 3 : 0) + 1; accumField < i2; accumField++) {
                cArr[i8] = ' ';
                i8++;
            }
        }
        cArr[i8] = c;
        i8++;
        accumField = i8;
        Object obj = i2 != 0 ? 1 : null;
        i8 = printField(cArr, i5, 'd', i8, false, 0);
        i8 = printField(cArr, i6, 'h', i8, i8 != accumField, obj != null ? 2 : 0);
        i8 = printField(cArr, i7, 'm', i8, i8 != accumField, obj != null ? 2 : 0);
        i8 = printField(cArr, floor, 's', i8, i8 != accumField, obj != null ? 2 : 0);
        char[] cArr2 = cArr;
        int i9 = i4;
        int i10 = i8;
        int i11 = (obj == null || i8 == accumField) ? 0 : 3;
        i8 = printField(cArr2, i9, 'm', i10, true, i11);
        cArr[i8] = 's';
        return i8 + 1;
    }

    public static void formatDuration(long j, StringBuilder stringBuilder) {
        long j2 = j;
        StringBuilder stringBuilder2 = stringBuilder;
        Object obj = sFormatSync;
        Object obj2 = obj;
        synchronized (obj) {
            try {
                StringBuilder append = stringBuilder2.append(sFormatStr, 0, formatDurationLocked(j2, 0));
            } catch (Throwable th) {
                Throwable th2 = th;
                Object obj3 = obj2;
                Throwable th3 = th2;
            }
        }
    }

    public static void formatDuration(long j, PrintWriter printWriter, int i) {
        long j2 = j;
        PrintWriter printWriter2 = printWriter;
        int i2 = i;
        Object obj = sFormatSync;
        Object obj2 = obj;
        synchronized (obj) {
            try {
                PrintWriter printWriter3 = printWriter2;
                String str = r13;
                String str2 = new String(sFormatStr, 0, formatDurationLocked(j2, i2));
                printWriter3.print(str);
            } catch (Throwable th) {
                Throwable th2 = th;
                Object obj3 = obj2;
                Throwable th3 = th2;
            }
        }
    }

    public static void formatDuration(long j, PrintWriter printWriter) {
        formatDuration(j, printWriter, 0);
    }

    public static void formatDuration(long j, long j2, PrintWriter printWriter) {
        long j3 = j;
        long j4 = j2;
        PrintWriter printWriter2 = printWriter;
        if (j3 == 0) {
            printWriter2.print("--");
        } else {
            formatDuration(j3 - j4, printWriter2, 0);
        }
    }
}
