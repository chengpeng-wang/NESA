package org.xbill.DNS;

import java.io.IOException;

public class NULLRecord extends Record {
    private static final long serialVersionUID = -5796493183235216538L;
    private byte[] data;

    NULLRecord() {
    }

    /* access modifiers changed from: 0000 */
    public Record getObject() {
        return new NULLRecord();
    }

    public NULLRecord(Name name, int dclass, long ttl, byte[] data) {
        super(name, 10, dclass, ttl);
        if (data.length > 65535) {
            throw new IllegalArgumentException("data must be <65536 bytes");
        }
        this.data = data;
    }

    /* access modifiers changed from: 0000 */
    public void rrFromWire(DNSInput in) throws IOException {
        this.data = in.readByteArray();
    }

    /* access modifiers changed from: 0000 */
    public void rdataFromString(Tokenizer st, Name origin) throws IOException {
        throw st.exception("no defined text format for NULL records");
    }

    /* access modifiers changed from: 0000 */
    public String rrToString() {
        return Record.unknownToString(this.data);
    }

    public byte[] getData() {
        return this.data;
    }

    /* access modifiers changed from: 0000 */
    public void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeByteArray(this.data);
    }
}
