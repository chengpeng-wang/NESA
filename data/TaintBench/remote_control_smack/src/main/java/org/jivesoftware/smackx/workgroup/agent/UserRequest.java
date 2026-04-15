package org.jivesoftware.smackx.workgroup.agent;

public class UserRequest extends OfferContent {
    private static UserRequest instance = new UserRequest();

    public static OfferContent getInstance() {
        return instance;
    }

    /* access modifiers changed from: 0000 */
    public boolean isUserRequest() {
        return true;
    }

    /* access modifiers changed from: 0000 */
    public boolean isInvitation() {
        return false;
    }

    /* access modifiers changed from: 0000 */
    public boolean isTransfer() {
        return false;
    }
}
