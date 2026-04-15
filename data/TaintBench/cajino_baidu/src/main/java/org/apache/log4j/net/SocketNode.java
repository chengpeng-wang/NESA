package org.apache.log4j.net;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;

public class SocketNode implements Runnable {
    static Class class$org$apache$log4j$net$SocketNode;
    static Logger logger;
    LoggerRepository hierarchy;
    ObjectInputStream ois;
    Socket socket;

    static {
        Class class$;
        if (class$org$apache$log4j$net$SocketNode == null) {
            class$ = class$("org.apache.log4j.net.SocketNode");
            class$org$apache$log4j$net$SocketNode = class$;
        } else {
            class$ = class$org$apache$log4j$net$SocketNode;
        }
        logger = Logger.getLogger(class$);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public SocketNode(Socket socket, LoggerRepository hierarchy) {
        this.socket = socket;
        this.hierarchy = hierarchy;
        try {
            this.ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (InterruptedIOException e) {
            Thread.currentThread().interrupt();
            logger.error(new StringBuffer().append("Could not open ObjectInputStream to ").append(socket).toString(), e);
        } catch (IOException e2) {
            logger.error(new StringBuffer().append("Could not open ObjectInputStream to ").append(socket).toString(), e2);
        } catch (RuntimeException e3) {
            logger.error(new StringBuffer().append("Could not open ObjectInputStream to ").append(socket).toString(), e3);
        }
    }

    public void run() {
        try {
            if (this.ois != null) {
                while (true) {
                    LoggingEvent event = (LoggingEvent) this.ois.readObject();
                    Logger remoteLogger = this.hierarchy.getLogger(event.getLoggerName());
                    if (event.getLevel().isGreaterOrEqual(remoteLogger.getEffectiveLevel())) {
                        remoteLogger.callAppenders(event);
                    }
                }
            } else {
                if (this.ois != null) {
                    try {
                        this.ois.close();
                    } catch (Exception e) {
                        logger.info("Could not close connection.", e);
                    }
                }
                if (this.socket != null) {
                    try {
                        this.socket.close();
                    } catch (InterruptedIOException e2) {
                        Thread.currentThread().interrupt();
                    } catch (IOException e3) {
                    }
                }
            }
        } catch (EOFException e4) {
            logger.info("Caught java.io.EOFException closing conneciton.");
            if (this.ois != null) {
                try {
                    this.ois.close();
                } catch (Exception e5) {
                    logger.info("Could not close connection.", e5);
                }
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (InterruptedIOException e6) {
                    Thread.currentThread().interrupt();
                } catch (IOException e7) {
                }
            }
        } catch (SocketException e8) {
            logger.info("Caught java.net.SocketException closing conneciton.");
            if (this.ois != null) {
                try {
                    this.ois.close();
                } catch (Exception e52) {
                    logger.info("Could not close connection.", e52);
                }
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (InterruptedIOException e9) {
                    Thread.currentThread().interrupt();
                } catch (IOException e10) {
                }
            }
        } catch (InterruptedIOException e11) {
            Thread.currentThread().interrupt();
            logger.info(new StringBuffer().append("Caught java.io.InterruptedIOException: ").append(e11).toString());
            logger.info("Closing connection.");
            if (this.ois != null) {
                try {
                    this.ois.close();
                } catch (Exception e522) {
                    logger.info("Could not close connection.", e522);
                }
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (InterruptedIOException e12) {
                    Thread.currentThread().interrupt();
                } catch (IOException e13) {
                }
            }
        } catch (IOException e14) {
            logger.info(new StringBuffer().append("Caught java.io.IOException: ").append(e14).toString());
            logger.info("Closing connection.");
            if (this.ois != null) {
                try {
                    this.ois.close();
                } catch (Exception e5222) {
                    logger.info("Could not close connection.", e5222);
                }
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (InterruptedIOException e15) {
                    Thread.currentThread().interrupt();
                } catch (IOException e16) {
                }
            }
        } catch (Exception e52222) {
            logger.error("Unexpected exception. Closing conneciton.", e52222);
            if (this.ois != null) {
                try {
                    this.ois.close();
                } catch (Exception e522222) {
                    logger.info("Could not close connection.", e522222);
                }
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (InterruptedIOException e17) {
                    Thread.currentThread().interrupt();
                } catch (IOException e18) {
                }
            }
        } catch (Throwable th) {
            if (this.ois != null) {
                try {
                    this.ois.close();
                } catch (Exception e5222222) {
                    logger.info("Could not close connection.", e5222222);
                }
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (InterruptedIOException e19) {
                    Thread.currentThread().interrupt();
                } catch (IOException e20) {
                }
            }
        }
    }
}
