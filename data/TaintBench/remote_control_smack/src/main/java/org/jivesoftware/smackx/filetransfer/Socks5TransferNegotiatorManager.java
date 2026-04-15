package org.jivesoftware.smackx.filetransfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.util.Cache;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.Bytestream;
import org.jivesoftware.smackx.packet.Bytestream.StreamHost;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public class Socks5TransferNegotiatorManager implements FileTransferNegotiatorManager {
    private static final long BLACKLIST_LIFETIME = 7200000;
    private static ProxyProcess proxyProcess;
    private final Cache<String, Integer> addressBlacklist = new Cache(100, BLACKLIST_LIFETIME);
    private XMPPConnection connection;
    private final Object processLock = new Object();
    private List<String> proxies;
    private final Object proxyLock = new Object();
    private List<StreamHost> streamHosts;

    class ProxyProcess implements Runnable {
        private final Map<String, Socket> connectionMap = new HashMap();
        private boolean done = false;
        private final ServerSocket listeningSocket;
        private Thread thread = new Thread(this, "File Transfer Connection Listener");
        private int transfers;

        public void run() {
            try {
                this.listeningSocket.setSoTimeout(10000);
                while (!this.done) {
                    Socket conn = null;
                    synchronized (this) {
                        while (this.transfers <= 0 && !this.done) {
                            this.transfers = -1;
                            try {
                                wait();
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                    if (!this.done) {
                        try {
                            synchronized (this.listeningSocket) {
                                conn = this.listeningSocket.accept();
                            }
                            if (conn != null) {
                                String digest = establishSocks5UploadConnection(conn);
                                synchronized (this.connectionMap) {
                                    this.connectionMap.put(digest, conn);
                                }
                            }
                        } catch (SocketTimeoutException e2) {
                        } catch (IOException e3) {
                        } catch (XMPPException e4) {
                            e4.printStackTrace();
                            if (conn != null) {
                                try {
                                    conn.close();
                                } catch (IOException e5) {
                                }
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                }
                try {
                    this.listeningSocket.close();
                } catch (IOException e6) {
                }
            } catch (SocketException e42) {
                e42.printStackTrace();
                try {
                    this.listeningSocket.close();
                } catch (IOException e7) {
                }
            } catch (Throwable th2) {
                try {
                    this.listeningSocket.close();
                } catch (IOException e8) {
                }
                throw th2;
            }
        }

        private String establishSocks5UploadConnection(Socket connection) throws XMPPException, IOException {
            OutputStream out = new DataOutputStream(connection.getOutputStream());
            InputStream in = new DataInputStream(connection.getInputStream());
            if (in.read() != 5) {
                throw new XMPPException("Only SOCKS5 supported");
            }
            int b = in.read();
            int[] auth = new int[b];
            for (int i = 0; i < b; i++) {
                auth[i] = in.read();
            }
            int authMethod = -1;
            for (int anAuth : auth) {
                authMethod = anAuth == 0 ? 0 : -1;
                if (authMethod == 0) {
                    break;
                }
            }
            if (authMethod != 0) {
                throw new XMPPException("Authentication method not supported");
            }
            out.write(new byte[]{(byte) 5, (byte) 0});
            String responseDigest = Socks5TransferNegotiator.createIncomingSocks5Message(in);
            byte[] cmd = Socks5TransferNegotiator.createOutgoingSocks5Message(0, responseDigest);
            if (connection.isConnected()) {
                out.write(cmd);
                return responseDigest;
            }
            throw new XMPPException("Socket closed by remote user");
        }

        public void start() {
            this.thread.start();
        }

        public void stop() {
            this.done = true;
            synchronized (this) {
                notify();
            }
            synchronized (this.listeningSocket) {
                this.listeningSocket.notify();
            }
        }

        public int getPort() {
            return this.listeningSocket.getLocalPort();
        }

        ProxyProcess(ServerSocket listeningSocket) {
            this.listeningSocket = listeningSocket;
        }

        public Socket getSocket(String digest) {
            Socket socket;
            synchronized (this.connectionMap) {
                socket = (Socket) this.connectionMap.get(digest);
            }
            return socket;
        }

        public void addTransfer() {
            synchronized (this) {
                if (this.transfers == -1) {
                    this.transfers = 1;
                    notify();
                } else {
                    this.transfers++;
                }
            }
        }

        public void removeTransfer() {
            synchronized (this) {
                this.transfers--;
            }
        }
    }

    public Socks5TransferNegotiatorManager(XMPPConnection connection) {
        this.connection = connection;
    }

    public StreamNegotiator createNegotiator() {
        return new Socks5TransferNegotiator(this, this.connection);
    }

    public void incrementConnectionFailures(String address) {
        Integer count = (Integer) this.addressBlacklist.get(address);
        if (count == null) {
            count = Integer.valueOf(1);
        } else {
            count = Integer.valueOf(count.intValue() + 1);
        }
        this.addressBlacklist.put(address, count);
    }

    public int getConnectionFailures(String address) {
        Integer count = (Integer) this.addressBlacklist.get(address);
        return count != null ? count.intValue() : 0;
    }

    public ProxyProcess addTransfer() throws IOException {
        synchronized (this.processLock) {
            if (proxyProcess == null) {
                proxyProcess = new ProxyProcess(new ServerSocket(7777));
                proxyProcess.start();
            }
        }
        proxyProcess.addTransfer();
        return proxyProcess;
    }

    public void removeTransfer() {
        if (proxyProcess != null) {
            proxyProcess.removeTransfer();
        }
    }

    public Collection<StreamHost> getStreamHosts() {
        synchronized (this.proxyLock) {
            if (this.proxies == null) {
                initProxies();
            }
        }
        return Collections.unmodifiableCollection(this.streamHosts);
    }

    private String checkIsProxy(ServiceDiscoveryManager manager, Item item) {
        try {
            DiscoverInfo info = manager.discoverInfo(item.getEntityID());
            Iterator itx = info.getIdentities();
            while (itx.hasNext()) {
                Identity identity = (Identity) itx.next();
                if ("proxy".equalsIgnoreCase(identity.getCategory()) && "bytestreams".equalsIgnoreCase(identity.getType())) {
                    return info.getFrom();
                }
            }
            return null;
        } catch (XMPPException e) {
            return null;
        }
    }

    private void initProxies() {
        this.proxies = new ArrayList();
        ServiceDiscoveryManager manager = ServiceDiscoveryManager.getInstanceFor(this.connection);
        try {
            Iterator it = manager.discoverItems(this.connection.getServiceName()).getItems();
            while (it.hasNext()) {
                String proxy = checkIsProxy(manager, (Item) it.next());
                if (proxy != null) {
                    this.proxies.add(proxy);
                }
            }
            if (this.proxies.size() > 0) {
                initStreamHosts();
            }
        } catch (XMPPException e) {
        }
    }

    private void initStreamHosts() {
        List<StreamHost> streamHosts = new ArrayList();
        for (Object obj : this.proxies) {
            String jid = obj.toString();
            IQ query = new IQ() {
                public String getChildElementXML() {
                    return "<query xmlns=\"http://jabber.org/protocol/bytestreams\"/>";
                }
            };
            query.setType(Type.GET);
            query.setTo(jid);
            PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(query.getPacketID()));
            this.connection.sendPacket(query);
            Bytestream response = (Bytestream) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
            if (response != null) {
                streamHosts.addAll(response.getStreamHosts());
            }
            collector.cancel();
        }
        this.streamHosts = streamHosts;
    }

    public void cleanup() {
        synchronized (this.processLock) {
            if (proxyProcess != null) {
                proxyProcess.stop();
                proxyProcess = null;
            }
        }
    }
}
