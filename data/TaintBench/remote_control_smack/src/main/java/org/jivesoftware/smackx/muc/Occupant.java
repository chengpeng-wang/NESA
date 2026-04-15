package org.jivesoftware.smackx.muc;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.packet.MUCAdmin.Item;
import org.jivesoftware.smackx.packet.MUCUser;

public class Occupant {
    private String affiliation;
    private String jid;
    private String nick;
    private String role;

    Occupant(Item item) {
        this.jid = item.getJid();
        this.affiliation = item.getAffiliation();
        this.role = item.getRole();
        this.nick = item.getNick();
    }

    Occupant(Presence presence) {
        MUCUser.Item item = ((MUCUser) presence.getExtension(GroupChatInvitation.ELEMENT_NAME, "http://jabber.org/protocol/muc#user")).getItem();
        this.jid = item.getJid();
        this.affiliation = item.getAffiliation();
        this.role = item.getRole();
        this.nick = StringUtils.parseResource(presence.getFrom());
    }

    public String getJid() {
        return this.jid;
    }

    public String getAffiliation() {
        return this.affiliation;
    }

    public String getRole() {
        return this.role;
    }

    public String getNick() {
        return this.nick;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Occupant)) {
            return false;
        }
        return this.jid.equals(((Occupant) obj).jid);
    }

    public int hashCode() {
        return (((((this.affiliation.hashCode() * 17) + this.role.hashCode()) * 17) + this.jid.hashCode()) * 17) + (this.nick != null ? this.nick.hashCode() : 0);
    }
}
