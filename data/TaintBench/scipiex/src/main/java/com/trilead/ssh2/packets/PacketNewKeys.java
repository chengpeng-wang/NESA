package com.trilead.ssh2.packets;

import java.io.IOException;

public class PacketNewKeys {
    byte[] payload;

    public PacketNewKeys(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != 21) {
            throw new IOException("This is not a SSH_MSG_NEWKEYS! (" + packet_type + ")");
        } else if (tr.remain() != 0) {
            throw new IOException("Padding in SSH_MSG_NEWKEYS packet!");
        }
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(21);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
