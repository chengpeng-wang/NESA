package org.apache.log4j.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class JDBCAppender extends AppenderSkeleton implements Appender {
    protected ArrayList buffer = new ArrayList(this.bufferSize);
    protected int bufferSize = 1;
    protected Connection connection = null;
    protected String databasePassword = "mypassword";
    protected String databaseURL = "jdbc:odbc:myDB";
    protected String databaseUser = "me";
    private boolean locationInfo = false;
    protected ArrayList removes = new ArrayList(this.bufferSize);
    protected String sqlStatement = "";

    public boolean getLocationInfo() {
        return this.locationInfo;
    }

    public void setLocationInfo(boolean flag) {
        this.locationInfo = flag;
    }

    public void append(LoggingEvent event) {
        event.getNDC();
        event.getThreadName();
        event.getMDCCopy();
        if (this.locationInfo) {
            event.getLocationInformation();
        }
        event.getRenderedMessage();
        event.getThrowableStrRep();
        this.buffer.add(event);
        if (this.buffer.size() >= this.bufferSize) {
            flushBuffer();
        }
    }

    /* access modifiers changed from: protected */
    public String getLogStatement(LoggingEvent event) {
        return getLayout().format(event);
    }

    /* access modifiers changed from: protected */
    public void execute(String sql) throws SQLException {
        Statement stmt = null;
        try {
            Connection con = getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            closeConnection(con);
        } catch (SQLException e) {
            if (stmt != null) {
                stmt.close();
            }
            throw e;
        }
    }

    /* access modifiers changed from: protected */
    public void closeConnection(Connection con) {
    }

    /* access modifiers changed from: protected */
    public Connection getConnection() throws SQLException {
        if (!DriverManager.getDrivers().hasMoreElements()) {
            setDriver("sun.jdbc.odbc.JdbcOdbcDriver");
        }
        if (this.connection == null) {
            this.connection = DriverManager.getConnection(this.databaseURL, this.databaseUser, this.databasePassword);
        }
        return this.connection;
    }

    public void close() {
        flushBuffer();
        try {
            if (!(this.connection == null || this.connection.isClosed())) {
                this.connection.close();
            }
        } catch (SQLException e) {
            this.errorHandler.error("Error closing connection", e, 0);
        }
        this.closed = true;
    }

    public void flushBuffer() {
        this.removes.ensureCapacity(this.buffer.size());
        Iterator i = this.buffer.iterator();
        while (i.hasNext()) {
            try {
                LoggingEvent logEvent = (LoggingEvent) i.next();
                execute(getLogStatement(logEvent));
                this.removes.add(logEvent);
            } catch (SQLException e) {
                this.errorHandler.error("Failed to excute sql", e, 2);
            }
        }
        this.buffer.removeAll(this.removes);
        this.removes.clear();
    }

    public void finalize() {
        close();
    }

    public boolean requiresLayout() {
        return true;
    }

    public void setSql(String s) {
        this.sqlStatement = s;
        if (getLayout() == null) {
            setLayout(new PatternLayout(s));
        } else {
            ((PatternLayout) getLayout()).setConversionPattern(s);
        }
    }

    public String getSql() {
        return this.sqlStatement;
    }

    public void setUser(String user) {
        this.databaseUser = user;
    }

    public void setURL(String url) {
        this.databaseURL = url;
    }

    public void setPassword(String password) {
        this.databasePassword = password;
    }

    public void setBufferSize(int newBufferSize) {
        this.bufferSize = newBufferSize;
        this.buffer.ensureCapacity(this.bufferSize);
        this.removes.ensureCapacity(this.bufferSize);
    }

    public String getUser() {
        return this.databaseUser;
    }

    public String getURL() {
        return this.databaseURL;
    }

    public String getPassword() {
        return this.databasePassword;
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public void setDriver(String driverClass) {
        try {
            Class.forName(driverClass);
        } catch (Exception e) {
            this.errorHandler.error("Failed to load driver", e, 0);
        }
    }
}
