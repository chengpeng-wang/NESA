package org.mozilla.javascript;

public class EcmaError extends RhinoException {
    static final long serialVersionUID = -6261226256957286699L;
    private String errorMessage;
    private String errorName;

    EcmaError(String errorName, String errorMessage, String sourceName, int lineNumber, String lineSource, int columnNumber) {
        recordErrorOrigin(sourceName, lineNumber, lineSource, columnNumber);
        this.errorName = errorName;
        this.errorMessage = errorMessage;
    }

    @Deprecated
    public EcmaError(Scriptable nativeError, String sourceName, int lineNumber, int columnNumber, String lineSource) {
        this("InternalError", ScriptRuntime.toString((Object) nativeError), sourceName, lineNumber, lineSource, columnNumber);
    }

    public String details() {
        return this.errorName + ": " + this.errorMessage;
    }

    public String getName() {
        return this.errorName;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Deprecated
    public String getSourceName() {
        return sourceName();
    }

    @Deprecated
    public int getLineNumber() {
        return lineNumber();
    }

    @Deprecated
    public int getColumnNumber() {
        return columnNumber();
    }

    @Deprecated
    public String getLineSource() {
        return lineSource();
    }

    @Deprecated
    public Scriptable getErrorObject() {
        return null;
    }
}
