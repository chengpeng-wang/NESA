package org.mozilla.javascript;

public final class NativeGenerator extends IdScriptableObject {
    public static final int GENERATOR_CLOSE = 2;
    public static final int GENERATOR_SEND = 0;
    private static final Object GENERATOR_TAG = "Generator";
    public static final int GENERATOR_THROW = 1;
    private static final int Id___iterator__ = 5;
    private static final int Id_close = 1;
    private static final int Id_next = 2;
    private static final int Id_send = 3;
    private static final int Id_throw = 4;
    private static final int MAX_PROTOTYPE_ID = 5;
    private static final long serialVersionUID = 1645892441041347273L;
    private boolean firstTime = true;
    private NativeFunction function;
    private int lineNumber;
    private String lineSource;
    private boolean locked;
    private Object savedState;

    public static class GeneratorClosedException extends RuntimeException {
        private static final long serialVersionUID = 2561315658662379681L;
    }

    private static class CloseGeneratorAction implements ContextAction {
        private NativeGenerator generator;

        CloseGeneratorAction(NativeGenerator generator) {
            this.generator = generator;
        }

        public Object run(Context cx) {
            return ScriptRuntime.doTopCall(new Callable() {
                public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    return ((NativeGenerator) thisObj).resume(cx, scope, 2, new GeneratorClosedException());
                }
            }, cx, ScriptableObject.getTopLevelScope(this.generator), this.generator, null);
        }
    }

    static NativeGenerator init(ScriptableObject scope, boolean sealed) {
        NativeGenerator prototype = new NativeGenerator();
        if (scope != null) {
            prototype.setParentScope(scope);
            prototype.setPrototype(ScriptableObject.getObjectPrototype(scope));
        }
        prototype.activatePrototypeMap(5);
        if (sealed) {
            prototype.sealObject();
        }
        if (scope != null) {
            scope.associateValue(GENERATOR_TAG, prototype);
        }
        return prototype;
    }

    private NativeGenerator() {
    }

    public NativeGenerator(Scriptable scope, NativeFunction function, Object savedState) {
        this.function = function;
        this.savedState = savedState;
        Scriptable top = ScriptableObject.getTopLevelScope(scope);
        setParentScope(top);
        setPrototype((NativeGenerator) ScriptableObject.getTopScopeValue(top, GENERATOR_TAG));
    }

    public String getClassName() {
        return "Generator";
    }

    /* access modifiers changed from: protected */
    public void initPrototypeId(int id) {
        int arity;
        String s;
        switch (id) {
            case 1:
                arity = 1;
                s = "close";
                break;
            case 2:
                arity = 1;
                s = "next";
                break;
            case 3:
                arity = 0;
                s = "send";
                break;
            case 4:
                arity = 0;
                s = "throw";
                break;
            case 5:
                arity = 1;
                s = NativeIterator.ITERATOR_PROPERTY_NAME;
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        initPrototypeMethod(GENERATOR_TAG, id, s, arity);
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(GENERATOR_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        }
        int id = f.methodId();
        if (thisObj instanceof NativeGenerator) {
            NativeGenerator generator = (NativeGenerator) thisObj;
            switch (id) {
                case 1:
                    return generator.resume(cx, scope, 2, new GeneratorClosedException());
                case 2:
                    generator.firstTime = false;
                    return generator.resume(cx, scope, 0, Undefined.instance);
                case 3:
                    Object arg = args.length > 0 ? args[0] : Undefined.instance;
                    if (!generator.firstTime || arg.equals(Undefined.instance)) {
                        return generator.resume(cx, scope, 0, arg);
                    }
                    throw ScriptRuntime.typeError0("msg.send.newborn");
                case 4:
                    return generator.resume(cx, scope, 1, args.length > 0 ? args[0] : Undefined.instance);
                case 5:
                    return thisObj;
                default:
                    throw new IllegalArgumentException(String.valueOf(id));
            }
        }
        throw IdScriptableObject.incompatibleCallError(f);
    }

    /* access modifiers changed from: private */
    public Object resume(Context cx, Scriptable scope, int operation, Object value) {
        if (this.savedState != null) {
            Object resumeGenerator;
            try {
                synchronized (this) {
                    if (this.locked) {
                        throw ScriptRuntime.typeError0("msg.already.exec.gen");
                    }
                    this.locked = true;
                }
                resumeGenerator = this.function.resumeGenerator(cx, scope, operation, this.savedState, value);
                synchronized (this) {
                    this.locked = false;
                }
                if (operation != 2) {
                    return resumeGenerator;
                }
                this.savedState = null;
                return resumeGenerator;
            } catch (GeneratorClosedException e) {
                try {
                    resumeGenerator = Undefined.instance;
                    synchronized (this) {
                        this.locked = false;
                        if (operation != 2) {
                            return resumeGenerator;
                        }
                        this.savedState = null;
                        return resumeGenerator;
                    }
                } catch (Throwable th) {
                    synchronized (this) {
                        this.locked = false;
                        if (operation == 2) {
                            this.savedState = null;
                        }
                    }
                }
            } catch (RhinoException e2) {
                this.lineNumber = e2.lineNumber();
                this.lineSource = e2.lineSource();
                this.savedState = null;
                throw e2;
            }
        } else if (operation == 2) {
            return Undefined.instance;
        } else {
            Object thrown;
            if (operation == 1) {
                thrown = value;
            } else {
                thrown = NativeIterator.getStopIterationObject(scope);
            }
            throw new JavaScriptException(thrown, this.lineSource, this.lineNumber);
        }
    }

    /* access modifiers changed from: protected */
    public int findPrototypeId(String s) {
        int id = 0;
        String X = null;
        int s_length = s.length();
        int c;
        if (s_length == 4) {
            c = s.charAt(0);
            if (c == 110) {
                X = "next";
                id = 2;
            } else if (c == 115) {
                X = "send";
                id = 3;
            }
        } else if (s_length == 5) {
            c = s.charAt(0);
            if (c == 99) {
                X = "close";
                id = 1;
            } else if (c == 116) {
                X = "throw";
                id = 4;
            }
        } else if (s_length == 12) {
            X = NativeIterator.ITERATOR_PROPERTY_NAME;
            id = 5;
        }
        if (X == null || X == s || X.equals(s)) {
            return id;
        }
        return 0;
    }
}
