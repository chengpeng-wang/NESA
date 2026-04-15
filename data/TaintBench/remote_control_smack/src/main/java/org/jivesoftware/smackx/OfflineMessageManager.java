package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;
import org.jivesoftware.smackx.packet.MessageEvent;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;

public class OfflineMessageManager {
    private static final String namespace = "http://jabber.org/protocol/offline";
    private XMPPConnection connection;
    private PacketFilter packetFilter = new AndFilter(new PacketExtensionFilter(MessageEvent.OFFLINE, namespace), new PacketTypeFilter(Message.class));

    public OfflineMessageManager(XMPPConnection connection) {
        this.connection = connection;
    }

    public boolean supportsFlexibleRetrieval() throws XMPPException {
        return ServiceDiscoveryManager.getInstanceFor(this.connection).discoverInfo(null).containsFeature(namespace);
    }

    public int getMessageCount() throws XMPPException {
        Form extendedInfo = Form.getFormFrom(ServiceDiscoveryManager.getInstanceFor(this.connection).discoverInfo(null, namespace));
        if (extendedInfo != null) {
            return Integer.parseInt((String) extendedInfo.getField("number_of_messages").getValues().next());
        }
        return 0;
    }

    public Iterator<OfflineMessageHeader> getHeaders() throws XMPPException {
        List<OfflineMessageHeader> answer = new ArrayList();
        Iterator it = ServiceDiscoveryManager.getInstanceFor(this.connection).discoverItems(null, namespace).getItems();
        while (it.hasNext()) {
            answer.add(new OfflineMessageHeader((Item) it.next()));
        }
        return answer.iterator();
    }

    public Iterator<Message> getMessages(final List<String> nodes) throws XMPPException {
        List<Message> messages = new ArrayList();
        OfflineMessageRequest request = new OfflineMessageRequest();
        for (String node : nodes) {
            OfflineMessageRequest.Item item = new OfflineMessageRequest.Item(node);
            item.setAction("view");
            request.addItem(item);
        }
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        PacketCollector messageCollector = this.connection.createPacketCollector(new AndFilter(this.packetFilter, new PacketFilter() {
            public boolean accept(Packet packet) {
                return nodes.contains(((OfflineMessageInfo) packet.getExtension(MessageEvent.OFFLINE, OfflineMessageManager.namespace)).getNode());
            }
        }));
        this.connection.sendPacket(request);
        IQ answer = (IQ) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        } else {
            for (Message message = (Message) messageCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout()); message != null; message = (Message) messageCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout())) {
                messages.add(message);
            }
            messageCollector.cancel();
            return messages.iterator();
        }
    }

    public Iterator<Message> getMessages() throws XMPPException {
        List<Message> messages = new ArrayList();
        OfflineMessageRequest request = new OfflineMessageRequest();
        request.setFetch(true);
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        PacketCollector messageCollector = this.connection.createPacketCollector(this.packetFilter);
        this.connection.sendPacket(request);
        IQ answer = (IQ) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        } else {
            for (Message message = (Message) messageCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout()); message != null; message = (Message) messageCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout())) {
                messages.add(message);
            }
            messageCollector.cancel();
            return messages.iterator();
        }
    }

    public void deleteMessages(List<String> nodes) throws XMPPException {
        OfflineMessageRequest request = new OfflineMessageRequest();
        for (String node : nodes) {
            OfflineMessageRequest.Item item = new OfflineMessageRequest.Item(node);
            item.setAction(Item.REMOVE_ACTION);
            request.addItem(item);
        }
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        IQ answer = (IQ) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        }
    }

    public void deleteMessages() throws XMPPException {
        OfflineMessageRequest request = new OfflineMessageRequest();
        request.setPurge(true);
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        IQ answer = (IQ) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        }
    }
}
