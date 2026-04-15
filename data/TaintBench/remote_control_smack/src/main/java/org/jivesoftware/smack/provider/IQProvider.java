package org.jivesoftware.smack.provider;

import org.jivesoftware.smack.packet.IQ;
import org.xmlpull.v1.XmlPullParser;

public interface IQProvider {
    IQ parseIQ(XmlPullParser xmlPullParser) throws Exception;
}
