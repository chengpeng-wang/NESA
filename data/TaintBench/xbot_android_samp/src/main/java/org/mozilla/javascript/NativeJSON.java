package org.mozilla.javascript;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import org.mozilla.javascript.json.JsonParser;
import org.mozilla.javascript.json.JsonParser.ParseException;

public final class NativeJSON extends IdScriptableObject {
    private static final int Id_parse = 2;
    private static final int Id_stringify = 3;
    private static final int Id_toSource = 1;
    private static final Object JSON_TAG = "JSON";
    private static final int LAST_METHOD_ID = 3;
    private static final int MAX_ID = 3;
    private static final int MAX_STRINGIFY_GAP_LENGTH = 10;
    static final long serialVersionUID = -4567599697595654984L;

    private static class StringifyState {
        Context cx;
        String gap;
        String indent;
        List<Object> propertyList;
        Callable replacer;
        Scriptable scope;
        Object space;
        Stack<Scriptable> stack = new Stack();

        StringifyState(Context cx, Scriptable scope, String indent, String gap, Callable replacer, List<Object> propertyList, Object space) {
            this.cx = cx;
            this.scope = scope;
            this.indent = indent;
            this.gap = gap;
            this.replacer = replacer;
            this.propertyList = propertyList;
            this.space = space;
        }
    }

    static void init(Scriptable scope, boolean sealed) {
        NativeJSON obj = new NativeJSON();
        obj.activatePrototypeMap(3);
        obj.setPrototype(ScriptableObject.getObjectPrototype(scope));
        obj.setParentScope(scope);
        if (sealed) {
            obj.sealObject();
        }
        ScriptableObject.defineProperty(scope, "JSON", obj, 2);
    }

    private NativeJSON() {
    }

    public String getClassName() {
        return "JSON";
    }

    /* access modifiers changed from: protected */
    public void initPrototypeId(int id) {
        if (id <= 3) {
            int arity;
            String name;
            switch (id) {
                case 1:
                    arity = 0;
                    name = "toSource";
                    break;
                case 2:
                    arity = 2;
                    name = "parse";
                    break;
                case 3:
                    arity = 3;
                    name = "stringify";
                    break;
                default:
                    throw new IllegalStateException(String.valueOf(id));
            }
            initPrototypeMethod(JSON_TAG, id, name, arity);
            return;
        }
        throw new IllegalStateException(String.valueOf(id));
    }

