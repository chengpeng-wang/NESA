package com.trilead.ssh2.signature;

import java.math.BigInteger;

public class RSAPublicKey {
    BigInteger e;
    BigInteger n;

    public RSAPublicKey(BigInteger e, BigInteger n) {
        this.e = e;
        this.n = n;
    }

    public BigInteger getE() {
        return this.e;
    }

    public BigInteger getN() {
        return this.n;
    }
}
