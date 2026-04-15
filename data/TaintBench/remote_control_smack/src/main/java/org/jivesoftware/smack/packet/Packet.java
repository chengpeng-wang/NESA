package org.jivesoftware.smack.packet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jivesoftware.smack.util.StringUtils;

public abstract class Packet {
    protected static final String DEFAULT_LANGUAGE = Locale.getDefault().getLanguage().toLowerCase();
    private static String DEFAULT_XML_NS = null;
    public static final String ID_NOT_AVAILABLE = "ID_NOT_AVAILABLE";
    private static long id = 0;
    private static String prefix = (StringUtils.randomString(5) + "-");
    private XMPPError error = null;
    private String from = null;
    private final List<PacketExtension> packetExtensions = new CopyOnWriteArrayList();
    private String packetID = null;
    private final Map<String, Object> properties = new HashMap();
    private String to = null;
    private String xmlns = DEFAULT_XML_NS;

    public abstract String toXML();

    public static synchronized String nextID() {
        String stringBuilder;
        synchronized (Packet.class) {
            StringBuilder append = new StringBuilder().append(prefix);
            long j = id;
            id = 1 + j;
            stringBuilder = append.append(Long.toString(j)).toString();
        }
        return stringBuilder;
    }

    public static void setDefaultXmlns(String defaultXmlns) {
        DEFAULT_XML_NS = defaultXmlns;
    }

    public String getPacketID() {
        if (ID_NOT_AVAILABLE.equals(this.packetID)) {
            return null;
        }
        if (this.packetID == null) {
            this.packetID = nextID();
        }
        return this.packetID;
    }

