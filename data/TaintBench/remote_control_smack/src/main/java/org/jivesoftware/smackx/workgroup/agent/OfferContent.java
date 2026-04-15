package org.jivesoftware.smackx.workgroup.agent;

public abstract class OfferContent {
    public abstract boolean isInvitation();

    public abstract boolean isTransfer();

    public abstract boolean isUserRequest();
}
