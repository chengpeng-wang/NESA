package com.trilead.ssh2.packets;

import java.io.IOException;

public class PacketServiceAccept {
    byte[] payload;
    String serviceName;

    public PacketServiceAccept(String serviceName) {
        this.serviceName = serviceName;
    }

    public PacketServiceAccept(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != 6) {
            throw new IOException("This is not a SSH_MSG_SERVICE_ACCEPT! (" + packet_type + ")");
        }
        if (tr.remain() > 0) {
            this.serviceName = tr.readString();
        } else {
            this.serviceName = "";
        }
        if (tr.remain() != 0) {
            throw new IOException("Padding in SSH_MSG_SERVICE_ACCEPT packet!");
        }
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(6);
            tw.writeString(this.serviceName);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
