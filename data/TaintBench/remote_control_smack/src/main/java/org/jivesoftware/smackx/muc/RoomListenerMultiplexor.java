package org.jivesoftware.smackx.muc;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

class RoomListenerMultiplexor implements ConnectionListener {
    private static final Map<XMPPConnection, WeakReference<RoomListenerMultiplexor>> monitors = new WeakHashMap();
    private XMPPConnection connection;
    private RoomMultiplexFilter filter;
    private RoomMultiplexListener listener;

    private static class RoomMultiplexFilter implements PacketFilter {
        private Map<String, String> roomAddressTable;

        private RoomMultiplexFilter() {
            this.roomAddressTable = new ConcurrentHashMap();
        }

        public boolean accept(Packet p) {
            String from = p.getFrom();
            if (from == null) {
                return false;
            }
            return this.roomAddressTable.containsKey(StringUtils.parseBareAddress(from).toLowerCase());
        }

        public void addRoom(String address) {
            if (address != null) {
                this.roomAddressTable.put(address.toLowerCase(), address);
            }
        }

        public void removeRoom(String address) {
            if (address != null) {
                this.roomAddressTable.remove(address.toLowerCase());
            }
        }
    }

    private static class RoomMultiplexListener implements PacketListener {
        private Map<String, PacketMultiplexListener> roomListenersByAddress;

        private RoomMultiplexListener() {
            this.roomListenersByAddress = new ConcurrentHashMap();
        }

        public void processPacket(Packet p) {
            String from = p.getFrom();
            if (from != null) {
                PacketMultiplexListener listener = (PacketMultiplexListener) this.roomListenersByAddress.get(StringUtils.parseBareAddress(from).toLowerCase());
                if (listener != null) {
                    listener.processPacket(p);
                }
            }
        }

        public void addRoom(String address, PacketMultiplexListener listener) {
            if (address != null) {
                this.roomListenersByAddress.put(address.toLowerCase(), listener);
            }
        }

        public void removeRoom(String address) {
            if (address != null) {
                this.roomListenersByAddress.remove(address.toLowerCase());
            }
        }
    }

    public static RoomListenerMultiplexor getRoomMultiplexor(XMPPConnection conn) {
        RoomListenerMultiplexor roomListenerMultiplexor;
        synchronized (monitors) {
            if (!monitors.containsKey(conn)) {
                RoomListenerMultiplexor rm = new RoomListenerMultiplexor(conn, new RoomMultiplexFilter(), new RoomMultiplexListener());
                rm.init();
                monitors.put(conn, new WeakReference(rm));
            }
            roomListenerMultiplexor = (RoomListenerMultiplexor) ((WeakReference) monitors.get(conn)).get();
        }
        return roomListenerMultiplexor;
    }

    private RoomListenerMultiplexor(XMPPConnection connection, RoomMultiplexFilter filter, RoomMultiplexListener listener) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection is null");
        } else if (filter == null) {
            throw new IllegalArgumentException("Filter is null");
        } else if (listener == null) {
            throw new IllegalArgumentException("Listener is null");
        } else {
            this.connection = connection;
            this.filter = filter;
            this.listener = listener;
        }
    }

    public void addRoom(String address, PacketMultiplexListener roomListener) {
        this.filter.addRoom(address);
        this.listener.addRoom(address, roomListener);
    }

    public void connectionClosed() {
        cancel();
    }

    public void connectionClosedOnError(Exception e) {
        cancel();
    }

    public void reconnectingIn(int seconds) {
    }

    public void reconnectionSuccessful() {
    }

    public void reconnectionFailed(Exception e) {
    }

    public void init() {
        this.connection.addConnectionListener(this);
        this.connection.addPacketListener(this.listener, this.filter);
    }

    public void removeRoom(String address) {
        this.filter.removeRoom(address);
        this.listener.removeRoom(address);
    }

    private void cancel() {
        this.connection.removeConnectionListener(this);
        this.connection.removePacketListener(this.listener);
    }
}
