package javax.mail.util;

import android.support.v4.view.MotionEventCompat;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import javax.mail.internet.SharedInputStream;

public class SharedFileInputStream extends BufferedInputStream implements SharedInputStream {
    private static int defaultBufferSize = 2048;
    protected long bufpos;
    protected int bufsize;
    protected long datalen;
    protected RandomAccessFile in;
    private boolean master;
    private SharedFile sf;
    protected long start;

    static class SharedFile {
        private int cnt;
        private RandomAccessFile in;

        SharedFile(String file) throws IOException {
            this.in = new RandomAccessFile(file, "r");
        }

        SharedFile(File file) throws IOException {
            this.in = new RandomAccessFile(file, "r");
        }

        public RandomAccessFile open() {
            this.cnt++;
            return this.in;
        }

        public synchronized void close() throws IOException {
            if (this.cnt > 0) {
                int i = this.cnt - 1;
                this.cnt = i;
                if (i <= 0) {
                    this.in.close();
                }
            }
        }

        public synchronized void forceClose() throws IOException {
            if (this.cnt > 0) {
                this.cnt = 0;
                this.in.close();
            } else {
                try {
                    this.in.close();
                } catch (IOException e) {
                }
            }
        }

        /* access modifiers changed from: protected */
        public void finalize() throws Throwable {
            super.finalize();
            this.in.close();
        }
    }

    private void ensureOpen() throws IOException {
        if (this.in == null) {
            throw new IOException("Stream closed");
        }
    }

    public SharedFileInputStream(File file) throws IOException {
        this(file, defaultBufferSize);
    }

    public SharedFileInputStream(String file) throws IOException {
        this(file, defaultBufferSize);
    }

    public SharedFileInputStream(File file, int size) throws IOException {
        super(null);
        this.start = 0;
        this.master = true;
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        init(new SharedFile(file), size);
    }

    public SharedFileInputStream(String file, int size) throws IOException {
        super(null);
        this.start = 0;
        this.master = true;
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        init(new SharedFile(file), size);
    }

    private void init(SharedFile sf, int size) throws IOException {
        this.sf = sf;
        this.in = sf.open();
        this.start = 0;
        this.datalen = this.in.length();
        this.bufsize = size;
        this.buf = new byte[size];
    }

    private SharedFileInputStream(SharedFile sf, long start, long len, int bufsize) {
        super(null);
        this.start = 0;
        this.master = true;
        this.master = false;
        this.sf = sf;
        this.in = sf.open();
        this.start = start;
        this.bufpos = start;
        this.datalen = len;
        this.bufsize = bufsize;
        this.buf = new byte[bufsize];
    }

    private void fill() throws IOException {
        if (this.markpos < 0) {
            this.pos = 0;
            this.bufpos += (long) this.count;
        } else if (this.pos >= this.buf.length) {
            if (this.markpos > 0) {
                int sz = this.pos - this.markpos;
                System.arraycopy(this.buf, this.markpos, this.buf, 0, sz);
                this.pos = sz;
                this.bufpos += (long) this.markpos;
                this.markpos = 0;
            } else if (this.buf.length >= this.marklimit) {
                this.markpos = -1;
                this.pos = 0;
                this.bufpos += (long) this.count;
            } else {
                int nsz = this.pos * 2;
                if (nsz > this.marklimit) {
                    nsz = this.marklimit;
                }
                byte[] nbuf = new byte[nsz];
                System.arraycopy(this.buf, 0, nbuf, 0, this.pos);
                this.buf = nbuf;
            }
        }
        this.count = this.pos;
        this.in.seek(this.bufpos + ((long) this.pos));
        int len = this.buf.length - this.pos;
        if (((this.bufpos - this.start) + ((long) this.pos)) + ((long) len) > this.datalen) {
            len = (int) (this.datalen - ((this.bufpos - this.start) + ((long) this.pos)));
        }
        int n = this.in.read(this.buf, this.pos, len);
        if (n > 0) {
            this.count = this.pos + n;
        }
    }

    public synchronized int read() throws IOException {
        int i;
        ensureOpen();
        if (this.pos >= this.count) {
            fill();
            if (this.pos >= this.count) {
                i = -1;
            }
        }
        byte[] bArr = this.buf;
        int i2 = this.pos;
        this.pos = i2 + 1;
        i = bArr[i2] & MotionEventCompat.ACTION_MASK;
        return i;
    }

