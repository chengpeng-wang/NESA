package org.xbill.DNS;

import java.io.IOException;
import org.xbill.DNS.utils.base16;

public class GenericEDNSOption extends EDNSOption {
    private byte[] data;

    GenericEDNSOption(int code) {
        super(code);
    }

    public GenericEDNSOption(int code, byte[] data) {
        super(code);
        this.data = Record.checkByteArrayLength("option data", data, 65535);
    }

    /* access modifiers changed from: 0000 */
    public void optionFromWire(DNSInput in) throws IOException {
        this.data = in.readByteArray();
    }

    /* access modifiers changed from: 0000 */
    public void optionToWire(DNSOutput out) {
        out.writeByteArray(this.data);
    }

    /* access modifiers changed from: 0000 */
    public String optionToString() {
        return new StringBuffer().append("<").append(base16.toString(this.data)).append(">").toString();
    }
}
