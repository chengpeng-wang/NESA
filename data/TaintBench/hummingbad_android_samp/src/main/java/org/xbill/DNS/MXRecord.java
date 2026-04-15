package org.xbill.DNS;

public class MXRecord extends U16NameBase {
    private static final long serialVersionUID = 2914841027584208546L;

    MXRecord() {
    }

    /* access modifiers changed from: 0000 */
    public Record getObject() {
        return new MXRecord();
    }

    public MXRecord(Name name, int dclass, long ttl, int priority, Name target) {
        super(name, 15, dclass, ttl, priority, "priority", target, "target");
    }

    public Name getTarget() {
        return getNameField();
    }

    public int getPriority() {
        return getU16Field();
    }

    /* access modifiers changed from: 0000 */
    public void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.u16Field);
        this.nameField.toWire(out, c, canonical);
    }

    public Name getAdditionalName() {
        return getNameField();
    }
}
