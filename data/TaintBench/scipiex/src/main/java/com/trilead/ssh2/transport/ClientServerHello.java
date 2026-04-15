package com.trilead.ssh2.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class ClientServerHello {
    String client_line = "SSH-2.0-TrileadSSH2Java_213";
    String server_line;
    String server_versioncomment;

    public static final int readLineRN(InputStream is, byte[] buffer) throws IOException {
        int pos = 0;
        boolean need10 = false;
        int len = 0;
        while (true) {
            int c = is.read();
            if (c == -1) {
                throw new IOException("Premature connection close");
            }
            int pos2 = pos + 1;
            buffer[pos] = (byte) c;
            if (c == 13) {
                need10 = true;
                pos = pos2;
            } else if (c == 10) {
                return len;
            } else {
                if (need10) {
                    throw new IOException("Malformed line sent by the server, the line does not end correctly.");
                }
                len++;
                if (pos2 >= buffer.length) {
                    throw new IOException("The server sent a too long line.");
                }
                pos = pos2;
            }
        }
    }

    public ClientServerHello(InputStream bi, OutputStream bo) throws IOException {
        bo.write((this.client_line + "\r\n").getBytes("ISO-8859-1"));
        bo.flush();
        byte[] serverVersion = new byte[512];
        for (int i = 0; i < 50; i++) {
            this.server_line = new String(serverVersion, 0, readLineRN(bi, serverVersion), "ISO-8859-1");
            if (this.server_line.startsWith("SSH-")) {
                break;
            }
        }
        if (!this.server_line.startsWith("SSH-")) {
            throw new IOException("Malformed server identification string. There was no line starting with 'SSH-' amongst the first 50 lines.");
        } else if (this.server_line.startsWith("SSH-1.99-")) {
            this.server_versioncomment = this.server_line.substring(9);
        } else if (this.server_line.startsWith("SSH-2.0-")) {
            this.server_versioncomment = this.server_line.substring(8);
        } else {
            throw new IOException("Server uses incompatible protocol, it is not SSH-2 compatible.");
        }
    }

    public byte[] getClientString() {
        try {
            return this.client_line.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            return this.client_line.getBytes();
        }
    }

    public byte[] getServerString() {
        try {
            return this.server_line.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            return this.server_line.getBytes();
        }
    }
}
