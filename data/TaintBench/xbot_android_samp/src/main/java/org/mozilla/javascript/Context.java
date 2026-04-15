package org.mozilla.javascript;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.mozilla.javascript.TopLevel.Builtins;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.ScriptNode;
import org.mozilla.javascript.debug.DebuggableScript;
import org.mozilla.javascript.debug.Debugger;
import org.mozilla.javascript.xml.XMLLib;
import org.mozilla.javascript.xml.XMLLib.Factory;

public class Context {
    public static final int FEATURE_DYNAMIC_SCOPE = 7;
    public static final int FEATURE_E4X = 6;
    public static final int FEATURE_ENHANCED_JAVA_ACCESS = 13;
    public static final int FEATURE_LOCATION_INFORMATION_IN_ERROR = 10;
    public static final int FEATURE_MEMBER_EXPR_AS_FUNCTION_NAME = 2;
    public static final int FEATURE_NON_ECMA_GET_YEAR = 1;
    public static final int FEATURE_PARENT_PROTO_PROPERTIES = 5;
    @Deprecated
    public static final int FEATURE_PARENT_PROTO_PROPRTIES = 5;
    public static final int FEATURE_RESERVED_KEYWORD_AS_IDENTIFIER = 3;
    public static final int FEATURE_STRICT_EVAL = 9;
    public static final int FEATURE_STRICT_MODE = 11;
    public static final int FEATURE_STRICT_VARS = 8;
    public static final int FEATURE_TO_STRING_AS_SOURCE = 4;
    public static final int FEATURE_V8_EXTENSIONS = 14;
    public static final int FEATURE_WARNING_AS_ERROR = 12;
    public static final int VERSION_1_0 = 100;
    public static final int VERSION_1_1 = 110;
    public static final int VERSION_1_2 = 120;
    public static final int VERSION_1_3 = 130;
    public static final int VERSION_1_4 = 140;
    public static final int VERSION_1_5 = 150;
    public static final int VERSION_1_6 = 160;
    public static final int VERSION_1_7 = 170;
    public static final int VERSION_1_8 = 180;
    public static final int VERSION_DEFAULT = 0;
    public static final int VERSION_UNKNOWN = -1;
    private static Class<?> codegenClass = Kit.classOrNull("org.mozilla.javascript.optimizer.Codegen");
    public static final Object[] emptyArgs = ScriptRuntime.emptyArgs;
    public static final String errorReporterProperty = "error reporter";
    private static String implementationVersion = null;
    private static Class<?> interpreterClass = Kit.classOrNull("org.mozilla.javascript.Interpreter");
    public static final String languageVersionProperty = "language version";
    Set<String> activationNames;
    private ClassLoader applicationClassLoader;
    XMLLib cachedXMLLib;
    /* access modifiers changed from: private */
    public ClassShutter classShutter;
    NativeCall currentActivationCall;
    Debugger debugger;
    private Object debuggerData;
    private int enterCount;
    private ErrorReporter errorReporter;
    private final ContextFactory factory;
    public boolean generateObserverCount;
    private boolean generatingDebug;
    private boolean generatingDebugChanged;
    private boolean generatingSource;
    private boolean hasClassShutter;
    int instructionCount;
    int instructionThreshold;
    Object interpreterSecurityDomain;
    boolean isContinuationsTopCall;
    ObjToIntMap iterating;
    Object lastInterpreterFrame;
    private Locale locale;
    private int maximumInterpreterStackDepth;
    private int optimizationLevel;
    ObjArray previousInterpreterInvocations;
    private Object propertyListeners;
    RegExpProxy regExpProxy;
    int scratchIndex;
    Scriptable scratchScriptable;
    long scratchUint32;
    private Object sealKey;
    private boolean sealed;
    private SecurityController securityController;
    private Map<Object, Object> threadLocalMap;
    Scriptable topCallScope;
    BaseFunction typeErrorThrower;
    boolean useDynamicScope;
    int version;
    private WrapFactory wrapFactory;

    public interface ClassShutterSetter {
        ClassShutter getClassShutter();

        void setClassShutter(ClassShutter classShutter);
    }

    @Deprecated
    public Context() {
        this(ContextFactory.getGlobal());
    }

