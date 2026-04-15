package org.xbill.DNS;

public class KXRecord extends U16NameBase {
    private static final long serialVersionUID = 7448568832769757809L;

    KXRecord() {
    }

    /* access modifiers changed from: 0000 */
    public Record getObject() {
        return new KXRecord();
    }

    public KXRecord(Name name, int dclass, long ttl, int preference, Name target) {
        super(name, 36, dclass, ttl, preference, "preference", target, "target");
    }

    public Name getTarget() {
        return getNameField();
    }

    public int getPreference() {
        return getU16Field();
    }

    public Name getAdditionalName() {
        return getNameField();
    }
}
