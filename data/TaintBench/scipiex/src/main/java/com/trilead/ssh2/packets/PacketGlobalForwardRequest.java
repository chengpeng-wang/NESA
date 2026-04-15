package com.trilead.ssh2.packets;

public class PacketGlobalForwardRequest {
    public String bindAddress;
    public int bindPort;
    byte[] payload;
    public boolean wantReply;

    public PacketGlobalForwardRequest(boolean wantReply, String bindAddress, int bindPort) {
        this.wantReply = wantReply;
        this.bindAddress = bindAddress;
        this.bindPort = bindPort;
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(80);
            tw.writeString("tcpip-forward");
            tw.writeBoolean(this.wantReply);
            tw.writeString(this.bindAddress);
            tw.writeUINT32(this.bindPort);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
