package org.jivesoftware.smackx.filetransfer;

import org.jivesoftware.smackx.packet.StreamInitiation;

public class FileTransferRequest {
    private final FileTransferManager manager;
    private final StreamInitiation streamInitiation;

    public FileTransferRequest(FileTransferManager manager, StreamInitiation si) {
        this.streamInitiation = si;
        this.manager = manager;
    }

    public String getFileName() {
        return this.streamInitiation.getFile().getName();
    }

    public long getFileSize() {
        return this.streamInitiation.getFile().getSize();
    }

    public String getDescription() {
        return this.streamInitiation.getFile().getDesc();
    }

    public String getMimeType() {
        return this.streamInitiation.getMimeType();
    }

    public String getRequestor() {
        return this.streamInitiation.getFrom();
    }

    public String getStreamID() {
        return this.streamInitiation.getSessionID();
    }

    /* access modifiers changed from: protected */
    public StreamInitiation getStreamInitiation() {
        return this.streamInitiation;
    }

    public IncomingFileTransfer accept() {
        return this.manager.createIncomingFileTransfer(this);
    }

    public void reject() {
        this.manager.rejectIncomingFileTransfer(this);
    }
}
