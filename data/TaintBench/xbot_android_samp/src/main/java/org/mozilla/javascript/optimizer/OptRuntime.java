package org.mozilla.javascript.optimizer;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.ConsString;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.NativeFunction;
import org.mozilla.javascript.NativeGenerator;
import org.mozilla.javascript.NativeIterator;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

public final class OptRuntime extends ScriptRuntime {
    public static final Double minusOneObj = new Double(-1.0d);
    public static final Double oneObj = new Double(1.0d);
    public static final Double zeroObj = new Double(0.0d);

    public static class GeneratorState {
        static final String CLASS_NAME = "org/mozilla/javascript/optimizer/OptRuntime$GeneratorState";
        static final String resumptionPoint_NAME = "resumptionPoint";
        static final String resumptionPoint_TYPE = "I";
        static final String thisObj_NAME = "thisObj";
        static final String thisObj_TYPE = "Lorg/mozilla/javascript/Scriptable;";
        Object[] localsState;
        int maxLocals;
        int maxStack;
        public int resumptionPoint;
        Object[] stackState;
        public Scriptable thisObj;

        GeneratorState(Scriptable thisObj, int maxLocals, int maxStack) {
            this.thisObj = thisObj;
            this.maxLocals = maxLocals;
            this.maxStack = maxStack;
        }
    }

    public static Object call0(Callable fun, Scriptable thisObj, Context cx, Scriptable scope) {
        return fun.call(cx, scope, thisObj, ScriptRuntime.emptyArgs);
    }

    public static Object call1(Callable fun, Scriptable thisObj, Object arg0, Context cx, Scriptable scope) {
        return fun.call(cx, scope, thisObj, new Object[]{arg0});
    }

    public static Object call2(Callable fun, Scriptable thisObj, Object arg0, Object arg1, Context cx, Scriptable scope) {
        return fun.call(cx, scope, thisObj, new Object[]{arg0, arg1});
    }

    public static Object callN(Callable fun, Scriptable thisObj, Object[] args, Context cx, Scriptable scope) {
        return fun.call(cx, scope, thisObj, args);
    }

    public static Object callName(Object[] args, String name, Context cx, Scriptable scope) {
        return ScriptRuntime.getNameFunctionAndThis(name, cx, scope).call(cx, scope, ScriptRuntime.lastStoredScriptable(cx), args);
    }

    public static Object callName0(String name, Context cx, Scriptable scope) {
        return ScriptRuntime.getNameFunctionAndThis(name, cx, scope).call(cx, scope, ScriptRuntime.lastStoredScriptable(cx), ScriptRuntime.emptyArgs);
    }

    public static Object callProp0(Object value, String property, Context cx, Scriptable scope) {
        return ScriptRuntime.getPropFunctionAndThis(value, property, cx, scope).call(cx, scope, ScriptRuntime.lastStoredScriptable(cx), ScriptRuntime.emptyArgs);
    }

    public static Object add(Object val1, double val2) {
        if (val1 instanceof Scriptable) {
            val1 = ((Scriptable) val1).getDefaultValue(null);
        }
        if (val1 instanceof CharSequence) {
            return new ConsString((CharSequence) val1, ScriptRuntime.toString(val2));
        }
        return wrapDouble(ScriptRuntime.toNumber(val1) + val2);
    }

    public static Object add(double val1, Object val2) {
        if (val2 instanceof Scriptable) {
            val2 = ((Scriptable) val2).getDefaultValue(null);
        }
        if (val2 instanceof CharSequence) {
            return new ConsString(ScriptRuntime.toString(val1), (CharSequence) val2);
        }
        return wrapDouble(ScriptRuntime.toNumber(val2) + val1);
    }

    @Deprecated
    public static Object elemIncrDecr(Object obj, double index, Context cx, int incrDecrMask) {
        return elemIncrDecr(obj, index, cx, ScriptRuntime.getTopCallScope(cx), incrDecrMask);
    }

    public static Object elemIncrDecr(Object obj, double index, Context cx, Scriptable scope, int incrDecrMask) {
        return ScriptRuntime.elemIncrDecr(obj, new Double(index), cx, scope, incrDecrMask);
    }

