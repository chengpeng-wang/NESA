package org.xbill.DNS;

import java.io.IOException;

public class NAPTRRecord extends Record {
    private static final long serialVersionUID = 5191232392044947002L;
    private byte[] flags;
    private int order;
    private int preference;
    private byte[] regexp;
    private Name replacement;
    private byte[] service;

    NAPTRRecord() {
    }

    /* access modifiers changed from: 0000 */
    public Record getObject() {
        return new NAPTRRecord();
    }

    public NAPTRRecord(Name name, int dclass, long ttl, int order, int preference, String flags, String service, String regexp, Name replacement) {
        super(name, 35, dclass, ttl);
        this.order = Record.checkU16("order", order);
        this.preference = Record.checkU16("preference", preference);
        try {
            this.flags = Record.byteArrayFromString(flags);
            this.service = Record.byteArrayFromString(service);
            this.regexp = Record.byteArrayFromString(regexp);
            this.replacement = Record.checkName("replacement", replacement);
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /* access modifiers changed from: 0000 */
    public void rrFromWire(DNSInput in) throws IOException {
        this.order = in.readU16();
        this.preference = in.readU16();
        this.flags = in.readCountedString();
        this.service = in.readCountedString();
        this.regexp = in.readCountedString();
        this.replacement = new Name(in);
    }

    /* access modifiers changed from: 0000 */
    public void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.order = st.getUInt16();
        this.preference = st.getUInt16();
        try {
            this.flags = Record.byteArrayFromString(st.getString());
            this.service = Record.byteArrayFromString(st.getString());
            this.regexp = Record.byteArrayFromString(st.getString());
            this.replacement = st.getName(origin);
        } catch (TextParseException e) {
            throw st.exception(e.getMessage());
        }
    }

    /* access modifiers changed from: 0000 */
    public String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.order);
        sb.append(" ");
        sb.append(this.preference);
        sb.append(" ");
        sb.append(Record.byteArrayToString(this.flags, true));
        sb.append(" ");
        sb.append(Record.byteArrayToString(this.service, true));
        sb.append(" ");
        sb.append(Record.byteArrayToString(this.regexp, true));
        sb.append(" ");
        sb.append(this.replacement);
        return sb.toString();
    }

    public int getOrder() {
        return this.order;
    }

    public int getPreference() {
        return this.preference;
    }

    public String getFlags() {
        return Record.byteArrayToString(this.flags, false);
    }

    public String getService() {
        return Record.byteArrayToString(this.service, false);
    }

    public String getRegexp() {
        return Record.byteArrayToString(this.regexp, false);
    }

    public Name getReplacement() {
        return this.replacement;
    }

    /* access modifiers changed from: 0000 */
    public void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.order);
        out.writeU16(this.preference);
        out.writeCountedString(this.flags);
        out.writeCountedString(this.service);
        out.writeCountedString(this.regexp);
        this.replacement.toWire(out, null, canonical);
    }

    public Name getAdditionalName() {
        return this.replacement;
    }
}
