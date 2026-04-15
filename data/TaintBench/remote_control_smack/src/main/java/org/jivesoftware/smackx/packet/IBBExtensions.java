package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PacketExtension;

public class IBBExtensions {
    public static final String NAMESPACE = "http://jabber.org/protocol/ibb";

    public static class Data implements PacketExtension {
        public static final String ELEMENT_NAME = "data";
        private String data;
        private long seq;
        final String sid;

        public String getSessionID() {
            return this.sid;
        }

        public String getNamespace() {
            return "http://jabber.org/protocol/ibb";
        }

        public Data(String sid) {
            this.sid = sid;
        }

        public Data(String sid, long seq, String data) {
            this(sid);
            this.seq = seq;
            this.data = data;
        }

        public String getElementName() {
            return ELEMENT_NAME;
        }

        public String getData() {
            return this.data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public long getSeq() {
            return this.seq;
        }

        public void setSeq(long seq) {
            this.seq = seq;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\" ");
            buf.append("sid=\"").append(getSessionID()).append("\" ");
            buf.append("seq=\"").append(getSeq()).append("\"");
            buf.append(">");
            buf.append(getData());
            buf.append("</").append(getElementName()).append(">");
            return buf.toString();
        }
    }

    private static abstract class IBB extends IQ {
        final String sid;

        private IBB(String sid) {
            this.sid = sid;
        }

        public String getSessionID() {
            return this.sid;
        }

        public String getNamespace() {
            return "http://jabber.org/protocol/ibb";
        }
    }

    public static class Close extends IBB {
        public static final String ELEMENT_NAME = "close";

        public Close(String sid) {
            super(sid);
        }

        public String getElementName() {
            return ELEMENT_NAME;
        }

        public String getChildElementXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\" ");
            buf.append("sid=\"").append(getSessionID()).append("\"");
            buf.append("/>");
            return buf.toString();
        }
    }

    public static class Open extends IBB {
        public static final String ELEMENT_NAME = "open";
        private final int blockSize;

        public Open(String sid, int blockSize) {
            super(sid);
            this.blockSize = blockSize;
        }

        public int getBlockSize() {
            return this.blockSize;
        }

        public String getElementName() {
            return ELEMENT_NAME;
        }

        public String getChildElementXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\" ");
            buf.append("sid=\"").append(getSessionID()).append("\" ");
            buf.append("block-size=\"").append(getBlockSize()).append("\"");
            buf.append("/>");
            return buf.toString();
        }
    }
}
