package com.trilead.ssh2.channel;

import com.trilead.ssh2.log.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RemoteX11AcceptThread extends Thread {
    private static final Logger log = Logger.getLogger(RemoteX11AcceptThread.class);
    Channel c;
    String remoteOriginatorAddress;
    int remoteOriginatorPort;
    Socket s;

    public RemoteX11AcceptThread(Channel c, String remoteOriginatorAddress, int remoteOriginatorPort) {
        this.c = c;
        this.remoteOriginatorAddress = remoteOriginatorAddress;
        this.remoteOriginatorPort = remoteOriginatorPort;
    }

    public void run() {
        try {
            this.c.cm.sendOpenConfirmation(this.c);
            OutputStream remote_os = this.c.getStdinStream();
            InputStream remote_is = this.c.getStdoutStream();
            byte[] header = new byte[6];
            if (remote_is.read(header) != 6) {
                throw new IOException("Unexpected EOF on X11 startup!");
            } else if (header[0] == (byte) 66 || header[0] == (byte) 108) {
                int idxMSB = header[0] == (byte) 66 ? 0 : 1;
                byte[] auth_buff = new byte[6];
                if (remote_is.read(auth_buff) != 6) {
                    throw new IOException("Unexpected EOF on X11 startup!");
                }
                int authProtocolNameLength = ((auth_buff[idxMSB] & 255) << 8) | (auth_buff[1 - idxMSB] & 255);
                int authProtocolDataLength = ((auth_buff[idxMSB + 2] & 255) << 8) | (auth_buff[3 - idxMSB] & 255);
                if (authProtocolNameLength > 256 || authProtocolDataLength > 256) {
                    throw new IOException("Buggy X11 authorization data");
                }
                int authProtocolNamePadding = (4 - (authProtocolNameLength % 4)) % 4;
                int authProtocolDataPadding = (4 - (authProtocolDataLength % 4)) % 4;
                byte[] authProtocolName = new byte[authProtocolNameLength];
                byte[] authProtocolData = new byte[authProtocolDataLength];
                byte[] paddingBuffer = new byte[4];
                if (remote_is.read(authProtocolName) != authProtocolNameLength) {
                    throw new IOException("Unexpected EOF on X11 startup! (authProtocolName)");
                } else if (remote_is.read(paddingBuffer, 0, authProtocolNamePadding) != authProtocolNamePadding) {
                    throw new IOException("Unexpected EOF on X11 startup! (authProtocolNamePadding)");
                } else if (remote_is.read(authProtocolData) != authProtocolDataLength) {
                    throw new IOException("Unexpected EOF on X11 startup! (authProtocolData)");
                } else if (remote_is.read(paddingBuffer, 0, authProtocolDataPadding) != authProtocolDataPadding) {
                    throw new IOException("Unexpected EOF on X11 startup! (authProtocolDataPadding)");
                } else {
                    if (!"MIT-MAGIC-COOKIE-1".equals(new String(authProtocolName, "ISO-8859-1"))) {
                        throw new IOException("Unknown X11 authorization protocol!");
                    } else if (authProtocolDataLength != 16) {
                        throw new IOException("Wrong data length for X11 authorization data!");
                    } else {
                        StringBuffer stringBuffer = new StringBuffer(32);
                        for (byte b : authProtocolData) {
                            String digit2 = Integer.toHexString(b & 255);
                            if (digit2.length() != 2) {
                                digit2 = "0" + digit2;
                            }
                            stringBuffer.append(digit2);
                        }
                        String hexEncodedFakeCookie = stringBuffer.toString();
                        synchronized (this.c) {
                            this.c.hexX11FakeCookie = hexEncodedFakeCookie;
                        }
                        X11ServerData sd = this.c.cm.checkX11Cookie(hexEncodedFakeCookie);
                        if (sd == null) {
                            throw new IOException("Invalid X11 cookie received.");
                        }
                        this.s = new Socket(sd.hostname, sd.port);
                        OutputStream x11_os = this.s.getOutputStream();
                        InputStream x11_is = this.s.getInputStream();
                        x11_os.write(header);
                        if (sd.x11_magic_cookie == null) {
                            x11_os.write(new byte[6]);
                        } else if (sd.x11_magic_cookie.length != 16) {
                            throw new IOException("The real X11 cookie has an invalid length!");
                        } else {
                            x11_os.write(auth_buff);
                            x11_os.write(authProtocolName);
                            x11_os.write(paddingBuffer, 0, authProtocolNamePadding);
                            x11_os.write(sd.x11_magic_cookie);
                            x11_os.write(paddingBuffer, 0, authProtocolDataPadding);
                        }
                        x11_os.flush();
                        StreamForwarder r2l = new StreamForwarder(this.c, null, null, remote_is, x11_os, "RemoteToX11");
                        StreamForwarder l2r = new StreamForwarder(this.c, null, null, x11_is, remote_os, "X11ToRemote");
                        r2l.setDaemon(true);
                        r2l.start();
                        l2r.run();
                        while (r2l.isAlive()) {
                            try {
                                r2l.join();
                            } catch (InterruptedException e) {
                            }
                        }
                        this.c.cm.closeChannel(this.c, "EOF on both X11 streams reached.", true);
                        this.s.close();
                    }
                }
            } else {
                throw new IOException("Unknown endian format in X11 message!");
            }
        } catch (IOException e2) {
            log.log(50, "IOException in X11 proxy code: " + e2.getMessage());
            try {
                this.c.cm.closeChannel(this.c, "IOException in X11 proxy code (" + e2.getMessage() + ")", true);
            } catch (IOException e3) {
            }
            try {
                if (this.s != null) {
                    this.s.close();
                }
            } catch (IOException e4) {
            }
        }
    }
}
