package org.jivesoftware.smackx.filetransfer;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.Bytestream;
import org.jivesoftware.smackx.packet.Bytestream.Mode;
import org.jivesoftware.smackx.packet.Bytestream.StreamHost;
import org.jivesoftware.smackx.packet.Bytestream.StreamHostUsed;
import org.jivesoftware.smackx.packet.StreamInitiation;

public class Socks5TransferNegotiator extends StreamNegotiator {
    private static final int CONNECT_FAILURE_THRESHOLD = 2;
    protected static final String NAMESPACE = "http://jabber.org/protocol/bytestreams";
    public static boolean isAllowLocalProxyHost = true;
    private final XMPPConnection connection;
    private Socks5TransferNegotiatorManager transferNegotiatorManager;

    private static class SelectedHostInfo {
        protected Socket establishedSocket;
        protected XMPPException exception;
        protected StreamHost selectedHost;

        SelectedHostInfo(StreamHost selectedHost, Socket establishedSocket) {
            this.selectedHost = selectedHost;
            this.establishedSocket = establishedSocket;
        }
    }

    private static class BytestreamSIDFilter implements PacketFilter {
        private String sessionID;

        public BytestreamSIDFilter(String sessionID) {
            if (sessionID == null) {
                throw new IllegalArgumentException("StreamID cannot be null");
            }
            this.sessionID = sessionID;
        }

        public boolean accept(Packet packet) {
            if (!Bytestream.class.isInstance(packet)) {
                return false;
            }
            String sessionID = ((Bytestream) packet).getSessionID();
            if (sessionID == null || !sessionID.equals(this.sessionID)) {
                return false;
            }
            return true;
        }
    }

    public Socks5TransferNegotiator(Socks5TransferNegotiatorManager transferNegotiatorManager, XMPPConnection connection) {
        this.connection = connection;
        this.transferNegotiatorManager = transferNegotiatorManager;
    }

    public PacketFilter getInitiationPacketFilter(String from, String sessionID) {
        return new AndFilter(new FromMatchesFilter(from), new BytestreamSIDFilter(sessionID));
    }

    /* access modifiers changed from: 0000 */
    public InputStream negotiateIncomingStream(Packet streamInitiation) throws XMPPException {
        Bytestream streamHostsInfo = (Bytestream) streamInitiation;
        if (streamHostsInfo.getType().equals(Type.ERROR)) {
            throw new XMPPException(streamHostsInfo.getError());
        }
        try {
            SelectedHostInfo selectedHost = selectHost(streamHostsInfo);
            this.connection.sendPacket(createUsedHostConfirmation(selectedHost.selectedHost, streamHostsInfo.getFrom(), streamHostsInfo.getTo(), streamHostsInfo.getPacketID()));
            try {
                PushbackInputStream stream = new PushbackInputStream(selectedHost.establishedSocket.getInputStream());
                stream.unread(stream.read());
                return stream;
            } catch (IOException e) {
                throw new XMPPException("Error establishing input stream", e);
            }
        } catch (XMPPException ex) {
            if (ex.getXMPPError() != null) {
                this.connection.sendPacket(super.createError(streamHostsInfo.getTo(), streamHostsInfo.getFrom(), streamHostsInfo.getPacketID(), ex.getXMPPError()));
            }
            throw ex;
        }
    }

    public InputStream createIncomingStream(StreamInitiation initiation) throws XMPPException {
        return negotiateIncomingStream(initiateIncomingStream(this.connection, initiation));
    }

    private Bytestream createUsedHostConfirmation(StreamHost selectedHost, String initiator, String target, String packetID) {
        Bytestream streamResponse = new Bytestream();
        streamResponse.setTo(initiator);
        streamResponse.setFrom(target);
        streamResponse.setType(Type.RESULT);
        streamResponse.setPacketID(packetID);
        streamResponse.setUsedHost(selectedHost.getJID());
        return streamResponse;
    }

    private SelectedHostInfo selectHost(Bytestream streamHostsInfo) throws XMPPException {
        IOException e;
        StreamHost selectedHost = null;
        Socket socket = null;
        for (StreamHost selectedHost2 : streamHostsInfo.getStreamHosts()) {
            String address = selectedHost2.getAddress();
            if (getConnectionFailures(address) < 2) {
                try {
                    Socket socket2 = new Socket(address, selectedHost2.getPort());
                    try {
                        establishSOCKS5ConnectionToProxy(socket2, createDigest(streamHostsInfo.getSessionID(), streamHostsInfo.getFrom(), streamHostsInfo.getTo()));
                        socket = socket2;
                        break;
                    } catch (IOException e2) {
                        e = e2;
                        socket = socket2;
                    }
                } catch (IOException e3) {
                    e = e3;
                    e.printStackTrace();
                    incrementConnectionFailures(address);
                    selectedHost2 = null;
                    socket = null;
                }
            }
        }
        if (selectedHost2 != null && socket != null && socket.isConnected()) {
            return new SelectedHostInfo(selectedHost2, socket);
        }
        String errorMessage = "Could not establish socket with any provided host";
        throw new XMPPException(errorMessage, new XMPPError(Condition.no_acceptable, errorMessage));
    }

    private void incrementConnectionFailures(String address) {
        this.transferNegotiatorManager.incrementConnectionFailures(address);
    }

    private int getConnectionFailures(String address) {
        return this.transferNegotiatorManager.getConnectionFailures(address);
    }

    private String createDigest(String sessionID, String initiator, String target) {
        return StringUtils.hash(sessionID + StringUtils.parseName(initiator) + "@" + StringUtils.parseServer(initiator) + "/" + StringUtils.parseResource(initiator) + StringUtils.parseName(target) + "@" + StringUtils.parseServer(target) + "/" + StringUtils.parseResource(target));
    }

