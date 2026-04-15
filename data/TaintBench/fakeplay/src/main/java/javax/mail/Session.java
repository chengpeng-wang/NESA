package javax.mail;

import com.sun.mail.util.LineInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.mail.Provider.Type;

public final class Session {
    private static Session defaultSession = null;
    /* access modifiers changed from: private|final */
    public final Properties addressMap = new Properties();
    private final Hashtable authTable = new Hashtable();
    private final Authenticator authenticator;
    private boolean debug = false;
    private PrintStream out;
    private final Properties props;
    private final Vector providers = new Vector();
    private final Hashtable providersByClassName = new Hashtable();
    private final Hashtable providersByProtocol = new Hashtable();

    private Session(Properties props, Authenticator authenticator) {
        Class cl;
        this.props = props;
        this.authenticator = authenticator;
        if (Boolean.valueOf(props.getProperty("mail.debug")).booleanValue()) {
            this.debug = true;
        }
        if (this.debug) {
            pr("DEBUG: JavaMail version 1.4.1");
        }
        if (authenticator != null) {
            cl = authenticator.getClass();
        } else {
            cl = getClass();
        }
        loadProviders(cl);
        loadAddressMap(cl);
    }

    public static Session getInstance(Properties props, Authenticator authenticator) {
        return new Session(props, authenticator);
    }

    public static Session getInstance(Properties props) {
        return new Session(props, null);
    }

    public static synchronized Session getDefaultInstance(Properties props, Authenticator authenticator) {
        Session session;
        synchronized (Session.class) {
            if (defaultSession == null) {
                defaultSession = new Session(props, authenticator);
            } else if (defaultSession.authenticator != authenticator && (defaultSession.authenticator == null || authenticator == null || defaultSession.authenticator.getClass().getClassLoader() != authenticator.getClass().getClassLoader())) {
                throw new SecurityException("Access to default session denied");
            }
            session = defaultSession;
        }
        return session;
    }

    public static Session getDefaultInstance(Properties props) {
        return getDefaultInstance(props, null);
    }

    public synchronized void setDebug(boolean debug) {
        this.debug = debug;
        if (debug) {
            pr("DEBUG: setDebug: JavaMail version 1.4.1");
        }
    }

    public synchronized boolean getDebug() {
        return this.debug;
    }

    public synchronized void setDebugOut(PrintStream out) {
        this.out = out;
    }

    public synchronized PrintStream getDebugOut() {
        PrintStream printStream;
        if (this.out == null) {
            printStream = System.out;
        } else {
            printStream = this.out;
        }
        return printStream;
    }

    public synchronized Provider[] getProviders() {
        Provider[] _providers;
        _providers = new Provider[this.providers.size()];
        this.providers.copyInto(_providers);
        return _providers;
    }

    public synchronized Provider getProvider(String protocol) throws NoSuchProviderException {
        Provider _provider;
        if (protocol != null) {
            if (protocol.length() > 0) {
                Provider _provider2 = null;
                String _className = this.props.getProperty("mail." + protocol + ".class");
                if (_className != null) {
                    if (this.debug) {
                        pr("DEBUG: mail." + protocol + ".class property exists and points to " + _className);
                    }
                    _provider2 = (Provider) this.providersByClassName.get(_className);
                }
                if (_provider2 != null) {
                    _provider = _provider2;
                } else {
                    _provider2 = (Provider) this.providersByProtocol.get(protocol);
                    if (_provider2 == null) {
                        throw new NoSuchProviderException("No provider for " + protocol);
                    }
                    if (this.debug) {
                        pr("DEBUG: getProvider() returning " + _provider2.toString());
                    }
                    _provider = _provider2;
                }
            }
        }
        throw new NoSuchProviderException("Invalid protocol: null");
        return _provider;
    }

    public synchronized void setProvider(Provider provider) throws NoSuchProviderException {
        if (provider == null) {
            throw new NoSuchProviderException("Can't set null provider");
        }
        this.providersByProtocol.put(provider.getProtocol(), provider);
        this.props.put("mail." + provider.getProtocol() + ".class", provider.getClassName());
    }

