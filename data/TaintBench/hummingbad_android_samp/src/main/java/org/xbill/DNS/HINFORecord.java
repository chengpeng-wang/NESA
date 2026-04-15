package org.xbill.DNS;

import java.io.IOException;

public class HINFORecord extends Record {
    private static final long serialVersionUID = -4732870630947452112L;
    private byte[] cpu;
    private byte[] os;

    HINFORecord() {
    }

    /* access modifiers changed from: 0000 */
    public Record getObject() {
        return new HINFORecord();
    }

    public HINFORecord(Name name, int dclass, long ttl, String cpu, String os) {
        super(name, 13, dclass, ttl);
        try {
            this.cpu = Record.byteArrayFromString(cpu);
            this.os = Record.byteArrayFromString(os);
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /* access modifiers changed from: 0000 */
    public void rrFromWire(DNSInput in) throws IOException {
        this.cpu = in.readCountedString();
        this.os = in.readCountedString();
    }

    /* access modifiers changed from: 0000 */
    public void rdataFromString(Tokenizer st, Name origin) throws IOException {
        try {
            this.cpu = Record.byteArrayFromString(st.getString());
            this.os = Record.byteArrayFromString(st.getString());
        } catch (TextParseException e) {
            throw st.exception(e.getMessage());
        }
    }

    public String getCPU() {
        return Record.byteArrayToString(this.cpu, false);
    }

    public String getOS() {
        return Record.byteArrayToString(this.os, false);
    }

    /* access modifiers changed from: 0000 */
    public void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeCountedString(this.cpu);
        out.writeCountedString(this.os);
    }

    /* access modifiers changed from: 0000 */
    public String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(Record.byteArrayToString(this.cpu, true));
        sb.append(" ");
        sb.append(Record.byteArrayToString(this.os, true));
        return sb.toString();
    }
}
