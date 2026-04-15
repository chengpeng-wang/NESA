package javax.mail;

import javax.mail.FetchProfile.Item;

public interface UIDFolder {
    public static final long LASTUID = -1;

    public static class FetchProfileItem extends Item {
        public static final FetchProfileItem UID = new FetchProfileItem("UID");

        protected FetchProfileItem(String name) {
            super(name);
        }
    }

    Message getMessageByUID(long j) throws MessagingException;

    Message[] getMessagesByUID(long j, long j2) throws MessagingException;

    Message[] getMessagesByUID(long[] jArr) throws MessagingException;

    long getUID(Message message) throws MessagingException;

    long getUIDValidity() throws MessagingException;
}
