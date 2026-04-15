package org.jivesoftware.smack;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.ThreadFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.collections.ReferenceMap;

public class ChatManager {
    private static long id = 0;
    private static String prefix = StringUtils.randomString(5);
    private Set<ChatManagerListener> chatManagerListeners = new CopyOnWriteArraySet();
    private XMPPConnection connection;
    private Map<PacketInterceptor, PacketFilter> interceptors = new WeakHashMap();
    private Map<String, Chat> jidChats = new ReferenceMap(0, 2);
    private Map<String, Chat> threadChats = new ReferenceMap(0, 2);

    private static synchronized String nextID() {
        String stringBuilder;
        synchronized (ChatManager.class) {
            StringBuilder append = new StringBuilder().append(prefix);
            long j = id;
            id = 1 + j;
            stringBuilder = append.append(Long.toString(j)).toString();
        }
        return stringBuilder;
    }

    ChatManager(XMPPConnection connection) {
        this.connection = connection;
        connection.addPacketListener(new PacketListener() {
            public void processPacket(Packet packet) {
                Chat chat;
                Message message = (Message) packet;
                if (message.getThread() == null) {
                    chat = ChatManager.this.getUserChat(StringUtils.parseBareAddress(message.getFrom()));
                } else {
                    chat = ChatManager.this.getThreadChat(message.getThread());
                    if (chat == null) {
                        chat = ChatManager.this.getUserChat(StringUtils.parseBareAddress(message.getFrom()));
                    }
                }
                if (chat == null) {
                    chat = ChatManager.this.createChat(message);
                }
                ChatManager.this.deliverMessage(chat, message);
            }
        }, new PacketFilter() {
            public boolean accept(Packet packet) {
                if (!(packet instanceof Message)) {
                    return false;
                }
                Type messageType = ((Message) packet).getType();
                if (messageType == Type.groupchat || messageType == Type.headline) {
                    return false;
                }
                return true;
            }
        });
    }

    public Chat createChat(String userJID, MessageListener listener) {
        String threadID;
        do {
            threadID = nextID();
        } while (this.threadChats.get(threadID) != null);
        return createChat(userJID, threadID, listener);
    }

    public Chat createChat(String userJID, String thread, MessageListener listener) {
        if (thread == null) {
            thread = nextID();
        }
        if (((Chat) this.threadChats.get(thread)) != null) {
            throw new IllegalArgumentException("ThreadID is already used");
        }
        Chat chat = createChat(userJID, thread, true);
        chat.addMessageListener(listener);
        return chat;
    }

    private Chat createChat(String userJID, String threadID, boolean createdLocally) {
        Chat chat = new Chat(this, userJID, threadID);
        this.threadChats.put(threadID, chat);
        this.jidChats.put(userJID, chat);
        for (ChatManagerListener listener : this.chatManagerListeners) {
            listener.chatCreated(chat, createdLocally);
        }
        return chat;
    }

    /* access modifiers changed from: private */
    public Chat createChat(Message message) {
        String threadID = message.getThread();
        if (threadID == null) {
            threadID = nextID();
        }
        return createChat(message.getFrom(), threadID, false);
    }

    /* access modifiers changed from: private */
    public Chat getUserChat(String userJID) {
        return (Chat) this.jidChats.get(userJID);
    }

    public Chat getThreadChat(String thread) {
        return (Chat) this.threadChats.get(thread);
    }

    public void addChatListener(ChatManagerListener listener) {
        this.chatManagerListeners.add(listener);
    }

    public void removeChatListener(ChatManagerListener listener) {
        this.chatManagerListeners.remove(listener);
    }

    public Collection<ChatManagerListener> getChatListeners() {
        return Collections.unmodifiableCollection(this.chatManagerListeners);
    }

    /* access modifiers changed from: private */
    public void deliverMessage(Chat chat, Message message) {
        chat.deliver(message);
    }

    /* access modifiers changed from: 0000 */
    public void sendMessage(Chat chat, Message message) {
        for (Entry<PacketInterceptor, PacketFilter> interceptor : this.interceptors.entrySet()) {
            PacketFilter filter = (PacketFilter) interceptor.getValue();
            if (filter != null && filter.accept(message)) {
                ((PacketInterceptor) interceptor.getKey()).interceptPacket(message);
            }
        }
        if (message.getFrom() == null) {
            message.setFrom(this.connection.getUser());
        }
        this.connection.sendPacket(message);
    }

    /* access modifiers changed from: 0000 */
    public PacketCollector createPacketCollector(Chat chat) {
        return this.connection.createPacketCollector(new AndFilter(new ThreadFilter(chat.getThreadID()), new FromContainsFilter(chat.getParticipant())));
    }

    public void addOutgoingMessageInterceptor(PacketInterceptor packetInterceptor) {
        addOutgoingMessageInterceptor(packetInterceptor, null);
    }

    public void addOutgoingMessageInterceptor(PacketInterceptor packetInterceptor, PacketFilter filter) {
        if (packetInterceptor != null) {
            this.interceptors.put(packetInterceptor, filter);
        }
    }
}
