package org.jivesoftware.smackx.muc;

import java.util.LinkedList;
import org.jivesoftware.smack.packet.Packet;

class ConnectionDetachedPacketCollector {
    private static final int MAX_PACKETS = 65536;
    private LinkedList<Packet> resultQueue = new LinkedList();

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
            try {
                wait(timeout);
            } catch (InterruptedException e) {
            }
        }
        if (this.resultQueue.isEmpty()) {
            packet = null;
        } else {
            packet = (Packet) this.resultQueue.removeLast();
        }
        return packet;
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void processPacket(Packet packet) {
        if (packet != null) {
            if (this.resultQueue.size() == 65536) {
                this.resultQueue.removeLast();
            }
            this.resultQueue.addFirst(packet);
            notifyAll();
        }
    }
}
