package org.mozilla.javascript.xmlimpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptRuntime;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

class XmlProcessor implements Serializable {
    private static final long serialVersionUID = 6903514433204808713L;
    private transient LinkedBlockingDeque<DocumentBuilder> documentBuilderPool;
    private transient DocumentBuilderFactory dom;
    private RhinoSAXErrorHandler errorHandler = new RhinoSAXErrorHandler();
    private boolean ignoreComments;
    private boolean ignoreProcessingInstructions;
    private boolean ignoreWhitespace;
    private int prettyIndent;
    private boolean prettyPrint;
    private transient TransformerFactory xform;

    private static class RhinoSAXErrorHandler implements ErrorHandler, Serializable {
        private static final long serialVersionUID = 6918417235413084055L;

        private RhinoSAXErrorHandler() {
        }

        private void throwError(SAXParseException e) {
            throw ScriptRuntime.constructError("TypeError", e.getMessage(), e.getLineNumber() - 1);
        }

        public void error(SAXParseException e) {
            throwError(e);
        }

        public void fatalError(SAXParseException e) {
            throwError(e);
        }

        public void warning(SAXParseException e) {
            Context.reportWarning(e.getMessage());
        }
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.dom = DocumentBuilderFactory.newInstance();
        this.dom.setNamespaceAware(true);
        this.dom.setIgnoringComments(false);
        this.xform = TransformerFactory.newInstance();
        this.documentBuilderPool = new LinkedBlockingDeque(Runtime.getRuntime().availableProcessors() * 2);
    }

    XmlProcessor() {
        setDefault();
        this.dom = DocumentBuilderFactory.newInstance();
        this.dom.setNamespaceAware(true);
        this.dom.setIgnoringComments(false);
        this.xform = TransformerFactory.newInstance();
        this.documentBuilderPool = new LinkedBlockingDeque(Runtime.getRuntime().availableProcessors() * 2);
    }

    /* access modifiers changed from: final */
    public final void setDefault() {
        setIgnoreComments(true);
        setIgnoreProcessingInstructions(true);
        setIgnoreWhitespace(true);
        setPrettyPrinting(true);
        setPrettyIndent(2);
    }

    /* access modifiers changed from: final */
    public final void setIgnoreComments(boolean b) {
        this.ignoreComments = b;
    }

    /* access modifiers changed from: final */
    public final void setIgnoreWhitespace(boolean b) {
        this.ignoreWhitespace = b;
    }

    /* access modifiers changed from: final */
    public final void setIgnoreProcessingInstructions(boolean b) {
        this.ignoreProcessingInstructions = b;
    }

    /* access modifiers changed from: final */
    public final void setPrettyPrinting(boolean b) {
        this.prettyPrint = b;
    }

    /* access modifiers changed from: final */
    public final void setPrettyIndent(int i) {
        this.prettyIndent = i;
    }

    /* access modifiers changed from: final */
    public final boolean isIgnoreComments() {
        return this.ignoreComments;
    }

    /* access modifiers changed from: final */
    public final boolean isIgnoreProcessingInstructions() {
        return this.ignoreProcessingInstructions;
    }

    /* access modifiers changed from: final */
    public final boolean isIgnoreWhitespace() {
        return this.ignoreWhitespace;
    }

    /* access modifiers changed from: final */
    public final boolean isPrettyPrinting() {
        return this.prettyPrint;
    }

    /* access modifiers changed from: final */
    public final int getPrettyIndent() {
        return this.prettyIndent;
    }

    private String toXmlNewlines(String rv) {
        StringBuilder nl = new StringBuilder();
        for (int i = 0; i < rv.length(); i++) {
            if (rv.charAt(i) != 13) {
                nl.append(rv.charAt(i));
            } else if (rv.charAt(i + 1) != 10) {
                nl.append(10);
            }
        }
        return nl.toString();
    }

    private DocumentBuilderFactory getDomFactory() {
        return this.dom;
    }

