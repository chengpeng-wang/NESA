package org.jivesoftware.smackx.filetransfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jivesoftware.smack.XMPPException;

public abstract class FileTransfer {
    private static final int BUFFER_SIZE = 8192;
    protected long amountWritten = -1;
    private Error error;
    private Exception exception;
    private String fileName;
    private String filePath;
    private long fileSize;
    protected FileTransferNegotiator negotiator;
    private String peer;
    private Status status = Status.initial;
    private final Object statusMonitor = new Object();
    protected String streamID;

    public enum Error {
        none("No error"),
        not_acceptable("The peer did not find any of the provided stream mechanisms acceptable."),
        bad_file("The provided file to transfer does not exist or could not be read."),
        no_response("The remote user did not respond or the connection timed out."),
        connection("An error occured over the socket connected to send the file."),
        stream("An error occured while sending or recieving the file.");
        
        private final String msg;

        private Error(String msg) {
            this.msg = msg;
        }

        public String getMessage() {
            return this.msg;
        }

        public String toString() {
            return this.msg;
        }
    }

    public enum Status {
        error("Error"),
        initial("Initial"),
        negotiating_transfer("Negotiating Transfer"),
        refused("Refused"),
        negotiating_stream("Negotiating Stream"),
        negotiated("Negotiated"),
        in_progress("In Progress"),
        complete("Complete"),
        cancelled("Cancelled");
        
        private String status;

        private Status(String status) {
            this.status = status;
        }

        public String toString() {
            return this.status;
        }
    }

    public abstract void cancel();

    protected FileTransfer(String peer, String streamID, FileTransferNegotiator negotiator) {
        this.peer = peer;
        this.streamID = streamID;
        this.negotiator = negotiator;
    }

    /* access modifiers changed from: protected */
    public void setFileInfo(String fileName, long fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    /* access modifiers changed from: protected */
    public void setFileInfo(String path, String fileName, long fileSize) {
        this.filePath = path;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getPeer() {
        return this.peer;
    }

    public double getProgress() {
        if (this.amountWritten <= 0 || this.fileSize <= 0) {
            return 0.0d;
        }
        return ((double) this.amountWritten) / ((double) this.fileSize);
    }

    public boolean isDone() {
        return this.status == Status.cancelled || this.status == Status.error || this.status == Status.complete || this.status == Status.refused;
    }

    public Status getStatus() {
        return this.status;
    }

    /* access modifiers changed from: protected */
    public void setError(Error type) {
        this.error = type;
    }

    public Error getError() {
        return this.error;
    }

    public Exception getException() {
        return this.exception;
    }

    /* access modifiers changed from: protected */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    /* access modifiers changed from: protected */
    public void setStatus(Status status) {
        synchronized (this.statusMonitor) {
            this.status = status;
        }
    }

    /* access modifiers changed from: protected */
    public boolean updateStatus(Status oldStatus, Status newStatus) {
        boolean z;
        synchronized (this.statusMonitor) {
            if (oldStatus != this.status) {
                z = false;
            } else {
                this.status = newStatus;
                z = true;
            }
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void writeToStream(InputStream in, OutputStream out) throws XMPPException {
        byte[] b = new byte[8192];
        int count = 0;
        this.amountWritten = 0;
        do {
            try {
                out.write(b, 0, count);
                this.amountWritten += (long) count;
                try {
                    count = in.read(b);
                    if (count == -1) {
                        break;
                    }
                } catch (IOException e) {
                    throw new XMPPException("error reading from input stream", e);
                }
            } catch (IOException e2) {
                throw new XMPPException("error writing to output stream", e2);
            }
        } while (!getStatus().equals(Status.cancelled));
        if (!getStatus().equals(Status.cancelled) && getError() == Error.none && this.amountWritten != this.fileSize) {
            setStatus(Status.error);
            this.error = Error.connection;
        }
    }

    public long getAmountWritten() {
        return this.amountWritten;
    }
}
