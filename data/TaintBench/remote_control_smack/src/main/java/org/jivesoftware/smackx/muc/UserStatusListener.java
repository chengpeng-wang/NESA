package org.jivesoftware.smackx.muc;

public interface UserStatusListener {
    void adminGranted();

    void adminRevoked();

    void banned(String str, String str2);

    void kicked(String str, String str2);

    void membershipGranted();

    void membershipRevoked();

    void moderatorGranted();

    void moderatorRevoked();

    void ownershipGranted();

    void ownershipRevoked();

    void voiceGranted();

    void voiceRevoked();
}
