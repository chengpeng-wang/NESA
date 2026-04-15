package com.trilead.ssh2.packets;

import java.io.IOException;
import org.apache.http.protocol.HTTP;

public class PacketUserauthBanner {
    String language;
    String message;
    byte[] payload;

    public PacketUserauthBanner(String message, String language) {
        this.message = message;
        this.language = language;
    }

    public String getBanner() {
        return this.message;
    }

    public PacketUserauthBanner(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != 53) {
            throw new IOException("This is not a SSH_MSG_USERAUTH_BANNER! (" + packet_type + ")");
        }
        this.message = tr.readString(HTTP.UTF_8);
        this.language = tr.readString();
        if (tr.remain() != 0) {
            throw new IOException("Padding in SSH_MSG_USERAUTH_REQUEST packet!");
        }
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(53);
            tw.writeString(this.message);
            tw.writeString(this.language);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
