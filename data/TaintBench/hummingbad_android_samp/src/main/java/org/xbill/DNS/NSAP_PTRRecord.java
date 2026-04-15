package org.xbill.DNS;

public class NSAP_PTRRecord extends SingleNameBase {
    private static final long serialVersionUID = 2386284746382064904L;

    NSAP_PTRRecord() {
    }

    /* access modifiers changed from: 0000 */
    public Record getObject() {
        return new NSAP_PTRRecord();
    }

    public NSAP_PTRRecord(Name name, int dclass, long ttl, Name target) {
        super(name, 23, dclass, ttl, target, "target");
    }

    public Name getTarget() {
        return getSingleName();
    }
}
