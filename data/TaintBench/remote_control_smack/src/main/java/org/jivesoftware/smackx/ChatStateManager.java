package org.jivesoftware.smackx;

import java.util.Map;
import java.util.WeakHashMap;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.NotFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.collections.ReferenceMap;
import org.jivesoftware.smackx.packet.ChatStateExtension;

public class ChatStateManager {
    private static final PacketFilter filter = new NotFilter(new PacketExtensionFilter("http://jabber.org/protocol/chatstates"));
    private static final Map<XMPPConnection, ChatStateManager> managers = new WeakHashMap();
    private final Map<Chat, ChatState> chatStates = new ReferenceMap(2, 0);
    /* access modifiers changed from: private|final */
    public final XMPPConnection connection;
    private final IncomingMessageInterceptor incomingInterceptor = new IncomingMessageInterceptor();
    private final OutgoingMessageInterceptor outgoingInterceptor = new OutgoingMessageInterceptor();

    private class IncomingMessageInterceptor implements ChatManagerListener, MessageListener {
        private IncomingMessageInterceptor() {
        }

        public void chatCreated(Chat chat, boolean createdLocally) {
            chat.addMessageListener(this);
        }

        public void processMessage(Chat chat, Message message) {
            PacketExtension extension = message.getExtension("http://jabber.org/protocol/chatstates");
            if (extension != null) {
                try {
                    ChatStateManager.this.fireNewChatState(chat, ChatState.valueOf(extension.getElementName()));
                } catch (Exception e) {
                }
            }
        }
    }

    private class OutgoingMessageInterceptor implements PacketInterceptor {
        private OutgoingMessageInterceptor() {
        }

        public void interceptPacket(Packet packet) {
            Message message = (Message) packet;
            Chat chat = ChatStateManager.this.connection.getChatManager().getThreadChat(message.getThread());
            if (chat != null && ChatStateManager.this.updateChatState(chat, ChatState.active)) {
                message.addExtension(new ChatStateExtension(ChatState.active));
            }
        }
    }

    public static ChatStateManager getInstance(XMPPConnection connection) {
        if (connection == null) {
            return null;
        }
        ChatStateManager manager;
        synchronized (managers) {
            manager = (ChatStateManager) managers.get(connection);
            if (manager == null) {
                manager = new ChatStateManager(connection);
                manager.init();
                managers.put(connection, manager);
            }
        }
        return manager;
    }

    private ChatStateManager(XMPPConnection connection) {
        this.connection = connection;
    }

    private void init() {
        this.connection.getChatManager().addOutgoingMessageInterceptor(this.outgoingInterceptor, filter);
        this.connection.getChatManager().addChatListener(this.incomingInterceptor);
        ServiceDiscoveryManager.getInstanceFor(this.connection).addFeature("http://jabber.org/protocol/chatstates");
    }

    public void setCurrentState(ChatState newState, Chat chat) throws XMPPException {
        if (chat == null || newState == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        } else if (updateChatState(chat, newState)) {
            Message message = new Message();
            message.addExtension(new ChatStateExtension(newState));
            chat.sendMessage(message);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return this.connection.equals(((ChatStateManager) o).connection);
    }

    public int hashCode() {
        return this.connection.hashCode();
    }

    /* access modifiers changed from: private */
    public boolean updateChatState(Chat chat, ChatState newState) {
        if (((ChatState) this.chatStates.get(chat)) == newState) {
            return false;
        }
        this.chatStates.put(chat, newState);
        return true;
    }

    /* access modifiers changed from: private */
    public void fireNewChatState(Chat chat, ChatState state) {
        for (MessageListener listener : chat.getListeners()) {
            if (listener instanceof ChatStateListener) {
                ((ChatStateListener) listener).stateChanged(chat, state);
            }
        }
    }
}
