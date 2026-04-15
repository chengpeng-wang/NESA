package org.jivesoftware.smackx;

public class DefaultMessageEventRequestListener implements MessageEventRequestListener {
    public void deliveredNotificationRequested(String from, String packetID, MessageEventManager messageEventManager) {
        messageEventManager.sendDeliveredNotification(from, packetID);
    }

    public void displayedNotificationRequested(String from, String packetID, MessageEventManager messageEventManager) {
    }

    public void composingNotificationRequested(String from, String packetID, MessageEventManager messageEventManager) {
    }

    public void offlineNotificationRequested(String from, String packetID, MessageEventManager messageEventManager) {
    }
}
