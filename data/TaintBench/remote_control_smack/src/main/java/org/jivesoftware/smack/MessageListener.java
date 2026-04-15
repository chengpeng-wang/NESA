package org.jivesoftware.smack;

import org.jivesoftware.smack.packet.Message;

public interface MessageListener {
    void processMessage(Chat chat, Message message);
}
