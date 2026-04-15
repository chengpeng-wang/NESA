package org.mozilla.javascript.xmlimpl;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdFunctionObject;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.Kit;
import org.mozilla.javascript.NativeWith;
import org.mozilla.javascript.Ref;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.xml.XMLObject;

abstract class XMLObjectImpl extends XMLObject {
    private static final int Id_addNamespace = 2;
    private static final int Id_appendChild = 3;
    private static final int Id_attribute = 4;
    private static final int Id_attributes = 5;
    private static final int Id_child = 6;
    private static final int Id_childIndex = 7;
    private static final int Id_children = 8;
    private static final int Id_comments = 9;
    private static final int Id_constructor = 1;
    private static final int Id_contains = 10;
    private static final int Id_copy = 11;
    private static final int Id_descendants = 12;
    private static final int Id_elements = 13;
    private static final int Id_hasComplexContent = 18;
    private static final int Id_hasOwnProperty = 17;
    private static final int Id_hasSimpleContent = 19;
    private static final int Id_inScopeNamespaces = 14;
    private static final int Id_insertChildAfter = 15;
    private static final int Id_insertChildBefore = 16;
    private static final int Id_length = 20;
    private static final int Id_localName = 21;
    private static final int Id_name = 22;
    private static final int Id_namespace = 23;
    private static final int Id_namespaceDeclarations = 24;
    private static final int Id_nodeKind = 25;
    private static final int Id_normalize = 26;
    private static final int Id_parent = 27;
    private static final int Id_prependChild = 28;
    private static final int Id_processingInstructions = 29;
    private static final int Id_propertyIsEnumerable = 30;
    private static final int Id_removeNamespace = 31;
    private static final int Id_replace = 32;
    private static final int Id_setChildren = 33;
    private static final int Id_setLocalName = 34;
    private static final int Id_setName = 35;
    private static final int Id_setNamespace = 36;
    private static final int Id_text = 37;
    private static final int Id_toSource = 39;
    private static final int Id_toString = 38;
    private static final int Id_toXMLString = 40;
    private static final int Id_valueOf = 41;
    private static final int MAX_PROTOTYPE_ID = 41;
    private static final Object XMLOBJECT_TAG = "XMLObject";
    private XMLLibImpl lib;
    private boolean prototypeFlag;

    public abstract void addMatches(XMLList xMLList, XMLName xMLName);

    public abstract XMLList child(int i);

    public abstract XMLList child(XMLName xMLName);

    public abstract XMLList children();

    public abstract XMLList comments();

    public abstract boolean contains(Object obj);

    public abstract XMLObjectImpl copy();

    public abstract void deleteXMLProperty(XMLName xMLName);

    public abstract XMLList elements(XMLName xMLName);

    public abstract boolean equivalentXml(Object obj);

    public abstract XML getXML();

    public abstract Object getXMLProperty(XMLName xMLName);

    public abstract boolean hasComplexContent();

    public abstract boolean hasOwnProperty(XMLName xMLName);

    public abstract boolean hasSimpleContent();

    public abstract boolean hasXMLProperty(XMLName xMLName);

    public abstract Object jsConstructor(Context context, boolean z, Object[] objArr);

    public abstract int length();

    public abstract void normalize();

    public abstract Object parent();

    public abstract XMLList processingInstructions(XMLName xMLName);

    public abstract boolean propertyIsEnumerable(Object obj);

    public abstract void putXMLProperty(XMLName xMLName, Object obj);

    public abstract XMLList text();

    public abstract String toSource(int i);

    public abstract String toString();

    public abstract String toXMLString();

    public abstract Object valueOf();

    protected XMLObjectImpl(XMLLibImpl lib, Scriptable scope, XMLObject prototype) {
        initialize(lib, scope, prototype);
    }

    /* access modifiers changed from: final */
    public final void initialize(XMLLibImpl lib, Scriptable scope, XMLObject prototype) {
        setParentScope(scope);
        setPrototype(prototype);
        this.prototypeFlag = prototype == null;
        this.lib = lib;
    }

    /* access modifiers changed from: final */
    public final boolean isPrototype() {
        return this.prototypeFlag;
    }

    /* access modifiers changed from: 0000 */
    public XMLLibImpl getLib() {
        return this.lib;
    }

    /* access modifiers changed from: final */
    public final XML newXML(XmlNode node) {
        return this.lib.newXML(node);
    }

