package com.trilead.ssh2;

import com.trilead.ssh2.sftp.AttribFlags;
import java.io.IOException;
import java.io.InputStream;

public class StreamGobbler extends InputStream {
    /* access modifiers changed from: private */
    public byte[] buffer = new byte[2048];
    /* access modifiers changed from: private */
    public IOException exception = null;
    /* access modifiers changed from: private */
    public InputStream is;
    private boolean isClosed = false;
    /* access modifiers changed from: private */
    public boolean isEOF = false;
    /* access modifiers changed from: private */
    public int read_pos = 0;
    /* access modifiers changed from: private */
    public Object synchronizer = new Object();
    private GobblerThread t;
    /* access modifiers changed from: private */
    public int write_pos = 0;

    class GobblerThread extends Thread {
        GobblerThread() {
        }

        public void run() {
            byte[] buff = new byte[AttribFlags.SSH_FILEXFER_ATTR_LINK_COUNT];
            while (true) {
                try {
                    int avail = StreamGobbler.this.is.read(buff);
                    synchronized (StreamGobbler.this.synchronizer) {
                        if (avail <= 0) {
                            StreamGobbler.this.isEOF = true;
                            StreamGobbler.this.synchronizer.notifyAll();
                            return;
                        }
                        if (StreamGobbler.this.buffer.length - StreamGobbler.this.write_pos < avail) {
                            int unread_size = StreamGobbler.this.write_pos - StreamGobbler.this.read_pos;
                            int need_space = unread_size + avail;
                            byte[] new_buffer = StreamGobbler.this.buffer;
                            if (need_space > StreamGobbler.this.buffer.length) {
                                int inc = need_space / 3;
                                if (inc < 256) {
                                    inc = 256;
                                }
                                if (inc > AttribFlags.SSH_FILEXFER_ATTR_LINK_COUNT) {
                                    inc = AttribFlags.SSH_FILEXFER_ATTR_LINK_COUNT;
                                }
                                new_buffer = new byte[(need_space + inc)];
                            }
                            if (unread_size > 0) {
                                System.arraycopy(StreamGobbler.this.buffer, StreamGobbler.this.read_pos, new_buffer, 0, unread_size);
                            }
                            StreamGobbler.this.buffer = new_buffer;
                            StreamGobbler.this.read_pos = 0;
                            StreamGobbler.this.write_pos = unread_size;
                        }
                        System.arraycopy(buff, 0, StreamGobbler.this.buffer, StreamGobbler.this.write_pos, avail);
                        StreamGobbler streamGobbler = StreamGobbler.this;
                        streamGobbler.write_pos = streamGobbler.write_pos + avail;
                        StreamGobbler.this.synchronizer.notifyAll();
                    }
                } catch (IOException e) {
                    synchronized (StreamGobbler.this.synchronizer) {
                        StreamGobbler.this.exception = e;
                        StreamGobbler.this.synchronizer.notifyAll();
                        return;
                    }
                }
            }
        }
    }

    public StreamGobbler(InputStream is) {
        this.is = is;
        this.t = new GobblerThread();
        this.t.setDaemon(true);
        this.t.start();
    }

    public int read() throws IOException {
        synchronized (this.synchronizer) {
            if (this.isClosed) {
                throw new IOException("This StreamGobbler is closed.");
            }
            while (this.read_pos == this.write_pos) {
                if (this.exception != null) {
                    throw this.exception;
                } else if (this.isEOF) {
                    return -1;
                } else {
                    try {
                        this.synchronizer.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            byte[] bArr = this.buffer;
            int i = this.read_pos;
            this.read_pos = i + 1;
            int b = bArr[i] & 255;
            return b;
        }
    }

    public int available() throws IOException {
        int i;
        synchronized (this.synchronizer) {
            if (this.isClosed) {
                throw new IOException("This StreamGobbler is closed.");
            }
            i = this.write_pos - this.read_pos;
        }
        return i;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public void close() throws IOException {
        synchronized (this.synchronizer) {
            if (this.isClosed) {
                return;
            }
            this.isClosed = true;
            this.isEOF = true;
            this.synchronizer.notifyAll();
            this.is.close();
        }
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || off + len > b.length || off + len < 0 || off > b.length) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        } else {
            synchronized (this.synchronizer) {
                if (this.isClosed) {
                    throw new IOException("This StreamGobbler is closed.");
                }
                while (this.read_pos == this.write_pos) {
                    if (this.exception != null) {
                        throw this.exception;
                    } else if (this.isEOF) {
                        return -1;
                    } else {
                        try {
                            this.synchronizer.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                }
                int avail = this.write_pos - this.read_pos;
                if (avail > len) {
                    avail = len;
                }
                System.arraycopy(this.buffer, this.read_pos, b, off, avail);
                this.read_pos += avail;
                return avail;
            }
        }
    }
}
