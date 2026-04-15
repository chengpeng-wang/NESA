package com.trilead.ssh2.packets;

public class PacketSessionStartShell {
    byte[] payload;
    public int recipientChannelID;
    public boolean wantReply;

    public PacketSessionStartShell(int recipientChannelID, boolean wantReply) {
        this.recipientChannelID = recipientChannelID;
        this.wantReply = wantReply;
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(98);
            tw.writeUINT32(this.recipientChannelID);
            tw.writeString("shell");
            tw.writeBoolean(this.wantReply);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
