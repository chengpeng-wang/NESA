package com.trilead.ssh2.packets;

public class PacketUserauthInfoResponse {
    byte[] payload;
    String[] responses;

    public PacketUserauthInfoResponse(String[] responses) {
        this.responses = responses;
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(61);
            tw.writeUINT32(this.responses.length);
            for (String writeString : this.responses) {
                tw.writeString(writeString);
            }
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
