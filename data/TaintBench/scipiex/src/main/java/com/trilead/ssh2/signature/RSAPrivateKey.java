package com.trilead.ssh2.signature;

import java.math.BigInteger;

public class RSAPrivateKey {
    private BigInteger d;
    private BigInteger e;
    private BigInteger n;

    public RSAPrivateKey(BigInteger d, BigInteger e, BigInteger n) {
        this.d = d;
        this.e = e;
        this.n = n;
    }

    public BigInteger getD() {
        return this.d;
    }

    public BigInteger getE() {
        return this.e;
    }

    public BigInteger getN() {
        return this.n;
    }

    public RSAPublicKey getPublicKey() {
        return new RSAPublicKey(this.e, this.n);
    }
}
