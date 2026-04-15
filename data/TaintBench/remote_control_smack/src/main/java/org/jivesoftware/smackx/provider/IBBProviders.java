package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class IBBProviders {

    public static class Close implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            return new org.jivesoftware.smackx.packet.IBBExtensions.Close(parser.getAttributeValue("", "sid"));
        }
    }

    public static class Data implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            return new org.jivesoftware.smackx.packet.IBBExtensions.Data(parser.getAttributeValue("", "sid"), Long.parseLong(parser.getAttributeValue("", "seq")), parser.nextText());
        }
    }

    public static class Open implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            return new org.jivesoftware.smackx.packet.IBBExtensions.Open(parser.getAttributeValue("", "sid"), Integer.parseInt(parser.getAttributeValue("", "block-size")));
        }
    }
}