    /* JADX WARNING: Missing block: B:18:0x0047, code skipped:
            r2 = r14[1];
     */
    /* JADX WARNING: Missing block: B:19:0x0049, code skipped:
            r5 = r14[0];
     */
    /* JADX WARNING: Missing block: B:25:?, code skipped:
            return stringify(r11, r12, r5, r2, r4);
     */
    public java.lang.Object execIdCall(org.mozilla.javascript.IdFunctionObject r10, org.mozilla.javascript.Context r11, org.mozilla.javascript.Scriptable r12, org.mozilla.javascript.Scriptable r13, java.lang.Object[] r14) {
        /*
        r9 = this;
        r8 = 0;
        r7 = 1;
        r6 = JSON_TAG;
        r6 = r10.hasTag(r6);
        if (r6 != 0) goto L_0x000f;
    L_0x000a:
        r6 = super.execIdCall(r10, r11, r12, r13, r14);
    L_0x000e:
        return r6;
    L_0x000f:
        r1 = r10.methodId();
        switch(r1) {
            case 1: goto L_0x0020;
            case 2: goto L_0x0023;
            case 3: goto L_0x003d;
            default: goto L_0x0016;
        };
    L_0x0016:
        r6 = new java.lang.IllegalStateException;
        r7 = java.lang.String.valueOf(r1);
        r6.<init>(r7);
        throw r6;
    L_0x0020:
        r6 = "JSON";
        goto L_0x000e;
    L_0x0023:
        r0 = org.mozilla.javascript.ScriptRuntime.toString(r14, r8);
        r3 = 0;
        r6 = r14.length;
        if (r6 <= r7) goto L_0x002d;
    L_0x002b:
        r3 = r14[r7];
    L_0x002d:
        r6 = r3 instanceof org.mozilla.javascript.Callable;
        if (r6 == 0) goto L_0x0038;
    L_0x0031:
        r3 = (org.mozilla.javascript.Callable) r3;
        r6 = parse(r11, r12, r0, r3);
        goto L_0x000e;
    L_0x0038:
        r6 = parse(r11, r12, r0);
        goto L_0x000e;
    L_0x003d:
        r5 = 0;
        r2 = 0;
        r4 = 0;
        r6 = r14.length;
        switch(r6) {
            case 0: goto L_0x004b;
            case 1: goto L_0x0049;
            case 2: goto L_0x0047;
            default: goto L_0x0044;
        };
    L_0x0044:
        r6 = 2;
        r4 = r14[r6];
    L_0x0047:
        r2 = r14[r7];
    L_0x0049:
        r5 = r14[r8];
    L_0x004b:
        r6 = stringify(r11, r12, r5, r2, r4);
        goto L_0x000e;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.NativeJSON.execIdCall(org.mozilla.javascript.IdFunctionObject, org.mozilla.javascript.Context, org.mozilla.javascript.Scriptable, org.mozilla.javascript.Scriptable, java.lang.Object[]):java.lang.Object");
    }

    private static Object parse(Context cx, Scriptable scope, String jtext) {
        try {
            return new JsonParser(cx, scope).parseValue(jtext);
        } catch (ParseException ex) {
            throw ScriptRuntime.constructError("SyntaxError", ex.getMessage());
        }
    }

    public static Object parse(Context cx, Scriptable scope, String jtext, Callable reviver) {
        Object unfiltered = parse(cx, scope, jtext);
        Scriptable root = cx.newObject(scope);
        root.put("", root, unfiltered);
        return walk(cx, scope, reviver, root, "");
    }

    private static Object walk(Context cx, Scriptable scope, Callable reviver, Scriptable holder, Object name) {
        Scriptable property;
        if (name instanceof Number) {
            property = holder.get(((Number) name).intValue(), holder);
        } else {
            property = holder.get((String) name, holder);
        }
        if (property instanceof Scriptable) {
            Scriptable val = property;
            Object newElement;
            if (val instanceof NativeArray) {
                long len = ((NativeArray) val).getLength();
                for (long i = 0; i < len; i++) {
                    if (i > 2147483647L) {
                        String id = Long.toString(i);
                        newElement = walk(cx, scope, reviver, val, id);
                        if (newElement == Undefined.instance) {
                            val.delete(id);
                        } else {
                            val.put(id, val, newElement);
                        }
                    } else {
                        int idx = (int) i;
                        newElement = walk(cx, scope, reviver, val, Integer.valueOf(idx));
                        if (newElement == Undefined.instance) {
                            val.delete(idx);
                        } else {
                            val.put(idx, val, newElement);
                        }
                    }
                }
            } else {
                for (Object p : val.getIds()) {
                    newElement = walk(cx, scope, reviver, val, p);
                    if (newElement == Undefined.instance) {
                        if (p instanceof Number) {
                            val.delete(((Number) p).intValue());
                        } else {
                            val.delete((String) p);
                        }
                    } else if (p instanceof Number) {
                        val.put(((Number) p).intValue(), val, newElement);
                    } else {
                        val.put((String) p, val, newElement);
                    }
                }
            }
        }
        return reviver.call(cx, scope, holder, new Object[]{name, property});
    }

    private static String repeat(char c, int count) {
        char[] chars = new char[count];
        Arrays.fill(chars, c);
        return new String(chars);
    }

    public static Object stringify(Context cx, Scriptable scope, Object value, Object replacer, Object space) {
        String space2;
        Object valueOf;
        String indent = "";
        String gap = "";
        List<Object> propertyList = null;
        Callable replacerFunction = null;
        if (replacer instanceof Callable) {
            replacerFunction = (Callable) replacer;
        } else if (replacer instanceof NativeArray) {
            propertyList = new LinkedList();
            NativeArray replacerArray = (NativeArray) replacer;
            for (Integer intValue : replacerArray.getIndexIds()) {
                Object v = replacerArray.get(intValue.intValue(), replacerArray);
                if ((v instanceof String) || (v instanceof Number)) {
                    propertyList.add(v);
                } else if ((v instanceof NativeString) || (v instanceof NativeNumber)) {
                    propertyList.add(ScriptRuntime.toString(v));
                }
            }
        }
        if (space2 instanceof NativeNumber) {
            space2 = Double.valueOf(ScriptRuntime.toNumber(space2));
        } else if (space2 instanceof NativeString) {
            space2 = ScriptRuntime.toString(space2);
        }
        if (space2 instanceof Number) {
            int gapLength = Math.min(10, (int) ScriptRuntime.toInteger((Object) space2));
            gap = gapLength > 0 ? repeat(' ', gapLength) : "";
            valueOf = Integer.valueOf(gapLength);
        } else {
            String valueOf2;
            if (space2 instanceof String) {
                gap = space2;
                if (gap.length() > 10) {
                    gap = gap.substring(0, 10);
                    valueOf2 = space2;
                }
            }
            valueOf2 = space2;
        }
        StringifyState state = new StringifyState(cx, scope, indent, gap, replacerFunction, propertyList, valueOf2);
        ScriptableObject wrapper = new NativeObject();
        wrapper.setParentScope(scope);
        wrapper.setPrototype(ScriptableObject.getObjectPrototype(scope));
        wrapper.defineProperty("", value, 0);
        return str("", wrapper, state);
    }

    private static Object str(Object key, Scriptable holder, StringifyState state) {
        Object value;
        if (key instanceof String) {
            value = ScriptableObject.getProperty(holder, (String) key);
        } else {
            value = ScriptableObject.getProperty(holder, ((Number) key).intValue());
        }
        if ((value instanceof Scriptable) && (ScriptableObject.getProperty((Scriptable) value, "toJSON") instanceof Callable)) {
            value = ScriptableObject.callMethod(state.cx, (Scriptable) value, "toJSON", new Object[]{key});
        }
        if (state.replacer != null) {
            value = state.replacer.call(state.cx, state.scope, holder, new Object[]{key, value});
        }
        if (value instanceof NativeNumber) {
            value = Double.valueOf(ScriptRuntime.toNumber(value));
        } else if (value instanceof NativeString) {
            value = ScriptRuntime.toString(value);
        } else if (value instanceof NativeBoolean) {
            value = ((NativeBoolean) value).getDefaultValue(ScriptRuntime.BooleanClass);
        }
        if (value == null) {
            return "null";
        }
        if (value.equals(Boolean.TRUE)) {
            return "true";
        }
        if (value.equals(Boolean.FALSE)) {
            return "false";
        }
        if (value instanceof CharSequence) {
            return quote(value.toString());
        }
        if (value instanceof Number) {
            double d = ((Number) value).doubleValue();
            if (d != d || d == Double.POSITIVE_INFINITY || d == Double.NEGATIVE_INFINITY) {
                return "null";
            }
            return ScriptRuntime.toString(value);
        } else if (!(value instanceof Scriptable) || (value instanceof Callable)) {
            return Undefined.instance;
        } else {
            if (value instanceof NativeArray) {
                return ja((NativeArray) value, state);
            }
            return jo((Scriptable) value, state);
        }
    }

    private static String join(Collection<Object> objs, String delimiter) {
        if (objs == null || objs.isEmpty()) {
            return "";
        }
        Iterator<Object> iter = objs.iterator();
        if (!iter.hasNext()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(iter.next().toString());
        while (iter.hasNext()) {
            builder.append(delimiter).append(iter.next().toString());
        }
        return builder.toString();
    }

    private static String jo(Scriptable value, StringifyState state) {
        if (state.stack.search(value) != -1) {
            throw ScriptRuntime.typeError0("msg.cyclic.value");
        }
        String finalValue;
        state.stack.push(value);
        String stepback = state.indent;
        state.indent += state.gap;
        Object[] k;
        if (state.propertyList != null) {
            k = state.propertyList.toArray();
        } else {
            k = value.getIds();
        }
        List<Object> partial = new LinkedList();
        for (Object p : k) {
            Object strP = str(p, value, state);
            if (strP != Undefined.instance) {
                String member = quote(p.toString()) + ":";
                if (state.gap.length() > 0) {
                    member = member + " ";
                }
                partial.add(member + strP);
            }
        }
        if (partial.isEmpty()) {
            finalValue = "{}";
        } else if (state.gap.length() == 0) {
            finalValue = '{' + join(partial, ",") + '}';
        } else {
            finalValue = "{\n" + state.indent + join(partial, ",\n" + state.indent) + 10 + stepback + '}';
        }
        state.stack.pop();
        state.indent = stepback;
        return finalValue;
    }

    private static String ja(NativeArray value, StringifyState state) {
        if (state.stack.search(value) != -1) {
            throw ScriptRuntime.typeError0("msg.cyclic.value");
        }
        String finalValue;
        state.stack.push(value);
        String stepback = state.indent;
        state.indent += state.gap;
        List<Object> partial = new LinkedList();
        long len = value.getLength();
        for (long index = 0; index < len; index++) {
            Object strP;
            if (index > 2147483647L) {
                strP = str(Long.toString(index), value, state);
            } else {
                strP = str(Integer.valueOf((int) index), value, state);
            }
            if (strP == Undefined.instance) {
                partial.add("null");
            } else {
                partial.add(strP);
            }
        }
        if (partial.isEmpty()) {
            finalValue = "[]";
        } else if (state.gap.length() == 0) {
            finalValue = '[' + join(partial, ",") + ']';
        } else {
            finalValue = "[\n" + state.indent + join(partial, ",\n" + state.indent) + 10 + stepback + ']';
        }
        state.stack.pop();
        state.indent = stepback;
        return finalValue;
    }

    private static String quote(String string) {
        StringBuilder product = new StringBuilder(string.length() + 2);
        product.append('\"');
        int length = string.length();
        for (int i = 0; i < length; i++) {
            char c = string.charAt(i);
            switch (c) {
                case 8:
                    product.append("\\b");
                    break;
                case 9:
                    product.append("\\t");
                    break;
                case 10:
                    product.append("\\n");
                    break;
                case 12:
                    product.append("\\f");
                    break;
                case 13:
                    product.append("\\r");
                    break;
                case '\"':
                    product.append("\\\"");
                    break;
                case '\\':
                    product.append("\\\\");
                    break;
                default:
                    if (c >= ' ') {
                        product.append(c);
                        break;
                    }
                    product.append("\\u");
                    product.append(String.format("%04x", new Object[]{Integer.valueOf(c)}));
                    break;
            }
        }
        product.append('\"');
        return product.toString();
    }

    /* access modifiers changed from: protected */
    public int findPrototypeId(String s) {
        int id = 0;
        String X = null;
        switch (s.length()) {
            case 5:
                X = "parse";
                id = 2;
                break;
            case 8:
                X = "toSource";
                id = 1;
                break;
            case 9:
                X = "stringify";
                id = 3;
                break;
        }
        if (X == null || X == s || X.equals(s)) {
            return id;
        }
        return 0;
    }
}
