package downloader.exception;

public class FileIoException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public FileIoException(Throwable e) {
        super(e);
    }
}