    protected Context(ContextFactory factory) {
        int i = 0;
        this.generatingSource = true;
        this.generateObserverCount = false;
        if (factory == null) {
            throw new IllegalArgumentException("factory == null");
        }
        this.factory = factory;
        this.version = 0;
        if (codegenClass == null) {
            i = -1;
        }
        this.optimizationLevel = i;
        this.maximumInterpreterStackDepth = Integer.MAX_VALUE;
    }

    public static Context getCurrentContext() {
        return VMBridge.instance.getContext(VMBridge.instance.getThreadContextHelper());
    }

    public static Context enter() {
        return enter(null);
    }

    @Deprecated
    public static Context enter(Context cx) {
        return enter(cx, ContextFactory.getGlobal());
    }

    static final Context enter(Context cx, ContextFactory factory) {
        Object helper = VMBridge.instance.getThreadContextHelper();
        Context old = VMBridge.instance.getContext(helper);
        if (old != null) {
            cx = old;
        } else {
            if (cx == null) {
                cx = factory.makeContext();
                if (cx.enterCount != 0) {
                    throw new IllegalStateException("factory.makeContext() returned Context instance already associated with some thread");
                }
                factory.onContextCreated(cx);
                if (factory.isSealed() && !cx.isSealed()) {
                    cx.seal(null);
                }
            } else if (cx.enterCount != 0) {
                throw new IllegalStateException("can not use Context instance already associated with some thread");
            }
            VMBridge.instance.setContext(helper, cx);
        }
        cx.enterCount++;
        return cx;
    }

    public static void exit() {
        Object helper = VMBridge.instance.getThreadContextHelper();
        Context cx = VMBridge.instance.getContext(helper);
        if (cx == null) {
            throw new IllegalStateException("Calling Context.exit without previous Context.enter");
        }
        if (cx.enterCount < 1) {
            Kit.codeBug();
        }
        int i = cx.enterCount - 1;
        cx.enterCount = i;
        if (i == 0) {
            VMBridge.instance.setContext(helper, null);
            cx.factory.onContextReleased(cx);
        }
    }

    @Deprecated
    public static Object call(ContextAction action) {
        return call(ContextFactory.getGlobal(), action);
    }

    public static Object call(ContextFactory factory, final Callable callable, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        if (factory == null) {
            factory = ContextFactory.getGlobal();
        }
        return call(factory, new ContextAction() {
            public Object run(Context cx) {
                return callable.call(cx, scope, thisObj, args);
            }
        });
    }

    static Object call(ContextFactory factory, ContextAction action) {
        try {
            Object run = action.run(enter(null, factory));
            return run;
        } finally {
            exit();
        }
    }

    @Deprecated
    public static void addContextListener(ContextListener listener) {
        if ("org.mozilla.javascript.tools.debugger.Main".equals(listener.getClass().getName())) {
            try {
                listener.getClass().getMethod("attachTo", new Class[]{Kit.classOrNull("org.mozilla.javascript.ContextFactory")}).invoke(listener, new Object[]{ContextFactory.getGlobal()});
                return;
            } catch (Exception ex) {
                RuntimeException rex = new RuntimeException();
                Kit.initCause(rex, ex);
                throw rex;
            }
        }
        ContextFactory.getGlobal().addListener(listener);
    }

    @Deprecated
    public static void removeContextListener(ContextListener listener) {
        ContextFactory.getGlobal().addListener(listener);
    }

    public final ContextFactory getFactory() {
        return this.factory;
    }

    public final boolean isSealed() {
        return this.sealed;
    }

    public final void seal(Object sealKey) {
        if (this.sealed) {
            onSealedMutation();
        }
        this.sealed = true;
        this.sealKey = sealKey;
    }

    public final void unseal(Object sealKey) {
        if (sealKey == null) {
            throw new IllegalArgumentException();
        } else if (this.sealKey != sealKey) {
            throw new IllegalArgumentException();
        } else if (this.sealed) {
            this.sealed = false;
            this.sealKey = null;
        } else {
            throw new IllegalStateException();
        }
    }

    static void onSealedMutation() {
        throw new IllegalStateException();
    }

    public final int getLanguageVersion() {
        return this.version;
    }

    public void setLanguageVersion(int version) {
        if (this.sealed) {
            onSealedMutation();
        }
        checkLanguageVersion(version);
        Object listeners = this.propertyListeners;
        if (!(listeners == null || version == this.version)) {
            firePropertyChangeImpl(listeners, languageVersionProperty, Integer.valueOf(this.version), Integer.valueOf(version));
        }
        this.version = version;
    }