    private int read1(byte[] b, int off, int len) throws IOException {
        int cnt;
        int avail = this.count - this.pos;
        if (avail <= 0) {
            fill();
            avail = this.count - this.pos;
            if (avail <= 0) {
                return -1;
            }
        }
        if (avail < len) {
            cnt = avail;
        } else {
            cnt = len;
        }
        System.arraycopy(this.buf, this.pos, b, off, cnt);
        this.pos += cnt;
        return cnt;
    }

    public synchronized int read(byte[] b, int off, int len) throws IOException {
        int n;
        ensureOpen();
        if ((((off | len) | (off + len)) | (b.length - (off + len))) < 0) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            n = 0;
        } else {
            n = read1(b, off, len);
            if (n > 0) {
                while (n < len) {
                    int n1 = read1(b, off + n, len - n);
                    if (n1 <= 0) {
                        break;
                    }
                    n += n1;
                }
            }
        }
        return n;
    }

    /* JADX WARNING: Missing block: B:12:0x0021, code skipped:
            if (r0 > 0) goto L_0x0023;
     */
    public synchronized long skip(long r7) throws java.io.IOException {
        /*
        r6 = this;
        r2 = 0;
        monitor-enter(r6);
        r6.ensureOpen();	 Catch:{ all -> 0x0030 }
        r4 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1));
        if (r4 > 0) goto L_0x000c;
    L_0x000a:
        monitor-exit(r6);
        return r2;
    L_0x000c:
        r4 = r6.count;	 Catch:{ all -> 0x0030 }
        r5 = r6.pos;	 Catch:{ all -> 0x0030 }
        r4 = r4 - r5;
        r0 = (long) r4;	 Catch:{ all -> 0x0030 }
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r4 > 0) goto L_0x0023;
    L_0x0016:
        r6.fill();	 Catch:{ all -> 0x0030 }
        r4 = r6.count;	 Catch:{ all -> 0x0030 }
        r5 = r6.pos;	 Catch:{ all -> 0x0030 }
        r4 = r4 - r5;
        r0 = (long) r4;	 Catch:{ all -> 0x0030 }
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r4 <= 0) goto L_0x000a;
    L_0x0023:
        r4 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1));
        if (r4 >= 0) goto L_0x0033;
    L_0x0027:
        r2 = r0;
    L_0x0028:
        r4 = r6.pos;	 Catch:{ all -> 0x0030 }
        r4 = (long) r4;	 Catch:{ all -> 0x0030 }
        r4 = r4 + r2;
        r4 = (int) r4;	 Catch:{ all -> 0x0030 }
        r6.pos = r4;	 Catch:{ all -> 0x0030 }
        goto L_0x000a;
    L_0x0030:
        r4 = move-exception;
        monitor-exit(r6);
        throw r4;
    L_0x0033:
        r2 = r7;
        goto L_0x0028;
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.util.SharedFileInputStream.skip(long):long");
    }

    public synchronized int available() throws IOException {
        ensureOpen();
        return (this.count - this.pos) + in_available();
    }

    private int in_available() throws IOException {
        return (int) ((this.start + this.datalen) - (this.bufpos + ((long) this.count)));
    }

    public synchronized void mark(int readlimit) {
        this.marklimit = readlimit;
        this.markpos = this.pos;
    }

    public synchronized void reset() throws IOException {
        ensureOpen();
        if (this.markpos < 0) {
            throw new IOException("Resetting to invalid mark");
        }
        this.pos = this.markpos;
    }

    public boolean markSupported() {
        return true;
    }

    public void close() throws IOException {
        if (this.in != null) {
            try {
                if (this.master) {
                    this.sf.forceClose();
                } else {
                    this.sf.close();
                }
                this.sf = null;
                this.in = null;
                this.buf = null;
            } catch (Throwable th) {
                this.sf = null;
                this.in = null;
                this.buf = null;
            }
        }
    }

    public long getPosition() {
        if (this.in != null) {
            return (this.bufpos + ((long) this.pos)) - this.start;
        }
        throw new RuntimeException("Stream closed");
    }

    public InputStream newStream(long start, long end) {
        if (this.in == null) {
            throw new RuntimeException("Stream closed");
        } else if (start < 0) {
            throw new IllegalArgumentException("start < 0");
        } else {
            if (end == -1) {
                end = this.datalen;
            }
            return new SharedFileInputStream(this.sf, this.start + ((long) ((int) start)), (long) ((int) (end - start)), this.bufsize);
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        close();
    }
}
