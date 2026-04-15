package com.trilead.ssh2.packets;

public class PacketSessionPtyRequest {
    public int character_height;
    public int character_width;
    byte[] payload;
    public int pixel_height;
    public int pixel_width;
    public int recipientChannelID;
    public String term;
    public byte[] terminal_modes;
    public boolean wantReply;

    public PacketSessionPtyRequest(int recipientChannelID, boolean wantReply, String term, int character_width, int character_height, int pixel_width, int pixel_height, byte[] terminal_modes) {
        this.recipientChannelID = recipientChannelID;
        this.wantReply = wantReply;
        this.term = term;
        this.character_width = character_width;
        this.character_height = character_height;
        this.pixel_width = pixel_width;
        this.pixel_height = pixel_height;
        this.terminal_modes = terminal_modes;
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(98);
            tw.writeUINT32(this.recipientChannelID);
            tw.writeString("pty-req");
            tw.writeBoolean(this.wantReply);
            tw.writeString(this.term);
            tw.writeUINT32(this.character_width);
            tw.writeUINT32(this.character_height);
            tw.writeUINT32(this.pixel_width);
            tw.writeUINT32(this.pixel_height);
            tw.writeString(this.terminal_modes, 0, this.terminal_modes.length);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
