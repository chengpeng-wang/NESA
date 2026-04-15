package com.trilead.ssh2.signature;

import java.math.BigInteger;

public class DSAPrivateKey {
    private BigInteger g;
    private BigInteger p;
    private BigInteger q;
    private BigInteger x;
    private BigInteger y;

    public DSAPrivateKey(BigInteger p, BigInteger q, BigInteger g, BigInteger y, BigInteger x) {
        this.p = p;
        this.q = q;
        this.g = g;
        this.y = y;
        this.x = x;
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

    public BigInteger getX() {
        return this.x;
    }

    public DSAPublicKey getPublicKey() {
        return new DSAPublicKey(this.p, this.q, this.g, this.y);
    }
}
