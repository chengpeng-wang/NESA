package org.jivesoftware.smackx;

import java.util.List;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;

public class SharedGroupManager {
    public static List getSharedGroups(XMPPConnection connection) throws XMPPException {
        SharedGroupsInfo info = new SharedGroupsInfo();
        info.setType(Type.GET);
        PacketCollector collector = connection.createPacketCollector(new PacketIDFilter(info.getPacketID()));
        connection.sendPacket(info);
        IQ result = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (result == null) {
            throw new XMPPException("No response from the server.");
        } else if (result.getType() != Type.ERROR) {
            return ((SharedGroupsInfo) result).getGroups();
        } else {
            throw new XMPPException(result.getError());
        }
    }
}
