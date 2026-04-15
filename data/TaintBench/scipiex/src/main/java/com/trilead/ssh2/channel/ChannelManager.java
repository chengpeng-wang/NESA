package com.trilead.ssh2.channel;

import com.trilead.ssh2.log.Logger;
import com.trilead.ssh2.packets.PacketChannelOpenConfirmation;
import com.trilead.ssh2.packets.PacketChannelTrileadPing;
import com.trilead.ssh2.packets.PacketGlobalCancelForwardRequest;
import com.trilead.ssh2.packets.PacketGlobalForwardRequest;
import com.trilead.ssh2.packets.PacketGlobalTrileadPing;
import com.trilead.ssh2.packets.PacketOpenDirectTCPIPChannel;
import com.trilead.ssh2.packets.PacketOpenSessionChannel;
import com.trilead.ssh2.packets.PacketSessionExecCommand;
import com.trilead.ssh2.packets.PacketSessionPtyRequest;
import com.trilead.ssh2.packets.PacketSessionStartShell;
import com.trilead.ssh2.packets.PacketSessionSubsystemRequest;
import com.trilead.ssh2.packets.PacketSessionX11Request;
import com.trilead.ssh2.packets.Packets;
import com.trilead.ssh2.packets.TypesReader;
import com.trilead.ssh2.transport.MessageHandler;
import com.trilead.ssh2.transport.TransportManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import org.apache.http.protocol.HTTP;

public class ChannelManager implements MessageHandler {
    private static final Logger log = Logger.getLogger(ChannelManager.class);
    private Vector channels = new Vector();
    private int globalFailedCounter = 0;
    private int globalSuccessCounter = 0;
    private Vector listenerThreads = new Vector();
    private boolean listenerThreadsAllowed = true;
    private int nextLocalChannel = 100;
    private HashMap remoteForwardings = new HashMap();
    private boolean shutdown = false;
    private TransportManager tm;
    private HashMap x11_magic_cookies = new HashMap();

    public ChannelManager(TransportManager tm) {
        this.tm = tm;
        tm.registerMessageHandler(this, 80, 100);
    }

    private Channel getChannel(int id) {
        synchronized (this.channels) {
            for (int i = 0; i < this.channels.size(); i++) {
                Channel c = (Channel) this.channels.elementAt(i);
                if (c.localID == id) {
                    return c;
                }
            }
            return null;
        }
    }

    private void removeChannel(int id) {
        synchronized (this.channels) {
            for (int i = 0; i < this.channels.size(); i++) {
                if (((Channel) this.channels.elementAt(i)).localID == id) {
                    this.channels.removeElementAt(i);
                    break;
                }
            }
        }
    }

    private int addChannel(Channel c) {
        int i;
        synchronized (this.channels) {
            this.channels.addElement(c);
            i = this.nextLocalChannel;
            this.nextLocalChannel = i + 1;
        }
        return i;
    }

    private void waitUntilChannelOpen(Channel c) throws IOException {
        synchronized (c) {
            while (c.state == 1) {
                try {
                    c.wait();
                } catch (InterruptedException e) {
                }
            }
            if (c.state != 2) {
                removeChannel(c.localID);
                String detail = c.getReasonClosed();
                if (detail == null) {
                    detail = "state: " + c.state;
                }
                throw new IOException("Could not open channel (" + detail + ")");
            }
        }
    }

    private final boolean waitForGlobalRequestResult() throws IOException {
        synchronized (this.channels) {
            while (this.globalSuccessCounter == 0 && this.globalFailedCounter == 0) {
                if (this.shutdown) {
                    throw new IOException("The connection is being shutdown");
                }
                try {
                    this.channels.wait();
                } catch (InterruptedException e) {
                }
            }
            if (this.globalFailedCounter == 0 && this.globalSuccessCounter == 1) {
                return true;
            } else if (this.globalFailedCounter == 1 && this.globalSuccessCounter == 0) {
                return false;
            } else {
                throw new IOException("Illegal state. The server sent " + this.globalSuccessCounter + " SSH_MSG_REQUEST_SUCCESS and " + this.globalFailedCounter + " SSH_MSG_REQUEST_FAILURE messages.");
            }
        }
    }

    private final boolean waitForChannelRequestResult(Channel c) throws IOException {
        synchronized (c) {
            while (c.successCounter == 0 && c.failedCounter == 0) {
                if (c.state != 2) {
                    String detail = c.getReasonClosed();
                    if (detail == null) {
                        detail = "state: " + c.state;
                    }
                    throw new IOException("This SSH2 channel is not open (" + detail + ")");
                }
                try {
                    c.wait();
                } catch (InterruptedException e) {
                }
            }
            if (c.failedCounter == 0 && c.successCounter == 1) {
                return true;
            } else if (c.failedCounter == 1 && c.successCounter == 0) {
                return false;
            } else {
                throw new IOException("Illegal state. The server sent " + c.successCounter + " SSH_MSG_CHANNEL_SUCCESS and " + c.failedCounter + " SSH_MSG_CHANNEL_FAILURE messages.");
            }
        }
    }

    public void registerX11Cookie(String hexFakeCookie, X11ServerData data) {
        synchronized (this.x11_magic_cookies) {
            this.x11_magic_cookies.put(hexFakeCookie, data);
        }
    }

