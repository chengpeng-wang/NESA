package com.sun.mail.pop3;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

public class POP3Store extends Store {
    private int defaultPort;
    boolean disableTop;
    boolean forgetTopHeaders;
    private String host;
    private boolean isSSL;
    Constructor messageConstructor;
    private String name;
    private String passwd;
    private Protocol port;
    private int portNum;
    private POP3Folder portOwner;
    boolean rsetBeforeQuit;
    private String user;

    public POP3Store(Session session, URLName url) {
        this(session, url, "pop3", 110, false);
    }

    public POP3Store(Session session, URLName url, String name, int defaultPort, boolean isSSL) {
        super(session, url);
        this.name = "pop3";
        this.defaultPort = 110;
        this.isSSL = false;
        this.port = null;
        this.portOwner = null;
        this.host = null;
        this.portNum = -1;
        this.user = null;
        this.passwd = null;
        this.rsetBeforeQuit = false;
        this.disableTop = false;
        this.forgetTopHeaders = false;
        this.messageConstructor = null;
        if (url != null) {
            name = url.getProtocol();
        }
        this.name = name;
        this.defaultPort = defaultPort;
        this.isSSL = isSSL;
        String s = session.getProperty("mail." + name + ".rsetbeforequit");
        if (s != null && s.equalsIgnoreCase("true")) {
            this.rsetBeforeQuit = true;
        }
        s = session.getProperty("mail." + name + ".disabletop");
        if (s != null && s.equalsIgnoreCase("true")) {
            this.disableTop = true;
        }
        s = session.getProperty("mail." + name + ".forgettopheaders");
        if (s != null && s.equalsIgnoreCase("true")) {
            this.forgetTopHeaders = true;
        }
        s = session.getProperty("mail." + name + ".message.class");
        if (s != null) {
            if (session.getDebug()) {
                session.getDebugOut().println("DEBUG: POP3 message class: " + s);
            }
            try {
                Class messageClass;
                try {
                    messageClass = getClass().getClassLoader().loadClass(s);
                } catch (ClassNotFoundException e) {
                    messageClass = Class.forName(s);
                }
                this.messageConstructor = messageClass.getConstructor(new Class[]{Folder.class, Integer.TYPE});
            } catch (Exception ex) {
                if (session.getDebug()) {
                    session.getDebugOut().println("DEBUG: failed to load POP3 message class: " + ex);
                }
            }
        }
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized boolean protocolConnect(String host, int portNum, String user, String passwd) throws MessagingException {
        boolean z;
        if (host == null || passwd == null || user == null) {
            z = false;
        } else {
            if (portNum == -1) {
                String portstring = this.session.getProperty("mail." + this.name + ".port");
                if (portstring != null) {
                    portNum = Integer.parseInt(portstring);
                }
            }
            if (portNum == -1) {
                portNum = this.defaultPort;
            }
            this.host = host;
            this.portNum = portNum;
            this.user = user;
            this.passwd = passwd;
            try {
                this.port = getPort(null);
                z = true;
            } catch (EOFException eex) {
                throw new AuthenticationFailedException(eex.getMessage());
            } catch (IOException ioex) {
                throw new MessagingException("Connect failed", ioex);
            }
        }
        return z;
    }

    public synchronized boolean isConnected() {
        boolean z = false;
        synchronized (this) {
            if (super.isConnected()) {
                synchronized (this) {
                    try {
                        if (this.port == null) {
                            this.port = getPort(null);
                        } else {
                            this.port.noop();
                        }
                        z = true;
                    } catch (IOException e) {
                        try {
                            super.close();
                        } catch (MessagingException e2) {
                        }
                    }
                }
            }
        }
        return z;
    }

    /* access modifiers changed from: declared_synchronized */
    public synchronized Protocol getPort(POP3Folder owner) throws IOException {
        Protocol p;
        if (this.port == null || this.portOwner != null) {
            p = new Protocol(this.host, this.portNum, this.session.getDebug(), this.session.getDebugOut(), this.session.getProperties(), "mail." + this.name, this.isSSL);
            String msg = p.login(this.user, this.passwd);
            if (msg != null) {
                try {
                    p.quit();
                } catch (IOException e) {
                }
                throw new EOFException(msg);
            }
            if (this.port == null && owner != null) {
                this.port = p;
                this.portOwner = owner;
            }
            if (this.portOwner == null) {
                this.portOwner = owner;
            }
        } else {
            this.portOwner = owner;
            p = this.port;
        }
        return p;
    }

    /* access modifiers changed from: declared_synchronized */
    public synchronized void closePort(POP3Folder owner) {
        if (this.portOwner == owner) {
            this.port = null;
            this.portOwner = null;
        }
    }

    public synchronized void close() throws MessagingException {
        try {
            if (this.port != null) {
                this.port.quit();
            }
            this.port = null;
            super.close();
        } catch (IOException e) {
            this.port = null;
            super.close();
        } catch (Throwable th) {
            this.port = null;
            super.close();
        }
        return;
    }

    public Folder getDefaultFolder() throws MessagingException {
        checkConnected();
        return new DefaultFolder(this);
    }

    public Folder getFolder(String name) throws MessagingException {
        checkConnected();
        return new POP3Folder(this, name);
    }

    public Folder getFolder(URLName url) throws MessagingException {
        checkConnected();
        return new POP3Folder(this, url.getFile());
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        if (this.port != null) {
            close();
        }
    }

    private void checkConnected() throws MessagingException {
        if (!super.isConnected()) {
            throw new MessagingException("Not connected");
        }
    }
}
