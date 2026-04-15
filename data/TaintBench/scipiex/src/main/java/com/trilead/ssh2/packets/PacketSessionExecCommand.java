package com.trilead.ssh2.packets;

public class PacketSessionExecCommand {
    public String command;
    byte[] payload;
    public int recipientChannelID;
    public boolean wantReply;

    public PacketSessionExecCommand(int recipientChannelID, boolean wantReply, String command) {
        this.recipientChannelID = recipientChannelID;
        this.wantReply = wantReply;
        this.command = command;
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(98);
            tw.writeUINT32(this.recipientChannelID);
            tw.writeString("exec");
            tw.writeBoolean(this.wantReply);
            tw.writeString(this.command);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
