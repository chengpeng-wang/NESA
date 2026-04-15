package org.jivesoftware.smackx.filetransfer;

import java.io.File;
import java.io.OutputStream;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Error;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;

public class OutgoingFileTransfer extends FileTransfer {
    private static int RESPONSE_TIMEOUT = 60000;
    private NegotiationProgress callback;
    private String initiator;
    /* access modifiers changed from: private */
    public OutputStream outputStream;
    private Thread transferThread;

    public interface NegotiationProgress {
        void errorEstablishingStream(Exception exception);

        void outputStreamEstablished(OutputStream outputStream);

        void statusUpdated(Status status, Status status2);
    }

    public static int getResponseTimeout() {
        return RESPONSE_TIMEOUT;
    }

    public static void setResponseTimeout(int responseTimeout) {
        RESPONSE_TIMEOUT = responseTimeout;
    }

    protected OutgoingFileTransfer(String initiator, String target, String streamID, FileTransferNegotiator transferNegotiator) {
        super(target, streamID, transferNegotiator);
        this.initiator = initiator;
    }

    /* access modifiers changed from: protected */
    public void setOutputStream(OutputStream stream) {
        if (this.outputStream == null) {
            this.outputStream = stream;
        }
    }

    /* access modifiers changed from: protected */
    public OutputStream getOutputStream() {
        if (getStatus().equals(Status.negotiated)) {
            return this.outputStream;
        }
        return null;
    }

    public synchronized OutputStream sendFile(String fileName, long fileSize, String description) throws XMPPException {
        if (isDone() || this.outputStream != null) {
            throw new IllegalStateException("The negotation process has already been attempted on this file transfer");
        }
        try {
            this.outputStream = negotiateStream(fileName, fileSize, description);
        } catch (XMPPException e) {
            handleXMPPException(e);
            throw e;
        }
        return this.outputStream;
    }

    public synchronized void sendFile(String fileName, long fileSize, String description, NegotiationProgress progress) {
        if (progress == null) {
            throw new IllegalArgumentException("Callback progress cannot be null.");
        }
        checkTransferThread();
        if (isDone() || this.outputStream != null) {
            throw new IllegalStateException("The negotation process has already been attempted for this file transfer");
        }
        this.callback = progress;
        final String str = fileName;
        final long j = fileSize;
        final String str2 = description;
        final NegotiationProgress negotiationProgress = progress;
        this.transferThread = new Thread(new Runnable() {
            public void run() {
                try {
                    OutgoingFileTransfer.this.outputStream = OutgoingFileTransfer.this.negotiateStream(str, j, str2);
                    negotiationProgress.outputStreamEstablished(OutgoingFileTransfer.this.outputStream);
                } catch (XMPPException e) {
                    OutgoingFileTransfer.this.handleXMPPException(e);
                }
            }
        }, "File Transfer Negotiation " + this.streamID);
        this.transferThread.start();
    }

    private void checkTransferThread() {
        if ((this.transferThread != null && this.transferThread.isAlive()) || isDone()) {
            throw new IllegalStateException("File transfer in progress or has already completed.");
        }
    }

