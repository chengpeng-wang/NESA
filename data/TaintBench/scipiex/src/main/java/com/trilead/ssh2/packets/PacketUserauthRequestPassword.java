package com.trilead.ssh2.packets;

import java.io.IOException;

public class PacketUserauthRequestPassword {
    String password;
    byte[] payload;
    String serviceName;
    String userName;

    public PacketUserauthRequestPassword(String serviceName, String user, String pass) {
        this.serviceName = serviceName;
        this.userName = user;
        this.password = pass;
    }

    public PacketUserauthRequestPassword(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != 50) {
            throw new IOException("This is not a SSH_MSG_USERAUTH_REQUEST! (" + packet_type + ")");
        }
        this.userName = tr.readString();
        this.serviceName = tr.readString();
        if (!tr.readString().equals("password")) {
            throw new IOException("This is not a SSH_MSG_USERAUTH_REQUEST with type password!");
        } else if (tr.remain() != 0) {
            throw new IOException("Padding in SSH_MSG_USERAUTH_REQUEST packet!");
        }
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(50);
            tw.writeString(this.userName);
            tw.writeString(this.serviceName);
            tw.writeString("password");
            tw.writeBoolean(false);
            tw.writeString(this.password);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