    public Store getStore() throws NoSuchProviderException {
        return getStore(getProperty("mail.store.protocol"));
    }

    public Store getStore(String protocol) throws NoSuchProviderException {
        return getStore(new URLName(protocol, null, -1, null, null, null));
    }

    public Store getStore(URLName url) throws NoSuchProviderException {
        return getStore(getProvider(url.getProtocol()), url);
    }

    public Store getStore(Provider provider) throws NoSuchProviderException {
        return getStore(provider, null);
    }

    private Store getStore(Provider provider, URLName url) throws NoSuchProviderException {
        if (provider == null || provider.getType() != Type.STORE) {
            throw new NoSuchProviderException("invalid provider");
        }
        try {
            return (Store) getService(provider, url);
        } catch (ClassCastException e) {
            throw new NoSuchProviderException("incorrect class");
        }
    }

    public Folder getFolder(URLName url) throws MessagingException {
        Store store = getStore(url);
        store.connect();
        return store.getFolder(url);
    }

    public Transport getTransport() throws NoSuchProviderException {
        return getTransport(getProperty("mail.transport.protocol"));
    }

    public Transport getTransport(String protocol) throws NoSuchProviderException {
        return getTransport(new URLName(protocol, null, -1, null, null, null));
    }

    public Transport getTransport(URLName url) throws NoSuchProviderException {
        return getTransport(getProvider(url.getProtocol()), url);
    }

    public Transport getTransport(Provider provider) throws NoSuchProviderException {
        return getTransport(provider, null);
    }

    public Transport getTransport(Address address) throws NoSuchProviderException {
        String transportProtocol = (String) this.addressMap.get(address.getType());
        if (transportProtocol != null) {
            return getTransport(transportProtocol);
        }
        throw new NoSuchProviderException("No provider for Address type: " + address.getType());
    }

    private Transport getTransport(Provider provider, URLName url) throws NoSuchProviderException {
        if (provider == null || provider.getType() != Type.TRANSPORT) {
            throw new NoSuchProviderException("invalid provider");
        }
        try {
            return (Transport) getService(provider, url);
        } catch (ClassCastException e) {
            throw new NoSuchProviderException("incorrect class");
        }
    }

    private Object getService(Provider provider, URLName url) throws NoSuchProviderException {
        if (provider == null) {
            throw new NoSuchProviderException("null");
        }
        ClassLoader cl;
        if (url == null) {
            URLName uRLName = new URLName(provider.getProtocol(), null, -1, null, null, null);
        }
        if (this.authenticator != null) {
            cl = this.authenticator.getClass().getClassLoader();
        } else {
            cl = getClass().getClassLoader();
        }
        Class serviceClass = null;
        try {
            ClassLoader ccl = getContextClassLoader();
            if (ccl != null) {
                try {
                    serviceClass = ccl.loadClass(provider.getClassName());
                } catch (ClassNotFoundException e) {
                }
            }
            if (serviceClass == null) {
                serviceClass = cl.loadClass(provider.getClassName());
            }
        } catch (Exception e2) {
            try {
                serviceClass = Class.forName(provider.getClassName());
            } catch (Exception ex) {
                if (this.debug) {
                    ex.printStackTrace(getDebugOut());
                }
                throw new NoSuchProviderException(provider.getProtocol());
            }
        }
        try {
            return serviceClass.getConstructor(new Class[]{Session.class, URLName.class}).newInstance(new Object[]{this, url});
        } catch (Exception ex2) {
            if (this.debug) {
                ex2.printStackTrace(getDebugOut());
            }
            throw new NoSuchProviderException(provider.getProtocol());
        }
    }

    public void setPasswordAuthentication(URLName url, PasswordAuthentication pw) {
        if (pw == null) {
            this.authTable.remove(url);
        } else {
            this.authTable.put(url, pw);
        }
    }

