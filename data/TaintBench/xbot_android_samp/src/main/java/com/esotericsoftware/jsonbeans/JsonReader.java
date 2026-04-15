package com.esotericsoftware.jsonbeans;

import com.esotericsoftware.jsonbeans.JsonValue.ValueType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import org.java_websocket.drafts.Draft_75;
import org.mozilla.classfile.ByteCode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

public class JsonReader {
    private static final byte[] _json_actions = init__json_actions_0();
    private static final byte[] _json_eof_actions = init__json_eof_actions_0();
    private static final short[] _json_index_offsets = init__json_index_offsets_0();
    private static final short[] _json_key_offsets = init__json_key_offsets_0();
    private static final byte[] _json_range_lengths = init__json_range_lengths_0();
    private static final byte[] _json_single_lengths = init__json_single_lengths_0();
    private static final byte[] _json_trans_actions = init__json_trans_actions_0();
    private static final char[] _json_trans_keys = init__json_trans_keys_0();
    private static final byte[] _json_trans_targs = init__json_trans_targs_0();
    static final int json_en_array = 46;
    static final int json_en_main = 1;
    static final int json_en_object = 8;
    static final int json_error = 0;
    static final int json_first_final = 72;
    static final int json_start = 1;
    private JsonValue current;
    private final ArrayList<JsonValue> elements = new ArrayList(8);
    private JsonValue root;

    public JsonValue parse(String str) {
        char[] toCharArray = str.toCharArray();
        return parse(toCharArray, 0, toCharArray.length);
    }

    public JsonValue parse(Reader reader) {
        int i = 0;
        try {
            char[] cArr = new char[Opcodes.ACC_ABSTRACT];
            while (true) {
                int read = reader.read(cArr, i, cArr.length - i);
                if (read == -1) {
                    break;
                }
                char[] cArr2;
                if (read == 0) {
                    cArr2 = new char[(cArr.length * 2)];
                    System.arraycopy(cArr, 0, cArr2, 0, cArr.length);
                } else {
                    i += read;
                    cArr2 = cArr;
                }
                cArr = cArr2;
            }
            JsonValue parse = parse(cArr, 0, i);
            try {
                reader.close();
            } catch (IOException e) {
            }
            return parse;
        } catch (IOException e2) {
            throw new JsonException(e2);
        } catch (Throwable th) {
            try {
                reader.close();
            } catch (IOException e3) {
            }
        }
    }

