package org.xbill.DNS;

public class PTRRecord extends SingleCompressedNameBase {
    private static final long serialVersionUID = -8321636610425434192L;

    PTRRecord() {
    }

    /* access modifiers changed from: 0000 */
    public Record getObject() {
        return new PTRRecord();
    }

    public PTRRecord(Name name, int dclass, long ttl, Name target) {
        super(name, 12, dclass, ttl, target, "target");
    }

    public Name getTarget() {
        return getSingleName();
    }
}
