package org.mozilla.javascript.tools;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.SecurityUtilities;
import org.mozilla.javascript.WrappedException;

public class ToolErrorReporter implements ErrorReporter {
    private static final String messagePrefix = "js: ";
    private PrintStream err;
    private boolean hasReportedErrorFlag;
    private boolean reportWarnings;

    public ToolErrorReporter(boolean reportWarnings) {
        this(reportWarnings, System.err);
    }

    public ToolErrorReporter(boolean reportWarnings, PrintStream err) {
        this.reportWarnings = reportWarnings;
        this.err = err;
    }

    public static String getMessage(String messageId) {
        return getMessage(messageId, (Object[]) null);
    }

    public static String getMessage(String messageId, String argument) {
        return getMessage(messageId, new Object[]{argument});
    }

    public static String getMessage(String messageId, Object arg1, Object arg2) {
        return getMessage(messageId, new Object[]{arg1, arg2});
    }

    public static String getMessage(String messageId, Object[] args) {
        Context cx = Context.getCurrentContext();
        try {
            String formatString = ResourceBundle.getBundle("org.mozilla.javascript.tools.resources.Messages", cx == null ? Locale.getDefault() : cx.getLocale()).getString(messageId);
            return args == null ? formatString : new MessageFormat(formatString).format(args);
        } catch (MissingResourceException e) {
            throw new RuntimeException("no message resource found for message property " + messageId);
        }
    }

    private static String getExceptionMessage(RhinoException ex) {
        if (ex instanceof JavaScriptException) {
            return getMessage("msg.uncaughtJSException", ex.details());
        }
        if (ex instanceof EcmaError) {
            return getMessage("msg.uncaughtEcmaError", ex.details());
        }
        if (ex instanceof EvaluatorException) {
            return ex.details();
        }
        return ex.toString();
    }

    public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
        if (this.reportWarnings) {
            reportErrorMessage(message, sourceName, line, lineSource, lineOffset, true);
        }
    }

    public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
        this.hasReportedErrorFlag = true;
        reportErrorMessage(message, sourceName, line, lineSource, lineOffset, false);
    }

    public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
        return new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
    }

    public boolean hasReportedError() {
        return this.hasReportedErrorFlag;
    }

    public boolean isReportingWarnings() {
        return this.reportWarnings;
    }

    public void setIsReportingWarnings(boolean reportWarnings) {
        this.reportWarnings = reportWarnings;
    }

    public static void reportException(ErrorReporter er, RhinoException ex) {
        if (er instanceof ToolErrorReporter) {
            ((ToolErrorReporter) er).reportException(ex);
            return;
        }
        er.error(getExceptionMessage(ex), ex.sourceName(), ex.lineNumber(), ex.lineSource(), ex.columnNumber());
    }

    public void reportException(RhinoException ex) {
        if (ex instanceof WrappedException) {
            ((WrappedException) ex).printStackTrace(this.err);
            return;
        }
        reportErrorMessage(getExceptionMessage(ex) + SecurityUtilities.getSystemProperty("line.separator") + ex.getScriptStackTrace(), ex.sourceName(), ex.lineNumber(), ex.lineSource(), ex.columnNumber(), false);
    }

    private void reportErrorMessage(String message, String sourceName, int line, String lineSource, int lineOffset, boolean justWarning) {
        if (line > 0) {
            String lineStr = String.valueOf(line);
            if (sourceName != null) {
                message = getMessage("msg.format3", new Object[]{sourceName, lineStr, message});
            } else {
                message = getMessage("msg.format2", new Object[]{lineStr, message});
            }
        } else {
            message = getMessage("msg.format1", new Object[]{message});
        }
        if (justWarning) {
            message = getMessage("msg.warning", message);
        }
        this.err.println(messagePrefix + message);
        if (lineSource != null) {
            this.err.println(messagePrefix + lineSource);
            this.err.println(messagePrefix + buildIndicator(lineOffset));
        }
    }

    private String buildIndicator(int offset) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < offset - 1; i++) {
            sb.append(".");
        }
        sb.append("^");
        return sb.toString();
    }
}
