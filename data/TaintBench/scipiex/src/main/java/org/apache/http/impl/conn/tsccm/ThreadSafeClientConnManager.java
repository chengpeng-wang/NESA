package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.DefaultClientConnectionOperator;
import org.apache.http.params.HttpParams;

public class ThreadSafeClientConnManager implements ClientConnectionManager {
    protected final ClientConnectionOperator connOperator;
    protected final AbstractConnPool connectionPool;
    /* access modifiers changed from: private|final */
    public final Log log = LogFactory.getLog(getClass());
    protected final SchemeRegistry schemeRegistry;

    public ThreadSafeClientConnManager(HttpParams params, SchemeRegistry schreg) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else if (schreg == null) {
            throw new IllegalArgumentException("Scheme registry may not be null");
        } else {
            this.schemeRegistry = schreg;
            this.connOperator = createConnectionOperator(schreg);
            this.connectionPool = createConnectionPool(params);
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    /* access modifiers changed from: protected */
    public AbstractConnPool createConnectionPool(HttpParams params) {
        return new ConnPoolByRoute(this.connOperator, params);
    }

    /* access modifiers changed from: protected */
    public ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
        return new DefaultClientConnectionOperator(schreg);
    }

    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }

    public ClientConnectionRequest requestConnection(final HttpRoute route, Object state) {
        final PoolEntryRequest poolRequest = this.connectionPool.requestPoolEntry(route, state);
        return new ClientConnectionRequest() {
            public void abortRequest() {
                poolRequest.abortRequest();
            }

            public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
                if (route == null) {
                    throw new IllegalArgumentException("Route may not be null.");
                }
                if (ThreadSafeClientConnManager.this.log.isDebugEnabled()) {
                    ThreadSafeClientConnManager.this.log.debug("Get connection: " + route + ", timeout = " + timeout);
                }
                return new BasicPooledConnAdapter(ThreadSafeClientConnManager.this, poolRequest.getPoolEntry(timeout, tunit));
            }
        };
    }

    public void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit) {
        if (conn instanceof BasicPooledConnAdapter) {
            BasicPooledConnAdapter hca = (BasicPooledConnAdapter) conn;
            if (hca.getPoolEntry() == null || hca.getManager() == this) {
                synchronized (hca) {
                    BasicPoolEntry entry = (BasicPoolEntry) hca.getPoolEntry();
                    if (entry == null) {
                        return;
                    }
                    boolean reusable;
                    try {
                        if (hca.isOpen() && !hca.isMarkedReusable()) {
                            hca.shutdown();
                        }
                        reusable = hca.isMarkedReusable();
                        if (this.log.isDebugEnabled()) {
                            if (reusable) {
                                this.log.debug("Released connection is reusable.");
                            } else {
                                this.log.debug("Released connection is not reusable.");
                            }
                        }
                        hca.detach();
                        this.connectionPool.freeEntry(entry, reusable, validDuration, timeUnit);
                    } catch (IOException iox) {
                        if (this.log.isDebugEnabled()) {
                            this.log.debug("Exception shutting down released connection.", iox);
                        }
                        reusable = hca.isMarkedReusable();
                        if (this.log.isDebugEnabled()) {
                            if (reusable) {
                                this.log.debug("Released connection is reusable.");
                            } else {
                                this.log.debug("Released connection is not reusable.");
                            }
                        }
                        hca.detach();
                        this.connectionPool.freeEntry(entry, reusable, validDuration, timeUnit);
                        return;
                    } catch (Throwable th) {
                        Throwable th2 = th;
                        reusable = hca.isMarkedReusable();
                        if (this.log.isDebugEnabled()) {
                            if (reusable) {
                                this.log.debug("Released connection is reusable.");
                            } else {
                                this.log.debug("Released connection is not reusable.");
                            }
                        }
                        hca.detach();
                        this.connectionPool.freeEntry(entry, reusable, validDuration, timeUnit);
                    }
                }
            } else {
                throw new IllegalArgumentException("Connection not obtained from this manager.");
            }
        }
        throw new IllegalArgumentException("Connection class mismatch, connection not obtained from this manager.");
    }

    public void shutdown() {
        this.log.debug("Shutting down");
        this.connectionPool.shutdown();
    }

    public int getConnectionsInPool(HttpRoute route) {
        return ((ConnPoolByRoute) this.connectionPool).getConnectionsInPool(route);
    }

    public int getConnectionsInPool() {
        this.connectionPool.poolLock.lock();
        try {
            int i = this.connectionPool.numConnections;
            return i;
        } finally {
            this.connectionPool.poolLock.unlock();
        }
    }

    public void closeIdleConnections(long idleTimeout, TimeUnit tunit) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("Closing connections idle for " + idleTimeout + " " + tunit);
        }
        this.connectionPool.poolLock.lock();
        try {
            this.connectionPool.closeIdleConnections(idleTimeout, tunit);
            this.connectionPool.deleteClosedConnections();
        } finally {
            this.connectionPool.poolLock.unlock();
        }
    }

    public void closeExpiredConnections() {
        this.log.debug("Closing expired connections");
        this.connectionPool.poolLock.lock();
        try {
            this.connectionPool.closeExpiredConnections();
            this.connectionPool.deleteClosedConnections();
        } finally {
            this.connectionPool.poolLock.unlock();
        }
    }
}