    public PasswordAuthentication getPasswordAuthentication(URLName url) {
        return (PasswordAuthentication) this.authTable.get(url);
    }

    public PasswordAuthentication requestPasswordAuthentication(InetAddress addr, int port, String protocol, String prompt, String defaultUserName) {
        if (this.authenticator != null) {
            return this.authenticator.requestPasswordAuthentication(addr, port, protocol, prompt, defaultUserName);
        }
        return null;
    }

    public Properties getProperties() {
        return this.props;
    }

    public String getProperty(String name) {
        return this.props.getProperty(name);
    }

    private void loadProviders(Class cl) {
        StreamLoader loader = new StreamLoader() {
            public void load(InputStream is) throws IOException {
                Session.this.loadProvidersFromStream(is);
            }
        };
        try {
            loadFile(new StringBuilder(String.valueOf(System.getProperty("java.home"))).append(File.separator).append("lib").append(File.separator).append("javamail.providers").toString(), loader);
        } catch (SecurityException sex) {
            if (this.debug) {
                pr("DEBUG: can't get java.home: " + sex);
            }
        }
        loadAllResources("META-INF/javamail.providers", cl, loader);
        loadResource("/META-INF/javamail.default.providers", cl, loader);
        if (this.providers.size() == 0) {
            if (this.debug) {
                pr("DEBUG: failed to load any providers, using defaults");
            }
            addProvider(new Provider(Type.STORE, "imap", "com.sun.mail.imap.IMAPStore", "Sun Microsystems, Inc.", Version.version));
            addProvider(new Provider(Type.STORE, "imaps", "com.sun.mail.imap.IMAPSSLStore", "Sun Microsystems, Inc.", Version.version));
            addProvider(new Provider(Type.STORE, "pop3", "com.sun.mail.pop3.POP3Store", "Sun Microsystems, Inc.", Version.version));
            addProvider(new Provider(Type.STORE, "pop3s", "com.sun.mail.pop3.POP3SSLStore", "Sun Microsystems, Inc.", Version.version));
            addProvider(new Provider(Type.TRANSPORT, "smtp", "com.sun.mail.smtp.SMTPTransport", "Sun Microsystems, Inc.", Version.version));
            addProvider(new Provider(Type.TRANSPORT, "smtps", "com.sun.mail.smtp.SMTPSSLTransport", "Sun Microsystems, Inc.", Version.version));
        }
        if (this.debug) {
            pr("DEBUG: Tables of loaded providers");
            pr("DEBUG: Providers Listed By Class Name: " + this.providersByClassName.toString());
            pr("DEBUG: Providers Listed By Protocol: " + this.providersByProtocol.toString());
        }
    }

