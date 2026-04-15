package javax.mail.internet;

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
        encodeParameters = false;
        decodeParameters = false;
        decodeParametersStrict = false;
        applehack = false;
        try {
            boolean z;
            String s = System.getProperty("mail.mime.encodeparameters");
            if (s == null || !s.equalsIgnoreCase("true")) {
                z = false;
            } else {
                z = true;
            }
            encodeParameters = z;
            s = System.getProperty("mail.mime.decodeparameters");
            if (s == null || !s.equalsIgnoreCase("true")) {
                z = false;
            } else {
                z = true;
            }
            decodeParameters = z;
            s = System.getProperty("mail.mime.decodeparameters.strict");
            if (s == null || !s.equalsIgnoreCase("true")) {
                z = false;
            } else {
                z = true;
            }
            decodeParametersStrict = z;
            s = System.getProperty("mail.mime.applefilenames");
            if (s == null || !s.equalsIgnoreCase("true")) {
                z = false;
            } else {
                z = true;
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
        r10.m519init();
        r0 = new javax.mail.internet.HeaderTokenizer;
        r6 = "()<>@,;:\\\"\t []/?=";
        r0.m494init(r11, r6);
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
        r6.m479init(r7);
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
        r6.m479init(r7);
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
        r6.m479init(r7);
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
        r6.m479init(r7);
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.internet.ParameterList.m520init(java.lang.String):void");
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
            this.list.put(rname, "");
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

    /* JADX WARNING: Removed duplicated region for block: B:91:0x017a A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x018c A:{SYNTHETIC} */
    private void combineMultisegmentNames(boolean r26) throws javax.mail.internet.ParseException {
        /*
        r25 = this;
        r15 = 0;
        r0 = r25;
        r0 = r0.multisegmentNames;	 Catch:{ all -> 0x00a9 }
        r22 = r0;
        r7 = r22.iterator();	 Catch:{ all -> 0x00a9 }
    L_0x000b:
        r22 = r7.hasNext();	 Catch:{ all -> 0x00a9 }
        if (r22 != 0) goto L_0x0058;
    L_0x0011:
        r15 = 1;
        if (r26 != 0) goto L_0x0016;
    L_0x0014:
        if (r15 == 0) goto L_0x0057;
    L_0x0016:
        r0 = r25;
        r0 = r0.slist;
        r22 = r0;
        r22 = r22.size();
        if (r22 <= 0) goto L_0x0045;
    L_0x0022:
        r0 = r25;
        r0 = r0.slist;
        r22 = r0;
        r22 = r22.values();
        r13 = r22.iterator();
    L_0x0030:
        r22 = r13.hasNext();
        if (r22 != 0) goto L_0x01ec;
    L_0x0036:
        r0 = r25;
        r0 = r0.list;
        r22 = r0;
        r0 = r25;
        r0 = r0.slist;
        r23 = r0;
        r22.putAll(r23);
    L_0x0045:
        r0 = r25;
        r0 = r0.multisegmentNames;
        r22 = r0;
        r22.clear();
        r0 = r25;
        r0 = r0.slist;
        r22 = r0;
        r22.clear();
    L_0x0057:
        return;
    L_0x0058:
        r9 = r7.next();	 Catch:{ all -> 0x00a9 }
        r9 = (java.lang.String) r9;	 Catch:{ all -> 0x00a9 }
        r11 = new java.lang.StringBuffer;	 Catch:{ all -> 0x00a9 }
        r11.<init>();	 Catch:{ all -> 0x00a9 }
        r8 = new javax.mail.internet.ParameterList$MultiValue;	 Catch:{ all -> 0x00a9 }
        r22 = 0;
        r0 = r8;
        r1 = r22;
        r0.m513init(r1);	 Catch:{ all -> 0x00a9 }
        r3 = 0;
        r12 = 0;
        r4 = r3;
    L_0x0070:
        r22 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a9 }
        r23 = java.lang.String.valueOf(r9);	 Catch:{ all -> 0x00a9 }
        r22.<init>(r23);	 Catch:{ all -> 0x00a9 }
        r23 = "*";
        r22 = r22.append(r23);	 Catch:{ all -> 0x00a9 }
        r0 = r22;
        r1 = r12;
        r22 = r0.append(r1);	 Catch:{ all -> 0x00a9 }
        r14 = r22.toString();	 Catch:{ all -> 0x00a9 }
        r0 = r25;
        r0 = r0.slist;	 Catch:{ all -> 0x00a9 }
        r22 = r0;
        r0 = r22;
        r1 = r14;
        r17 = r0.get(r1);	 Catch:{ all -> 0x00a9 }
        if (r17 != 0) goto L_0x00f0;
    L_0x0099:
        if (r12 != 0) goto L_0x019e;
    L_0x009b:
        r0 = r25;
        r0 = r0.list;	 Catch:{ all -> 0x00a9 }
        r22 = r0;
        r0 = r22;
        r1 = r9;
        r0.remove(r1);	 Catch:{ all -> 0x00a9 }
        goto L_0x000b;
    L_0x00a9:
        r22 = move-exception;
        if (r26 != 0) goto L_0x00ae;
    L_0x00ac:
        if (r15 == 0) goto L_0x00ef;
    L_0x00ae:
        r0 = r25;
        r0 = r0.slist;
        r23 = r0;
        r23 = r23.size();
        if (r23 <= 0) goto L_0x00dd;
    L_0x00ba:
        r0 = r25;
        r0 = r0.slist;
        r23 = r0;
        r23 = r23.values();
        r13 = r23.iterator();
    L_0x00c8:
        r23 = r13.hasNext();
        if (r23 != 0) goto L_0x01b6;
    L_0x00ce:
        r0 = r25;
        r0 = r0.list;
        r23 = r0;
        r0 = r25;
        r0 = r0.slist;
        r24 = r0;
        r23.putAll(r24);
    L_0x00dd:
        r0 = r25;
        r0 = r0.multisegmentNames;
        r23 = r0;
        r23.clear();
        r0 = r25;
        r0 = r0.slist;
        r23 = r0;
        r23.clear();
    L_0x00ef:
        throw r22;
    L_0x00f0:
        r0 = r8;
        r1 = r17;
        r0.add(r1);	 Catch:{ all -> 0x00a9 }
        r18 = 0;
        r0 = r17;
        r0 = r0 instanceof javax.mail.internet.ParameterList.Value;	 Catch:{ all -> 0x00a9 }
        r22 = r0;
        if (r22 == 0) goto L_0x0196;
    L_0x0100:
        r0 = r17;
        r0 = (javax.mail.internet.ParameterList.Value) r0;	 Catch:{ NumberFormatException -> 0x0152, UnsupportedEncodingException -> 0x0172, StringIndexOutOfBoundsException -> 0x0184 }
        r21 = r0;
        r0 = r21;
        r0 = r0.encodedValue;	 Catch:{ NumberFormatException -> 0x0152, UnsupportedEncodingException -> 0x0172, StringIndexOutOfBoundsException -> 0x0184 }
        r5 = r0;
        r18 = r5;
        if (r12 != 0) goto L_0x0142;
    L_0x010f:
        r20 = decodeValue(r5);	 Catch:{ NumberFormatException -> 0x0152, UnsupportedEncodingException -> 0x0172, StringIndexOutOfBoundsException -> 0x0184 }
        r0 = r20;
        r0 = r0.charset;	 Catch:{ NumberFormatException -> 0x0152, UnsupportedEncodingException -> 0x0172, StringIndexOutOfBoundsException -> 0x0184 }
        r3 = r0;
        r0 = r3;
        r1 = r21;
        r1.charset = r0;	 Catch:{ NumberFormatException -> 0x0152, UnsupportedEncodingException -> 0x0172, StringIndexOutOfBoundsException -> 0x0184 }
        r0 = r20;
        r0 = r0.value;	 Catch:{ NumberFormatException -> 0x022c, UnsupportedEncodingException -> 0x0227, StringIndexOutOfBoundsException -> 0x0222 }
        r19 = r0;
        r0 = r19;
        r1 = r21;
        r1.value = r0;	 Catch:{ NumberFormatException -> 0x022c, UnsupportedEncodingException -> 0x0227, StringIndexOutOfBoundsException -> 0x0222 }
        r18 = r19;
    L_0x012b:
        r0 = r11;
        r1 = r18;
        r0.append(r1);	 Catch:{ all -> 0x00a9 }
        r0 = r25;
        r0 = r0.slist;	 Catch:{ all -> 0x00a9 }
        r22 = r0;
        r0 = r22;
        r1 = r14;
        r0.remove(r1);	 Catch:{ all -> 0x00a9 }
        r12 = r12 + 1;
        r4 = r3;
        goto L_0x0070;
    L_0x0142:
        if (r4 != 0) goto L_0x0164;
    L_0x0144:
        r0 = r25;
        r0 = r0.multisegmentNames;	 Catch:{ NumberFormatException -> 0x0152, UnsupportedEncodingException -> 0x0172, StringIndexOutOfBoundsException -> 0x0184 }
        r22 = r0;
        r0 = r22;
        r1 = r9;
        r0.remove(r1);	 Catch:{ NumberFormatException -> 0x0152, UnsupportedEncodingException -> 0x0172, StringIndexOutOfBoundsException -> 0x0184 }
        goto L_0x0099;
    L_0x0152:
        r22 = move-exception;
        r10 = r22;
        r3 = r4;
    L_0x0156:
        r22 = decodeParametersStrict;	 Catch:{ all -> 0x00a9 }
        if (r22 == 0) goto L_0x012b;
    L_0x015a:
        r22 = new javax.mail.internet.ParseException;	 Catch:{ all -> 0x00a9 }
        r23 = r10.toString();	 Catch:{ all -> 0x00a9 }
        r22.m479init(r23);	 Catch:{ all -> 0x00a9 }
        throw r22;	 Catch:{ all -> 0x00a9 }
    L_0x0164:
        r19 = decodeBytes(r5, r4);	 Catch:{ NumberFormatException -> 0x0152, UnsupportedEncodingException -> 0x0172, StringIndexOutOfBoundsException -> 0x0184 }
        r0 = r19;
        r1 = r21;
        r1.value = r0;	 Catch:{ NumberFormatException -> 0x0152, UnsupportedEncodingException -> 0x0172, StringIndexOutOfBoundsException -> 0x0184 }
        r18 = r19;
        r3 = r4;
        goto L_0x012b;
    L_0x0172:
        r22 = move-exception;
        r16 = r22;
        r3 = r4;
    L_0x0176:
        r22 = decodeParametersStrict;	 Catch:{ all -> 0x00a9 }
        if (r22 == 0) goto L_0x012b;
    L_0x017a:
        r22 = new javax.mail.internet.ParseException;	 Catch:{ all -> 0x00a9 }
        r23 = r16.toString();	 Catch:{ all -> 0x00a9 }
        r22.m479init(r23);	 Catch:{ all -> 0x00a9 }
        throw r22;	 Catch:{ all -> 0x00a9 }
    L_0x0184:
        r22 = move-exception;
        r6 = r22;
        r3 = r4;
    L_0x0188:
        r22 = decodeParametersStrict;	 Catch:{ all -> 0x00a9 }
        if (r22 == 0) goto L_0x012b;
    L_0x018c:
        r22 = new javax.mail.internet.ParseException;	 Catch:{ all -> 0x00a9 }
        r23 = r6.toString();	 Catch:{ all -> 0x00a9 }
        r22.m479init(r23);	 Catch:{ all -> 0x00a9 }
        throw r22;	 Catch:{ all -> 0x00a9 }
    L_0x0196:
        r0 = r17;
        r0 = (java.lang.String) r0;	 Catch:{ all -> 0x00a9 }
        r18 = r0;
        r3 = r4;
        goto L_0x012b;
    L_0x019e:
        r22 = r11.toString();	 Catch:{ all -> 0x00a9 }
        r0 = r22;
        r1 = r8;
        r1.value = r0;	 Catch:{ all -> 0x00a9 }
        r0 = r25;
        r0 = r0.list;	 Catch:{ all -> 0x00a9 }
        r22 = r0;
        r0 = r22;
        r1 = r9;
        r2 = r8;
        r0.put(r1, r2);	 Catch:{ all -> 0x00a9 }
        goto L_0x000b;
    L_0x01b6:
        r17 = r13.next();
        r0 = r17;
        r0 = r0 instanceof javax.mail.internet.ParameterList.Value;
        r23 = r0;
        if (r23 == 0) goto L_0x00c8;
    L_0x01c2:
        r0 = r17;
        r0 = (javax.mail.internet.ParameterList.Value) r0;
        r21 = r0;
        r0 = r21;
        r0 = r0.encodedValue;
        r23 = r0;
        r20 = decodeValue(r23);
        r0 = r20;
        r0 = r0.charset;
        r23 = r0;
        r0 = r23;
        r1 = r21;
        r1.charset = r0;
        r0 = r20;
        r0 = r0.value;
        r23 = r0;
        r0 = r23;
        r1 = r21;
        r1.value = r0;
        goto L_0x00c8;
    L_0x01ec:
        r17 = r13.next();
        r0 = r17;
        r0 = r0 instanceof javax.mail.internet.ParameterList.Value;
        r22 = r0;
        if (r22 == 0) goto L_0x0030;
    L_0x01f8:
        r0 = r17;
        r0 = (javax.mail.internet.ParameterList.Value) r0;
        r21 = r0;
        r0 = r21;
        r0 = r0.encodedValue;
        r22 = r0;
        r20 = decodeValue(r22);
        r0 = r20;
        r0 = r0.charset;
        r22 = r0;
        r0 = r22;
        r1 = r21;
        r1.charset = r0;
        r0 = r20;
        r0 = r0.value;
        r22 = r0;
        r0 = r22;
        r1 = r21;
        r1.value = r0;
        goto L_0x0030;
    L_0x0222:
        r22 = move-exception;
        r6 = r22;
        goto L_0x0188;
    L_0x0227:
        r22 = move-exception;
        r16 = r22;
        goto L_0x0176;
    L_0x022c:
        r22 = move-exception;
        r10 = r22;
        goto L_0x0156;
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
            Object v = this.list.get(name);
            if (v instanceof MultiValue) {
                MultiValue vv = (MultiValue) v;
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
                char c = (char) (b2 & 255);
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
            UnsupportedEncodingException ex = e;
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
        } catch (NumberFormatException e) {
            NumberFormatException nex = e;
            if (decodeParametersStrict) {
                throw new ParseException(nex.toString());
            }
        } catch (UnsupportedEncodingException e2) {
            UnsupportedEncodingException uex = e2;
            if (decodeParametersStrict) {
                throw new ParseException(uex.toString());
            }
        } catch (StringIndexOutOfBoundsException e3) {
            StringIndexOutOfBoundsException ex = e3;
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
