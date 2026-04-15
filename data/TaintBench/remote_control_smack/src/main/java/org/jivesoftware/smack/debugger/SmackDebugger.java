package org.jivesoftware.smack.debugger;

import java.io.Reader;
import java.io.Writer;
import org.jivesoftware.smack.PacketListener;

public interface SmackDebugger {
    Reader getReader();

    PacketListener getReaderListener();

    Writer getWriter();

    PacketListener getWriterListener();

    Reader newConnectionReader(Reader reader);

    Writer newConnectionWriter(Writer writer);

    void userHasLogged(String str);
}
