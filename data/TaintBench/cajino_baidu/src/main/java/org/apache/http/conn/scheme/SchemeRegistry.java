package org.apache.http.conn.scheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public final class SchemeRegistry {
    private final ConcurrentHashMap<String, Scheme> registeredSchemes = new ConcurrentHashMap();

    public final Scheme getScheme(String name) {
        Scheme found = get(name);
        if (found != null) {
            return found;
        }
        throw new IllegalStateException("Scheme '" + name + "' not registered.");
    }

    public final Scheme getScheme(HttpHost host) {
        if (host != null) {
            return getScheme(host.getSchemeName());
        }
        throw new IllegalArgumentException("Host must not be null.");
    }

    public final Scheme get(String name) {
        if (name != null) {
            return (Scheme) this.registeredSchemes.get(name);
        }
        throw new IllegalArgumentException("Name must not be null.");
    }

    public final Scheme register(Scheme sch) {
        if (sch != null) {
            return (Scheme) this.registeredSchemes.put(sch.getName(), sch);
        }
        throw new IllegalArgumentException("Scheme must not be null.");
    }

    public final Scheme unregister(String name) {
        if (name != null) {
            return (Scheme) this.registeredSchemes.remove(name);
        }
        throw new IllegalArgumentException("Name must not be null.");
    }

    public final List<String> getSchemeNames() {
        return new ArrayList(this.registeredSchemes.keySet());
    }

    public void setItems(Map<String, Scheme> map) {
        if (map != null) {
            this.registeredSchemes.clear();
            this.registeredSchemes.putAll(map);
        }
    }
}
