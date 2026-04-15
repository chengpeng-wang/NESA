package org.jivesoftware.smack.packet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jivesoftware.smack.util.StringUtils;

public class Message extends Packet {
    private final Set<Body> bodies = new HashSet();
    private String language;
    private String subject = null;
    private String thread = null;
    private Type type = Type.normal;

    public static class Body {
        /* access modifiers changed from: private */
        public String langauge;
        /* access modifiers changed from: private */
        public String message;

        private Body(String language, String message) {
            if (message == null) {
                throw new NullPointerException("Message cannot be null.");
            }
            this.langauge = language;
            this.message = message;
        }

        public String getLanguage() {
            if (Packet.DEFAULT_LANGUAGE.equals(this.langauge)) {
                return null;
            }
            return this.langauge;
        }

        public String getMessage() {
            return this.message;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Body body = (Body) o;
            if (this.langauge != null) {
                if (!this.langauge.equals(body.langauge)) {
                    return false;
                }
            } else if (body.langauge != null) {
                return false;
            }
            return this.message.equals(body.message);
        }

        public int hashCode() {
            return (this.message.hashCode() * 31) + (this.langauge != null ? this.langauge.hashCode() : 0);
        }
    }

    public enum Type {
        normal,
        chat,
        groupchat,
        headline,
        error;

        public static Type fromString(String name) {
            try {
                return valueOf(name);
            } catch (Exception e) {
                return normal;
            }
        }
    }

    public Message(String to) {
        setTo(to);
    }

    public Message(String to, Type type) {
        setTo(to);
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }
        this.type = type;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return getBody(null);
    }

    public String getBody(String language) {
        language = Packet.parseXMLLang(language);
        for (Body body : this.bodies) {
            if ((body.langauge == null && language == null) || (body != null && body.langauge.equals(language))) {
                return body.message;
            }
        }
        return null;
    }

    public Collection<Body> getBodies() {
        return Collections.unmodifiableCollection(this.bodies);
    }

    public void setBody(String body) {
        if (body == null) {
            removeBody("");
        } else {
            addBody(null, body);
        }
    }

    public Body addBody(String language, String body) {
        if (body == null) {
            throw new NullPointerException("Body must be specified");
        }
        Body messageBody = new Body(Packet.parseXMLLang(language), body);
        this.bodies.add(messageBody);
        return messageBody;
    }

    public boolean removeBody(String language) {
        language = Packet.parseXMLLang(language);
        for (Body body : this.bodies) {
            if (language.equals(body.langauge)) {
                return this.bodies.remove(body);
            }
        }
        return false;
    }

    public boolean removeBody(Body body) {
        return this.bodies.remove(body);
    }

    public Collection<String> getBodyLanguages() {
        List<String> languages = new ArrayList(this.bodies.size());
        for (Body body : this.bodies) {
            if (!Packet.parseXMLLang(body.langauge).equals(Packet.getDefaultLanguage())) {
                languages.add(body.langauge);
            }
        }
        return Collections.unmodifiableCollection(languages);
    }

    public String getThread() {
        return this.thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    private String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<message");
        if (getXmlns() != null) {
            buf.append(" xmlns=\"").append(getXmlns()).append("\"");
        }
        if (this.language != null) {
            buf.append(" xml:lang=\"").append(getLanguage()).append("\"");
        }
        if (getPacketID() != null) {
            buf.append(" id=\"").append(getPacketID()).append("\"");
        }
        if (getTo() != null) {
            buf.append(" to=\"").append(StringUtils.escapeForXML(getTo())).append("\"");
        }
        if (getFrom() != null) {
            buf.append(" from=\"").append(StringUtils.escapeForXML(getFrom())).append("\"");
        }
        if (this.type != Type.normal) {
            buf.append(" type=\"").append(this.type).append("\"");
        }
        buf.append(">");
        if (this.subject != null) {
            buf.append("<subject>").append(StringUtils.escapeForXML(this.subject)).append("</subject>");
        }
        if (getBody() != null) {
            buf.append("<body>").append(StringUtils.escapeForXML(getBody())).append("</body>");
        }
        for (Body body : getBodies()) {
            if (!(DEFAULT_LANGUAGE.equals(body.getLanguage()) || body.getLanguage() == null)) {
                buf.append("<body xml:lang=\"").append(body.getLanguage()).append("\">");
                buf.append(StringUtils.escapeForXML(body.getMessage()));
                buf.append("</body>");
            }
        }
        if (this.thread != null) {
            buf.append("<thread>").append(this.thread).append("</thread>");
        }
        if (this.type == Type.error) {
            XMPPError error = getError();
            if (error != null) {
                buf.append(error.toXML());
            }
        }
        buf.append(getExtensionsXML());
        buf.append("</message>");
        return buf.toString();
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        if (!super.equals(message) || this.bodies.size() != message.bodies.size() || !this.bodies.containsAll(message.bodies)) {
            return false;
        }
        if (this.language != null) {
            if (!this.language.equals(message.language)) {
                return false;
            }
        } else if (message.language != null) {
            return false;
        }
        if (this.subject != null) {
            if (!this.subject.equals(message.subject)) {
                return false;
            }
        } else if (message.subject != null) {
            return false;
        }
        if (this.thread != null) {
            if (!this.thread.equals(message.thread)) {
                return false;
            }
        } else if (message.thread != null) {
            return false;
        }
        if (this.type != message.type) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int hashCode;
        int i = 0;
        if (this.type != null) {
            result = this.type.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.subject != null) {
            hashCode = this.subject.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.thread != null) {
            hashCode = this.thread.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (i2 + hashCode) * 31;
        if (this.language != null) {
            i = this.language.hashCode();
        }
        return ((hashCode + i) * 31) + this.bodies.hashCode();
    }
}