    public synchronized void sendFile(final File file, final String description) throws XMPPException {
        checkTransferThread();
        if (file != null && file.exists() && file.canRead()) {
            setFileInfo(file.getAbsolutePath(), file.getName(), file.length());
            this.transferThread = new Thread(new Runnable() {
                /* JADX WARNING: Unknown top exception splitter block from list: {B:30:0x009c=Splitter:B:30:0x009c, B:22:0x006e=Splitter:B:22:0x006e} */
                /* JADX WARNING: Removed duplicated region for block: B:39:0x00c5 A:{SYNTHETIC, Splitter:B:39:0x00c5} */
                /* JADX WARNING: Removed duplicated region for block: B:25:0x0083 A:{SYNTHETIC, Splitter:B:25:0x0083} */
                /* JADX WARNING: Removed duplicated region for block: B:33:0x00aa A:{SYNTHETIC, Splitter:B:33:0x00aa} */
                public void run() {
                    /*
                    r9 = this;
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ XMPPException -> 0x0022 }
                    r4 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ XMPPException -> 0x0022 }
                    r5 = r5;	 Catch:{ XMPPException -> 0x0022 }
                    r5 = r5.getName();	 Catch:{ XMPPException -> 0x0022 }
                    r6 = r5;	 Catch:{ XMPPException -> 0x0022 }
                    r6 = r6.length();	 Catch:{ XMPPException -> 0x0022 }
                    r8 = r6;	 Catch:{ XMPPException -> 0x0022 }
                    r4 = r4.negotiateStream(r5, r6, r8);	 Catch:{ XMPPException -> 0x0022 }
                    r3.outputStream = r4;	 Catch:{ XMPPException -> 0x0022 }
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;
                    r3 = r3.outputStream;
                    if (r3 != 0) goto L_0x0029;
                L_0x0021:
                    return;
                L_0x0022:
                    r0 = move-exception;
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;
                    r3.handleXMPPException(r0);
                    goto L_0x0021;
                L_0x0029:
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;
                    r4 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.negotiated;
                    r5 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.in_progress;
                    r3 = r3.updateStatus(r4, r5);
                    if (r3 == 0) goto L_0x0021;
                L_0x0035:
                    r1 = 0;
                    r2 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x006d, XMPPException -> 0x009b }
                    r3 = r5;	 Catch:{ FileNotFoundException -> 0x006d, XMPPException -> 0x009b }
                    r2.<init>(r3);	 Catch:{ FileNotFoundException -> 0x006d, XMPPException -> 0x009b }
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ FileNotFoundException -> 0x00e3, XMPPException -> 0x00e0, all -> 0x00dd }
                    r4 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ FileNotFoundException -> 0x00e3, XMPPException -> 0x00e0, all -> 0x00dd }
                    r4 = r4.outputStream;	 Catch:{ FileNotFoundException -> 0x00e3, XMPPException -> 0x00e0, all -> 0x00dd }
                    r3.writeToStream(r2, r4);	 Catch:{ FileNotFoundException -> 0x00e3, XMPPException -> 0x00e0, all -> 0x00dd }
                    if (r2 == 0) goto L_0x004d;
                L_0x004a:
                    r2.close();	 Catch:{ IOException -> 0x006a }
                L_0x004d:
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ IOException -> 0x006a }
                    r3 = r3.outputStream;	 Catch:{ IOException -> 0x006a }
                    r3.flush();	 Catch:{ IOException -> 0x006a }
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ IOException -> 0x006a }
                    r3 = r3.outputStream;	 Catch:{ IOException -> 0x006a }
                    r3.close();	 Catch:{ IOException -> 0x006a }
                    r1 = r2;
                L_0x0060:
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;
                    r4 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.in_progress;
                    r5 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.complete;
                    r3.updateStatus(r4, r5);
                    goto L_0x0021;
                L_0x006a:
                    r3 = move-exception;
                    r1 = r2;
                    goto L_0x0060;
                L_0x006d:
                    r0 = move-exception;
                L_0x006e:
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ all -> 0x00c2 }
                    r4 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.error;	 Catch:{ all -> 0x00c2 }
                    r3.setStatus(r4);	 Catch:{ all -> 0x00c2 }
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ all -> 0x00c2 }
                    r4 = org.jivesoftware.smackx.filetransfer.FileTransfer.Error.bad_file;	 Catch:{ all -> 0x00c2 }
                    r3.setError(r4);	 Catch:{ all -> 0x00c2 }
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ all -> 0x00c2 }
                    r3.setException(r0);	 Catch:{ all -> 0x00c2 }
                    if (r1 == 0) goto L_0x0086;
                L_0x0083:
                    r1.close();	 Catch:{ IOException -> 0x0099 }
                L_0x0086:
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ IOException -> 0x0099 }
                    r3 = r3.outputStream;	 Catch:{ IOException -> 0x0099 }
                    r3.flush();	 Catch:{ IOException -> 0x0099 }
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ IOException -> 0x0099 }
                    r3 = r3.outputStream;	 Catch:{ IOException -> 0x0099 }
                    r3.close();	 Catch:{ IOException -> 0x0099 }
                    goto L_0x0060;
                L_0x0099:
                    r3 = move-exception;
                    goto L_0x0060;
                L_0x009b:
                    r0 = move-exception;
                L_0x009c:
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ all -> 0x00c2 }
                    r4 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.error;	 Catch:{ all -> 0x00c2 }
                    r3.setStatus(r4);	 Catch:{ all -> 0x00c2 }
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ all -> 0x00c2 }
                    r3.setException(r0);	 Catch:{ all -> 0x00c2 }
                    if (r1 == 0) goto L_0x00ad;
                L_0x00aa:
                    r1.close();	 Catch:{ IOException -> 0x00c0 }
                L_0x00ad:
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ IOException -> 0x00c0 }
                    r3 = r3.outputStream;	 Catch:{ IOException -> 0x00c0 }
                    r3.flush();	 Catch:{ IOException -> 0x00c0 }
                    r3 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ IOException -> 0x00c0 }
                    r3 = r3.outputStream;	 Catch:{ IOException -> 0x00c0 }
                    r3.close();	 Catch:{ IOException -> 0x00c0 }
                    goto L_0x0060;
                L_0x00c0:
                    r3 = move-exception;
                    goto L_0x0060;
                L_0x00c2:
                    r3 = move-exception;
                L_0x00c3:
                    if (r1 == 0) goto L_0x00c8;
                L_0x00c5:
                    r1.close();	 Catch:{ IOException -> 0x00db }
                L_0x00c8:
                    r4 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ IOException -> 0x00db }
                    r4 = r4.outputStream;	 Catch:{ IOException -> 0x00db }
                    r4.flush();	 Catch:{ IOException -> 0x00db }
                    r4 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ IOException -> 0x00db }
                    r4 = r4.outputStream;	 Catch:{ IOException -> 0x00db }
                    r4.close();	 Catch:{ IOException -> 0x00db }
                L_0x00da:
                    throw r3;
                L_0x00db:
                    r4 = move-exception;
                    goto L_0x00da;
                L_0x00dd:
                    r3 = move-exception;
                    r1 = r2;
                    goto L_0x00c3;
                L_0x00e0:
                    r0 = move-exception;
                    r1 = r2;
                    goto L_0x009c;
                L_0x00e3:
                    r0 = move-exception;
                    r1 = r2;
                    goto L_0x006e;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer$AnonymousClass2.run():void");
                }
            }, "File Transfer " + this.streamID);
            this.transferThread.start();
        } else {
            throw new IllegalArgumentException("Could not read file");
        }
    }

    /* access modifiers changed from: private */
    public void handleXMPPException(XMPPException e) {
        XMPPError error = e.getXMPPError();
        if (error != null) {
            int code = error.getCode();
            if (code == 403) {
                setStatus(Status.refused);
                return;
            } else if (code == 400) {
                setStatus(Status.error);
                setError(Error.not_acceptable);
            } else {
                setStatus(Status.error);
            }
        }
        setException(e);
    }

    public long getBytesSent() {
        return this.amountWritten;
    }

    /* access modifiers changed from: private */
    public OutputStream negotiateStream(String fileName, long fileSize, String description) throws XMPPException {
        if (updateStatus(Status.initial, Status.negotiating_transfer)) {
            StreamNegotiator streamNegotiator = this.negotiator.negotiateOutgoingTransfer(getPeer(), this.streamID, fileName, fileSize, description, RESPONSE_TIMEOUT);
            if (streamNegotiator == null) {
                setStatus(Status.error);
                setError(Error.no_response);
                return null;
            } else if (updateStatus(Status.negotiating_transfer, Status.negotiating_stream)) {
                this.outputStream = streamNegotiator.createOutgoingStream(this.streamID, this.initiator, getPeer());
                if (updateStatus(Status.negotiating_stream, Status.negotiated)) {
                    return this.outputStream;
                }
                throw new XMPPException("Illegal state change");
            } else {
                throw new XMPPException("Illegal state change");
            }
        }
        throw new XMPPException("Illegal state change");
    }

    public void cancel() {
        setStatus(Status.cancelled);
    }

    /* access modifiers changed from: protected */
    public boolean updateStatus(Status oldStatus, Status newStatus) {
        boolean isUpdated = super.updateStatus(oldStatus, newStatus);
        if (this.callback != null && isUpdated) {
            this.callback.statusUpdated(oldStatus, newStatus);
        }
        return isUpdated;
    }

    /* access modifiers changed from: protected */
    public void setStatus(Status status) {
        Status oldStatus = getStatus();
        super.setStatus(status);
        if (this.callback != null) {
            this.callback.statusUpdated(oldStatus, status);
        }
    }

    /* access modifiers changed from: protected */
    public void setException(Exception exception) {
        super.setException(exception);
        if (this.callback != null) {
            this.callback.errorEstablishingStream(exception);
        }
    }
}
