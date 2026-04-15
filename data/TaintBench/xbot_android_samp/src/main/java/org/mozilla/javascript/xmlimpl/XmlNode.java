package org.mozilla.javascript.xmlimpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mozilla.javascript.Undefined;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;
import org.xml.sax.SAXException;

class XmlNode implements Serializable {
    private static final boolean DOM_LEVEL_3 = true;
    private static final String USER_DATA_XMLNODE_KEY = XmlNode.class.getName();
    private static final String XML_NAMESPACES_NAMESPACE_URI = "http://www.w3.org/2000/xmlns/";
    private static final long serialVersionUID = 1;
    private Node dom;
    private UserDataHandler events = new XmlNodeUserDataHandler();
    private XML xml;

    static abstract class Filter {
        static final Filter COMMENT = new Filter() {
            /* access modifiers changed from: 0000 */
            public boolean accept(Node node) {
                return node.getNodeType() == (short) 8 ? XmlNode.DOM_LEVEL_3 : false;
            }
        };
        static Filter ELEMENT = new Filter() {
            /* access modifiers changed from: 0000 */
            public boolean accept(Node node) {
                return node.getNodeType() == (short) 1 ? XmlNode.DOM_LEVEL_3 : false;
            }
        };
        static final Filter TEXT = new Filter() {
            /* access modifiers changed from: 0000 */
            public boolean accept(Node node) {
                return node.getNodeType() == (short) 3 ? XmlNode.DOM_LEVEL_3 : false;
            }
        };
        static Filter TRUE = new Filter() {
            /* access modifiers changed from: 0000 */
            public boolean accept(Node node) {
                return XmlNode.DOM_LEVEL_3;
            }
        };

        public abstract boolean accept(Node node);

        Filter() {
        }

        static Filter PROCESSING_INSTRUCTION(final XMLName name) {
            return new Filter() {
                /* access modifiers changed from: 0000 */
                public boolean accept(Node node) {
                    if (node.getNodeType() != (short) 7) {
                        return false;
                    }
                    return name.matchesLocalName(((ProcessingInstruction) node).getTarget());
                }
            };
        }
    }

    static class InternalList implements Serializable {
        private static final long serialVersionUID = -3633151157292048978L;
        private List<XmlNode> list = new ArrayList();

        InternalList() {
        }

        private void _add(XmlNode n) {
            this.list.add(n);
        }

        /* access modifiers changed from: 0000 */
        public XmlNode item(int index) {
            return (XmlNode) this.list.get(index);
        }

        /* access modifiers changed from: 0000 */
        public void remove(int index) {
            this.list.remove(index);
        }

        /* access modifiers changed from: 0000 */
        public void add(InternalList other) {
            for (int i = 0; i < other.length(); i++) {
                _add(other.item(i));
            }
        }

        /* access modifiers changed from: 0000 */
        public void add(InternalList from, int startInclusive, int endExclusive) {
            for (int i = startInclusive; i < endExclusive; i++) {
                _add(from.item(i));
            }
        }

        /* access modifiers changed from: 0000 */
        public void add(XmlNode node) {
            _add(node);
        }

        /* access modifiers changed from: 0000 */
        public void add(XML xml) {
            _add(xml.getAnnotation());
        }

