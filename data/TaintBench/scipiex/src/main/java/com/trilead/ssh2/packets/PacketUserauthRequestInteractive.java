package com.trilead.ssh2.packets;

public class PacketUserauthRequestInteractive {
    byte[] payload;
    String serviceName;
    String[] submethods;
    String userName;

    public PacketUserauthRequestInteractive(String serviceName, String user, String[] submethods) {
        this.serviceName = serviceName;
        this.userName = user;
        this.submethods = submethods;
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(50);
            tw.writeString(this.userName);
            tw.writeString(this.serviceName);
            tw.writeString("keyboard-interactive");
            tw.writeString("");
            tw.writeNameList(this.submethods);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
