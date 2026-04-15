package org.jivesoftware.smackx;

public interface MessageEventNotificationListener {
    void cancelledNotification(String str, String str2);

    void composingNotification(String str, String str2);

    void deliveredNotification(String str, String str2);

    void displayedNotification(String str, String str2);

    void offlineNotification(String str, String str2);
}
