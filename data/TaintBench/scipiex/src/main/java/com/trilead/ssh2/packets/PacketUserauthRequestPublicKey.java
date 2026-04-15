package com.trilead.ssh2.packets;

import java.io.IOException;

public class PacketUserauthRequestPublicKey {
    String password;
    byte[] payload;
    byte[] pk;
    String pkAlgoName;
    String serviceName;
    byte[] sig;
    String userName;

    public PacketUserauthRequestPublicKey(String serviceName, String user, String pkAlgorithmName, byte[] pk, byte[] sig) {
        this.serviceName = serviceName;
        this.userName = user;
        this.pkAlgoName = pkAlgorithmName;
        this.pk = pk;
        this.sig = sig;
    }

    public PacketUserauthRequestPublicKey(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        int packet_type = new TypesReader(payload, off, len).readByte();
        if (packet_type != 50) {
            throw new IOException("This is not a SSH_MSG_USERAUTH_REQUEST! (" + packet_type + ")");
        }
        throw new IOException("Not implemented!");
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(50);
            tw.writeString(this.userName);
            tw.writeString(this.serviceName);
            tw.writeString("publickey");
            tw.writeBoolean(true);
            tw.writeString(this.pkAlgoName);
            tw.writeString(this.pk, 0, this.pk.length);
            tw.writeString(this.sig, 0, this.sig.length);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
