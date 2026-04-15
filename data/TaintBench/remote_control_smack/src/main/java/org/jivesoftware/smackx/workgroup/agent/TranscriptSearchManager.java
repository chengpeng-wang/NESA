package org.jivesoftware.smackx.workgroup.agent;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.workgroup.packet.TranscriptSearch;

public class TranscriptSearchManager {
    private XMPPConnection connection;

    public TranscriptSearchManager(XMPPConnection connection) {
        this.connection = connection;
    }

    public Form getSearchForm(String serviceJID) throws XMPPException {
        TranscriptSearch search = new TranscriptSearch();
        search.setType(Type.GET);
        search.setTo(serviceJID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(search.getPacketID()));
        this.connection.sendPacket(search);
        TranscriptSearch response = (TranscriptSearch) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() == null) {
            return Form.getFormFrom(response);
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public ReportedData submitSearch(String serviceJID, Form completedForm) throws XMPPException {
        TranscriptSearch search = new TranscriptSearch();
        search.setType(Type.GET);
        search.setTo(serviceJID);
        search.addExtension(completedForm.getDataFormToSend());
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(search.getPacketID()));
        this.connection.sendPacket(search);
        TranscriptSearch response = (TranscriptSearch) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() == null) {
            return ReportedData.getReportedDataFrom(response);
        } else {
            throw new XMPPException(response.getError());
        }
    }
}
