package com.trilead.ssh2.transport;

import com.trilead.ssh2.crypto.cipher.BlockCipher;
import com.trilead.ssh2.crypto.cipher.CipherInputStream;
import com.trilead.ssh2.crypto.cipher.CipherOutputStream;
import com.trilead.ssh2.crypto.cipher.NullCipher;
import com.trilead.ssh2.crypto.digest.MAC;
import com.trilead.ssh2.log.Logger;
import com.trilead.ssh2.packets.Packets;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

public class TransportConnection {
    private static final Logger log = Logger.getLogger(TransportConnection.class);
    CipherInputStream cis;
    CipherOutputStream cos;
    ClientServerHello csh;
    MAC recv_mac;
    byte[] recv_mac_buffer;
    byte[] recv_mac_buffer_cmp;
    final byte[] recv_packet_header_buffer = new byte[5];
    boolean recv_packet_header_present = false;
    int recv_padd_blocksize = 8;
    final byte[] recv_padding_buffer = new byte[256];
    int recv_seq_number = 0;
    final SecureRandom rnd;
    MAC send_mac;
    byte[] send_mac_buffer;
    final byte[] send_packet_header_buffer = new byte[5];
    int send_padd_blocksize = 8;
    final byte[] send_padding_buffer = new byte[256];
    int send_seq_number = 0;
    boolean useRandomPadding = false;

    public TransportConnection(InputStream is, OutputStream os, SecureRandom rnd) {
        this.cis = new CipherInputStream(new NullCipher(), is);
        this.cos = new CipherOutputStream(new NullCipher(), os);
        this.rnd = rnd;
    }

    public void changeRecvCipher(BlockCipher bc, MAC mac) {
        byte[] bArr = null;
        this.cis.changeCipher(bc);
        this.recv_mac = mac;
        this.recv_mac_buffer = mac != null ? new byte[mac.size()] : null;
        if (mac != null) {
            bArr = new byte[mac.size()];
        }
        this.recv_mac_buffer_cmp = bArr;
        this.recv_padd_blocksize = bc.getBlockSize();
        if (this.recv_padd_blocksize < 8) {
            this.recv_padd_blocksize = 8;
        }
    }

    public void changeSendCipher(BlockCipher bc, MAC mac) {
        if (!(bc instanceof NullCipher)) {
            this.useRandomPadding = true;
        }
        this.cos.changeCipher(bc);
        this.send_mac = mac;
        this.send_mac_buffer = mac != null ? new byte[mac.size()] : null;
        this.send_padd_blocksize = bc.getBlockSize();
        if (this.send_padd_blocksize < 8) {
            this.send_padd_blocksize = 8;
        }
    }

    public void sendMessage(byte[] message) throws IOException {
        sendMessage(message, 0, message.length, 0);
    }

    public void sendMessage(byte[] message, int off, int len) throws IOException {
        sendMessage(message, off, len, 0);
    }

    public int getPacketOverheadEstimate() {
        return ((this.send_padd_blocksize - 1) + 9) + this.send_mac_buffer.length;
    }

    public void sendMessage(byte[] message, int off, int len, int padd) throws IOException {
        if (padd < 4) {
            padd = 4;
        } else if (padd > 64) {
            padd = 64;
        }
        int packet_len = (len + 5) + padd;
        int slack = packet_len % this.send_padd_blocksize;
        if (slack != 0) {
            packet_len += this.send_padd_blocksize - slack;
        }
        if (packet_len < 16) {
            packet_len = 16;
        }
        int padd_len = packet_len - (len + 5);
        int i;
        if (this.useRandomPadding) {
            for (i = 0; i < padd_len; i += 4) {
                int r = this.rnd.nextInt();
                this.send_padding_buffer[i] = (byte) r;
                this.send_padding_buffer[i + 1] = (byte) (r >> 8);
                this.send_padding_buffer[i + 2] = (byte) (r >> 16);
                this.send_padding_buffer[i + 3] = (byte) (r >> 24);
            }
        } else {
            for (i = 0; i < padd_len; i++) {
                this.send_padding_buffer[i] = (byte) 0;
            }
        }
        this.send_packet_header_buffer[0] = (byte) ((packet_len - 4) >> 24);
        this.send_packet_header_buffer[1] = (byte) ((packet_len - 4) >> 16);
        this.send_packet_header_buffer[2] = (byte) ((packet_len - 4) >> 8);
        this.send_packet_header_buffer[3] = (byte) (packet_len - 4);
        this.send_packet_header_buffer[4] = (byte) padd_len;
        this.cos.write(this.send_packet_header_buffer, 0, 5);
        this.cos.write(message, off, len);
        this.cos.write(this.send_padding_buffer, 0, padd_len);
        if (this.send_mac != null) {
            this.send_mac.initMac(this.send_seq_number);
            this.send_mac.update(this.send_packet_header_buffer, 0, 5);
            this.send_mac.update(message, off, len);
            this.send_mac.update(this.send_padding_buffer, 0, padd_len);
            this.send_mac.getMac(this.send_mac_buffer, 0);
            this.cos.writePlain(this.send_mac_buffer, 0, this.send_mac_buffer.length);
        }
        this.cos.flush();
        if (log.isEnabled()) {
            log.log(90, "Sent " + Packets.getMessageName(message[off] & 255) + " " + len + " bytes payload");
        }
        this.send_seq_number++;
    }

