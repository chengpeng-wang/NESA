package javax.mail;

import java.io.IOException;
import java.io.InputStream;

/* compiled from: Session */
interface StreamLoader {
    void load(InputStream inputStream) throws IOException;
}
