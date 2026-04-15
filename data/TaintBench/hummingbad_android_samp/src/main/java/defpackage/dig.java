package defpackage;

import java.io.IOException;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

/* renamed from: dig */
public class dig {
    static int dclass = 1;
    static Name name = null;
    static int type = 1;

    static void usage() {
        System.out.println("Usage: dig [@server] name [<type>] [<class>] [options]");
        System.exit(0);
    }

    static void doQuery(Message response, long ms) throws IOException {
        System.out.println("; java dig 0.0");
        System.out.println(response);
        System.out.println(new StringBuffer().append(";; Query time: ").append(ms).append(" ms").toString());
    }

    static void doAXFR(Message response) throws IOException {
        System.out.println(new StringBuffer().append("; java dig 0.0 <> ").append(name).append(" axfr").toString());
        if (response.isSigned()) {
            System.out.print(";; TSIG ");
            if (response.isVerified()) {
                System.out.println("ok");
            } else {
                System.out.println("failed");
            }
        }
        if (response.getRcode() != 0) {
            System.out.println(response);
            return;
        }
        Record[] records = response.getSectionArray(1);
        for (Object println : records) {
            System.out.println(println);
        }
        System.out.print(";; done (");
        System.out.print(response.getHeader().getCount(1));
        System.out.print(" records, ");
        System.out.print(response.getHeader().getCount(3));
        System.out.println(" additional)");
    }

    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00df  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x021a  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0102  */
    public static void main(java.lang.String[] r31) throws java.io.IOException {
        /*
        r24 = 0;
        r21 = 0;
        r18 = 0;
        r0 = r31;
        r0 = r0.length;
        r27 = r0;
        r28 = 1;
        r0 = r27;
        r1 = r28;
        if (r0 >= r1) goto L_0x0016;
    L_0x0013:
        defpackage.dig.usage();
    L_0x0016:
        r7 = 0;
        r27 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = "@";
        r27 = r27.startsWith(r28);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        if (r27 == 0) goto L_0x0229;
    L_0x0021:
        r8 = r7 + 1;
        r27 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0225 }
        r28 = 1;
        r24 = r27.substring(r28);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0225 }
    L_0x002b:
        if (r24 == 0) goto L_0x008d;
    L_0x002d:
        r22 = new org.xbill.DNS.SimpleResolver;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0225 }
        r0 = r22;
        r1 = r24;
        r0.m4168init(r1);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0225 }
        r21 = r22;
    L_0x0038:
        r7 = r8 + 1;
        r15 = r31[r8];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r27 = "-x";
        r0 = r27;
        r27 = r15.equals(r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        if (r27 == 0) goto L_0x0095;
    L_0x0046:
        r8 = r7 + 1;
        r27 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0225 }
        r27 = org.xbill.DNS.ReverseMap.fromAddress(r27);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0225 }
        name = r27;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0225 }
        r27 = 12;
        type = r27;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0225 }
        r27 = 1;
        dclass = r27;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0225 }
        r7 = r8;
    L_0x0059:
        r27 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = "-";
        r27 = r27.startsWith(r28);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        if (r27 == 0) goto L_0x00c8;
    L_0x0063:
        r27 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r27 = r27.length();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = 1;
        r0 = r27;
        r1 = r28;
        if (r0 <= r1) goto L_0x00c8;
    L_0x0071:
        r27 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = 1;
        r27 = r27.charAt(r28);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        switch(r27) {
            case 98: goto L_0x0147;
            case 99: goto L_0x007c;
            case 100: goto L_0x01fe;
            case 101: goto L_0x01b4;
            case 102: goto L_0x007c;
            case 103: goto L_0x007c;
            case 104: goto L_0x007c;
            case 105: goto L_0x01a9;
            case 106: goto L_0x007c;
            case 107: goto L_0x0176;
            case 108: goto L_0x007c;
            case 109: goto L_0x007c;
            case 110: goto L_0x007c;
            case 111: goto L_0x007c;
            case 112: goto L_0x010d;
            case 113: goto L_0x0216;
            case 114: goto L_0x007c;
            case 115: goto L_0x007c;
            case 116: goto L_0x019e;
            default: goto L_0x007c;
        };	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
    L_0x007c:
        r27 = java.lang.System.out;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = "Invalid option: ";
        r27.print(r28);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r27 = java.lang.System.out;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r27.println(r28);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
    L_0x008a:
        r7 = r7 + 1;
        goto L_0x0059;
    L_0x008d:
        r22 = new org.xbill.DNS.SimpleResolver;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0225 }
        r22.m4167init();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0225 }
        r21 = r22;
        goto L_0x0038;
    L_0x0095:
        r27 = org.xbill.DNS.Name.root;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r0 = r27;
        r27 = org.xbill.DNS.Name.fromString(r15, r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        name = r27;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r27 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r27 = org.xbill.DNS.Type.value(r27);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        type = r27;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r27 = type;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        if (r27 >= 0) goto L_0x0106;
    L_0x00ab:
        r27 = 1;
        type = r27;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
    L_0x00af:
        r27 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r27 = org.xbill.DNS.DClass.value(r27);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        dclass = r27;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r27 = dclass;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        if (r27 >= 0) goto L_0x0109;
    L_0x00bb:
        r27 = 1;
        dclass = r27;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        goto L_0x0059;
    L_0x00c0:
        r9 = move-exception;
    L_0x00c1:
        r27 = name;
        if (r27 != 0) goto L_0x00c8;
    L_0x00c5:
        defpackage.dig.usage();
    L_0x00c8:
        if (r21 != 0) goto L_0x00cf;
    L_0x00ca:
        r21 = new org.xbill.DNS.SimpleResolver;
        r21.m4167init();
    L_0x00cf:
        r27 = name;
        r28 = type;
        r29 = dclass;
        r20 = org.xbill.DNS.Record.newRecord(r27, r28, r29);
        r19 = org.xbill.DNS.Message.newQuery(r20);
        if (r18 == 0) goto L_0x00e8;
    L_0x00df:
        r27 = java.lang.System.out;
        r0 = r27;
        r1 = r19;
        r0.println(r1);
    L_0x00e8:
        r25 = java.lang.System.currentTimeMillis();
        r0 = r21;
        r1 = r19;
        r23 = r0.send(r1);
        r12 = java.lang.System.currentTimeMillis();
        r27 = type;
        r28 = 252; // 0xfc float:3.53E-43 double:1.245E-321;
        r0 = r27;
        r1 = r28;
        if (r0 != r1) goto L_0x021a;
    L_0x0102:
        defpackage.dig.doAXFR(r23);
    L_0x0105:
        return;
    L_0x0106:
        r7 = r7 + 1;
        goto L_0x00af;
    L_0x0109:
        r7 = r7 + 1;
        goto L_0x0059;
    L_0x010d:
        r27 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r27 = r27.length();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = 2;
        r0 = r27;
        r1 = r28;
        if (r0 <= r1) goto L_0x0139;
    L_0x011b:
        r27 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = 2;
        r17 = r27.substring(r28);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
    L_0x0123:
        r16 = java.lang.Integer.parseInt(r17);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        if (r16 < 0) goto L_0x0131;
    L_0x0129:
        r27 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r0 = r16;
        r1 = r27;
        if (r0 <= r1) goto L_0x013e;
    L_0x0131:
        r27 = java.lang.System.out;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = "Invalid port";
        r27.println(r28);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        goto L_0x0105;
    L_0x0139:
        r7 = r7 + 1;
        r17 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        goto L_0x0123;
    L_0x013e:
        r0 = r21;
        r1 = r16;
        r0.setPort(r1);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        goto L_0x008a;
    L_0x0147:
        r27 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r27 = r27.length();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = 2;
        r0 = r27;
        r1 = r28;
        if (r0 <= r1) goto L_0x0168;
    L_0x0155:
        r27 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = 2;
        r6 = r27.substring(r28);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
    L_0x015d:
        r5 = java.net.InetAddress.getByName(r6);	 Catch:{ Exception -> 0x016d }
        r0 = r21;
        r0.setLocalAddress(r5);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        goto L_0x008a;
    L_0x0168:
        r7 = r7 + 1;
        r6 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        goto L_0x015d;
    L_0x016d:
        r9 = move-exception;
        r27 = java.lang.System.out;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = "Invalid address";
        r27.println(r28);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        goto L_0x0105;
    L_0x0176:
        r27 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r27 = r27.length();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = 2;
        r0 = r27;
        r1 = r28;
        if (r0 <= r1) goto L_0x0199;
    L_0x0184:
        r27 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = 2;
        r14 = r27.substring(r28);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
    L_0x018c:
        r27 = org.xbill.DNS.TSIG.fromString(r14);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r0 = r21;
        r1 = r27;
        r0.setTSIGKey(r1);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        goto L_0x008a;
    L_0x0199:
        r7 = r7 + 1;
        r14 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        goto L_0x018c;
    L_0x019e:
        r27 = 1;
        r0 = r21;
        r1 = r27;
        r0.setTCP(r1);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        goto L_0x008a;
    L_0x01a9:
        r27 = 1;
        r0 = r21;
        r1 = r27;
        r0.setIgnoreTruncation(r1);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        goto L_0x008a;
    L_0x01b4:
        r27 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r27 = r27.length();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = 2;
        r0 = r27;
        r1 = r28;
        if (r0 <= r1) goto L_0x01f2;
    L_0x01c2:
        r27 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = 2;
        r11 = r27.substring(r28);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
    L_0x01ca:
        r10 = java.lang.Integer.parseInt(r11);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        if (r10 < 0) goto L_0x01d6;
    L_0x01d0:
        r27 = 1;
        r0 = r27;
        if (r10 <= r0) goto L_0x01f7;
    L_0x01d6:
        r27 = java.lang.System.out;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = new java.lang.StringBuffer;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28.<init>();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r29 = "Unsupported EDNS level: ";
        r28 = r28.append(r29);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r0 = r28;
        r28 = r0.append(r10);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r28 = r28.toString();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        r27.println(r28);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        goto L_0x0105;
    L_0x01f2:
        r7 = r7 + 1;
        r11 = r31[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        goto L_0x01ca;
    L_0x01f7:
        r0 = r21;
        r0.setEDNS(r10);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        goto L_0x008a;
    L_0x01fe:
        r27 = 0;
        r28 = 0;
        r29 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r30 = 0;
        r0 = r21;
        r1 = r27;
        r2 = r28;
        r3 = r29;
        r4 = r30;
        r0.setEDNS(r1, r2, r3, r4);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x00c0 }
        goto L_0x008a;
    L_0x0216:
        r18 = 1;
        goto L_0x008a;
    L_0x021a:
        r27 = r12 - r25;
        r0 = r23;
        r1 = r27;
        defpackage.dig.doQuery(r0, r1);
        goto L_0x0105;
    L_0x0225:
        r9 = move-exception;
        r7 = r8;
        goto L_0x00c1;
    L_0x0229:
        r8 = r7;
        goto L_0x002b;
        */
        throw new UnsupportedOperationException("Method not decompiled: defpackage.dig.main(java.lang.String[]):void");
    }
}
