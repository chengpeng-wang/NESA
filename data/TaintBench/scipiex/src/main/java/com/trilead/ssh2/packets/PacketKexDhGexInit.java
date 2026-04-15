package com.trilead.ssh2.packets;

import java.math.BigInteger;

public class PacketKexDhGexInit {
    BigInteger e;
    byte[] payload;

    public PacketKexDhGexInit(BigInteger e) {
        this.e = e;
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(32);
            tw.writeMPInt(this.e);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
