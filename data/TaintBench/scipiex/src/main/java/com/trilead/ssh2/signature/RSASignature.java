package com.trilead.ssh2.signature;

import java.math.BigInteger;

public class RSASignature {
    BigInteger s;

    public BigInteger getS() {
        return this.s;
    }

    public RSASignature(BigInteger s) {
        this.s = s;
    }
}
