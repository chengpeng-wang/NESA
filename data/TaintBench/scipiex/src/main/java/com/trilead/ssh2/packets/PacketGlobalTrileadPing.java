package com.trilead.ssh2.packets;

public class PacketGlobalTrileadPing {
    byte[] payload;

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(80);
            tw.writeString("trilead-ping");
            tw.writeBoolean(true);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