    public void unRegisterX11Cookie(String hexFakeCookie, boolean killChannels) {
        if (hexFakeCookie == null) {
            throw new IllegalStateException("hexFakeCookie may not be null");
        }
        synchronized (this.x11_magic_cookies) {
            this.x11_magic_cookies.remove(hexFakeCookie);
        }
        if (killChannels) {
            Vector channel_copy;
            if (log.isEnabled()) {
                log.log(50, "Closing all X11 channels for the given fake cookie");
            }
            synchronized (this.channels) {
                channel_copy = (Vector) this.channels.clone();
            }
            for (int i = 0; i < channel_copy.size(); i++) {
                Channel c = (Channel) channel_copy.elementAt(i);
                synchronized (c) {
                    if (hexFakeCookie.equals(c.hexX11FakeCookie)) {
                        try {
                            closeChannel(c, "Closing X11 channel since the corresponding session is closing", true);
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }
    }

    public X11ServerData checkX11Cookie(String hexFakeCookie) {
        synchronized (this.x11_magic_cookies) {
            if (hexFakeCookie != null) {
                X11ServerData x11ServerData = (X11ServerData) this.x11_magic_cookies.get(hexFakeCookie);
                return x11ServerData;
            }
            return null;
        }
    }

    public void closeAllChannels() {
        Vector channel_copy;
        if (log.isEnabled()) {
            log.log(50, "Closing all channels");
        }
        synchronized (this.channels) {
            channel_copy = (Vector) this.channels.clone();
        }
        for (int i = 0; i < channel_copy.size(); i++) {
            try {
                closeChannel((Channel) channel_copy.elementAt(i), "Closing all channels", true);
            } catch (IOException e) {
            }
        }
    }

    /* JADX WARNING: Missing block: B:22:0x0051, code skipped:
            if (log.isEnabled() == false) goto L_?;
     */
    /* JADX WARNING: Missing block: B:23:0x0053, code skipped:
            log.log(50, "Sent SSH_MSG_CHANNEL_CLOSE (channel " + r6.localID + ")");
     */
    /* JADX WARNING: Missing block: B:35:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:36:?, code skipped:
            return;
     */
    public void closeChannel(com.trilead.ssh2.channel.Channel r6, java.lang.String r7, boolean r8) throws java.io.IOException {
        /*
        r5 = this;
        r1 = 5;
        r0 = new byte[r1];
        monitor-enter(r6);
        if (r8 == 0) goto L_0x000c;
    L_0x0006:
        r1 = 4;
        r6.state = r1;	 Catch:{ all -> 0x003f }
        r1 = 1;
        r6.EOF = r1;	 Catch:{ all -> 0x003f }
    L_0x000c:
        r6.setReasonClosed(r7);	 Catch:{ all -> 0x003f }
        r1 = 0;
        r2 = 97;
        r0[r1] = r2;	 Catch:{ all -> 0x003f }
        r1 = 1;
        r2 = r6.remoteID;	 Catch:{ all -> 0x003f }
        r2 = r2 >> 24;
        r2 = (byte) r2;	 Catch:{ all -> 0x003f }
        r0[r1] = r2;	 Catch:{ all -> 0x003f }
        r1 = 2;
        r2 = r6.remoteID;	 Catch:{ all -> 0x003f }
        r2 = r2 >> 16;
        r2 = (byte) r2;	 Catch:{ all -> 0x003f }
        r0[r1] = r2;	 Catch:{ all -> 0x003f }
        r1 = 3;
        r2 = r6.remoteID;	 Catch:{ all -> 0x003f }
        r2 = r2 >> 8;
        r2 = (byte) r2;	 Catch:{ all -> 0x003f }
        r0[r1] = r2;	 Catch:{ all -> 0x003f }
        r1 = 4;
        r2 = r6.remoteID;	 Catch:{ all -> 0x003f }
        r2 = (byte) r2;	 Catch:{ all -> 0x003f }
        r0[r1] = r2;	 Catch:{ all -> 0x003f }
        r6.notifyAll();	 Catch:{ all -> 0x003f }
        monitor-exit(r6);	 Catch:{ all -> 0x003f }
        r2 = r6.channelSendLock;
        monitor-enter(r2);
        r1 = r6.closeMessageSent;	 Catch:{ all -> 0x0072 }
        if (r1 == 0) goto L_0x0042;
    L_0x003d:
        monitor-exit(r2);	 Catch:{ all -> 0x0072 }
    L_0x003e:
        return;
    L_0x003f:
        r1 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x003f }
        throw r1;
    L_0x0042:
        r1 = r5.tm;	 Catch:{ all -> 0x0072 }
        r1.sendMessage(r0);	 Catch:{ all -> 0x0072 }
        r1 = 1;
        r6.closeMessageSent = r1;	 Catch:{ all -> 0x0072 }
        monitor-exit(r2);	 Catch:{ all -> 0x0072 }
        r1 = log;
        r1 = r1.isEnabled();
        if (r1 == 0) goto L_0x003e;
    L_0x0053:
        r1 = log;
        r2 = 50;
        r3 = new java.lang.StringBuilder;
        r4 = "Sent SSH_MSG_CHANNEL_CLOSE (channel ";
        r3.<init>(r4);
        r4 = r6.localID;
        r3 = r3.append(r4);
        r4 = ")";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r1.log(r2, r3);
        goto L_0x003e;
    L_0x0072:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0072 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trilead.ssh2.channel.ChannelManager.closeChannel(com.trilead.ssh2.channel.Channel, java.lang.String, boolean):void");
    }

    /* JADX WARNING: Missing block: B:8:0x002f, code skipped:
            r2 = r6.channelSendLock;
     */
    /* JADX WARNING: Missing block: B:9:0x0031, code skipped:
            monitor-enter(r2);
     */
    /* JADX WARNING: Missing block: B:12:0x0034, code skipped:
            if (r6.closeMessageSent == false) goto L_0x003e;
     */
    /* JADX WARNING: Missing block: B:13:0x0036, code skipped:
            monitor-exit(r2);
     */
    /* JADX WARNING: Missing block: B:23:?, code skipped:
            r5.tm.sendMessage(r0);
     */
    /* JADX WARNING: Missing block: B:24:0x0043, code skipped:
            monitor-exit(r2);
     */
    /* JADX WARNING: Missing block: B:26:0x004a, code skipped:
            if (log.isEnabled() == false) goto L_?;
     */
    /* JADX WARNING: Missing block: B:27:0x004c, code skipped:
            log.log(50, "Sent EOF (Channel " + r6.localID + "/" + r6.remoteID + ")");
     */
    /* JADX WARNING: Missing block: B:32:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:33:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:34:?, code skipped:
            return;
     */
    public void sendEOF(com.trilead.ssh2.channel.Channel r6) throws java.io.IOException {
        /*
        r5 = this;
        r2 = 2;
        r1 = 5;
        r0 = new byte[r1];
        monitor-enter(r6);
        r1 = r6.state;	 Catch:{ all -> 0x003b }
        if (r1 == r2) goto L_0x000b;
    L_0x0009:
        monitor-exit(r6);	 Catch:{ all -> 0x003b }
    L_0x000a:
        return;
    L_0x000b:
        r1 = 0;
        r2 = 96;
        r0[r1] = r2;	 Catch:{ all -> 0x003b }
        r1 = 1;
        r2 = r6.remoteID;	 Catch:{ all -> 0x003b }
        r2 = r2 >> 24;
        r2 = (byte) r2;	 Catch:{ all -> 0x003b }
        r0[r1] = r2;	 Catch:{ all -> 0x003b }
        r1 = 2;
        r2 = r6.remoteID;	 Catch:{ all -> 0x003b }
        r2 = r2 >> 16;
        r2 = (byte) r2;	 Catch:{ all -> 0x003b }
        r0[r1] = r2;	 Catch:{ all -> 0x003b }
        r1 = 3;
        r2 = r6.remoteID;	 Catch:{ all -> 0x003b }
        r2 = r2 >> 8;
        r2 = (byte) r2;	 Catch:{ all -> 0x003b }
        r0[r1] = r2;	 Catch:{ all -> 0x003b }
        r1 = 4;
        r2 = r6.remoteID;	 Catch:{ all -> 0x003b }
        r2 = (byte) r2;	 Catch:{ all -> 0x003b }
        r0[r1] = r2;	 Catch:{ all -> 0x003b }
        monitor-exit(r6);	 Catch:{ all -> 0x003b }
        r2 = r6.channelSendLock;
        monitor-enter(r2);
        r1 = r6.closeMessageSent;	 Catch:{ all -> 0x0038 }
        if (r1 == 0) goto L_0x003e;
    L_0x0036:
        monitor-exit(r2);	 Catch:{ all -> 0x0038 }
        goto L_0x000a;
    L_0x0038:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0038 }
        throw r1;
    L_0x003b:
        r1 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x003b }
        throw r1;
    L_0x003e:
        r1 = r5.tm;	 Catch:{ all -> 0x0038 }
        r1.sendMessage(r0);	 Catch:{ all -> 0x0038 }
        monitor-exit(r2);	 Catch:{ all -> 0x0038 }
        r1 = log;
        r1 = r1.isEnabled();
        if (r1 == 0) goto L_0x000a;
    L_0x004c:
        r1 = log;
        r2 = 50;
        r3 = new java.lang.StringBuilder;
        r4 = "Sent EOF (Channel ";
        r3.<init>(r4);
        r4 = r6.localID;
        r3 = r3.append(r4);
        r4 = "/";
        r3 = r3.append(r4);
        r4 = r6.remoteID;
        r3 = r3.append(r4);
        r4 = ")";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r1.log(r2, r3);
        goto L_0x000a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trilead.ssh2.channel.ChannelManager.sendEOF(com.trilead.ssh2.channel.Channel):void");
    }

    /* JADX WARNING: Missing block: B:9:0x001a, code skipped:
            r3 = r7.channelSendLock;
     */
    /* JADX WARNING: Missing block: B:10:0x001c, code skipped:
            monitor-enter(r3);
     */
    /* JADX WARNING: Missing block: B:13:0x001f, code skipped:
            if (r7.closeMessageSent == false) goto L_0x0027;
     */
    /* JADX WARNING: Missing block: B:14:0x0021, code skipped:
            monitor-exit(r3);
     */
    /* JADX WARNING: Missing block: B:15:0x0022, code skipped:
            r0 = r1;
     */
    /* JADX WARNING: Missing block: B:21:?, code skipped:
            r6.tm.sendMessage(r1.getPayload());
     */
    /* JADX WARNING: Missing block: B:22:0x0030, code skipped:
            monitor-exit(r3);
     */
    /* JADX WARNING: Missing block: B:23:0x0031, code skipped:
            r0 = r1;
     */
    /* JADX WARNING: Missing block: B:30:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:31:?, code skipped:
            return;
     */
    public void sendOpenConfirmation(com.trilead.ssh2.channel.Channel r7) throws java.io.IOException {
        /*
        r6 = this;
        r0 = 0;
        monitor-enter(r7);
        r2 = r7.state;	 Catch:{ all -> 0x0024 }
        r3 = 1;
        if (r2 == r3) goto L_0x0009;
    L_0x0007:
        monitor-exit(r7);	 Catch:{ all -> 0x0024 }
    L_0x0008:
        return;
    L_0x0009:
        r2 = 2;
        r7.state = r2;	 Catch:{ all -> 0x0024 }
        r1 = new com.trilead.ssh2.packets.PacketChannelOpenConfirmation;	 Catch:{ all -> 0x0024 }
        r2 = r7.remoteID;	 Catch:{ all -> 0x0024 }
        r3 = r7.localID;	 Catch:{ all -> 0x0024 }
        r4 = r7.localWindow;	 Catch:{ all -> 0x0024 }
        r5 = r7.localMaxPacketSize;	 Catch:{ all -> 0x0024 }
        r1.m58init(r2, r3, r4, r5);	 Catch:{ all -> 0x0024 }
        monitor-exit(r7);	 Catch:{ all -> 0x0036 }
        r3 = r7.channelSendLock;
        monitor-enter(r3);
        r2 = r7.closeMessageSent;	 Catch:{ all -> 0x0033 }
        if (r2 == 0) goto L_0x0027;
    L_0x0021:
        monitor-exit(r3);	 Catch:{ all -> 0x0033 }
        r0 = r1;
        goto L_0x0008;
    L_0x0024:
        r2 = move-exception;
    L_0x0025:
        monitor-exit(r7);	 Catch:{ all -> 0x0024 }
        throw r2;
    L_0x0027:
        r2 = r6.tm;	 Catch:{ all -> 0x0033 }
        r4 = r1.getPayload();	 Catch:{ all -> 0x0033 }
        r2.sendMessage(r4);	 Catch:{ all -> 0x0033 }
        monitor-exit(r3);	 Catch:{ all -> 0x0033 }
        r0 = r1;
        goto L_0x0008;
    L_0x0033:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0033 }
        throw r2;
    L_0x0036:
        r2 = move-exception;
        r0 = r1;
        goto L_0x0025;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trilead.ssh2.channel.ChannelManager.sendOpenConfirmation(com.trilead.ssh2.channel.Channel):void");
    }

    public void sendData(Channel c, byte[] buffer, int pos, int len) throws IOException {
        while (len > 0) {
            int thislen;
            byte[] msg;
            synchronized (c) {
                while (c.state != 4) {
                    if (c.state != 2) {
                        throw new IOException("SSH channel in strange state. (" + c.state + ")");
                    } else if (c.remoteWindow != 0) {
                        if (c.remoteWindow >= ((long) len)) {
                            thislen = len;
                        } else {
                            thislen = (int) c.remoteWindow;
                        }
                        int estimatedMaxDataLen = c.remoteMaxPacketSize - (this.tm.getPacketOverheadEstimate() + 9);
                        if (estimatedMaxDataLen <= 0) {
                            estimatedMaxDataLen = 1;
                        }
                        if (thislen > estimatedMaxDataLen) {
                            thislen = estimatedMaxDataLen;
                        }
                        c.remoteWindow -= (long) thislen;
                        msg = new byte[(thislen + 9)];
                        msg[0] = (byte) 94;
                        msg[1] = (byte) (c.remoteID >> 24);
                        msg[2] = (byte) (c.remoteID >> 16);
                        msg[3] = (byte) (c.remoteID >> 8);
                        msg[4] = (byte) c.remoteID;
                        msg[5] = (byte) (thislen >> 24);
                        msg[6] = (byte) (thislen >> 16);
                        msg[7] = (byte) (thislen >> 8);
                        msg[8] = (byte) thislen;
                        System.arraycopy(buffer, pos, msg, 9, thislen);
                    } else {
                        try {
                            c.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                }
                throw new IOException("SSH channel is closed. (" + c.getReasonClosed() + ")");
            }
            synchronized (c.channelSendLock) {
                if (c.closeMessageSent) {
                    throw new IOException("SSH channel is closed. (" + c.getReasonClosed() + ")");
                }
                this.tm.sendMessage(msg);
            }
            pos += thislen;
            len -= thislen;
        }
    }

    public int requestGlobalForward(String bindAddress, int bindPort, String targetAddress, int targetPort) throws IOException {
        RemoteForwardingData rfd = new RemoteForwardingData();
        rfd.bindAddress = bindAddress;
        rfd.bindPort = bindPort;
        rfd.targetAddress = targetAddress;
        rfd.targetPort = targetPort;
        synchronized (this.remoteForwardings) {
            Integer key = new Integer(bindPort);
            if (this.remoteForwardings.get(key) != null) {
                throw new IOException("There is already a forwarding for remote port " + bindPort);
            }
            this.remoteForwardings.put(key, rfd);
        }
        synchronized (this.channels) {
            this.globalFailedCounter = 0;
            this.globalSuccessCounter = 0;
        }
        this.tm.sendMessage(new PacketGlobalForwardRequest(true, bindAddress, bindPort).getPayload());
        if (log.isEnabled()) {
            log.log(50, "Requesting a remote forwarding ('" + bindAddress + "', " + bindPort + ")");
        }
        try {
            if (waitForGlobalRequestResult()) {
                return bindPort;
            }
            throw new IOException("The server denied the request (did you enable port forwarding?)");
        } catch (IOException e) {
            synchronized (this.remoteForwardings) {
                this.remoteForwardings.remove(rfd);
                throw e;
            }
        }
    }

    public void requestCancelGlobalForward(int bindPort) throws IOException {
        RemoteForwardingData rfd;
        synchronized (this.remoteForwardings) {
            rfd = (RemoteForwardingData) this.remoteForwardings.get(new Integer(bindPort));
            if (rfd == null) {
                throw new IOException("Sorry, there is no known remote forwarding for remote port " + bindPort);
            }
        }
        synchronized (this.channels) {
            this.globalFailedCounter = 0;
            this.globalSuccessCounter = 0;
        }
        this.tm.sendMessage(new PacketGlobalCancelForwardRequest(true, rfd.bindAddress, rfd.bindPort).getPayload());
        if (log.isEnabled()) {
            log.log(50, "Requesting cancelation of remote forward ('" + rfd.bindAddress + "', " + rfd.bindPort + ")");
        }
        try {
            if (waitForGlobalRequestResult()) {
                synchronized (this.remoteForwardings) {
                    this.remoteForwardings.remove(rfd);
                }
                return;
            }
            throw new IOException("The server denied the request.");
        } catch (Throwable th) {
            synchronized (this.remoteForwardings) {
                this.remoteForwardings.remove(rfd);
            }
        }
    }

    public void registerThread(IChannelWorkerThread thr) throws IOException {
        synchronized (this.listenerThreads) {
            if (this.listenerThreadsAllowed) {
                this.listenerThreads.addElement(thr);
            } else {
                throw new IOException("Too late, this connection is closed.");
            }
        }
    }

    public Channel openDirectTCPIPChannel(String host_to_connect, int port_to_connect, String originator_IP_address, int originator_port) throws IOException {
        Channel c = new Channel(this);
        synchronized (c) {
            c.localID = addChannel(c);
        }
        this.tm.sendMessage(new PacketOpenDirectTCPIPChannel(c.localID, c.localWindow, c.localMaxPacketSize, host_to_connect, port_to_connect, originator_IP_address, originator_port).getPayload());
        waitUntilChannelOpen(c);
        return c;
    }

    public Channel openSessionChannel() throws IOException {
        Channel c = new Channel(this);
        synchronized (c) {
            c.localID = addChannel(c);
        }
        if (log.isEnabled()) {
            log.log(50, "Sending SSH_MSG_CHANNEL_OPEN (Channel " + c.localID + ")");
        }
        this.tm.sendMessage(new PacketOpenSessionChannel(c.localID, c.localWindow, c.localMaxPacketSize).getPayload());
        waitUntilChannelOpen(c);
        return c;
    }

    public void requestGlobalTrileadPing() throws IOException {
        synchronized (this.channels) {
            this.globalFailedCounter = 0;
            this.globalSuccessCounter = 0;
        }
        this.tm.sendMessage(new PacketGlobalTrileadPing().getPayload());
        if (log.isEnabled()) {
            log.log(50, "Sending SSH_MSG_GLOBAL_REQUEST 'trilead-ping'.");
        }
        try {
            if (waitForGlobalRequestResult()) {
                throw new IOException("Your server is alive - but buggy. It replied with SSH_MSG_REQUEST_SUCCESS when it actually should not.");
            }
        } catch (IOException e) {
            throw ((IOException) new IOException("The ping request failed.").initCause(e));
        }
    }

    public void requestChannelTrileadPing(Channel c) throws IOException {
        PacketChannelTrileadPing pctp;
        synchronized (c) {
            if (c.state != 2) {
                throw new IOException("Cannot ping this channel (" + c.getReasonClosed() + ")");
            }
            pctp = new PacketChannelTrileadPing(c.remoteID);
            c.failedCounter = 0;
            c.successCounter = 0;
        }
        synchronized (c.channelSendLock) {
            if (c.closeMessageSent) {
                throw new IOException("Cannot ping this channel (" + c.getReasonClosed() + ")");
            }
            this.tm.sendMessage(pctp.getPayload());
        }
        try {
            if (waitForChannelRequestResult(c)) {
                throw new IOException("Your server is alive - but buggy. It replied with SSH_MSG_SESSION_SUCCESS when it actually should not.");
            }
        } catch (IOException e) {
            throw ((IOException) new IOException("The ping request failed.").initCause(e));
        }
    }

    public void requestPTY(Channel c, String term, int term_width_characters, int term_height_characters, int term_width_pixels, int term_height_pixels, byte[] terminal_modes) throws IOException {
        PacketSessionPtyRequest spr;
        synchronized (c) {
            if (c.state != 2) {
                throw new IOException("Cannot request PTY on this channel (" + c.getReasonClosed() + ")");
            }
            spr = new PacketSessionPtyRequest(c.remoteID, true, term, term_width_characters, term_height_characters, term_width_pixels, term_height_pixels, terminal_modes);
            c.failedCounter = 0;
            c.successCounter = 0;
        }
        synchronized (c.channelSendLock) {
            if (c.closeMessageSent) {
                throw new IOException("Cannot request PTY on this channel (" + c.getReasonClosed() + ")");
            }
            this.tm.sendMessage(spr.getPayload());
        }
        try {
            if (!waitForChannelRequestResult(c)) {
                throw new IOException("The server denied the request.");
            }
        } catch (IOException e) {
            throw ((IOException) new IOException("PTY request failed").initCause(e));
        }
    }

    public void requestX11(Channel c, boolean singleConnection, String x11AuthenticationProtocol, String x11AuthenticationCookie, int x11ScreenNumber) throws IOException {
        PacketSessionX11Request psr;
        synchronized (c) {
            if (c.state != 2) {
                throw new IOException("Cannot request X11 on this channel (" + c.getReasonClosed() + ")");
            }
            psr = new PacketSessionX11Request(c.remoteID, true, singleConnection, x11AuthenticationProtocol, x11AuthenticationCookie, x11ScreenNumber);
            c.failedCounter = 0;
            c.successCounter = 0;
        }
        synchronized (c.channelSendLock) {
            if (c.closeMessageSent) {
                throw new IOException("Cannot request X11 on this channel (" + c.getReasonClosed() + ")");
            }
            this.tm.sendMessage(psr.getPayload());
        }
        if (log.isEnabled()) {
            log.log(50, "Requesting X11 forwarding (Channel " + c.localID + "/" + c.remoteID + ")");
        }
        try {
            if (!waitForChannelRequestResult(c)) {
                throw new IOException("The server denied the request.");
            }
        } catch (IOException e) {
            throw ((IOException) new IOException("The X11 request failed.").initCause(e));
        }
    }

    public void requestSubSystem(Channel c, String subSystemName) throws IOException {
        PacketSessionSubsystemRequest ssr;
        synchronized (c) {
            if (c.state != 2) {
                throw new IOException("Cannot request subsystem on this channel (" + c.getReasonClosed() + ")");
            }
            ssr = new PacketSessionSubsystemRequest(c.remoteID, true, subSystemName);
            c.failedCounter = 0;
            c.successCounter = 0;
        }
        synchronized (c.channelSendLock) {
            if (c.closeMessageSent) {
                throw new IOException("Cannot request subsystem on this channel (" + c.getReasonClosed() + ")");
            }
            this.tm.sendMessage(ssr.getPayload());
        }
        try {
            if (!waitForChannelRequestResult(c)) {
                throw new IOException("The server denied the request.");
            }
        } catch (IOException e) {
            throw ((IOException) new IOException("The subsystem request failed.").initCause(e));
        }
    }

    public void requestExecCommand(Channel c, String cmd) throws IOException {
        PacketSessionExecCommand sm;
        synchronized (c) {
            if (c.state != 2) {
                throw new IOException("Cannot execute command on this channel (" + c.getReasonClosed() + ")");
            }
            sm = new PacketSessionExecCommand(c.remoteID, true, cmd);
            c.failedCounter = 0;
            c.successCounter = 0;
        }
        synchronized (c.channelSendLock) {
            if (c.closeMessageSent) {
                throw new IOException("Cannot execute command on this channel (" + c.getReasonClosed() + ")");
            }
            this.tm.sendMessage(sm.getPayload());
        }
        if (log.isEnabled()) {
            log.log(50, "Executing command (channel " + c.localID + ", '" + cmd + "')");
        }
        try {
            if (!waitForChannelRequestResult(c)) {
                throw new IOException("The server denied the request.");
            }
        } catch (IOException e) {
            throw ((IOException) new IOException("The execute request failed.").initCause(e));
        }
    }

    public void requestShell(Channel c) throws IOException {
        PacketSessionStartShell sm;
        synchronized (c) {
            if (c.state != 2) {
                throw new IOException("Cannot start shell on this channel (" + c.getReasonClosed() + ")");
            }
            sm = new PacketSessionStartShell(c.remoteID, true);
            c.failedCounter = 0;
            c.successCounter = 0;
        }
        synchronized (c.channelSendLock) {
            if (c.closeMessageSent) {
                throw new IOException("Cannot start shell on this channel (" + c.getReasonClosed() + ")");
            }
            this.tm.sendMessage(sm.getPayload());
        }
        try {
            if (!waitForChannelRequestResult(c)) {
                throw new IOException("The server denied the request.");
            }
        } catch (IOException e) {
            throw ((IOException) new IOException("The shell request failed.").initCause(e));
        }
    }

    public void msgChannelExtendedData(byte[] msg, int msglen) throws IOException {
        if (msglen <= 13) {
            throw new IOException("SSH_MSG_CHANNEL_EXTENDED_DATA message has wrong size (" + msglen + ")");
        }
        int id = ((((msg[1] & 255) << 24) | ((msg[2] & 255) << 16)) | ((msg[3] & 255) << 8)) | (msg[4] & 255);
        int dataType = ((((msg[5] & 255) << 24) | ((msg[6] & 255) << 16)) | ((msg[7] & 255) << 8)) | (msg[8] & 255);
        int len = ((((msg[9] & 255) << 24) | ((msg[10] & 255) << 16)) | ((msg[11] & 255) << 8)) | (msg[12] & 255);
        Channel c = getChannel(id);
        if (c == null) {
            throw new IOException("Unexpected SSH_MSG_CHANNEL_EXTENDED_DATA message for non-existent channel " + id);
        } else if (dataType != 1) {
            throw new IOException("SSH_MSG_CHANNEL_EXTENDED_DATA message has unknown type (" + dataType + ")");
        } else if (len != msglen - 13) {
            throw new IOException("SSH_MSG_CHANNEL_EXTENDED_DATA message has wrong len (calculated " + (msglen - 13) + ", got " + len + ")");
        } else {
            if (log.isEnabled()) {
                log.log(80, "Got SSH_MSG_CHANNEL_EXTENDED_DATA (channel " + id + ", " + len + ")");
            }
            synchronized (c) {
                if (c.state == 4) {
                } else if (c.state != 2) {
                    throw new IOException("Got SSH_MSG_CHANNEL_EXTENDED_DATA, but channel is not in correct state (" + c.state + ")");
                } else if (c.localWindow < len) {
                    throw new IOException("Remote sent too much data, does not fit into window.");
                } else {
                    c.localWindow -= len;
                    System.arraycopy(msg, 13, c.stderrBuffer, c.stderrWritepos, len);
                    c.stderrWritepos += len;
                    c.notifyAll();
                }
            }
        }
    }

    public int waitForCondition(Channel c, long timeout, int condition_mask) {
        long end_time = 0;
        boolean end_time_set = false;
        synchronized (c) {
            while (true) {
                int current_cond = 0;
                int stderrAvail = c.stderrWritepos - c.stderrReadpos;
                if (c.stdoutWritepos - c.stdoutReadpos > 0) {
                    current_cond = 0 | 4;
                }
                if (stderrAvail > 0) {
                    current_cond |= 8;
                }
                if (c.EOF) {
                    current_cond |= 16;
                }
                if (c.getExitStatus() != null) {
                    current_cond |= 32;
                }
                if (c.getExitSignal() != null) {
                    current_cond |= 64;
                }
                int i;
                if (c.state == 4) {
                    i = (current_cond | 2) | 16;
                    return i;
                } else if ((current_cond & condition_mask) != 0) {
                    return current_cond;
                } else {
                    if (timeout > 0) {
                        if (end_time_set) {
                            timeout = end_time - System.currentTimeMillis();
                            if (timeout <= 0) {
                                i = current_cond | 1;
                                return i;
                            }
                        }
                        end_time = System.currentTimeMillis() + timeout;
                        end_time_set = true;
                    }
                    if (timeout > 0) {
                        try {
                            c.wait(timeout);
                        } catch (InterruptedException e) {
                        }
                    } else {
                        c.wait();
                    }
                }
            }
        }
    }

    public int getAvailable(Channel c, boolean extended) throws IOException {
        int avail;
        synchronized (c) {
            if (extended) {
                avail = c.stderrWritepos - c.stderrReadpos;
            } else {
                avail = c.stdoutWritepos - c.stdoutReadpos;
            }
            if (avail <= 0) {
                avail = c.EOF ? -1 : 0;
            }
        }
        return avail;
    }

    /* JADX WARNING: Missing block: B:51:0x0120, code skipped:
            if (r3 <= 0) goto L_0x0199;
     */
    /* JADX WARNING: Missing block: B:53:0x0128, code skipped:
            if (log.isEnabled() == false) goto L_0x0150;
     */
    /* JADX WARNING: Missing block: B:54:0x012a, code skipped:
            log.log(80, "Sending SSH_MSG_CHANNEL_WINDOW_ADJUST (channel " + r4 + ", " + r3 + ")");
     */
    /* JADX WARNING: Missing block: B:55:0x0150, code skipped:
            r11 = r17.channelSendLock;
     */
    /* JADX WARNING: Missing block: B:56:0x0154, code skipped:
            monitor-enter(r11);
     */
    /* JADX WARNING: Missing block: B:58:?, code skipped:
            r6 = r17.msgWindowAdjust;
            r6[0] = (byte) 93;
            r6[1] = (byte) (r7 >> 24);
            r6[2] = (byte) (r7 >> 16);
            r6[3] = (byte) (r7 >> 8);
            r6[4] = (byte) r7;
            r6[5] = (byte) (r3 >> 24);
            r6[6] = (byte) (r3 >> 16);
            r6[7] = (byte) (r3 >> 8);
            r6[8] = (byte) r3;
     */
    /* JADX WARNING: Missing block: B:59:0x018f, code skipped:
            if (r17.closeMessageSent != false) goto L_0x0198;
     */
    /* JADX WARNING: Missing block: B:60:0x0191, code skipped:
            r16.tm.sendMessage(r6);
     */
    /* JADX WARNING: Missing block: B:61:0x0198, code skipped:
            monitor-exit(r11);
     */
    /* JADX WARNING: Missing block: B:74:?, code skipped:
            return -1;
     */
    /* JADX WARNING: Missing block: B:75:?, code skipped:
            return r2;
     */
    public int getChannelData(com.trilead.ssh2.channel.Channel r17, boolean r18, byte[] r19, int r20, int r21) throws java.io.IOException {
        /*
        r16 = this;
        r2 = 0;
        r3 = 0;
        r7 = 0;
        r4 = 0;
        monitor-enter(r17);
        r9 = 0;
        r8 = 0;
    L_0x0007:
        r0 = r17;
        r10 = r0.stdoutWritepos;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r11 = r0.stdoutReadpos;	 Catch:{ all -> 0x00f0 }
        r9 = r10 - r11;
        r0 = r17;
        r10 = r0.stderrWritepos;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r11 = r0.stderrReadpos;	 Catch:{ all -> 0x00f0 }
        r8 = r10 - r11;
        if (r18 != 0) goto L_0x007e;
    L_0x001d:
        if (r9 == 0) goto L_0x007e;
    L_0x001f:
        if (r18 != 0) goto L_0x009c;
    L_0x0021:
        r0 = r21;
        if (r9 <= r0) goto L_0x009a;
    L_0x0025:
        r2 = r21;
    L_0x0027:
        r0 = r17;
        r10 = r0.stdoutBuffer;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r11 = r0.stdoutReadpos;	 Catch:{ all -> 0x00f0 }
        r0 = r19;
        r1 = r20;
        java.lang.System.arraycopy(r10, r11, r0, r1, r2);	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r10 = r0.stdoutReadpos;	 Catch:{ all -> 0x00f0 }
        r10 = r10 + r2;
        r0 = r17;
        r0.stdoutReadpos = r10;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r10 = r0.stdoutReadpos;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r11 = r0.stdoutWritepos;	 Catch:{ all -> 0x00f0 }
        if (r10 == r11) goto L_0x0062;
    L_0x0049:
        r0 = r17;
        r10 = r0.stdoutBuffer;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r11 = r0.stdoutReadpos;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r12 = r0.stdoutBuffer;	 Catch:{ all -> 0x00f0 }
        r13 = 0;
        r0 = r17;
        r14 = r0.stdoutWritepos;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r15 = r0.stdoutReadpos;	 Catch:{ all -> 0x00f0 }
        r14 = r14 - r15;
        java.lang.System.arraycopy(r10, r11, r12, r13, r14);	 Catch:{ all -> 0x00f0 }
    L_0x0062:
        r0 = r17;
        r10 = r0.stdoutWritepos;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r11 = r0.stdoutReadpos;	 Catch:{ all -> 0x00f0 }
        r10 = r10 - r11;
        r0 = r17;
        r0.stdoutWritepos = r10;	 Catch:{ all -> 0x00f0 }
        r10 = 0;
        r0 = r17;
        r0.stdoutReadpos = r10;	 Catch:{ all -> 0x00f0 }
    L_0x0074:
        r0 = r17;
        r10 = r0.state;	 Catch:{ all -> 0x00f0 }
        r11 = 2;
        if (r10 == r11) goto L_0x00f5;
    L_0x007b:
        monitor-exit(r17);	 Catch:{ all -> 0x00f0 }
        r10 = r2;
    L_0x007d:
        return r10;
    L_0x007e:
        if (r18 == 0) goto L_0x0082;
    L_0x0080:
        if (r8 != 0) goto L_0x001f;
    L_0x0082:
        r0 = r17;
        r10 = r0.EOF;	 Catch:{ all -> 0x00f0 }
        if (r10 != 0) goto L_0x008f;
    L_0x0088:
        r0 = r17;
        r10 = r0.state;	 Catch:{ all -> 0x00f0 }
        r11 = 2;
        if (r10 == r11) goto L_0x0092;
    L_0x008f:
        monitor-exit(r17);	 Catch:{ all -> 0x00f0 }
        r10 = -1;
        goto L_0x007d;
    L_0x0092:
        r17.wait();	 Catch:{ InterruptedException -> 0x0097 }
        goto L_0x0007;
    L_0x0097:
        r10 = move-exception;
        goto L_0x0007;
    L_0x009a:
        r2 = r9;
        goto L_0x0027;
    L_0x009c:
        r0 = r21;
        if (r8 <= r0) goto L_0x00f3;
    L_0x00a0:
        r2 = r21;
    L_0x00a2:
        r0 = r17;
        r10 = r0.stderrBuffer;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r11 = r0.stderrReadpos;	 Catch:{ all -> 0x00f0 }
        r0 = r19;
        r1 = r20;
        java.lang.System.arraycopy(r10, r11, r0, r1, r2);	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r10 = r0.stderrReadpos;	 Catch:{ all -> 0x00f0 }
        r10 = r10 + r2;
        r0 = r17;
        r0.stderrReadpos = r10;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r10 = r0.stderrReadpos;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r11 = r0.stderrWritepos;	 Catch:{ all -> 0x00f0 }
        if (r10 == r11) goto L_0x00dd;
    L_0x00c4:
        r0 = r17;
        r10 = r0.stderrBuffer;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r11 = r0.stderrReadpos;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r12 = r0.stderrBuffer;	 Catch:{ all -> 0x00f0 }
        r13 = 0;
        r0 = r17;
        r14 = r0.stderrWritepos;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r15 = r0.stderrReadpos;	 Catch:{ all -> 0x00f0 }
        r14 = r14 - r15;
        java.lang.System.arraycopy(r10, r11, r12, r13, r14);	 Catch:{ all -> 0x00f0 }
    L_0x00dd:
        r0 = r17;
        r10 = r0.stderrWritepos;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r11 = r0.stderrReadpos;	 Catch:{ all -> 0x00f0 }
        r10 = r10 - r11;
        r0 = r17;
        r0.stderrWritepos = r10;	 Catch:{ all -> 0x00f0 }
        r10 = 0;
        r0 = r17;
        r0.stderrReadpos = r10;	 Catch:{ all -> 0x00f0 }
        goto L_0x0074;
    L_0x00f0:
        r10 = move-exception;
        monitor-exit(r17);	 Catch:{ all -> 0x00f0 }
        throw r10;
    L_0x00f3:
        r2 = r8;
        goto L_0x00a2;
    L_0x00f5:
        r0 = r17;
        r10 = r0.localWindow;	 Catch:{ all -> 0x00f0 }
        r11 = 15000; // 0x3a98 float:2.102E-41 double:7.411E-320;
        if (r10 >= r11) goto L_0x0117;
    L_0x00fd:
        r0 = r17;
        r10 = r0.stdoutWritepos;	 Catch:{ all -> 0x00f0 }
        r10 = 30000 - r10;
        r0 = r17;
        r11 = r0.stderrWritepos;	 Catch:{ all -> 0x00f0 }
        r11 = 30000 - r11;
        r5 = java.lang.Math.min(r10, r11);	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r10 = r0.localWindow;	 Catch:{ all -> 0x00f0 }
        r3 = r5 - r10;
        r0 = r17;
        r0.localWindow = r5;	 Catch:{ all -> 0x00f0 }
    L_0x0117:
        r0 = r17;
        r7 = r0.remoteID;	 Catch:{ all -> 0x00f0 }
        r0 = r17;
        r4 = r0.localID;	 Catch:{ all -> 0x00f0 }
        monitor-exit(r17);	 Catch:{ all -> 0x00f0 }
        if (r3 <= 0) goto L_0x0199;
    L_0x0122:
        r10 = log;
        r10 = r10.isEnabled();
        if (r10 == 0) goto L_0x0150;
    L_0x012a:
        r10 = log;
        r11 = 80;
        r12 = new java.lang.StringBuilder;
        r13 = "Sending SSH_MSG_CHANNEL_WINDOW_ADJUST (channel ";
        r12.<init>(r13);
        r12 = r12.append(r4);
        r13 = ", ";
        r12 = r12.append(r13);
        r12 = r12.append(r3);
        r13 = ")";
        r12 = r12.append(r13);
        r12 = r12.toString();
        r10.log(r11, r12);
    L_0x0150:
        r0 = r17;
        r11 = r0.channelSendLock;
        monitor-enter(r11);
        r0 = r17;
        r6 = r0.msgWindowAdjust;	 Catch:{ all -> 0x019c }
        r10 = 0;
        r12 = 93;
        r6[r10] = r12;	 Catch:{ all -> 0x019c }
        r10 = 1;
        r12 = r7 >> 24;
        r12 = (byte) r12;	 Catch:{ all -> 0x019c }
        r6[r10] = r12;	 Catch:{ all -> 0x019c }
        r10 = 2;
        r12 = r7 >> 16;
        r12 = (byte) r12;	 Catch:{ all -> 0x019c }
        r6[r10] = r12;	 Catch:{ all -> 0x019c }
        r10 = 3;
        r12 = r7 >> 8;
        r12 = (byte) r12;	 Catch:{ all -> 0x019c }
        r6[r10] = r12;	 Catch:{ all -> 0x019c }
        r10 = 4;
        r12 = (byte) r7;	 Catch:{ all -> 0x019c }
        r6[r10] = r12;	 Catch:{ all -> 0x019c }
        r10 = 5;
        r12 = r3 >> 24;
        r12 = (byte) r12;	 Catch:{ all -> 0x019c }
        r6[r10] = r12;	 Catch:{ all -> 0x019c }
        r10 = 6;
        r12 = r3 >> 16;
        r12 = (byte) r12;	 Catch:{ all -> 0x019c }
        r6[r10] = r12;	 Catch:{ all -> 0x019c }
        r10 = 7;
        r12 = r3 >> 8;
        r12 = (byte) r12;	 Catch:{ all -> 0x019c }
        r6[r10] = r12;	 Catch:{ all -> 0x019c }
        r10 = 8;
        r12 = (byte) r3;	 Catch:{ all -> 0x019c }
        r6[r10] = r12;	 Catch:{ all -> 0x019c }
        r0 = r17;
        r10 = r0.closeMessageSent;	 Catch:{ all -> 0x019c }
        if (r10 != 0) goto L_0x0198;
    L_0x0191:
        r0 = r16;
        r10 = r0.tm;	 Catch:{ all -> 0x019c }
        r10.sendMessage(r6);	 Catch:{ all -> 0x019c }
    L_0x0198:
        monitor-exit(r11);	 Catch:{ all -> 0x019c }
    L_0x0199:
        r10 = r2;
        goto L_0x007d;
    L_0x019c:
        r10 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x019c }
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trilead.ssh2.channel.ChannelManager.getChannelData(com.trilead.ssh2.channel.Channel, boolean, byte[], int, int):int");
    }

    public void msgChannelData(byte[] msg, int msglen) throws IOException {
        if (msglen <= 9) {
            throw new IOException("SSH_MSG_CHANNEL_DATA message has wrong size (" + msglen + ")");
        }
        int id = ((((msg[1] & 255) << 24) | ((msg[2] & 255) << 16)) | ((msg[3] & 255) << 8)) | (msg[4] & 255);
        int len = ((((msg[5] & 255) << 24) | ((msg[6] & 255) << 16)) | ((msg[7] & 255) << 8)) | (msg[8] & 255);
        Channel c = getChannel(id);
        if (c == null) {
            throw new IOException("Unexpected SSH_MSG_CHANNEL_DATA message for non-existent channel " + id);
        } else if (len != msglen - 9) {
            throw new IOException("SSH_MSG_CHANNEL_DATA message has wrong len (calculated " + (msglen - 9) + ", got " + len + ")");
        } else {
            if (log.isEnabled()) {
                log.log(80, "Got SSH_MSG_CHANNEL_DATA (channel " + id + ", " + len + ")");
            }
            synchronized (c) {
                if (c.state == 4) {
                } else if (c.state != 2) {
                    throw new IOException("Got SSH_MSG_CHANNEL_DATA, but channel is not in correct state (" + c.state + ")");
                } else if (c.localWindow < len) {
                    throw new IOException("Remote sent too much data, does not fit into window.");
                } else {
                    c.localWindow -= len;
                    System.arraycopy(msg, 9, c.stdoutBuffer, c.stdoutWritepos, len);
                    c.stdoutWritepos += len;
                    c.notifyAll();
                }
            }
        }
    }

    public void msgChannelWindowAdjust(byte[] msg, int msglen) throws IOException {
        if (msglen != 9) {
            throw new IOException("SSH_MSG_CHANNEL_WINDOW_ADJUST message has wrong size (" + msglen + ")");
        }
        int id = ((((msg[1] & 255) << 24) | ((msg[2] & 255) << 16)) | ((msg[3] & 255) << 8)) | (msg[4] & 255);
        int windowChange = ((((msg[5] & 255) << 24) | ((msg[6] & 255) << 16)) | ((msg[7] & 255) << 8)) | (msg[8] & 255);
        Channel c = getChannel(id);
        if (c == null) {
            throw new IOException("Unexpected SSH_MSG_CHANNEL_WINDOW_ADJUST message for non-existent channel " + id);
        }
        synchronized (c) {
            c.remoteWindow += ((long) windowChange) & 4294967295L;
            if (c.remoteWindow > 4294967295L) {
                c.remoteWindow = 4294967295L;
            }
            c.notifyAll();
        }
        if (log.isEnabled()) {
            log.log(80, "Got SSH_MSG_CHANNEL_WINDOW_ADJUST (channel " + id + ", " + windowChange + ")");
        }
    }

    /* JADX WARNING: Missing block: B:66:?, code skipped:
            return;
     */
    public void msgChannelOpen(byte[] r22, int r23) throws java.io.IOException {
        /*
        r21 = this;
        r18 = new com.trilead.ssh2.packets.TypesReader;
        r9 = 0;
        r0 = r18;
        r1 = r22;
        r2 = r23;
        r0.m112init(r1, r9, r2);
        r18.readByte();
        r11 = r18.readString();
        r13 = r18.readUINT32();
        r15 = r18.readUINT32();
        r14 = r18.readUINT32();
        r9 = "x11";
        r9 = r9.equals(r11);
        if (r9 == 0) goto L_0x00a7;
    L_0x0027:
        r0 = r21;
        r10 = r0.x11_magic_cookies;
        monitor-enter(r10);
        r0 = r21;
        r9 = r0.x11_magic_cookies;	 Catch:{ all -> 0x00a1 }
        r9 = r9.size();	 Catch:{ all -> 0x00a1 }
        if (r9 != 0) goto L_0x0068;
    L_0x0036:
        r12 = new com.trilead.ssh2.packets.PacketChannelOpenFailure;	 Catch:{ all -> 0x00a1 }
        r9 = 1;
        r19 = "X11 forwarding not activated";
        r20 = "";
        r0 = r19;
        r1 = r20;
        r12.m60init(r13, r9, r0, r1);	 Catch:{ all -> 0x00a1 }
        r0 = r21;
        r9 = r0.tm;	 Catch:{ all -> 0x00a1 }
        r19 = r12.getPayload();	 Catch:{ all -> 0x00a1 }
        r0 = r19;
        r9.sendAsynchronousMessage(r0);	 Catch:{ all -> 0x00a1 }
        r9 = log;	 Catch:{ all -> 0x00a1 }
        r9 = r9.isEnabled();	 Catch:{ all -> 0x00a1 }
        if (r9 == 0) goto L_0x0066;
    L_0x0059:
        r9 = log;	 Catch:{ all -> 0x00a1 }
        r19 = 20;
        r20 = "Unexpected X11 request, denying it!";
        r0 = r19;
        r1 = r20;
        r9.log(r0, r1);	 Catch:{ all -> 0x00a1 }
    L_0x0066:
        monitor-exit(r10);	 Catch:{ all -> 0x00a1 }
    L_0x0067:
        return;
    L_0x0068:
        monitor-exit(r10);	 Catch:{ all -> 0x00a1 }
        r7 = r18.readString();
        r8 = r18.readUINT32();
        r4 = new com.trilead.ssh2.channel.Channel;
        r0 = r21;
        r4.m27init(r0);
        monitor-enter(r4);
        r4.remoteID = r13;	 Catch:{ all -> 0x00a4 }
        r9 = (long) r15;	 Catch:{ all -> 0x00a4 }
        r19 = 4294967295; // 0xffffffff float:NaN double:2.1219957905E-314;
        r9 = r9 & r19;
        r4.remoteWindow = r9;	 Catch:{ all -> 0x00a4 }
        r4.remoteMaxPacketSize = r14;	 Catch:{ all -> 0x00a4 }
        r0 = r21;
        r9 = r0.addChannel(r4);	 Catch:{ all -> 0x00a4 }
        r4.localID = r9;	 Catch:{ all -> 0x00a4 }
        monitor-exit(r4);	 Catch:{ all -> 0x00a4 }
        r17 = new com.trilead.ssh2.channel.RemoteX11AcceptThread;
        r0 = r17;
        r0.m34init(r4, r7, r8);
        r9 = 1;
        r0 = r17;
        r0.setDaemon(r9);
        r17.start();
        goto L_0x0067;
    L_0x00a1:
        r9 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x00a1 }
        throw r9;
    L_0x00a4:
        r9 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x00a4 }
        throw r9;
    L_0x00a7:
        r9 = "forwarded-tcpip";
        r9 = r9.equals(r11);
        if (r9 == 0) goto L_0x0146;
    L_0x00af:
        r5 = r18.readString();
        r6 = r18.readUINT32();
        r7 = r18.readString();
        r8 = r18.readUINT32();
        r16 = 0;
        r0 = r21;
        r10 = r0.remoteForwardings;
        monitor-enter(r10);
        r0 = r21;
        r9 = r0.remoteForwardings;	 Catch:{ all -> 0x010b }
        r19 = new java.lang.Integer;	 Catch:{ all -> 0x010b }
        r0 = r19;
        r0.<init>(r6);	 Catch:{ all -> 0x010b }
        r0 = r19;
        r9 = r9.get(r0);	 Catch:{ all -> 0x010b }
        r0 = r9;
        r0 = (com.trilead.ssh2.channel.RemoteForwardingData) r0;	 Catch:{ all -> 0x010b }
        r16 = r0;
        monitor-exit(r10);	 Catch:{ all -> 0x010b }
        if (r16 != 0) goto L_0x010e;
    L_0x00df:
        r12 = new com.trilead.ssh2.packets.PacketChannelOpenFailure;
        r9 = 1;
        r10 = "No thanks, unknown port in forwarded-tcpip request";
        r19 = "";
        r0 = r19;
        r12.m60init(r13, r9, r10, r0);
        r0 = r21;
        r9 = r0.tm;
        r10 = r12.getPayload();
        r9.sendAsynchronousMessage(r10);
        r9 = log;
        r9 = r9.isEnabled();
        if (r9 == 0) goto L_0x0067;
    L_0x00fe:
        r9 = log;
        r10 = 20;
        r19 = "Unexpected forwarded-tcpip request, denying it!";
        r0 = r19;
        r9.log(r10, r0);
        goto L_0x0067;
    L_0x010b:
        r9 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x010b }
        throw r9;
    L_0x010e:
        r4 = new com.trilead.ssh2.channel.Channel;
        r0 = r21;
        r4.m27init(r0);
        monitor-enter(r4);
        r4.remoteID = r13;	 Catch:{ all -> 0x0143 }
        r9 = (long) r15;	 Catch:{ all -> 0x0143 }
        r19 = 4294967295; // 0xffffffff float:NaN double:2.1219957905E-314;
        r9 = r9 & r19;
        r4.remoteWindow = r9;	 Catch:{ all -> 0x0143 }
        r4.remoteMaxPacketSize = r14;	 Catch:{ all -> 0x0143 }
        r0 = r21;
        r9 = r0.addChannel(r4);	 Catch:{ all -> 0x0143 }
        r4.localID = r9;	 Catch:{ all -> 0x0143 }
        monitor-exit(r4);	 Catch:{ all -> 0x0143 }
        r3 = new com.trilead.ssh2.channel.RemoteAcceptThread;
        r0 = r16;
        r9 = r0.targetAddress;
        r0 = r16;
        r10 = r0.targetPort;
        r3.m31init(r4, r5, r6, r7, r8, r9, r10);
        r9 = 1;
        r3.setDaemon(r9);
        r3.start();
        goto L_0x0067;
    L_0x0143:
        r9 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0143 }
        throw r9;
    L_0x0146:
        r12 = new com.trilead.ssh2.packets.PacketChannelOpenFailure;
        r9 = 3;
        r10 = "Unknown channel type";
        r19 = "";
        r0 = r19;
        r12.m60init(r13, r9, r10, r0);
        r0 = r21;
        r9 = r0.tm;
        r10 = r12.getPayload();
        r9.sendAsynchronousMessage(r10);
        r9 = log;
        r9 = r9.isEnabled();
        if (r9 == 0) goto L_0x0067;
    L_0x0165:
        r9 = log;
        r10 = 20;
        r19 = new java.lang.StringBuilder;
        r20 = "The peer tried to open an unsupported channel type (";
        r19.<init>(r20);
        r0 = r19;
        r19 = r0.append(r11);
        r20 = ")";
        r19 = r19.append(r20);
        r19 = r19.toString();
        r0 = r19;
        r9.log(r10, r0);
        goto L_0x0067;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trilead.ssh2.channel.ChannelManager.msgChannelOpen(byte[], int):void");
    }

    public void msgChannelRequest(byte[] msg, int msglen) throws IOException {
        TypesReader tr = new TypesReader(msg, 0, msglen);
        tr.readByte();
        int id = tr.readUINT32();
        Channel c = getChannel(id);
        if (c == null) {
            throw new IOException("Unexpected SSH_MSG_CHANNEL_REQUEST message for non-existent channel " + id);
        }
        String type = tr.readString("US-ASCII");
        boolean wantReply = tr.readBoolean();
        if (log.isEnabled()) {
            log.log(80, "Got SSH_MSG_CHANNEL_REQUEST (channel " + id + ", '" + type + "')");
        }
        if (type.equals("exit-status")) {
            if (wantReply) {
                throw new IOException("Badly formatted SSH_MSG_CHANNEL_REQUEST message, 'want reply' is true");
            }
            int exit_status = tr.readUINT32();
            if (tr.remain() != 0) {
                throw new IOException("Badly formatted SSH_MSG_CHANNEL_REQUEST message");
            }
            synchronized (c) {
                c.exit_status = new Integer(exit_status);
                c.notifyAll();
            }
            if (log.isEnabled()) {
                log.log(50, "Got EXIT STATUS (channel " + id + ", status " + exit_status + ")");
            }
        } else if (!type.equals("exit-signal")) {
            if (wantReply) {
                this.tm.sendAsynchronousMessage(new byte[]{(byte) 100, (byte) (c.remoteID >> 24), (byte) (c.remoteID >> 16), (byte) (c.remoteID >> 8), (byte) c.remoteID});
            }
            if (log.isEnabled()) {
                log.log(50, "Channel request '" + type + "' is not known, ignoring it");
            }
        } else if (wantReply) {
            throw new IOException("Badly formatted SSH_MSG_CHANNEL_REQUEST message, 'want reply' is true");
        } else {
            String signame = tr.readString("US-ASCII");
            tr.readBoolean();
            tr.readString();
            tr.readString();
            if (tr.remain() != 0) {
                throw new IOException("Badly formatted SSH_MSG_CHANNEL_REQUEST message");
            }
            synchronized (c) {
                c.exit_signal = signame;
                c.notifyAll();
            }
            if (log.isEnabled()) {
                log.log(50, "Got EXIT SIGNAL (channel " + id + ", signal " + signame + ")");
            }
        }
    }

    public void msgChannelEOF(byte[] msg, int msglen) throws IOException {
        if (msglen != 5) {
            throw new IOException("SSH_MSG_CHANNEL_EOF message has wrong size (" + msglen + ")");
        }
        int id = ((((msg[1] & 255) << 24) | ((msg[2] & 255) << 16)) | ((msg[3] & 255) << 8)) | (msg[4] & 255);
        Channel c = getChannel(id);
        if (c == null) {
            throw new IOException("Unexpected SSH_MSG_CHANNEL_EOF message for non-existent channel " + id);
        }
        synchronized (c) {
            c.EOF = true;
            c.notifyAll();
        }
        if (log.isEnabled()) {
            log.log(50, "Got SSH_MSG_CHANNEL_EOF (channel " + id + ")");
        }
    }

    public void msgChannelClose(byte[] msg, int msglen) throws IOException {
        if (msglen != 5) {
            throw new IOException("SSH_MSG_CHANNEL_CLOSE message has wrong size (" + msglen + ")");
        }
        int id = ((((msg[1] & 255) << 24) | ((msg[2] & 255) << 16)) | ((msg[3] & 255) << 8)) | (msg[4] & 255);
        Channel c = getChannel(id);
        if (c == null) {
            throw new IOException("Unexpected SSH_MSG_CHANNEL_CLOSE message for non-existent channel " + id);
        }
        synchronized (c) {
            c.EOF = true;
            c.state = 4;
            c.setReasonClosed("Close requested by remote");
            c.closeMessageRecv = true;
            removeChannel(c.localID);
            c.notifyAll();
        }
        if (log.isEnabled()) {
            log.log(50, "Got SSH_MSG_CHANNEL_CLOSE (channel " + id + ")");
        }
    }

    public void msgChannelSuccess(byte[] msg, int msglen) throws IOException {
        if (msglen != 5) {
            throw new IOException("SSH_MSG_CHANNEL_SUCCESS message has wrong size (" + msglen + ")");
        }
        int id = ((((msg[1] & 255) << 24) | ((msg[2] & 255) << 16)) | ((msg[3] & 255) << 8)) | (msg[4] & 255);
        Channel c = getChannel(id);
        if (c == null) {
            throw new IOException("Unexpected SSH_MSG_CHANNEL_SUCCESS message for non-existent channel " + id);
        }
        synchronized (c) {
            c.successCounter++;
            c.notifyAll();
        }
        if (log.isEnabled()) {
            log.log(80, "Got SSH_MSG_CHANNEL_SUCCESS (channel " + id + ")");
        }
    }

    public void msgChannelFailure(byte[] msg, int msglen) throws IOException {
        if (msglen != 5) {
            throw new IOException("SSH_MSG_CHANNEL_FAILURE message has wrong size (" + msglen + ")");
        }
        int id = ((((msg[1] & 255) << 24) | ((msg[2] & 255) << 16)) | ((msg[3] & 255) << 8)) | (msg[4] & 255);
        Channel c = getChannel(id);
        if (c == null) {
            throw new IOException("Unexpected SSH_MSG_CHANNEL_FAILURE message for non-existent channel " + id);
        }
        synchronized (c) {
            c.failedCounter++;
            c.notifyAll();
        }
        if (log.isEnabled()) {
            log.log(50, "Got SSH_MSG_CHANNEL_FAILURE (channel " + id + ")");
        }
    }

    public void msgChannelOpenConfirmation(byte[] msg, int msglen) throws IOException {
        PacketChannelOpenConfirmation sm = new PacketChannelOpenConfirmation(msg, 0, msglen);
        Channel c = getChannel(sm.recipientChannelID);
        if (c == null) {
            throw new IOException("Unexpected SSH_MSG_CHANNEL_OPEN_CONFIRMATION message for non-existent channel " + sm.recipientChannelID);
        }
        synchronized (c) {
            if (c.state != 1) {
                throw new IOException("Unexpected SSH_MSG_CHANNEL_OPEN_CONFIRMATION message for channel " + sm.recipientChannelID);
            }
            c.remoteID = sm.senderChannelID;
            c.remoteWindow = ((long) sm.initialWindowSize) & 4294967295L;
            c.remoteMaxPacketSize = sm.maxPacketSize;
            c.state = 2;
            c.notifyAll();
        }
        if (log.isEnabled()) {
            log.log(50, "Got SSH_MSG_CHANNEL_OPEN_CONFIRMATION (channel " + sm.recipientChannelID + " / remote: " + sm.senderChannelID + ")");
        }
    }

    public void msgChannelOpenFailure(byte[] msg, int msglen) throws IOException {
        if (msglen < 5) {
            throw new IOException("SSH_MSG_CHANNEL_OPEN_FAILURE message has wrong size (" + msglen + ")");
        }
        TypesReader tr = new TypesReader(msg, 0, msglen);
        tr.readByte();
        int id = tr.readUINT32();
        Channel c = getChannel(id);
        if (c == null) {
            throw new IOException("Unexpected SSH_MSG_CHANNEL_OPEN_FAILURE message for non-existent channel " + id);
        }
        String reasonCodeSymbolicName;
        int reasonCode = tr.readUINT32();
        String description = tr.readString(HTTP.UTF_8);
        switch (reasonCode) {
            case 1:
                reasonCodeSymbolicName = "SSH_OPEN_ADMINISTRATIVELY_PROHIBITED";
                break;
            case 2:
                reasonCodeSymbolicName = "SSH_OPEN_CONNECT_FAILED";
                break;
            case 3:
                reasonCodeSymbolicName = "SSH_OPEN_UNKNOWN_CHANNEL_TYPE";
                break;
            case 4:
                reasonCodeSymbolicName = "SSH_OPEN_RESOURCE_SHORTAGE";
                break;
            default:
                reasonCodeSymbolicName = "UNKNOWN REASON CODE (" + reasonCode + ")";
                break;
        }
        StringBuffer descriptionBuffer = new StringBuffer();
        descriptionBuffer.append(description);
        for (int i = 0; i < descriptionBuffer.length(); i++) {
            char cc = descriptionBuffer.charAt(i);
            if (cc < ' ' || cc > '~') {
                descriptionBuffer.setCharAt(i, 65533);
            }
        }
        synchronized (c) {
            c.EOF = true;
            c.state = 4;
            c.setReasonClosed("The server refused to open the channel (" + reasonCodeSymbolicName + ", '" + descriptionBuffer.toString() + "')");
            c.notifyAll();
        }
        if (log.isEnabled()) {
            log.log(50, "Got SSH_MSG_CHANNEL_OPEN_FAILURE (channel " + id + ")");
        }
    }

    public void msgGlobalRequest(byte[] msg, int msglen) throws IOException {
        TypesReader tr = new TypesReader(msg, 0, msglen);
        tr.readByte();
        String requestName = tr.readString();
        if (tr.readBoolean()) {
            this.tm.sendAsynchronousMessage(new byte[]{(byte) 82});
        }
        if (log.isEnabled()) {
            log.log(80, "Got SSH_MSG_GLOBAL_REQUEST (" + requestName + ")");
        }
    }

    public void msgGlobalSuccess() throws IOException {
        synchronized (this.channels) {
            this.globalSuccessCounter++;
            this.channels.notifyAll();
        }
        if (log.isEnabled()) {
            log.log(80, "Got SSH_MSG_REQUEST_SUCCESS");
        }
    }

    public void msgGlobalFailure() throws IOException {
        synchronized (this.channels) {
            this.globalFailedCounter++;
            this.channels.notifyAll();
        }
        if (log.isEnabled()) {
            log.log(80, "Got SSH_MSG_REQUEST_FAILURE");
        }
    }

    public void handleMessage(byte[] msg, int msglen) throws IOException {
        if (msg == null) {
            int i;
            if (log.isEnabled()) {
                log.log(50, "HandleMessage: got shutdown");
            }
            synchronized (this.listenerThreads) {
                for (i = 0; i < this.listenerThreads.size(); i++) {
                    ((IChannelWorkerThread) this.listenerThreads.elementAt(i)).stopWorking();
                }
                this.listenerThreadsAllowed = false;
            }
            synchronized (this.channels) {
                this.shutdown = true;
                for (i = 0; i < this.channels.size(); i++) {
                    Channel c = (Channel) this.channels.elementAt(i);
                    synchronized (c) {
                        c.EOF = true;
                        c.state = 4;
                        c.setReasonClosed("The connection is being shutdown");
                        c.closeMessageRecv = true;
                        c.notifyAll();
                    }
                }
                this.channels.setSize(0);
                this.channels.trimToSize();
                this.channels.notifyAll();
            }
            return;
        }
        switch (msg[0]) {
            case Packets.SSH_MSG_GLOBAL_REQUEST /*80*/:
                msgGlobalRequest(msg, msglen);
                return;
            case Packets.SSH_MSG_REQUEST_SUCCESS /*81*/:
                msgGlobalSuccess();
                return;
            case Packets.SSH_MSG_REQUEST_FAILURE /*82*/:
                msgGlobalFailure();
                return;
            case Packets.SSH_MSG_CHANNEL_OPEN /*90*/:
                msgChannelOpen(msg, msglen);
                return;
            case Packets.SSH_MSG_CHANNEL_OPEN_CONFIRMATION /*91*/:
                msgChannelOpenConfirmation(msg, msglen);
                return;
            case Packets.SSH_MSG_CHANNEL_OPEN_FAILURE /*92*/:
                msgChannelOpenFailure(msg, msglen);
                return;
            case Packets.SSH_MSG_CHANNEL_WINDOW_ADJUST /*93*/:
                msgChannelWindowAdjust(msg, msglen);
                return;
            case Packets.SSH_MSG_CHANNEL_DATA /*94*/:
                msgChannelData(msg, msglen);
                return;
            case Packets.SSH_MSG_CHANNEL_EXTENDED_DATA /*95*/:
                msgChannelExtendedData(msg, msglen);
                return;
            case Packets.SSH_MSG_CHANNEL_EOF /*96*/:
                msgChannelEOF(msg, msglen);
                return;
            case Packets.SSH_MSG_CHANNEL_CLOSE /*97*/:
                msgChannelClose(msg, msglen);
                return;
            case Packets.SSH_MSG_CHANNEL_REQUEST /*98*/:
                msgChannelRequest(msg, msglen);
                return;
            case Packets.SSH_MSG_CHANNEL_SUCCESS /*99*/:
                msgChannelSuccess(msg, msglen);
                return;
            case (byte) 100:
                msgChannelFailure(msg, msglen);
                return;
            default:
                throw new IOException("Cannot handle unknown channel message " + (msg[0] & 255));
        }
    }
}
