package org.jivesoftware.smackx.workgroup.agent;

import java.util.Collection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smackx.workgroup.packet.AgentInfo;
import org.jivesoftware.smackx.workgroup.packet.AgentWorkgroups;

public class Agent {
    private XMPPConnection connection;
    private String workgroupJID;

    public static Collection getWorkgroups(String serviceJID, String agentJID, XMPPConnection connection) throws XMPPException {
        AgentWorkgroups request = new AgentWorkgroups(agentJID);
        request.setTo(serviceJID);
        PacketCollector collector = connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        connection.sendPacket(request);
        AgentWorkgroups response = (AgentWorkgroups) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() == null) {
            return response.getWorkgroups();
        } else {
            throw new XMPPException(response.getError());
        }
    }

    Agent(XMPPConnection connection, String workgroupJID) {
        this.connection = connection;
        this.workgroupJID = workgroupJID;
    }

    public String getUser() {
        return this.connection.getUser();
    }

    public String getName() throws XMPPException {
        AgentInfo agentInfo = new AgentInfo();
        agentInfo.setType(Type.GET);
        agentInfo.setTo(this.workgroupJID);
        agentInfo.setFrom(getUser());
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(agentInfo.getPacketID()));
        this.connection.sendPacket(agentInfo);
        AgentInfo response = (AgentInfo) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() == null) {
            return response.getName();
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public void setName(String newName) throws XMPPException {
        AgentInfo agentInfo = new AgentInfo();
        agentInfo.setType(Type.SET);
        agentInfo.setTo(this.workgroupJID);
        agentInfo.setFrom(getUser());
        agentInfo.setName(newName);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(agentInfo.getPacketID()));
        this.connection.sendPacket(agentInfo);
        IQ response = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() != null) {
            throw new XMPPException(response.getError());
        }
    }
}