    /* access modifiers changed from: 0000 */
    public XML xmlFromNode(XmlNode node) {
        if (node.getXml() == null) {
            node.setXml(newXML(node));
        }
        return node.getXml();
    }

    /* access modifiers changed from: final */
    public final XMLList newXMLList() {
        return this.lib.newXMLList();
    }

    /* access modifiers changed from: final */
    public final XMLList newXMLListFrom(Object o) {
        return this.lib.newXMLListFrom(o);
    }

    /* access modifiers changed from: final */
    public final XmlProcessor getProcessor() {
        return this.lib.getProcessor();
    }

    /* access modifiers changed from: final */
    public final QName newQName(String uri, String localName, String prefix) {
        return this.lib.newQName(uri, localName, prefix);
    }

    /* access modifiers changed from: final */
    public final QName newQName(QName name) {
        return this.lib.newQName(name);
    }

    /* access modifiers changed from: final */
    public final Namespace createNamespace(Namespace declaration) {
        if (declaration == null) {
            return null;
        }
        return this.lib.createNamespaces(new Namespace[]{declaration})[0];
    }

    /* access modifiers changed from: final */
    public final Namespace[] createNamespaces(Namespace[] declarations) {
        return this.lib.createNamespaces(declarations);
    }

    public final Scriptable getPrototype() {
        return super.getPrototype();
    }

    public final void setPrototype(Scriptable prototype) {
        super.setPrototype(prototype);
    }

    public final Scriptable getParentScope() {
        return super.getParentScope();
    }

    public final void setParentScope(Scriptable parent) {
        super.setParentScope(parent);
    }

    public final Object getDefaultValue(Class<?> cls) {
        return toString();
    }

    public final boolean hasInstance(Scriptable scriptable) {
        return super.hasInstance(scriptable);
    }

    private XMLList getMatches(XMLName name) {
        XMLList rv = newXMLList();
        addMatches(rv, name);
        return rv;
    }

    /* access modifiers changed from: protected|final */
    public final Object equivalentValues(Object value) {
        return equivalentXml(value) ? Boolean.TRUE : Boolean.FALSE;
    }

    public final boolean has(Context cx, Object id) {
        if (cx == null) {
            cx = Context.getCurrentContext();
        }
        XMLName xmlName = this.lib.toXMLNameOrIndex(cx, id);
        if (xmlName == null) {
            return has((int) ScriptRuntime.lastUint32Result(cx), (Scriptable) this);
        }
        return hasXMLProperty(xmlName);
    }

    public boolean has(String name, Scriptable start) {
        return hasXMLProperty(this.lib.toXMLNameFromString(Context.getCurrentContext(), name));
    }

    public final Object get(Context cx, Object id) {
        if (cx == null) {
            cx = Context.getCurrentContext();
        }
        XMLName xmlName = this.lib.toXMLNameOrIndex(cx, id);
        if (xmlName != null) {
            return getXMLProperty(xmlName);
        }
        Object result = get((int) ScriptRuntime.lastUint32Result(cx), (Scriptable) this);
        if (result == Scriptable.NOT_FOUND) {
            return Undefined.instance;
        }
        return result;
    }

    public Object get(String name, Scriptable start) {
        return getXMLProperty(this.lib.toXMLNameFromString(Context.getCurrentContext(), name));
    }

    public final void put(Context cx, Object id, Object value) {
        if (cx == null) {
            cx = Context.getCurrentContext();
        }
        XMLName xmlName = this.lib.toXMLNameOrIndex(cx, id);
        if (xmlName == null) {
            put((int) ScriptRuntime.lastUint32Result(cx), (Scriptable) this, value);
        } else {
            putXMLProperty(xmlName, value);
        }
    }

    public void put(String name, Scriptable start, Object value) {
        putXMLProperty(this.lib.toXMLNameFromString(Context.getCurrentContext(), name), value);
    }

    public final boolean delete(Context cx, Object id) {
        if (cx == null) {
            cx = Context.getCurrentContext();
        }
        XMLName xmlName = this.lib.toXMLNameOrIndex(cx, id);
        if (xmlName == null) {
            delete((int) ScriptRuntime.lastUint32Result(cx));
        } else {
            deleteXMLProperty(xmlName);
        }
        return true;
    }

    public void delete(String name) {
        deleteXMLProperty(this.lib.toXMLNameFromString(Context.getCurrentContext(), name));
    }