    public OutputStream createOutgoingStream(String streamID, String initiator, String target) throws XMPPException {
        try {
            Socket socket = initBytestreamSocket(streamID, initiator, target);
            if (socket == null) {
                return null;
            }
            try {
                return new BufferedOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                throw new XMPPException("Error establishing output stream", e);
            }
        } catch (Exception e2) {
            throw new XMPPException("Error establishing transfer socket", e2);
        }
    }

    private Socket initBytestreamSocket(String sessionID, String initiator, String target) throws Exception {
        ProxyProcess process;
        String localIP;
        int port;
        try {
            process = establishListeningSocket();
        } catch (IOException e) {
            process = null;
        }
        try {
            localIP = discoverLocalIP();
        } catch (UnknownHostException e2) {
            localIP = null;
        }
        if (process != null) {
            try {
                port = process.getPort();
            } catch (Throwable th) {
                cleanupListeningSocket();
            }
        } else {
            port = 0;
        }
        Socket conn = waitForUsedHostResponse(sessionID, process, createDigest(sessionID, initiator, target), createByteStreamInit(initiator, target, sessionID, localIP, port)).establishedSocket;
        cleanupListeningSocket();
        return conn;
    }

    private SelectedHostInfo waitForUsedHostResponse(String sessionID, ProxyProcess proxy, String digest, Bytestream query) throws XMPPException, IOException {
        SelectedHostInfo info = new SelectedHostInfo();
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(query.getPacketID()));
        this.connection.sendPacket(query);
        Packet packet = collector.nextResult(10000);
        collector.cancel();
        if (packet == null || !(packet instanceof Bytestream)) {
            throw new XMPPException("Unexpected response from remote user");
        }
        Bytestream response = (Bytestream) packet;
        if (response.getType().equals(Type.ERROR)) {
            throw new XMPPException("Remote client returned error, stream hosts expected", response.getError());
        }
        StreamHostUsed used = response.getUsedHost();
        StreamHost usedHost = query.getStreamHost(used.getJID());
        if (usedHost == null) {
            throw new XMPPException("Remote user responded with unknown host");
        } else if (used.getJID().equals(query.getFrom())) {
            info.establishedSocket = proxy.getSocket(digest);
            info.selectedHost = usedHost;
            return info;
        } else {
            info.establishedSocket = new Socket(usedHost.getAddress(), usedHost.getPort());
            establishSOCKS5ConnectionToProxy(info.establishedSocket, digest);
            Bytestream activate = createByteStreamActivate(sessionID, response.getTo(), usedHost.getJID(), response.getFrom());
            collector = this.connection.createPacketCollector(new PacketIDFilter(activate.getPacketID()));
            this.connection.sendPacket(activate);
            IQ serverResponse = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
            collector.cancel();
            if (serverResponse.getType().equals(Type.RESULT)) {
                return info;
            }
            info.establishedSocket.close();
            return null;
        }
    }

    private ProxyProcess establishListeningSocket() throws IOException {
        return this.transferNegotiatorManager.addTransfer();
    }

    private void cleanupListeningSocket() {
        this.transferNegotiatorManager.removeTransfer();
    }

    private String discoverLocalIP() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    private Bytestream createByteStreamInit(String from, String to, String sid, String localIP, int port) {
        Bytestream bs = new Bytestream();
        bs.setTo(to);
        bs.setFrom(from);
        bs.setSessionID(sid);
        bs.setType(Type.SET);
        bs.setMode(Mode.tcp);
        if (localIP != null && port > 0) {
            bs.addStreamHost(from, localIP, port);
        }
        Collection<StreamHost> streamHosts = this.transferNegotiatorManager.getStreamHosts();
        if (streamHosts != null) {
            for (StreamHost host : streamHosts) {
                bs.addStreamHost(host);
            }
        }
        return bs;
    }

    private static Bytestream createByteStreamActivate(String sessionID, String from, String to, String target) {
        Bytestream activate = new Bytestream(sessionID);
        activate.setMode(null);
        activate.setToActivate(target);
        activate.setFrom(from);
        activate.setTo(to);
        activate.setType(Type.SET);
        return activate;
    }

    public String[] getNamespaces() {
        return new String[]{"http://jabber.org/protocol/bytestreams"};
    }

    private void establishSOCKS5ConnectionToProxy(Socket socket, String digest) throws IOException {
        byte[] cmd = new byte[]{(byte) 5, (byte) 1, (byte) 0};
        OutputStream out = new DataOutputStream(socket.getOutputStream());
        out.write(cmd);
        InputStream in = new DataInputStream(socket.getInputStream());
        in.read(new byte[2]);
        out.write(createOutgoingSocks5Message(1, digest));
        createIncomingSocks5Message(in);
    }

    static String createIncomingSocks5Message(InputStream in) throws IOException {
        byte[] cmd = new byte[5];
        in.read(cmd, 0, 5);
        byte[] addr = new byte[cmd[4]];
        in.read(addr, 0, addr.length);
        String digest = new String(addr);
        in.read();
        in.read();
        return digest;
    }

    static byte[] createOutgoingSocks5Message(int cmd, String digest) {
        byte[] addr = digest.getBytes();
        byte[] data = new byte[(addr.length + 7)];
        data[0] = (byte) 5;
        data[1] = (byte) cmd;
        data[2] = (byte) 0;
        data[3] = (byte) 3;
        data[4] = (byte) addr.length;
        System.arraycopy(addr, 0, data, 5, addr.length);
        data[data.length - 2] = (byte) 0;
        data[data.length - 1] = (byte) 0;
        return data;
    }

    public void cleanup() {
    }
}
