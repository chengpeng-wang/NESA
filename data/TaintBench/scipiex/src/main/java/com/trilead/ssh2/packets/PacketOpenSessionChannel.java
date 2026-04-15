package com.trilead.ssh2.packets;

import java.io.IOException;

public class PacketOpenSessionChannel {
    int channelID;
    int initialWindowSize;
    int maxPacketSize;
    byte[] payload;

    public PacketOpenSessionChannel(int channelID, int initialWindowSize, int maxPacketSize) {
        this.channelID = channelID;
        this.initialWindowSize = initialWindowSize;
        this.maxPacketSize = maxPacketSize;
    }

    public PacketOpenSessionChannel(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload);
        int packet_type = tr.readByte();
        if (packet_type != 90) {
            throw new IOException("This is not a SSH_MSG_CHANNEL_OPEN! (" + packet_type + ")");
        }
        this.channelID = tr.readUINT32();
        this.initialWindowSize = tr.readUINT32();
        this.maxPacketSize = tr.readUINT32();
        if (tr.remain() != 0) {
            throw new IOException("Padding in SSH_MSG_CHANNEL_OPEN packet!");
        }
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(90);
            tw.writeString("session");
            tw.writeUINT32(this.channelID);
            tw.writeUINT32(this.initialWindowSize);
            tw.writeUINT32(this.maxPacketSize);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
