package com.trilead.ssh2.packets;

import java.io.IOException;

public class PacketChannelWindowAdjust {
    byte[] payload;
    public int recipientChannelID;
    public int windowChange;

    public PacketChannelWindowAdjust(int recipientChannelID, int windowChange) {
        this.recipientChannelID = recipientChannelID;
        this.windowChange = windowChange;
    }

    public PacketChannelWindowAdjust(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != 93) {
            throw new IOException("This is not a SSH_MSG_CHANNEL_WINDOW_ADJUST! (" + packet_type + ")");
        }
        this.recipientChannelID = tr.readUINT32();
        this.windowChange = tr.readUINT32();
        if (tr.remain() != 0) {
            throw new IOException("Padding in SSH_MSG_CHANNEL_WINDOW_ADJUST packet!");
        }
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(93);
            tw.writeUINT32(this.recipientChannelID);
            tw.writeUINT32(this.windowChange);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
