package org.jivesoftware.smackx.muc;

public interface ParticipantStatusListener {
    void adminGranted(String str);

    void adminRevoked(String str);

    void banned(String str, String str2, String str3);

    void joined(String str);

    void kicked(String str, String str2, String str3);

    void left(String str);

    void membershipGranted(String str);

    void membershipRevoked(String str);

    void moderatorGranted(String str);

    void moderatorRevoked(String str);

    void nicknameChanged(String str, String str2);

    void ownershipGranted(String str);

    void ownershipRevoked(String str);

    void voiceGranted(String str);

    void voiceRevoked(String str);
}
