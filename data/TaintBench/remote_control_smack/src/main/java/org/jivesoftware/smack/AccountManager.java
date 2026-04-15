package org.jivesoftware.smack;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public class AccountManager {
    private boolean accountCreationSupported = false;
    private XMPPConnection connection;
    private Registration info = null;

    public AccountManager(XMPPConnection connection) {
        this.connection = connection;
    }

    /* access modifiers changed from: 0000 */
    public void setSupportsAccountCreation(boolean accountCreationSupported) {
        this.accountCreationSupported = accountCreationSupported;
    }

    public boolean supportsAccountCreation() {
        boolean z = true;
        if (this.accountCreationSupported) {
            return true;
        }
        try {
            if (this.info == null) {
                getRegistrationInfo();
                if (this.info.getType() == Type.ERROR) {
                    z = false;
                }
                this.accountCreationSupported = z;
            }
            return this.accountCreationSupported;
        } catch (XMPPException e) {
            return false;
        }
    }

    public Collection<String> getAccountAttributes() {
        try {
            if (this.info == null) {
                getRegistrationInfo();
            }
            Map<String, String> attributes = this.info.getAttributes();
            if (attributes != null) {
                return Collections.unmodifiableSet(attributes.keySet());
            }
        } catch (XMPPException xe) {
            xe.printStackTrace();
        }
        return Collections.emptySet();
    }

    public String getAccountAttribute(String name) {
        try {
            if (this.info == null) {
                getRegistrationInfo();
            }
            return (String) this.info.getAttributes().get(name);
        } catch (XMPPException xe) {
            xe.printStackTrace();
            return null;
        }
    }

    public String getAccountInstructions() {
        try {
            if (this.info == null) {
                getRegistrationInfo();
            }
            return this.info.getInstructions();
        } catch (XMPPException e) {
            return null;
        }
    }

    public void createAccount(String username, String password) throws XMPPException {
        if (supportsAccountCreation()) {
            Map<String, String> attributes = new HashMap();
            for (String attributeName : getAccountAttributes()) {
                attributes.put(attributeName, "");
            }
            createAccount(username, password, attributes);
            return;
        }
        throw new XMPPException("Server does not support account creation.");
    }

    public void createAccount(String username, String password, Map<String, String> attributes) throws XMPPException {
        if (supportsAccountCreation()) {
            Registration reg = new Registration();
            reg.setType(Type.SET);
            reg.setTo(this.connection.getServiceName());
            attributes.put("username", username);
            attributes.put("password", password);
            reg.setAttributes(attributes);
            PacketCollector collector = this.connection.createPacketCollector(new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class)));
            this.connection.sendPacket(reg);
            IQ result = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
            collector.cancel();
            if (result == null) {
                throw new XMPPException("No response from server.");
            } else if (result.getType() == Type.ERROR) {
                throw new XMPPException(result.getError());
            } else {
                return;
            }
        }
        throw new XMPPException("Server does not support account creation.");
    }

    public void changePassword(String newPassword) throws XMPPException {
        Registration reg = new Registration();
        reg.setType(Type.SET);
        reg.setTo(this.connection.getServiceName());
        Map<String, String> map = new HashMap();
        map.put("username", StringUtils.parseName(this.connection.getUser()));
        map.put("password", newPassword);
        reg.setAttributes(map);
        PacketCollector collector = this.connection.createPacketCollector(new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class)));
        this.connection.sendPacket(reg);
        IQ result = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (result == null) {
            throw new XMPPException("No response from server.");
        } else if (result.getType() == Type.ERROR) {
            throw new XMPPException(result.getError());
        }
    }

    public void deleteAccount() throws XMPPException {
        if (this.connection.isAuthenticated()) {
            Registration reg = new Registration();
            reg.setType(Type.SET);
            reg.setTo(this.connection.getServiceName());
            Map<String, String> attributes = new HashMap();
            attributes.put(Item.REMOVE_ACTION, "");
            reg.setAttributes(attributes);
            PacketCollector collector = this.connection.createPacketCollector(new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class)));
            this.connection.sendPacket(reg);
            IQ result = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
            collector.cancel();
            if (result == null) {
                throw new XMPPException("No response from server.");
            } else if (result.getType() == Type.ERROR) {
                throw new XMPPException(result.getError());
            } else {
                return;
            }
        }
        throw new IllegalStateException("Must be logged in to delete a account.");
    }

    private synchronized void getRegistrationInfo() throws XMPPException {
        Registration reg = new Registration();
        reg.setTo(this.connection.getServiceName());
        PacketCollector collector = this.connection.createPacketCollector(new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class)));
        this.connection.sendPacket(reg);
        IQ result = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (result == null) {
            throw new XMPPException("No response from server.");
        } else if (result.getType() == Type.ERROR) {
            throw new XMPPException(result.getError());
        } else {
            this.info = (Registration) result;
        }
    }
}