    public JsonValue parse(InputStream inputStream) {
        try {
            return parse(new InputStreamReader(inputStream, "ISO-8859-1"));
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public JsonValue parse(File file) {
        try {
            return parse(new FileInputStream(file));
        } catch (Exception e) {
            throw new JsonException("Error parsing file: " + file, e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0076 A:{Catch:{ RuntimeException -> 0x03a9 }} */
    /* JADX WARNING: Removed duplicated region for block: B:166:0x03d9  */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0025  */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0025  */
    /* JADX WARNING: Removed duplicated region for block: B:166:0x03d9  */
    /* JADX WARNING: Missing block: B:111:0x028b, code skipped:
            if (r9 != (byte) 0) goto L_0x0295;
     */
    /* JADX WARNING: Missing block: B:112:0x028d, code skipped:
            r5 = r9;
            r9 = 5;
     */
    /* JADX WARNING: Missing block: B:113:0x0295, code skipped:
            r5 = r6 + 1;
     */
    /* JADX WARNING: Missing block: B:114:0x0299, code skipped:
            if (r5 == r24) goto L_0x02a4;
     */
    /* JADX WARNING: Missing block: B:115:0x029b, code skipped:
            r6 = r5;
            r5 = r9;
            r9 = 1;
     */
    /* JADX WARNING: Missing block: B:116:0x02a4, code skipped:
            r6 = r3;
            r11 = r4;
            r3 = r9;
     */
    public com.esotericsoftware.jsonbeans.JsonValue parse(char[] r22, int r23, int r24) {
        /*
        r21 = this;
        r3 = 4;
        r10 = new int[r3];
        r7 = 0;
        r15 = new java.util.ArrayList;
        r3 = 8;
        r15.<init>(r3);
        r5 = 0;
        r4 = 0;
        r8 = 0;
        r9 = 1;
        r6 = 0;
        r3 = 0;
        r14 = r6;
        r6 = r23;
        r20 = r5;
        r5 = r9;
        r9 = r3;
        r3 = r4;
        r4 = r20;
    L_0x001b:
        switch(r9) {
            case 0: goto L_0x0039;
            case 1: goto L_0x0043;
            case 2: goto L_0x0438;
            case 3: goto L_0x001e;
            case 4: goto L_0x042f;
            default: goto L_0x001e;
        };
    L_0x001e:
        r5 = r6;
    L_0x001f:
        r6 = r5;
        r5 = r8;
    L_0x0021:
        r0 = r24;
        if (r6 >= r0) goto L_0x03d9;
    L_0x0025:
        r4 = 1;
        r3 = 0;
        r20 = r3;
        r3 = r4;
        r4 = r20;
    L_0x002c:
        if (r4 >= r6) goto L_0x03ad;
    L_0x002e:
        r7 = r22[r4];
        r8 = 10;
        if (r7 != r8) goto L_0x0036;
    L_0x0034:
        r3 = r3 + 1;
    L_0x0036:
        r4 = r4 + 1;
        goto L_0x002c;
    L_0x0039:
        r0 = r24;
        if (r6 != r0) goto L_0x003f;
    L_0x003d:
        r9 = 4;
        goto L_0x001b;
    L_0x003f:
        if (r5 != 0) goto L_0x0043;
    L_0x0041:
        r9 = 5;
        goto L_0x001b;
    L_0x0043:
        r9 = _json_key_offsets;	 Catch:{ RuntimeException -> 0x03a9 }
        r12 = r9[r5];	 Catch:{ RuntimeException -> 0x03a9 }
        r9 = _json_index_offsets;	 Catch:{ RuntimeException -> 0x03a9 }
        r9 = r9[r5];	 Catch:{ RuntimeException -> 0x03a9 }
        r11 = _json_single_lengths;	 Catch:{ RuntimeException -> 0x03a9 }
        r16 = r11[r5];	 Catch:{ RuntimeException -> 0x03a9 }
        if (r16 <= 0) goto L_0x005c;
    L_0x0051:
        r11 = r12 + r16;
        r11 = r11 + -1;
        r13 = r12;
    L_0x0056:
        if (r11 >= r13) goto L_0x009d;
    L_0x0058:
        r12 = r12 + r16;
        r9 = r9 + r16;
    L_0x005c:
        r11 = _json_range_lengths;	 Catch:{ RuntimeException -> 0x03a9 }
        r13 = r11[r5];	 Catch:{ RuntimeException -> 0x03a9 }
        if (r13 <= 0) goto L_0x042c;
    L_0x0062:
        r5 = r13 << 1;
        r5 = r5 + r12;
        r5 = r5 + -2;
        r11 = r12;
    L_0x0068:
        if (r5 >= r11) goto L_0x00c3;
    L_0x006a:
        r5 = r9 + r13;
    L_0x006c:
        r9 = _json_trans_targs;	 Catch:{ RuntimeException -> 0x03a9 }
        r9 = r9[r5];	 Catch:{ RuntimeException -> 0x03a9 }
        r11 = _json_trans_actions;	 Catch:{ RuntimeException -> 0x03a9 }
        r11 = r11[r5];	 Catch:{ RuntimeException -> 0x03a9 }
        if (r11 == 0) goto L_0x028b;
    L_0x0076:
        r11 = _json_trans_actions;	 Catch:{ RuntimeException -> 0x03a9 }
        r5 = r11[r5];	 Catch:{ RuntimeException -> 0x03a9 }
        r12 = _json_actions;	 Catch:{ RuntimeException -> 0x03a9 }
        r11 = r5 + 1;
        r5 = r12[r5];	 Catch:{ RuntimeException -> 0x03a9 }
        r12 = r11;
        r20 = r5;
        r5 = r4;
        r4 = r3;
        r3 = r20;
    L_0x0087:
        r11 = r3 + -1;
        if (r3 <= 0) goto L_0x0289;
    L_0x008b:
        r3 = _json_actions;	 Catch:{ RuntimeException -> 0x03a9 }
        r13 = r12 + 1;
        r3 = r3[r12];	 Catch:{ RuntimeException -> 0x03a9 }
        switch(r3) {
            case 0: goto L_0x00f0;
            case 1: goto L_0x00f4;
            case 2: goto L_0x00fc;
            case 3: goto L_0x0114;
            case 4: goto L_0x0146;
            case 5: goto L_0x0173;
            case 6: goto L_0x01a0;
            case 7: goto L_0x01bf;
            case 8: goto L_0x01de;
            case 9: goto L_0x01fd;
            case 10: goto L_0x0236;
            case 11: goto L_0x0245;
            case 12: goto L_0x027a;
            default: goto L_0x0094;
        };	 Catch:{ RuntimeException -> 0x03a9 }
    L_0x0094:
        r3 = r4;
        r4 = r5;
        r5 = r7;
    L_0x0097:
        r12 = r13;
        r7 = r5;
        r5 = r4;
        r4 = r3;
        r3 = r11;
        goto L_0x0087;
    L_0x009d:
        r17 = r11 - r13;
        r17 = r17 >> 1;
        r17 = r17 + r13;
        r18 = r22[r6];	 Catch:{ RuntimeException -> 0x03a9 }
        r19 = _json_trans_keys;	 Catch:{ RuntimeException -> 0x03a9 }
        r19 = r19[r17];	 Catch:{ RuntimeException -> 0x03a9 }
        r0 = r18;
        r1 = r19;
        if (r0 >= r1) goto L_0x00b2;
    L_0x00af:
        r11 = r17 + -1;
        goto L_0x0056;
    L_0x00b2:
        r13 = r22[r6];	 Catch:{ RuntimeException -> 0x03a9 }
        r18 = _json_trans_keys;	 Catch:{ RuntimeException -> 0x03a9 }
        r18 = r18[r17];	 Catch:{ RuntimeException -> 0x03a9 }
        r0 = r18;
        if (r13 <= r0) goto L_0x00bf;
    L_0x00bc:
        r13 = r17 + 1;
        goto L_0x0056;
    L_0x00bf:
        r5 = r17 - r12;
        r5 = r5 + r9;
        goto L_0x006c;
    L_0x00c3:
        r16 = r5 - r11;
        r16 = r16 >> 1;
        r16 = r16 & -2;
        r16 = r16 + r11;
        r17 = r22[r6];	 Catch:{ RuntimeException -> 0x03a9 }
        r18 = _json_trans_keys;	 Catch:{ RuntimeException -> 0x03a9 }
        r18 = r18[r16];	 Catch:{ RuntimeException -> 0x03a9 }
        r0 = r17;
        r1 = r18;
        if (r0 >= r1) goto L_0x00da;
    L_0x00d7:
        r5 = r16 + -2;
        goto L_0x0068;
    L_0x00da:
        r11 = r22[r6];	 Catch:{ RuntimeException -> 0x03a9 }
        r17 = _json_trans_keys;	 Catch:{ RuntimeException -> 0x03a9 }
        r18 = r16 + 1;
        r17 = r17[r18];	 Catch:{ RuntimeException -> 0x03a9 }
        r0 = r17;
        if (r11 <= r0) goto L_0x00e9;
    L_0x00e6:
        r11 = r16 + 2;
        goto L_0x0068;
    L_0x00e9:
        r5 = r16 - r12;
        r5 = r5 >> 1;
        r5 = r5 + r9;
        goto L_0x006c;
    L_0x00f0:
        r4 = 0;
        r3 = 0;
        r5 = r6;
        goto L_0x0097;
    L_0x00f4:
        r3 = 1;
        r5 = r7;
        r20 = r3;
        r3 = r4;
        r4 = r20;
        goto L_0x0097;
    L_0x00fc:
        r3 = new java.lang.String;	 Catch:{ RuntimeException -> 0x03a9 }
        r12 = r6 - r7;
        r0 = r22;
        r3.<init>(r0, r7, r12);	 Catch:{ RuntimeException -> 0x03a9 }
        if (r5 == 0) goto L_0x010d;
    L_0x0107:
        r0 = r21;
        r3 = r0.unescape(r3);	 Catch:{ RuntimeException -> 0x03a9 }
    L_0x010d:
        r15.add(r3);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r4;
        r4 = r5;
        r5 = r6;
        goto L_0x0097;
    L_0x0114:
        if (r4 != 0) goto L_0x0094;
    L_0x0116:
        r3 = new java.lang.String;	 Catch:{ RuntimeException -> 0x03a9 }
        r12 = r6 - r7;
        r0 = r22;
        r3.<init>(r0, r7, r12);	 Catch:{ RuntimeException -> 0x03a9 }
        if (r5 == 0) goto L_0x0429;
    L_0x0121:
        r0 = r21;
        r3 = r0.unescape(r3);	 Catch:{ RuntimeException -> 0x03a9 }
        r7 = r3;
    L_0x0128:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x03a9 }
        if (r3 <= 0) goto L_0x0144;
    L_0x012e:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r3 + -1;
        r3 = r15.remove(r3);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = (java.lang.String) r3;	 Catch:{ RuntimeException -> 0x03a9 }
    L_0x013a:
        r0 = r21;
        r0.string(r3, r7);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r4;
        r4 = r5;
        r5 = r6;
        goto L_0x0097;
    L_0x0144:
        r3 = 0;
        goto L_0x013a;
    L_0x0146:
        r12 = new java.lang.String;	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r6 - r7;
        r0 = r22;
        r12.<init>(r0, r7, r3);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x03a9 }
        if (r3 <= 0) goto L_0x0171;
    L_0x0155:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r3 + -1;
        r3 = r15.remove(r3);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = (java.lang.String) r3;	 Catch:{ RuntimeException -> 0x03a9 }
    L_0x0161:
        r16 = java.lang.Double.parseDouble(r12);	 Catch:{ RuntimeException -> 0x03a9 }
        r0 = r21;
        r1 = r16;
        r0.number(r3, r1);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r4;
        r4 = r5;
        r5 = r6;
        goto L_0x0097;
    L_0x0171:
        r3 = 0;
        goto L_0x0161;
    L_0x0173:
        r12 = new java.lang.String;	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r6 - r7;
        r0 = r22;
        r12.<init>(r0, r7, r3);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x03a9 }
        if (r3 <= 0) goto L_0x019e;
    L_0x0182:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r3 + -1;
        r3 = r15.remove(r3);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = (java.lang.String) r3;	 Catch:{ RuntimeException -> 0x03a9 }
    L_0x018e:
        r16 = java.lang.Long.parseLong(r12);	 Catch:{ RuntimeException -> 0x03a9 }
        r0 = r21;
        r1 = r16;
        r0.number(r3, r1);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r4;
        r4 = r5;
        r5 = r6;
        goto L_0x0097;
    L_0x019e:
        r3 = 0;
        goto L_0x018e;
    L_0x01a0:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x03a9 }
        if (r3 <= 0) goto L_0x01bd;
    L_0x01a6:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r3 + -1;
        r3 = r15.remove(r3);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = (java.lang.String) r3;	 Catch:{ RuntimeException -> 0x03a9 }
    L_0x01b2:
        r4 = 1;
        r0 = r21;
        r0.bool(r3, r4);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = 1;
        r4 = r5;
        r5 = r7;
        goto L_0x0097;
    L_0x01bd:
        r3 = 0;
        goto L_0x01b2;
    L_0x01bf:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x03a9 }
        if (r3 <= 0) goto L_0x01dc;
    L_0x01c5:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r3 + -1;
        r3 = r15.remove(r3);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = (java.lang.String) r3;	 Catch:{ RuntimeException -> 0x03a9 }
    L_0x01d1:
        r4 = 0;
        r0 = r21;
        r0.bool(r3, r4);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = 1;
        r4 = r5;
        r5 = r7;
        goto L_0x0097;
    L_0x01dc:
        r3 = 0;
        goto L_0x01d1;
    L_0x01de:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x03a9 }
        if (r3 <= 0) goto L_0x01fb;
    L_0x01e4:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r3 + -1;
        r3 = r15.remove(r3);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = (java.lang.String) r3;	 Catch:{ RuntimeException -> 0x03a9 }
    L_0x01f0:
        r4 = 0;
        r0 = r21;
        r0.string(r3, r4);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = 1;
        r4 = r5;
        r5 = r7;
        goto L_0x0097;
    L_0x01fb:
        r3 = 0;
        goto L_0x01f0;
    L_0x01fd:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x03a9 }
        if (r3 <= 0) goto L_0x0234;
    L_0x0203:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r3 + -1;
        r3 = r15.remove(r3);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = (java.lang.String) r3;	 Catch:{ RuntimeException -> 0x03a9 }
    L_0x020f:
        r0 = r21;
        r0.startObject(r3);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r10.length;	 Catch:{ RuntimeException -> 0x03a9 }
        if (r14 != r3) goto L_0x0223;
    L_0x0217:
        r3 = r10.length;	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r3 * 2;
        r3 = new int[r3];	 Catch:{ RuntimeException -> 0x03a9 }
        r11 = 0;
        r12 = 0;
        r13 = r10.length;	 Catch:{ RuntimeException -> 0x03a9 }
        java.lang.System.arraycopy(r10, r11, r3, r12, r13);	 Catch:{ RuntimeException -> 0x03a9 }
        r10 = r3;
    L_0x0223:
        r11 = r14 + 1;
        r10[r14] = r9;	 Catch:{ RuntimeException -> 0x03a9 }
        r9 = 8;
        r3 = 2;
        r14 = r11;
        r20 = r4;
        r4 = r5;
        r5 = r9;
        r9 = r3;
        r3 = r20;
        goto L_0x001b;
    L_0x0234:
        r3 = 0;
        goto L_0x020f;
    L_0x0236:
        r21.pop();	 Catch:{ RuntimeException -> 0x03a9 }
        r9 = r14 + -1;
        r11 = r10[r9];	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = 2;
        r14 = r9;
        r9 = r3;
        r3 = r4;
        r4 = r5;
        r5 = r11;
        goto L_0x001b;
    L_0x0245:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x03a9 }
        if (r3 <= 0) goto L_0x0278;
    L_0x024b:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r3 + -1;
        r3 = r15.remove(r3);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = (java.lang.String) r3;	 Catch:{ RuntimeException -> 0x03a9 }
    L_0x0257:
        r0 = r21;
        r0.startArray(r3);	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r10.length;	 Catch:{ RuntimeException -> 0x03a9 }
        if (r14 != r3) goto L_0x0426;
    L_0x025f:
        r3 = r10.length;	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = r3 * 2;
        r3 = new int[r3];	 Catch:{ RuntimeException -> 0x03a9 }
        r11 = 0;
        r12 = 0;
        r13 = r10.length;	 Catch:{ RuntimeException -> 0x03a9 }
        java.lang.System.arraycopy(r10, r11, r3, r12, r13);	 Catch:{ RuntimeException -> 0x03a9 }
    L_0x026a:
        r10 = r14 + 1;
        r3[r14] = r9;	 Catch:{ RuntimeException -> 0x03a9 }
        r11 = 46;
        r9 = 2;
        r14 = r10;
        r10 = r3;
        r3 = r4;
        r4 = r5;
        r5 = r11;
        goto L_0x001b;
    L_0x0278:
        r3 = 0;
        goto L_0x0257;
    L_0x027a:
        r21.pop();	 Catch:{ RuntimeException -> 0x03a9 }
        r9 = r14 + -1;
        r11 = r10[r9];	 Catch:{ RuntimeException -> 0x03a9 }
        r3 = 2;
        r14 = r9;
        r9 = r3;
        r3 = r4;
        r4 = r5;
        r5 = r11;
        goto L_0x001b;
    L_0x0289:
        r3 = r4;
        r4 = r5;
    L_0x028b:
        if (r9 != 0) goto L_0x0295;
    L_0x028d:
        r5 = 5;
        r20 = r5;
        r5 = r9;
        r9 = r20;
        goto L_0x001b;
    L_0x0295:
        r5 = r6 + 1;
        r0 = r24;
        if (r5 == r0) goto L_0x02a4;
    L_0x029b:
        r6 = 1;
        r20 = r6;
        r6 = r5;
        r5 = r9;
        r9 = r20;
        goto L_0x001b;
    L_0x02a4:
        r6 = r3;
        r11 = r4;
        r3 = r9;
    L_0x02a7:
        r0 = r24;
        if (r5 != r0) goto L_0x001f;
    L_0x02ab:
        r4 = _json_eof_actions;	 Catch:{ RuntimeException -> 0x0420 }
        r3 = r4[r3];	 Catch:{ RuntimeException -> 0x0420 }
        r9 = _json_actions;	 Catch:{ RuntimeException -> 0x0420 }
        r4 = r3 + 1;
        r3 = r9[r3];	 Catch:{ RuntimeException -> 0x0420 }
        r9 = r4;
        r4 = r6;
        r6 = r7;
    L_0x02b8:
        r7 = r3 + -1;
        if (r3 <= 0) goto L_0x001f;
    L_0x02bc:
        r3 = _json_actions;	 Catch:{ RuntimeException -> 0x0420 }
        r10 = r9 + 1;
        r3 = r3[r9];	 Catch:{ RuntimeException -> 0x0420 }
        switch(r3) {
            case 3: goto L_0x02cc;
            case 4: goto L_0x02fc;
            case 5: goto L_0x0325;
            case 6: goto L_0x034f;
            case 7: goto L_0x036d;
            case 8: goto L_0x038b;
            default: goto L_0x02c5;
        };	 Catch:{ RuntimeException -> 0x0420 }
    L_0x02c5:
        r3 = r4;
        r4 = r6;
    L_0x02c7:
        r9 = r10;
        r6 = r4;
        r4 = r3;
        r3 = r7;
        goto L_0x02b8;
    L_0x02cc:
        if (r4 != 0) goto L_0x02c5;
    L_0x02ce:
        r3 = new java.lang.String;	 Catch:{ RuntimeException -> 0x0420 }
        r9 = r5 - r6;
        r0 = r22;
        r3.<init>(r0, r6, r9);	 Catch:{ RuntimeException -> 0x0420 }
        if (r11 == 0) goto L_0x0423;
    L_0x02d9:
        r0 = r21;
        r3 = r0.unescape(r3);	 Catch:{ RuntimeException -> 0x0420 }
        r6 = r3;
    L_0x02e0:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x0420 }
        if (r3 <= 0) goto L_0x02fa;
    L_0x02e6:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x0420 }
        r3 = r3 + -1;
        r3 = r15.remove(r3);	 Catch:{ RuntimeException -> 0x0420 }
        r3 = (java.lang.String) r3;	 Catch:{ RuntimeException -> 0x0420 }
    L_0x02f2:
        r0 = r21;
        r0.string(r3, r6);	 Catch:{ RuntimeException -> 0x0420 }
        r3 = r4;
        r4 = r5;
        goto L_0x02c7;
    L_0x02fa:
        r3 = 0;
        goto L_0x02f2;
    L_0x02fc:
        r9 = new java.lang.String;	 Catch:{ RuntimeException -> 0x0420 }
        r3 = r5 - r6;
        r0 = r22;
        r9.<init>(r0, r6, r3);	 Catch:{ RuntimeException -> 0x0420 }
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x0420 }
        if (r3 <= 0) goto L_0x0323;
    L_0x030b:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x0420 }
        r3 = r3 + -1;
        r3 = r15.remove(r3);	 Catch:{ RuntimeException -> 0x0420 }
        r3 = (java.lang.String) r3;	 Catch:{ RuntimeException -> 0x0420 }
    L_0x0317:
        r12 = java.lang.Double.parseDouble(r9);	 Catch:{ RuntimeException -> 0x0420 }
        r0 = r21;
        r0.number(r3, r12);	 Catch:{ RuntimeException -> 0x0420 }
        r3 = r4;
        r4 = r5;
        goto L_0x02c7;
    L_0x0323:
        r3 = 0;
        goto L_0x0317;
    L_0x0325:
        r9 = new java.lang.String;	 Catch:{ RuntimeException -> 0x0420 }
        r3 = r5 - r6;
        r0 = r22;
        r9.<init>(r0, r6, r3);	 Catch:{ RuntimeException -> 0x0420 }
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x0420 }
        if (r3 <= 0) goto L_0x034d;
    L_0x0334:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x0420 }
        r3 = r3 + -1;
        r3 = r15.remove(r3);	 Catch:{ RuntimeException -> 0x0420 }
        r3 = (java.lang.String) r3;	 Catch:{ RuntimeException -> 0x0420 }
    L_0x0340:
        r12 = java.lang.Long.parseLong(r9);	 Catch:{ RuntimeException -> 0x0420 }
        r0 = r21;
        r0.number(r3, r12);	 Catch:{ RuntimeException -> 0x0420 }
        r3 = r4;
        r4 = r5;
        goto L_0x02c7;
    L_0x034d:
        r3 = 0;
        goto L_0x0340;
    L_0x034f:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x0420 }
        if (r3 <= 0) goto L_0x036b;
    L_0x0355:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x0420 }
        r3 = r3 + -1;
        r3 = r15.remove(r3);	 Catch:{ RuntimeException -> 0x0420 }
        r3 = (java.lang.String) r3;	 Catch:{ RuntimeException -> 0x0420 }
    L_0x0361:
        r4 = 1;
        r0 = r21;
        r0.bool(r3, r4);	 Catch:{ RuntimeException -> 0x0420 }
        r3 = 1;
        r4 = r6;
        goto L_0x02c7;
    L_0x036b:
        r3 = 0;
        goto L_0x0361;
    L_0x036d:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x0420 }
        if (r3 <= 0) goto L_0x0389;
    L_0x0373:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x0420 }
        r3 = r3 + -1;
        r3 = r15.remove(r3);	 Catch:{ RuntimeException -> 0x0420 }
        r3 = (java.lang.String) r3;	 Catch:{ RuntimeException -> 0x0420 }
    L_0x037f:
        r4 = 0;
        r0 = r21;
        r0.bool(r3, r4);	 Catch:{ RuntimeException -> 0x0420 }
        r3 = 1;
        r4 = r6;
        goto L_0x02c7;
    L_0x0389:
        r3 = 0;
        goto L_0x037f;
    L_0x038b:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x0420 }
        if (r3 <= 0) goto L_0x03a7;
    L_0x0391:
        r3 = r15.size();	 Catch:{ RuntimeException -> 0x0420 }
        r3 = r3 + -1;
        r3 = r15.remove(r3);	 Catch:{ RuntimeException -> 0x0420 }
        r3 = (java.lang.String) r3;	 Catch:{ RuntimeException -> 0x0420 }
    L_0x039d:
        r4 = 0;
        r0 = r21;
        r0.string(r3, r4);	 Catch:{ RuntimeException -> 0x0420 }
        r3 = 1;
        r4 = r6;
        goto L_0x02c7;
    L_0x03a7:
        r3 = 0;
        goto L_0x039d;
    L_0x03a9:
        r3 = move-exception;
    L_0x03aa:
        r5 = r3;
        goto L_0x0021;
    L_0x03ad:
        r4 = new com.esotericsoftware.jsonbeans.JsonException;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "Error parsing JSON on line ";
        r7 = r7.append(r8);
        r3 = r7.append(r3);
        r7 = " near: ";
        r3 = r3.append(r7);
        r7 = new java.lang.String;
        r8 = r24 - r6;
        r0 = r22;
        r7.<init>(r0, r6, r8);
        r3 = r3.append(r7);
        r3 = r3.toString();
        r4.m63init(r3, r5);
        throw r4;
    L_0x03d9:
        r0 = r21;
        r3 = r0.elements;
        r3 = r3.isEmpty();
        if (r3 != 0) goto L_0x0416;
    L_0x03e3:
        r0 = r21;
        r3 = r0.elements;
        r0 = r21;
        r4 = r0.elements;
        r4 = r4.size();
        r4 = r4 + -1;
        r3 = r3.get(r4);
        r3 = (com.esotericsoftware.jsonbeans.JsonValue) r3;
        r0 = r21;
        r4 = r0.elements;
        r4.clear();
        if (r3 == 0) goto L_0x040e;
    L_0x0400:
        r3 = r3.isObject();
        if (r3 == 0) goto L_0x040e;
    L_0x0406:
        r3 = new com.esotericsoftware.jsonbeans.JsonException;
        r4 = "Error parsing JSON, unmatched brace.";
        r3.m62init(r4);
        throw r3;
    L_0x040e:
        r3 = new com.esotericsoftware.jsonbeans.JsonException;
        r4 = "Error parsing JSON, unmatched bracket.";
        r3.m62init(r4);
        throw r3;
    L_0x0416:
        r0 = r21;
        r3 = r0.root;
        r4 = 0;
        r0 = r21;
        r0.root = r4;
        return r3;
    L_0x0420:
        r3 = move-exception;
        r6 = r5;
        goto L_0x03aa;
    L_0x0423:
        r6 = r3;
        goto L_0x02e0;
    L_0x0426:
        r3 = r10;
        goto L_0x026a;
    L_0x0429:
        r7 = r3;
        goto L_0x0128;
    L_0x042c:
        r5 = r9;
        goto L_0x006c;
    L_0x042f:
        r11 = r4;
        r20 = r3;
        r3 = r5;
        r5 = r6;
        r6 = r20;
        goto L_0x02a7;
    L_0x0438:
        r9 = r5;
        goto L_0x028b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.esotericsoftware.jsonbeans.JsonReader.parse(char[], int, int):com.esotericsoftware.jsonbeans.JsonValue");
    }

    private static byte[] init__json_actions_0() {
        return new byte[]{(byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 2, (byte) 1, (byte) 3, (byte) 1, (byte) 4, (byte) 1, (byte) 5, (byte) 1, (byte) 9, (byte) 1, (byte) 10, (byte) 1, ByteCode.T_LONG, (byte) 1, (byte) 12, (byte) 2, (byte) 0, (byte) 2, (byte) 2, (byte) 0, (byte) 3, (byte) 2, (byte) 3, (byte) 10, (byte) 2, (byte) 3, (byte) 12, (byte) 2, (byte) 4, (byte) 10, (byte) 2, (byte) 4, (byte) 12, (byte) 2, (byte) 5, (byte) 10, (byte) 2, (byte) 5, (byte) 12, (byte) 2, (byte) 6, (byte) 3, (byte) 2, (byte) 7, (byte) 3, (byte) 2, (byte) 8, (byte) 3, (byte) 3, (byte) 6, (byte) 3, (byte) 10, (byte) 3, (byte) 6, (byte) 3, (byte) 12, (byte) 3, (byte) 7, (byte) 3, (byte) 10, (byte) 3, (byte) 7, (byte) 3, (byte) 12, (byte) 3, (byte) 8, (byte) 3, (byte) 10, (byte) 3, (byte) 8, (byte) 3, (byte) 12};
    }

    private static short[] init__json_key_offsets_0() {
        return new short[]{(short) 0, (short) 0, (short) 18, (short) 20, (short) 22, (short) 31, (short) 33, (short) 37, (short) 39, (short) 54, (short) 56, (short) 58, (short) 62, (short) 80, (short) 82, (short) 84, (short) 89, (short) 103, (short) 110, (short) 112, (short) 115, (short) 123, (short) 127, (short) 129, (short) 135, (short) 144, (short) 151, (short) 153, (short) 161, (short) 170, (short) 174, (short) 176, (short) 183, (short) 191, (short) 199, (short) 207, (short) 215, (short) 222, (short) 230, (short) 238, (short) 246, (short) 253, (short) 261, (short) 269, (short) 277, (short) 284, (short) 293, (short) 313, (short) 315, (short) 317, (short) 322, (short) 341, (short) 348, (short) 350, (short) 358, (short) 367, (short) 371, (short) 373, (short) 380, (short) 388, (short) 396, (short) 404, (short) 412, (short) 419, (short) 427, (short) 435, (short) 443, (short) 450, (short) 458, (short) 466, (short) 474, (short) 481, (short) 490, (short) 493, (short) 500, (short) 506, (short) 513, (short) 518, (short) 526, (short) 534, (short) 542, (short) 550, (short) 557, (short) 565, (short) 573, (short) 581, (short) 588, (short) 596, (short) 604, (short) 612, (short) 619, (short) 619};
    }

    private static char[] init__json_trans_keys_0() {
        return new char[]{' ', '\"', '$', SignatureVisitor.SUPER, '[', '_', 'f', 'n', 't', '{', 9, 13, '0', '9', 'A', 'Z', 'a', 'z', '\"', '\\', '\"', '\\', '\"', '/', '\\', 'b', 'f', 'n', 'r', 't', 'u', '0', '9', SignatureVisitor.EXTENDS, SignatureVisitor.SUPER, '0', '9', '0', '9', ' ', '\"', '$', ',', SignatureVisitor.SUPER, '_', '}', 9, 13, '0', '9', 'A', 'Z', 'a', 'z', '\"', '\\', '\"', '\\', ' ', ':', 9, 13, ' ', '\"', '$', SignatureVisitor.SUPER, '[', '_', 'f', 'n', 't', '{', 9, 13, '0', '9', 'A', 'Z', 'a', 'z', '\"', '\\', '\"', '\\', ' ', ',', '}', 9, 13, ' ', '\"', '$', SignatureVisitor.SUPER, '_', '}', 9, 13, '0', '9', 'A', 'Z', 'a', 'z', ' ', ',', ':', ']', '}', 9, 13, '0', '9', '.', '0', '9', ' ', ':', 'E', 'e', 9, 13, '0', '9', SignatureVisitor.EXTENDS, SignatureVisitor.SUPER, '0', '9', '0', '9', ' ', ':', 9, 13, '0', '9', '\"', '/', '\\', 'b', 'f', 'n', 'r', 't', 'u', ' ', ',', ':', ']', '}', 9, 13, '0', '9', ' ', ',', '.', '}', 9, 13, '0', '9', ' ', ',', 'E', 'e', '}', 9, 13, '0', '9', SignatureVisitor.EXTENDS, SignatureVisitor.SUPER, '0', '9', '0', '9', ' ', ',', '}', 9, 13, '0', '9', ' ', ',', ':', ']', 'a', '}', 9, 13, ' ', ',', ':', ']', 'l', '}', 9, 13, ' ', ',', ':', ']', 's', '}', 9, 13, ' ', ',', ':', ']', 'e', '}', 9, 13, ' ', ',', ':', ']', '}', 9, 13, ' ', ',', ':', ']', 'u', '}', 9, 13, ' ', ',', ':', ']', 'l', '}', 9, 13, ' ', ',', ':', ']', 'l', '}', 9, 13, ' ', ',', ':', ']', '}', 9, 13, ' ', ',', ':', ']', 'r', '}', 9, 13, ' ', ',', ':', ']', 'u', '}', 9, 13, ' ', ',', ':', ']', 'e', '}', 9, 13, ' ', ',', ':', ']', '}', 9, 13, '\"', '/', '\\', 'b', 'f', 'n', 'r', 't', 'u', ' ', '\"', '$', ',', SignatureVisitor.SUPER, '[', ']', '_', 'f', 'n', 't', '{', 9, 13, '0', '9', 'A', 'Z', 'a', 'z', '\"', '\\', '\"', '\\', ' ', ',', ']', 9, 13, ' ', '\"', '$', SignatureVisitor.SUPER, '[', ']', '_', 'f', 'n', 't', '{', 9, 13, '0', '9', 'A', 'Z', 'a', 'z', ' ', ',', ':', ']', '}', 9, 13, '0', '9', ' ', ',', '.', ']', 9, 13, '0', '9', ' ', ',', 'E', ']', 'e', 9, 13, '0', '9', SignatureVisitor.EXTENDS, SignatureVisitor.SUPER, '0', '9', '0', '9', ' ', ',', ']', 9, 13, '0', '9', ' ', ',', ':', ']', 'a', '}', 9, 13, ' ', ',', ':', ']', 'l', '}', 9, 13, ' ', ',', ':', ']', 's', '}', 9, 13, ' ', ',', ':', ']', 'e', '}', 9, 13, ' ', ',', ':', ']', '}', 9, 13, ' ', ',', ':', ']', 'u', '}', 9, 13, ' ', ',', ':', ']', 'l', '}', 9, 13, ' ', ',', ':', ']', 'l', '}', 9, 13, ' ', ',', ':', ']', '}', 9, 13, ' ', ',', ':', ']', 'r', '}', 9, 13, ' ', ',', ':', ']', 'u', '}', 9, 13, ' ', ',', ':', ']', 'e', '}', 9, 13, ' ', ',', ':', ']', '}', 9, 13, '\"', '/', '\\', 'b', 'f', 'n', 'r', 't', 'u', ' ', 9, 13, ' ', ',', ':', ']', '}', 9, 13, ' ', '.', 9, 13, '0', '9', ' ', 'E', 'e', 9, 13, '0', '9', ' ', 9, 13, '0', '9', ' ', ',', ':', ']', 'a', '}', 9, 13, ' ', ',', ':', ']', 'l', '}', 9, 13, ' ', ',', ':', ']', 's', '}', 9, 13, ' ', ',', ':', ']', 'e', '}', 9, 13, ' ', ',', ':', ']', '}', 9, 13, ' ', ',', ':', ']', 'u', '}', 9, 13, ' ', ',', ':', ']', 'l', '}', 9, 13, ' ', ',', ':', ']', 'l', '}', 9, 13, ' ', ',', ':', ']', '}', 9, 13, ' ', ',', ':', ']', 'r', '}', 9, 13, ' ', ',', ':', ']', 'u', '}', 9, 13, ' ', ',', ':', ']', 'e', '}', 9, 13, ' ', ',', ':', ']', '}', 9, 13, 0};
    }

    private static byte[] init__json_single_lengths_0() {
        return new byte[]{(byte) 0, (byte) 10, (byte) 2, (byte) 2, (byte) 7, (byte) 0, (byte) 2, (byte) 0, (byte) 7, (byte) 2, (byte) 2, (byte) 2, (byte) 10, (byte) 2, (byte) 2, (byte) 3, (byte) 6, (byte) 5, (byte) 0, (byte) 1, (byte) 4, (byte) 2, (byte) 0, (byte) 2, (byte) 7, (byte) 5, (byte) 0, (byte) 4, (byte) 5, (byte) 2, (byte) 0, (byte) 3, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 7, (byte) 12, (byte) 2, (byte) 2, (byte) 3, ByteCode.T_LONG, (byte) 5, (byte) 0, (byte) 4, (byte) 5, (byte) 2, (byte) 0, (byte) 3, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 7, (byte) 1, (byte) 5, (byte) 2, (byte) 3, (byte) 1, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 0, (byte) 0};
    }

    private static byte[] init__json_range_lengths_0() {
        return new byte[]{(byte) 0, (byte) 4, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 4, (byte) 0, (byte) 0, (byte) 1, (byte) 4, (byte) 0, (byte) 0, (byte) 1, (byte) 4, (byte) 1, (byte) 1, (byte) 1, (byte) 2, (byte) 1, (byte) 1, (byte) 2, (byte) 1, (byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 2, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 4, (byte) 0, (byte) 0, (byte) 1, (byte) 4, (byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 2, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 0};
    }

    private static short[] init__json_index_offsets_0() {
        return new short[]{(short) 0, (short) 0, (short) 15, (short) 18, (short) 21, (short) 30, (short) 32, (short) 36, (short) 38, (short) 50, (short) 53, (short) 56, (short) 60, (short) 75, (short) 78, (short) 81, (short) 86, (short) 97, (short) 104, (short) 106, (short) 109, (short) 116, (short) 120, (short) 122, (short) 127, (short) 136, (short) 143, (short) 145, (short) 152, (short) 160, (short) 164, (short) 166, (short) 172, (short) 180, (short) 188, (short) 196, (short) 204, (short) 211, (short) 219, (short) 227, (short) 235, (short) 242, (short) 250, (short) 258, (short) 266, (short) 273, (short) 282, (short) 299, (short) 302, (short) 305, (short) 310, (short) 326, (short) 333, (short) 335, (short) 342, (short) 350, (short) 354, (short) 356, (short) 362, (short) 370, (short) 378, (short) 386, (short) 394, (short) 401, (short) 409, (short) 417, (short) 425, (short) 432, (short) 440, (short) 448, (short) 456, (short) 463, (short) 472, (short) 475, (short) 482, (short) 487, (short) 493, (short) 497, (short) 505, (short) 513, (short) 521, (short) 529, (short) 536, (short) 544, (short) 552, (short) 560, (short) 567, (short) 575, (short) 583, (short) 591, (short) 598, (short) 599};
    }

    private static byte[] init__json_trans_targs_0() {
        return new byte[]{(byte) 1, (byte) 2, (byte) 73, (byte) 5, (byte) 72, (byte) 73, (byte) 77, (byte) 82, (byte) 86, (byte) 72, (byte) 1, (byte) 74, (byte) 73, (byte) 73, (byte) 0, (byte) 72, (byte) 4, (byte) 3, (byte) 72, (byte) 4, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 0, (byte) 74, (byte) 0, (byte) 7, (byte) 7, (byte) 76, (byte) 0, (byte) 76, (byte) 0, (byte) 8, (byte) 9, (byte) 17, (byte) 16, (byte) 18, (byte) 17, (byte) 90, (byte) 8, (byte) 17, (byte) 17, (byte) 17, (byte) 0, ByteCode.T_LONG, (byte) 45, (byte) 10, ByteCode.T_LONG, (byte) 45, (byte) 10, ByteCode.T_LONG, (byte) 12, ByteCode.T_LONG, (byte) 0, (byte) 12, Draft_75.CR, (byte) 25, (byte) 26, (byte) 15, (byte) 25, (byte) 32, (byte) 37, (byte) 41, (byte) 15, (byte) 12, (byte) 27, (byte) 25, (byte) 25, (byte) 0, (byte) 15, (byte) 24, (byte) 14, (byte) 15, (byte) 24, (byte) 14, (byte) 15, (byte) 16, (byte) 90, (byte) 15, (byte) 0, (byte) 16, (byte) 9, (byte) 17, (byte) 18, (byte) 17, (byte) 90, (byte) 16, (byte) 17, (byte) 17, (byte) 17, (byte) 0, ByteCode.T_LONG, (byte) 0, (byte) 12, (byte) 0, (byte) 0, ByteCode.T_LONG, (byte) 17, (byte) 19, (byte) 0, (byte) 20, (byte) 19, (byte) 0, ByteCode.T_LONG, (byte) 12, (byte) 21, (byte) 21, ByteCode.T_LONG, (byte) 20, (byte) 0, (byte) 22, (byte) 22, (byte) 23, (byte) 0, (byte) 23, (byte) 0, ByteCode.T_LONG, (byte) 12, ByteCode.T_LONG, (byte) 23, (byte) 0, (byte) 14, (byte) 14, (byte) 14, (byte) 14, (byte) 14, (byte) 14, (byte) 14, (byte) 14, (byte) 0, (byte) 15, (byte) 16, (byte) 0, (byte) 0, (byte) 90, (byte) 15, (byte) 25, (byte) 27, (byte) 0, (byte) 15, (byte) 16, (byte) 28, (byte) 90, (byte) 15, (byte) 27, (byte) 0, (byte) 15, (byte) 16, (byte) 29, (byte) 29, (byte) 90, (byte) 15, (byte) 28, (byte) 0, (byte) 30, (byte) 30, (byte) 31, (byte) 0, (byte) 31, (byte) 0, (byte) 15, (byte) 16, (byte) 90, (byte) 15, (byte) 31, (byte) 0, (byte) 15, (byte) 16, (byte) 0, (byte) 0, (byte) 33, (byte) 90, (byte) 15, (byte) 25, (byte) 15, (byte) 16, (byte) 0, (byte) 0, (byte) 34, (byte) 90, (byte) 15, (byte) 25, (byte) 15, (byte) 16, (byte) 0, (byte) 0, (byte) 35, (byte) 90, (byte) 15, (byte) 25, (byte) 15, (byte) 16, (byte) 0, (byte) 0, (byte) 36, (byte) 90, (byte) 15, (byte) 25, (byte) 15, (byte) 16, (byte) 0, (byte) 0, (byte) 90, (byte) 15, (byte) 25, (byte) 15, (byte) 16, (byte) 0, (byte) 0, (byte) 38, (byte) 90, (byte) 15, (byte) 25, (byte) 15, (byte) 16, (byte) 0, (byte) 0, (byte) 39, (byte) 90, (byte) 15, (byte) 25, (byte) 15, (byte) 16, (byte) 0, (byte) 0, (byte) 40, (byte) 90, (byte) 15, (byte) 25, (byte) 15, (byte) 16, (byte) 0, (byte) 0, (byte) 90, (byte) 15, (byte) 25, (byte) 15, (byte) 16, (byte) 0, (byte) 0, (byte) 42, (byte) 90, (byte) 15, (byte) 25, (byte) 15, (byte) 16, (byte) 0, (byte) 0, (byte) 43, (byte) 90, (byte) 15, (byte) 25, (byte) 15, (byte) 16, (byte) 0, (byte) 0, (byte) 44, (byte) 90, (byte) 15, (byte) 25, (byte) 15, (byte) 16, (byte) 0, (byte) 0, (byte) 90, (byte) 15, (byte) 25, (byte) 10, (byte) 10, (byte) 10, (byte) 10, (byte) 10, (byte) 10, (byte) 10, (byte) 10, (byte) 0, (byte) 46, (byte) 47, (byte) 51, (byte) 50, (byte) 52, (byte) 49, (byte) 91, (byte) 51, (byte) 58, (byte) 63, (byte) 67, (byte) 49, (byte) 46, (byte) 53, (byte) 51, (byte) 51, (byte) 0, (byte) 49, (byte) 71, (byte) 48, (byte) 49, (byte) 71, (byte) 48, (byte) 49, (byte) 50, (byte) 91, (byte) 49, (byte) 0, (byte) 50, (byte) 47, (byte) 51, (byte) 52, (byte) 49, (byte) 91, (byte) 51, (byte) 58, (byte) 63, (byte) 67, (byte) 49, (byte) 50, (byte) 53, (byte) 51, (byte) 51, (byte) 0, (byte) 49, (byte) 50, (byte) 0, (byte) 91, (byte) 0, (byte) 49, (byte) 51, (byte) 53, (byte) 0, (byte) 49, (byte) 50, (byte) 54, (byte) 91, (byte) 49, (byte) 53, (byte) 0, (byte) 49, (byte) 50, (byte) 55, (byte) 91, (byte) 55, (byte) 49, (byte) 54, (byte) 0, (byte) 56, (byte) 56, (byte) 57, (byte) 0, (byte) 57, (byte) 0, (byte) 49, (byte) 50, (byte) 91, (byte) 49, (byte) 57, (byte) 0, (byte) 49, (byte) 50, (byte) 0, (byte) 91, (byte) 59, (byte) 0, (byte) 49, (byte) 51, (byte) 49, (byte) 50, (byte) 0, (byte) 91, (byte) 60, (byte) 0, (byte) 49, (byte) 51, (byte) 49, (byte) 50, (byte) 0, (byte) 91, (byte) 61, (byte) 0, (byte) 49, (byte) 51, (byte) 49, (byte) 50, (byte) 0, (byte) 91, (byte) 62, (byte) 0, (byte) 49, (byte) 51, (byte) 49, (byte) 50, (byte) 0, (byte) 91, (byte) 0, (byte) 49, (byte) 51, (byte) 49, (byte) 50, (byte) 0, (byte) 91, (byte) 64, (byte) 0, (byte) 49, (byte) 51, (byte) 49, (byte) 50, (byte) 0, (byte) 91, (byte) 65, (byte) 0, (byte) 49, (byte) 51, (byte) 49, (byte) 50, (byte) 0, (byte) 91, (byte) 66, (byte) 0, (byte) 49, (byte) 51, (byte) 49, (byte) 50, (byte) 0, (byte) 91, (byte) 0, (byte) 49, (byte) 51, (byte) 49, (byte) 50, (byte) 0, (byte) 91, (byte) 68, (byte) 0, (byte) 49, (byte) 51, (byte) 49, (byte) 50, (byte) 0, (byte) 91, (byte) 69, (byte) 0, (byte) 49, (byte) 51, (byte) 49, (byte) 50, (byte) 0, (byte) 91, (byte) 70, (byte) 0, (byte) 49, (byte) 51, (byte) 49, (byte) 50, (byte) 0, (byte) 91, (byte) 0, (byte) 49, (byte) 51, (byte) 48, (byte) 48, (byte) 48, (byte) 48, (byte) 48, (byte) 48, (byte) 48, (byte) 48, (byte) 0, (byte) 72, (byte) 72, (byte) 0, (byte) 72, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 72, (byte) 73, (byte) 72, (byte) 75, (byte) 72, (byte) 74, (byte) 0, (byte) 72, (byte) 6, (byte) 6, (byte) 72, (byte) 75, (byte) 0, (byte) 72, (byte) 72, (byte) 76, (byte) 0, (byte) 72, (byte) 0, (byte) 0, (byte) 0, (byte) 78, (byte) 0, (byte) 72, (byte) 73, (byte) 72, (byte) 0, (byte) 0, (byte) 0, (byte) 79, (byte) 0, (byte) 72, (byte) 73, (byte) 72, (byte) 0, (byte) 0, (byte) 0, (byte) 80, (byte) 0, (byte) 72, (byte) 73, (byte) 72, (byte) 0, (byte) 0, (byte) 0, (byte) 81, (byte) 0, (byte) 72, (byte) 73, (byte) 72, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 72, (byte) 73, (byte) 72, (byte) 0, (byte) 0, (byte) 0, (byte) 83, (byte) 0, (byte) 72, (byte) 73, (byte) 72, (byte) 0, (byte) 0, (byte) 0, (byte) 84, (byte) 0, (byte) 72, (byte) 73, (byte) 72, (byte) 0, (byte) 0, (byte) 0, (byte) 85, (byte) 0, (byte) 72, (byte) 73, (byte) 72, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 72, (byte) 73, (byte) 72, (byte) 0, (byte) 0, (byte) 0, (byte) 87, (byte) 0, (byte) 72, (byte) 73, (byte) 72, (byte) 0, (byte) 0, (byte) 0, (byte) 88, (byte) 0, (byte) 72, (byte) 73, (byte) 72, (byte) 0, (byte) 0, (byte) 0, (byte) 89, (byte) 0, (byte) 72, (byte) 73, (byte) 72, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 72, (byte) 73, (byte) 0, (byte) 0, (byte) 0};
    }

    private static byte[] init__json_trans_actions_0() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 17, (byte) 1, (byte) 1, (byte) 1, (byte) 1, Draft_75.CR, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 24, (byte) 1, (byte) 1, (byte) 7, (byte) 0, (byte) 0, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 1, (byte) 15, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 21, (byte) 1, (byte) 1, (byte) 5, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 17, (byte) 1, (byte) 1, (byte) 1, (byte) 1, Draft_75.CR, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 24, (byte) 1, (byte) 1, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 15, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 15, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 5, (byte) 0, (byte) 5, (byte) 0, (byte) 0, (byte) 5, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 5, (byte) 5, (byte) 0, (byte) 0, (byte) 5, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 5, (byte) 5, (byte) 5, (byte) 0, (byte) 0, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 0, (byte) 27, (byte) 7, (byte) 0, (byte) 0, (byte) 0, ByteCode.T_LONG, ByteCode.T_LONG, (byte) 0, (byte) 39, ByteCode.T_LONG, (byte) 0, (byte) 0, (byte) 9, (byte) 9, (byte) 0, (byte) 0, (byte) 33, (byte) 9, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 9, (byte) 9, (byte) 33, (byte) 9, (byte) 0, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 27, (byte) 7, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 27, (byte) 7, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 27, (byte) 7, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 27, (byte) 7, (byte) 0, (byte) 48, (byte) 48, (byte) 0, (byte) 0, (byte) 62, (byte) 48, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 27, (byte) 7, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 27, (byte) 7, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 27, (byte) 7, (byte) 0, (byte) 51, (byte) 51, (byte) 0, (byte) 0, (byte) 70, (byte) 51, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 27, (byte) 7, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 27, (byte) 7, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 27, (byte) 7, (byte) 0, (byte) 45, (byte) 45, (byte) 0, (byte) 0, (byte) 54, (byte) 45, (byte) 0, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 17, (byte) 19, (byte) 1, (byte) 1, (byte) 1, (byte) 1, Draft_75.CR, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 24, (byte) 1, (byte) 1, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 19, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 17, (byte) 19, (byte) 1, (byte) 1, (byte) 1, (byte) 1, Draft_75.CR, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 30, (byte) 0, (byte) 7, (byte) 0, (byte) 0, (byte) 0, ByteCode.T_LONG, ByteCode.T_LONG, (byte) 0, (byte) 42, ByteCode.T_LONG, (byte) 0, (byte) 0, (byte) 9, (byte) 9, (byte) 0, (byte) 36, (byte) 0, (byte) 9, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 9, (byte) 9, (byte) 36, (byte) 9, (byte) 0, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 30, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 30, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 30, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 30, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 48, (byte) 48, (byte) 0, (byte) 66, (byte) 0, (byte) 48, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 30, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 30, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 30, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 51, (byte) 51, (byte) 0, (byte) 74, (byte) 0, (byte) 51, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 30, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 30, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 30, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 45, (byte) 45, (byte) 0, (byte) 58, (byte) 0, (byte) 45, (byte) 0, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 7, (byte) 0, ByteCode.T_LONG, (byte) 0, ByteCode.T_LONG, (byte) 0, (byte) 0, (byte) 9, (byte) 0, (byte) 0, (byte) 9, (byte) 0, (byte) 0, (byte) 9, (byte) 9, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 48, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 48, (byte) 0, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 51, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 51, (byte) 0, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 7, (byte) 0, (byte) 45, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 45, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
    }

    private static byte[] init__json_eof_actions_0() {
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 7, ByteCode.T_LONG, (byte) 9, (byte) 9, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 48, (byte) 7, (byte) 7, (byte) 7, (byte) 51, (byte) 7, (byte) 7, (byte) 7, (byte) 45, (byte) 0, (byte) 0};
    }

    private void addChild(String str, JsonValue jsonValue) {
        jsonValue.setName(str);
        if (this.current.isArray() || this.current.isObject()) {
            this.current.addChild(jsonValue);
        } else {
            this.root = this.current;
        }
    }

    /* access modifiers changed from: protected */
    public void startObject(String str) {
        JsonValue jsonValue = new JsonValue(ValueType.object);
        if (this.current != null) {
            addChild(str, jsonValue);
        }
        this.elements.add(jsonValue);
        this.current = jsonValue;
    }

    /* access modifiers changed from: protected */
    public void startArray(String str) {
        JsonValue jsonValue = new JsonValue(ValueType.array);
        if (this.current != null) {
            addChild(str, jsonValue);
        }
        this.elements.add(jsonValue);
        this.current = jsonValue;
    }

    /* access modifiers changed from: protected */
    public void pop() {
        this.root = (JsonValue) this.elements.remove(this.elements.size() - 1);
        this.current = !this.elements.isEmpty() ? (JsonValue) this.elements.get(this.elements.size() - 1) : null;
    }

    /* access modifiers changed from: protected */
    public void string(String str, String str2) {
        addChild(str, new JsonValue(str2));
    }

    /* access modifiers changed from: protected */
    public void number(String str, double d) {
        addChild(str, new JsonValue(d));
    }

    /* access modifiers changed from: protected */
    public void number(String str, long j) {
        addChild(str, new JsonValue(j));
    }

    /* access modifiers changed from: protected */
    public void bool(String str, boolean z) {
        addChild(str, new JsonValue(z));
    }

    private String unescape(String str) {
        int length = str.length();
        StringBuilder stringBuilder = new StringBuilder(length + 16);
        int i = 0;
        while (i < length) {
            int i2 = i + 1;
            char charAt = str.charAt(i);
            if (charAt != '\\') {
                stringBuilder.append(charAt);
                i = i2;
            } else if (i2 == length) {
                return stringBuilder.toString();
            } else {
                int i3 = i2 + 1;
                charAt = str.charAt(i2);
                if (charAt == 'u') {
                    stringBuilder.append(Character.toChars(Integer.parseInt(str.substring(i3, i3 + 4), 16)));
                    i = i3 + 4;
                } else {
                    switch (charAt) {
                        case '\"':
                        case '/':
                        case '\\':
                            break;
                        case 'b':
                            charAt = 8;
                            break;
                        case 'f':
                            charAt = 12;
                            break;
                        case 'n':
                            charAt = 10;
                            break;
                        case 'r':
                            charAt = 13;
                            break;
                        case 't':
                            charAt = 9;
                            break;
                        default:
                            throw new JsonException("Illegal escaped character: \\" + charAt);
                    }
                    stringBuilder.append(charAt);
                    i = i3;
                }
            }
        }
        return stringBuilder.toString();
    }
}