    /* access modifiers changed from: private */
    public void loadProvidersFromStream(InputStream is) throws IOException {
        if (is != null) {
            LineInputStream lis = new LineInputStream(is);
            while (true) {
                String currLine = lis.readLine();
                if (currLine != null) {
                    if (!currLine.startsWith("#")) {
                        Type type = null;
                        String protocol = null;
                        String className = null;
                        String vendor = null;
                        String version = null;
                        StringTokenizer tuples = new StringTokenizer(currLine, ";");
                        while (tuples.hasMoreTokens()) {
                            String currTuple = tuples.nextToken().trim();
                            int sep = currTuple.indexOf("=");
                            if (currTuple.startsWith("protocol=")) {
                                protocol = currTuple.substring(sep + 1);
                            } else if (currTuple.startsWith("type=")) {
                                String strType = currTuple.substring(sep + 1);
                                if (strType.equalsIgnoreCase("store")) {
                                    type = Type.STORE;
                                } else if (strType.equalsIgnoreCase("transport")) {
                                    type = Type.TRANSPORT;
                                }
                            } else if (currTuple.startsWith("class=")) {
                                className = currTuple.substring(sep + 1);
                            } else if (currTuple.startsWith("vendor=")) {
                                vendor = currTuple.substring(sep + 1);
                            } else if (currTuple.startsWith("version=")) {
                                version = currTuple.substring(sep + 1);
                            }
                        }
                        if (type != null && protocol != null && className != null && protocol.length() > 0 && className.length() > 0) {
                            addProvider(new Provider(type, protocol, className, vendor, version));
                        } else if (this.debug) {
                            pr("DEBUG: Bad provider entry: " + currLine);
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    public synchronized void addProvider(Provider provider) {
        this.providers.addElement(provider);
        this.providersByClassName.put(provider.getClassName(), provider);
        if (!this.providersByProtocol.containsKey(provider.getProtocol())) {
            this.providersByProtocol.put(provider.getProtocol(), provider);
        }
    }

    private void loadAddressMap(Class cl) {
        StreamLoader loader = new StreamLoader() {
            public void load(InputStream is) throws IOException {
                Session.this.addressMap.load(is);
            }
        };
        loadResource("/META-INF/javamail.default.address.map", cl, loader);
        loadAllResources("META-INF/javamail.address.map", cl, loader);
        try {
            loadFile(new StringBuilder(String.valueOf(System.getProperty("java.home"))).append(File.separator).append("lib").append(File.separator).append("javamail.address.map").toString(), loader);
        } catch (SecurityException sex) {
            if (this.debug) {
                pr("DEBUG: can't get java.home: " + sex);
            }
        }
        if (this.addressMap.isEmpty()) {
            if (this.debug) {
                pr("DEBUG: failed to load address map, using defaults");
            }
            this.addressMap.put("rfc822", "smtp");
        }
    }

    public synchronized void setProtocolForAddress(String addresstype, String protocol) {
        if (protocol == null) {
            this.addressMap.remove(addresstype);
        } else {
            this.addressMap.put(addresstype, protocol);
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:12:0x002c=Splitter:B:12:0x002c, B:21:0x005d=Splitter:B:21:0x005d} */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0030 A:{Catch:{ all -> 0x008d }} */
    /* JADX WARNING: Removed duplicated region for block: B:45:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0056 A:{SYNTHETIC, Splitter:B:17:0x0056} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0061 A:{Catch:{ all -> 0x008d }} */
    /* JADX WARNING: Removed duplicated region for block: B:46:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0087 A:{SYNTHETIC, Splitter:B:26:0x0087} */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0090 A:{SYNTHETIC, Splitter:B:31:0x0090} */
    private void loadFile(java.lang.String r7, javax.mail.StreamLoader r8) {
        /*
        r6 = this;
        r0 = 0;
        r1 = new java.io.BufferedInputStream;	 Catch:{ IOException -> 0x002b, SecurityException -> 0x005c }
        r4 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x002b, SecurityException -> 0x005c }
        r4.<init>(r7);	 Catch:{ IOException -> 0x002b, SecurityException -> 0x005c }
        r1.<init>(r4);	 Catch:{ IOException -> 0x002b, SecurityException -> 0x005c }
        r8.load(r1);	 Catch:{ IOException -> 0x009f, SecurityException -> 0x009c, all -> 0x0099 }
        r4 = r6.debug;	 Catch:{ IOException -> 0x009f, SecurityException -> 0x009c, all -> 0x0099 }
        if (r4 == 0) goto L_0x0024;
    L_0x0012:
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x009f, SecurityException -> 0x009c, all -> 0x0099 }
        r5 = "DEBUG: successfully loaded file: ";
        r4.<init>(r5);	 Catch:{ IOException -> 0x009f, SecurityException -> 0x009c, all -> 0x0099 }
        r4 = r4.append(r7);	 Catch:{ IOException -> 0x009f, SecurityException -> 0x009c, all -> 0x0099 }
        r4 = r4.toString();	 Catch:{ IOException -> 0x009f, SecurityException -> 0x009c, all -> 0x0099 }
        r6.pr(r4);	 Catch:{ IOException -> 0x009f, SecurityException -> 0x009c, all -> 0x0099 }
    L_0x0024:
        if (r1 == 0) goto L_0x00a2;
    L_0x0026:
        r1.close();	 Catch:{ IOException -> 0x0094 }
        r0 = r1;
    L_0x002a:
        return;
    L_0x002b:
        r2 = move-exception;
    L_0x002c:
        r4 = r6.debug;	 Catch:{ all -> 0x008d }
        if (r4 == 0) goto L_0x0054;
    L_0x0030:
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008d }
        r5 = "DEBUG: not loading file: ";
        r4.<init>(r5);	 Catch:{ all -> 0x008d }
        r4 = r4.append(r7);	 Catch:{ all -> 0x008d }
        r4 = r4.toString();	 Catch:{ all -> 0x008d }
        r6.pr(r4);	 Catch:{ all -> 0x008d }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008d }
        r5 = "DEBUG: ";
        r4.<init>(r5);	 Catch:{ all -> 0x008d }
        r4 = r4.append(r2);	 Catch:{ all -> 0x008d }
        r4 = r4.toString();	 Catch:{ all -> 0x008d }
        r6.pr(r4);	 Catch:{ all -> 0x008d }
    L_0x0054:
        if (r0 == 0) goto L_0x002a;
    L_0x0056:
        r0.close();	 Catch:{ IOException -> 0x005a }
        goto L_0x002a;
    L_0x005a:
        r4 = move-exception;
        goto L_0x002a;
    L_0x005c:
        r3 = move-exception;
    L_0x005d:
        r4 = r6.debug;	 Catch:{ all -> 0x008d }
        if (r4 == 0) goto L_0x0085;
    L_0x0061:
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008d }
        r5 = "DEBUG: not loading file: ";
        r4.<init>(r5);	 Catch:{ all -> 0x008d }
        r4 = r4.append(r7);	 Catch:{ all -> 0x008d }
        r4 = r4.toString();	 Catch:{ all -> 0x008d }
        r6.pr(r4);	 Catch:{ all -> 0x008d }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008d }
        r5 = "DEBUG: ";
        r4.<init>(r5);	 Catch:{ all -> 0x008d }
        r4 = r4.append(r3);	 Catch:{ all -> 0x008d }
        r4 = r4.toString();	 Catch:{ all -> 0x008d }
        r6.pr(r4);	 Catch:{ all -> 0x008d }
    L_0x0085:
        if (r0 == 0) goto L_0x002a;
    L_0x0087:
        r0.close();	 Catch:{ IOException -> 0x008b }
        goto L_0x002a;
    L_0x008b:
        r4 = move-exception;
        goto L_0x002a;
    L_0x008d:
        r4 = move-exception;
    L_0x008e:
        if (r0 == 0) goto L_0x0093;
    L_0x0090:
        r0.close();	 Catch:{ IOException -> 0x0097 }
    L_0x0093:
        throw r4;
    L_0x0094:
        r4 = move-exception;
        r0 = r1;
        goto L_0x002a;
    L_0x0097:
        r5 = move-exception;
        goto L_0x0093;
    L_0x0099:
        r4 = move-exception;
        r0 = r1;
        goto L_0x008e;
    L_0x009c:
        r3 = move-exception;
        r0 = r1;
        goto L_0x005d;
    L_0x009f:
        r2 = move-exception;
        r0 = r1;
        goto L_0x002c;
    L_0x00a2:
        r0 = r1;
        goto L_0x002a;
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.Session.loadFile(java.lang.String, javax.mail.StreamLoader):void");
    }

    private void loadResource(String name, Class cl, StreamLoader loader) {
        InputStream clis = null;
        try {
            clis = getResourceAsStream(cl, name);
            if (clis != null) {
                loader.load(clis);
                if (this.debug) {
                    pr("DEBUG: successfully loaded resource: " + name);
                }
            } else if (this.debug) {
                pr("DEBUG: not loading resource: " + name);
            }
            if (clis != null) {
                try {
                    clis.close();
                } catch (IOException e) {
                }
            }
        } catch (IOException e2) {
            if (this.debug) {
                pr("DEBUG: " + e2);
            }
            if (clis != null) {
                try {
                    clis.close();
                } catch (IOException e3) {
                }
            }
        } catch (SecurityException sex) {
            if (this.debug) {
                pr("DEBUG: " + sex);
            }
            if (clis != null) {
                try {
                    clis.close();
                } catch (IOException e4) {
                }
            }
        } catch (Throwable th) {
            if (clis != null) {
                try {
                    clis.close();
                } catch (IOException e5) {
                }
            }
        }
    }

    private void loadAllResources(String name, Class cl, StreamLoader loader) {
        boolean anyLoaded = false;
        try {
            URL[] urls;
            ClassLoader cld = getContextClassLoader();
            if (cld == null) {
                cld = cl.getClassLoader();
            }
            if (cld != null) {
                urls = getResources(cld, name);
            } else {
                urls = getSystemResources(name);
            }
            if (urls != null) {
                for (URL url : urls) {
                    InputStream clis = null;
                    if (this.debug) {
                        pr("DEBUG: URL " + url);
                    }
                    try {
                        clis = openStream(url);
                        if (clis != null) {
                            loader.load(clis);
                            anyLoaded = true;
                            if (this.debug) {
                                pr("DEBUG: successfully loaded resource: " + url);
                            }
                        } else if (this.debug) {
                            pr("DEBUG: not loading resource: " + url);
                        }
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e) {
                            }
                        }
                    } catch (IOException ioex) {
                        if (this.debug) {
                            pr("DEBUG: " + ioex);
                        }
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e2) {
                            }
                        }
                    } catch (SecurityException sex) {
                        if (this.debug) {
                            pr("DEBUG: " + sex);
                        }
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e3) {
                            }
                        }
                    } catch (Throwable th) {
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e4) {
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            if (this.debug) {
                pr("DEBUG: " + ex);
            }
        }
        if (!anyLoaded) {
            if (this.debug) {
                pr("DEBUG: !anyLoaded");
            }
            loadResource("/" + name, cl, loader);
        }
    }

    private void pr(String str) {
        getDebugOut().println(str);
    }

    private static ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                ClassLoader cl = null;
                try {
                    return Thread.currentThread().getContextClassLoader();
                } catch (SecurityException e) {
                    return cl;
                }
            }
        });
    }

    private static InputStream getResourceAsStream(final Class c, final String name) throws IOException {
        try {
            return (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws IOException {
                    return c.getResourceAsStream(name);
                }
            });
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getException());
        }
    }

    private static URL[] getResources(final ClassLoader cl, final String name) {
        return (URL[]) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                URL[] ret = null;
                try {
                    Vector v = new Vector();
                    Enumeration e = cl.getResources(name);
                    while (e != null && e.hasMoreElements()) {
                        URL url = (URL) e.nextElement();
                        if (url != null) {
                            v.addElement(url);
                        }
                    }
                    if (v.size() <= 0) {
                        return ret;
                    }
                    ret = new URL[v.size()];
                    v.copyInto(ret);
                    return ret;
                } catch (IOException | SecurityException e2) {
                    return ret;
                }
            }
        });
    }

    private static URL[] getSystemResources(final String name) {
        return (URL[]) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                URL[] ret = null;
                try {
                    Vector v = new Vector();
                    Enumeration e = ClassLoader.getSystemResources(name);
                    while (e != null && e.hasMoreElements()) {
                        URL url = (URL) e.nextElement();
                        if (url != null) {
                            v.addElement(url);
                        }
                    }
                    if (v.size() <= 0) {
                        return ret;
                    }
                    ret = new URL[v.size()];
                    v.copyInto(ret);
                    return ret;
                } catch (IOException | SecurityException e2) {
                    return ret;
                }
            }
        });
    }

    private static InputStream openStream(final URL url) throws IOException {
        try {
            return (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws IOException {
                    return url.openStream();
                }
            });
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getException());
        }
    }
}
