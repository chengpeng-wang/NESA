package org.jivesoftware.smack.util;

import java.util.Hashtable;
import java.util.Map;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class DNSUtil {
    private static Map cache = new Cache(100, 600000);
    private static DirContext context;

    public static class HostAddress {
        private String host;
        private int port;

        private HostAddress(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return this.host;
        }

        public int getPort() {
            return this.port;
        }

        public String toString() {
            return this.host + ":" + this.port;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof HostAddress)) {
                return false;
            }
            HostAddress address = (HostAddress) o;
            if (!this.host.equals(address.host)) {
                return false;
            }
            if (this.port != address.port) {
                return false;
            }
            return true;
        }
    }

    static {
        try {
            Hashtable env = new Hashtable();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            context = new InitialDirContext(env);
        } catch (Exception e) {
        }
    }

    public static HostAddress resolveXMPPDomain(String domain) {
        if (context == null) {
            return new HostAddress(domain, 5222);
        }
        HostAddress address;
        String key = "c" + domain;
        if (cache.containsKey(key)) {
            address = (HostAddress) cache.get(key);
            if (address != null) {
                return address;
            }
        }
        String host = domain;
        int port = 5222;
        try {
            String[] srvRecordEntries = ((String) context.getAttributes("_xmpp-client._tcp." + domain, new String[]{"SRV"}).get("SRV").get()).split(" ");
            port = Integer.parseInt(srvRecordEntries[srvRecordEntries.length - 2]);
            host = srvRecordEntries[srvRecordEntries.length - 1];
        } catch (Exception e) {
        }
        if (host.endsWith(".")) {
            host = host.substring(0, host.length() - 1);
        }
        address = new HostAddress(host, port);
        cache.put(key, address);
        return address;
    }

    public static HostAddress resolveXMPPServerDomain(String domain) {
        if (context == null) {
            return new HostAddress(domain, 5269);
        }
        HostAddress address;
        String key = "s" + domain;
        if (cache.containsKey(key)) {
            address = (HostAddress) cache.get(key);
            if (address != null) {
                return address;
            }
        }
        String host = domain;
        int port = 5269;
        String[] srvRecordEntries;
        try {
            srvRecordEntries = ((String) context.getAttributes("_xmpp-server._tcp." + domain, new String[]{"SRV"}).get("SRV").get()).split(" ");
            port = Integer.parseInt(srvRecordEntries[srvRecordEntries.length - 2]);
            host = srvRecordEntries[srvRecordEntries.length - 1];
        } catch (Exception e) {
            try {
                srvRecordEntries = ((String) context.getAttributes("_jabber._tcp." + domain, new String[]{"SRV"}).get("SRV").get()).split(" ");
                port = Integer.parseInt(srvRecordEntries[srvRecordEntries.length - 2]);
                host = srvRecordEntries[srvRecordEntries.length - 1];
            } catch (Exception e2) {
            }
        }
        if (host.endsWith(".")) {
            host = host.substring(0, host.length() - 1);
        }
        address = new HostAddress(host, port);
        cache.put(key, address);
        return address;
    }
}