    public Object getFunctionProperty(Context cx, int id) {
        if (isPrototype()) {
            return super.get(id, (Scriptable) this);
        }
        Scriptable proto = getPrototype();
        if (proto instanceof XMLObject) {
            return ((XMLObject) proto).getFunctionProperty(cx, id);
        }
        return NOT_FOUND;
    }

    public Object getFunctionProperty(Context cx, String name) {
        if (isPrototype()) {
            return super.get(name, this);
        }
        Scriptable proto = getPrototype();
        if (proto instanceof XMLObject) {
            return ((XMLObject) proto).getFunctionProperty(cx, name);
        }
        return NOT_FOUND;
    }

    public Ref memberRef(Context cx, Object elem, int memberTypeFlags) {
        boolean attribute;
        boolean descendants = true;
        if ((memberTypeFlags & 2) != 0) {
            attribute = true;
        } else {
            attribute = false;
        }
        if ((memberTypeFlags & 4) == 0) {
            descendants = false;
        }
        if (attribute || descendants) {
            XMLName rv = XMLName.create(this.lib.toNodeQName(cx, elem, attribute), attribute, descendants);
            rv.initXMLObject(this);
            return rv;
        }
        throw Kit.codeBug();
    }

    public Ref memberRef(Context cx, Object namespace, Object elem, int memberTypeFlags) {
        boolean attribute;
        boolean descendants = true;
        if ((memberTypeFlags & 2) != 0) {
            attribute = true;
        } else {
            attribute = false;
        }
        if ((memberTypeFlags & 4) == 0) {
            descendants = false;
        }
        XMLName rv = XMLName.create(this.lib.toNodeQName(cx, namespace, elem), attribute, descendants);
        rv.initXMLObject(this);
        return rv;
    }

    public NativeWith enterWith(Scriptable scope) {
        return new XMLWithScope(this.lib, scope, this);
    }

    public NativeWith enterDotQuery(Scriptable scope) {
        XMLWithScope xws = new XMLWithScope(this.lib, scope, this);
        xws.initAsDotQuery();
        return xws;
    }

    public final Object addValues(Context cx, boolean thisIsLeft, Object value) {
        if (value instanceof XMLObject) {
            XMLObject v1;
            XMLObject v2;
            if (thisIsLeft) {
                v1 = this;
                v2 = (XMLObject) value;
            } else {
                v1 = (XMLObject) value;
                v2 = this;
            }
            return this.lib.addXMLObjects(cx, v1, v2);
        } else if (value == Undefined.instance) {
            return ScriptRuntime.toString((Object) this);
        } else {
            return super.addValues(cx, thisIsLeft, value);
        }
    }

    /* access modifiers changed from: final */
    public final void exportAsJSClass(boolean sealed) {
        this.prototypeFlag = true;
        exportAsJSClass(41, getParentScope(), sealed);
    }

