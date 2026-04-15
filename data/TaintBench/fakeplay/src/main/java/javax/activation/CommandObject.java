package javax.activation;

import java.io.IOException;

public interface CommandObject {
    void setCommandContext(String str, DataHandler dataHandler) throws IOException;
}
