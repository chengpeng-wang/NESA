package org.jivesoftware.smack;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;

class PacketWriter {
    private XMPPConnection connection;
    /* access modifiers changed from: private */
    public boolean done;
    private final Map<PacketInterceptor, InterceptorWrapper> interceptors = new ConcurrentHashMap();
    /* access modifiers changed from: private */
    public Thread keepAliveThread;
    /* access modifiers changed from: private */
    public long lastActive = System.currentTimeMillis();
    private final Map<PacketListener, ListenerWrapper> listeners = new ConcurrentHashMap();
    private final BlockingQueue<Packet> queue = new ArrayBlockingQueue(500, true);
    /* access modifiers changed from: private */
    public Writer writer;
    private Thread writerThread;

    private static class InterceptorWrapper {
        private PacketFilter packetFilter;
        private PacketInterceptor packetInterceptor;

        public InterceptorWrapper(PacketInterceptor packetInterceptor, PacketFilter packetFilter) {
            this.packetInterceptor = packetInterceptor;
            this.packetFilter = packetFilter;
        }

        public boolean equals(Object object) {
            if (object == null) {
                return false;
            }
            if (object instanceof InterceptorWrapper) {
                return ((InterceptorWrapper) object).packetInterceptor.equals(this.packetInterceptor);
            }
            if (object instanceof PacketInterceptor) {
                return object.equals(this.packetInterceptor);
            }
            return false;
        }

        public void notifyListener(Packet packet) {
            if (this.packetFilter == null || this.packetFilter.accept(packet)) {
                this.packetInterceptor.interceptPacket(packet);
            }
        }
    }

    private class KeepAliveTask implements Runnable {
        private int delay;
        private Thread thread;

        public KeepAliveTask(int delay) {
            this.delay = delay;
        }

        /* access modifiers changed from: protected */
        public void setThread(Thread thread) {
            this.thread = thread;
        }

        public void run() {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
            }
            while (!PacketWriter.this.done && PacketWriter.this.keepAliveThread == this.thread) {
                synchronized (PacketWriter.this.writer) {
                    if (System.currentTimeMillis() - PacketWriter.this.lastActive >= ((long) this.delay)) {
                        try {
                            PacketWriter.this.writer.write(" ");
                            PacketWriter.this.writer.flush();
                        } catch (Exception e2) {
                        }
                    }
                }
                try {
                    Thread.sleep((long) this.delay);
                } catch (InterruptedException e3) {
                }
            }
        }
    }

    private static class ListenerWrapper {
        private PacketFilter packetFilter;
        private PacketListener packetListener;

        public ListenerWrapper(PacketListener packetListener, PacketFilter packetFilter) {
            this.packetListener = packetListener;
            this.packetFilter = packetFilter;
        }

        public void notifyListener(Packet packet) {
            if (this.packetFilter == null || this.packetFilter.accept(packet)) {
                this.packetListener.processPacket(packet);
            }
        }
    }

    protected PacketWriter(XMPPConnection connection) {
        this.connection = connection;
        init();
    }

    /* access modifiers changed from: protected */
    public void init() {
        this.writer = this.connection.writer;
        this.done = false;
        this.writerThread = new Thread() {
            public void run() {
                PacketWriter.this.writePackets(this);
            }
        };
        this.writerThread.setName("Smack Packet Writer (" + this.connection.connectionCounterValue + ")");
        this.writerThread.setDaemon(true);
    }

    public void sendPacket(Packet packet) {
        if (!this.done) {
            processInterceptors(packet);
            try {
                this.queue.put(packet);
                synchronized (this.queue) {
                    this.queue.notifyAll();
                }
                processListeners(packet);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    public void addPacketListener(PacketListener packetListener, PacketFilter packetFilter) {
        this.listeners.put(packetListener, new ListenerWrapper(packetListener, packetFilter));
    }

    public void removePacketListener(PacketListener packetListener) {
        this.listeners.remove(packetListener);
    }

    public int getPacketListenerCount() {
        return this.listeners.size();
    }

    public void addPacketInterceptor(PacketInterceptor packetInterceptor, PacketFilter packetFilter) {
        this.interceptors.put(packetInterceptor, new InterceptorWrapper(packetInterceptor, packetFilter));
    }

    public void removePacketInterceptor(PacketInterceptor packetInterceptor) {
        this.interceptors.remove(packetInterceptor);
    }

    public void startup() {
        this.writerThread.start();
    }

    /* access modifiers changed from: 0000 */
    public void startKeepAliveProcess() {
        int keepAliveInterval = SmackConfiguration.getKeepAliveInterval();
        if (keepAliveInterval > 0) {
            KeepAliveTask task = new KeepAliveTask(keepAliveInterval);
            this.keepAliveThread = new Thread(task);
            task.setThread(this.keepAliveThread);
            this.keepAliveThread.setDaemon(true);
            this.keepAliveThread.setName("Smack Keep Alive (" + this.connection.connectionCounterValue + ")");
            this.keepAliveThread.start();
        }
    }

    /* access modifiers changed from: 0000 */
    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public void shutdown() {
        this.done = true;
        synchronized (this.queue) {
            this.queue.notifyAll();
        }
    }

    /* access modifiers changed from: 0000 */
    public void cleanup() {
        this.interceptors.clear();
        this.listeners.clear();
    }

    private Packet nextPacket() {
        Packet packet = null;
        while (!this.done) {
            packet = (Packet) this.queue.poll();
            if (packet != null) {
                break;
            }
            try {
                synchronized (this.queue) {
                    this.queue.wait();
                }
            } catch (InterruptedException e) {
            }
        }
        return packet;
    }

    /* access modifiers changed from: private */
    public void writePackets(Thread thisThread) {
        try {
            openStream();
            while (!this.done && this.writerThread == thisThread) {
                Packet packet = nextPacket();
                if (packet != null) {
                    synchronized (this.writer) {
                        this.writer.write(packet.toXML());
                        this.writer.flush();
                        this.lastActive = System.currentTimeMillis();
                    }
                }
            }
            try {
                synchronized (this.writer) {
                    while (!this.queue.isEmpty()) {
                        this.writer.write(((Packet) this.queue.remove()).toXML());
                    }
                    this.writer.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.queue.clear();
            this.writer.write("</stream:stream>");
            this.writer.flush();
            try {
                this.writer.close();
            } catch (Exception e2) {
            }
        } catch (Exception e3) {
            try {
                this.writer.close();
            } catch (Exception e4) {
            }
        } catch (IOException ioe) {
            if (!this.done) {
                this.done = true;
                this.connection.packetReader.notifyConnectionError(ioe);
            }
        } catch (Throwable th) {
            try {
                this.writer.close();
            } catch (Exception e5) {
            }
        }
    }

    private void processListeners(Packet packet) {
        for (ListenerWrapper listenerWrapper : this.listeners.values()) {
            listenerWrapper.notifyListener(packet);
        }
    }

    private void processInterceptors(Packet packet) {
        if (packet != null) {
            for (InterceptorWrapper interceptorWrapper : this.interceptors.values()) {
                interceptorWrapper.notifyListener(packet);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void openStream() throws IOException {
        StringBuilder stream = new StringBuilder();
        stream.append("<stream:stream");
        stream.append(" to=\"").append(this.connection.serviceName).append("\"");
        stream.append(" xmlns=\"jabber:client\"");
        stream.append(" xmlns:stream=\"http://etherx.jabber.org/streams\"");
        stream.append(" version=\"1.0\">");
        this.writer.write(stream.toString());
        this.writer.flush();
    }
}
