package org.jivesoftware.smack;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

public class Chat {
    private ChatManager chatManager;
    private final Set<MessageListener> listeners = new CopyOnWriteArraySet();
    private String participant;
    private String threadID;

    Chat(ChatManager chatManager, String participant, String threadID) {
        this.chatManager = chatManager;
        this.participant = participant;
        this.threadID = threadID;
    }

    public String getThreadID() {
        return this.threadID;
    }

    public String getParticipant() {
        return this.participant;
    }

    public void sendMessage(String text) throws XMPPException {
        Message message = new Message(this.participant, Type.chat);
        message.setThread(this.threadID);
        message.setBody(text);
        this.chatManager.sendMessage(this, message);
    }

    public void sendMessage(Message message) throws XMPPException {
        message.setTo(this.participant);
        message.setType(Type.chat);
        message.setThread(this.threadID);
        this.chatManager.sendMessage(this, message);
    }

    public void addMessageListener(MessageListener listener) {
        if (listener != null) {
            this.listeners.add(listener);
        }
    }

    public void removeMessageListener(MessageListener listener) {
        this.listeners.remove(listener);
    }

    public Collection<MessageListener> getListeners() {
        return Collections.unmodifiableCollection(this.listeners);
    }

    public PacketCollector createCollector() {
        return this.chatManager.createPacketCollector(this);
    }

    /* access modifiers changed from: 0000 */
    public void deliver(Message message) {
        message.setThread(this.threadID);
        for (MessageListener listener : this.listeners) {
            listener.processMessage(this, message);
        }
    }

    public boolean equals(Object obj) {
        return (obj instanceof Chat) && this.threadID.equals(((Chat) obj).getThreadID()) && this.participant.equals(((Chat) obj).getParticipant());
    }
}
