package javax.mail.internet;

import com.sun.mail.imap.IMAPStore;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class MailDateFormat extends SimpleDateFormat {
    private static Calendar cal = new GregorianCalendar(tz);
    static boolean debug = false;
    private static final long serialVersionUID = -8148227605210628779L;
    private static TimeZone tz = TimeZone.getTimeZone("GMT");

    public MailDateFormat() {
        super("EEE, d MMM yyyy HH:mm:ss 'XXXXX' (z)", Locale.US);
    }

    public StringBuffer format(Date date, StringBuffer dateStrBuf, FieldPosition fieldPosition) {
        int pos;
        int start = dateStrBuf.length();
        super.format(date, dateStrBuf, fieldPosition);
        int pos2 = start + 25;
        while (dateStrBuf.charAt(pos2) != 'X') {
            pos2++;
        }
        this.calendar.clear();
        this.calendar.setTime(date);
        int offset = this.calendar.get(15) + this.calendar.get(16);
        if (offset < 0) {
            pos = pos2 + 1;
            dateStrBuf.setCharAt(pos2, '-');
            offset = -offset;
            pos2 = pos;
        } else {
            pos = pos2 + 1;
            dateStrBuf.setCharAt(pos2, '+');
            pos2 = pos;
        }
        int rawOffsetInMins = (offset / 60) / IMAPStore.RESPONSE;
        int offsetInHrs = rawOffsetInMins / 60;
        int offsetInMins = rawOffsetInMins % 60;
        pos = pos2 + 1;
        dateStrBuf.setCharAt(pos2, Character.forDigit(offsetInHrs / 10, 10));
        pos2 = pos + 1;
        dateStrBuf.setCharAt(pos, Character.forDigit(offsetInHrs % 10, 10));
        pos = pos2 + 1;
        dateStrBuf.setCharAt(pos2, Character.forDigit(offsetInMins / 10, 10));
        pos2 = pos + 1;
        dateStrBuf.setCharAt(pos, Character.forDigit(offsetInMins % 10, 10));
        return dateStrBuf;
    }

    public Date parse(String text, ParsePosition pos) {
        return parseDate(text.toCharArray(), pos, isLenient());
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:23:0x0071, code skipped:
            if (debug != null) goto L_0x0073;
     */
    /* JADX WARNING: Missing block: B:24:0x0073, code skipped:
            java.lang.System.out.println("No timezone? : '" + new java.lang.String(r11) + "'");
     */
    private static java.util.Date parseDate(char[] r11, java.text.ParsePosition r12, boolean r13) {
        /*
        r2 = -1;
        r1 = -1;
        r0 = -1;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = new javax.mail.internet.MailDateParser;	 Catch:{ Exception -> 0x0093 }
        r7.m503init(r11);	 Catch:{ Exception -> 0x0093 }
        r7.skipUntilNumber();	 Catch:{ Exception -> 0x0093 }
        r2 = r7.parseNumber();	 Catch:{ Exception -> 0x0093 }
        r8 = 45;
        r8 = r7.skipIfChar(r8);	 Catch:{ Exception -> 0x0093 }
        if (r8 != 0) goto L_0x001e;
    L_0x001b:
        r7.skipWhiteSpace();	 Catch:{ Exception -> 0x0093 }
    L_0x001e:
        r1 = r7.parseMonth();	 Catch:{ Exception -> 0x0093 }
        r8 = 45;
        r8 = r7.skipIfChar(r8);	 Catch:{ Exception -> 0x0093 }
        if (r8 != 0) goto L_0x002d;
    L_0x002a:
        r7.skipWhiteSpace();	 Catch:{ Exception -> 0x0093 }
    L_0x002d:
        r0 = r7.parseNumber();	 Catch:{ Exception -> 0x0093 }
        r8 = 50;
        if (r0 >= r8) goto L_0x0067;
    L_0x0035:
        r0 = r0 + 2000;
    L_0x0037:
        r7.skipWhiteSpace();	 Catch:{ Exception -> 0x0093 }
        r3 = r7.parseNumber();	 Catch:{ Exception -> 0x0093 }
        r8 = 58;
        r7.skipChar(r8);	 Catch:{ Exception -> 0x0093 }
        r4 = r7.parseNumber();	 Catch:{ Exception -> 0x0093 }
        r8 = 58;
        r8 = r7.skipIfChar(r8);	 Catch:{ Exception -> 0x0093 }
        if (r8 == 0) goto L_0x0053;
    L_0x004f:
        r5 = r7.parseNumber();	 Catch:{ Exception -> 0x0093 }
    L_0x0053:
        r7.skipWhiteSpace();	 Catch:{ ParseException -> 0x006e }
        r6 = r7.parseTimeZone();	 Catch:{ ParseException -> 0x006e }
    L_0x005a:
        r7 = r7.getIndex();	 Catch:{ Exception -> 0x0093 }
        r12.setIndex(r7);	 Catch:{ Exception -> 0x0093 }
        r7 = r13;
        r11 = ourUTC(r0, r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0093 }
    L_0x0066:
        return r11;
    L_0x0067:
        r8 = 100;
        if (r0 >= r8) goto L_0x0037;
    L_0x006b:
        r0 = r0 + 1900;
        goto L_0x0037;
    L_0x006e:
        r8 = move-exception;
        r8 = debug;	 Catch:{ Exception -> 0x0093 }
        if (r8 == 0) goto L_0x005a;
    L_0x0073:
        r8 = java.lang.System.out;	 Catch:{ Exception -> 0x0093 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0093 }
        r10 = "No timezone? : '";
        r9.<init>(r10);	 Catch:{ Exception -> 0x0093 }
        r10 = new java.lang.String;	 Catch:{ Exception -> 0x0093 }
        r10.<init>(r11);	 Catch:{ Exception -> 0x0093 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x0093 }
        r10 = "'";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x0093 }
        r9 = r9.toString();	 Catch:{ Exception -> 0x0093 }
        r8.println(r9);	 Catch:{ Exception -> 0x0093 }
        goto L_0x005a;
    L_0x0093:
        r13 = move-exception;
        r2 = debug;
        if (r2 == 0) goto L_0x00ba;
    L_0x0098:
        r2 = java.lang.System.out;
        r7 = new java.lang.StringBuilder;
        r8 = "Bad date: '";
        r7.<init>(r8);
        r8 = new java.lang.String;
        r8.<init>(r11);
        r11 = r7.append(r8);
        r7 = "'";
        r11 = r11.append(r7);
        r11 = r11.toString();
        r2.println(r11);
        r13.printStackTrace();
    L_0x00ba:
        r11 = 1;
        r12.setIndex(r11);
        r11 = 0;
        goto L_0x0066;
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.internet.MailDateFormat.parseDate(char[], java.text.ParsePosition, boolean):java.util.Date");
    }

    private static synchronized Date ourUTC(int year, int mon, int mday, int hour, int min, int sec, int tzoffset, boolean lenient) {
        Date time;
        synchronized (MailDateFormat.class) {
            cal.clear();
            cal.setLenient(lenient);
            cal.set(1, year);
            cal.set(2, mon);
            cal.set(5, mday);
            cal.set(11, hour);
            cal.set(12, min + tzoffset);
            cal.set(13, sec);
            time = cal.getTime();
        }
        return time;
    }

    public void setCalendar(Calendar newCalendar) {
        throw new RuntimeException("Method setCalendar() shouldn't be called");
    }

    public void setNumberFormat(NumberFormat newNumberFormat) {
        throw new RuntimeException("Method setNumberFormat() shouldn't be called");
    }
}
