package org.apache.log4j.lf5;

import org.apache.log4j.spi.ThrowableInformation;

public class Log4JLogRecord extends LogRecord {
    public boolean isSevereLevel() {
        if (LogLevel.ERROR.equals(getLevel()) || LogLevel.FATAL.equals(getLevel())) {
            return true;
        }
        return false;
    }

    public void setThrownStackTrace(ThrowableInformation throwableInfo) {
        String[] stackTraceArray = throwableInfo.getThrowableStrRep();
        StringBuffer stackTrace = new StringBuffer();
        for (String append : stackTraceArray) {
            stackTrace.append(new StringBuffer().append(append).append("\n").toString());
        }
        this._thrownStackTrace = stackTrace.toString();
    }
}
