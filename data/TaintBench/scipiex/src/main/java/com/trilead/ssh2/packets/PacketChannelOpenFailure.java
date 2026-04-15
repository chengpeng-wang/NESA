package com.trilead.ssh2.packets;

import java.io.IOException;

public class PacketChannelOpenFailure {
    public String description;
    public String languageTag;
    byte[] payload;
    public int reasonCode;
    public int recipientChannelID;

    public PacketChannelOpenFailure(int recipientChannelID, int reasonCode, String description, String languageTag) {
        this.recipientChannelID = recipientChannelID;
        this.reasonCode = reasonCode;
        this.description = description;
        this.languageTag = languageTag;
    }

    public PacketChannelOpenFailure(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != 92) {
            throw new IOException("This is not a SSH_MSG_CHANNEL_OPEN_FAILURE! (" + packet_type + ")");
        }
        this.recipientChannelID = tr.readUINT32();
        this.reasonCode = tr.readUINT32();
        this.description = tr.readString();
        this.languageTag = tr.readString();
        if (tr.remain() != 0) {
            throw new IOException("Padding in SSH_MSG_CHANNEL_OPEN_FAILURE packet!");
        }
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(92);
            tw.writeUINT32(this.recipientChannelID);
            tw.writeUINT32(this.reasonCode);
            tw.writeString(this.description);
            tw.writeString(this.languageTag);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
