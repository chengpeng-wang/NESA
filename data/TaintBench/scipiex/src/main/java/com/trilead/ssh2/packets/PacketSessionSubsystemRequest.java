package com.trilead.ssh2.packets;

public class PacketSessionSubsystemRequest {
    byte[] payload;
    public int recipientChannelID;
    public String subsystem;
    public boolean wantReply;

    public PacketSessionSubsystemRequest(int recipientChannelID, boolean wantReply, String subsystem) {
        this.recipientChannelID = recipientChannelID;
        this.wantReply = wantReply;
        this.subsystem = subsystem;
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(98);
            tw.writeUINT32(this.recipientChannelID);
            tw.writeString("subsystem");
            tw.writeBoolean(this.wantReply);
            tw.writeString(this.subsystem);
            this.payload = tw.getBytes();
            tw.getBytes(this.payload);
        }
        return this.payload;
    }
}
