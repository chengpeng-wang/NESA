package org.xbill.DNS;

import java.io.IOException;

public class MINFORecord extends Record {
    private static final long serialVersionUID = -3962147172340353796L;
    private Name errorAddress;
    private Name responsibleAddress;

    MINFORecord() {
    }

    /* access modifiers changed from: 0000 */
    public Record getObject() {
        return new MINFORecord();
    }

    public MINFORecord(Name name, int dclass, long ttl, Name responsibleAddress, Name errorAddress) {
        super(name, 14, dclass, ttl);
        this.responsibleAddress = Record.checkName("responsibleAddress", responsibleAddress);
        this.errorAddress = Record.checkName("errorAddress", errorAddress);
    }

    /* access modifiers changed from: 0000 */
    public void rrFromWire(DNSInput in) throws IOException {
        this.responsibleAddress = new Name(in);
        this.errorAddress = new Name(in);
    }

    /* access modifiers changed from: 0000 */
    public void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.responsibleAddress = st.getName(origin);
        this.errorAddress = st.getName(origin);
    }

    /* access modifiers changed from: 0000 */
    public String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.responsibleAddress);
        sb.append(" ");
        sb.append(this.errorAddress);
        return sb.toString();
    }

    public Name getResponsibleAddress() {
        return this.responsibleAddress;
    }

    public Name getErrorAddress() {
        return this.errorAddress;
    }

    /* access modifiers changed from: 0000 */
    public void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        this.responsibleAddress.toWire(out, null, canonical);
        this.errorAddress.toWire(out, null, canonical);
    }
}
