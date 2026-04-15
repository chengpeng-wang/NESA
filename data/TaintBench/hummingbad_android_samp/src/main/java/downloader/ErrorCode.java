package downloader;

public interface ErrorCode {
    public static final int FILE_IO_ERROR = 4;
    public static final int FILE_LENGTH_NOT_MATCH = 10;
    public static final int FILE_NOT_FOUND = 14;
    public static final int FILE_WRITE_ERROR = 13;
    public static final int HTTP_ERROR = 1;
    public static final int HTTP_IO_ERROR = 6;
    public static final int HTTP_REQUESTED_RANGE_NOT_SATISFIABLE = 9;
    public static final int HTTP_SOCKET_TIMEOUT = 12;
    public static final int HTTP_TOO_MANY_REDIRECT_TIMES = 8;
    public static final int HTTP_UNKNOWNHOST_ERROR = 7;
    public static final int INTERRUPTED_ERROR = 3;
    public static final int PROTOCOL_ERROR = 2;
    public static final int TRANSFER_ERROR = 11;
    public static final int UNKNOWN_ERROR = 0;
}
