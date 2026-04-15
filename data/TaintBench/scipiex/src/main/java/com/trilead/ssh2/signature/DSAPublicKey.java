package com.trilead.ssh2.signature;

import java.math.BigInteger;

public class DSAPublicKey {
    private BigInteger g;
    private BigInteger p;
    private BigInteger q;
    private BigInteger y;

    public DSAPublicKey(BigInteger p, BigInteger q, BigInteger g, BigInteger y) {
        this.p = p;
        this.q = q;
        this.g = g;
        this.y = y;
    }

    public BigInteger getP() {
        return this.p;
    }

    public BigInteger getQ() {
        return this.q;
    }

    public BigInteger getG() {
        return this.g;
    }

    public BigInteger getY() {
        return this.y;
    }
}
