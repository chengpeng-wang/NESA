package org.mozilla.javascript.xmlimpl;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.xml.XMLObject;
import org.w3c.dom.Node;

class XML extends XMLObjectImpl {
    static final long serialVersionUID = -630969919086449092L;
    private XmlNode node;

    XML(XMLLibImpl lib, Scriptable scope, XMLObject prototype, XmlNode node) {
        super(lib, scope, prototype);
        initialize(node);
    }

    /* access modifiers changed from: 0000 */
    public void initialize(XmlNode node) {
        this.node = node;
        this.node.setXml(this);
    }

    /* access modifiers changed from: final */
    public final XML getXML() {
        return this;
    }

    /* access modifiers changed from: 0000 */
    public void replaceWith(XML value) {
        if (this.node.parent() == null) {
            initialize(value.node);
        } else {
            this.node.replaceWith(value.node);
        }
    }

    /* access modifiers changed from: 0000 */
    public XML makeXmlFromString(XMLName name, String value) {
        try {
            return newTextElementXML(this.node, name.toQname(), value);
        } catch (Exception e) {
            throw ScriptRuntime.typeError(e.getMessage());
        }
    }

    /* access modifiers changed from: 0000 */
    public XmlNode getAnnotation() {
        return this.node;
    }

    public Object get(int index, Scriptable start) {
        return index == 0 ? this : Scriptable.NOT_FOUND;
    }

    public boolean has(int index, Scriptable start) {
        return index == 0;
    }

    public void put(int index, Scriptable start, Object value) {
        throw ScriptRuntime.typeError("Assignment to indexed XML is not allowed");
    }

    public Object[] getIds() {
        if (isPrototype()) {
            return new Object[0];
        }
        return new Object[]{Integer.valueOf(0)};
    }

