package org.jivesoftware.smack;

import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.Authentication;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;

class NonSASLAuthentication implements UserAuthentication {
    private XMPPConnection connection;

    public NonSASLAuthentication(XMPPConnection connection) {
        this.connection = connection;
    }

    public String authenticate(String username, String password, String resource) throws XMPPException {
        Authentication discoveryAuth = new Authentication();
        discoveryAuth.setType(Type.GET);
        discoveryAuth.setUsername(username);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(discoveryAuth.getPacketID()));
        this.connection.sendPacket(discoveryAuth);
        IQ response = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        if (response == null) {
            throw new XMPPException("No response from the server.");
        } else if (response.getType() == Type.ERROR) {
            throw new XMPPException(response.getError());
        } else {
            Authentication authTypes = (Authentication) response;
            collector.cancel();
            Authentication auth = new Authentication();
            auth.setUsername(username);
            if (authTypes.getDigest() != null) {
                auth.setDigest(this.connection.getConnectionID(), password);
            } else if (authTypes.getPassword() != null) {
                auth.setPassword(password);
            } else {
                throw new XMPPException("Server does not support compatible authentication mechanism.");
            }
            auth.setResource(resource);
            collector = this.connection.createPacketCollector(new PacketIDFilter(auth.getPacketID()));
            this.connection.sendPacket(auth);
            response = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
            if (response == null) {
                throw new XMPPException("Authentication failed.");
            } else if (response.getType() == Type.ERROR) {
                throw new XMPPException(response.getError());
            } else {
                collector.cancel();
                return response.getTo();
            }
        }
    }

    public String authenticateAnonymously() throws XMPPException {
        Authentication auth = new Authentication();
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(auth.getPacketID()));
        this.connection.sendPacket(auth);
        IQ response = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        if (response == null) {
            throw new XMPPException("Anonymous login failed.");
        } else if (response.getType() == Type.ERROR) {
            throw new XMPPException(response.getError());
        } else {
            collector.cancel();
            if (response.getTo() != null) {
                return response.getTo();
            }
            return this.connection.serviceName + "/" + ((Authentication) response).getResource();
        }
    }
}
