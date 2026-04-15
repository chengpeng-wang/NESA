package org.jivesoftware.smackx.muc;

import java.util.Iterator;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.packet.DiscoverInfo;

public class RoomInfo {
    private String description = "";
    private boolean membersOnly;
    private boolean moderated;
    private boolean nonanonymous;
    private int occupantsCount = -1;
    private boolean passwordProtected;
    private boolean persistent;
    private String room;
    private String subject = "";

    RoomInfo(DiscoverInfo info) {
        this.room = info.getFrom();
        this.membersOnly = info.containsFeature("muc_membersonly");
        this.moderated = info.containsFeature("muc_moderated");
        this.nonanonymous = info.containsFeature("muc_nonanonymous");
        this.passwordProtected = info.containsFeature("muc_passwordprotected");
        this.persistent = info.containsFeature("muc_persistent");
        Form form = Form.getFormFrom(info);
        if (form != null) {
            this.description = (String) form.getField("muc#roominfo_description").getValues().next();
            Iterator<String> values = form.getField("muc#roominfo_subject").getValues();
            if (values.hasNext()) {
                this.subject = (String) values.next();
            } else {
                this.subject = "";
            }
            this.occupantsCount = Integer.parseInt((String) form.getField("muc#roominfo_occupants").getValues().next());
        }
    }

    public String getRoom() {
        return this.room;
    }

    public String getDescription() {
        return this.description;
    }

    public String getSubject() {
        return this.subject;
    }

    public int getOccupantsCount() {
        return this.occupantsCount;
    }

    public boolean isMembersOnly() {
        return this.membersOnly;
    }

    public boolean isModerated() {
        return this.moderated;
    }

    public boolean isNonanonymous() {
        return this.nonanonymous;
    }

    public boolean isPasswordProtected() {
        return this.passwordProtected;
    }

    public boolean isPersistent() {
        return this.persistent;
    }
}