    private DocumentBuilder getDocumentBuilderFromPool() throws ParserConfigurationException {
        DocumentBuilder builder = (DocumentBuilder) this.documentBuilderPool.pollFirst();
        if (builder == null) {
            builder = getDomFactory().newDocumentBuilder();
        }
        builder.setErrorHandler(this.errorHandler);
        return builder;
    }

    private void returnDocumentBuilderToPool(DocumentBuilder db) {
        try {
            db.reset();
            this.documentBuilderPool.offerFirst(db);
        } catch (UnsupportedOperationException e) {
        }
    }

    private void addProcessingInstructionsTo(List<Node> list, Node node) {
        if (node instanceof ProcessingInstruction) {
            list.add(node);
        }
        if (node.getChildNodes() != null) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                addProcessingInstructionsTo(list, node.getChildNodes().item(i));
            }
        }
    }

    private void addCommentsTo(List<Node> list, Node node) {
        if (node instanceof Comment) {
            list.add(node);
        }
        if (node.getChildNodes() != null) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                addProcessingInstructionsTo(list, node.getChildNodes().item(i));
            }
        }
    }

    private void addTextNodesToRemoveAndTrim(List<Node> toRemove, Node node) {
        if (node instanceof Text) {
            Text text = (Text) node;
            if (!false) {
                text.setData(text.getData().trim());
            } else if (text.getData().trim().length() == 0) {
                text.setData("");
            }
            if (text.getData().length() == 0) {
                toRemove.add(node);
            }
        }
        if (node.getChildNodes() != null) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                addTextNodesToRemoveAndTrim(toRemove, node.getChildNodes().item(i));
            }
        }
    }

    /* access modifiers changed from: final */
    public final Node toXml(String defaultNamespaceUri, String xml) throws SAXException {
        DocumentBuilder builder = null;
        try {
            List<Node> list;
            Node node;
            String syntheticXml = "<parent xmlns=\"" + defaultNamespaceUri + "\">" + xml + "</parent>";
            builder = getDocumentBuilderFromPool();
            Document document = builder.parse(new InputSource(new StringReader(syntheticXml)));
            if (this.ignoreProcessingInstructions) {
                list = new ArrayList();
                addProcessingInstructionsTo(list, document);
                for (Node node2 : list) {
                    node2.getParentNode().removeChild(node2);
                }
            }
            if (this.ignoreComments) {
                list = new ArrayList();
                addCommentsTo(list, document);
                for (Node node22 : list) {
                    node22.getParentNode().removeChild(node22);
                }
            }
            if (this.ignoreWhitespace) {
                list = new ArrayList();
                addTextNodesToRemoveAndTrim(list, document);
                for (Node node222 : list) {
                    node222.getParentNode().removeChild(node222);
                }
            }
            NodeList rv = document.getDocumentElement().getChildNodes();
            if (rv.getLength() > 1) {
                throw ScriptRuntime.constructError("SyntaxError", "XML objects may contain at most one node.");
            } else if (rv.getLength() == 0) {
                node222 = document.createTextNode("");
                if (builder != null) {
                    returnDocumentBuilderToPool(builder);
                }
                return node222;
            } else {
                node222 = rv.item(0);
                document.getDocumentElement().removeChild(node222);
                if (builder != null) {
                    returnDocumentBuilderToPool(builder);
                }
                return node222;
            }
        } catch (IOException e) {
            throw new RuntimeException("Unreachable.");
        } catch (ParserConfigurationException e2) {
            throw new RuntimeException(e2);
        } catch (Throwable th) {
            if (builder != null) {
                returnDocumentBuilderToPool(builder);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public Document newDocument() {
        DocumentBuilder builder = null;
        try {
            builder = getDocumentBuilderFromPool();
            Document newDocument = builder.newDocument();
            if (builder != null) {
                returnDocumentBuilderToPool(builder);
            }
            return newDocument;
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        } catch (Throwable th) {
            if (builder != null) {
                returnDocumentBuilderToPool(builder);
            }
        }
    }

    private String toString(Node node) {
        DOMSource source = new DOMSource(node);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        try {
            Transformer transformer = this.xform.newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.setOutputProperty("indent", "no");
            transformer.setOutputProperty("method", "xml");
            transformer.transform(source, result);
            return toXmlNewlines(writer.toString());
        } catch (TransformerConfigurationException ex) {
            throw new RuntimeException(ex);
        } catch (TransformerException ex2) {
            throw new RuntimeException(ex2);
        }
    }

    /* access modifiers changed from: 0000 */
    public String escapeAttributeValue(Object value) {
        String text = ScriptRuntime.toString(value);
        if (text.length() == 0) {
            return "";
        }
        Element e = newDocument().createElement("a");
        e.setAttribute("b", text);
        String elementText = toString(e);
        return elementText.substring(elementText.indexOf(34) + 1, elementText.lastIndexOf(34));
    }

    /* access modifiers changed from: 0000 */
    public String escapeTextValue(Object value) {
        if (value instanceof XMLObjectImpl) {
            return ((XMLObjectImpl) value).toXMLString();
        }
        String text = ScriptRuntime.toString(value);
        if (text.length() == 0) {
            return text;
        }
        Element e = newDocument().createElement("a");
        e.setTextContent(text);
        String elementText = toString(e);
        int begin = elementText.indexOf(62) + 1;
        int end = elementText.lastIndexOf(60);
        return begin < end ? elementText.substring(begin, end) : "";
    }

    private String escapeElementValue(String s) {
        return escapeTextValue(s);
    }

    private String elementToXmlString(Element element) {
        Element copy = (Element) element.cloneNode(true);
        if (this.prettyPrint) {
            beautifyElement(copy, 0);
        }
        return toString(copy);
    }

    /* access modifiers changed from: final */
    public final String ecmaToXmlString(Node node) {
        StringBuilder s = new StringBuilder();
        if (this.prettyPrint) {
            for (int i = 0; i < 0; i++) {
                s.append(' ');
            }
        }
        if (node instanceof Text) {
            String v;
            String data = ((Text) node).getData();
            if (this.prettyPrint) {
                v = data.trim();
            } else {
                v = data;
            }
            s.append(escapeElementValue(v));
            return s.toString();
        } else if (node instanceof Attr) {
            s.append(escapeAttributeValue(((Attr) node).getValue()));
            return s.toString();
        } else if (node instanceof Comment) {
            s.append("<!--" + ((Comment) node).getNodeValue() + "-->");
            return s.toString();
        } else if (node instanceof ProcessingInstruction) {
            ProcessingInstruction pi = (ProcessingInstruction) node;
            s.append("<?" + pi.getTarget() + " " + pi.getData() + "?>");
            return s.toString();
        } else {
            s.append(elementToXmlString((Element) node));
            return s.toString();
        }
    }

    private void beautifyElement(Element e, int indent) {
        int i;
        StringBuilder s = new StringBuilder();
        s.append(10);
        for (i = 0; i < indent; i++) {
            s.append(' ');
        }
        String afterContent = s.toString();
        for (i = 0; i < this.prettyIndent; i++) {
            s.append(' ');
        }
        String beforeContent = s.toString();
        ArrayList<Node> toIndent = new ArrayList();
        boolean indentChildren = false;
        for (i = 0; i < e.getChildNodes().getLength(); i++) {
            if (i == 1) {
                indentChildren = true;
            }
            if (e.getChildNodes().item(i) instanceof Text) {
                toIndent.add(e.getChildNodes().item(i));
            } else {
                indentChildren = true;
                toIndent.add(e.getChildNodes().item(i));
            }
        }
        if (indentChildren) {
            for (i = 0; i < toIndent.size(); i++) {
                e.insertBefore(e.getOwnerDocument().createTextNode(beforeContent), (Node) toIndent.get(i));
            }
        }
        NodeList nodes = e.getChildNodes();
        ArrayList<Element> list = new ArrayList();
        for (i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i) instanceof Element) {
                list.add((Element) nodes.item(i));
            }
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            beautifyElement((Element) it.next(), this.prettyIndent + indent);
        }
        if (indentChildren) {
            e.appendChild(e.getOwnerDocument().createTextNode(afterContent));
        }
    }
}
