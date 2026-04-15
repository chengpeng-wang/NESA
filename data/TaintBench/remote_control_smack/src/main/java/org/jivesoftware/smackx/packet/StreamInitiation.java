package org.jivesoftware.smackx.packet;

import java.util.Date;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.StringUtils;

public class StreamInitiation extends IQ {
    private Feature featureNegotiation;
    private File file;
    private String id;
    private String mimeType;

    public class Feature implements PacketExtension {
        private final DataForm data;

        public Feature(DataForm data) {
            this.data = data;
        }

        public DataForm getData() {
            return this.data;
        }

        public String getNamespace() {
            return "http://jabber.org/protocol/feature-neg";
        }

        public String getElementName() {
            return "feature";
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<feature xmlns=\"http://jabber.org/protocol/feature-neg\">");
            buf.append(this.data.toXML());
            buf.append("</feature>");
            return buf.toString();
        }
    }

    public static class File implements PacketExtension {
        private Date date;
        private String desc;
        private String hash;
        private boolean isRanged;
        private final String name;
        private final long size;

        public File(String name, long size) {
            if (name == null) {
                throw new NullPointerException("name cannot be null");
            }
            this.name = name;
            this.size = size;
        }

        public String getName() {
            return this.name;
        }

        public long getSize() {
            return this.size;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getHash() {
            return this.hash;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Date getDate() {
            return this.date;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setRanged(boolean isRanged) {
            this.isRanged = isRanged;
        }

        public boolean isRanged() {
            return this.isRanged;
        }

        public String getElementName() {
            return "file";
        }

        public String getNamespace() {
            return "http://jabber.org/protocol/si/profile/file-transfer";
        }

        public String toXML() {
            StringBuilder buffer = new StringBuilder();
            buffer.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\" ");
            if (getName() != null) {
                buffer.append("name=\"").append(StringUtils.escapeForXML(getName())).append("\" ");
            }
            if (getSize() > 0) {
                buffer.append("size=\"").append(getSize()).append("\" ");
            }
            if (getDate() != null) {
                buffer.append("date=\"").append(DelayInformation.UTC_FORMAT.format(this.date)).append("\" ");
            }
            if (getHash() != null) {
                buffer.append("hash=\"").append(getHash()).append("\" ");
            }
            if ((this.desc == null || this.desc.length() <= 0) && !this.isRanged) {
                buffer.append("/>");
            } else {
                buffer.append(">");
                if (getDesc() != null && this.desc.length() > 0) {
                    buffer.append("<desc>").append(StringUtils.escapeForXML(getDesc())).append("</desc>");
                }
                if (isRanged()) {
                    buffer.append("<range/>");
                }
                buffer.append("</").append(getElementName()).append(">");
            }
            return buffer.toString();
        }
    }

    public void setSesssionID(String id) {
        this.id = id;
    }

    public String getSessionID() {
        return this.id;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public void setFeatureNegotiationForm(DataForm form) {
        this.featureNegotiation = new Feature(form);
    }

    public DataForm getFeatureNegotiationForm() {
        return this.featureNegotiation.getData();
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        if (getType().equals(Type.SET)) {
            buf.append("<si xmlns=\"http://jabber.org/protocol/si\" ");
            if (getSessionID() != null) {
                buf.append("id=\"").append(getSessionID()).append("\" ");
            }
            if (getMimeType() != null) {
                buf.append("mime-type=\"").append(getMimeType()).append("\" ");
            }
            buf.append("profile=\"http://jabber.org/protocol/si/profile/file-transfer\">");
            String fileXML = this.file.toXML();
            if (fileXML != null) {
                buf.append(fileXML);
            }
        } else if (getType().equals(Type.RESULT)) {
            buf.append("<si xmlns=\"http://jabber.org/protocol/si\">");
        } else {
            throw new IllegalArgumentException("IQ Type not understood");
        }
        if (this.featureNegotiation != null) {
            buf.append(this.featureNegotiation.toXML());
        }
        buf.append("</si>");
        return buf.toString();
    }
}
