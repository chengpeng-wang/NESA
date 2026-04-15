package org.mozilla.javascript.xmlimpl;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.Kit;
import org.mozilla.javascript.Ref;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Undefined;

class XMLName extends Ref {
    static final long serialVersionUID = 3832176310755686977L;
    private boolean isAttributeName;
    private boolean isDescendants;
    private QName qname;
    private XMLObjectImpl xmlObject;

    private static boolean isNCNameStartChar(int c) {
        if ((c & -128) == 0) {
            if (c >= 97) {
                if (c <= 122) {
                    return true;
                }
                return false;
            } else if (c >= 65) {
                if (c <= 90 || c == 95) {
                    return true;
                }
                return false;
            }
        } else if ((c & -8192) == 0) {
            if (192 <= c && c <= 214) {
                return true;
            }
            if (216 <= c && c <= 246) {
                return true;
            }
            if (248 <= c && c <= 767) {
                return true;
            }
            if ((880 > c || c > 893) && 895 > c) {
                return false;
            }
            return true;
        }
        if (8204 <= c && c <= 8205) {
            return true;
        }
        if (8304 <= c && c <= 8591) {
            return true;
        }
        if (11264 <= c && c <= 12271) {
            return true;
        }
        if (12289 <= c && c <= 55295) {
            return true;
        }
        if (63744 <= c && c <= 64975) {
            return true;
        }
        if (65008 <= c && c <= 65533) {
            return true;
        }
        if (65536 > c || c > 983039) {
            return false;
        }
        return true;
    }

    private static boolean isNCNameChar(int c) {
        boolean z = false;
        if ((c & -128) == 0) {
            if (c >= 97) {
                if (c <= 122) {
                    return true;
                }
                return false;
            } else if (c >= 65) {
                if (c <= 90 || c == 95) {
                    return true;
                }
                return false;
            } else if (c < 48) {
                if (c == 45 || c == 46) {
                    z = true;
                }
                return z;
            } else if (c > 57) {
                return false;
            } else {
                return true;
            }
        } else if ((c & -8192) == 0) {
            if (isNCNameStartChar(c) || c == 183 || (768 <= c && c <= 879)) {
                z = true;
            }
            return z;
        } else {
            if (isNCNameStartChar(c) || (8255 <= c && c <= 8256)) {
                z = true;
            }
            return z;
        }
    }

    static boolean accept(Object nameObj) {
        try {
            String name = ScriptRuntime.toString(nameObj);
            int length = name.length();
            if (length == 0 || !isNCNameStartChar(name.charAt(0))) {
                return false;
            }
            for (int i = 1; i != length; i++) {
                if (!isNCNameChar(name.charAt(i))) {
                    return false;
                }
            }
            return true;
        } catch (EcmaError ee) {
            if ("TypeError".equals(ee.getName())) {
                return false;
            }
            throw ee;
        }
    }

    private XMLName() {
    }

    static XMLName formStar() {
        XMLName rv = new XMLName();
        rv.qname = QName.create(null, null);
        return rv;
    }

    @Deprecated
    static XMLName formProperty(Namespace namespace, String localName) {
        if (localName != null && localName.equals("*")) {
            localName = null;
        }
        XMLName rv = new XMLName();
        rv.qname = QName.create(namespace, localName);
        return rv;
    }

    static XMLName formProperty(String uri, String localName) {
        return formProperty(Namespace.create(uri), localName);
    }

    static XMLName create(String defaultNamespaceUri, String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        int l = name.length();
        if (l != 0) {
            char firstChar = name.charAt(0);
            if (firstChar == '*') {
                if (l == 1) {
                    return formStar();
                }
            } else if (firstChar == '@') {
                XMLName xmlName = formProperty("", name.substring(1));
                xmlName.setAttributeName();
                return xmlName;
            }
        }
        return formProperty(defaultNamespaceUri, name);
    }

    static XMLName create(QName qname, boolean attribute, boolean descendants) {
        XMLName rv = new XMLName();
        rv.qname = qname;
        rv.isAttributeName = attribute;
        rv.isDescendants = descendants;
        return rv;
    }

    @Deprecated
    static XMLName create(QName qname) {
        return create(qname, false, false);
    }

    /* access modifiers changed from: 0000 */
    public void initXMLObject(XMLObjectImpl xmlObject) {
        if (xmlObject == null) {
            throw new IllegalArgumentException();
        } else if (this.xmlObject != null) {
            throw new IllegalStateException();
        } else {
            this.xmlObject = xmlObject;
        }
    }

    /* access modifiers changed from: 0000 */
    public String uri() {
        if (this.qname.getNamespace() == null) {
            return null;
        }
        return this.qname.getNamespace().getUri();
    }

    /* access modifiers changed from: 0000 */
    public String localName() {
        if (this.qname.getLocalName() == null) {
            return "*";
        }
        return this.qname.getLocalName();
    }

