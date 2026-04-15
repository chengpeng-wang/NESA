package com.androidquery.util;

import android.util.Xml;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

public class XmlDom {
    private Element root;

    public Element getElement() {
        return this.root;
    }

    public XmlDom(Element element) {
        this.root = element;
    }

    public XmlDom(String str) throws SAXException {
        this(str.getBytes());
    }

    public XmlDom(byte[] data) throws SAXException {
        this(new ByteArrayInputStream(data));
    }

    public XmlDom(InputStream is) throws SAXException {
        try {
            this.root = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is).getDocumentElement();
        } catch (ParserConfigurationException e) {
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    public XmlDom tag(String tag) {
        NodeList nl = this.root.getElementsByTagName(tag);
        if (nl == null || nl.getLength() <= 0) {
            return null;
        }
        return new XmlDom((Element) nl.item(0));
    }

    public XmlDom tag(String tag, String attr, String value) {
        List<XmlDom> tags = tags(tag, attr, value);
        if (tags.size() == 0) {
            return null;
        }
        return (XmlDom) tags.get(0);
    }

    public List<XmlDom> tags(String tag) {
        return tags(tag, null, null);
    }

    public XmlDom child(String tag) {
        return child(tag, null, null);
    }

    public XmlDom child(String tag, String attr, String value) {
        List<XmlDom> c = children(tag, attr, value);
        if (c.size() == 0) {
            return null;
        }
        return (XmlDom) c.get(0);
    }

    public List<XmlDom> children(String tag) {
        return children(tag, null, null);
    }

    public List<XmlDom> children(String tag, String attr, String value) {
        return convert(this.root.getChildNodes(), tag, attr, value);
    }

    public List<XmlDom> tags(String tag, String attr, String value) {
        return convert(this.root.getElementsByTagName(tag), null, attr, value);
    }

    private static List<XmlDom> convert(NodeList nl, String tag, String attr, String value) {
        List<XmlDom> result = new ArrayList();
        for (int i = 0; i < nl.getLength(); i++) {
            XmlDom xml = convert(nl.item(i), tag, attr, value);
            if (xml != null) {
                result.add(xml);
            }
        }
        return result;
    }

    private static XmlDom convert(Node node, String tag, String attr, String value) {
        if (node.getNodeType() != (short) 1) {
            return null;
        }
        Element e = (Element) node;
        if (tag != null && !tag.equals(e.getTagName())) {
            return null;
        }
        if (attr != null && !e.hasAttribute(attr)) {
            return null;
        }
        if (value == null || value.equals(e.getAttribute(attr))) {
            return new XmlDom(e);
        }
        return null;
    }

    public String text(String tag) {
        XmlDom dom = child(tag);
        if (dom == null) {
            return null;
        }
        return dom.text();
    }

    public String attr(String name) {
        return this.root.getAttribute(name);
    }

    public String toString() {
        return toString(0);
    }

    public String toString(int intentSpaces) {
        return serialize(this.root, intentSpaces);
    }

    private String serialize(Element e, int intent) {
        try {
            XmlSerializer s = Xml.newSerializer();
            StringWriter sw = new StringWriter();
            s.setOutput(sw);
            s.startDocument("utf-8", null);
            String spaces = null;
            if (intent > 0) {
                char[] chars = new char[intent];
                Arrays.fill(chars, ' ');
                spaces = new String(chars);
            }
            serialize(this.root, s, 0, spaces);
            s.endDocument();
            return sw.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void writeSpace(XmlSerializer s, int depth, String spaces) throws Exception {
        if (spaces != null) {
            s.text("\n");
            for (int i = 0; i < depth; i++) {
                s.text(spaces);
            }
        }
    }

    public String text() {
        NodeList list = this.root.getChildNodes();
        if (list.getLength() == 1) {
            return list.item(0).getNodeValue();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.getLength(); i++) {
            sb.append(text(list.item(i)));
        }
        return sb.toString();
    }

    private String text(Node n) {
        String text = null;
        switch (n.getNodeType()) {
            case (short) 3:
                text = n.getNodeValue();
                if (text != null) {
                    text = text.trim();
                    break;
                }
                break;
            case (short) 4:
                text = n.getNodeValue();
                break;
        }
        if (text == null) {
            return "";
        }
        return text;
    }

    private void serialize(Element e, XmlSerializer s, int depth, String spaces) throws Exception {
        int i;
        String name = e.getTagName();
        writeSpace(s, depth, spaces);
        s.startTag("", name);
        if (e.hasAttributes()) {
            NamedNodeMap nm = e.getAttributes();
            for (i = 0; i < nm.getLength(); i++) {
                Attr attr = (Attr) nm.item(i);
                s.attribute("", attr.getName(), attr.getValue());
            }
        }
        if (e.hasChildNodes()) {
            NodeList nl = e.getChildNodes();
            int elements = 0;
            for (i = 0; i < nl.getLength(); i++) {
                Node n = nl.item(i);
                switch (n.getNodeType()) {
                    case (short) 1:
                        serialize((Element) n, s, depth + 1, spaces);
                        elements++;
                        break;
                    case (short) 3:
                        s.text(text(n));
                        break;
                    case (short) 4:
                        s.cdsect(text(n));
                        break;
                    default:
                        break;
                }
            }
            if (elements > 0) {
                writeSpace(s, depth, spaces);
            }
        }
        s.endTag("", name);
    }
}
