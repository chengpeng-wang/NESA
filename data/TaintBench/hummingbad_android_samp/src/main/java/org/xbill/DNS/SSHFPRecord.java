package org.xbill.DNS;

import java.io.IOException;
import org.xbill.DNS.utils.base16;

public class SSHFPRecord extends Record {
    private static final long serialVersionUID = -8104701402654687025L;
    private int alg;
    private int digestType;
    private byte[] fingerprint;

    public static class Algorithm {
        public static final int DSS = 2;
        public static final int RSA = 1;

        private Algorithm() {
        }
    }

    public static class Digest {
        public static final int SHA1 = 1;

        private Digest() {
        }
    }

    SSHFPRecord() {
    }

    /* access modifiers changed from: 0000 */
    public Record getObject() {
        return new SSHFPRecord();
    }

    public SSHFPRecord(Name name, int dclass, long ttl, int alg, int digestType, byte[] fingerprint) {
        super(name, 44, dclass, ttl);
        this.alg = Record.checkU8("alg", alg);
        this.digestType = Record.checkU8("digestType", digestType);
        this.fingerprint = fingerprint;
    }

    /* access modifiers changed from: 0000 */
    public void rrFromWire(DNSInput in) throws IOException {
        this.alg = in.readU8();
        this.digestType = in.readU8();
        this.fingerprint = in.readByteArray();
    }

    /* access modifiers changed from: 0000 */
    public void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.alg = st.getUInt8();
        this.digestType = st.getUInt8();
        this.fingerprint = st.getHex(true);
    }

    /* access modifiers changed from: 0000 */
    public String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.alg);
        sb.append(" ");
        sb.append(this.digestType);
        sb.append(" ");
        sb.append(base16.toString(this.fingerprint));
        return sb.toString();
    }

    public int getAlgorithm() {
        return this.alg;
    }

    public int getDigestType() {
        return this.digestType;
    }

    public byte[] getFingerPrint() {
        return this.fingerprint;
    }

    /* access modifiers changed from: 0000 */
    public void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU8(this.alg);
        out.writeU8(this.digestType);
        out.writeByteArray(this.fingerprint);
    }
}
