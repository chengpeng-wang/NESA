package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.ChatState;
import org.xmlpull.v1.XmlPullParser;

public class ChatStateExtension implements PacketExtension {
    private ChatState state;

    public static class Provider implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            ChatState state;
            try {
                state = ChatState.valueOf(parser.getName());
            } catch (Exception e) {
                state = ChatState.active;
            }
            return new ChatStateExtension(state);
        }
    }

    public ChatStateExtension(ChatState state) {
        this.state = state;
    }

    public String getElementName() {
        return this.state.name();
    }

    public String getNamespace() {
        return "http://jabber.org/protocol/chatstates";
    }

    public String toXML() {
        return "<" + getElementName() + " xmlns=\"" + getNamespace() + "\" />";
    }
}
