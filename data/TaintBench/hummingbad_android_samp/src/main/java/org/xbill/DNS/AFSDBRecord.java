package org.xbill.DNS;

import com.moceanmobile.mast.MASTNativeAdConstants;

public class AFSDBRecord extends U16NameBase {
    private static final long serialVersionUID = 3034379930729102437L;

    AFSDBRecord() {
    }

    /* access modifiers changed from: 0000 */
    public Record getObject() {
        return new AFSDBRecord();
    }

    public AFSDBRecord(Name name, int dclass, long ttl, int subtype, Name host) {
        super(name, 18, dclass, ttl, subtype, MASTNativeAdConstants.RESPONSE_SUBTYPE, host, "host");
    }

    public int getSubtype() {
        return getU16Field();
    }

    public Name getHost() {
        return getNameField();
    }
}
