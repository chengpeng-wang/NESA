package org.jivesoftware.smackx.packet;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jivesoftware.smack.packet.IQ;

public class DiscoverInfo extends IQ {
    private final List<Feature> features = new CopyOnWriteArrayList();
    private final List<Identity> identities = new CopyOnWriteArrayList();
    private String node;

    public static class Feature {
        private String variable;

        public Feature(String variable) {
            this.variable = variable;
        }

        public String getVar() {
            return this.variable;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<feature var=\"").append(this.variable).append("\"/>");
            return buf.toString();
        }
    }

    public static class Identity {
        private String category;
        private String name;
        private String type;

        public Identity(String category, String name) {
            this.category = category;
            this.name = name;
        }

        public String getCategory() {
            return this.category;
        }

        public String getName() {
            return this.name;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<identity category=\"").append(this.category).append("\"");
            buf.append(" name=\"").append(this.name).append("\"");
            if (this.type != null) {
                buf.append(" type=\"").append(this.type).append("\"");
            }
            buf.append("/>");
            return buf.toString();
        }
    }

    public void addFeature(String feature) {
        addFeature(new Feature(feature));
    }

    private void addFeature(Feature feature) {
        synchronized (this.features) {
            this.features.add(feature);
        }
    }

    /* access modifiers changed from: 0000 */
    public Iterator<Feature> getFeatures() {
        Iterator it;
        synchronized (this.features) {
            it = Collections.unmodifiableList(this.features).iterator();
        }
        return it;
    }

    public void addIdentity(Identity identity) {
        synchronized (this.identities) {
            this.identities.add(identity);
        }
    }

    public Iterator<Identity> getIdentities() {
        Iterator it;
        synchronized (this.identities) {
            it = Collections.unmodifiableList(this.identities).iterator();
        }
        return it;
    }

    public String getNode() {
        return this.node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public boolean containsFeature(String feature) {
        Iterator<Feature> it = getFeatures();
        while (it.hasNext()) {
            if (feature.equals(((Feature) it.next()).getVar())) {
                return true;
            }
        }
        return false;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"http://jabber.org/protocol/disco#info\"");
        if (getNode() != null) {
            buf.append(" node=\"");
            buf.append(getNode());
            buf.append("\"");
        }
        buf.append(">");
        synchronized (this.identities) {
            for (Identity identity : this.identities) {
                buf.append(identity.toXML());
            }
        }
        synchronized (this.features) {
            for (Feature feature : this.features) {
                buf.append(feature.toXML());
            }
        }
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
    }
}
