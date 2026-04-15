package org.jivesoftware.smackx;

import java.util.List;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.jivesoftware.smackx.packet.MultipleAddresses.Address;

public class MultipleRecipientInfo {
    MultipleAddresses extension;

    MultipleRecipientInfo(MultipleAddresses extension) {
        this.extension = extension;
    }

    public List getTOAddresses() {
        return this.extension.getAddressesOfType("to");
    }

    public List getCCAddresses() {
        return this.extension.getAddressesOfType(MultipleAddresses.CC);
    }

    public String getReplyRoom() {
        List replyRoom = this.extension.getAddressesOfType(MultipleAddresses.REPLY_ROOM);
        return replyRoom.isEmpty() ? null : ((Address) replyRoom.get(0)).getJid();
    }

    public boolean shouldNotReply() {
        return !this.extension.getAddressesOfType(MultipleAddresses.NO_REPLY).isEmpty();
    }

    public Address getReplyAddress() {
        List replyTo = this.extension.getAddressesOfType(MultipleAddresses.REPLY_TO);
        return replyTo.isEmpty() ? null : (Address) replyTo.get(0);
    }
}
