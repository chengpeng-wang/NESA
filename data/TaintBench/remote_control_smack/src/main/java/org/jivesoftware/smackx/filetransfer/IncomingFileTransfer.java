package org.jivesoftware.smackx.filetransfer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;

public class IncomingFileTransfer extends FileTransfer {
    /* access modifiers changed from: private */
    public InputStream inputStream;
    /* access modifiers changed from: private */
    public FileTransferRequest recieveRequest;

    protected IncomingFileTransfer(FileTransferRequest request, FileTransferNegotiator transferNegotiator) {
        super(request.getRequestor(), request.getStreamID(), transferNegotiator);
        this.recieveRequest = request;
    }

    public InputStream recieveFile() throws XMPPException {
        if (this.inputStream != null) {
            throw new IllegalStateException("Transfer already negotiated!");
        }
        try {
            this.inputStream = negotiateStream();
            return this.inputStream;
        } catch (XMPPException e) {
            setException(e);
            throw e;
        }
    }

    public void recieveFile(final File file) throws XMPPException {
        if (file != null) {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new XMPPException("Could not create file to write too", e);
                }
            }
            if (file.canWrite()) {
                new Thread(new Runnable() {
                    /* JADX WARNING: Removed duplicated region for block: B:10:0x0034  */
                    /* JADX WARNING: Removed duplicated region for block: B:13:0x0043 A:{SYNTHETIC, Splitter:B:13:0x0043} */
                    /* JADX WARNING: Removed duplicated region for block: B:31:? A:{SYNTHETIC, RETURN} */
                    /* JADX WARNING: Removed duplicated region for block: B:16:0x004e A:{SYNTHETIC, Splitter:B:16:0x004e} */
                    /* JADX WARNING: Removed duplicated region for block: B:10:0x0034  */
                    /* JADX WARNING: Removed duplicated region for block: B:13:0x0043 A:{SYNTHETIC, Splitter:B:13:0x0043} */
                    /* JADX WARNING: Removed duplicated region for block: B:16:0x004e A:{SYNTHETIC, Splitter:B:16:0x004e} */
                    /* JADX WARNING: Removed duplicated region for block: B:31:? A:{SYNTHETIC, RETURN} */
                    /* JADX WARNING: Removed duplicated region for block: B:10:0x0034  */
                    /* JADX WARNING: Removed duplicated region for block: B:13:0x0043 A:{SYNTHETIC, Splitter:B:13:0x0043} */
                    /* JADX WARNING: Removed duplicated region for block: B:31:? A:{SYNTHETIC, RETURN} */
                    /* JADX WARNING: Removed duplicated region for block: B:16:0x004e A:{SYNTHETIC, Splitter:B:16:0x004e} */
                    /* JADX WARNING: Removed duplicated region for block: B:10:0x0034  */
                    /* JADX WARNING: Removed duplicated region for block: B:13:0x0043 A:{SYNTHETIC, Splitter:B:13:0x0043} */
                    /* JADX WARNING: Removed duplicated region for block: B:16:0x004e A:{SYNTHETIC, Splitter:B:16:0x004e} */
                    /* JADX WARNING: Removed duplicated region for block: B:31:? A:{SYNTHETIC, RETURN} */
                    public void run() {
                        /*
                        r5 = this;
                        r3 = org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.this;	 Catch:{ XMPPException -> 0x0052 }
                        r4 = org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.this;	 Catch:{ XMPPException -> 0x0052 }
                        r4 = r4.negotiateStream();	 Catch:{ XMPPException -> 0x0052 }
                        r3.inputStream = r4;	 Catch:{ XMPPException -> 0x0052 }
                        r1 = 0;
                        r2 = new java.io.FileOutputStream;	 Catch:{ XMPPException -> 0x0059, FileNotFoundException -> 0x006e }
                        r3 = r6;	 Catch:{ XMPPException -> 0x0059, FileNotFoundException -> 0x006e }
                        r2.<init>(r3);	 Catch:{ XMPPException -> 0x0059, FileNotFoundException -> 0x006e }
                        r3 = org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.this;	 Catch:{ XMPPException -> 0x008a, FileNotFoundException -> 0x0087 }
                        r4 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.in_progress;	 Catch:{ XMPPException -> 0x008a, FileNotFoundException -> 0x0087 }
                        r3.setStatus(r4);	 Catch:{ XMPPException -> 0x008a, FileNotFoundException -> 0x0087 }
                        r3 = org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.this;	 Catch:{ XMPPException -> 0x008a, FileNotFoundException -> 0x0087 }
                        r4 = org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.this;	 Catch:{ XMPPException -> 0x008a, FileNotFoundException -> 0x0087 }
                        r4 = r4.inputStream;	 Catch:{ XMPPException -> 0x008a, FileNotFoundException -> 0x0087 }
                        r3.writeToStream(r4, r2);	 Catch:{ XMPPException -> 0x008a, FileNotFoundException -> 0x0087 }
                        r1 = r2;
                    L_0x0026:
                        r3 = org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.this;
                        r3 = r3.getStatus();
                        r4 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.in_progress;
                        r3 = r3.equals(r4);
                        if (r3 == 0) goto L_0x003b;
                    L_0x0034:
                        r3 = org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.this;
                        r4 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.complete;
                        r3.setStatus(r4);
                    L_0x003b:
                        r3 = org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.this;
                        r3 = r3.inputStream;
                        if (r3 == 0) goto L_0x004c;
                    L_0x0043:
                        r3 = org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.this;	 Catch:{ Throwable -> 0x0085 }
                        r3 = r3.inputStream;	 Catch:{ Throwable -> 0x0085 }
                        r3.close();	 Catch:{ Throwable -> 0x0085 }
                    L_0x004c:
                        if (r1 == 0) goto L_0x0051;
                    L_0x004e:
                        r1.close();	 Catch:{ Throwable -> 0x0083 }
                    L_0x0051:
                        return;
                    L_0x0052:
                        r0 = move-exception;
                        r3 = org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.this;
                        r3.handleXMPPException(r0);
                        goto L_0x0051;
                    L_0x0059:
                        r0 = move-exception;
                    L_0x005a:
                        r3 = org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.this;
                        r4 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.error;
                        r3.setStatus(r4);
                        r3 = org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.this;
                        r4 = org.jivesoftware.smackx.filetransfer.FileTransfer.Error.stream;
                        r3.setError(r4);
                        r3 = org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.this;
                        r3.setException(r0);
                        goto L_0x0026;
                    L_0x006e:
                        r0 = move-exception;
                    L_0x006f:
                        r3 = org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.this;
                        r4 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.error;
                        r3.setStatus(r4);
                        r3 = org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.this;
                        r4 = org.jivesoftware.smackx.filetransfer.FileTransfer.Error.bad_file;
                        r3.setError(r4);
                        r3 = org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.this;
                        r3.setException(r0);
                        goto L_0x0026;
                    L_0x0083:
                        r3 = move-exception;
                        goto L_0x0051;
                    L_0x0085:
                        r3 = move-exception;
                        goto L_0x004c;
                    L_0x0087:
                        r0 = move-exception;
                        r1 = r2;
                        goto L_0x006f;
                    L_0x008a:
                        r0 = move-exception;
                        r1 = r2;
                        goto L_0x005a;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smackx.filetransfer.IncomingFileTransfer$AnonymousClass1.run():void");
                    }
                }, "File Transfer " + this.streamID).start();
                return;
            }
            throw new IllegalArgumentException("Cannot write to provided file");
        }
        throw new IllegalArgumentException("File cannot be null");
    }

    /* access modifiers changed from: private */
    public void handleXMPPException(XMPPException e) {
        setStatus(Status.error);
        setException(e);
    }

    /* access modifiers changed from: private */
    public InputStream negotiateStream() throws XMPPException {
        setStatus(Status.negotiating_transfer);
        final StreamNegotiator streamNegotiator = this.negotiator.selectStreamNegotiator(this.recieveRequest);
        setStatus(Status.negotiating_stream);
        FutureTask<InputStream> streamNegotiatorTask = new FutureTask(new Callable<InputStream>() {
            public InputStream call() throws Exception {
                return streamNegotiator.createIncomingStream(IncomingFileTransfer.this.recieveRequest.getStreamInitiation());
            }
        });
        streamNegotiatorTask.run();
        try {
            InputStream inputStream = (InputStream) streamNegotiatorTask.get(15, TimeUnit.SECONDS);
            streamNegotiatorTask.cancel(true);
            setStatus(Status.negotiated);
            return inputStream;
        } catch (InterruptedException e) {
            throw new XMPPException("Interruption while executing", e);
        } catch (ExecutionException e2) {
            throw new XMPPException("Error in execution", e2);
        } catch (TimeoutException e22) {
            throw new XMPPException("Request timed out", e22);
        } catch (Throwable th) {
            streamNegotiatorTask.cancel(true);
        }
    }

    public void cancel() {
        setStatus(Status.cancelled);
    }
}
