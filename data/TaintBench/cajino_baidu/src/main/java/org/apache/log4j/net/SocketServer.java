package org.apache.log4j.net;

import java.io.File;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RootLogger;

public class SocketServer {
    static String CONFIG_FILE_EXT = ".lcf";
    static String GENERIC = "generic";
    static Logger cat;
    static Class class$org$apache$log4j$net$SocketServer;
    static int port;
    static SocketServer server;
    File dir;
    LoggerRepository genericHierarchy;
    Hashtable hierarchyMap = new Hashtable(11);

    static {
        Class class$;
        if (class$org$apache$log4j$net$SocketServer == null) {
            class$ = class$("org.apache.log4j.net.SocketServer");
            class$org$apache$log4j$net$SocketServer = class$;
        } else {
            class$ = class$org$apache$log4j$net$SocketServer;
        }
        cat = Logger.getLogger(class$);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public static void main(String[] argv) {
        if (argv.length == 3) {
            init(argv[0], argv[1], argv[2]);
        } else {
            usage("Wrong number of arguments.");
        }
        try {
            cat.info(new StringBuffer().append("Listening on port ").append(port).toString());
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                cat.info("Waiting to accept a new client.");
                Socket socket = serverSocket.accept();
                InetAddress inetAddress = socket.getInetAddress();
                cat.info(new StringBuffer().append("Connected to client at ").append(inetAddress).toString());
                LoggerRepository h = (LoggerRepository) server.hierarchyMap.get(inetAddress);
                if (h == null) {
                    h = server.configureHierarchy(inetAddress);
                }
                cat.info("Starting new socket node.");
                new Thread(new SocketNode(socket, h)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void usage(String msg) {
        Class class$;
        System.err.println(msg);
        PrintStream printStream = System.err;
        StringBuffer append = new StringBuffer().append("Usage: java ");
        if (class$org$apache$log4j$net$SocketServer == null) {
            class$ = class$("org.apache.log4j.net.SocketServer");
            class$org$apache$log4j$net$SocketServer = class$;
        } else {
            class$ = class$org$apache$log4j$net$SocketServer;
        }
        printStream.println(append.append(class$.getName()).append(" port configFile directory").toString());
        System.exit(1);
    }

    static void init(String portStr, String configFile, String dirStr) {
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            usage(new StringBuffer().append("Could not interpret port number [").append(portStr).append("].").toString());
        }
        PropertyConfigurator.configure(configFile);
        File dir = new File(dirStr);
        if (!dir.isDirectory()) {
            usage(new StringBuffer().append("[").append(dirStr).append("] is not a directory.").toString());
        }
        server = new SocketServer(dir);
    }

    public SocketServer(File directory) {
        this.dir = directory;
    }

    /* access modifiers changed from: 0000 */
    public LoggerRepository configureHierarchy(InetAddress inetAddress) {
        cat.info(new StringBuffer().append("Locating configuration file for ").append(inetAddress).toString());
        String s = inetAddress.toString();
        int i = s.indexOf("/");
        if (i == -1) {
            cat.warn(new StringBuffer().append("Could not parse the inetAddress [").append(inetAddress).append("]. Using default hierarchy.").toString());
            return genericHierarchy();
        }
        File configFile = new File(this.dir, new StringBuffer().append(s.substring(0, i)).append(CONFIG_FILE_EXT).toString());
        if (configFile.exists()) {
            LoggerRepository h = new Hierarchy(new RootLogger(Level.DEBUG));
            this.hierarchyMap.put(inetAddress, h);
            new PropertyConfigurator().doConfigure(configFile.getAbsolutePath(), h);
            return h;
        }
        cat.warn(new StringBuffer().append("Could not find config file [").append(configFile).append("].").toString());
        return genericHierarchy();
    }

    /* access modifiers changed from: 0000 */
    public LoggerRepository genericHierarchy() {
        if (this.genericHierarchy == null) {
            File f = new File(this.dir, new StringBuffer().append(GENERIC).append(CONFIG_FILE_EXT).toString());
            if (f.exists()) {
                this.genericHierarchy = new Hierarchy(new RootLogger(Level.DEBUG));
                new PropertyConfigurator().doConfigure(f.getAbsolutePath(), this.genericHierarchy);
            } else {
                cat.warn(new StringBuffer().append("Could not find config file [").append(f).append("]. Will use the default hierarchy.").toString());
                this.genericHierarchy = LogManager.getLoggerRepository();
            }
        }
        return this.genericHierarchy;
    }
}
