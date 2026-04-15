package javax.mail;

public class FolderClosedException extends MessagingException {
    private static final long serialVersionUID = 1687879213433302315L;
    private transient Folder folder;

    public FolderClosedException(Folder folder) {
        this(folder, null);
    }

    public FolderClosedException(Folder folder, String message) {
        super(message);
        this.folder = folder;
    }

    public Folder getFolder() {
        return this.folder;
    }
}
