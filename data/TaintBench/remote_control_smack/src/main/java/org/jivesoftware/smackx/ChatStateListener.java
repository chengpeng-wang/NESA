package org.jivesoftware.smackx;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;

public interface ChatStateListener extends MessageListener {
    void stateChanged(Chat chat, ChatState chatState);
}
