package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.GroupChatInvitation;

public class MUCUser implements PacketExtension {
    private Decline decline;
    private Destroy destroy;
    private Invite invite;
    private Item item;
    private String password;
    private Status status;

    public static class Decline {
        private String from;
        private String reason;
        private String to;

        public String getFrom() {
            return this.from;
        }

        public String getReason() {
            return this.reason;
        }

        public String getTo() {
            return this.to;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<decline ");
            if (getTo() != null) {
                buf.append(" to=\"").append(getTo()).append("\"");
            }
            if (getFrom() != null) {
                buf.append(" from=\"").append(getFrom()).append("\"");
            }
            buf.append(">");
            if (getReason() != null) {
                buf.append("<reason>").append(getReason()).append("</reason>");
            }
            buf.append("</decline>");
            return buf.toString();
        }
    }

    public static class Destroy {
        private String jid;
        private String reason;

        public String getJid() {
            return this.jid;
        }

        public String getReason() {
            return this.reason;
        }

        public void setJid(String jid) {
            this.jid = jid;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<destroy");
            if (getJid() != null) {
                buf.append(" jid=\"").append(getJid()).append("\"");
            }
            if (getReason() == null) {
                buf.append("/>");
            } else {
                buf.append(">");
                if (getReason() != null) {
                    buf.append("<reason>").append(getReason()).append("</reason>");
                }
                buf.append("</destroy>");
            }
            return buf.toString();
        }
    }

    public static class Invite {
        private String from;
        private String reason;
        private String to;

        public String getFrom() {
            return this.from;
        }

        public String getReason() {
            return this.reason;
        }

        public String getTo() {
            return this.to;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<invite ");
            if (getTo() != null) {
                buf.append(" to=\"").append(getTo()).append("\"");
            }
            if (getFrom() != null) {
                buf.append(" from=\"").append(getFrom()).append("\"");
            }
            buf.append(">");
            if (getReason() != null) {
                buf.append("<reason>").append(getReason()).append("</reason>");
            }
            buf.append("</invite>");
            return buf.toString();
        }
    }

    public static class Item {
        private String actor;
        private String affiliation;
        private String jid;
        private String nick;
        private String reason;
        private String role;

        public Item(String affiliation, String role) {
            this.affiliation = affiliation;
            this.role = role;
        }

        public String getActor() {
            return this.actor == null ? "" : this.actor;
        }

        public String getReason() {
            return this.reason == null ? "" : this.reason;
        }

        public String getAffiliation() {
            return this.affiliation;
        }

        public String getJid() {
            return this.jid;
        }

        public String getNick() {
            return this.nick;
        }

        public String getRole() {
            return this.role;
        }

        public void setActor(String actor) {
            this.actor = actor;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public void setJid(String jid) {
            this.jid = jid;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<item");
            if (getAffiliation() != null) {
                buf.append(" affiliation=\"").append(getAffiliation()).append("\"");
            }
            if (getJid() != null) {
                buf.append(" jid=\"").append(getJid()).append("\"");
            }
            if (getNick() != null) {
                buf.append(" nick=\"").append(getNick()).append("\"");
            }
            if (getRole() != null) {
                buf.append(" role=\"").append(getRole()).append("\"");
            }
            if (getReason() == null && getActor() == null) {
                buf.append("/>");
            } else {
                buf.append(">");
                if (getReason() != null) {
                    buf.append("<reason>").append(getReason()).append("</reason>");
                }
                if (getActor() != null) {
                    buf.append("<actor jid=\"").append(getActor()).append("\"/>");
                }
                buf.append("</item>");
            }
            return buf.toString();
        }
    }

    public static class Status {
        private String code;

        public Status(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<status code=\"").append(getCode()).append("\"/>");
            return buf.toString();
        }
    }

    public String getElementName() {
        return GroupChatInvitation.ELEMENT_NAME;
    }

    public String getNamespace() {
        return "http://jabber.org/protocol/muc#user";
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
        if (getInvite() != null) {
            buf.append(getInvite().toXML());
        }
        if (getDecline() != null) {
            buf.append(getDecline().toXML());
        }
        if (getItem() != null) {
            buf.append(getItem().toXML());
        }
        if (getPassword() != null) {
            buf.append("<password>").append(getPassword()).append("</password>");
        }
        if (getStatus() != null) {
            buf.append(getStatus().toXML());
        }
        if (getDestroy() != null) {
            buf.append(getDestroy().toXML());
        }
        buf.append("</").append(getElementName()).append(">");
        return buf.toString();
    }

    public Invite getInvite() {
        return this.invite;
    }

    public Decline getDecline() {
        return this.decline;
    }

    public Item getItem() {
        return this.item;
    }

    public String getPassword() {
        return this.password;
    }

    public Status getStatus() {
        return this.status;
    }

    public Destroy getDestroy() {
        return this.destroy;
    }

    public void setInvite(Invite invite) {
        this.invite = invite;
    }

    public void setDecline(Decline decline) {
        this.decline = decline;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setPassword(String string) {
        this.password = string;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDestroy(Destroy destroy) {
        this.destroy = destroy;
    }
}