    private void addDescendantChildren(XMLList list, XML target) {
        if (target.isElement()) {
            XML[] children = target.getChildren();
            for (int i = 0; i < children.length; i++) {
                if (matches(children[i])) {
                    list.addToList(children[i]);
                }
                addDescendantChildren(list, children[i]);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void addMatchingAttributes(XMLList list, XML target) {
        if (target.isElement()) {
            XML[] attributes = target.getAttributes();
            for (int i = 0; i < attributes.length; i++) {
                if (matches(attributes[i])) {
                    list.addToList(attributes[i]);
                }
            }
        }
    }

    private void addDescendantAttributes(XMLList list, XML target) {
        if (target.isElement()) {
            addMatchingAttributes(list, target);
            XML[] children = target.getChildren();
            for (XML addDescendantAttributes : children) {
                addDescendantAttributes(list, addDescendantAttributes);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public XMLList matchDescendantAttributes(XMLList rv, XML target) {
        rv.setTargets(target, null);
        addDescendantAttributes(rv, target);
        return rv;
    }

    /* access modifiers changed from: 0000 */
    public XMLList matchDescendantChildren(XMLList rv, XML target) {
        rv.setTargets(target, null);
        addDescendantChildren(rv, target);
        return rv;
    }

    /* access modifiers changed from: 0000 */
    public void addDescendants(XMLList rv, XML target) {
        if (isAttributeName()) {
            matchDescendantAttributes(rv, target);
        } else {
            matchDescendantChildren(rv, target);
        }
    }

    private void addAttributes(XMLList rv, XML target) {
        addMatchingAttributes(rv, target);
    }

    /* access modifiers changed from: 0000 */
    public void addMatches(XMLList rv, XML target) {
        if (isDescendants()) {
            addDescendants(rv, target);
        } else if (isAttributeName()) {
            addAttributes(rv, target);
        } else {
            XML[] children = target.getChildren();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    if (matches(children[i])) {
                        rv.addToList(children[i]);
                    }
                }
            }
            rv.setTargets(target, toQname());
        }
    }

    /* access modifiers changed from: 0000 */
    public XMLList getMyValueOn(XML target) {
        XMLList rv = target.newXMLList();
        addMatches(rv, target);
        return rv;
    }

    /* access modifiers changed from: 0000 */
    public void setMyValueOn(XML target, Object value) {
        if (value == null) {
            value = "null";
        } else if (value instanceof Undefined) {
            value = "undefined";
        }
        if (isAttributeName()) {
            target.setAttribute(this, value);
        } else if (uri() == null && localName().equals("*")) {
            target.setChildren(value);
        } else {
            Object xmlValue;
            int i;
            if (value instanceof XMLObjectImpl) {
                xmlValue = (XMLObjectImpl) value;
                if ((xmlValue instanceof XML) && ((XML) xmlValue).isAttribute()) {
                    xmlValue = target.makeXmlFromString(this, xmlValue.toString());
                }
                if (xmlValue instanceof XMLList) {
                    for (i = 0; i < xmlValue.length(); i++) {
                        XML xml = ((XMLList) xmlValue).item(i);
                        if (xml.isAttribute()) {
                            ((XMLList) xmlValue).replace(i, target.makeXmlFromString(this, xml.toString()));
                        }
                    }
                }
            } else {
                xmlValue = target.makeXmlFromString(this, ScriptRuntime.toString(value));
            }
            XMLList matches = target.getPropertyList(this);
            if (matches.length() == 0) {
                target.appendChild(xmlValue);
                return;
            }
            for (i = 1; i < matches.length(); i++) {
                target.removeChild(matches.item(i).childIndex());
            }
            target.replace(matches.item(0).childIndex(), xmlValue);
        }
    }

    public boolean has(Context cx) {
        if (this.xmlObject == null) {
            return false;
        }
        return this.xmlObject.hasXMLProperty(this);
    }

    public Object get(Context cx) {
        if (this.xmlObject != null) {
            return this.xmlObject.getXMLProperty(this);
        }
        throw ScriptRuntime.undefReadError(Undefined.instance, toString());
    }

    public Object set(Context cx, Object value) {
        if (this.xmlObject == null) {
            throw ScriptRuntime.undefWriteError(Undefined.instance, toString(), value);
        } else if (this.isDescendants) {
            throw Kit.codeBug();
        } else {
            this.xmlObject.putXMLProperty(this, value);
            return value;
        }
    }

    public boolean delete(Context cx) {
        if (this.xmlObject == null) {
            return true;
        }
        this.xmlObject.deleteXMLProperty(this);
        if (this.xmlObject.hasXMLProperty(this)) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder buff = new StringBuilder();
        if (this.isDescendants) {
            buff.append("..");
        }
        if (this.isAttributeName) {
            buff.append('@');
        }
        if (uri() == null) {
            buff.append('*');
            if (localName().equals("*")) {
                return buff.toString();
            }
        }
        buff.append('\"').append(uri()).append('\"');
        buff.append(':').append(localName());
        return buff.toString();
    }

    /* access modifiers changed from: final */
    public final QName toQname() {
        return this.qname;
    }

    /* access modifiers changed from: final */
    public final boolean matchesLocalName(String localName) {
        return localName().equals("*") || localName().equals(localName);
    }

    /* access modifiers changed from: final */
    public final boolean matchesElement(QName qname) {
        if ((uri() == null || uri().equals(qname.getNamespace().getUri())) && (localName().equals("*") || localName().equals(qname.getLocalName()))) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: final */
    public final boolean matches(XML node) {
        QName qname = node.getNodeQname();
        String nodeUri = null;
        if (qname.getNamespace() != null) {
            nodeUri = qname.getNamespace().getUri();
        }
        if (!this.isAttributeName) {
            if (uri() == null || (node.isElement() && uri().equals(nodeUri))) {
                if (localName().equals("*")) {
                    return true;
                }
                if (node.isElement() && localName().equals(qname.getLocalName())) {
                    return true;
                }
            }
            return false;
        } else if (!node.isAttribute()) {
            return false;
        } else {
            if ((uri() == null || uri().equals(nodeUri)) && (localName().equals("*") || localName().equals(qname.getLocalName()))) {
                return true;
            }
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isAttributeName() {
        return this.isAttributeName;
    }

    /* access modifiers changed from: 0000 */
    public void setAttributeName() {
        this.isAttributeName = true;
    }

    /* access modifiers changed from: 0000 */
    public boolean isDescendants() {
        return this.isDescendants;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    public void setIsDescendants() {
        this.isDescendants = true;
    }
}
