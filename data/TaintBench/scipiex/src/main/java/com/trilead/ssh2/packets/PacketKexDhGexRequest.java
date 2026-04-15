package com.trilead.ssh2.packets;

import com.trilead.ssh2.DHGexParameters;

public class PacketKexDhGexRequest {
    int max;
    int min;
    int n;
    byte[] payload;

    public PacketKexDhGexRequest(DHGexParameters para) {
        this.min = para.getMin_group_len();
        this.n = para.getPref_group_len();
        this.max = para.getMax_group_len();
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(34);
            tw.writeUINT32(this.min);
            tw.writeUINT32(this.n);
            tw.writeUINT32(this.max);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
