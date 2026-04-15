package org.mozilla.javascript;

import java.io.Serializable;
import org.mozilla.javascript.debug.DebuggableScript;

final class InterpreterData implements Serializable, DebuggableScript {
    static final int INITIAL_MAX_ICODE_LENGTH = 1024;
    static final int INITIAL_NUMBERTABLE_SIZE = 64;
    static final int INITIAL_STRINGTABLE_SIZE = 64;
    static final long serialVersionUID = 5067677351589230234L;
    int argCount;
    boolean[] argIsConst;
    String[] argNames;
    String encodedSource;
    int encodedSourceEnd;
    int encodedSourceStart;
    boolean evalScriptFlag;
    int firstLinePC = -1;
    boolean isStrict;
    double[] itsDoubleTable;
    int[] itsExceptionTable;
    int itsFunctionType;
    byte[] itsICode;
    int itsMaxCalleeArgs;
    int itsMaxFrameArray;
    int itsMaxLocals;
    int itsMaxStack;
    int itsMaxVars;
    String itsName;
    boolean itsNeedsActivation;
    InterpreterData[] itsNestedFunctions;
    Object[] itsRegExpLiterals;
    String itsSourceFile;
    String[] itsStringTable;
    int languageVersion;
    Object[] literalIds;
    UintMap longJumps;
    InterpreterData parentData;
    boolean topLevel;

    InterpreterData(int languageVersion, String sourceFile, String encodedSource, boolean isStrict) {
        this.languageVersion = languageVersion;
        this.itsSourceFile = sourceFile;
        this.encodedSource = encodedSource;
        this.isStrict = isStrict;
        init();
    }

    InterpreterData(InterpreterData parent) {
        this.parentData = parent;
        this.languageVersion = parent.languageVersion;
        this.itsSourceFile = parent.itsSourceFile;
        this.encodedSource = parent.encodedSource;
        init();
    }

    private void init() {
        this.itsICode = new byte[1024];
        this.itsStringTable = new String[64];
    }

    public boolean isTopLevel() {
        return this.topLevel;
    }

    public boolean isFunction() {
        return this.itsFunctionType != 0;
    }

    public String getFunctionName() {
        return this.itsName;
    }

    public int getParamCount() {
        return this.argCount;
    }

    public int getParamAndVarCount() {
        return this.argNames.length;
    }

    public String getParamOrVarName(int index) {
        return this.argNames[index];
    }

    public boolean getParamOrVarConst(int index) {
        return this.argIsConst[index];
    }

    public String getSourceName() {
        return this.itsSourceFile;
    }

    public boolean isGeneratedScript() {
        return ScriptRuntime.isGeneratedScript(this.itsSourceFile);
    }

    public int[] getLineNumbers() {
        return Interpreter.getLineNumbers(this);
    }

    public int getFunctionCount() {
        return this.itsNestedFunctions == null ? 0 : this.itsNestedFunctions.length;
    }

    public DebuggableScript getFunction(int index) {
        return this.itsNestedFunctions[index];
    }

    public DebuggableScript getParent() {
        return this.parentData;
    }
}
