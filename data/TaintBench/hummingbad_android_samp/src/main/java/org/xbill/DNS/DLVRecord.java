package org.xbill.DNS;

import java.io.IOException;
import org.xbill.DNS.utils.base16;

public class DLVRecord extends Record {
    public static final int SHA1_DIGEST_ID = 1;
    public static final int SHA256_DIGEST_ID = 1;
    private static final long serialVersionUID = 1960742375677534148L;
    private int alg;
    private byte[] digest;
    private int digestid;
    private int footprint;

    DLVRecord() {
    }

    /* access modifiers changed from: 0000 */
    public Record getObject() {
        return new DLVRecord();
    }

    public DLVRecord(Name name, int dclass, long ttl, int footprint, int alg, int digestid, byte[] digest) {
        super(name, Type.DLV, dclass, ttl);
        this.footprint = Record.checkU16("footprint", footprint);
        this.alg = Record.checkU8("alg", alg);
        this.digestid = Record.checkU8("digestid", digestid);
        this.digest = digest;
    }

    /* access modifiers changed from: 0000 */
    public void rrFromWire(DNSInput in) throws IOException {
        this.footprint = in.readU16();
        this.alg = in.readU8();
        this.digestid = in.readU8();
        this.digest = in.readByteArray();
    }

    /* access modifiers changed from: 0000 */
    public void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.footprint = st.getUInt16();
        this.alg = st.getUInt8();
        this.digestid = st.getUInt8();
        this.digest = st.getHex();
    }

    /* access modifiers changed from: 0000 */
    public String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.footprint);
        sb.append(" ");
        sb.append(this.alg);
        sb.append(" ");
        sb.append(this.digestid);
        if (this.digest != null) {
            sb.append(" ");
            sb.append(base16.toString(this.digest));
        }
        return sb.toString();
    }

    public int getAlgorithm() {
        return this.alg;
    }

    public int getDigestID() {
        return this.digestid;
    }

    public byte[] getDigest() {
        return this.digest;
    }

    public int getFootprint() {
        return this.footprint;
    }

    /* access modifiers changed from: 0000 */
    public void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.footprint);
        out.writeU8(this.alg);
        out.writeU8(this.digestid);
        if (this.digest != null) {
            out.writeByteArray(this.digest);
        }
    }
}
