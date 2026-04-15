package org.jivesoftware.smackx.muc;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

public interface InvitationListener {
    void invitationReceived(XMPPConnection xMPPConnection, String str, String str2, String str3, String str4, Message message);
}