    public static Object[] padStart(Object[] currentArgs, int count) {
        Object[] result = new Object[(currentArgs.length + count)];
        System.arraycopy(currentArgs, 0, result, count, currentArgs.length);
        return result;
    }

    public static void initFunction(NativeFunction fn, int functionType, Scriptable scope, Context cx) {
        ScriptRuntime.initFunction(cx, scope, fn, functionType, false);
    }

    public static Object callSpecial(Context cx, Callable fun, Scriptable thisObj, Object[] args, Scriptable scope, Scriptable callerThis, int callType, String fileName, int lineNumber) {
        return ScriptRuntime.callSpecial(cx, fun, thisObj, args, scope, callerThis, callType, fileName, lineNumber);
    }

    public static Object newObjectSpecial(Context cx, Object fun, Object[] args, Scriptable scope, Scriptable callerThis, int callType) {
        return ScriptRuntime.newSpecial(cx, fun, args, scope, callType);
    }

    public static Double wrapDouble(double num) {
        if (num == 0.0d) {
            if (1.0d / num > 0.0d) {
                return zeroObj;
            }
        } else if (num == 1.0d) {
            return oneObj;
        } else {
            if (num == -1.0d) {
                return minusOneObj;
            }
            if (num != num) {
                return NaNobj;
            }
        }
        return new Double(num);
    }

    static String encodeIntArray(int[] array) {
        if (array == null) {
            return null;
        }
        int n = array.length;
        char[] buffer = new char[((n * 2) + 1)];
        buffer[0] = 1;
        for (int i = 0; i != n; i++) {
            int value = array[i];
            int shift = (i * 2) + 1;
            buffer[shift] = (char) (value >>> 16);
            buffer[shift + 1] = (char) value;
        }
        return new String(buffer);
    }

    private static int[] decodeIntArray(String str, int arraySize) {
        if (arraySize == 0) {
            if (str == null) {
                return null;
            }
            throw new IllegalArgumentException();
        } else if (str.length() == (arraySize * 2) + 1 || str.charAt(0) == 1) {
            int[] array = new int[arraySize];
            for (int i = 0; i != arraySize; i++) {
                int shift = (i * 2) + 1;
                array[i] = (str.charAt(shift) << 16) | str.charAt(shift + 1);
            }
            return array;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static Scriptable newArrayLiteral(Object[] objects, String encodedInts, int skipCount, Context cx, Scriptable scope) {
        return ScriptRuntime.newArrayLiteral(objects, decodeIntArray(encodedInts, skipCount), cx, scope);
    }

    public static void main(final Script script, final String[] args) {
        ContextFactory.getGlobal().call(new ContextAction() {
            public Object run(Context cx) {
                Scriptable global = ScriptRuntime.getGlobal(cx);
                Object[] argsCopy = new Object[args.length];
                System.arraycopy(args, 0, argsCopy, 0, args.length);
                global.defineProperty("arguments", cx.newArray(global, argsCopy), 2);
                script.exec(cx, global);
                return null;
            }
        });
    }

    public static void throwStopIteration(Object obj) {
        throw new JavaScriptException(NativeIterator.getStopIterationObject((Scriptable) obj), "", 0);
    }

    public static Scriptable createNativeGenerator(NativeFunction funObj, Scriptable scope, Scriptable thisObj, int maxLocals, int maxStack) {
        return new NativeGenerator(scope, funObj, new GeneratorState(thisObj, maxLocals, maxStack));
    }

    public static Object[] getGeneratorStackState(Object obj) {
        GeneratorState rgs = (GeneratorState) obj;
        if (rgs.stackState == null) {
            rgs.stackState = new Object[rgs.maxStack];
        }
        return rgs.stackState;
    }

    public static Object[] getGeneratorLocalsState(Object obj) {
        GeneratorState rgs = (GeneratorState) obj;
        if (rgs.localsState == null) {
            rgs.localsState = new Object[rgs.maxLocals];
        }
        return rgs.localsState;
    }
}
