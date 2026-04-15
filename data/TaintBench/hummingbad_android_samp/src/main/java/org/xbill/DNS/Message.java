package org.xbill.DNS;

import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Message implements Cloneable {
    public static final int MAXLENGTH = 65535;
    static final int TSIG_FAILED = 4;
    static final int TSIG_INTERMEDIATE = 2;
    static final int TSIG_SIGNED = 3;
    static final int TSIG_UNSIGNED = 0;
    static final int TSIG_VERIFIED = 1;
    private static RRset[] emptyRRsetArray = new RRset[0];
    private static Record[] emptyRecordArray = new Record[0];
    private Header header;
    private TSIGRecord querytsig;
    private List[] sections;
    int sig0start;
    private int size;
    int tsigState;
    private int tsigerror;
    private TSIG tsigkey;
    int tsigstart;

    private Message(Header header) {
        this.sections = new List[4];
        this.header = header;
    }

    public Message(int id) {
        this(new Header(id));
    }

    public Message() {
        this(new Header());
    }

    public static Message newQuery(Record r) {
        Message m = new Message();
        m.header.setOpcode(0);
        m.header.setFlag(7);
        m.addRecord(r, 0);
        return m;
    }

    public static Message newUpdate(Name zone) {
        return new Update(zone);
    }

    Message(DNSInput in) throws IOException {
        this(new Header(in));
        boolean isUpdate = this.header.getOpcode() == 5;
        boolean truncated = this.header.getFlag(6);
        int i = 0;
        while (i < 4) {
            try {
                int count = this.header.getCount(i);
                if (count > 0) {
                    this.sections[i] = new ArrayList(count);
                }
                for (int j = 0; j < count; j++) {
                    int pos = in.current();
                    Record rec = Record.fromWire(in, i, isUpdate);
                    this.sections[i].add(rec);
                    if (i == 3) {
                        if (rec.getType() == Type.TSIG) {
                            this.tsigstart = pos;
                        }
                        if (rec.getType() == 24 && ((SIGRecord) rec).getTypeCovered() == 0) {
                            this.sig0start = pos;
                        }
                    }
                }
                i++;
            } catch (WireParseException e) {
                if (!truncated) {
                    throw e;
                }
            }
        }
        this.size = in.current();
    }

    public Message(byte[] b) throws IOException {
        this(new DNSInput(b));
    }

    public void setHeader(Header h) {
        this.header = h;
    }

    public Header getHeader() {
        return this.header;
    }

    public void addRecord(Record r, int section) {
        if (this.sections[section] == null) {
            this.sections[section] = new LinkedList();
        }
        this.header.incCount(section);
        this.sections[section].add(r);
    }

    public boolean removeRecord(Record r, int section) {
        if (this.sections[section] == null || !this.sections[section].remove(r)) {
            return false;
        }
        this.header.decCount(section);
        return true;
    }

    public void removeAllRecords(int section) {
        this.sections[section] = null;
        this.header.setCount(section, 0);
    }

    public boolean findRecord(Record r, int section) {
        return this.sections[section] != null && this.sections[section].contains(r);
    }

    public boolean findRecord(Record r) {
        int i = 1;
        while (i <= 3) {
            if (this.sections[i] != null && this.sections[i].contains(r)) {
                return true;
            }
            i++;
        }
        return false;
    }

    public boolean findRRset(Name name, int type, int section) {
        if (this.sections[section] == null) {
            return false;
        }
        for (int i = 0; i < this.sections[section].size(); i++) {
            Record r = (Record) this.sections[section].get(i);
            if (r.getType() == type && name.equals(r.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean findRRset(Name name, int type) {
        return findRRset(name, type, 1) || findRRset(name, type, 2) || findRRset(name, type, 3);
    }

    public Record getQuestion() {
        List l = this.sections[0];
        if (l == null || l.size() == 0) {
            return null;
        }
        return (Record) l.get(0);
    }

    public TSIGRecord getTSIG() {
        int count = this.header.getCount(3);
        if (count == 0) {
            return null;
        }
        Record rec = (Record) this.sections[3].get(count - 1);
        if (rec.type != Type.TSIG) {
            return null;
        }
        return (TSIGRecord) rec;
    }

    public boolean isSigned() {
        return this.tsigState == 3 || this.tsigState == 1 || this.tsigState == 4;
    }

    public boolean isVerified() {
        return this.tsigState == 1;
    }

    public OPTRecord getOPT() {
        Record[] additional = getSectionArray(3);
        for (int i = 0; i < additional.length; i++) {
            if (additional[i] instanceof OPTRecord) {
                return (OPTRecord) additional[i];
            }
        }
        return null;
    }

    public int getRcode() {
        int rcode = this.header.getRcode();
        OPTRecord opt = getOPT();
        if (opt != null) {
            return rcode + (opt.getExtendedRcode() << 4);
        }
        return rcode;
    }

    public Record[] getSectionArray(int section) {
        if (this.sections[section] == null) {
            return emptyRecordArray;
        }
        List l = this.sections[section];
        return (Record[]) l.toArray(new Record[l.size()]);
    }

    private static boolean sameSet(Record r1, Record r2) {
        return r1.getRRsetType() == r2.getRRsetType() && r1.getDClass() == r2.getDClass() && r1.getName().equals(r2.getName());
    }

    public RRset[] getSectionRRsets(int section) {
        if (this.sections[section] == null) {
            return emptyRRsetArray;
        }
        List sets = new LinkedList();
        Record[] recs = getSectionArray(section);
        Set hash = new HashSet();
        int i = 0;
        while (i < recs.length) {
            Name name = recs[i].getName();
            boolean newset = true;
            if (hash.contains(name)) {
                for (int j = sets.size() - 1; j >= 0; j--) {
                    RRset set = (RRset) sets.get(j);
                    if (set.getType() == recs[i].getRRsetType() && set.getDClass() == recs[i].getDClass() && set.getName().equals(name)) {
                        set.addRR(recs[i]);
                        newset = false;
                        break;
                    }
                }
            }
            if (newset) {
                sets.add(new RRset(recs[i]));
                hash.add(name);
            }
            i++;
        }
        return (RRset[]) sets.toArray(new RRset[sets.size()]);
    }

    /* access modifiers changed from: 0000 */
    public void toWire(DNSOutput out) {
        this.header.toWire(out);
        Compression c = new Compression();
        for (int i = 0; i < 4; i++) {
            if (this.sections[i] != null) {
                for (int j = 0; j < this.sections[i].size(); j++) {
                    ((Record) this.sections[i].get(j)).toWire(out, i, c);
                }
            }
        }
    }

    private int sectionToWire(DNSOutput out, int section, Compression c, int maxLength) {
        int n = this.sections[section].size();
        int pos = out.current();
        int rendered = 0;
        int skipped = 0;
        Record lastrec = null;
        for (int i = 0; i < n; i++) {
            Record rec = (Record) this.sections[section].get(i);
            if (section == 3 && (rec instanceof OPTRecord)) {
                skipped++;
            } else {
                if (!(lastrec == null || sameSet(rec, lastrec))) {
                    pos = out.current();
                    rendered = i;
                }
                lastrec = rec;
                rec.toWire(out, section, c);
                if (out.current() > maxLength) {
                    out.jump(pos);
                    return skipped + (n - rendered);
                }
            }
        }
        return skipped;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x00d7  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00ec  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0105  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0116  */
    private boolean toWire(org.xbill.DNS.DNSOutput r22, int r23) {
        /*
        r21 = this;
        r17 = 12;
        r0 = r23;
        r1 = r17;
        if (r0 >= r1) goto L_0x000b;
    L_0x0008:
        r17 = 0;
    L_0x000a:
        return r17;
    L_0x000b:
        r10 = 0;
        r15 = r23;
        r0 = r21;
        r0 = r0.tsigkey;
        r17 = r0;
        if (r17 == 0) goto L_0x0022;
    L_0x0016:
        r0 = r21;
        r0 = r0.tsigkey;
        r17 = r0;
        r17 = r17.recordLength();
        r15 = r15 - r17;
    L_0x0022:
        r11 = r21.getOPT();
        r12 = 0;
        if (r11 == 0) goto L_0x0036;
    L_0x0029:
        r17 = 3;
        r0 = r17;
        r12 = r11.toWire(r0);
        r0 = r12.length;
        r17 = r0;
        r15 = r15 - r17;
    L_0x0036:
        r14 = r22.current();
        r0 = r21;
        r0 = r0.header;
        r17 = r0;
        r0 = r17;
        r1 = r22;
        r0.toWire(r1);
        r6 = new org.xbill.DNS.Compression;
        r6.m2715init();
        r0 = r21;
        r0 = r0.header;
        r17 = r0;
        r7 = r17.getFlagsByte();
        r5 = 0;
        r8 = 0;
    L_0x0058:
        r17 = 4;
        r0 = r17;
        if (r8 >= r0) goto L_0x00d5;
    L_0x005e:
        r0 = r21;
        r0 = r0.sections;
        r17 = r0;
        r17 = r17[r8];
        if (r17 != 0) goto L_0x006b;
    L_0x0068:
        r8 = r8 + 1;
        goto L_0x0058;
    L_0x006b:
        r0 = r21;
        r1 = r22;
        r13 = r0.sectionToWire(r1, r8, r6, r15);
        if (r13 == 0) goto L_0x00c0;
    L_0x0075:
        r17 = 3;
        r0 = r17;
        if (r8 == r0) goto L_0x00c0;
    L_0x007b:
        r17 = 6;
        r18 = 1;
        r0 = r17;
        r1 = r18;
        r7 = org.xbill.DNS.Header.setFlag(r7, r0, r1);
        r0 = r21;
        r0 = r0.header;
        r17 = r0;
        r0 = r17;
        r17 = r0.getCount(r8);
        r17 = r17 - r13;
        r18 = r14 + 4;
        r19 = r8 * 2;
        r18 = r18 + r19;
        r0 = r22;
        r1 = r17;
        r2 = r18;
        r0.writeU16At(r1, r2);
        r9 = r8 + 1;
    L_0x00a6:
        r17 = 3;
        r0 = r17;
        if (r9 >= r0) goto L_0x00d5;
    L_0x00ac:
        r17 = 0;
        r18 = r14 + 4;
        r19 = r9 * 2;
        r18 = r18 + r19;
        r0 = r22;
        r1 = r17;
        r2 = r18;
        r0.writeU16At(r1, r2);
        r9 = r9 + 1;
        goto L_0x00a6;
    L_0x00c0:
        r17 = 3;
        r0 = r17;
        if (r8 != r0) goto L_0x0068;
    L_0x00c6:
        r0 = r21;
        r0 = r0.header;
        r17 = r0;
        r0 = r17;
        r17 = r0.getCount(r8);
        r5 = r17 - r13;
        goto L_0x0068;
    L_0x00d5:
        if (r12 == 0) goto L_0x00de;
    L_0x00d7:
        r0 = r22;
        r0.writeByteArray(r12);
        r5 = r5 + 1;
    L_0x00de:
        r0 = r21;
        r0 = r0.header;
        r17 = r0;
        r17 = r17.getFlagsByte();
        r0 = r17;
        if (r7 == r0) goto L_0x00f5;
    L_0x00ec:
        r17 = r14 + 2;
        r0 = r22;
        r1 = r17;
        r0.writeU16At(r7, r1);
    L_0x00f5:
        r0 = r21;
        r0 = r0.header;
        r17 = r0;
        r18 = 3;
        r17 = r17.getCount(r18);
        r0 = r17;
        if (r5 == r0) goto L_0x010e;
    L_0x0105:
        r17 = r14 + 10;
        r0 = r22;
        r1 = r17;
        r0.writeU16At(r5, r1);
    L_0x010e:
        r0 = r21;
        r0 = r0.tsigkey;
        r17 = r0;
        if (r17 == 0) goto L_0x0152;
    L_0x0116:
        r0 = r21;
        r0 = r0.tsigkey;
        r17 = r0;
        r18 = r22.toByteArray();
        r0 = r21;
        r0 = r0.tsigerror;
        r19 = r0;
        r0 = r21;
        r0 = r0.querytsig;
        r20 = r0;
        r0 = r17;
        r1 = r21;
        r2 = r18;
        r3 = r19;
        r4 = r20;
        r16 = r0.generate(r1, r2, r3, r4);
        r17 = 3;
        r0 = r16;
        r1 = r22;
        r2 = r17;
        r0.toWire(r1, r2, r6);
        r17 = r5 + 1;
        r18 = r14 + 10;
        r0 = r22;
        r1 = r17;
        r2 = r18;
        r0.writeU16At(r1, r2);
    L_0x0152:
        r17 = 1;
        goto L_0x000a;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xbill.DNS.Message.toWire(org.xbill.DNS.DNSOutput, int):boolean");
    }

    public byte[] toWire() {
        DNSOutput out = new DNSOutput();
        toWire(out);
        this.size = out.current();
        return out.toByteArray();
    }

    public byte[] toWire(int maxLength) {
        DNSOutput out = new DNSOutput();
        toWire(out, maxLength);
        this.size = out.current();
        return out.toByteArray();
    }

    public void setTSIG(TSIG key, int error, TSIGRecord querytsig) {
        this.tsigkey = key;
        this.tsigerror = error;
        this.querytsig = querytsig;
    }

    public int numBytes() {
        return this.size;
    }

    public String sectionToString(int i) {
        if (i > 3) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        Record[] records = getSectionArray(i);
        for (Record rec : records) {
            if (i == 0) {
                sb.append(new StringBuffer().append(";;\t").append(rec.name).toString());
                sb.append(new StringBuffer().append(", type = ").append(Type.string(rec.type)).toString());
                sb.append(new StringBuffer().append(", class = ").append(DClass.string(rec.dclass)).toString());
            } else {
                sb.append(rec);
            }
            sb.append(MASTNativeAdConstants.NEWLINE);
        }
        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (getOPT() != null) {
            sb.append(new StringBuffer().append(this.header.toStringWithRcode(getRcode())).append(MASTNativeAdConstants.NEWLINE).toString());
        } else {
            sb.append(new StringBuffer().append(this.header).append(MASTNativeAdConstants.NEWLINE).toString());
        }
        if (isSigned()) {
            sb.append(";; TSIG ");
            if (isVerified()) {
                sb.append("ok");
            } else {
                sb.append("invalid");
            }
            sb.append(10);
        }
        for (int i = 0; i < 4; i++) {
            if (this.header.getOpcode() != 5) {
                sb.append(new StringBuffer().append(";; ").append(Section.longString(i)).append(":\n").toString());
            } else {
                sb.append(new StringBuffer().append(";; ").append(Section.updString(i)).append(":\n").toString());
            }
            sb.append(new StringBuffer().append(sectionToString(i)).append(MASTNativeAdConstants.NEWLINE).toString());
        }
        sb.append(new StringBuffer().append(";; Message size: ").append(numBytes()).append(" bytes").toString());
        return sb.toString();
    }

    public Object clone() {
        Message m = new Message();
        for (int i = 0; i < this.sections.length; i++) {
            if (this.sections[i] != null) {
                m.sections[i] = new LinkedList(this.sections[i]);
            }
        }
        m.header = (Header) this.header.clone();
        m.size = this.size;
        return m;
    }
}