    public int peekNextMessageLength() throws IOException {
        if (!this.recv_packet_header_present) {
            this.cis.read(this.recv_packet_header_buffer, 0, 5);
            this.recv_packet_header_present = true;
        }
        int packet_length = ((((this.recv_packet_header_buffer[0] & 255) << 24) | ((this.recv_packet_header_buffer[1] & 255) << 16)) | ((this.recv_packet_header_buffer[2] & 255) << 8)) | (this.recv_packet_header_buffer[3] & 255);
        int padding_length = this.recv_packet_header_buffer[4] & 255;
        if (packet_length > 35000 || packet_length < 12) {
            throw new IOException("Illegal packet size! (" + packet_length + ")");
        }
        int payload_length = (packet_length - padding_length) - 1;
        if (payload_length >= 0) {
            return payload_length;
        }
        throw new IOException("Illegal padding_length in packet from remote (" + padding_length + ")");
    }

    public int receiveMessage(byte[] buffer, int off, int len) throws IOException {
        if (this.recv_packet_header_present) {
            this.recv_packet_header_present = false;
        } else {
            this.cis.read(this.recv_packet_header_buffer, 0, 5);
        }
        int packet_length = ((((this.recv_packet_header_buffer[0] & 255) << 24) | ((this.recv_packet_header_buffer[1] & 255) << 16)) | ((this.recv_packet_header_buffer[2] & 255) << 8)) | (this.recv_packet_header_buffer[3] & 255);
        int padding_length = this.recv_packet_header_buffer[4] & 255;
        if (packet_length > 35000 || packet_length < 12) {
            throw new IOException("Illegal packet size! (" + packet_length + ")");
        }
        int payload_length = (packet_length - padding_length) - 1;
        if (payload_length < 0) {
            throw new IOException("Illegal padding_length in packet from remote (" + padding_length + ")");
        } else if (payload_length >= len) {
            throw new IOException("Receive buffer too small (" + len + ", need " + payload_length + ")");
        } else {
            this.cis.read(buffer, off, payload_length);
            this.cis.read(this.recv_padding_buffer, 0, padding_length);
            if (this.recv_mac != null) {
                this.cis.readPlain(this.recv_mac_buffer, 0, this.recv_mac_buffer.length);
                this.recv_mac.initMac(this.recv_seq_number);
                this.recv_mac.update(this.recv_packet_header_buffer, 0, 5);
                this.recv_mac.update(buffer, off, payload_length);
                this.recv_mac.update(this.recv_padding_buffer, 0, padding_length);
                this.recv_mac.getMac(this.recv_mac_buffer_cmp, 0);
                for (int i = 0; i < this.recv_mac_buffer.length; i++) {
                    if (this.recv_mac_buffer[i] != this.recv_mac_buffer_cmp[i]) {
                        throw new IOException("Remote sent corrupt MAC.");
                    }
                }
            }
            this.recv_seq_number++;
            if (log.isEnabled()) {
                log.log(90, "Received " + Packets.getMessageName(buffer[off] & 255) + " " + payload_length + " bytes payload");
            }
            return payload_length;
        }
    }
}
