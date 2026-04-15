package org.mozilla.javascript;

public class NativeJavaTopPackage extends NativeJavaPackage implements Function, IdFunctionCall {
    private static final Object FTAG = "JavaTopPackage";
    private static final int Id_getClass = 1;
    private static final String[][] commonPackages;
    static final long serialVersionUID = -1455787259477709999L;

    static {
        r0 = new String[8][];
        r0[0] = new String[]{"java", "lang", "reflect"};
        r0[1] = new String[]{"java", "io"};
        r0[2] = new String[]{"java", "math"};
        r0[3] = new String[]{"java", "net"};
        r0[4] = new String[]{"java", "util", "zip"};
        r0[5] = new String[]{"java", "text", "resources"};
        r0[6] = new String[]{"java", "applet"};
        r0[7] = new String[]{"javax", "swing"};
        commonPackages = r0;
    }

    NativeJavaTopPackage(ClassLoader loader) {
        super(true, "", loader);
    }

    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return construct(cx, scope, args);
    }

    public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
        ClassLoader loader = null;
        if (args.length != 0) {
            ClassLoader arg = args[0];
            if (arg instanceof Wrapper) {
                arg = ((Wrapper) arg).unwrap();
            }
            if (arg instanceof ClassLoader) {
                loader = arg;
            }
        }
        if (loader == null) {
            Context.reportRuntimeError0("msg.not.classloader");
            return null;
        }
        Scriptable pkg = new NativeJavaPackage(true, "", loader);
        ScriptRuntime.setObjectProtoAndParent(pkg, scope);
        return pkg;
    }

    public static void init(Context cx, Scriptable scope, boolean sealed) {
        int i;
        Object top = new NativeJavaTopPackage(cx.getApplicationClassLoader());
        top.setPrototype(ScriptableObject.getObjectPrototype(scope));
        top.setParentScope(scope);
        for (i = 0; i != commonPackages.length; i++) {
            NativeJavaPackage parent = top;
            for (int j = 0; j != commonPackages[i].length; j++) {
                parent = parent.forcePackage(commonPackages[i][j], scope);
            }
        }
        IdFunctionObject getClass = new IdFunctionObject(top, FTAG, 1, "getClass", 1, scope);
        String[] topNames = ScriptRuntime.getTopPackageNames();
        NativeJavaPackage[] topPackages = new NativeJavaPackage[topNames.length];
        for (i = 0; i < topNames.length; i++) {
            topPackages[i] = (NativeJavaPackage) top.get(topNames[i], (Scriptable) top);
        }
        ScriptableObject global = (ScriptableObject) scope;
        if (sealed) {
            getClass.sealObject();
        }
        getClass.exportAsScopeProperty();
        global.defineProperty("Packages", top, 2);
        for (i = 0; i < topNames.length; i++) {
            global.defineProperty(topNames[i], topPackages[i], 2);
        }
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (f.hasTag(FTAG) && f.methodId() == 1) {
            return js_getClass(cx, scope, args);
        }
        throw f.unknown();
    }

    private Scriptable js_getClass(Context cx, Scriptable scope, Object[] args) {
        if (args.length > 0 && (args[0] instanceof Wrapper)) {
            Scriptable result = this;
            String name = ((Wrapper) args[0]).unwrap().getClass().getName();
            int offset = 0;
            while (true) {
                String propName;
                int index = name.indexOf(46, offset);
                if (index == -1) {
                    propName = name.substring(offset);
                } else {
                    propName = name.substring(offset, index);
                }
                Scriptable prop = result.get(propName, result);
                if (!(prop instanceof Scriptable)) {
                    break;
                }
                result = prop;
                if (index == -1) {
                    return result;
                }
                offset = index + 1;
            }
        }
        throw Context.reportRuntimeError0("msg.not.java.obj");
    }
}
