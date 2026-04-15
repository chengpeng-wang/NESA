package downloader.exception;

public class HttpIoException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public HttpIoException(Throwable e) {
        super(e);
    }
}