    public void delete(int index) {
        if (index == 0) {
            remove();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean hasXMLProperty(XMLName xmlName) {
        return getPropertyList(xmlName).length() > 0;
    }

    /* access modifiers changed from: 0000 */
    public Object getXMLProperty(XMLName xmlName) {
        return getPropertyList(xmlName);
    }

    /* access modifiers changed from: 0000 */
    public QName getNodeQname() {
        return this.node.getQname();
    }

    /* access modifiers changed from: 0000 */
    public XML[] getChildren() {
        if (!isElement()) {
            return null;
        }
        XmlNode[] children = this.node.getMatchingChildren(Filter.TRUE);
        XML[] rv = new XML[children.length];
        for (int i = 0; i < rv.length; i++) {
            rv[i] = toXML(children[i]);
        }
        return rv;
    }

    /* access modifiers changed from: 0000 */
    public XML[] getAttributes() {
        XmlNode[] attributes = this.node.getAttributes();
        XML[] rv = new XML[attributes.length];
        for (int i = 0; i < rv.length; i++) {
            rv[i] = toXML(attributes[i]);
        }
        return rv;
    }

    /* access modifiers changed from: 0000 */
    public XMLList getPropertyList(XMLName name) {
        return name.getMyValueOn(this);
    }

    /* access modifiers changed from: 0000 */
    public void deleteXMLProperty(XMLName name) {
        XMLList list = getPropertyList(name);
        for (int i = 0; i < list.length(); i++) {
            list.item(i).node.deleteMe();
        }
    }

    /* access modifiers changed from: 0000 */
    public void putXMLProperty(XMLName xmlName, Object value) {
        if (!isPrototype()) {
            xmlName.setMyValueOn(this, value);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean hasOwnProperty(XMLName xmlName) {
        if (!isPrototype()) {
            return getPropertyList(xmlName).length() > 0;
        } else {
            if (findPrototypeId(xmlName.localName()) != 0) {
                return true;
            }
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public Object jsConstructor(Context cx, boolean inNewExpr, Object[] args) {
        if (args.length == 0 || args[0] == null || args[0] == Undefined.instance) {
            args = new Object[]{""};
        }
        XML toXml = ecmaToXml(args[0]);
        if (inNewExpr) {
            return toXml.copy();
        }
        return toXml;
    }

    public Scriptable getExtraMethodSource(Context cx) {
        if (hasSimpleContent()) {
            return ScriptRuntime.toObjectOrNull(cx, toString());
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void removeChild(int index) {
        this.node.removeChild(index);
    }

    /* access modifiers changed from: 0000 */
    public void normalize() {
        this.node.normalize();
    }

    private XML toXML(XmlNode node) {
        if (node.getXml() == null) {
            node.setXml(newXML(node));
        }
        return node.getXml();
    }

    /* access modifiers changed from: 0000 */
    public void setAttribute(XMLName xmlName, Object value) {
        if (!isElement()) {
            throw new IllegalStateException("Can only set attributes on elements.");
        } else if (xmlName.uri() == null && xmlName.localName().equals("*")) {
            throw ScriptRuntime.typeError("@* assignment not supported.");
        } else {
            this.node.setAttribute(xmlName.toQname(), ScriptRuntime.toString(value));
        }
    }

    /* access modifiers changed from: 0000 */
    public void remove() {
        this.node.deleteMe();
    }

    /* access modifiers changed from: 0000 */
    public void addMatches(XMLList rv, XMLName name) {
        name.addMatches(rv, this);
    }

    /* access modifiers changed from: 0000 */
    public XMLList elements(XMLName name) {
        XMLList rv = newXMLList();
        rv.setTargets(this, name.toQname());
        XmlNode[] elements = this.node.getMatchingChildren(Filter.ELEMENT);
        for (int i = 0; i < elements.length; i++) {
            if (name.matches(toXML(elements[i]))) {
                rv.addToList(toXML(elements[i]));
            }
        }
        return rv;
    }

    /* access modifiers changed from: 0000 */
    public XMLList child(XMLName xmlName) {
        XMLList rv = newXMLList();
        XmlNode[] elements = this.node.getMatchingChildren(Filter.ELEMENT);
        for (int i = 0; i < elements.length; i++) {
            if (xmlName.matchesElement(elements[i].getQname())) {
                rv.addToList(toXML(elements[i]));
            }
        }
        rv.setTargets(this, xmlName.toQname());
        return rv;
    }

    /* access modifiers changed from: 0000 */
    public XML replace(XMLName xmlName, Object xml) {
        putXMLProperty(xmlName, xml);
        return this;
    }

    /* access modifiers changed from: 0000 */
    public XMLList children() {
        XMLList rv = newXMLList();
        rv.setTargets(this, XMLName.formStar().toQname());
        XmlNode[] children = this.node.getMatchingChildren(Filter.TRUE);
        for (XmlNode toXML : children) {
            rv.addToList(toXML(toXML));
        }
        return rv;
    }

    /* access modifiers changed from: 0000 */
    public XMLList child(int index) {
        XMLList result = newXMLList();
        result.setTargets(this, null);
        if (index >= 0 && index < this.node.getChildCount()) {
            result.addToList(getXmlChild(index));
        }
        return result;
    }

    /* access modifiers changed from: 0000 */
    public XML getXmlChild(int index) {
        XmlNode child = this.node.getChild(index);
        if (child.getXml() == null) {
            child.setXml(newXML(child));
        }
        return child.getXml();
    }

    /* access modifiers changed from: 0000 */
    public XML getLastXmlChild() {
        int pos = this.node.getChildCount() - 1;
        if (pos < 0) {
            return null;
        }
        return getXmlChild(pos);
    }

    /* access modifiers changed from: 0000 */
    public int childIndex() {
        return this.node.getChildIndex();
    }

    /* access modifiers changed from: 0000 */
    public boolean contains(Object xml) {
        if (xml instanceof XML) {
            return equivalentXml(xml);
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public boolean equivalentXml(Object target) {
        boolean result = false;
        if (target instanceof XML) {
            return this.node.toXmlString(getProcessor()).equals(((XML) target).node.toXmlString(getProcessor()));
        }
        if (target instanceof XMLList) {
            XMLList otherList = (XMLList) target;
            if (otherList.length() == 1) {
                result = equivalentXml(otherList.getXML());
            }
        } else if (hasSimpleContent()) {
            result = toString().equals(ScriptRuntime.toString(target));
        }
        return result;
    }

    /* access modifiers changed from: 0000 */
    public XMLObjectImpl copy() {
        return newXML(this.node.copy());
    }

    /* access modifiers changed from: 0000 */
    public boolean hasSimpleContent() {
        if (isComment() || isProcessingInstruction()) {
            return false;
        }
        if (isText() || this.node.isAttributeType() || !this.node.hasChildElement()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public boolean hasComplexContent() {
        return !hasSimpleContent();
    }

    /* access modifiers changed from: 0000 */
    public int length() {
        return 1;
    }

    /* access modifiers changed from: 0000 */
    public boolean is(XML other) {
        return this.node.isSameNode(other.node);
    }

    /* access modifiers changed from: 0000 */
    public Object nodeKind() {
        return ecmaClass();
    }

    /* access modifiers changed from: 0000 */
    public Object parent() {
        if (this.node.parent() == null) {
            return null;
        }
        return newXML(this.node.parent());
    }

    /* access modifiers changed from: 0000 */
    public boolean propertyIsEnumerable(Object name) {
        if (name instanceof Integer) {
            if (((Integer) name).intValue() == 0) {
                return true;
            }
            return false;
        } else if (!(name instanceof Number)) {
            return ScriptRuntime.toString(name).equals("0");
        } else {
            double x = ((Number) name).doubleValue();
            if (x != 0.0d || 1.0d / x <= 0.0d) {
                return false;
            }
            return true;
        }
    }

    /* access modifiers changed from: 0000 */
    public Object valueOf() {
        return this;
    }

    /* access modifiers changed from: 0000 */
    public XMLList comments() {
        XMLList rv = newXMLList();
        this.node.addMatchingChildren(rv, Filter.COMMENT);
        return rv;
    }

    /* access modifiers changed from: 0000 */
    public XMLList text() {
        XMLList rv = newXMLList();
        this.node.addMatchingChildren(rv, Filter.TEXT);
        return rv;
    }

    /* access modifiers changed from: 0000 */
    public XMLList processingInstructions(XMLName xmlName) {
        XMLList rv = newXMLList();
        this.node.addMatchingChildren(rv, Filter.PROCESSING_INSTRUCTION(xmlName));
        return rv;
    }

    private XmlNode[] getNodesForInsert(Object value) {
        if (value instanceof XML) {
            return new XmlNode[]{((XML) value).node};
        } else if (value instanceof XMLList) {
            XMLList list = (XMLList) value;
            XmlNode[] rv = new XmlNode[list.length()];
            for (int i = 0; i < list.length(); i++) {
                rv[i] = list.item(i).node;
            }
            return rv;
        } else {
            return new XmlNode[]{XmlNode.createText(getProcessor(), ScriptRuntime.toString(value))};
        }
    }

    /* access modifiers changed from: 0000 */
    public XML replace(int index, Object xml) {
        XMLList xlChildToReplace = child(index);
        if (xlChildToReplace.length() > 0) {
            insertChildAfter(xlChildToReplace.item(0), xml);
            removeChild(index);
        }
        return this;
    }

    /* access modifiers changed from: 0000 */
    public XML prependChild(Object xml) {
        if (this.node.isParentType()) {
            this.node.insertChildrenAt(0, getNodesForInsert(xml));
        }
        return this;
    }

    /* access modifiers changed from: 0000 */
    public XML appendChild(Object xml) {
        if (this.node.isParentType()) {
            this.node.insertChildrenAt(this.node.getChildCount(), getNodesForInsert(xml));
        }
        return this;
    }

    private int getChildIndexOf(XML child) {
        for (int i = 0; i < this.node.getChildCount(); i++) {
            if (this.node.getChild(i).isSameNode(child.node)) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: 0000 */
    public XML insertChildBefore(XML child, Object xml) {
        if (child == null) {
            appendChild(xml);
        } else {
            XmlNode[] toInsert = getNodesForInsert(xml);
            int index = getChildIndexOf(child);
            if (index != -1) {
                this.node.insertChildrenAt(index, toInsert);
            }
        }
        return this;
    }

    /* access modifiers changed from: 0000 */
    public XML insertChildAfter(XML child, Object xml) {
        if (child == null) {
            prependChild(xml);
        } else {
            XmlNode[] toInsert = getNodesForInsert(xml);
            int index = getChildIndexOf(child);
            if (index != -1) {
                this.node.insertChildrenAt(index + 1, toInsert);
            }
        }
        return this;
    }

    /* access modifiers changed from: 0000 */
    public XML setChildren(Object xml) {
        if (isElement()) {
            while (this.node.getChildCount() > 0) {
                this.node.removeChild(0);
            }
            this.node.insertChildrenAt(0, getNodesForInsert(xml));
        }
        return this;
    }

    private void addInScopeNamespace(Namespace ns) {
        if (!isElement() || ns.prefix() == null) {
            return;
        }
        if (ns.prefix().length() != 0 || ns.uri().length() != 0) {
            if (this.node.getQname().getNamespace().getPrefix().equals(ns.prefix())) {
                this.node.invalidateNamespacePrefix();
            }
            this.node.declareNamespace(ns.prefix(), ns.uri());
        }
    }

    /* access modifiers changed from: 0000 */
    public Namespace[] inScopeNamespaces() {
        return createNamespaces(this.node.getInScopeNamespaces());
    }

    private Namespace adapt(Namespace ns) {
        if (ns.prefix() == null) {
            return Namespace.create(ns.uri());
        }
        return Namespace.create(ns.prefix(), ns.uri());
    }

    /* access modifiers changed from: 0000 */
    public XML removeNamespace(Namespace ns) {
        if (isElement()) {
            this.node.removeNamespace(adapt(ns));
        }
        return this;
    }

    /* access modifiers changed from: 0000 */
    public XML addNamespace(Namespace ns) {
        addInScopeNamespace(ns);
        return this;
    }

    /* access modifiers changed from: 0000 */
    public QName name() {
        if (isText() || isComment()) {
            return null;
        }
        if (isProcessingInstruction()) {
            return newQName("", this.node.getQname().getLocalName(), null);
        }
        return newQName(this.node.getQname());
    }

    /* access modifiers changed from: 0000 */
    public Namespace[] namespaceDeclarations() {
        return createNamespaces(this.node.getNamespaceDeclarations());
    }

    /* access modifiers changed from: 0000 */
    public Namespace namespace(String prefix) {
        if (prefix == null) {
            return createNamespace(this.node.getNamespaceDeclaration());
        }
        return createNamespace(this.node.getNamespaceDeclaration(prefix));
    }

    /* access modifiers changed from: 0000 */
    public String localName() {
        if (name() == null) {
            return null;
        }
        return name().localName();
    }

    /* access modifiers changed from: 0000 */
    public void setLocalName(String localName) {
        if (!isText() && !isComment()) {
            this.node.setLocalName(localName);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setName(QName name) {
        if (!isText() && !isComment()) {
            if (isProcessingInstruction()) {
                this.node.setLocalName(name.localName());
            } else {
                this.node.renameNode(name.getDelegate());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setNamespace(Namespace ns) {
        if (!isText() && !isComment() && !isProcessingInstruction()) {
            setName(newQName(ns.uri(), localName(), ns.prefix()));
        }
    }

    /* access modifiers changed from: final */
    public final String ecmaClass() {
        if (this.node.isTextType()) {
            return "text";
        }
        if (this.node.isAttributeType()) {
            return "attribute";
        }
        if (this.node.isCommentType()) {
            return "comment";
        }
        if (this.node.isProcessingInstructionType()) {
            return "processing-instruction";
        }
        if (this.node.isElementType()) {
            return "element";
        }
        throw new RuntimeException("Unrecognized type: " + this.node);
    }

    public String getClassName() {
        return "XML";
    }

    private String ecmaValue() {
        return this.node.ecmaValue();
    }

    private String ecmaToString() {
        if (isAttribute() || isText()) {
            return ecmaValue();
        }
        if (!hasSimpleContent()) {
            return toXMLString();
        }
        StringBuilder rv = new StringBuilder();
        for (int i = 0; i < this.node.getChildCount(); i++) {
            XmlNode child = this.node.getChild(i);
            if (!(child.isProcessingInstructionType() || child.isCommentType())) {
                rv.append(new XML(getLib(), getParentScope(), (XMLObject) getPrototype(), child).toString());
            }
        }
        return rv.toString();
    }

    public String toString() {
        return ecmaToString();
    }

    /* access modifiers changed from: 0000 */
    public String toSource(int indent) {
        return toXMLString();
    }

    /* access modifiers changed from: 0000 */
    public String toXMLString() {
        return this.node.ecmaToXMLString(getProcessor());
    }

    /* access modifiers changed from: final */
    public final boolean isAttribute() {
        return this.node.isAttributeType();
    }

    /* access modifiers changed from: final */
    public final boolean isComment() {
        return this.node.isCommentType();
    }

    /* access modifiers changed from: final */
    public final boolean isText() {
        return this.node.isTextType();
    }

    /* access modifiers changed from: final */
    public final boolean isElement() {
        return this.node.isElementType();
    }

    /* access modifiers changed from: final */
    public final boolean isProcessingInstruction() {
        return this.node.isProcessingInstructionType();
    }

    /* access modifiers changed from: 0000 */
    public Node toDomNode() {
        return this.node.toDomNode();
    }
}
