package downloader.exception;

public class FileWriteException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public FileWriteException(Throwable e) {
        super(e);
    }

    public FileWriteException(String errorMsg) {
        super(errorMsg);
    }
}
