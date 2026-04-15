package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.Cache;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.jivesoftware.smackx.packet.MultipleAddresses.Address;

public class MultipleRecipientManager {
    private static Cache services = new Cache(100, 86400000);

    private static class PacketCopy extends Packet {
        private String text;

        public PacketCopy(String text) {
            this.text = text;
        }

        public String toXML() {
            return this.text;
        }
    }

    public static void send(XMPPConnection connection, Packet packet, List to, List cc, List bcc) throws XMPPException {
        send(connection, packet, to, cc, bcc, null, null, false);
    }

    public static void send(XMPPConnection connection, Packet packet, List to, List cc, List bcc, String replyTo, String replyRoom, boolean noReply) throws XMPPException {
        String serviceAddress = getMultipleRecipienServiceAddress(connection);
        if (serviceAddress != null) {
            sendThroughService(connection, packet, to, cc, bcc, replyTo, replyRoom, noReply, serviceAddress);
        } else if (noReply || ((replyTo != null && replyTo.trim().length() > 0) || (replyRoom != null && replyRoom.trim().length() > 0))) {
            throw new XMPPException("Extended Stanza Addressing not supported by server");
        } else {
            sendToIndividualRecipients(connection, packet, to, cc, bcc);
        }
    }

    public static void reply(XMPPConnection connection, Message original, Message reply) throws XMPPException {
        MultipleRecipientInfo info = getMultipleRecipientInfo(original);
        if (info == null) {
            throw new XMPPException("Original message does not contain multiple recipient info");
        } else if (info.shouldNotReply()) {
            throw new XMPPException("Original message should not be replied");
        } else if (info.getReplyRoom() != null) {
            throw new XMPPException("Reply should be sent through a room");
        } else {
            if (original.getThread() != null) {
                reply.setThread(original.getThread());
            }
            Address replyAddress = info.getReplyAddress();
            if (replyAddress == null || replyAddress.getJid() == null) {
                List to = new ArrayList();
                List cc = new ArrayList();
                for (Address jid : info.getTOAddresses()) {
                    to.add(jid.getJid());
                }
                for (Address jid2 : info.getCCAddresses()) {
                    cc.add(jid2.getJid());
                }
                if (!(to.contains(original.getFrom()) || cc.contains(original.getFrom()))) {
                    to.add(original.getFrom());
                }
                String from = connection.getUser();
                if (!(to.remove(from) || cc.remove(from))) {
                    String bareJID = StringUtils.parseBareAddress(from);
                    to.remove(bareJID);
                    cc.remove(bareJID);
                }
                String serviceAddress = getMultipleRecipienServiceAddress(connection);
                if (serviceAddress != null) {
                    sendThroughService(connection, reply, to, cc, null, null, null, false, serviceAddress);
                    return;
                } else {
                    sendToIndividualRecipients(connection, reply, to, cc, null);
                    return;
                }
            }
            reply.setTo(replyAddress.getJid());
            connection.sendPacket(reply);
        }
    }

    public static MultipleRecipientInfo getMultipleRecipientInfo(Packet packet) {
        MultipleAddresses extension = (MultipleAddresses) packet.getExtension("addresses", "http://jabber.org/protocol/address");
        return extension == null ? null : new MultipleRecipientInfo(extension);
    }

    private static void sendToIndividualRecipients(XMPPConnection connection, Packet packet, List to, List cc, List bcc) {
        if (to != null) {
            for (String jid : to) {
                packet.setTo(jid);
                connection.sendPacket(new PacketCopy(packet.toXML()));
            }
        }
        if (cc != null) {
            for (String jid2 : cc) {
                packet.setTo(jid2);
                connection.sendPacket(new PacketCopy(packet.toXML()));
            }
        }
        if (bcc != null) {
            for (String jid22 : bcc) {
                packet.setTo(jid22);
                connection.sendPacket(new PacketCopy(packet.toXML()));
            }
        }
    }

    private static void sendThroughService(XMPPConnection connection, Packet packet, List to, List cc, List bcc, String replyTo, String replyRoom, boolean noReply, String serviceAddress) {
        MultipleAddresses multipleAddresses = new MultipleAddresses();
        if (to != null) {
            for (String jid : to) {
                multipleAddresses.addAddress("to", jid, null, null, false, null);
            }
        }
        if (cc != null) {
            for (String jid2 : cc) {
                multipleAddresses.addAddress(MultipleAddresses.CC, jid2, null, null, false, null);
            }
        }
        if (bcc != null) {
            for (String jid22 : bcc) {
                multipleAddresses.addAddress(MultipleAddresses.BCC, jid22, null, null, false, null);
            }
        }
        if (noReply) {
            multipleAddresses.setNoReply();
        } else {
            if (replyTo != null && replyTo.trim().length() > 0) {
                multipleAddresses.addAddress(MultipleAddresses.REPLY_TO, replyTo, null, null, false, null);
            }
            if (replyRoom != null && replyRoom.trim().length() > 0) {
                multipleAddresses.addAddress(MultipleAddresses.REPLY_ROOM, replyRoom, null, null, false, null);
            }
        }
        packet.setTo(serviceAddress);
        packet.addExtension(multipleAddresses);
        connection.sendPacket(packet);
    }

    private static String getMultipleRecipienServiceAddress(XMPPConnection connection) {
        String serviceName = connection.getServiceName();
        String serviceAddress = (String) services.get(serviceName);
        if (serviceAddress == null) {
            synchronized (services) {
                serviceAddress = (String) services.get(serviceName);
                if (serviceAddress == null) {
                    try {
                        Object obj;
                        if (!ServiceDiscoveryManager.getInstanceFor(connection).discoverInfo(serviceName).containsFeature("http://jabber.org/protocol/address")) {
                            Iterator it = ServiceDiscoveryManager.getInstanceFor(connection).discoverItems(serviceName).getItems();
                            while (it.hasNext()) {
                                Item item = (Item) it.next();
                                if (ServiceDiscoveryManager.getInstanceFor(connection).discoverInfo(item.getEntityID(), item.getNode()).containsFeature("http://jabber.org/protocol/address")) {
                                    serviceAddress = serviceName;
                                    break;
                                }
                            }
                        }
                        serviceAddress = serviceName;
                        Cache cache = services;
                        if (serviceAddress == null) {
                            obj = "";
                        } else {
                            String obj2 = serviceAddress;
                        }
                        cache.put(serviceName, obj2);
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "".equals(serviceAddress) ? null : serviceAddress;
    }
}
