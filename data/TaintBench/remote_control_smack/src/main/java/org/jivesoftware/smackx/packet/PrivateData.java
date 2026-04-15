package org.jivesoftware.smackx.packet;

public interface PrivateData {
    String getElementName();

    String getNamespace();

    String toXML();
}