    public void setPacketID(String packetID) {
        this.packetID = packetID;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public XMPPError getError() {
        return this.error;
    }

    public void setError(XMPPError error) {
        this.error = error;
    }

    public synchronized Collection<PacketExtension> getExtensions() {
        Collection<PacketExtension> emptyList;
        if (this.packetExtensions == null) {
            emptyList = Collections.emptyList();
        } else {
            emptyList = Collections.unmodifiableList(new ArrayList(this.packetExtensions));
        }
        return emptyList;
    }

    public PacketExtension getExtension(String namespace) {
        return getExtension(null, namespace);
    }

    public PacketExtension getExtension(String elementName, String namespace) {
        if (namespace == null) {
            return null;
        }
        for (PacketExtension ext : this.packetExtensions) {
            if ((elementName == null || elementName.equals(ext.getElementName())) && namespace.equals(ext.getNamespace())) {
                return ext;
            }
        }
        return null;
    }

    public void addExtension(PacketExtension extension) {
        this.packetExtensions.add(extension);
    }

    public void removeExtension(PacketExtension extension) {
        this.packetExtensions.remove(extension);
    }

    public synchronized Object getProperty(String name) {
        Object obj;
        if (this.properties == null) {
            obj = null;
        } else {
            obj = this.properties.get(name);
        }
        return obj;
    }

    public synchronized void setProperty(String name, Object value) {
        if (value instanceof Serializable) {
            this.properties.put(name, value);
        } else {
            throw new IllegalArgumentException("Value must be serialiazble");
        }
    }

    public synchronized void deleteProperty(String name) {
        if (this.properties != null) {
            this.properties.remove(name);
        }
    }

    public synchronized Collection<String> getPropertyNames() {
        Collection<String> emptySet;
        if (this.properties == null) {
            emptySet = Collections.emptySet();
        } else {
            emptySet = Collections.unmodifiableSet(new HashSet(this.properties.keySet()));
        }
        return emptySet;
    }

    /* access modifiers changed from: protected|declared_synchronized */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0126 A:{SYNTHETIC, Splitter:B:55:0x0126} */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x007e A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x012b A:{SYNTHETIC, Splitter:B:58:0x012b} */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0136 A:{SYNTHETIC, Splitter:B:63:0x0136} */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x013b A:{SYNTHETIC, Splitter:B:66:0x013b} */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0126 A:{SYNTHETIC, Splitter:B:55:0x0126} */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x012b A:{SYNTHETIC, Splitter:B:58:0x012b} */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x007e A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0136 A:{SYNTHETIC, Splitter:B:63:0x0136} */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x013b A:{SYNTHETIC, Splitter:B:66:0x013b} */
    public synchronized java.lang.String getExtensionsXML() {
        /*
        r13 = this;
        monitor-enter(r13);
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0022 }
        r0.<init>();	 Catch:{ all -> 0x0022 }
        r11 = r13.getExtensions();	 Catch:{ all -> 0x0022 }
        r6 = r11.iterator();	 Catch:{ all -> 0x0022 }
    L_0x000e:
        r11 = r6.hasNext();	 Catch:{ all -> 0x0022 }
        if (r11 == 0) goto L_0x0025;
    L_0x0014:
        r5 = r6.next();	 Catch:{ all -> 0x0022 }
        r5 = (org.jivesoftware.smack.packet.PacketExtension) r5;	 Catch:{ all -> 0x0022 }
        r11 = r5.toXML();	 Catch:{ all -> 0x0022 }
        r0.append(r11);	 Catch:{ all -> 0x0022 }
        goto L_0x000e;
    L_0x0022:
        r11 = move-exception;
        monitor-exit(r13);
        throw r11;
    L_0x0025:
        r11 = r13.properties;	 Catch:{ all -> 0x0022 }
        if (r11 == 0) goto L_0x0144;
    L_0x0029:
        r11 = r13.properties;	 Catch:{ all -> 0x0022 }
        r11 = r11.isEmpty();	 Catch:{ all -> 0x0022 }
        if (r11 != 0) goto L_0x0144;
    L_0x0031:
        r11 = "<properties xmlns=\"http://www.jivesoftware.com/xmlns/xmpp/properties\">";
        r0.append(r11);	 Catch:{ all -> 0x0022 }
        r11 = r13.getPropertyNames();	 Catch:{ all -> 0x0022 }
        r6 = r11.iterator();	 Catch:{ all -> 0x0022 }
    L_0x003e:
        r11 = r6.hasNext();	 Catch:{ all -> 0x0022 }
        if (r11 == 0) goto L_0x013f;
    L_0x0044:
        r7 = r6.next();	 Catch:{ all -> 0x0022 }
        r7 = (java.lang.String) r7;	 Catch:{ all -> 0x0022 }
        r10 = r13.getProperty(r7);	 Catch:{ all -> 0x0022 }
        r11 = "<property>";
        r0.append(r11);	 Catch:{ all -> 0x0022 }
        r11 = "<name>";
        r11 = r0.append(r11);	 Catch:{ all -> 0x0022 }
        r12 = org.jivesoftware.smack.util.StringUtils.escapeForXML(r7);	 Catch:{ all -> 0x0022 }
        r11 = r11.append(r12);	 Catch:{ all -> 0x0022 }
        r12 = "</name>";
        r11.append(r12);	 Catch:{ all -> 0x0022 }
        r11 = "<value type=\"";
        r0.append(r11);	 Catch:{ all -> 0x0022 }
        r11 = r10 instanceof java.lang.Integer;	 Catch:{ all -> 0x0022 }
        if (r11 == 0) goto L_0x0084;
    L_0x006f:
        r11 = "integer\">";
        r11 = r0.append(r11);	 Catch:{ all -> 0x0022 }
        r11 = r11.append(r10);	 Catch:{ all -> 0x0022 }
        r12 = "</value>";
        r11.append(r12);	 Catch:{ all -> 0x0022 }
    L_0x007e:
        r11 = "</property>";
        r0.append(r11);	 Catch:{ all -> 0x0022 }
        goto L_0x003e;
    L_0x0084:
        r11 = r10 instanceof java.lang.Long;	 Catch:{ all -> 0x0022 }
        if (r11 == 0) goto L_0x0098;
    L_0x0088:
        r11 = "long\">";
        r11 = r0.append(r11);	 Catch:{ all -> 0x0022 }
        r11 = r11.append(r10);	 Catch:{ all -> 0x0022 }
        r12 = "</value>";
        r11.append(r12);	 Catch:{ all -> 0x0022 }
        goto L_0x007e;
    L_0x0098:
        r11 = r10 instanceof java.lang.Float;	 Catch:{ all -> 0x0022 }
        if (r11 == 0) goto L_0x00ac;
    L_0x009c:
        r11 = "float\">";
        r11 = r0.append(r11);	 Catch:{ all -> 0x0022 }
        r11 = r11.append(r10);	 Catch:{ all -> 0x0022 }
        r12 = "</value>";
        r11.append(r12);	 Catch:{ all -> 0x0022 }
        goto L_0x007e;
    L_0x00ac:
        r11 = r10 instanceof java.lang.Double;	 Catch:{ all -> 0x0022 }
        if (r11 == 0) goto L_0x00c0;
    L_0x00b0:
        r11 = "double\">";
        r11 = r0.append(r11);	 Catch:{ all -> 0x0022 }
        r11 = r11.append(r10);	 Catch:{ all -> 0x0022 }
        r12 = "</value>";
        r11.append(r12);	 Catch:{ all -> 0x0022 }
        goto L_0x007e;
    L_0x00c0:
        r11 = r10 instanceof java.lang.Boolean;	 Catch:{ all -> 0x0022 }
        if (r11 == 0) goto L_0x00d4;
    L_0x00c4:
        r11 = "boolean\">";
        r11 = r0.append(r11);	 Catch:{ all -> 0x0022 }
        r11 = r11.append(r10);	 Catch:{ all -> 0x0022 }
        r12 = "</value>";
        r11.append(r12);	 Catch:{ all -> 0x0022 }
        goto L_0x007e;
    L_0x00d4:
        r11 = r10 instanceof java.lang.String;	 Catch:{ all -> 0x0022 }
        if (r11 == 0) goto L_0x00ec;
    L_0x00d8:
        r11 = "string\">";
        r0.append(r11);	 Catch:{ all -> 0x0022 }
        r10 = (java.lang.String) r10;	 Catch:{ all -> 0x0022 }
        r11 = org.jivesoftware.smack.util.StringUtils.escapeForXML(r10);	 Catch:{ all -> 0x0022 }
        r0.append(r11);	 Catch:{ all -> 0x0022 }
        r11 = "</value>";
        r0.append(r11);	 Catch:{ all -> 0x0022 }
        goto L_0x007e;
    L_0x00ec:
        r1 = 0;
        r8 = 0;
        r2 = new java.io.ByteArrayOutputStream;	 Catch:{ Exception -> 0x0120 }
        r2.<init>();	 Catch:{ Exception -> 0x0120 }
        r9 = new java.io.ObjectOutputStream;	 Catch:{ Exception -> 0x0159, all -> 0x0152 }
        r9.<init>(r2);	 Catch:{ Exception -> 0x0159, all -> 0x0152 }
        r9.writeObject(r10);	 Catch:{ Exception -> 0x015c, all -> 0x0155 }
        r11 = "java-object\">";
        r0.append(r11);	 Catch:{ Exception -> 0x015c, all -> 0x0155 }
        r11 = r2.toByteArray();	 Catch:{ Exception -> 0x015c, all -> 0x0155 }
        r4 = org.jivesoftware.smack.util.StringUtils.encodeBase64(r11);	 Catch:{ Exception -> 0x015c, all -> 0x0155 }
        r11 = r0.append(r4);	 Catch:{ Exception -> 0x015c, all -> 0x0155 }
        r12 = "</value>";
        r11.append(r12);	 Catch:{ Exception -> 0x015c, all -> 0x0155 }
        if (r9 == 0) goto L_0x0116;
    L_0x0113:
        r9.close();	 Catch:{ Exception -> 0x014a }
    L_0x0116:
        if (r2 == 0) goto L_0x007e;
    L_0x0118:
        r2.close();	 Catch:{ Exception -> 0x011d }
        goto L_0x007e;
    L_0x011d:
        r11 = move-exception;
        goto L_0x007e;
    L_0x0120:
        r3 = move-exception;
    L_0x0121:
        r3.printStackTrace();	 Catch:{ all -> 0x0133 }
        if (r8 == 0) goto L_0x0129;
    L_0x0126:
        r8.close();	 Catch:{ Exception -> 0x014c }
    L_0x0129:
        if (r1 == 0) goto L_0x007e;
    L_0x012b:
        r1.close();	 Catch:{ Exception -> 0x0130 }
        goto L_0x007e;
    L_0x0130:
        r11 = move-exception;
        goto L_0x007e;
    L_0x0133:
        r11 = move-exception;
    L_0x0134:
        if (r8 == 0) goto L_0x0139;
    L_0x0136:
        r8.close();	 Catch:{ Exception -> 0x014e }
    L_0x0139:
        if (r1 == 0) goto L_0x013e;
    L_0x013b:
        r1.close();	 Catch:{ Exception -> 0x0150 }
    L_0x013e:
        throw r11;	 Catch:{ all -> 0x0022 }
    L_0x013f:
        r11 = "</properties>";
        r0.append(r11);	 Catch:{ all -> 0x0022 }
    L_0x0144:
        r11 = r0.toString();	 Catch:{ all -> 0x0022 }
        monitor-exit(r13);
        return r11;
    L_0x014a:
        r11 = move-exception;
        goto L_0x0116;
    L_0x014c:
        r11 = move-exception;
        goto L_0x0129;
    L_0x014e:
        r12 = move-exception;
        goto L_0x0139;
    L_0x0150:
        r12 = move-exception;
        goto L_0x013e;
    L_0x0152:
        r11 = move-exception;
        r1 = r2;
        goto L_0x0134;
    L_0x0155:
        r11 = move-exception;
        r8 = r9;
        r1 = r2;
        goto L_0x0134;
    L_0x0159:
        r3 = move-exception;
        r1 = r2;
        goto L_0x0121;
    L_0x015c:
        r3 = move-exception;
        r8 = r9;
        r1 = r2;
        goto L_0x0121;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.packet.Packet.getExtensionsXML():java.lang.String");
    }

    public String getXmlns() {
        return this.xmlns;
    }

    protected static String parseXMLLang(String language) {
        if (language == null || "".equals(language)) {
            return DEFAULT_LANGUAGE;
        }
        return language;
    }

    protected static String getDefaultLanguage() {
        return DEFAULT_LANGUAGE;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Packet packet = (Packet) o;
        if (this.error != null) {
            if (!this.error.equals(packet.error)) {
                return false;
            }
        } else if (packet.error != null) {
            return false;
        }
        if (this.from != null) {
            if (!this.from.equals(packet.from)) {
                return false;
            }
        } else if (packet.from != null) {
            return false;
        }
        if (!this.packetExtensions.equals(packet.packetExtensions)) {
            return false;
        }
        if (this.packetID != null) {
            if (!this.packetID.equals(packet.packetID)) {
                return false;
            }
        } else if (packet.packetID != null) {
            return false;
        }
        if (this.properties != null) {
            if (!this.properties.equals(packet.properties)) {
                return false;
            }
        } else if (packet.properties != null) {
            return false;
        }
        if (this.to != null) {
            if (!this.to.equals(packet.to)) {
                return false;
            }
        } else if (packet.to != null) {
            return false;
        }
        if (this.xmlns == null ? packet.xmlns != null : !this.xmlns.equals(packet.xmlns)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int hashCode;
        int i = 0;
        if (this.xmlns != null) {
            result = this.xmlns.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.packetID != null) {
            hashCode = this.packetID.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.to != null) {
            hashCode = this.to.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.from != null) {
            hashCode = this.from.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (((((i2 + hashCode) * 31) + this.packetExtensions.hashCode()) * 31) + this.properties.hashCode()) * 31;
        if (this.error != null) {
            i = this.error.hashCode();
        }
        return hashCode + i;
    }
}
