package org.jivesoftware.smack;

import java.util.LinkedList;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;

public class PacketCollector {
    private static final int MAX_PACKETS = 65536;
    private boolean cancelled = false;
    private PacketFilter packetFilter;
    private PacketReader packetReader;
    private LinkedList<Packet> resultQueue;

    protected PacketCollector(PacketReader packetReader, PacketFilter packetFilter) {
        this.packetReader = packetReader;
        this.packetFilter = packetFilter;
        this.resultQueue = new LinkedList();
    }

    public void cancel() {
        if (!this.cancelled) {
            this.cancelled = true;
            this.packetReader.cancelPacketCollector(this);
        }
    }

    public PacketFilter getPacketFilter() {
        return this.packetFilter;
    }

    public synchronized Packet pollResult() {
        Packet packet;
        if (this.resultQueue.isEmpty()) {
            packet = null;
        } else {
            packet = (Packet) this.resultQueue.removeLast();
        }
        return packet;
    }

    public synchronized Packet nextResult() {
        while (this.resultQueue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        return (Packet) this.resultQueue.removeLast();
    }

    public synchronized Packet nextResult(long timeout) {
        Packet packet;
        if (this.resultQueue.isEmpty()) {
            long waitTime = timeout;
            long start = System.currentTimeMillis();
            while (this.resultQueue.isEmpty() && waitTime > 0) {
                try {
                    wait(waitTime);
                    long now = System.currentTimeMillis();
                    waitTime -= now - start;
                    start = now;
                } catch (InterruptedException e) {
                }
            }
            if (this.resultQueue.isEmpty()) {
                packet = null;
            } else {
                packet = (Packet) this.resultQueue.removeLast();
            }
        } else {
            packet = (Packet) this.resultQueue.removeLast();
        }
        return packet;
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void processPacket(Packet packet) {
        if (packet != null) {
            if (this.packetFilter == null || this.packetFilter.accept(packet)) {
                if (this.resultQueue.size() == 65536) {
                    this.resultQueue.removeLast();
                }
                this.resultQueue.addFirst(packet);
                notifyAll();
            }
        }
    }
}
