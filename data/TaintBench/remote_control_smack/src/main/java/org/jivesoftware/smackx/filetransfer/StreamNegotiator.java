package org.jivesoftware.smackx.filetransfer;

import java.io.InputStream;
import java.io.OutputStream;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.StreamInitiation;

public abstract class StreamNegotiator {
    public abstract void cleanup();

    public abstract InputStream createIncomingStream(StreamInitiation streamInitiation) throws XMPPException;

    public abstract OutputStream createOutgoingStream(String str, String str2, String str3) throws XMPPException;

    public abstract PacketFilter getInitiationPacketFilter(String str, String str2);

    public abstract String[] getNamespaces();

    public abstract InputStream negotiateIncomingStream(Packet packet) throws XMPPException;

    public StreamInitiation createInitiationAccept(StreamInitiation streamInitiationOffer, String[] namespaces) {
        StreamInitiation response = new StreamInitiation();
        response.setTo(streamInitiationOffer.getFrom());
        response.setFrom(streamInitiationOffer.getTo());
        response.setType(Type.RESULT);
        response.setPacketID(streamInitiationOffer.getPacketID());
        DataForm form = new DataForm(Form.TYPE_SUBMIT);
        FormField field = new FormField("stream-method");
        for (String namespace : namespaces) {
            field.addValue(namespace);
        }
        form.addField(field);
        response.setFeatureNegotiationForm(form);
        return response;
    }

    public IQ createError(String from, String to, String packetID, XMPPError xmppError) {
        IQ iq = FileTransferNegotiator.createIQ(packetID, to, from, Type.ERROR);
        iq.setError(xmppError);
        return iq;
    }

    /* access modifiers changed from: 0000 */
    public Packet initiateIncomingStream(XMPPConnection connection, StreamInitiation initiation) throws XMPPException {
        StreamInitiation response = createInitiationAccept(initiation, getNamespaces());
        PacketCollector collector = connection.createPacketCollector(getInitiationPacketFilter(initiation.getFrom(), initiation.getSessionID()));
        connection.sendPacket(response);
        Packet streamMethodInitiation = collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (streamMethodInitiation != null) {
            return streamMethodInitiation;
        }
        throw new XMPPException("No response from file transfer initiator");
    }
}
