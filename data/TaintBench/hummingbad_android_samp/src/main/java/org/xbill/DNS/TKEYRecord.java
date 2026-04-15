package org.xbill.DNS;

import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.IOException;
import java.util.Date;
import org.xbill.DNS.utils.base64;

public class TKEYRecord extends Record {
    public static final int DELETE = 5;
    public static final int DIFFIEHELLMAN = 2;
    public static final int GSSAPI = 3;
    public static final int RESOLVERASSIGNED = 4;
    public static final int SERVERASSIGNED = 1;
    private static final long serialVersionUID = 8828458121926391756L;
    private Name alg;
    private int error;
    private byte[] key;
    private int mode;
    private byte[] other;
    private Date timeExpire;
    private Date timeInception;

    TKEYRecord() {
    }

    /* access modifiers changed from: 0000 */
    public Record getObject() {
        return new TKEYRecord();
    }

    public TKEYRecord(Name name, int dclass, long ttl, Name alg, Date timeInception, Date timeExpire, int mode, int error, byte[] key, byte[] other) {
        super(name, Type.TKEY, dclass, ttl);
        this.alg = Record.checkName("alg", alg);
        this.timeInception = timeInception;
        this.timeExpire = timeExpire;
        this.mode = Record.checkU16("mode", mode);
        this.error = Record.checkU16(MASTNativeAdConstants.RESPONSE_ERROR, error);
        this.key = key;
        this.other = other;
    }

    /* access modifiers changed from: 0000 */
    public void rrFromWire(DNSInput in) throws IOException {
        this.alg = new Name(in);
        this.timeInception = new Date(in.readU32() * 1000);
        this.timeExpire = new Date(in.readU32() * 1000);
        this.mode = in.readU16();
        this.error = in.readU16();
        int keylen = in.readU16();
        if (keylen > 0) {
            this.key = in.readByteArray(keylen);
        } else {
            this.key = null;
        }
        int otherlen = in.readU16();
        if (otherlen > 0) {
            this.other = in.readByteArray(otherlen);
        } else {
            this.other = null;
        }
    }

    /* access modifiers changed from: 0000 */
    public void rdataFromString(Tokenizer st, Name origin) throws IOException {
        throw st.exception("no text format defined for TKEY");
    }

    /* access modifiers changed from: protected */
    public String modeString() {
        switch (this.mode) {
            case 1:
                return "SERVERASSIGNED";
            case 2:
                return "DIFFIEHELLMAN";
            case 3:
                return "GSSAPI";
            case 4:
                return "RESOLVERASSIGNED";
            case 5:
                return "DELETE";
            default:
                return Integer.toString(this.mode);
        }
    }

    /* access modifiers changed from: 0000 */
    public String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.alg);
        sb.append(" ");
        if (Options.check("multiline")) {
            sb.append("(\n\t");
        }
        sb.append(FormattedTime.format(this.timeInception));
        sb.append(" ");
        sb.append(FormattedTime.format(this.timeExpire));
        sb.append(" ");
        sb.append(modeString());
        sb.append(" ");
        sb.append(Rcode.TSIGstring(this.error));
        if (Options.check("multiline")) {
            sb.append(MASTNativeAdConstants.NEWLINE);
            if (this.key != null) {
                sb.append(base64.formatString(this.key, 64, "\t", false));
                sb.append(MASTNativeAdConstants.NEWLINE);
            }
            if (this.other != null) {
                sb.append(base64.formatString(this.other, 64, "\t", false));
            }
            sb.append(" )");
        } else {
            sb.append(" ");
            if (this.key != null) {
                sb.append(base64.toString(this.key));
                sb.append(" ");
            }
            if (this.other != null) {
                sb.append(base64.toString(this.other));
            }
        }
        return sb.toString();
    }

    public Name getAlgorithm() {
        return this.alg;
    }

    public Date getTimeInception() {
        return this.timeInception;
    }

    public Date getTimeExpire() {
        return this.timeExpire;
    }

    public int getMode() {
        return this.mode;
    }

    public int getError() {
        return this.error;
    }

    public byte[] getKey() {
        return this.key;
    }

    public byte[] getOther() {
        return this.other;
    }

    /* access modifiers changed from: 0000 */
    public void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        this.alg.toWire(out, null, canonical);
        out.writeU32(this.timeInception.getTime() / 1000);
        out.writeU32(this.timeExpire.getTime() / 1000);
        out.writeU16(this.mode);
        out.writeU16(this.error);
        if (this.key != null) {
            out.writeU16(this.key.length);
            out.writeByteArray(this.key);
        } else {
            out.writeU16(0);
        }
        if (this.other != null) {
            out.writeU16(this.other.length);
            out.writeByteArray(this.other);
            return;
        }
        out.writeU16(0);
    }
}
