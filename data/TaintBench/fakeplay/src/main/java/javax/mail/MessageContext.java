package javax.mail;

public class MessageContext {
    private Part part;

    public MessageContext(Part part) {
        this.part = part;
    }

    public Part getPart() {
        return this.part;
    }

    public Message getMessage() {
        try {
            return getMessage(this.part);
        } catch (MessagingException e) {
            return null;
        }
    }

    private static Message getMessage(Part p) throws MessagingException {
        while (p != null) {
            if (p instanceof Message) {
                return (Message) p;
            }
            Multipart mp = ((BodyPart) p).getParent();
            if (mp == null) {
                return null;
            }
            p = mp.getParent();
        }
        return null;
    }

    public Session getSession() {
        Message msg = getMessage();
        return msg != null ? msg.session : null;
    }
}
