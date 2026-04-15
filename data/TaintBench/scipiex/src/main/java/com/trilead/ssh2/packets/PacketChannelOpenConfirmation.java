package com.trilead.ssh2.packets;

import java.io.IOException;

public class PacketChannelOpenConfirmation {
    public int initialWindowSize;
    public int maxPacketSize;
    byte[] payload;
    public int recipientChannelID;
    public int senderChannelID;

    public PacketChannelOpenConfirmation(int recipientChannelID, int senderChannelID, int initialWindowSize, int maxPacketSize) {
        this.recipientChannelID = recipientChannelID;
        this.senderChannelID = senderChannelID;
        this.initialWindowSize = initialWindowSize;
        this.maxPacketSize = maxPacketSize;
    }

    public PacketChannelOpenConfirmation(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != 91) {
            throw new IOException("This is not a SSH_MSG_CHANNEL_OPEN_CONFIRMATION! (" + packet_type + ")");
        }
        this.recipientChannelID = tr.readUINT32();
        this.senderChannelID = tr.readUINT32();
        this.initialWindowSize = tr.readUINT32();
        this.maxPacketSize = tr.readUINT32();
        if (tr.remain() != 0) {
            throw new IOException("Padding in SSH_MSG_CHANNEL_OPEN_CONFIRMATION packet!");
        }
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(91);
            tw.writeUINT32(this.recipientChannelID);
            tw.writeUINT32(this.senderChannelID);
            tw.writeUINT32(this.initialWindowSize);
            tw.writeUINT32(this.maxPacketSize);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
