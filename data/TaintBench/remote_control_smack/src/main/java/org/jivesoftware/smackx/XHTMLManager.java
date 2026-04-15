package org.jivesoftware.smackx;

import java.util.Iterator;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.XHTMLExtension;

public class XHTMLManager {
    private static final String namespace = "http://jabber.org/protocol/xhtml-im";

    static {
        XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener() {
            public void connectionCreated(XMPPConnection connection) {
                XHTMLManager.setServiceEnabled(connection, true);
            }
        });
    }

    public static Iterator getBodies(Message message) {
        XHTMLExtension xhtmlExtension = (XHTMLExtension) message.getExtension("html", namespace);
        if (xhtmlExtension != null) {
            return xhtmlExtension.getBodies();
        }
        return null;
    }

    public static void addBody(Message message, String body) {
        XHTMLExtension xhtmlExtension = (XHTMLExtension) message.getExtension("html", namespace);
        if (xhtmlExtension == null) {
            xhtmlExtension = new XHTMLExtension();
            message.addExtension(xhtmlExtension);
        }
        xhtmlExtension.addBody(body);
    }

    public static boolean isXHTMLMessage(Message message) {
        return message.getExtension("html", namespace) != null;
    }

    public static synchronized void setServiceEnabled(XMPPConnection connection, boolean enabled) {
        synchronized (XHTMLManager.class) {
            if (isServiceEnabled(connection) != enabled) {
                if (enabled) {
                    ServiceDiscoveryManager.getInstanceFor(connection).addFeature(namespace);
                } else {
                    ServiceDiscoveryManager.getInstanceFor(connection).removeFeature(namespace);
                }
            }
        }
    }

    public static boolean isServiceEnabled(XMPPConnection connection) {
        return ServiceDiscoveryManager.getInstanceFor(connection).includesFeature(namespace);
    }

    public static boolean isServiceEnabled(XMPPConnection connection, String userID) {
        try {
            return ServiceDiscoveryManager.getInstanceFor(connection).discoverInfo(userID).containsFeature(namespace);
        } catch (XMPPException e) {
            e.printStackTrace();
            return false;
        }
    }
}
