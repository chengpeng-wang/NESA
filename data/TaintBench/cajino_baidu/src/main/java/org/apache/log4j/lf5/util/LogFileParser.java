package org.apache.log4j.lf5.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.SwingUtilities;
import org.apache.log4j.lf5.Log4JLogRecord;
import org.apache.log4j.lf5.LogLevel;
import org.apache.log4j.lf5.LogLevelFormatException;
import org.apache.log4j.lf5.LogRecord;
import org.apache.log4j.lf5.viewer.LogBrokerMonitor;
import org.apache.log4j.lf5.viewer.LogFactor5ErrorDialog;
import org.apache.log4j.lf5.viewer.LogFactor5LoadingDialog;

public class LogFileParser implements Runnable {
    public static final String ATTRIBUTE_DELIMITER = "[slf5s.";
    public static final String CATEGORY_DELIMITER = "[slf5s.CATEGORY]";
    public static final String DATE_DELIMITER = "[slf5s.DATE]";
    public static final String LOCATION_DELIMITER = "[slf5s.LOCATION]";
    public static final String MESSAGE_DELIMITER = "[slf5s.MESSAGE]";
    public static final String NDC_DELIMITER = "[slf5s.NDC]";
    public static final String PRIORITY_DELIMITER = "[slf5s.PRIORITY]";
    public static final String RECORD_DELIMITER = "[slf5s.start]";
    public static final String THREAD_DELIMITER = "[slf5s.THREAD]";
    private static SimpleDateFormat _sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss,S");
    private InputStream _in;
    LogFactor5LoadingDialog _loadDialog;
    private LogBrokerMonitor _monitor;

    static void access$000(LogFileParser x0) {
        x0.destroyDialog();
    }

    public LogFileParser(File file) throws IOException, FileNotFoundException {
        this(new FileInputStream(file));
    }

    public LogFileParser(InputStream stream) throws IOException {
        this._in = null;
        this._in = stream;
    }

    public void parse(LogBrokerMonitor monitor) throws RuntimeException {
        this._monitor = monitor;
        new Thread(this).start();
    }

    public void run() {
        int index = 0;
        boolean isLogFile = false;
        this._loadDialog = new LogFactor5LoadingDialog(this._monitor.getBaseFrame(), "Loading file...");
        try {
            LogRecord temp;
            String logRecords = loadLogFile(this._in);
            while (true) {
                int counter = logRecords.indexOf(RECORD_DELIMITER, index);
                if (counter == -1) {
                    break;
                }
                temp = createLogRecord(logRecords.substring(index, counter));
                isLogFile = true;
                if (temp != null) {
                    this._monitor.addMessage(temp);
                }
                index = counter + RECORD_DELIMITER.length();
            }
            if (index < logRecords.length() && isLogFile) {
                temp = createLogRecord(logRecords.substring(index));
                if (temp != null) {
                    this._monitor.addMessage(temp);
                }
            }
            if (isLogFile) {
                SwingUtilities.invokeLater(new Runnable(this) {
                    private final LogFileParser this$0;

                    {
                        this.this$0 = r1;
                    }

                    public void run() {
                        LogFileParser.access$000(this.this$0);
                    }
                });
                this._in = null;
                return;
            }
            throw new RuntimeException("Invalid log file format");
        } catch (RuntimeException e) {
            destroyDialog();
            displayError("Error - Invalid log file format.\nPlease see documentation on how to load log files.");
        } catch (IOException e2) {
            destroyDialog();
            displayError("Error - Unable to load log file!");
        }
    }

    /* access modifiers changed from: protected */
    public void displayError(String message) {
        LogFactor5ErrorDialog error = new LogFactor5ErrorDialog(this._monitor.getBaseFrame(), message);
    }

    private void destroyDialog() {
        this._loadDialog.hide();
        this._loadDialog.dispose();
    }

    private String loadLogFile(InputStream stream) throws IOException {
        StringBuffer sb;
        BufferedInputStream br = new BufferedInputStream(stream);
        int size = br.available();
        if (size > 0) {
            sb = new StringBuffer(size);
        } else {
            sb = new StringBuffer(1024);
        }
        while (true) {
            int count = br.read();
            if (count != -1) {
                sb.append((char) count);
            } else {
                br.close();
                return sb.toString();
            }
        }
    }

    private String parseAttribute(String name, String record) {
        int index = record.indexOf(name);
        if (index == -1) {
            return null;
        }
        return getAttribute(index, record);
    }

    private long parseDate(String record) {
        try {
            String s = parseAttribute(DATE_DELIMITER, record);
            if (s == null) {
                return 0;
            }
            return _sdf.parse(s).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    private LogLevel parsePriority(String record) {
        String temp = parseAttribute(PRIORITY_DELIMITER, record);
        if (temp == null) {
            return LogLevel.DEBUG;
        }
        try {
            return LogLevel.valueOf(temp);
        } catch (LogLevelFormatException e) {
            return LogLevel.DEBUG;
        }
    }

    private String parseThread(String record) {
        return parseAttribute(THREAD_DELIMITER, record);
    }

    private String parseCategory(String record) {
        return parseAttribute(CATEGORY_DELIMITER, record);
    }

    private String parseLocation(String record) {
        return parseAttribute(LOCATION_DELIMITER, record);
    }

    private String parseMessage(String record) {
        return parseAttribute(MESSAGE_DELIMITER, record);
    }

    private String parseNDC(String record) {
        return parseAttribute(NDC_DELIMITER, record);
    }

    private String parseThrowable(String record) {
        return getAttribute(record.length(), record);
    }

    private LogRecord createLogRecord(String record) {
        if (record == null || record.trim().length() == 0) {
            return null;
        }
        LogRecord lr = new Log4JLogRecord();
        lr.setMillis(parseDate(record));
        lr.setLevel(parsePriority(record));
        lr.setCategory(parseCategory(record));
        lr.setLocation(parseLocation(record));
        lr.setThreadDescription(parseThread(record));
        lr.setNDC(parseNDC(record));
        lr.setMessage(parseMessage(record));
        lr.setThrownStackTrace(parseThrowable(record));
        return lr;
    }

    private String getAttribute(int index, String record) {
        int start = record.lastIndexOf(ATTRIBUTE_DELIMITER, index - 1);
        if (start == -1) {
            return record.substring(0, index);
        }
        return record.substring(record.indexOf("]", start) + 1, index).trim();
    }
}
