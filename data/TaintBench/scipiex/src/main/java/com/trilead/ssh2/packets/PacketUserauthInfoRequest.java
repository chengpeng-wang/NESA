package com.trilead.ssh2.packets;

import java.io.IOException;

public class PacketUserauthInfoRequest {
    boolean[] echo;
    String instruction;
    String languageTag;
    String name;
    int numPrompts;
    byte[] payload;
    String[] prompt;

    public PacketUserauthInfoRequest(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != 60) {
            throw new IOException("This is not a SSH_MSG_USERAUTH_INFO_REQUEST! (" + packet_type + ")");
        }
        this.name = tr.readString();
        this.instruction = tr.readString();
        this.languageTag = tr.readString();
        this.numPrompts = tr.readUINT32();
        this.prompt = new String[this.numPrompts];
        this.echo = new boolean[this.numPrompts];
        for (int i = 0; i < this.numPrompts; i++) {
            this.prompt[i] = tr.readString();
            this.echo[i] = tr.readBoolean();
        }
        if (tr.remain() != 0) {
            throw new IOException("Padding in SSH_MSG_USERAUTH_INFO_REQUEST packet!");
        }
    }

    public boolean[] getEcho() {
        return this.echo;
    }

    public String getInstruction() {
        return this.instruction;
    }

    public String getLanguageTag() {
        return this.languageTag;
    }

    public String getName() {
        return this.name;
    }

    public int getNumPrompts() {
        return this.numPrompts;
    }

    public String[] getPrompt() {
        return this.prompt;
    }
}
