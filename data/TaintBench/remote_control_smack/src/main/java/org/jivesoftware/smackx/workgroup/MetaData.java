package org.jivesoftware.smackx.workgroup;

import java.util.Map;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.workgroup.util.MetaDataUtils;

public class MetaData implements PacketExtension {
    public static final String ELEMENT_NAME = "metadata";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private Map metaData;

    public MetaData(Map metaData) {
        this.metaData = metaData;
    }

    public Map getMetaData() {
        return this.metaData;
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return "http://jivesoftware.com/protocol/workgroup";
    }

    public String toXML() {
        return MetaDataUtils.serializeMetaData(getMetaData());
    }
}
