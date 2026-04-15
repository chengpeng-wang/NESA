package com.baidu.android.common.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

class SimpleFormatter extends Formatter {
    private static String format = "{0,date} {0,time}";
    private Object[] args = new Object[1];
    Date dat = new Date();
    private MessageFormat formatter;

    SimpleFormatter() {
    }

    public synchronized String format(LogRecord logRecord) {
        StringBuffer stringBuffer;
        String str = null;
        synchronized (this) {
            String methodName;
            int lineNumber;
            Object obj = null;
            for (StackTraceElement stackTraceElement : new Throwable().getStackTrace()) {
                if (stackTraceElement.getClassName().startsWith(Log.class.getName())) {
                    obj = 1;
                } else if (obj != null) {
                    str = stackTraceElement.getClassName();
                    methodName = stackTraceElement.getMethodName();
                    lineNumber = stackTraceElement.getLineNumber();
                    break;
                }
            }
            lineNumber = 0;
            methodName = null;
            logRecord.setSourceClassName(str);
            logRecord.setSourceMethodName(methodName);
            stringBuffer = new StringBuffer();
            this.dat.setTime(logRecord.getMillis());
            this.args[0] = this.dat;
            StringBuffer stringBuffer2 = new StringBuffer();
            if (this.formatter == null) {
                this.formatter = new MessageFormat(format);
            }
            this.formatter.format(this.args, stringBuffer2, null);
            stringBuffer.append(stringBuffer2);
            stringBuffer.append("." + (logRecord.getMillis() % 1000));
            stringBuffer.append(" ");
            if (logRecord.getSourceClassName() != null) {
                stringBuffer.append(logRecord.getSourceClassName());
            } else {
                stringBuffer.append(logRecord.getLoggerName());
            }
            if (logRecord.getSourceMethodName() != null) {
                stringBuffer.append(" ");
                stringBuffer.append(logRecord.getSourceMethodName());
            }
            stringBuffer.append(" ");
            stringBuffer.append(lineNumber);
            stringBuffer.append(" ");
            String formatMessage = formatMessage(logRecord);
            stringBuffer.append(logRecord.getLevel().getLocalizedName());
            stringBuffer.append(": ");
            stringBuffer.append(formatMessage);
            stringBuffer.append("\n");
            if (logRecord.getThrown() != null) {
                try {
                    StringWriter stringWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(stringWriter);
                    logRecord.getThrown().printStackTrace(printWriter);
                    printWriter.close();
                    stringBuffer.append(stringWriter.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuffer.toString();
    }
}
