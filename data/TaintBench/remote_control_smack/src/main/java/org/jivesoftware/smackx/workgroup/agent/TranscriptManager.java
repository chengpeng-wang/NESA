package org.jivesoftware.smackx.workgroup.agent;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smackx.workgroup.packet.Transcript;
import org.jivesoftware.smackx.workgroup.packet.Transcripts;

public class TranscriptManager {
    private XMPPConnection connection;

    public TranscriptManager(XMPPConnection connection) {
        this.connection = connection;
    }

    public Transcript getTranscript(String workgroupJID, String sessionID) throws XMPPException {
        Transcript request = new Transcript(sessionID);
        request.setTo(workgroupJID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        Transcript response = (Transcript) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() == null) {
            return response;
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public Transcripts getTranscripts(String workgroupJID, String userID) throws XMPPException {
        Transcripts request = new Transcripts(userID);
        request.setTo(workgroupJID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        Transcripts response = (Transcripts) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() == null) {
            return response;
        } else {
            throw new XMPPException(response.getError());
        }
    }
}
