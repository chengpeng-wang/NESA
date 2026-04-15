package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.DelayInformation;
import org.jivesoftware.smackx.packet.StreamInitiation;
import org.jivesoftware.smackx.packet.StreamInitiation.File;
import org.xmlpull.v1.XmlPullParser;

public class StreamInitiationProvider implements IQProvider {
    public IQ parseIQ(XmlPullParser parser) throws Exception {
        boolean done = false;
        String id = parser.getAttributeValue("", "id");
        String mimeType = parser.getAttributeValue("", "mime-type");
        StreamInitiation initiation = new StreamInitiation();
        String name = null;
        String size = null;
        String hash = null;
        String date = null;
        String desc = null;
        boolean isRanged = false;
        DataForm form = null;
        DataFormProvider dataFormProvider = new DataFormProvider();
        while (!done) {
            int eventType = parser.next();
            String elementName = parser.getName();
            String namespace = parser.getNamespace();
            if (eventType == 2) {
                if (elementName.equals("file")) {
                    name = parser.getAttributeValue("", "name");
                    size = parser.getAttributeValue("", "size");
                    hash = parser.getAttributeValue("", "hash");
                    date = parser.getAttributeValue("", "date");
                } else if (elementName.equals("desc")) {
                    desc = parser.nextText();
                } else if (elementName.equals("range")) {
                    isRanged = true;
                } else if (elementName.equals(GroupChatInvitation.ELEMENT_NAME) && namespace.equals("jabber:x:data")) {
                    form = (DataForm) dataFormProvider.parseExtension(parser);
                }
            } else if (eventType == 3) {
                if (elementName.equals("si")) {
                    done = true;
                } else if (elementName.equals("file")) {
                    long fileSize = 0;
                    if (!(size == null || size.trim().length() == 0)) {
                        try {
                            fileSize = Long.parseLong(size);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    File file = new File(name, fileSize);
                    file.setHash(hash);
                    if (date != null) {
                        file.setDate(DelayInformation.UTC_FORMAT.parse(date));
                    }
                    file.setDesc(desc);
                    file.setRanged(isRanged);
                    initiation.setFile(file);
                }
            }
        }
        initiation.setSesssionID(id);
        initiation.setMimeType(mimeType);
        initiation.setFeatureNegotiationForm(form);
        return initiation;
    }
}