    public static boolean isValidLanguageVersion(int version) {
        switch (version) {
            case 0:
            case 100:
            case 110:
            case 120:
            case 130:
            case 140:
            case 150:
            case 160:
            case 170:
            case 180:
                return true;
            default:
                return false;
        }
    }

    public static void checkLanguageVersion(int version) {
        if (!isValidLanguageVersion(version)) {
            throw new IllegalArgumentException("Bad language version: " + version);
        }
    }

    public final String getImplementationVersion() {
        if (implementationVersion == null) {
            implementationVersion = ScriptRuntime.getMessage0("implementation.version");
        }
        return implementationVersion;
    }

    public final ErrorReporter getErrorReporter() {
        if (this.errorReporter == null) {
            return DefaultErrorReporter.instance;
        }
        return this.errorReporter;
    }

    public final ErrorReporter setErrorReporter(ErrorReporter reporter) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (reporter == null) {
            throw new IllegalArgumentException();
        }
        ErrorReporter old = getErrorReporter();
        if (reporter != old) {
            Object listeners = this.propertyListeners;
            if (listeners != null) {
                firePropertyChangeImpl(listeners, errorReporterProperty, old, reporter);
            }
            this.errorReporter = reporter;
        }
        return old;
    }

    public final Locale getLocale() {
        if (this.locale == null) {
            this.locale = Locale.getDefault();
        }
        return this.locale;
    }

    public final Locale setLocale(Locale loc) {
        if (this.sealed) {
            onSealedMutation();
        }
        Locale result = this.locale;
        this.locale = loc;
        return result;
    }

    public final void addPropertyChangeListener(PropertyChangeListener l) {
        if (this.sealed) {
            onSealedMutation();
        }
        this.propertyListeners = Kit.addListener(this.propertyListeners, l);
    }

    public final void removePropertyChangeListener(PropertyChangeListener l) {
        if (this.sealed) {
            onSealedMutation();
        }
        this.propertyListeners = Kit.removeListener(this.propertyListeners, l);
    }

    /* access modifiers changed from: final */
    public final void firePropertyChange(String property, Object oldValue, Object newValue) {
        Object listeners = this.propertyListeners;
        if (listeners != null) {
            firePropertyChangeImpl(listeners, property, oldValue, newValue);
        }
    }

    private void firePropertyChangeImpl(Object listeners, String property, Object oldValue, Object newValue) {
        int i = 0;
        while (true) {
            PropertyChangeListener l = Kit.getListener(listeners, i);
            if (l != null) {
                if (l instanceof PropertyChangeListener) {
                    l.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
                }
                i++;
            } else {
                return;
            }
        }
    }

    public static void reportWarning(String message, String sourceName, int lineno, String lineSource, int lineOffset) {
        Context cx = getContext();
        if (cx.hasFeature(12)) {
            reportError(message, sourceName, lineno, lineSource, lineOffset);
        } else {
            cx.getErrorReporter().warning(message, sourceName, lineno, lineSource, lineOffset);
        }
    }

    public static void reportWarning(String message) {
        int[] linep = new int[]{0};
        reportWarning(message, getSourcePositionFromStack(linep), linep[0], null, 0);
    }

    public static void reportWarning(String message, Throwable t) {
        int[] linep = new int[]{0};
        String filename = getSourcePositionFromStack(linep);
        Writer sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println(message);
        t.printStackTrace(pw);
        pw.flush();
        reportWarning(sw.toString(), filename, linep[0], null, 0);
    }

    public static void reportError(String message, String sourceName, int lineno, String lineSource, int lineOffset) {
        Context cx = getCurrentContext();
        if (cx != null) {
            cx.getErrorReporter().error(message, sourceName, lineno, lineSource, lineOffset);
            return;
        }
        throw new EvaluatorException(message, sourceName, lineno, lineSource, lineOffset);
    }

    public static void reportError(String message) {
        int[] linep = new int[]{0};
        reportError(message, getSourcePositionFromStack(linep), linep[0], null, 0);
    }

    public static EvaluatorException reportRuntimeError(String message, String sourceName, int lineno, String lineSource, int lineOffset) {
        Context cx = getCurrentContext();
        if (cx != null) {
            return cx.getErrorReporter().runtimeError(message, sourceName, lineno, lineSource, lineOffset);
        }
        throw new EvaluatorException(message, sourceName, lineno, lineSource, lineOffset);
    }

    static EvaluatorException reportRuntimeError0(String messageId) {
        return reportRuntimeError(ScriptRuntime.getMessage0(messageId));
    }

    static EvaluatorException reportRuntimeError1(String messageId, Object arg1) {
        return reportRuntimeError(ScriptRuntime.getMessage1(messageId, arg1));
    }

    static EvaluatorException reportRuntimeError2(String messageId, Object arg1, Object arg2) {
        return reportRuntimeError(ScriptRuntime.getMessage2(messageId, arg1, arg2));
    }

    static EvaluatorException reportRuntimeError3(String messageId, Object arg1, Object arg2, Object arg3) {
        return reportRuntimeError(ScriptRuntime.getMessage3(messageId, arg1, arg2, arg3));
    }

    static EvaluatorException reportRuntimeError4(String messageId, Object arg1, Object arg2, Object arg3, Object arg4) {
        return reportRuntimeError(ScriptRuntime.getMessage4(messageId, arg1, arg2, arg3, arg4));
    }

    public static EvaluatorException reportRuntimeError(String message) {
        int[] linep = new int[]{0};
        return reportRuntimeError(message, getSourcePositionFromStack(linep), linep[0], null, 0);
    }

    public final ScriptableObject initStandardObjects() {
        return initStandardObjects(null, false);
    }

    public final ScriptableObject initSafeStandardObjects() {
        return initSafeStandardObjects(null, false);
    }

    public final Scriptable initStandardObjects(ScriptableObject scope) {
        return initStandardObjects(scope, false);
    }

    public final Scriptable initSafeStandardObjects(ScriptableObject scope) {
        return initSafeStandardObjects(scope, false);
    }

    public ScriptableObject initStandardObjects(ScriptableObject scope, boolean sealed) {
        return ScriptRuntime.initStandardObjects(this, scope, sealed);
    }

    public ScriptableObject initSafeStandardObjects(ScriptableObject scope, boolean sealed) {
        return ScriptRuntime.initSafeStandardObjects(this, scope, sealed);
    }

    public static Object getUndefinedValue() {
        return Undefined.instance;
    }

    public final Object evaluateString(Scriptable scope, String source, String sourceName, int lineno, Object securityDomain) {
        Script script = compileString(source, sourceName, lineno, securityDomain);
        if (script != null) {
            return script.exec(this, scope);
        }
        return null;
    }

    public final Object evaluateReader(Scriptable scope, Reader in, String sourceName, int lineno, Object securityDomain) throws IOException {
        Script script = compileReader(scope, in, sourceName, lineno, securityDomain);
        if (script != null) {
            return script.exec(this, scope);
        }
        return null;
    }

    public Object executeScriptWithContinuations(Script script, Scriptable scope) throws ContinuationPending {
        if ((script instanceof InterpretedFunction) && ((InterpretedFunction) script).isScript()) {
            return callFunctionWithContinuations((InterpretedFunction) script, scope, ScriptRuntime.emptyArgs);
        }
        throw new IllegalArgumentException("Script argument was not a script or was not created by interpreted mode ");
    }

    public Object callFunctionWithContinuations(Callable function, Scriptable scope, Object[] args) throws ContinuationPending {
        if (!(function instanceof InterpretedFunction)) {
            throw new IllegalArgumentException("Function argument was not created by interpreted mode ");
        } else if (ScriptRuntime.hasTopCall(this)) {
            throw new IllegalStateException("Cannot have any pending top calls when executing a script with continuations");
        } else {
            this.isContinuationsTopCall = true;
            return ScriptRuntime.doTopCall(function, this, scope, scope, args);
        }
    }

    public ContinuationPending captureContinuation() {
        return new ContinuationPending(Interpreter.captureContinuation(this));
    }

    public Object resumeContinuation(Object continuation, Scriptable scope, Object functionResult) throws ContinuationPending {
        return Interpreter.restartContinuation((NativeContinuation) continuation, this, scope, new Object[]{functionResult});
    }

    public final boolean stringIsCompilableUnit(String source) {
        boolean errorseen = false;
        CompilerEnvirons compilerEnv = new CompilerEnvirons();
        compilerEnv.initFromContext(this);
        compilerEnv.setGeneratingSource(false);
        Parser p = new Parser(compilerEnv, DefaultErrorReporter.instance);
        try {
            p.parse(source, null, 1);
        } catch (EvaluatorException e) {
            errorseen = true;
        }
        if (errorseen && p.eof()) {
            return false;
        }
        return true;
    }

    @Deprecated
    public final Script compileReader(Scriptable scope, Reader in, String sourceName, int lineno, Object securityDomain) throws IOException {
        return compileReader(in, sourceName, lineno, securityDomain);
    }

    public final Script compileReader(Reader in, String sourceName, int lineno, Object securityDomain) throws IOException {
        if (lineno < 0) {
            lineno = 0;
        }
        return (Script) compileImpl(null, in, null, sourceName, lineno, securityDomain, false, null, null);
    }

    public final Script compileString(String source, String sourceName, int lineno, Object securityDomain) {
        if (lineno < 0) {
            lineno = 0;
        }
        return compileString(source, null, null, sourceName, lineno, securityDomain);
    }

    /* access modifiers changed from: final */
    public final Script compileString(String source, Evaluator compiler, ErrorReporter compilationErrorReporter, String sourceName, int lineno, Object securityDomain) {
        try {
            return (Script) compileImpl(null, null, source, sourceName, lineno, securityDomain, false, compiler, compilationErrorReporter);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public final Function compileFunction(Scriptable scope, String source, String sourceName, int lineno, Object securityDomain) {
        return compileFunction(scope, source, null, null, sourceName, lineno, securityDomain);
    }

    /* access modifiers changed from: final */
    public final Function compileFunction(Scriptable scope, String source, Evaluator compiler, ErrorReporter compilationErrorReporter, String sourceName, int lineno, Object securityDomain) {
        try {
            return (Function) compileImpl(scope, null, source, sourceName, lineno, securityDomain, true, compiler, compilationErrorReporter);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public final String decompileScript(Script script, int indent) {
        return ((NativeFunction) script).decompile(indent, 0);
    }

    public final String decompileFunction(Function fun, int indent) {
        if (fun instanceof BaseFunction) {
            return ((BaseFunction) fun).decompile(indent, 0);
        }
        return "function " + fun.getClassName() + "() {\n\t[native code]\n}\n";
    }

    public final String decompileFunctionBody(Function fun, int indent) {
        if (fun instanceof BaseFunction) {
            return ((BaseFunction) fun).decompile(indent, 1);
        }
        return "[native code]\n";
    }

    public Scriptable newObject(Scriptable scope) {
        NativeObject result = new NativeObject();
        ScriptRuntime.setBuiltinProtoAndParent(result, scope, Builtins.Object);
        return result;
    }

    public Scriptable newObject(Scriptable scope, String constructorName) {
        return newObject(scope, constructorName, ScriptRuntime.emptyArgs);
    }

    public Scriptable newObject(Scriptable scope, String constructorName, Object[] args) {
        return ScriptRuntime.newObject(this, scope, constructorName, args);
    }

    public Scriptable newArray(Scriptable scope, int length) {
        NativeArray result = new NativeArray((long) length);
        ScriptRuntime.setBuiltinProtoAndParent(result, scope, Builtins.Array);
        return result;
    }

    public Scriptable newArray(Scriptable scope, Object[] elements) {
        if (elements.getClass().getComponentType() != ScriptRuntime.ObjectClass) {
            throw new IllegalArgumentException();
        }
        NativeArray result = new NativeArray(elements);
        ScriptRuntime.setBuiltinProtoAndParent(result, scope, Builtins.Array);
        return result;
    }

    public final Object[] getElements(Scriptable object) {
        return ScriptRuntime.getArrayElements(object);
    }

    public static boolean toBoolean(Object value) {
        return ScriptRuntime.toBoolean(value);
    }

    public static double toNumber(Object value) {
        return ScriptRuntime.toNumber(value);
    }

    public static String toString(Object value) {
        return ScriptRuntime.toString(value);
    }

    public static Scriptable toObject(Object value, Scriptable scope) {
        return ScriptRuntime.toObject(scope, value);
    }

    @Deprecated
    public static Scriptable toObject(Object value, Scriptable scope, Class<?> cls) {
        return ScriptRuntime.toObject(scope, value);
    }

    public static Object javaToJS(Object value, Scriptable scope) {
        if ((value instanceof String) || (value instanceof Number) || (value instanceof Boolean) || (value instanceof Scriptable)) {
            return value;
        }
        if (value instanceof Character) {
            return String.valueOf(((Character) value).charValue());
        }
        Context cx = getContext();
        return cx.getWrapFactory().wrap(cx, scope, value, null);
    }

    public static Object jsToJava(Object value, Class<?> desiredType) throws EvaluatorException {
        return NativeJavaObject.coerceTypeImpl(desiredType, value);
    }

    @Deprecated
    public static Object toType(Object value, Class<?> desiredType) throws IllegalArgumentException {
        try {
            return jsToJava(value, desiredType);
        } catch (EvaluatorException ex) {
            IllegalArgumentException ex2 = new IllegalArgumentException(ex.getMessage());
            Kit.initCause(ex2, ex);
            throw ex2;
        }
    }

    public static RuntimeException throwAsScriptRuntimeEx(Throwable e) {
        while (e instanceof InvocationTargetException) {
            e = ((InvocationTargetException) e).getTargetException();
        }
        if (e instanceof Error) {
            Context cx = getContext();
            if (cx == null || !cx.hasFeature(13)) {
                throw ((Error) e);
            }
        }
        if (e instanceof RhinoException) {
            throw ((RhinoException) e);
        }
        throw new WrappedException(e);
    }

    public final boolean isGeneratingDebug() {
        return this.generatingDebug;
    }

    public final void setGeneratingDebug(boolean generatingDebug) {
        if (this.sealed) {
            onSealedMutation();
        }
        this.generatingDebugChanged = true;
        if (generatingDebug && getOptimizationLevel() > 0) {
            setOptimizationLevel(0);
        }
        this.generatingDebug = generatingDebug;
    }

    public final boolean isGeneratingSource() {
        return this.generatingSource;
    }

    public final void setGeneratingSource(boolean generatingSource) {
        if (this.sealed) {
            onSealedMutation();
        }
        this.generatingSource = generatingSource;
    }

    public final int getOptimizationLevel() {
        return this.optimizationLevel;
    }

    public final void setOptimizationLevel(int optimizationLevel) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (optimizationLevel == -2) {
            optimizationLevel = -1;
        }
        checkOptimizationLevel(optimizationLevel);
        if (codegenClass == null) {
            optimizationLevel = -1;
        }
        this.optimizationLevel = optimizationLevel;
    }

    public static boolean isValidOptimizationLevel(int optimizationLevel) {
        return -1 <= optimizationLevel && optimizationLevel <= 9;
    }

    public static void checkOptimizationLevel(int optimizationLevel) {
        if (!isValidOptimizationLevel(optimizationLevel)) {
            throw new IllegalArgumentException("Optimization level outside [-1..9]: " + optimizationLevel);
        }
    }

    public final int getMaximumInterpreterStackDepth() {
        return this.maximumInterpreterStackDepth;
    }

    public final void setMaximumInterpreterStackDepth(int max) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (this.optimizationLevel != -1) {
            throw new IllegalStateException("Cannot set maximumInterpreterStackDepth when optimizationLevel != -1");
        } else if (max < 1) {
            throw new IllegalArgumentException("Cannot set maximumInterpreterStackDepth to less than 1");
        } else {
            this.maximumInterpreterStackDepth = max;
        }
    }

    public final void setSecurityController(SecurityController controller) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (controller == null) {
            throw new IllegalArgumentException();
        } else if (this.securityController != null) {
            throw new SecurityException("Can not overwrite existing SecurityController object");
        } else if (SecurityController.hasGlobal()) {
            throw new SecurityException("Can not overwrite existing global SecurityController object");
        } else {
            this.securityController = controller;
        }
    }

    public final synchronized void setClassShutter(ClassShutter shutter) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (shutter == null) {
            throw new IllegalArgumentException();
        } else if (this.hasClassShutter) {
            throw new SecurityException("Cannot overwrite existing ClassShutter object");
        } else {
            this.classShutter = shutter;
            this.hasClassShutter = true;
        }
    }

    /* access modifiers changed from: final|declared_synchronized */
    public final synchronized ClassShutter getClassShutter() {
        return this.classShutter;
    }

    public final synchronized ClassShutterSetter getClassShutterSetter() {
        ClassShutterSetter classShutterSetter;
        if (this.hasClassShutter) {
            classShutterSetter = null;
        } else {
            this.hasClassShutter = true;
            classShutterSetter = new ClassShutterSetter() {
                public void setClassShutter(ClassShutter shutter) {
                    Context.this.classShutter = shutter;
                }

                public ClassShutter getClassShutter() {
                    return Context.this.classShutter;
                }
            };
        }
        return classShutterSetter;
    }

    public final Object getThreadLocal(Object key) {
        if (this.threadLocalMap == null) {
            return null;
        }
        return this.threadLocalMap.get(key);
    }

    public final synchronized void putThreadLocal(Object key, Object value) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (this.threadLocalMap == null) {
            this.threadLocalMap = new HashMap();
        }
        this.threadLocalMap.put(key, value);
    }

    public final void removeThreadLocal(Object key) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (this.threadLocalMap != null) {
            this.threadLocalMap.remove(key);
        }
    }

    @Deprecated
    public static void setCachingEnabled(boolean cachingEnabled) {
    }

    public final void setWrapFactory(WrapFactory wrapFactory) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (wrapFactory == null) {
            throw new IllegalArgumentException();
        }
        this.wrapFactory = wrapFactory;
    }

    public final WrapFactory getWrapFactory() {
        if (this.wrapFactory == null) {
            this.wrapFactory = new WrapFactory();
        }
        return this.wrapFactory;
    }

    public final Debugger getDebugger() {
        return this.debugger;
    }

    public final Object getDebuggerContextData() {
        return this.debuggerData;
    }

    public final void setDebugger(Debugger debugger, Object contextData) {
        if (this.sealed) {
            onSealedMutation();
        }
        this.debugger = debugger;
        this.debuggerData = contextData;
    }

    public static DebuggableScript getDebuggableView(Script script) {
        if (script instanceof NativeFunction) {
            return ((NativeFunction) script).getDebuggableView();
        }
        return null;
    }

    public boolean hasFeature(int featureIndex) {
        return getFactory().hasFeature(this, featureIndex);
    }

    public Factory getE4xImplementationFactory() {
        return getFactory().getE4xImplementationFactory();
    }

    public final int getInstructionObserverThreshold() {
        return this.instructionThreshold;
    }

    public final void setInstructionObserverThreshold(int threshold) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (threshold < 0) {
            throw new IllegalArgumentException();
        }
        this.instructionThreshold = threshold;
        setGenerateObserverCount(threshold > 0);
    }

    public void setGenerateObserverCount(boolean generateObserverCount) {
        this.generateObserverCount = generateObserverCount;
    }

    /* access modifiers changed from: protected */
    public void observeInstructionCount(int instructionCount) {
        getFactory().observeInstructionCount(this, instructionCount);
    }

    public GeneratedClassLoader createClassLoader(ClassLoader parent) {
        return getFactory().createClassLoader(parent);
    }

    public final ClassLoader getApplicationClassLoader() {
        if (this.applicationClassLoader == null) {
            ContextFactory f = getFactory();
            ClassLoader loader = f.getApplicationClassLoader();
            if (loader == null) {
                ClassLoader threadLoader = VMBridge.instance.getCurrentThreadClassLoader();
                if (threadLoader != null && Kit.testIfCanLoadRhinoClasses(threadLoader)) {
                    return threadLoader;
                }
                Class<?> fClass = f.getClass();
                if (fClass != ScriptRuntime.ContextFactoryClass) {
                    loader = fClass.getClassLoader();
                } else {
                    loader = getClass().getClassLoader();
                }
            }
            this.applicationClassLoader = loader;
        }
        return this.applicationClassLoader;
    }

    public final void setApplicationClassLoader(ClassLoader loader) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (loader == null) {
            this.applicationClassLoader = null;
        } else if (Kit.testIfCanLoadRhinoClasses(loader)) {
            this.applicationClassLoader = loader;
        } else {
            throw new IllegalArgumentException("Loader can not resolve Rhino classes");
        }
    }

    static Context getContext() {
        Context cx = getCurrentContext();
        if (cx != null) {
            return cx;
        }
        throw new RuntimeException("No Context associated with current Thread");
    }

    private Object compileImpl(Scriptable scope, Reader sourceReader, String sourceString, String sourceName, int lineno, Object securityDomain, boolean returnFunction, Evaluator compiler, ErrorReporter compilationErrorReporter) throws IOException {
        if (sourceName == null) {
            sourceName = "unnamed script";
        }
        if (securityDomain == null || getSecurityController() != null) {
            AstRoot ast;
            if (((sourceString == null ? 1 : 0) ^ (sourceReader == null ? 1 : 0)) == 0) {
                Kit.codeBug();
            }
            if (((scope == null ? 1 : 0) ^ returnFunction) == 0) {
                Kit.codeBug();
            }
            CompilerEnvirons compilerEnv = new CompilerEnvirons();
            compilerEnv.initFromContext(this);
            if (compilationErrorReporter == null) {
                compilationErrorReporter = compilerEnv.getErrorReporter();
            }
            if (!(this.debugger == null || sourceReader == null)) {
                sourceString = Kit.readReader(sourceReader);
                sourceReader = null;
            }
            Parser p = new Parser(compilerEnv, compilationErrorReporter);
            if (returnFunction) {
                p.calledByCompileFunction = true;
            }
            if (sourceString != null) {
                ast = p.parse(sourceString, sourceName, lineno);
            } else {
                ast = p.parse(sourceReader, sourceName, lineno);
            }
            if (!returnFunction || (ast.getFirstChild() != null && ast.getFirstChild().getType() == 109)) {
                ScriptNode tree = new IRFactory(compilerEnv, compilationErrorReporter).transformTree(ast);
                if (compiler == null) {
                    compiler = createCompiler();
                }
                DebuggableScript bytecode = compiler.compile(compilerEnv, tree, tree.getEncodedSource(), returnFunction);
                if (this.debugger != null) {
                    if (sourceString == null) {
                        Kit.codeBug();
                    }
                    if (bytecode instanceof DebuggableScript) {
                        notifyDebugger_r(this, bytecode, sourceString);
                    } else {
                        throw new RuntimeException("NOT SUPPORTED");
                    }
                }
                if (returnFunction) {
                    return compiler.createFunctionObject(this, scope, bytecode, securityDomain);
                }
                return compiler.createScriptObject(bytecode, securityDomain);
            }
            throw new IllegalArgumentException("compileFunction only accepts source with single JS function: " + sourceString);
        }
        throw new IllegalArgumentException("securityDomain should be null if setSecurityController() was never called");
    }

    private static void notifyDebugger_r(Context cx, DebuggableScript dscript, String debugSource) {
        cx.debugger.handleCompilationDone(cx, dscript, debugSource);
        for (int i = 0; i != dscript.getFunctionCount(); i++) {
            notifyDebugger_r(cx, dscript.getFunction(i), debugSource);
        }
    }

    private Evaluator createCompiler() {
        Evaluator result = null;
        if (this.optimizationLevel >= 0 && codegenClass != null) {
            result = (Evaluator) Kit.newInstanceOrNull(codegenClass);
        }
        if (result == null) {
            return createInterpreter();
        }
        return result;
    }

    static Evaluator createInterpreter() {
        return (Evaluator) Kit.newInstanceOrNull(interpreterClass);
    }

    static String getSourcePositionFromStack(int[] linep) {
        Context cx = getCurrentContext();
        if (cx == null) {
            return null;
        }
        if (cx.lastInterpreterFrame != null) {
            Evaluator evaluator = createInterpreter();
            if (evaluator != null) {
                return evaluator.getSourcePositionFromStack(cx, linep);
            }
        }
        for (StackTraceElement st : new Throwable().getStackTrace()) {
            String file = st.getFileName();
            if (!(file == null || file.endsWith(".java"))) {
                int line = st.getLineNumber();
                if (line >= 0) {
                    linep[0] = line;
                    return file;
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public RegExpProxy getRegExpProxy() {
        if (this.regExpProxy == null) {
            Class<?> cl = Kit.classOrNull("org.mozilla.javascript.regexp.RegExpImpl");
            if (cl != null) {
                this.regExpProxy = (RegExpProxy) Kit.newInstanceOrNull(cl);
            }
        }
        return this.regExpProxy;
    }

    /* access modifiers changed from: final */
    public final boolean isVersionECMA1() {
        return this.version == 0 || this.version >= 130;
    }

    /* access modifiers changed from: 0000 */
    public SecurityController getSecurityController() {
        SecurityController global = SecurityController.global();
        return global != null ? global : this.securityController;
    }

    public final boolean isGeneratingDebugChanged() {
        return this.generatingDebugChanged;
    }

    public void addActivationName(String name) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (this.activationNames == null) {
            this.activationNames = new HashSet();
        }
        this.activationNames.add(name);
    }

    public final boolean isActivationNeeded(String name) {
        return this.activationNames != null && this.activationNames.contains(name);
    }

    public void removeActivationName(String name) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (this.activationNames != null) {
            this.activationNames.remove(name);
        }
    }
}
