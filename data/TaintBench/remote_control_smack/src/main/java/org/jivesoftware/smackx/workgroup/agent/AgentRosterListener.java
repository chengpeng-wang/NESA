package org.jivesoftware.smackx.workgroup.agent;

import org.jivesoftware.smack.packet.Presence;

public interface AgentRosterListener {
    void agentAdded(String str);

    void agentRemoved(String str);

    void presenceChanged(Presence presence);
}