    /* access modifiers changed from: protected */
    public int findPrototypeId(String s) {
        int id = 0;
        String X = null;
        int c;
        switch (s.length()) {
            case 4:
                c = s.charAt(0);
                if (c != 99) {
                    if (c != 110) {
                        if (c == 116) {
                            X = "text";
                            id = 37;
                            break;
                        }
                    }
                    X = "name";
                    id = 22;
                    break;
                }
                X = "copy";
                id = 11;
                break;
                break;
            case 5:
                X = "child";
                id = 6;
                break;
            case 6:
                c = s.charAt(0);
                if (c != 108) {
                    if (c == 112) {
                        X = "parent";
                        id = 27;
                        break;
                    }
                }
                X = "length";
                id = 20;
                break;
                break;
            case 7:
                c = s.charAt(0);
                if (c != 114) {
                    if (c != 115) {
                        if (c == 118) {
                            X = "valueOf";
                            id = 41;
                            break;
                        }
                    }
                    X = "setName";
                    id = 35;
                    break;
                }
                X = "replace";
                id = 32;
                break;
                break;
            case 8:
                switch (s.charAt(2)) {
                    case 'S':
                        c = s.charAt(7);
                        if (c != 101) {
                            if (c == 103) {
                                X = "toString";
                                id = 38;
                                break;
                            }
                        }
                        X = "toSource";
                        id = 39;
                        break;
                        break;
                    case 'd':
                        X = "nodeKind";
                        id = 25;
                        break;
                    case 'e':
                        X = "elements";
                        id = 13;
                        break;
                    case 'i':
                        X = "children";
                        id = 8;
                        break;
                    case 'm':
                        X = "comments";
                        id = 9;
                        break;
                    case 'n':
                        X = "contains";
                        id = 10;
                        break;
                }
                break;
            case 9:
                switch (s.charAt(2)) {
                    case 'c':
                        X = "localName";
                        id = 21;
                        break;
                    case 'm':
                        X = "namespace";
                        id = 23;
                        break;
                    case 'r':
                        X = "normalize";
                        id = 26;
                        break;
                    case 't':
                        X = "attribute";
                        id = 4;
                        break;
                }
                break;
            case 10:
                c = s.charAt(0);
                if (c != 97) {
                    if (c == 99) {
                        X = "childIndex";
                        id = 7;
                        break;
                    }
                }
                X = "attributes";
                id = 5;
                break;
                break;
            case 11:
                switch (s.charAt(0)) {
                    case 'a':
                        X = "appendChild";
                        id = 3;
                        break;
                    case 'c':
                        X = "constructor";
                        id = 1;
                        break;
                    case 'd':
                        X = "descendants";
                        id = 12;
                        break;
                    case 's':
                        X = "setChildren";
                        id = 33;
                        break;
                    case 't':
                        X = "toXMLString";
                        id = 40;
                        break;
                }
                break;
            case 12:
                c = s.charAt(0);
                if (c != 97) {
                    if (c != 112) {
                        if (c == 115) {
                            c = s.charAt(3);
                            if (c != 76) {
                                if (c == 78) {
                                    X = "setNamespace";
                                    id = 36;
                                    break;
                                }
                            }
                            X = "setLocalName";
                            id = 34;
                            break;
                        }
                    }
                    X = "prependChild";
                    id = 28;
                    break;
                }
                X = "addNamespace";
                id = 2;
                break;
                break;
            case 14:
                X = "hasOwnProperty";
                id = 17;
                break;
            case 15:
                X = "removeNamespace";
                id = 31;
                break;
            case 16:
                c = s.charAt(0);
                if (c != 104) {
                    if (c == 105) {
                        X = "insertChildAfter";
                        id = 15;
                        break;
                    }
                }
                X = "hasSimpleContent";
                id = 19;
                break;
                break;
            case 17:
                c = s.charAt(3);
                if (c != 67) {
                    if (c != 99) {
                        if (c == 101) {
                            X = "insertChildBefore";
                            id = 16;
                            break;
                        }
                    }
                    X = "inScopeNamespaces";
                    id = 14;
                    break;
                }
                X = "hasComplexContent";
                id = 18;
                break;
                break;
            case 20:
                X = "propertyIsEnumerable";
                id = 30;
                break;
            case 21:
                X = "namespaceDeclarations";
                id = 24;
                break;
            case 22:
                X = "processingInstructions";
                id = 29;
                break;
        }
        if (X == null || X == s || X.equals(s)) {
            return id;
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void initPrototypeId(int id) {
        int arity;
        String s;
        switch (id) {
            case 1:
                IdFunctionObject ctor;
                if (this instanceof XML) {
                    ctor = new XMLCtor((XML) this, XMLOBJECT_TAG, id, 1);
                } else {
                    ctor = new IdFunctionObject(this, XMLOBJECT_TAG, id, 1);
                }
                initPrototypeConstructor(ctor);
                return;
            case 2:
                arity = 1;
                s = "addNamespace";
                break;
            case 3:
                arity = 1;
                s = "appendChild";
                break;
            case 4:
                arity = 1;
                s = "attribute";
                break;
            case 5:
                arity = 0;
                s = "attributes";
                break;
            case 6:
                arity = 1;
                s = "child";
                break;
            case 7:
                arity = 0;
                s = "childIndex";
                break;
            case 8:
                arity = 0;
                s = "children";
                break;
            case 9:
                arity = 0;
                s = "comments";
                break;
            case 10:
                arity = 1;
                s = "contains";
                break;
            case 11:
                arity = 0;
                s = "copy";
                break;
            case 12:
                arity = 1;
                s = "descendants";
                break;
            case 13:
                arity = 1;
                s = "elements";
                break;
            case 14:
                arity = 0;
                s = "inScopeNamespaces";
                break;
            case 15:
                arity = 2;
                s = "insertChildAfter";
                break;
            case 16:
                arity = 2;
                s = "insertChildBefore";
                break;
            case 17:
                arity = 1;
                s = "hasOwnProperty";
                break;
            case 18:
                arity = 0;
                s = "hasComplexContent";
                break;
            case 19:
                arity = 0;
                s = "hasSimpleContent";
                break;
            case 20:
                arity = 0;
                s = "length";
                break;
            case 21:
                arity = 0;
                s = "localName";
                break;
            case 22:
                arity = 0;
                s = "name";
                break;
            case 23:
                arity = 1;
                s = "namespace";
                break;
            case 24:
                arity = 0;
                s = "namespaceDeclarations";
                break;
            case 25:
                arity = 0;
                s = "nodeKind";
                break;
            case 26:
                arity = 0;
                s = "normalize";
                break;
            case 27:
                arity = 0;
                s = "parent";
                break;
            case 28:
                arity = 1;
                s = "prependChild";
                break;
            case 29:
                arity = 1;
                s = "processingInstructions";
                break;
            case 30:
                arity = 1;
                s = "propertyIsEnumerable";
                break;
            case 31:
                arity = 1;
                s = "removeNamespace";
                break;
            case 32:
                arity = 2;
                s = "replace";
                break;
            case 33:
                arity = 1;
                s = "setChildren";
                break;
            case 34:
                arity = 1;
                s = "setLocalName";
                break;
            case 35:
                arity = 1;
                s = "setName";
                break;
            case 36:
                arity = 1;
                s = "setNamespace";
                break;
            case 37:
                arity = 0;
                s = "text";
                break;
            case 38:
                arity = 0;
                s = "toString";
                break;
            case 39:
                arity = 1;
                s = "toSource";
                break;
            case 40:
                arity = 1;
                s = "toXMLString";
                break;
            case 41:
                arity = 0;
                s = "valueOf";
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        initPrototypeMethod(XMLOBJECT_TAG, id, s, arity);
    }

    private Object[] toObjectArray(Object[] typed) {
        Object[] rv = new Object[typed.length];
        for (int i = 0; i < rv.length; i++) {
            rv[i] = typed[i];
        }
        return rv;
    }

    private void xmlMethodNotFound(Object object, String name) {
        throw ScriptRuntime.notFunctionError(object, name);
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(XMLOBJECT_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        }
        int id = f.methodId();
        if (id == 1) {
            return jsConstructor(cx, thisObj == null, args);
        } else if (thisObj instanceof XMLObjectImpl) {
            XMLObjectImpl realThis = (XMLObjectImpl) thisObj;
            XML xml = realThis.getXML();
            XMLName xmlName;
            Object arg0;
            switch (id) {
                case 2:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "addNamespace");
                    }
                    return xml.addNamespace(this.lib.castToNamespace(cx, arg(args, 0)));
                case 3:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "appendChild");
                    }
                    return xml.appendChild(arg(args, 0));
                case 4:
                    return realThis.getMatches(XMLName.create(this.lib.toNodeQName(cx, arg(args, 0), true), true, false));
                case 5:
                    return realThis.getMatches(XMLName.create(QName.create(null, null), true, false));
                case 6:
                    xmlName = this.lib.toXMLNameOrIndex(cx, arg(args, 0));
                    if (xmlName == null) {
                        return realThis.child((int) ScriptRuntime.lastUint32Result(cx));
                    }
                    return realThis.child(xmlName);
                case 7:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "childIndex");
                    }
                    return ScriptRuntime.wrapInt(xml.childIndex());
                case 8:
                    return realThis.children();
                case 9:
                    return realThis.comments();
                case 10:
                    return ScriptRuntime.wrapBoolean(realThis.contains(arg(args, 0)));
                case 11:
                    return realThis.copy();
                case 12:
                    return realThis.getMatches(XMLName.create(args.length == 0 ? QName.create(null, null) : this.lib.toNodeQName(cx, args[0], false), false, true));
                case 13:
                    if (args.length == 0) {
                        xmlName = XMLName.formStar();
                    } else {
                        xmlName = this.lib.toXMLName(cx, args[0]);
                    }
                    return realThis.elements(xmlName);
                case 14:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "inScopeNamespaces");
                    }
                    return cx.newArray(scope, toObjectArray(xml.inScopeNamespaces()));
                case 15:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "insertChildAfter");
                    }
                    arg0 = arg(args, 0);
                    if (arg0 != null && !(arg0 instanceof XML)) {
                        return Undefined.instance;
                    }
                    return xml.insertChildAfter((XML) arg0, arg(args, 1));
                case 16:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "insertChildBefore");
                    }
                    arg0 = arg(args, 0);
                    if (arg0 != null && !(arg0 instanceof XML)) {
                        return Undefined.instance;
                    }
                    return xml.insertChildBefore((XML) arg0, arg(args, 1));
                case 17:
                    return ScriptRuntime.wrapBoolean(realThis.hasOwnProperty(this.lib.toXMLName(cx, arg(args, 0))));
                case 18:
                    return ScriptRuntime.wrapBoolean(realThis.hasComplexContent());
                case 19:
                    return ScriptRuntime.wrapBoolean(realThis.hasSimpleContent());
                case 20:
                    return ScriptRuntime.wrapInt(realThis.length());
                case 21:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "localName");
                    }
                    return xml.localName();
                case 22:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "name");
                    }
                    return xml.name();
                case 23:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "namespace");
                    }
                    Object rv = xml.namespace(args.length > 0 ? ScriptRuntime.toString(args[0]) : null);
                    if (rv == null) {
                        return Undefined.instance;
                    }
                    return rv;
                case 24:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "namespaceDeclarations");
                    }
                    return cx.newArray(scope, toObjectArray(xml.namespaceDeclarations()));
                case 25:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "nodeKind");
                    }
                    return xml.nodeKind();
                case 26:
                    realThis.normalize();
                    return Undefined.instance;
                case 27:
                    return realThis.parent();
                case 28:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "prependChild");
                    }
                    return xml.prependChild(arg(args, 0));
                case 29:
                    if (args.length > 0) {
                        xmlName = this.lib.toXMLName(cx, args[0]);
                    } else {
                        xmlName = XMLName.formStar();
                    }
                    return realThis.processingInstructions(xmlName);
                case 30:
                    return ScriptRuntime.wrapBoolean(realThis.propertyIsEnumerable(arg(args, 0)));
                case 31:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "removeNamespace");
                    }
                    return xml.removeNamespace(this.lib.castToNamespace(cx, arg(args, 0)));
                case 32:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "replace");
                    }
                    xmlName = this.lib.toXMLNameOrIndex(cx, arg(args, 0));
                    Object arg1 = arg(args, 1);
                    if (xmlName != null) {
                        return xml.replace(xmlName, arg1);
                    }
                    return xml.replace((int) ScriptRuntime.lastUint32Result(cx), arg1);
                case 33:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "setChildren");
                    }
                    return xml.setChildren(arg(args, 0));
                case 34:
                    String localName;
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "setLocalName");
                    }
                    Object arg = arg(args, 0);
                    if (arg instanceof QName) {
                        localName = ((QName) arg).localName();
                    } else {
                        localName = ScriptRuntime.toString(arg);
                    }
                    xml.setLocalName(localName);
                    return Undefined.instance;
                case 35:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "setName");
                    }
                    xml.setName(this.lib.constructQName(cx, args.length != 0 ? args[0] : Undefined.instance));
                    return Undefined.instance;
                case 36:
                    if (xml == null) {
                        xmlMethodNotFound(realThis, "setNamespace");
                    }
                    xml.setNamespace(this.lib.castToNamespace(cx, arg(args, 0)));
                    return Undefined.instance;
                case 37:
                    return realThis.text();
                case 38:
                    return realThis.toString();
                case 39:
                    return realThis.toSource(ScriptRuntime.toInt32(args, 0));
                case 40:
                    return realThis.toXMLString();
                case 41:
                    return realThis.valueOf();
                default:
                    throw new IllegalArgumentException(String.valueOf(id));
            }
        } else {
            throw IdScriptableObject.incompatibleCallError(f);
        }
    }

    private static Object arg(Object[] args, int i) {
        return i < args.length ? args[i] : Undefined.instance;
    }

    /* access modifiers changed from: final */
    public final XML newTextElementXML(XmlNode reference, QName qname, String value) {
        return this.lib.newTextElementXML(reference, qname, value);
    }

    /* access modifiers changed from: final */
    public final XML newXMLFromJs(Object inputObject) {
        return this.lib.newXMLFromJs(inputObject);
    }

    /* access modifiers changed from: final */
    public final XML ecmaToXml(Object object) {
        return this.lib.ecmaToXml(object);
    }

    /* access modifiers changed from: final */
    public final String ecmaEscapeAttributeValue(String s) {
        String quoted = this.lib.escapeAttributeValue(s);
        return quoted.substring(1, quoted.length() - 1);
    }

    /* access modifiers changed from: final */
    public final XML createEmptyXML() {
        return newXML(XmlNode.createEmpty(getProcessor()));
    }
}
