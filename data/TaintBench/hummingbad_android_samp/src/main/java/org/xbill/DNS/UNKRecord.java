package org.xbill.DNS;

import java.io.IOException;

public class UNKRecord extends Record {
    private static final long serialVersionUID = -4193583311594626915L;
    private byte[] data;

    UNKRecord() {
    }

    /* access modifiers changed from: 0000 */
    public Record getObject() {
        return new UNKRecord();
    }

    /* access modifiers changed from: 0000 */
    public void rrFromWire(DNSInput in) throws IOException {
        this.data = in.readByteArray();
    }

    /* access modifiers changed from: 0000 */
    public void rdataFromString(Tokenizer st, Name origin) throws IOException {
        throw st.exception("invalid unknown RR encoding");
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
