package org.jivesoftware.smackx.workgroup.ext.notes;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class ChatNotes extends IQ {
    public static final String ELEMENT_NAME = "chat-notes";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private String notes;
    private String sessionID;

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            ChatNotes chatNotes = new ChatNotes();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2) {
                    if (parser.getName().equals("sessionID")) {
                        chatNotes.setSessionID(parser.nextText());
                    } else if (parser.getName().equals("text")) {
                        chatNotes.setNotes(parser.nextText().replaceAll("\\\\n", "\n"));
                    }
                } else if (eventType == 3 && parser.getName().equals(ChatNotes.ELEMENT_NAME)) {
                    done = true;
                }
            }
            return chatNotes;
        }
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append("http://jivesoftware.com/protocol/workgroup").append("\">");
        buf.append("<sessionID>").append(getSessionID()).append("</sessionID>");
        if (getNotes() != null) {
            buf.append("<notes>").append(getNotes()).append("</notes>");
        }
        buf.append("</").append(ELEMENT_NAME).append("> ");
        return buf.toString();
    }

    public static final String replace(String string, String oldString, String newString) {
        if (string == null) {
            return null;
        }
        if (newString == null) {
            return string;
        }
        int i = string.indexOf(oldString, 0);
        if (i < 0) {
            return string;
        }
        char[] string2 = string.toCharArray();
        char[] newString2 = newString.toCharArray();
        int oLength = oldString.length();
        StringBuilder buf = new StringBuilder(string2.length);
        buf.append(string2, 0, i).append(newString2);
        while (true) {
            i += oLength;
            int j = i;
            i = string.indexOf(oldString, i);
            if (i > 0) {
                buf.append(string2, j, i - j).append(newString2);
            } else {
                buf.append(string2, j, string2.length - j);
                return buf.toString();
            }
        }
    }
}
