package javax.mail.internet;

import android.support.v4.view.MotionEventCompat;
import com.googleprojects.mm.MMMailContentUtil;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ParameterList {
    private static boolean applehack;
    private static boolean decodeParameters;
    private static boolean decodeParametersStrict;
    private static boolean encodeParameters;
    private static final char[] hex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private String lastName;
    private Map list;
    private Set multisegmentNames;
    private Map slist;

    private static class MultiValue extends ArrayList {
        String value;

        private MultiValue() {
        }

        /* synthetic */ MultiValue(MultiValue multiValue) {
            this();
        }
    }

    private static class ParamEnum implements Enumeration {
        private Iterator it;

        ParamEnum(Iterator it) {
            this.it = it;
        }

        public boolean hasMoreElements() {
            return this.it.hasNext();
        }

        public Object nextElement() {
            return this.it.next();
        }
    }

    private static class ToStringBuffer {
        private StringBuffer sb = new StringBuffer();
        private int used;

        public ToStringBuffer(int used) {
            this.used = used;
        }

        public void addNV(String name, String value) {
            value = ParameterList.quote(value);
            this.sb.append("; ");
            this.used += 2;
            if (this.used + ((name.length() + value.length()) + 1) > 76) {
                this.sb.append("\r\n\t");
                this.used = 8;
            }
            this.sb.append(name).append('=');
            this.used += name.length() + 1;
            if (this.used + value.length() > 76) {
                String s = MimeUtility.fold(this.used, value);
                this.sb.append(s);
                int lastlf = s.lastIndexOf(10);
                if (lastlf >= 0) {
                    this.used += (s.length() - lastlf) - 1;
                    return;
                } else {
                    this.used += s.length();
                    return;
                }
            }
            this.sb.append(value);
            this.used += value.length();
        }

        public String toString() {
            return this.sb.toString();
        }
    }

    private static class Value {
        String charset;
        String encodedValue;
        String value;

        private Value() {
        }

        /* synthetic */ Value(Value value) {
            this();
        }
    }

    static {
        boolean z = true;
        encodeParameters = false;
        decodeParameters = false;
        decodeParametersStrict = false;
        applehack = false;
        try {
            String s = System.getProperty("mail.mime.encodeparameters");
            boolean z2 = s != null && s.equalsIgnoreCase("true");
            encodeParameters = z2;
            s = System.getProperty("mail.mime.decodeparameters");
            if (s == null || !s.equalsIgnoreCase("true")) {
                z2 = false;
            } else {
                z2 = true;
            }
            decodeParameters = z2;
            s = System.getProperty("mail.mime.decodeparameters.strict");
            if (s == null || !s.equalsIgnoreCase("true")) {
                z2 = false;
            } else {
                z2 = true;
            }
            decodeParametersStrict = z2;
            s = System.getProperty("mail.mime.applefilenames");
            if (s == null || !s.equalsIgnoreCase("true")) {
                z = false;
            }
            applehack = z;
        } catch (SecurityException e) {
        }
    }

    public ParameterList() {
        this.list = new LinkedHashMap();
        this.lastName = null;
        if (decodeParameters) {
            this.multisegmentNames = new HashSet();
            this.slist = new HashMap();
        }
    }

    /* JADX WARNING: Missing block: B:39:0x0135, code skipped:
            throw new javax.mail.internet.ParseException("Expected ';', got \"" + r3.getValue() + "\"");
     */
    public ParameterList(java.lang.String r11) throws javax.mail.internet.ParseException {
        /*
        r10 = this;
        r9 = -4;
        r8 = -1;
        r10.m454init();
        r0 = new javax.mail.internet.HeaderTokenizer;
        r6 = "()<>@,;:\\\"\t []/?=";
        r0.m436init(r11, r6);
    L_0x000c:
        r3 = r0.next();
        r4 = r3.getType();
        if (r4 != r9) goto L_0x001f;
    L_0x0016:
        r6 = decodeParameters;
        if (r6 == 0) goto L_0x001e;
    L_0x001a:
        r6 = 0;
        r10.combineMultisegmentNames(r6);
    L_0x001e:
        return;
    L_0x001f:
        r6 = (char) r4;
        r7 = 59;
        if (r6 != r7) goto L_0x00cb;
    L_0x0024:
        r3 = r0.next();
        r6 = r3.getType();
        if (r6 == r9) goto L_0x0016;
    L_0x002e:
        r6 = r3.getType();
        if (r6 == r8) goto L_0x0053;
    L_0x0034:
        r6 = new javax.mail.internet.ParseException;
        r7 = new java.lang.StringBuilder;
        r8 = "Expected parameter name, got \"";
        r7.<init>(r8);
        r8 = r3.getValue();
        r7 = r7.append(r8);
        r8 = "\"";
        r7 = r7.append(r8);
        r7 = r7.toString();
        r6.m754init(r7);
        throw r6;
    L_0x0053:
        r6 = r3.getValue();
        r7 = java.util.Locale.ENGLISH;
        r2 = r6.toLowerCase(r7);
        r3 = r0.next();
        r6 = r3.getType();
        r6 = (char) r6;
        r7 = 61;
        if (r6 == r7) goto L_0x0089;
    L_0x006a:
        r6 = new javax.mail.internet.ParseException;
        r7 = new java.lang.StringBuilder;
        r8 = "Expected '=', got \"";
        r7.<init>(r8);
        r8 = r3.getValue();
        r7 = r7.append(r8);
        r8 = "\"";
        r7 = r7.append(r8);
        r7 = r7.toString();
        r6.m754init(r7);
        throw r6;
    L_0x0089:
        r3 = r0.next();
        r4 = r3.getType();
        if (r4 == r8) goto L_0x00b5;
    L_0x0093:
        r6 = -2;
        if (r4 == r6) goto L_0x00b5;
    L_0x0096:
        r6 = new javax.mail.internet.ParseException;
        r7 = new java.lang.StringBuilder;
        r8 = "Expected parameter value, got \"";
        r7.<init>(r8);
        r8 = r3.getValue();
        r7 = r7.append(r8);
        r8 = "\"";
        r7 = r7.append(r8);
        r7 = r7.toString();
        r6.m754init(r7);
        throw r6;
    L_0x00b5:
        r5 = r3.getValue();
        r10.lastName = r2;
        r6 = decodeParameters;
        if (r6 == 0) goto L_0x00c4;
    L_0x00bf:
        r10.putEncodedName(r2, r5);
        goto L_0x000c;
    L_0x00c4:
        r6 = r10.list;
        r6.put(r2, r5);
        goto L_0x000c;
    L_0x00cb:
        r6 = applehack;
        if (r6 == 0) goto L_0x0117;
    L_0x00cf:
        if (r4 != r8) goto L_0x0117;
    L_0x00d1:
        r6 = r10.lastName;
        if (r6 == 0) goto L_0x0117;
    L_0x00d5:
        r6 = r10.lastName;
        r7 = "name";
        r6 = r6.equals(r7);
        if (r6 != 0) goto L_0x00e9;
    L_0x00df:
        r6 = r10.lastName;
        r7 = "filename";
        r6 = r6.equals(r7);
        if (r6 == 0) goto L_0x0117;
    L_0x00e9:
        r6 = r10.list;
        r7 = r10.lastName;
        r1 = r6.get(r7);
        r1 = (java.lang.String) r1;
        r6 = new java.lang.StringBuilder;
        r7 = java.lang.String.valueOf(r1);
        r6.<init>(r7);
        r7 = " ";
        r6 = r6.append(r7);
        r7 = r3.getValue();
        r6 = r6.append(r7);
        r5 = r6.toString();
        r6 = r10.list;
        r7 = r10.lastName;
        r6.put(r7, r5);
        goto L_0x000c;
    L_0x0117:
        r6 = new javax.mail.internet.ParseException;
        r7 = new java.lang.StringBuilder;
        r8 = "Expected ';', got \"";
        r7.<init>(r8);
        r8 = r3.getValue();
        r7 = r7.append(r8);
        r8 = "\"";
        r7 = r7.append(r8);
        r7 = r7.toString();
        r6.m754init(r7);
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.internet.ParameterList.m455init(java.lang.String):void");
    }

    private void putEncodedName(String name, String value) throws ParseException {
        int star = name.indexOf(42);
        if (star < 0) {
            this.list.put(name, value);
        } else if (star == name.length() - 1) {
            this.list.put(name.substring(0, star), decodeValue(value));
        } else {
            Object v;
            String rname = name.substring(0, star);
            this.multisegmentNames.add(rname);
            this.list.put(rname, MMMailContentUtil.MM_MESSAGE_SUBJECT);
            if (name.endsWith("*")) {
                v = new Value();
                ((Value) v).encodedValue = value;
                ((Value) v).value = value;
                name = name.substring(0, name.length() - 1);
            } else {
                String v2 = value;
            }
            this.slist.put(name, v2);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:87:0x016b A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x017b A:{SYNTHETIC} */
    private void combineMultisegmentNames(boolean r25) throws javax.mail.internet.ParseException {
        /*
        r24 = this;
        r14 = 0;
        r0 = r24;
        r0 = r0.multisegmentNames;	 Catch:{ all -> 0x00a5 }
        r21 = r0;
        r6 = r21.iterator();	 Catch:{ all -> 0x00a5 }
    L_0x000b:
        r21 = r6.hasNext();	 Catch:{ all -> 0x00a5 }
        if (r21 != 0) goto L_0x0058;
    L_0x0011:
        r14 = 1;
        if (r25 != 0) goto L_0x0016;
    L_0x0014:
        if (r14 == 0) goto L_0x0057;
    L_0x0016:
        r0 = r24;
        r0 = r0.slist;
        r21 = r0;
        r21 = r21.size();
        if (r21 <= 0) goto L_0x0045;
    L_0x0022:
        r0 = r24;
        r0 = r0.slist;
        r21 = r0;
        r21 = r21.values();
        r12 = r21.iterator();
    L_0x0030:
        r21 = r12.hasNext();
        if (r21 != 0) goto L_0x01d6;
    L_0x0036:
        r0 = r24;
        r0 = r0.list;
        r21 = r0;
        r0 = r24;
        r0 = r0.slist;
        r22 = r0;
        r21.putAll(r22);
    L_0x0045:
        r0 = r24;
        r0 = r0.multisegmentNames;
        r21 = r0;
        r21.clear();
        r0 = r24;
        r0 = r0.slist;
        r21 = r0;
        r21.clear();
    L_0x0057:
        return;
    L_0x0058:
        r8 = r6.next();	 Catch:{ all -> 0x00a5 }
        r8 = (java.lang.String) r8;	 Catch:{ all -> 0x00a5 }
        r10 = new java.lang.StringBuffer;	 Catch:{ all -> 0x00a5 }
        r10.<init>();	 Catch:{ all -> 0x00a5 }
        r7 = new javax.mail.internet.ParameterList$MultiValue;	 Catch:{ all -> 0x00a5 }
        r21 = 0;
        r0 = r21;
        r7.m448init(r0);	 Catch:{ all -> 0x00a5 }
        r2 = 0;
        r11 = 0;
        r3 = r2;
    L_0x006f:
        r21 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a5 }
        r22 = java.lang.String.valueOf(r8);	 Catch:{ all -> 0x00a5 }
        r21.<init>(r22);	 Catch:{ all -> 0x00a5 }
        r22 = "*";
        r21 = r21.append(r22);	 Catch:{ all -> 0x00a5 }
        r0 = r21;
        r21 = r0.append(r11);	 Catch:{ all -> 0x00a5 }
        r13 = r21.toString();	 Catch:{ all -> 0x00a5 }
        r0 = r24;
        r0 = r0.slist;	 Catch:{ all -> 0x00a5 }
        r21 = r0;
        r0 = r21;
        r16 = r0.get(r13);	 Catch:{ all -> 0x00a5 }
        if (r16 != 0) goto L_0x00ec;
    L_0x0096:
        if (r11 != 0) goto L_0x018d;
    L_0x0098:
        r0 = r24;
        r0 = r0.list;	 Catch:{ all -> 0x00a5 }
        r21 = r0;
        r0 = r21;
        r0.remove(r8);	 Catch:{ all -> 0x00a5 }
        goto L_0x000b;
    L_0x00a5:
        r21 = move-exception;
        if (r25 != 0) goto L_0x00aa;
    L_0x00a8:
        if (r14 == 0) goto L_0x00eb;
    L_0x00aa:
        r0 = r24;
        r0 = r0.slist;
        r22 = r0;
        r22 = r22.size();
        if (r22 <= 0) goto L_0x00d9;
    L_0x00b6:
        r0 = r24;
        r0 = r0.slist;
        r22 = r0;
        r22 = r22.values();
        r12 = r22.iterator();
    L_0x00c4:
        r22 = r12.hasNext();
        if (r22 != 0) goto L_0x01a2;
    L_0x00ca:
        r0 = r24;
        r0 = r0.list;
        r22 = r0;
        r0 = r24;
        r0 = r0.slist;
        r23 = r0;
        r22.putAll(r23);
    L_0x00d9:
        r0 = r24;
        r0 = r0.multisegmentNames;
        r22 = r0;
        r22.clear();
        r0 = r24;
        r0 = r0.slist;
        r22 = r0;
        r22.clear();
    L_0x00eb:
        throw r21;
    L_0x00ec:
        r0 = r16;
        r7.add(r0);	 Catch:{ all -> 0x00a5 }
        r17 = 0;
        r0 = r16;
        r0 = r0 instanceof javax.mail.internet.ParameterList.Value;	 Catch:{ all -> 0x00a5 }
        r21 = r0;
        if (r21 == 0) goto L_0x0185;
    L_0x00fb:
        r0 = r16;
        r0 = (javax.mail.internet.ParameterList.Value) r0;	 Catch:{ NumberFormatException -> 0x0147, UnsupportedEncodingException -> 0x0165, StringIndexOutOfBoundsException -> 0x0175 }
        r20 = r0;
        r0 = r20;
        r4 = r0.encodedValue;	 Catch:{ NumberFormatException -> 0x0147, UnsupportedEncodingException -> 0x0165, StringIndexOutOfBoundsException -> 0x0175 }
        r17 = r4;
        if (r11 != 0) goto L_0x0138;
    L_0x0109:
        r19 = decodeValue(r4);	 Catch:{ NumberFormatException -> 0x0147, UnsupportedEncodingException -> 0x0165, StringIndexOutOfBoundsException -> 0x0175 }
        r0 = r19;
        r2 = r0.charset;	 Catch:{ NumberFormatException -> 0x0147, UnsupportedEncodingException -> 0x0165, StringIndexOutOfBoundsException -> 0x0175 }
        r0 = r20;
        r0.charset = r2;	 Catch:{ NumberFormatException -> 0x0147, UnsupportedEncodingException -> 0x0165, StringIndexOutOfBoundsException -> 0x0175 }
        r0 = r19;
        r0 = r0.value;	 Catch:{ NumberFormatException -> 0x0210, UnsupportedEncodingException -> 0x020d, StringIndexOutOfBoundsException -> 0x020a }
        r18 = r0;
        r0 = r18;
        r1 = r20;
        r1.value = r0;	 Catch:{ NumberFormatException -> 0x0210, UnsupportedEncodingException -> 0x020d, StringIndexOutOfBoundsException -> 0x020a }
        r17 = r18;
    L_0x0123:
        r0 = r17;
        r10.append(r0);	 Catch:{ all -> 0x00a5 }
        r0 = r24;
        r0 = r0.slist;	 Catch:{ all -> 0x00a5 }
        r21 = r0;
        r0 = r21;
        r0.remove(r13);	 Catch:{ all -> 0x00a5 }
        r11 = r11 + 1;
        r3 = r2;
        goto L_0x006f;
    L_0x0138:
        if (r3 != 0) goto L_0x0157;
    L_0x013a:
        r0 = r24;
        r0 = r0.multisegmentNames;	 Catch:{ NumberFormatException -> 0x0147, UnsupportedEncodingException -> 0x0165, StringIndexOutOfBoundsException -> 0x0175 }
        r21 = r0;
        r0 = r21;
        r0.remove(r8);	 Catch:{ NumberFormatException -> 0x0147, UnsupportedEncodingException -> 0x0165, StringIndexOutOfBoundsException -> 0x0175 }
        goto L_0x0096;
    L_0x0147:
        r9 = move-exception;
        r2 = r3;
    L_0x0149:
        r21 = decodeParametersStrict;	 Catch:{ all -> 0x00a5 }
        if (r21 == 0) goto L_0x0123;
    L_0x014d:
        r21 = new javax.mail.internet.ParseException;	 Catch:{ all -> 0x00a5 }
        r22 = r9.toString();	 Catch:{ all -> 0x00a5 }
        r21.m754init(r22);	 Catch:{ all -> 0x00a5 }
        throw r21;	 Catch:{ all -> 0x00a5 }
    L_0x0157:
        r18 = decodeBytes(r4, r3);	 Catch:{ NumberFormatException -> 0x0147, UnsupportedEncodingException -> 0x0165, StringIndexOutOfBoundsException -> 0x0175 }
        r0 = r18;
        r1 = r20;
        r1.value = r0;	 Catch:{ NumberFormatException -> 0x0147, UnsupportedEncodingException -> 0x0165, StringIndexOutOfBoundsException -> 0x0175 }
        r17 = r18;
        r2 = r3;
        goto L_0x0123;
    L_0x0165:
        r15 = move-exception;
        r2 = r3;
    L_0x0167:
        r21 = decodeParametersStrict;	 Catch:{ all -> 0x00a5 }
        if (r21 == 0) goto L_0x0123;
    L_0x016b:
        r21 = new javax.mail.internet.ParseException;	 Catch:{ all -> 0x00a5 }
        r22 = r15.toString();	 Catch:{ all -> 0x00a5 }
        r21.m754init(r22);	 Catch:{ all -> 0x00a5 }
        throw r21;	 Catch:{ all -> 0x00a5 }
    L_0x0175:
        r5 = move-exception;
        r2 = r3;
    L_0x0177:
        r21 = decodeParametersStrict;	 Catch:{ all -> 0x00a5 }
        if (r21 == 0) goto L_0x0123;
    L_0x017b:
        r21 = new javax.mail.internet.ParseException;	 Catch:{ all -> 0x00a5 }
        r22 = r5.toString();	 Catch:{ all -> 0x00a5 }
        r21.m754init(r22);	 Catch:{ all -> 0x00a5 }
        throw r21;	 Catch:{ all -> 0x00a5 }
    L_0x0185:
        r0 = r16;
        r0 = (java.lang.String) r0;	 Catch:{ all -> 0x00a5 }
        r17 = r0;
        r2 = r3;
        goto L_0x0123;
    L_0x018d:
        r21 = r10.toString();	 Catch:{ all -> 0x00a5 }
        r0 = r21;
        r7.value = r0;	 Catch:{ all -> 0x00a5 }
        r0 = r24;
        r0 = r0.list;	 Catch:{ all -> 0x00a5 }
        r21 = r0;
        r0 = r21;
        r0.put(r8, r7);	 Catch:{ all -> 0x00a5 }
        goto L_0x000b;
    L_0x01a2:
        r16 = r12.next();
        r0 = r16;
        r0 = r0 instanceof javax.mail.internet.ParameterList.Value;
        r22 = r0;
        if (r22 == 0) goto L_0x00c4;
    L_0x01ae:
        r20 = r16;
        r20 = (javax.mail.internet.ParameterList.Value) r20;
        r0 = r20;
        r0 = r0.encodedValue;
        r22 = r0;
        r19 = decodeValue(r22);
        r0 = r19;
        r0 = r0.charset;
        r22 = r0;
        r0 = r22;
        r1 = r20;
        r1.charset = r0;
        r0 = r19;
        r0 = r0.value;
        r22 = r0;
        r0 = r22;
        r1 = r20;
        r1.value = r0;
        goto L_0x00c4;
    L_0x01d6:
        r16 = r12.next();
        r0 = r16;
        r0 = r0 instanceof javax.mail.internet.ParameterList.Value;
        r21 = r0;
        if (r21 == 0) goto L_0x0030;
    L_0x01e2:
        r20 = r16;
        r20 = (javax.mail.internet.ParameterList.Value) r20;
        r0 = r20;
        r0 = r0.encodedValue;
        r21 = r0;
        r19 = decodeValue(r21);
        r0 = r19;
        r0 = r0.charset;
        r21 = r0;
        r0 = r21;
        r1 = r20;
        r1.charset = r0;
        r0 = r19;
        r0 = r0.value;
        r21 = r0;
        r0 = r21;
        r1 = r20;
        r1.value = r0;
        goto L_0x0030;
    L_0x020a:
        r5 = move-exception;
        goto L_0x0177;
    L_0x020d:
        r15 = move-exception;
        goto L_0x0167;
    L_0x0210:
        r9 = move-exception;
        goto L_0x0149;
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.internet.ParameterList.combineMultisegmentNames(boolean):void");
    }

    public int size() {
        return this.list.size();
    }

    public String get(String name) {
        Object v = this.list.get(name.trim().toLowerCase(Locale.ENGLISH));
        if (v instanceof MultiValue) {
            return ((MultiValue) v).value;
        }
        if (v instanceof Value) {
            return ((Value) v).value;
        }
        return (String) v;
    }

    public void set(String name, String value) {
        if (name != null || value == null || !value.equals("DONE")) {
            name = name.trim().toLowerCase(Locale.ENGLISH);
            if (decodeParameters) {
                try {
                    putEncodedName(name, value);
                    return;
                } catch (ParseException e) {
                    this.list.put(name, value);
                    return;
                }
            }
            this.list.put(name, value);
        } else if (decodeParameters && this.multisegmentNames.size() > 0) {
            try {
                combineMultisegmentNames(true);
            } catch (ParseException e2) {
            }
        }
    }

    public void set(String name, String value, String charset) {
        if (encodeParameters) {
            Value ev = encodeValue(value, charset);
            if (ev != null) {
                this.list.put(name.trim().toLowerCase(Locale.ENGLISH), ev);
                return;
            } else {
                set(name, value);
                return;
            }
        }
        set(name, value);
    }

    public void remove(String name) {
        this.list.remove(name.trim().toLowerCase(Locale.ENGLISH));
    }

    public Enumeration getNames() {
        return new ParamEnum(this.list.keySet().iterator());
    }

    public String toString() {
        return toString(0);
    }

    public String toString(int used) {
        ToStringBuffer sb = new ToStringBuffer(used);
        for (String name : this.list.keySet()) {
            MultiValue v = this.list.get(name);
            if (v instanceof MultiValue) {
                MultiValue vv = v;
                String ns = new StringBuilder(String.valueOf(name)).append("*").toString();
                for (int i = 0; i < vv.size(); i++) {
                    Object va = vv.get(i);
                    if (va instanceof Value) {
                        sb.addNV(new StringBuilder(String.valueOf(ns)).append(i).append("*").toString(), ((Value) va).encodedValue);
                    } else {
                        sb.addNV(new StringBuilder(String.valueOf(ns)).append(i).toString(), (String) va);
                    }
                }
            } else if (v instanceof Value) {
                sb.addNV(new StringBuilder(String.valueOf(name)).append("*").toString(), ((Value) v).encodedValue);
            } else {
                sb.addNV(name, (String) v);
            }
        }
        return sb.toString();
    }

    /* access modifiers changed from: private|static */
    public static String quote(String value) {
        return MimeUtility.quote(value, HeaderTokenizer.MIME);
    }

    private static Value encodeValue(String value, String charset) {
        if (MimeUtility.checkAscii(value) == 1) {
            return null;
        }
        try {
            byte[] b = value.getBytes(MimeUtility.javaCharset(charset));
            StringBuffer sb = new StringBuffer((b.length + charset.length()) + 2);
            sb.append(charset).append("''");
            for (byte b2 : b) {
                char c = (char) (b2 & MotionEventCompat.ACTION_MASK);
                if (c <= ' ' || c >= 127 || c == '*' || c == '\'' || c == '%' || HeaderTokenizer.MIME.indexOf(c) >= 0) {
                    sb.append('%').append(hex[c >> 4]).append(hex[c & 15]);
                } else {
                    sb.append(c);
                }
            }
            Value v = new Value();
            v.charset = charset;
            v.value = value;
            v.encodedValue = sb.toString();
            return v;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private static Value decodeValue(String value) throws ParseException {
        Value v = new Value();
        v.encodedValue = value;
        v.value = value;
        try {
            int i = value.indexOf(39);
            if (i > 0) {
                String charset = value.substring(0, i);
                int li = value.indexOf(39, i + 1);
                if (li >= 0) {
                    String lang = value.substring(i + 1, li);
                    value = value.substring(li + 1);
                    v.charset = charset;
                    v.value = decodeBytes(value, charset);
                } else if (decodeParametersStrict) {
                    throw new ParseException("Missing language in encoded value: " + value);
                }
            } else if (decodeParametersStrict) {
                throw new ParseException("Missing charset in encoded value: " + value);
            }
        } catch (NumberFormatException nex) {
            if (decodeParametersStrict) {
                throw new ParseException(nex.toString());
            }
        } catch (UnsupportedEncodingException uex) {
            if (decodeParametersStrict) {
                throw new ParseException(uex.toString());
            }
        } catch (StringIndexOutOfBoundsException ex) {
            if (decodeParametersStrict) {
                throw new ParseException(ex.toString());
            }
        }
        return v;
    }

    private static String decodeBytes(String value, String charset) throws UnsupportedEncodingException {
        byte[] b = new byte[value.length()];
        int i = 0;
        int bi = 0;
        while (i < value.length()) {
            char c = value.charAt(i);
            if (c == '%') {
                c = (char) Integer.parseInt(value.substring(i + 1, i + 3), 16);
                i += 2;
            }
            int bi2 = bi + 1;
            b[bi] = (byte) c;
            i++;
            bi = bi2;
        }
        return new String(b, 0, bi, MimeUtility.javaCharset(charset));
    }
}
