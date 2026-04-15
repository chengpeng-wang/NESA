package org.xbill.DNS;

public class MFRecord extends SingleNameBase {
    private static final long serialVersionUID = -6670449036843028169L;

    MFRecord() {
    }

    /* access modifiers changed from: 0000 */
    public Record getObject() {
        return new MFRecord();
    }

    public MFRecord(Name name, int dclass, long ttl, Name mailAgent) {
        super(name, 4, dclass, ttl, mailAgent, "mail agent");
    }

    public Name getMailAgent() {
        return getSingleName();
    }

    public Name getAdditionalName() {
        return getSingleName();
    }
}
