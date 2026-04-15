package org.springframework.core.io;

import java.io.IOException;
import java.io.OutputStream;

public interface WritableResource extends Resource {
    OutputStream getOutputStream() throws IOException;

    boolean isWritable();
}
