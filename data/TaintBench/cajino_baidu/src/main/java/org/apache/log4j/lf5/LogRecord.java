package org.apache.log4j.lf5;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

public abstract class LogRecord implements Serializable {
    protected static long _seqCount = 0;
    protected String _category = "Debug";
    protected LogLevel _level = LogLevel.INFO;
    protected String _location = "";
    protected String _message = "";
    protected long _millis = System.currentTimeMillis();
    protected String _ndc = "";
    protected long _sequenceNumber = getNextId();
    protected String _thread = Thread.currentThread().toString();
    protected Throwable _thrown;
    protected String _thrownStackTrace;

    public abstract boolean isSevereLevel();

    public LogLevel getLevel() {
        return this._level;
    }

    public void setLevel(LogLevel level) {
        this._level = level;
    }

    public boolean hasThrown() {
        Throwable thrown = getThrown();
        if (thrown == null) {
            return false;
        }
        String thrownString = thrown.toString();
        if (thrownString == null || thrownString.trim().length() == 0) {
            return false;
        }
        return true;
    }

    public boolean isFatal() {
        return isSevereLevel() || hasThrown();
    }

    public String getCategory() {
        return this._category;
    }

    public void setCategory(String category) {
        this._category = category;
    }

    public String getMessage() {
        return this._message;
    }

    public void setMessage(String message) {
        this._message = message;
    }

    public long getSequenceNumber() {
        return this._sequenceNumber;
    }

    public void setSequenceNumber(long number) {
        this._sequenceNumber = number;
    }

    public long getMillis() {
        return this._millis;
    }

    public void setMillis(long millis) {
        this._millis = millis;
    }

    public String getThreadDescription() {
        return this._thread;
    }

    public void setThreadDescription(String threadDescription) {
        this._thread = threadDescription;
    }

    public String getThrownStackTrace() {
        return this._thrownStackTrace;
    }

    public void setThrownStackTrace(String trace) {
        this._thrownStackTrace = trace;
    }

    public Throwable getThrown() {
        return this._thrown;
    }

    public void setThrown(Throwable thrown) {
        if (thrown != null) {
            this._thrown = thrown;
            StringWriter sw = new StringWriter();
            PrintWriter out = new PrintWriter(sw);
            thrown.printStackTrace(out);
            out.flush();
            this._thrownStackTrace = sw.toString();
            try {
                out.close();
                sw.close();
            } catch (IOException e) {
            }
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(new StringBuffer().append("LogRecord: [").append(this._level).append(", ").append(this._message).append("]").toString());
        return buf.toString();
    }

    public String getNDC() {
        return this._ndc;
    }

    public void setNDC(String ndc) {
        this._ndc = ndc;
    }

    public String getLocation() {
        return this._location;
    }

    public void setLocation(String location) {
        this._location = location;
    }

    public static synchronized void resetSequenceNumber() {
        synchronized (LogRecord.class) {
            _seqCount = 0;
        }
    }

    protected static synchronized long getNextId() {
        long j;
        synchronized (LogRecord.class) {
            _seqCount++;
            j = _seqCount;
        }
        return j;
    }
}
