package org.xbill.DNS;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Address {
    public static final int IPv4 = 1;
    public static final int IPv6 = 2;

    private Address() {
    }

    private static byte[] parseV4(String s) {
        byte[] values = new byte[4];
        int length = s.length();
        int currentValue = 0;
        int numDigits = 0;
        int i = 0;
        int currentOctet = 0;
        while (i < length) {
            int currentOctet2;
            char c = s.charAt(i);
            if (c < '0' || c > '9') {
                if (c != '.') {
                    return null;
                }
                if (currentOctet == 3) {
                    return null;
                }
                if (numDigits == 0) {
                    return null;
                }
                currentOctet2 = currentOctet + 1;
                values[currentOctet] = (byte) currentValue;
                currentValue = 0;
                numDigits = 0;
            } else if (numDigits == 3) {
                return null;
            } else {
                if (numDigits > 0 && currentValue == 0) {
                    return null;
                }
                numDigits++;
                currentValue = (currentValue * 10) + (c - 48);
                if (currentValue > 255) {
                    return null;
                }
                currentOctet2 = currentOctet;
            }
            i++;
            currentOctet = currentOctet2;
        }
        if (currentOctet != 3) {
            return null;
        }
        if (numDigits == 0) {
            return null;
        }
        values[currentOctet] = (byte) currentValue;
        return values;
    }

    /* JADX WARNING: Removed duplicated region for block: B:97:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0110  */
    private static byte[] parseV6(java.lang.String r19) {
        /*
        r13 = -1;
        r17 = 16;
        r0 = r17;
        r4 = new byte[r0];
        r17 = ":";
        r18 = -1;
        r0 = r19;
        r1 = r17;
        r2 = r18;
        r14 = r0.split(r1, r2);
        r7 = 0;
        r0 = r14.length;
        r17 = r0;
        r12 = r17 + -1;
        r17 = 0;
        r17 = r14[r17];
        r17 = r17.length();
        if (r17 != 0) goto L_0x0035;
    L_0x0025:
        r17 = r12 - r7;
        if (r17 <= 0) goto L_0x005b;
    L_0x0029:
        r17 = 1;
        r17 = r14[r17];
        r17 = r17.length();
        if (r17 != 0) goto L_0x005b;
    L_0x0033:
        r7 = r7 + 1;
    L_0x0035:
        r17 = r14[r12];
        r17 = r17.length();
        if (r17 != 0) goto L_0x004d;
    L_0x003d:
        r17 = r12 - r7;
        if (r17 <= 0) goto L_0x005d;
    L_0x0041:
        r17 = r12 + -1;
        r17 = r14[r17];
        r17 = r17.length();
        if (r17 != 0) goto L_0x005d;
    L_0x004b:
        r12 = r12 + -1;
    L_0x004d:
        r17 = r12 - r7;
        r17 = r17 + 1;
        r18 = 8;
        r0 = r17;
        r1 = r18;
        if (r0 <= r1) goto L_0x005f;
    L_0x0059:
        r4 = 0;
    L_0x005a:
        return r4;
    L_0x005b:
        r4 = 0;
        goto L_0x005a;
    L_0x005d:
        r4 = 0;
        goto L_0x005a;
    L_0x005f:
        r8 = r7;
        r9 = 0;
        r10 = r9;
    L_0x0062:
        if (r8 > r12) goto L_0x012d;
    L_0x0064:
        r17 = r14[r8];
        r17 = r17.length();
        if (r17 != 0) goto L_0x0076;
    L_0x006c:
        if (r13 < 0) goto L_0x0070;
    L_0x006e:
        r4 = 0;
        goto L_0x005a;
    L_0x0070:
        r13 = r10;
        r9 = r10;
    L_0x0072:
        r8 = r8 + 1;
        r10 = r9;
        goto L_0x0062;
    L_0x0076:
        r17 = r14[r8];
        r18 = 46;
        r17 = r17.indexOf(r18);
        if (r17 < 0) goto L_0x00b4;
    L_0x0080:
        if (r8 >= r12) goto L_0x0084;
    L_0x0082:
        r4 = 0;
        goto L_0x005a;
    L_0x0084:
        r17 = 6;
        r0 = r17;
        if (r8 <= r0) goto L_0x008c;
    L_0x008a:
        r4 = 0;
        goto L_0x005a;
    L_0x008c:
        r17 = r14[r8];
        r18 = 1;
        r15 = toByteArray(r17, r18);
        if (r15 != 0) goto L_0x0098;
    L_0x0096:
        r4 = 0;
        goto L_0x005a;
    L_0x0098:
        r11 = 0;
    L_0x0099:
        r17 = 4;
        r0 = r17;
        if (r11 >= r0) goto L_0x00a9;
    L_0x009f:
        r9 = r10 + 1;
        r17 = r15[r11];
        r4[r10] = r17;
        r11 = r11 + 1;
        r10 = r9;
        goto L_0x0099;
    L_0x00a9:
        r9 = r10;
    L_0x00aa:
        r17 = 16;
        r0 = r17;
        if (r9 >= r0) goto L_0x010e;
    L_0x00b0:
        if (r13 >= 0) goto L_0x010e;
    L_0x00b2:
        r4 = 0;
        goto L_0x005a;
    L_0x00b4:
        r11 = 0;
    L_0x00b5:
        r17 = r14[r8];	 Catch:{ NumberFormatException -> 0x0109 }
        r17 = r17.length();	 Catch:{ NumberFormatException -> 0x0109 }
        r0 = r17;
        if (r11 >= r0) goto L_0x00d6;
    L_0x00bf:
        r17 = r14[r8];	 Catch:{ NumberFormatException -> 0x0109 }
        r0 = r17;
        r3 = r0.charAt(r11);	 Catch:{ NumberFormatException -> 0x0109 }
        r17 = 16;
        r0 = r17;
        r17 = java.lang.Character.digit(r3, r0);	 Catch:{ NumberFormatException -> 0x0109 }
        if (r17 >= 0) goto L_0x00d3;
    L_0x00d1:
        r4 = 0;
        goto L_0x005a;
    L_0x00d3:
        r11 = r11 + 1;
        goto L_0x00b5;
    L_0x00d6:
        r17 = r14[r8];	 Catch:{ NumberFormatException -> 0x0109 }
        r18 = 16;
        r16 = java.lang.Integer.parseInt(r17, r18);	 Catch:{ NumberFormatException -> 0x0109 }
        r17 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r0 = r16;
        r1 = r17;
        if (r0 > r1) goto L_0x00e9;
    L_0x00e7:
        if (r16 >= 0) goto L_0x00ec;
    L_0x00e9:
        r4 = 0;
        goto L_0x005a;
    L_0x00ec:
        r9 = r10 + 1;
        r17 = r16 >>> 8;
        r0 = r17;
        r0 = (byte) r0;
        r17 = r0;
        r4[r10] = r17;	 Catch:{ NumberFormatException -> 0x012b }
        r10 = r9 + 1;
        r0 = r16;
        r0 = r0 & 255;
        r17 = r0;
        r0 = r17;
        r0 = (byte) r0;
        r17 = r0;
        r4[r9] = r17;	 Catch:{ NumberFormatException -> 0x0109 }
        r9 = r10;
        goto L_0x0072;
    L_0x0109:
        r5 = move-exception;
        r9 = r10;
    L_0x010b:
        r4 = 0;
        goto L_0x005a;
    L_0x010e:
        if (r13 < 0) goto L_0x005a;
    L_0x0110:
        r6 = 16 - r9;
        r17 = r13 + r6;
        r18 = r9 - r13;
        r0 = r17;
        r1 = r18;
        java.lang.System.arraycopy(r4, r13, r4, r0, r1);
        r8 = r13;
    L_0x011e:
        r17 = r13 + r6;
        r0 = r17;
        if (r8 >= r0) goto L_0x005a;
    L_0x0124:
        r17 = 0;
        r4[r8] = r17;
        r8 = r8 + 1;
        goto L_0x011e;
    L_0x012b:
        r5 = move-exception;
        goto L_0x010b;
    L_0x012d:
        r9 = r10;
        goto L_0x00aa;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xbill.DNS.Address.parseV6(java.lang.String):byte[]");
    }

    public static int[] toArray(String s, int family) {
        byte[] byteArray = toByteArray(s, family);
        if (byteArray == null) {
            return null;
        }
        int[] intArray = new int[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            intArray[i] = byteArray[i] & 255;
        }
        return intArray;
    }

    public static int[] toArray(String s) {
        return toArray(s, 1);
    }

    public static byte[] toByteArray(String s, int family) {
        if (family == 1) {
            return parseV4(s);
        }
        if (family == 2) {
            return parseV6(s);
        }
        throw new IllegalArgumentException("unknown address family");
    }

    public static boolean isDottedQuad(String s) {
        if (toByteArray(s, 1) != null) {
            return true;
        }
        return false;
    }

    public static String toDottedQuad(byte[] addr) {
        return new StringBuffer().append(addr[0] & 255).append(".").append(addr[1] & 255).append(".").append(addr[2] & 255).append(".").append(addr[3] & 255).toString();
    }

    public static String toDottedQuad(int[] addr) {
        return new StringBuffer().append(addr[0]).append(".").append(addr[1]).append(".").append(addr[2]).append(".").append(addr[3]).toString();
    }

    private static Record[] lookupHostName(String name, boolean all) throws UnknownHostException {
        try {
            Lookup lookup = new Lookup(name, 1);
            Record[] a = lookup.run();
            Record[] aaaa;
            if (a == null) {
                if (lookup.getResult() == 4) {
                    aaaa = new Lookup(name, 28).run();
                    if (aaaa != null) {
                        return aaaa;
                    }
                }
                throw new UnknownHostException("unknown host");
            } else if (!all) {
                return a;
            } else {
                aaaa = new Lookup(name, 28).run();
                if (aaaa == null) {
                    return a;
                }
                Record[] merged = new Record[(a.length + aaaa.length)];
                System.arraycopy(a, 0, merged, 0, a.length);
                System.arraycopy(aaaa, 0, merged, a.length, aaaa.length);
                return merged;
            }
        } catch (TextParseException e) {
            throw new UnknownHostException("invalid name");
        }
    }

    private static InetAddress addrFromRecord(String name, Record r) throws UnknownHostException {
        InetAddress addr;
        if (r instanceof ARecord) {
            addr = ((ARecord) r).getAddress();
        } else {
            addr = ((AAAARecord) r).getAddress();
        }
        return InetAddress.getByAddress(name, addr.getAddress());
    }

    public static InetAddress getByName(String name) throws UnknownHostException {
        int i = 0;
        try {
            return getByAddress(name);
        } catch (UnknownHostException e) {
            return addrFromRecord(name, lookupHostName(name, i)[i]);
        }
    }

    public static InetAddress[] getAllByName(String name) throws UnknownHostException {
        try {
            return new InetAddress[]{getByAddress(name)};
        } catch (UnknownHostException e) {
            Record[] records = lookupHostName(name, true);
            InetAddress[] addrs = new InetAddress[records.length];
            for (int i = 0; i < records.length; i++) {
                addrs[i] = addrFromRecord(name, records[i]);
            }
            return addrs;
        }
    }

    public static InetAddress getByAddress(String addr) throws UnknownHostException {
        byte[] bytes = toByteArray(addr, 1);
        if (bytes != null) {
            return InetAddress.getByAddress(addr, bytes);
        }
        bytes = toByteArray(addr, 2);
        if (bytes != null) {
            return InetAddress.getByAddress(addr, bytes);
        }
        throw new UnknownHostException(new StringBuffer().append("Invalid address: ").append(addr).toString());
    }

    public static InetAddress getByAddress(String addr, int family) throws UnknownHostException {
        if (family == 1 || family == 2) {
            byte[] bytes = toByteArray(addr, family);
            if (bytes != null) {
                return InetAddress.getByAddress(addr, bytes);
            }
            throw new UnknownHostException(new StringBuffer().append("Invalid address: ").append(addr).toString());
        }
        throw new IllegalArgumentException("unknown address family");
    }

    public static String getHostName(InetAddress addr) throws UnknownHostException {
        Record[] records = new Lookup(ReverseMap.fromAddress(addr), 12).run();
        if (records != null) {
            return records[0].getTarget().toString();
        }
        throw new UnknownHostException("unknown address");
    }

    public static int familyOf(InetAddress address) {
        if (address instanceof Inet4Address) {
            return 1;
        }
        if (address instanceof Inet6Address) {
            return 2;
        }
        throw new IllegalArgumentException("unknown address family");
    }

    public static int addressLength(int family) {
        if (family == 1) {
            return 4;
        }
        if (family == 2) {
            return 16;
        }
        throw new IllegalArgumentException("unknown address family");
    }

    public static InetAddress truncate(InetAddress address, int maskLength) {
        int maxMaskLength = addressLength(familyOf(address)) * 8;
        if (maskLength < 0 || maskLength > maxMaskLength) {
            throw new IllegalArgumentException("invalid mask length");
        } else if (maskLength == maxMaskLength) {
            return address;
        } else {
            int i;
            byte[] bytes = address.getAddress();
            for (i = (maskLength / 8) + 1; i < bytes.length; i++) {
                bytes[i] = (byte) 0;
            }
            int bitmask = 0;
            for (i = 0; i < maskLength % 8; i++) {
                bitmask |= 1 << (7 - i);
            }
            int i2 = maskLength / 8;
            bytes[i2] = (byte) (bytes[i2] & bitmask);
            try {
                return InetAddress.getByAddress(bytes);
            } catch (UnknownHostException e) {
                throw new IllegalArgumentException("invalid address");
            }
        }
    }
}
