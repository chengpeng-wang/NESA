package u.aly;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* compiled from: TIOStreamTransport */
public class bk extends bm {
    protected InputStream a = null;
    protected OutputStream b = null;

    protected bk() {
    }

    public bk(InputStream inputStream) {
        this.a = inputStream;
    }

    public bk(OutputStream outputStream) {
        this.b = outputStream;
    }

    public bk(InputStream inputStream, OutputStream outputStream) {
        this.a = inputStream;
        this.b = outputStream;
    }

    public boolean a() {
        return true;
    }

    public void b() throws bn {
    }

    public void c() {
        if (this.a != null) {
            try {
                this.a.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.a = null;
        }
        if (this.b != null) {
            try {
                this.b.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            this.b = null;
        }
    }

    public int a(byte[] bArr, int i, int i2) throws bn {
        if (this.a == null) {
            throw new bn(1, "Cannot read from null inputStream");
        }
        try {
            int read = this.a.read(bArr, i, i2);
            if (read >= 0) {
                return read;
            }
            throw new bn(4);
        } catch (IOException e) {
            throw new bn(0, e);
        }
    }

    public void b(byte[] bArr, int i, int i2) throws bn {
        if (this.b == null) {
            throw new bn(1, "Cannot write to null outputStream");
        }
        try {
            this.b.write(bArr, i, i2);
        } catch (IOException e) {
            throw new bn(0, e);
        }
    }

    public void d() throws bn {
        if (this.b == null) {
            throw new bn(1, "Cannot flush null outputStream");
        }
        try {
            this.b.flush();
        } catch (IOException e) {
            throw new bn(0, e);
        }
    }
}
