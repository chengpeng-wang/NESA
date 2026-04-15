package com.trilead.ssh2.packets;

import com.trilead.ssh2.crypto.CryptoWishList;
import com.trilead.ssh2.transport.KexParameters;
import java.io.IOException;
import java.security.SecureRandom;

public class PacketKexInit {
    KexParameters kp;
    byte[] payload;

    public PacketKexInit(CryptoWishList cwl, SecureRandom rnd) {
        this.kp = new KexParameters();
        this.kp.cookie = new byte[16];
        rnd.nextBytes(this.kp.cookie);
        this.kp.kex_algorithms = cwl.kexAlgorithms;
        this.kp.server_host_key_algorithms = cwl.serverHostKeyAlgorithms;
        this.kp.encryption_algorithms_client_to_server = cwl.c2s_enc_algos;
        this.kp.encryption_algorithms_server_to_client = cwl.s2c_enc_algos;
        this.kp.mac_algorithms_client_to_server = cwl.c2s_mac_algos;
        this.kp.mac_algorithms_server_to_client = cwl.s2c_mac_algos;
        String[] strArr = new String[]{"none"};
        this.kp.compression_algorithms_client_to_server = strArr;
        strArr = new String[]{"none"};
        this.kp.compression_algorithms_server_to_client = strArr;
        this.kp.languages_client_to_server = new String[0];
        this.kp.languages_server_to_client = new String[0];
        this.kp.first_kex_packet_follows = false;
        this.kp.reserved_field1 = 0;
    }

    public PacketKexInit(byte[] payload, int off, int len) throws IOException {
        this.kp = new KexParameters();
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != 20) {
            throw new IOException("This is not a KexInitPacket! (" + packet_type + ")");
        }
        this.kp.cookie = tr.readBytes(16);
        this.kp.kex_algorithms = tr.readNameList();
        this.kp.server_host_key_algorithms = tr.readNameList();
        this.kp.encryption_algorithms_client_to_server = tr.readNameList();
        this.kp.encryption_algorithms_server_to_client = tr.readNameList();
        this.kp.mac_algorithms_client_to_server = tr.readNameList();
        this.kp.mac_algorithms_server_to_client = tr.readNameList();
        this.kp.compression_algorithms_client_to_server = tr.readNameList();
        this.kp.compression_algorithms_server_to_client = tr.readNameList();
        this.kp.languages_client_to_server = tr.readNameList();
        this.kp.languages_server_to_client = tr.readNameList();
        this.kp.first_kex_packet_follows = tr.readBoolean();
        this.kp.reserved_field1 = tr.readUINT32();
        if (tr.remain() != 0) {
            throw new IOException("Padding in KexInitPacket!");
        }
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(20);
            tw.writeBytes(this.kp.cookie, 0, 16);
            tw.writeNameList(this.kp.kex_algorithms);
            tw.writeNameList(this.kp.server_host_key_algorithms);
            tw.writeNameList(this.kp.encryption_algorithms_client_to_server);
            tw.writeNameList(this.kp.encryption_algorithms_server_to_client);
            tw.writeNameList(this.kp.mac_algorithms_client_to_server);
            tw.writeNameList(this.kp.mac_algorithms_server_to_client);
            tw.writeNameList(this.kp.compression_algorithms_client_to_server);
            tw.writeNameList(this.kp.compression_algorithms_server_to_client);
            tw.writeNameList(this.kp.languages_client_to_server);
            tw.writeNameList(this.kp.languages_server_to_client);
            tw.writeBoolean(this.kp.first_kex_packet_follows);
            tw.writeUINT32(this.kp.reserved_field1);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }

    public KexParameters getKexParameters() {
        return this.kp;
    }

    public String[] getCompression_algorithms_client_to_server() {
        return this.kp.compression_algorithms_client_to_server;
    }

    public String[] getCompression_algorithms_server_to_client() {
        return this.kp.compression_algorithms_server_to_client;
    }

    public byte[] getCookie() {
        return this.kp.cookie;
    }

    public String[] getEncryption_algorithms_client_to_server() {
        return this.kp.encryption_algorithms_client_to_server;
    }

    public String[] getEncryption_algorithms_server_to_client() {
        return this.kp.encryption_algorithms_server_to_client;
    }

    public boolean isFirst_kex_packet_follows() {
        return this.kp.first_kex_packet_follows;
    }

    public String[] getKex_algorithms() {
        return this.kp.kex_algorithms;
    }

    public String[] getLanguages_client_to_server() {
        return this.kp.languages_client_to_server;
    }

    public String[] getLanguages_server_to_client() {
        return this.kp.languages_server_to_client;
    }

    public String[] getMac_algorithms_client_to_server() {
        return this.kp.mac_algorithms_client_to_server;
    }

    public String[] getMac_algorithms_server_to_client() {
        return this.kp.mac_algorithms_server_to_client;
    }

    public int getReserved_field1() {
        return this.kp.reserved_field1;
    }

    public String[] getServer_host_key_algorithms() {
        return this.kp.server_host_key_algorithms;
    }
}
