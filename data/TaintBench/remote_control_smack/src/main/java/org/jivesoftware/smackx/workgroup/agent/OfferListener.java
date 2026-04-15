package org.jivesoftware.smackx.workgroup.agent;

public interface OfferListener {
    void offerReceived(Offer offer);

    void offerRevoked(RevokedOffer revokedOffer);
}