        /* access modifiers changed from: 0000 */
        public void addToList(Object toAdd) {
            if (!(toAdd instanceof Undefined)) {
                if (toAdd instanceof XMLList) {
                    XMLList xmlSrc = (XMLList) toAdd;
                    for (int i = 0; i < xmlSrc.length(); i++) {
                        _add(xmlSrc.item(i).getAnnotation());
                    }
                } else if (toAdd instanceof XML) {
                    _add(((XML) toAdd).getAnnotation());
                } else if (toAdd instanceof XmlNode) {
                    _add((XmlNode) toAdd);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public int length() {
            return this.list.size();
        }
    }

    static class Namespace implements Serializable {
        static final Namespace GLOBAL = create("", "");
        private static final long serialVersionUID = 4073904386884677090L;
        /* access modifiers changed from: private */
        public String prefix;
        /* access modifiers changed from: private */
        public String uri;

        static Namespace create(String prefix, String uri) {
            if (prefix == null) {
                throw new IllegalArgumentException("Empty string represents default namespace prefix");
            } else if (uri == null) {
                throw new IllegalArgumentException("Namespace may not lack a URI");
            } else {
                Namespace rv = new Namespace();
                rv.prefix = prefix;
                rv.uri = uri;
                return rv;
            }
        }

        static Namespace create(String uri) {
            Namespace rv = new Namespace();
            rv.uri = uri;
            if (uri == null || uri.length() == 0) {
                rv.prefix = "";
            }
            return rv;
        }

        private Namespace() {
        }

        public String toString() {
            if (this.prefix == null) {
                return "XmlNode.Namespace [" + this.uri + "]";
            }
            return "XmlNode.Namespace [" + this.prefix + "{" + this.uri + "}]";
        }

        /* access modifiers changed from: 0000 */
        public boolean isUnspecifiedPrefix() {
            return this.prefix == null ? XmlNode.DOM_LEVEL_3 : false;
        }

        /* access modifiers changed from: 0000 */
        public boolean is(Namespace other) {
            return (this.prefix == null || other.prefix == null || !this.prefix.equals(other.prefix) || !this.uri.equals(other.uri)) ? false : XmlNode.DOM_LEVEL_3;
        }

        /* access modifiers changed from: 0000 */
        public boolean isEmpty() {
            return (this.prefix != null && this.prefix.equals("") && this.uri.equals("")) ? XmlNode.DOM_LEVEL_3 : false;
        }

        /* access modifiers changed from: 0000 */
        public boolean isDefault() {
            return (this.prefix == null || !this.prefix.equals("")) ? false : XmlNode.DOM_LEVEL_3;
        }

        /* access modifiers changed from: 0000 */
        public boolean isGlobal() {
            return (this.uri == null || !this.uri.equals("")) ? false : XmlNode.DOM_LEVEL_3;
        }

        /* access modifiers changed from: private */
        public void setPrefix(String prefix) {
            if (prefix == null) {
                throw new IllegalArgumentException();
            }
            this.prefix = prefix;
        }

        /* access modifiers changed from: 0000 */
        public String getPrefix() {
            return this.prefix;
        }

        /* access modifiers changed from: 0000 */
        public String getUri() {
            return this.uri;
        }
    }

    private static class Namespaces {
        private Map<String, String> map = new HashMap();
        private Map<String, String> uriToPrefix = new HashMap();

        Namespaces() {
        }

        /* access modifiers changed from: 0000 */
        public void declare(Namespace n) {
            if (this.map.get(n.prefix) == null) {
                this.map.put(n.prefix, n.uri);
            }
            if (this.uriToPrefix.get(n.uri) == null) {
                this.uriToPrefix.put(n.uri, n.prefix);
            }
        }

        /* access modifiers changed from: 0000 */
        public Namespace getNamespaceByUri(String uri) {
            if (this.uriToPrefix.get(uri) == null) {
                return null;
            }
            return Namespace.create(uri, (String) this.uriToPrefix.get(uri));
        }

        /* access modifiers changed from: 0000 */
        public Namespace getNamespace(String prefix) {
            if (this.map.get(prefix) == null) {
                return null;
            }
            return Namespace.create(prefix, (String) this.map.get(prefix));
        }

        /* access modifiers changed from: 0000 */
        public Namespace[] getNamespaces() {
            ArrayList<Namespace> rv = new ArrayList();
            for (String prefix : this.map.keySet()) {
                Namespace n = Namespace.create(prefix, (String) this.map.get(prefix));
                if (!n.isEmpty()) {
                    rv.add(n);
                }
            }
            return (Namespace[]) rv.toArray(new Namespace[rv.size()]);
        }
    }

    static class QName implements Serializable {
        private static final long serialVersionUID = -6587069811691451077L;
        private String localName;
        private Namespace namespace;

        static QName create(Namespace namespace, String localName) {
            if (localName == null || !localName.equals("*")) {
                QName rv = new QName();
                rv.namespace = namespace;
                rv.localName = localName;
                return rv;
            }
            throw new RuntimeException("* is not valid localName");
        }

        @Deprecated
        static QName create(String uri, String localName, String prefix) {
            return create(Namespace.create(prefix, uri), localName);
        }

        static String qualify(String prefix, String localName) {
            if (prefix == null) {
                throw new IllegalArgumentException("prefix must not be null");
            } else if (prefix.length() > 0) {
                return prefix + ":" + localName;
            } else {
                return localName;
            }
        }

        private QName() {
        }

        public String toString() {
            return "XmlNode.QName [" + this.localName + "," + this.namespace + "]";
        }

        private boolean equals(String one, String two) {
            if (one == null && two == null) {
                return XmlNode.DOM_LEVEL_3;
            }
            if (one == null || two == null) {
                return false;
            }
            return one.equals(two);
        }

        private boolean namespacesEqual(Namespace one, Namespace two) {
            if (one == null && two == null) {
                return XmlNode.DOM_LEVEL_3;
            }
            if (one == null || two == null) {
                return false;
            }
            return equals(one.getUri(), two.getUri());
        }

        /* access modifiers changed from: final */
        public final boolean equals(QName other) {
            if (namespacesEqual(this.namespace, other.namespace) && equals(this.localName, other.localName)) {
                return XmlNode.DOM_LEVEL_3;
            }
            return false;
        }

        public boolean equals(Object obj) {
            if (obj instanceof QName) {
                return equals((QName) obj);
            }
            return false;
        }

        public int hashCode() {
            return this.localName == null ? 0 : this.localName.hashCode();
        }

        /* access modifiers changed from: 0000 */
        public void lookupPrefix(Node node) {
            if (node == null) {
                throw new IllegalArgumentException("node must not be null");
            }
            String prefix = node.lookupPrefix(this.namespace.getUri());
            if (prefix == null) {
                String defaultNamespace = node.lookupNamespaceURI(null);
                if (defaultNamespace == null) {
                    defaultNamespace = "";
                }
                if (this.namespace.getUri().equals(defaultNamespace)) {
                    prefix = "";
                }
            }
            int i = 0;
            while (prefix == null) {
                int i2 = i + 1;
                String generatedPrefix = "e4x_" + i;
                if (node.lookupNamespaceURI(generatedPrefix) == null) {
                    prefix = generatedPrefix;
                    Node top = node;
                    while (top.getParentNode() != null && (top.getParentNode() instanceof Element)) {
                        top = top.getParentNode();
                    }
                    ((Element) top).setAttributeNS(XmlNode.XML_NAMESPACES_NAMESPACE_URI, "xmlns:" + prefix, this.namespace.getUri());
                }
                i = i2;
            }
            this.namespace.setPrefix(prefix);
        }

        /* access modifiers changed from: 0000 */
        public String qualify(Node node) {
            if (this.namespace.getPrefix() == null) {
                if (node != null) {
                    lookupPrefix(node);
                } else if (this.namespace.getUri().equals("")) {
                    this.namespace.setPrefix("");
                } else {
                    this.namespace.setPrefix("");
                }
            }
            return qualify(this.namespace.getPrefix(), this.localName);
        }

        /* access modifiers changed from: 0000 */
        public void setAttribute(Element element, String value) {
            if (this.namespace.getPrefix() == null) {
                lookupPrefix(element);
            }
            element.setAttributeNS(this.namespace.getUri(), qualify(this.namespace.getPrefix(), this.localName), value);
        }

        /* access modifiers changed from: 0000 */
        public Namespace getNamespace() {
            return this.namespace;
        }

        /* access modifiers changed from: 0000 */
        public String getLocalName() {
            return this.localName;
        }
    }

    static class XmlNodeUserDataHandler implements UserDataHandler, Serializable {
        private static final long serialVersionUID = 4666895518900769588L;

        XmlNodeUserDataHandler() {
        }

        public void handle(short operation, String key, Object data, Node src, Node dest) {
        }
    }

    private static XmlNode getUserData(Node node) {
        return (XmlNode) node.getUserData(USER_DATA_XMLNODE_KEY);
    }

    private static void setUserData(Node node, XmlNode wrap) {
        node.setUserData(USER_DATA_XMLNODE_KEY, wrap, wrap.events);
    }

    private static XmlNode createImpl(Node node) {
        if (node instanceof Document) {
            throw new IllegalArgumentException();
        } else if (getUserData(node) != null) {
            return getUserData(node);
        } else {
            XmlNode rv = new XmlNode();
            rv.dom = node;
            setUserData(node, rv);
            return rv;
        }
    }

    static XmlNode newElementWithText(XmlProcessor processor, XmlNode reference, QName qname, String value) {
        if (reference instanceof Document) {
            throw new IllegalArgumentException("Cannot use Document node as reference");
        }
        Document document;
        Node referenceDom;
        Element e;
        if (reference != null) {
            document = reference.dom.getOwnerDocument();
        } else {
            document = processor.newDocument();
        }
        if (reference != null) {
            referenceDom = reference.dom;
        } else {
            referenceDom = null;
        }
        Namespace ns = qname.getNamespace();
        if (ns == null || ns.getUri().length() == 0) {
            e = document.createElementNS(null, qname.getLocalName());
        } else {
            e = document.createElementNS(ns.getUri(), qname.qualify(referenceDom));
        }
        if (value != null) {
            e.appendChild(document.createTextNode(value));
        }
        return createImpl(e);
    }

    static XmlNode createText(XmlProcessor processor, String value) {
        return createImpl(processor.newDocument().createTextNode(value));
    }

    static XmlNode createElementFromNode(Node node) {
        if (node instanceof Document) {
            node = ((Document) node).getDocumentElement();
        }
        return createImpl(node);
    }

    static XmlNode createElement(XmlProcessor processor, String namespaceUri, String xml) throws SAXException {
        return createImpl(processor.toXml(namespaceUri, xml));
    }

    static XmlNode createEmpty(XmlProcessor processor) {
        return createText(processor, "");
    }

    private static XmlNode copy(XmlNode other) {
        return createImpl(other.dom.cloneNode(DOM_LEVEL_3));
    }

    private XmlNode() {
    }

    /* access modifiers changed from: 0000 */
    public String debug() {
        XmlProcessor raw = new XmlProcessor();
        raw.setIgnoreComments(false);
        raw.setIgnoreProcessingInstructions(false);
        raw.setIgnoreWhitespace(false);
        raw.setPrettyPrinting(false);
        return raw.ecmaToXmlString(this.dom);
    }

    public String toString() {
        return "XmlNode: type=" + this.dom.getNodeType() + " dom=" + this.dom.toString();
    }

    /* access modifiers changed from: 0000 */
    public XML getXml() {
        return this.xml;
    }

    /* access modifiers changed from: 0000 */
    public void setXml(XML xml) {
        this.xml = xml;
    }

    /* access modifiers changed from: 0000 */
    public int getChildCount() {
        return this.dom.getChildNodes().getLength();
    }

    /* access modifiers changed from: 0000 */
    public XmlNode parent() {
        Node domParent = this.dom.getParentNode();
        if ((domParent instanceof Document) || domParent == null) {
            return null;
        }
        return createImpl(domParent);
    }

    /* access modifiers changed from: 0000 */
    public int getChildIndex() {
        int i = -1;
        if (!(isAttributeType() || parent() == null)) {
            NodeList siblings = this.dom.getParentNode().getChildNodes();
            i = 0;
            while (i < siblings.getLength()) {
                if (siblings.item(i) != this.dom) {
                    i++;
                }
            }
            throw new RuntimeException("Unreachable.");
        }
        return i;
    }

    /* access modifiers changed from: 0000 */
    public void removeChild(int index) {
        this.dom.removeChild(this.dom.getChildNodes().item(index));
    }

    /* access modifiers changed from: 0000 */
    public String toXmlString(XmlProcessor processor) {
        return processor.ecmaToXmlString(this.dom);
    }

    /* access modifiers changed from: 0000 */
    public String ecmaValue() {
        if (isTextType()) {
            return ((Text) this.dom).getData();
        }
        if (isAttributeType()) {
            return ((Attr) this.dom).getValue();
        }
        if (isProcessingInstructionType()) {
            return ((ProcessingInstruction) this.dom).getData();
        }
        if (isCommentType()) {
            return ((Comment) this.dom).getNodeValue();
        }
        if (isElementType()) {
            throw new RuntimeException("Unimplemented ecmaValue() for elements.");
        }
        throw new RuntimeException("Unimplemented for node " + this.dom);
    }

    /* access modifiers changed from: 0000 */
    public void deleteMe() {
        if (this.dom instanceof Attr) {
            Attr attr = this.dom;
            attr.getOwnerElement().getAttributes().removeNamedItemNS(attr.getNamespaceURI(), attr.getLocalName());
        } else if (this.dom.getParentNode() != null) {
            this.dom.getParentNode().removeChild(this.dom);
        }
    }

    /* access modifiers changed from: 0000 */
    public void normalize() {
        this.dom.normalize();
    }

    /* access modifiers changed from: 0000 */
    public void insertChildAt(int index, XmlNode node) {
        Node parent = this.dom;
        Node child = parent.getOwnerDocument().importNode(node.dom, DOM_LEVEL_3);
        if (parent.getChildNodes().getLength() < index) {
            throw new IllegalArgumentException("index=" + index + " length=" + parent.getChildNodes().getLength());
        } else if (parent.getChildNodes().getLength() == index) {
            parent.appendChild(child);
        } else {
            parent.insertBefore(child, parent.getChildNodes().item(index));
        }
    }

    /* access modifiers changed from: 0000 */
    public void insertChildrenAt(int index, XmlNode[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            insertChildAt(index + i, nodes[i]);
        }
    }

    /* access modifiers changed from: 0000 */
    public XmlNode getChild(int index) {
        return createImpl(this.dom.getChildNodes().item(index));
    }

    /* access modifiers changed from: 0000 */
    public boolean hasChildElement() {
        NodeList nodes = this.dom.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == (short) 1) {
                return DOM_LEVEL_3;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public boolean isSameNode(XmlNode other) {
        return this.dom == other.dom ? DOM_LEVEL_3 : false;
    }

    private String toUri(String ns) {
        return ns == null ? "" : ns;
    }

    private void addNamespaces(Namespaces rv, Element element) {
        if (element == null) {
            throw new RuntimeException("element must not be null");
        }
        String myDefaultNamespace = toUri(element.lookupNamespaceURI(null));
        String parentDefaultNamespace = "";
        if (element.getParentNode() != null) {
            parentDefaultNamespace = toUri(element.getParentNode().lookupNamespaceURI(null));
        }
        if (!(myDefaultNamespace.equals(parentDefaultNamespace) && (element.getParentNode() instanceof Element))) {
            rv.declare(Namespace.create("", myDefaultNamespace));
        }
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Attr attr = (Attr) attributes.item(i);
            if (attr.getPrefix() != null && attr.getPrefix().equals("xmlns")) {
                rv.declare(Namespace.create(attr.getLocalName(), attr.getValue()));
            }
        }
    }

    private Namespaces getAllNamespaces() {
        Namespaces rv = new Namespaces();
        Node target = this.dom;
        if (target instanceof Attr) {
            target = ((Attr) target).getOwnerElement();
        }
        while (target != null) {
            if (target instanceof Element) {
                addNamespaces(rv, (Element) target);
            }
            target = target.getParentNode();
        }
        rv.declare(Namespace.create("", ""));
        return rv;
    }

    /* access modifiers changed from: 0000 */
    public Namespace[] getInScopeNamespaces() {
        return getAllNamespaces().getNamespaces();
    }

    /* access modifiers changed from: 0000 */
    public Namespace[] getNamespaceDeclarations() {
        if (!(this.dom instanceof Element)) {
            return new Namespace[0];
        }
        Namespaces rv = new Namespaces();
        addNamespaces(rv, (Element) this.dom);
        return rv.getNamespaces();
    }

    /* access modifiers changed from: 0000 */
    public Namespace getNamespaceDeclaration(String prefix) {
        if (prefix.equals("") && (this.dom instanceof Attr)) {
            return Namespace.create("", "");
        }
        return getAllNamespaces().getNamespace(prefix);
    }

    /* access modifiers changed from: 0000 */
    public Namespace getNamespaceDeclaration() {
        if (this.dom.getPrefix() == null) {
            return getNamespaceDeclaration("");
        }
        return getNamespaceDeclaration(this.dom.getPrefix());
    }

    /* access modifiers changed from: final */
    public final XmlNode copy() {
        return copy(this);
    }

    /* access modifiers changed from: final */
    public final boolean isParentType() {
        return isElementType();
    }

    /* access modifiers changed from: final */
    public final boolean isTextType() {
        return (this.dom.getNodeType() == (short) 3 || this.dom.getNodeType() == (short) 4) ? DOM_LEVEL_3 : false;
    }

    /* access modifiers changed from: final */
    public final boolean isAttributeType() {
        return this.dom.getNodeType() == (short) 2 ? DOM_LEVEL_3 : false;
    }

    /* access modifiers changed from: final */
    public final boolean isProcessingInstructionType() {
        return this.dom.getNodeType() == (short) 7 ? DOM_LEVEL_3 : false;
    }

    /* access modifiers changed from: final */
    public final boolean isCommentType() {
        return this.dom.getNodeType() == (short) 8 ? DOM_LEVEL_3 : false;
    }

    /* access modifiers changed from: final */
    public final boolean isElementType() {
        return this.dom.getNodeType() == (short) 1 ? DOM_LEVEL_3 : false;
    }

    /* access modifiers changed from: final */
    public final void renameNode(QName qname) {
        this.dom = this.dom.getOwnerDocument().renameNode(this.dom, qname.getNamespace().getUri(), qname.qualify(this.dom));
    }

    /* access modifiers changed from: 0000 */
    public void invalidateNamespacePrefix() {
        if (this.dom instanceof Element) {
            String prefix = this.dom.getPrefix();
            renameNode(QName.create(this.dom.getNamespaceURI(), this.dom.getLocalName(), null));
            NamedNodeMap attrs = this.dom.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                if (attrs.item(i).getPrefix().equals(prefix)) {
                    createImpl(attrs.item(i)).renameNode(QName.create(attrs.item(i).getNamespaceURI(), attrs.item(i).getLocalName(), null));
                }
            }
            return;
        }
        throw new IllegalStateException();
    }

    private void declareNamespace(Element e, String prefix, String uri) {
        if (prefix.length() > 0) {
            e.setAttributeNS(XML_NAMESPACES_NAMESPACE_URI, "xmlns:" + prefix, uri);
        } else {
            e.setAttribute("xmlns", uri);
        }
    }

    /* access modifiers changed from: 0000 */
    public void declareNamespace(String prefix, String uri) {
        if (!(this.dom instanceof Element)) {
            throw new IllegalStateException();
        } else if (this.dom.lookupNamespaceURI(uri) == null || !this.dom.lookupNamespaceURI(uri).equals(prefix)) {
            declareNamespace(this.dom, prefix, uri);
        }
    }

    private Namespace getDefaultNamespace() {
        return Namespace.create("", this.dom.lookupNamespaceURI(null) == null ? "" : this.dom.lookupNamespaceURI(null));
    }

    private String getExistingPrefixFor(Namespace namespace) {
        if (getDefaultNamespace().getUri().equals(namespace.getUri())) {
            return "";
        }
        return this.dom.lookupPrefix(namespace.getUri());
    }

    private Namespace getNodeNamespace() {
        String uri = this.dom.getNamespaceURI();
        String prefix = this.dom.getPrefix();
        if (uri == null) {
            uri = "";
        }
        if (prefix == null) {
            prefix = "";
        }
        return Namespace.create(prefix, uri);
    }

    /* access modifiers changed from: 0000 */
    public Namespace getNamespace() {
        return getNodeNamespace();
    }

    /* access modifiers changed from: 0000 */
    public void removeNamespace(Namespace namespace) {
        if (!namespace.is(getNodeNamespace())) {
            NamedNodeMap attrs = this.dom.getAttributes();
            int i = 0;
            while (i < attrs.getLength()) {
                if (!namespace.is(createImpl(attrs.item(i)).getNodeNamespace())) {
                    i++;
                } else {
                    return;
                }
            }
            String existingPrefix = getExistingPrefixFor(namespace);
            if (existingPrefix == null) {
                return;
            }
            if (namespace.isUnspecifiedPrefix()) {
                declareNamespace(existingPrefix, getDefaultNamespace().getUri());
            } else if (existingPrefix.equals(namespace.getPrefix())) {
                declareNamespace(existingPrefix, getDefaultNamespace().getUri());
            }
        }
    }

    private void setProcessingInstructionName(String localName) {
        ProcessingInstruction pi = this.dom;
        pi.getParentNode().replaceChild(pi, pi.getOwnerDocument().createProcessingInstruction(localName, pi.getData()));
    }

    /* access modifiers changed from: final */
    public final void setLocalName(String localName) {
        if (this.dom instanceof ProcessingInstruction) {
            setProcessingInstructionName(localName);
            return;
        }
        String prefix = this.dom.getPrefix();
        if (prefix == null) {
            prefix = "";
        }
        this.dom = this.dom.getOwnerDocument().renameNode(this.dom, this.dom.getNamespaceURI(), QName.qualify(prefix, localName));
    }

    /* access modifiers changed from: final */
    public final QName getQname() {
        return QName.create(this.dom.getNamespaceURI() == null ? "" : this.dom.getNamespaceURI(), this.dom.getLocalName(), this.dom.getPrefix() == null ? "" : this.dom.getPrefix());
    }

    /* access modifiers changed from: 0000 */
    public void addMatchingChildren(XMLList result, Filter filter) {
        NodeList children = this.dom.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node childnode = children.item(i);
            XmlNode child = createImpl(childnode);
            if (filter.accept(childnode)) {
                result.addToList(child);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public XmlNode[] getMatchingChildren(Filter filter) {
        ArrayList<XmlNode> rv = new ArrayList();
        NodeList nodes = this.dom.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (filter.accept(node)) {
                rv.add(createImpl(node));
            }
        }
        return (XmlNode[]) rv.toArray(new XmlNode[rv.size()]);
    }

    /* access modifiers changed from: 0000 */
    public XmlNode[] getAttributes() {
        NamedNodeMap attrs = this.dom.getAttributes();
        if (attrs == null) {
            throw new IllegalStateException("Must be element.");
        }
        XmlNode[] rv = new XmlNode[attrs.getLength()];
        for (int i = 0; i < attrs.getLength(); i++) {
            rv[i] = createImpl(attrs.item(i));
        }
        return rv;
    }

    /* access modifiers changed from: 0000 */
    public String getAttributeValue() {
        return ((Attr) this.dom).getValue();
    }

    /* access modifiers changed from: 0000 */
    public void setAttribute(QName name, String value) {
        if (this.dom instanceof Element) {
            name.setAttribute((Element) this.dom, value);
            return;
        }
        throw new IllegalStateException("Can only set attribute on elements.");
    }

    /* access modifiers changed from: 0000 */
    public void replaceWith(XmlNode other) {
        Node replacement = other.dom;
        if (replacement.getOwnerDocument() != this.dom.getOwnerDocument()) {
            replacement = this.dom.getOwnerDocument().importNode(replacement, DOM_LEVEL_3);
        }
        this.dom.getParentNode().replaceChild(replacement, this.dom);
    }

    /* access modifiers changed from: 0000 */
    public String ecmaToXMLString(XmlProcessor processor) {
        if (!isElementType()) {
            return processor.ecmaToXmlString(this.dom);
        }
        Element copy = (Element) this.dom.cloneNode(DOM_LEVEL_3);
        Namespace[] inScope = getInScopeNamespaces();
        for (int i = 0; i < inScope.length; i++) {
            declareNamespace(copy, inScope[i].getPrefix(), inScope[i].getUri());
        }
        return processor.ecmaToXmlString(copy);
    }

    /* access modifiers changed from: 0000 */
    public Node toDomNode() {
        return this.dom;
    }
}
