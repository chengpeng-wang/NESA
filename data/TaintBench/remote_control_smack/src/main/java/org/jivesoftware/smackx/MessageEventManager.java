package org.jivesoftware.smackx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.MessageEvent;

public class MessageEventManager {
    private XMPPConnection con;
    private List messageEventNotificationListeners = new ArrayList();
    private List messageEventRequestListeners = new ArrayList();
    private PacketFilter packetFilter = new PacketExtensionFilter(GroupChatInvitation.ELEMENT_NAME, "jabber:x:event");
    private PacketListener packetListener;

    public MessageEventManager(XMPPConnection con) {
        this.con = con;
        init();
    }

    public static void addNotificationsRequests(Message message, boolean offline, boolean delivered, boolean displayed, boolean composing) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setOffline(offline);
        messageEvent.setDelivered(delivered);
        messageEvent.setDisplayed(displayed);
        messageEvent.setComposing(composing);
        message.addExtension(messageEvent);
    }

    public void addMessageEventRequestListener(MessageEventRequestListener messageEventRequestListener) {
        synchronized (this.messageEventRequestListeners) {
            if (!this.messageEventRequestListeners.contains(messageEventRequestListener)) {
                this.messageEventRequestListeners.add(messageEventRequestListener);
            }
        }
    }

    public void removeMessageEventRequestListener(MessageEventRequestListener messageEventRequestListener) {
        synchronized (this.messageEventRequestListeners) {
            this.messageEventRequestListeners.remove(messageEventRequestListener);
        }
    }

    public void addMessageEventNotificationListener(MessageEventNotificationListener messageEventNotificationListener) {
        synchronized (this.messageEventNotificationListeners) {
            if (!this.messageEventNotificationListeners.contains(messageEventNotificationListener)) {
                this.messageEventNotificationListeners.add(messageEventNotificationListener);
            }
        }
    }

    public void removeMessageEventNotificationListener(MessageEventNotificationListener messageEventNotificationListener) {
        synchronized (this.messageEventNotificationListeners) {
            this.messageEventNotificationListeners.remove(messageEventNotificationListener);
        }
    }

    /* access modifiers changed from: private */
    public void fireMessageEventRequestListeners(String from, String packetID, String methodName) {
        MessageEventRequestListener[] listeners;
        synchronized (this.messageEventRequestListeners) {
            listeners = new MessageEventRequestListener[this.messageEventRequestListeners.size()];
            this.messageEventRequestListeners.toArray(listeners);
        }
        try {
            Method method = MessageEventRequestListener.class.getDeclaredMethod(methodName, new Class[]{String.class, String.class, MessageEventManager.class});
            for (Object invoke : listeners) {
                method.invoke(invoke, new Object[]{from, packetID, this});
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void fireMessageEventNotificationListeners(String from, String packetID, String methodName) {
        MessageEventNotificationListener[] listeners;
        synchronized (this.messageEventNotificationListeners) {
            listeners = new MessageEventNotificationListener[this.messageEventNotificationListeners.size()];
            this.messageEventNotificationListeners.toArray(listeners);
        }
        try {
            Method method = MessageEventNotificationListener.class.getDeclaredMethod(methodName, new Class[]{String.class, String.class});
            for (Object invoke : listeners) {
                method.invoke(invoke, new Object[]{from, packetID});
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
    }

    private void init() {
        this.packetListener = new PacketListener() {
            public void processPacket(Packet packet) {
                Message message = (Message) packet;
                MessageEvent messageEvent = (MessageEvent) message.getExtension(GroupChatInvitation.ELEMENT_NAME, "jabber:x:event");
                Iterator it;
                if (messageEvent.isMessageEventRequest()) {
                    it = messageEvent.getEventTypes();
                    while (it.hasNext()) {
                        MessageEventManager.this.fireMessageEventRequestListeners(message.getFrom(), message.getPacketID(), ((String) it.next()).concat("NotificationRequested"));
                    }
                    return;
                }
                it = messageEvent.getEventTypes();
                while (it.hasNext()) {
                    MessageEventManager.this.fireMessageEventNotificationListeners(message.getFrom(), messageEvent.getPacketID(), ((String) it.next()).concat("Notification"));
                }
            }
        };
        this.con.addPacketListener(this.packetListener, this.packetFilter);
    }

    public void sendDeliveredNotification(String to, String packetID) {
        Message msg = new Message(to);
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setDelivered(true);
        messageEvent.setPacketID(packetID);
        msg.addExtension(messageEvent);
        this.con.sendPacket(msg);
    }

    public void sendDisplayedNotification(String to, String packetID) {
        Message msg = new Message(to);
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setDisplayed(true);
        messageEvent.setPacketID(packetID);
        msg.addExtension(messageEvent);
        this.con.sendPacket(msg);
    }

    public void sendComposingNotification(String to, String packetID) {
        Message msg = new Message(to);
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setComposing(true);
        messageEvent.setPacketID(packetID);
        msg.addExtension(messageEvent);
        this.con.sendPacket(msg);
    }

    public void sendCancelledNotification(String to, String packetID) {
        Message msg = new Message(to);
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setCancelled(true);
        messageEvent.setPacketID(packetID);
        msg.addExtension(messageEvent);
        this.con.sendPacket(msg);
    }

    public void destroy() {
        if (this.con != null) {
            this.con.removePacketListener(this.packetListener);
        }
    }

    public void finalize() {
        destroy();
    }
}
