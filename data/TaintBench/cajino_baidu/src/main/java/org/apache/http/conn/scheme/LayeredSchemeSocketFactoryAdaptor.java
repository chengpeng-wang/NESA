package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

@Deprecated
class LayeredSchemeSocketFactoryAdaptor extends SchemeSocketFactoryAdaptor implements LayeredSchemeSocketFactory {
    private final LayeredSocketFactory factory;

    LayeredSchemeSocketFactoryAdaptor(LayeredSocketFactory factory) {
        super(factory);
        this.factory = factory;
    }

    public Socket createLayeredSocket(Socket socket, String target, int port, boolean autoClose) throws IOException, UnknownHostException {
        return this.factory.createSocket(socket, target, port, autoClose);
    }
}
