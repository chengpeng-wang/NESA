package org.jivesoftware.smackx.workgroup.settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class ChatSettings extends IQ {
    public static final int BOT_SETTINGS = 2;
    public static final String ELEMENT_NAME = "chat-settings";
    public static final int IMAGE_SETTINGS = 0;
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    public static final int TEXT_SETTINGS = 1;
    private String key;
    private List settings;
    private int type;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            if (parser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            ChatSettings chatSettings = new ChatSettings();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && "chat-setting".equals(parser.getName())) {
                    chatSettings.addSetting(parseChatSetting(parser));
                } else if (eventType == 3 && ChatSettings.ELEMENT_NAME.equals(parser.getName())) {
                    done = true;
                }
            }
            return chatSettings;
        }

        private ChatSetting parseChatSetting(XmlPullParser parser) throws Exception {
            boolean done = false;
            String key = null;
            String value = null;
            int type = 0;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && "key".equals(parser.getName())) {
                    key = parser.nextText();
                } else if (eventType == 2 && "value".equals(parser.getName())) {
                    value = parser.nextText();
                } else if (eventType == 2 && "type".equals(parser.getName())) {
                    type = Integer.parseInt(parser.nextText());
                } else if (eventType == 3 && "chat-setting".equals(parser.getName())) {
                    done = true;
                }
            }
            return new ChatSetting(key, value, type);
        }
    }

    public ChatSettings() {
        this.type = -1;
        this.settings = new ArrayList();
    }

    public ChatSettings(String key) {
        this.type = -1;
        setKey(key);
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void addSetting(ChatSetting setting) {
        this.settings.add(setting);
    }

    public Collection getSettings() {
        return this.settings;
    }

    public ChatSetting getChatSetting(String key) {
        Collection<ChatSetting> col = getSettings();
        if (col != null) {
            for (ChatSetting chatSetting : col) {
                if (chatSetting.getKey().equals(key)) {
                    return chatSetting;
                }
            }
        }
        return null;
    }

    public ChatSetting getFirstEntry() {
        if (this.settings.size() > 0) {
            return (ChatSetting) this.settings.get(0);
        }
        return null;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=");
        buf.append('\"');
        buf.append("http://jivesoftware.com/protocol/workgroup");
        buf.append('\"');
        if (this.key != null) {
            buf.append(" key=\"" + this.key + "\"");
        }
        if (this.type != -1) {
            buf.append(" type=\"" + this.type + "\"");
        }
        buf.append("></").append(ELEMENT_NAME).append("> ");
        return buf.toString();
    }
}
