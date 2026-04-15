package com.trilead.ssh2.packets;

public class PacketChannelTrileadPing {
    byte[] payload;
    public int recipientChannelID;

    public PacketChannelTrileadPing(int recipientChannelID) {
        this.recipientChannelID = recipientChannelID;
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(98);
            tw.writeUINT32(this.recipientChannelID);
            tw.writeString("trilead-ping");
            tw.writeBoolean(true);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
