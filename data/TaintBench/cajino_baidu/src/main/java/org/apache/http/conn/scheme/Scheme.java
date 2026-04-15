package org.apache.http.conn.scheme;

import android.support.v4.internal.view.SupportMenu;
import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.LangUtils;

@Immutable
public final class Scheme {
    private final int defaultPort;
    private final boolean layered;
    private final String name;
    private final SchemeSocketFactory socketFactory;
    private String stringRep;

    public Scheme(String name, int port, SchemeSocketFactory factory) {
        if (name == null) {
            throw new IllegalArgumentException("Scheme name may not be null");
        } else if (port <= 0 || port > SupportMenu.USER_MASK) {
            throw new IllegalArgumentException("Port is invalid: " + port);
        } else if (factory == null) {
            throw new IllegalArgumentException("Socket factory may not be null");
        } else {
            this.name = name.toLowerCase(Locale.ENGLISH);
            this.socketFactory = factory;
            this.defaultPort = port;
            this.layered = factory instanceof LayeredSchemeSocketFactory;
        }
    }

    @Deprecated
    public Scheme(String name, SocketFactory factory, int port) {
        if (name == null) {
            throw new IllegalArgumentException("Scheme name may not be null");
        } else if (factory == null) {
            throw new IllegalArgumentException("Socket factory may not be null");
        } else if (port <= 0 || port > SupportMenu.USER_MASK) {
            throw new IllegalArgumentException("Port is invalid: " + port);
        } else {
            this.name = name.toLowerCase(Locale.ENGLISH);
            if (factory instanceof LayeredSocketFactory) {
                this.socketFactory = new LayeredSchemeSocketFactoryAdaptor((LayeredSocketFactory) factory);
                this.layered = true;
            } else {
                this.socketFactory = new SchemeSocketFactoryAdaptor(factory);
                this.layered = false;
            }
            this.defaultPort = port;
        }
    }

    public final int getDefaultPort() {
        return this.defaultPort;
    }

    @Deprecated
    public final SocketFactory getSocketFactory() {
        if (this.socketFactory instanceof SchemeSocketFactoryAdaptor) {
            return ((SchemeSocketFactoryAdaptor) this.socketFactory).getFactory();
        }
        if (this.layered) {
            return new LayeredSocketFactoryAdaptor((LayeredSchemeSocketFactory) this.socketFactory);
        }
        return new SocketFactoryAdaptor(this.socketFactory);
    }

    public final SchemeSocketFactory getSchemeSocketFactory() {
        return this.socketFactory;
    }

    public final String getName() {
        return this.name;
    }

    public final boolean isLayered() {
        return this.layered;
    }

    public final int resolvePort(int port) {
        return port <= 0 ? this.defaultPort : port;
    }

    public final String toString() {
        if (this.stringRep == null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(this.name);
            buffer.append(':');
            buffer.append(Integer.toString(this.defaultPort));
            this.stringRep = buffer.toString();
        }
        return this.stringRep;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Scheme)) {
            return false;
        }
        Scheme that = (Scheme) obj;
        if (this.name.equals(that.name) && this.defaultPort == that.defaultPort && this.layered == that.layered) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(17, this.defaultPort), this.name), this.layered);
    }
}
