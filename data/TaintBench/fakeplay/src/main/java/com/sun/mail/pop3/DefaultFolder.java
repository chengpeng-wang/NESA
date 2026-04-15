package com.sun.mail.pop3;

import com.googleprojects.mm.MMMailContentUtil;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.MethodNotSupportedException;

public class DefaultFolder extends Folder {
    DefaultFolder(POP3Store store) {
        super(store);
    }

    public String getName() {
        return MMMailContentUtil.MM_MESSAGE_SUBJECT;
    }

    public String getFullName() {
        return MMMailContentUtil.MM_MESSAGE_SUBJECT;
    }

    public Folder getParent() {
        return null;
    }

    public boolean exists() {
        return true;
    }

    public Folder[] list(String pattern) throws MessagingException {
        return new Folder[]{getInbox()};
    }

    public char getSeparator() {
        return '/';
    }

    public int getType() {
        return 2;
    }

    public boolean create(int type) throws MessagingException {
        return false;
    }

    public boolean hasNewMessages() throws MessagingException {
        return false;
    }

    public Folder getFolder(String name) throws MessagingException {
        if (name.equalsIgnoreCase("INBOX")) {
            return getInbox();
        }
        throw new MessagingException("only INBOX supported");
    }

    /* access modifiers changed from: protected */
    public Folder getInbox() throws MessagingException {
        return getStore().getFolder("INBOX");
    }

    public boolean delete(boolean recurse) throws MessagingException {
        throw new MethodNotSupportedException("delete");
    }

    public boolean renameTo(Folder f) throws MessagingException {
        throw new MethodNotSupportedException("renameTo");
    }

    public void open(int mode) throws MessagingException {
        throw new MethodNotSupportedException("open");
    }

    public void close(boolean expunge) throws MessagingException {
        throw new MethodNotSupportedException("close");
    }

    public boolean isOpen() {
        return false;
    }

    public Flags getPermanentFlags() {
        return new Flags();
    }

    public int getMessageCount() throws MessagingException {
        return 0;
    }

    public Message getMessage(int msgno) throws MessagingException {
        throw new MethodNotSupportedException("getMessage");
    }

    public void appendMessages(Message[] msgs) throws MessagingException {
        throw new MethodNotSupportedException("Append not supported");
    }

    public Message[] expunge() throws MessagingException {
        throw new MethodNotSupportedException("expunge");
    }
}
