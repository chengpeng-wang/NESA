package org.mozilla.javascript.xmlimpl;

import java.io.Serializable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Kit;
import org.mozilla.javascript.Ref;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.Wrapper;
import org.mozilla.javascript.xml.XMLLib;
import org.mozilla.javascript.xml.XMLObject;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public final class XMLLibImpl extends XMLLib implements Serializable {
    private static final long serialVersionUID = 1;
    private Scriptable globalScope;
    private Namespace namespacePrototype;
    private XmlProcessor options = new XmlProcessor();
    private QName qnamePrototype;
    private XMLList xmlListPrototype;
    private XML xmlPrototype;

    public static Node toDomNode(Object xmlObject) {
        if (xmlObject instanceof XML) {
            return ((XML) xmlObject).toDomNode();
        }
        throw new IllegalArgumentException("xmlObject is not an XML object in JavaScript.");
    }

    public static void init(Context cx, Scriptable scope, boolean sealed) {
        XMLLib lib = new XMLLibImpl(scope);
        if (lib.bindToScope(scope) == lib) {
            lib.exportToScope(sealed);
        }
    }

    public void setIgnoreComments(boolean b) {
        this.options.setIgnoreComments(b);
    }

    public void setIgnoreWhitespace(boolean b) {
        this.options.setIgnoreWhitespace(b);
    }

    public void setIgnoreProcessingInstructions(boolean b) {
        this.options.setIgnoreProcessingInstructions(b);
    }

    public void setPrettyPrinting(boolean b) {
        this.options.setPrettyPrinting(b);
    }

    public void setPrettyIndent(int i) {
        this.options.setPrettyIndent(i);
    }

    public boolean isIgnoreComments() {
        return this.options.isIgnoreComments();
    }

    public boolean isIgnoreProcessingInstructions() {
        return this.options.isIgnoreProcessingInstructions();
    }

    public boolean isIgnoreWhitespace() {
        return this.options.isIgnoreWhitespace();
    }

    public boolean isPrettyPrinting() {
        return this.options.isPrettyPrinting();
    }

    public int getPrettyIndent() {
        return this.options.getPrettyIndent();
    }

    private XMLLibImpl(Scriptable globalScope) {
        this.globalScope = globalScope;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    public QName qnamePrototype() {
        return this.qnamePrototype;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    public Scriptable globalScope() {
        return this.globalScope;
    }

    /* access modifiers changed from: 0000 */
    public XmlProcessor getProcessor() {
        return this.options;
    }

    private void exportToScope(boolean sealed) {
        this.xmlPrototype = newXML(XmlNode.createText(this.options, ""));
        this.xmlListPrototype = newXMLList();
        this.namespacePrototype = Namespace.create(this.globalScope, null, Namespace.GLOBAL);
        this.qnamePrototype = QName.create(this, this.globalScope, null, QName.create(Namespace.create(""), ""));
        this.xmlPrototype.exportAsJSClass(sealed);
        this.xmlListPrototype.exportAsJSClass(sealed);
        this.namespacePrototype.exportAsJSClass(sealed);
        this.qnamePrototype.exportAsJSClass(sealed);
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    public XMLName toAttributeName(Context cx, Object nameValue) {
        if (nameValue instanceof XMLName) {
            return (XMLName) nameValue;
        }
        if (nameValue instanceof QName) {
            return XMLName.create(((QName) nameValue).getDelegate(), true, false);
        }
        if ((nameValue instanceof Boolean) || (nameValue instanceof Number) || nameValue == Undefined.instance || nameValue == null) {
            throw badXMLName(nameValue);
        }
        String localName;
        if (nameValue instanceof String) {
            localName = (String) nameValue;
        } else {
            localName = ScriptRuntime.toString(nameValue);
        }
        if (localName != null && localName.equals("*")) {
            localName = null;
        }
        return XMLName.create(QName.create(Namespace.create(""), localName), true, false);
    }

    private static RuntimeException badXMLName(Object value) {
        String msg;
        if (value instanceof Number) {
            msg = "Can not construct XML name from number: ";
        } else if (value instanceof Boolean) {
            msg = "Can not construct XML name from boolean: ";
        } else if (value == Undefined.instance || value == null) {
            msg = "Can not construct XML name from ";
        } else {
            throw new IllegalArgumentException(value.toString());
        }
        return ScriptRuntime.typeError(msg + ScriptRuntime.toString(value));
    }

    /* access modifiers changed from: 0000 */
    public XMLName toXMLNameFromString(Context cx, String name) {
        return XMLName.create(getDefaultNamespaceURI(cx), name);
    }

    /* access modifiers changed from: 0000 */
    public XMLName toXMLName(Context cx, Object nameValue) {
        if (nameValue instanceof XMLName) {
            return (XMLName) nameValue;
        }
        if (nameValue instanceof QName) {
            QName qname = (QName) nameValue;
            return XMLName.formProperty(qname.uri(), qname.localName());
        } else if (nameValue instanceof String) {
            return toXMLNameFromString(cx, (String) nameValue);
        } else {
            if (!(nameValue instanceof Boolean) && !(nameValue instanceof Number) && nameValue != Undefined.instance && nameValue != null) {
                return toXMLNameFromString(cx, ScriptRuntime.toString(nameValue));
            }
            throw badXMLName(nameValue);
        }
    }

    /* access modifiers changed from: 0000 */
    public XMLName toXMLNameOrIndex(Context cx, Object value) {
        if (value instanceof XMLName) {
            return (XMLName) value;
        }
        String str;
        long test;
        if (value instanceof String) {
            str = (String) value;
            test = ScriptRuntime.testUint32String(str);
            if (test < 0) {
                return toXMLNameFromString(cx, str);
            }
            ScriptRuntime.storeUint32Result(cx, test);
            return null;
        } else if (value instanceof Number) {
            double d = ((Number) value).doubleValue();
            long l = (long) d;
            if (((double) l) != d || 0 > l || l > 4294967295L) {
                throw badXMLName(value);
            }
            ScriptRuntime.storeUint32Result(cx, l);
            return null;
        } else if (value instanceof QName) {
            QName qname = (QName) value;
            String uri = qname.uri();
            boolean number = false;
            if (uri != null && uri.length() == 0) {
                test = ScriptRuntime.testUint32String(uri);
                if (test >= 0) {
                    ScriptRuntime.storeUint32Result(cx, test);
                    number = true;
                }
            }
            if (number) {
                return null;
            }
            return XMLName.formProperty(uri, qname.localName());
        } else if ((value instanceof Boolean) || value == Undefined.instance || value == null) {
            throw badXMLName(value);
        } else {
            str = ScriptRuntime.toString(value);
            test = ScriptRuntime.testUint32String(str);
            if (test < 0) {
                return toXMLNameFromString(cx, str);
            }
            ScriptRuntime.storeUint32Result(cx, test);
            return null;
        }
    }

    /* access modifiers changed from: 0000 */
    public Object addXMLObjects(Context cx, XMLObject obj1, XMLObject obj2) {
        XMLList listToAdd = newXMLList();
        if (obj1 instanceof XMLList) {
            XMLList list1 = (XMLList) obj1;
            if (list1.length() == 1) {
                listToAdd.addToList(list1.item(0));
            } else {
                listToAdd = newXMLListFrom(obj1);
            }
        } else {
            listToAdd.addToList(obj1);
        }
        if (obj2 instanceof XMLList) {
            XMLList list2 = (XMLList) obj2;
            for (int i = 0; i < list2.length(); i++) {
                listToAdd.addToList(list2.item(i));
            }
        } else if (obj2 instanceof XML) {
            listToAdd.addToList(obj2);
        }
        return listToAdd;
    }

    private Ref xmlPrimaryReference(Context cx, XMLName xmlName, Scriptable scope) {
        XMLObjectImpl xmlObj;
        XMLObjectImpl firstXml = null;
        do {
            if (scope instanceof XMLWithScope) {
                xmlObj = (XMLObjectImpl) scope.getPrototype();
                if (xmlObj.hasXMLProperty(xmlName)) {
                    break;
                } else if (firstXml == null) {
                    firstXml = xmlObj;
                }
            }
            scope = scope.getParentScope();
        } while (scope != null);
        xmlObj = firstXml;
        if (xmlObj != null) {
            xmlName.initXMLObject(xmlObj);
        }
        return xmlName;
    }

    /* access modifiers changed from: 0000 */
    public Namespace castToNamespace(Context cx, Object namespaceObj) {
        return this.namespacePrototype.castToNamespace(namespaceObj);
    }

    private String getDefaultNamespaceURI(Context cx) {
        return getDefaultNamespace(cx).uri();
    }

    /* access modifiers changed from: 0000 */
    public Namespace newNamespace(String uri) {
        return this.namespacePrototype.newNamespace(uri);
    }

    /* access modifiers changed from: 0000 */
    public Namespace getDefaultNamespace(Context cx) {
        if (cx == null) {
            cx = Context.getCurrentContext();
            if (cx == null) {
                return this.namespacePrototype;
            }
        }
        Object ns = ScriptRuntime.searchDefaultNamespace(cx);
        if (ns == null) {
            return this.namespacePrototype;
        }
        if (ns instanceof Namespace) {
            return (Namespace) ns;
        }
        return this.namespacePrototype;
    }

    /* access modifiers changed from: 0000 */
    public Namespace[] createNamespaces(Namespace[] declarations) {
        Namespace[] rv = new Namespace[declarations.length];
        for (int i = 0; i < declarations.length; i++) {
            rv[i] = this.namespacePrototype.newNamespace(declarations[i].getPrefix(), declarations[i].getUri());
        }
        return rv;
    }

    /* access modifiers changed from: 0000 */
    public QName constructQName(Context cx, Object namespace, Object name) {
        return this.qnamePrototype.constructQName(this, cx, namespace, name);
    }

    /* access modifiers changed from: 0000 */
    public QName newQName(String uri, String localName, String prefix) {
        return this.qnamePrototype.newQName(this, uri, localName, prefix);
    }

    /* access modifiers changed from: 0000 */
    public QName constructQName(Context cx, Object nameValue) {
        return this.qnamePrototype.constructQName(this, cx, nameValue);
    }

    /* access modifiers changed from: 0000 */
    public QName castToQName(Context cx, Object qnameValue) {
        return this.qnamePrototype.castToQName(this, cx, qnameValue);
    }

    /* access modifiers changed from: 0000 */
    public QName newQName(QName qname) {
        return QName.create(this, this.globalScope, this.qnamePrototype, qname);
    }

    /* access modifiers changed from: 0000 */
    public XML newXML(XmlNode node) {
        return new XML(this, this.globalScope, this.xmlPrototype, node);
    }

    /* access modifiers changed from: final */
    public final XML newXMLFromJs(Object inputObject) {
        String frag;
        if (inputObject == null || inputObject == Undefined.instance) {
            frag = "";
        } else if (inputObject instanceof XMLObjectImpl) {
            frag = ((XMLObjectImpl) inputObject).toXMLString();
        } else {
            frag = ScriptRuntime.toString(inputObject);
        }
        if (frag.trim().startsWith("<>")) {
            throw ScriptRuntime.typeError("Invalid use of XML object anonymous tags <></>.");
        } else if (frag.indexOf("<") == -1) {
            return newXML(XmlNode.createText(this.options, frag));
        } else {
            return parse(frag);
        }
    }

    private XML parse(String frag) {
        try {
            return newXML(XmlNode.createElement(this.options, getDefaultNamespaceURI(Context.getCurrentContext()), frag));
        } catch (SAXException e) {
            throw ScriptRuntime.typeError("Cannot parse XML: " + e.getMessage());
        }
    }

    /* access modifiers changed from: final */
    public final XML ecmaToXml(Object object) {
        if (object == null || object == Undefined.instance) {
            throw ScriptRuntime.typeError("Cannot convert " + object + " to XML");
        } else if (object instanceof XML) {
            return (XML) object;
        } else {
            if (object instanceof XMLList) {
                XMLList list = (XMLList) object;
                if (list.getXML() != null) {
                    return list.getXML();
                }
                throw ScriptRuntime.typeError("Cannot convert list of >1 element to XML");
            }
            if (object instanceof Wrapper) {
                object = ((Wrapper) object).unwrap();
            }
            if (object instanceof Node) {
                return newXML(XmlNode.createElementFromNode((Node) object));
            }
            String s = ScriptRuntime.toString(object);
            if (s.length() <= 0 || s.charAt(0) != '<') {
                return newXML(XmlNode.createText(this.options, s));
            }
            return parse(s);
        }
    }

    /* access modifiers changed from: final */
    public final XML newTextElementXML(XmlNode reference, QName qname, String value) {
        return newXML(XmlNode.newElementWithText(this.options, reference, qname, value));
    }

    /* access modifiers changed from: 0000 */
    public XMLList newXMLList() {
        return new XMLList(this, this.globalScope, this.xmlListPrototype);
    }

    /* access modifiers changed from: final */
    public final XMLList newXMLListFrom(Object inputObject) {
        XMLList rv = newXMLList();
        if (!(inputObject == null || (inputObject instanceof Undefined))) {
            if (inputObject instanceof XML) {
                rv.getNodeList().add((XML) inputObject);
            } else if (inputObject instanceof XMLList) {
                rv.getNodeList().add(((XMLList) inputObject).getNodeList());
            } else {
                String frag = ScriptRuntime.toString(inputObject).trim();
                if (!frag.startsWith("<>")) {
                    frag = "<>" + frag + "</>";
                }
                frag = "<fragment>" + frag.substring(2);
                if (frag.endsWith("</>")) {
                    XMLList children = newXMLFromJs(frag.substring(0, frag.length() - 3) + "</fragment>").children();
                    for (int i = 0; i < children.getNodeList().length(); i++) {
                        rv.getNodeList().add((XML) children.item(i).copy());
                    }
                } else {
                    throw ScriptRuntime.typeError("XML with anonymous tag missing end anonymous tag");
                }
            }
        }
        return rv;
    }

    /* access modifiers changed from: 0000 */
    public QName toNodeQName(Context cx, Object namespaceValue, Object nameValue) {
        String localName;
        Namespace ns;
        if (nameValue instanceof QName) {
            localName = ((QName) nameValue).localName();
        } else {
            localName = ScriptRuntime.toString(nameValue);
        }
        if (namespaceValue == Undefined.instance) {
            if ("*".equals(localName)) {
                ns = null;
            } else {
                ns = getDefaultNamespace(cx).getDelegate();
            }
        } else if (namespaceValue == null) {
            ns = null;
        } else if (namespaceValue instanceof Namespace) {
            ns = ((Namespace) namespaceValue).getDelegate();
        } else {
            ns = this.namespacePrototype.constructNamespace(namespaceValue).getDelegate();
        }
        if (localName != null && localName.equals("*")) {
            localName = null;
        }
        return QName.create(ns, localName);
    }

    /* access modifiers changed from: 0000 */
    public QName toNodeQName(Context cx, String name, boolean attribute) {
        Namespace defaultNamespace = getDefaultNamespace(cx).getDelegate();
        if (name != null && name.equals("*")) {
            return QName.create(null, null);
        }
        if (attribute) {
            return QName.create(Namespace.GLOBAL, name);
        }
        return QName.create(defaultNamespace, name);
    }

    /* access modifiers changed from: 0000 */
    public QName toNodeQName(Context cx, Object nameValue, boolean attribute) {
        if (nameValue instanceof XMLName) {
            return ((XMLName) nameValue).toQname();
        }
        if (nameValue instanceof QName) {
            return ((QName) nameValue).getDelegate();
        }
        if ((nameValue instanceof Boolean) || (nameValue instanceof Number) || nameValue == Undefined.instance || nameValue == null) {
            throw badXMLName(nameValue);
        }
        String local;
        if (nameValue instanceof String) {
            local = (String) nameValue;
        } else {
            local = ScriptRuntime.toString(nameValue);
        }
        return toNodeQName(cx, local, attribute);
    }

    public boolean isXMLName(Context _cx, Object nameObj) {
        return XMLName.accept(nameObj);
    }

    public Object toDefaultXmlNamespace(Context cx, Object uriValue) {
        return this.namespacePrototype.constructNamespace(uriValue);
    }

    public String escapeTextValue(Object o) {
        return this.options.escapeTextValue(o);
    }

    public String escapeAttributeValue(Object o) {
        return this.options.escapeAttributeValue(o);
    }

    public Ref nameRef(Context cx, Object name, Scriptable scope, int memberTypeFlags) {
        if ((memberTypeFlags & 2) != 0) {
            return xmlPrimaryReference(cx, toAttributeName(cx, name), scope);
        }
        throw Kit.codeBug();
    }

    public Ref nameRef(Context cx, Object namespace, Object name, Scriptable scope, int memberTypeFlags) {
        XMLName xmlName = XMLName.create(toNodeQName(cx, namespace, name), false, false);
        if (!((memberTypeFlags & 2) == 0 || xmlName.isAttributeName())) {
            xmlName.setAttributeName();
        }
        return xmlPrimaryReference(cx, xmlName, scope);
    }
}
