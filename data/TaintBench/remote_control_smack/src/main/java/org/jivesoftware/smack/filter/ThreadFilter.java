package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class ThreadFilter implements PacketFilter {
    private String thread;

    public ThreadFilter(String thread) {
        if (thread == null) {
            throw new IllegalArgumentException("Thread cannot be null.");
        }
        this.thread = thread;
    }

    public boolean accept(Packet packet) {
        return (packet instanceof Message) && this.thread.equals(((Message) packet).getThread());
    }
}
