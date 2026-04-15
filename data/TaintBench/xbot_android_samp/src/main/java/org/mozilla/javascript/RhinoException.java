package org.mozilla.javascript;

import java.io.CharArrayWriter;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RhinoException extends RuntimeException {
    private static final Pattern JAVA_STACK_PATTERN = Pattern.compile("_c_(.*)_\\d+");
    static final long serialVersionUID = 1883500631321581169L;
    private static StackStyle stackStyle;
    private int columnNumber;
    int[] interpreterLineData;
    Object interpreterStackInfo;
    private int lineNumber;
    private String lineSource;
    private String sourceName;

    static {
        stackStyle = StackStyle.RHINO;
        String style = System.getProperty("rhino.stack.style");
        if (style == null) {
            return;
        }
        if ("Rhino".equalsIgnoreCase(style)) {
            stackStyle = StackStyle.RHINO;
        } else if ("Mozilla".equalsIgnoreCase(style)) {
            stackStyle = StackStyle.MOZILLA;
        } else if ("V8".equalsIgnoreCase(style)) {
            stackStyle = StackStyle.V8;
        }
    }

    RhinoException() {
        Evaluator e = Context.createInterpreter();
        if (e != null) {
            e.captureStackInfo(this);
        }
    }

    RhinoException(String details) {
        super(details);
        Evaluator e = Context.createInterpreter();
        if (e != null) {
            e.captureStackInfo(this);
        }
    }

    public final String getMessage() {
        String details = details();
        if (this.sourceName == null || this.lineNumber <= 0) {
            return details;
        }
        StringBuilder buf = new StringBuilder(details);
        buf.append(" (");
        if (this.sourceName != null) {
            buf.append(this.sourceName);
        }
        if (this.lineNumber > 0) {
            buf.append('#');
            buf.append(this.lineNumber);
        }
        buf.append(')');
        return buf.toString();
    }

    public String details() {
        return super.getMessage();
    }

    public final String sourceName() {
        return this.sourceName;
    }

    public final void initSourceName(String sourceName) {
        if (sourceName == null) {
            throw new IllegalArgumentException();
        } else if (this.sourceName != null) {
            throw new IllegalStateException();
        } else {
            this.sourceName = sourceName;
        }
    }

    public final int lineNumber() {
        return this.lineNumber;
    }

    public final void initLineNumber(int lineNumber) {
        if (lineNumber <= 0) {
            throw new IllegalArgumentException(String.valueOf(lineNumber));
        } else if (this.lineNumber > 0) {
            throw new IllegalStateException();
        } else {
            this.lineNumber = lineNumber;
        }
    }

    public final int columnNumber() {
        return this.columnNumber;
    }

    public final void initColumnNumber(int columnNumber) {
        if (columnNumber <= 0) {
            throw new IllegalArgumentException(String.valueOf(columnNumber));
        } else if (this.columnNumber > 0) {
            throw new IllegalStateException();
        } else {
            this.columnNumber = columnNumber;
        }
    }

    public final String lineSource() {
        return this.lineSource;
    }

    public final void initLineSource(String lineSource) {
        if (lineSource == null) {
            throw new IllegalArgumentException();
        } else if (this.lineSource != null) {
            throw new IllegalStateException();
        } else {
            this.lineSource = lineSource;
        }
    }

    /* access modifiers changed from: final */
    public final void recordErrorOrigin(String sourceName, int lineNumber, String lineSource, int columnNumber) {
        if (lineNumber == -1) {
            lineNumber = 0;
        }
        if (sourceName != null) {
            initSourceName(sourceName);
        }
        if (lineNumber != 0) {
            initLineNumber(lineNumber);
        }
        if (lineSource != null) {
            initLineSource(lineSource);
        }
        if (columnNumber != 0) {
            initColumnNumber(columnNumber);
        }
    }

    private String generateStackTrace() {
        CharArrayWriter writer = new CharArrayWriter();
        super.printStackTrace(new PrintWriter(writer));
        String origStackTrace = writer.toString();
        Evaluator e = Context.createInterpreter();
        if (e != null) {
            return e.getPatchedStack(this, origStackTrace);
        }
        return null;
    }

    public String getScriptStackTrace() {
        return getScriptStackTrace(-1, null);
    }

    public String getScriptStackTrace(int limit, String functionName) {
        return formatStackTrace(getScriptStack(limit, functionName), details());
    }

    static String formatStackTrace(ScriptStackElement[] stack, String message) {
        StringBuilder buffer = new StringBuilder();
        String lineSeparator = SecurityUtilities.getSystemProperty("line.separator");
        if (stackStyle == StackStyle.V8 && !"null".equals(message)) {
            buffer.append(message);
            buffer.append(lineSeparator);
        }
        for (ScriptStackElement elem : stack) {
            switch (stackStyle) {
                case MOZILLA:
                    elem.renderMozillaStyle(buffer);
                    break;
                case V8:
                    elem.renderV8Style(buffer);
                    break;
                case RHINO:
                    elem.renderJavaStyle(buffer);
                    break;
                default:
                    break;
            }
            buffer.append(lineSeparator);
        }
        return buffer.toString();
    }

    @Deprecated
    public String getScriptStackTrace(FilenameFilter filter) {
        return getScriptStackTrace();
    }

    public ScriptStackElement[] getScriptStack() {
        return getScriptStack(-1, null);
    }

    public ScriptStackElement[] getScriptStack(int limit, String hideFunction) {
        List<ScriptStackElement> list = new ArrayList();
        ScriptStackElement[][] interpreterStack = null;
        if (this.interpreterStackInfo != null) {
            Evaluator interpreter = Context.createInterpreter();
            if (interpreter instanceof Interpreter) {
                interpreterStack = ((Interpreter) interpreter).getScriptStackElements(this);
            }
        }
        int interpreterStackIndex = 0;
        StackTraceElement[] stack = getStackTrace();
        int count = 0;
        boolean printStarted = hideFunction == null;
        int length = stack.length;
        int i = 0;
        while (true) {
            int i2 = i;
            int interpreterStackIndex2 = interpreterStackIndex;
            if (i2 >= length) {
                return (ScriptStackElement[]) list.toArray(new ScriptStackElement[list.size()]);
            }
            StackTraceElement e = stack[i2];
            String fileName = e.getFileName();
            if (e.getMethodName().startsWith("_c_") && e.getLineNumber() > -1 && fileName != null && !fileName.endsWith(".java")) {
                String methodName = e.getMethodName();
                Matcher match = JAVA_STACK_PATTERN.matcher(methodName);
                methodName = ("_c_script_0".equals(methodName) || !match.find()) ? null : match.group(1);
                if (!printStarted && hideFunction.equals(methodName)) {
                    printStarted = true;
                } else if (printStarted && (limit < 0 || count < limit)) {
                    list.add(new ScriptStackElement(fileName, methodName, e.getLineNumber()));
                    count++;
                }
                interpreterStackIndex = interpreterStackIndex2;
            } else if (!"org.mozilla.javascript.Interpreter".equals(e.getClassName()) || !"interpretLoop".equals(e.getMethodName()) || interpreterStack == null || interpreterStack.length <= interpreterStackIndex2) {
                interpreterStackIndex = interpreterStackIndex2;
            } else {
                interpreterStackIndex = interpreterStackIndex2 + 1;
                for (ScriptStackElement elem : interpreterStack[interpreterStackIndex2]) {
                    if (!printStarted) {
                        if (hideFunction.equals(elem.functionName)) {
                            printStarted = true;
                        }
                    }
                    if (printStarted && (limit < 0 || count < limit)) {
                        list.add(elem);
                        count++;
                    }
                }
            }
            i = i2 + 1;
        }
    }

    public void printStackTrace(PrintWriter s) {
        if (this.interpreterStackInfo == null) {
            super.printStackTrace(s);
        } else {
            s.print(generateStackTrace());
        }
    }

    public void printStackTrace(PrintStream s) {
        if (this.interpreterStackInfo == null) {
            super.printStackTrace(s);
        } else {
            s.print(generateStackTrace());
        }
    }

    public static boolean usesMozillaStackStyle() {
        return stackStyle == StackStyle.MOZILLA;
    }

    public static void useMozillaStackStyle(boolean flag) {
        stackStyle = flag ? StackStyle.MOZILLA : StackStyle.RHINO;
    }

    public static void setStackStyle(StackStyle style) {
        stackStyle = style;
    }

    public static StackStyle getStackStyle() {
        return stackStyle;
    }
}
