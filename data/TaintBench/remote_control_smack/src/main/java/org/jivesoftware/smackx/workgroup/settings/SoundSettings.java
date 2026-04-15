package org.jivesoftware.smackx.workgroup.settings;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.xmlpull.v1.XmlPullParser;

public class SoundSettings extends IQ {
    public static final String ELEMENT_NAME = "sound-settings";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private String incomingSound;
    private String outgoingSound;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            if (parser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            SoundSettings soundSettings = new SoundSettings();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && "outgoingSound".equals(parser.getName())) {
                    soundSettings.setOutgoingSound(parser.nextText());
                } else if (eventType == 2 && "incomingSound".equals(parser.getName())) {
                    soundSettings.setIncomingSound(parser.nextText());
                } else if (eventType == 3 && SoundSettings.ELEMENT_NAME.equals(parser.getName())) {
                    done = true;
                }
            }
            return soundSettings;
        }
    }

    public void setOutgoingSound(String outgoingSound) {
        this.outgoingSound = outgoingSound;
    }

    public void setIncomingSound(String incomingSound) {
        this.incomingSound = incomingSound;
    }

    public byte[] getIncomingSoundBytes() {
        return StringUtils.decodeBase64(this.incomingSound);
    }

    public byte[] getOutgoingSoundBytes() {
        return StringUtils.decodeBase64(this.outgoingSound);
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=");
        buf.append('\"');
        buf.append("http://jivesoftware.com/protocol/workgroup");
        buf.append('\"');
        buf.append("></").append(ELEMENT_NAME).append("> ");
        return buf.toString();
    }
}
