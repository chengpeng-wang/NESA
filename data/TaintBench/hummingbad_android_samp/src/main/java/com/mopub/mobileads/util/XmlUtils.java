package com.mopub.mobileads.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtils {

    public interface NodeProcessor<T> {
        T process(Node node);
    }

    private XmlUtils() {
    }

    public static Node getFirstMatchingChildNode(Node node, String nodeName) {
        return getFirstMatchingChildNode(node, nodeName, null, null);
    }

    public static Node getFirstMatchingChildNode(Node node, String nodeName, String attributeName, List<String> attributeValues) {
        if (node == null || nodeName == null) {
            return null;
        }
        List matchingChildNodes = getMatchingChildNodes(node, nodeName, attributeName, attributeValues);
        if (matchingChildNodes == null || matchingChildNodes.isEmpty()) {
            return null;
        }
        return (Node) matchingChildNodes.get(0);
    }

    public static List<Node> getMatchingChildNodes(Node node, String nodeName) {
        return getMatchingChildNodes(node, nodeName, null, null);
    }

    public static List<Node> getMatchingChildNodes(Node node, String nodeName, String attributeName, List<String> attributeValues) {
        if (node == null || nodeName == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeName().equals(nodeName) && nodeMatchesAttributeFilter(item, attributeName, attributeValues)) {
                arrayList.add(item);
            }
        }
        return arrayList;
    }

    public static boolean nodeMatchesAttributeFilter(Node node, String attributeName, List<String> attributeValues) {
        if (attributeName == null || attributeValues == null) {
            return true;
        }
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            Node namedItem = attributes.getNamedItem(attributeName);
            if (namedItem != null && attributeValues.contains(namedItem.getNodeValue())) {
                return true;
            }
        }
        return false;
    }

    public static String getNodeValue(Node node) {
        if (node == null || node.getFirstChild() == null || node.getFirstChild().getNodeValue() == null) {
            return null;
        }
        return node.getFirstChild().getNodeValue().trim();
    }

    public static Integer getAttributeValueAsInt(Node node, String attributeName) {
        Integer num = null;
        if (node == null || attributeName == null) {
            return num;
        }
        try {
            return Integer.valueOf(Integer.parseInt(getAttributeValue(node, attributeName)));
        } catch (NumberFormatException e) {
            return num;
        }
    }

    public static String getAttributeValue(Node node, String attributeName) {
        if (node == null || attributeName == null) {
            return null;
        }
        Node namedItem = node.getAttributes().getNamedItem(attributeName);
        if (namedItem != null) {
            return namedItem.getNodeValue();
        }
        return null;
    }

    public static <T> List<T> getListFromDocument(Document vastDoc, String elementName, String attributeName, String attributeValue, NodeProcessor<T> nodeProcessor) {
        int i = 0;
        ArrayList arrayList = new ArrayList();
        if (vastDoc == null) {
            return arrayList;
        }
        NodeList elementsByTagName = vastDoc.getElementsByTagName(elementName);
        if (elementsByTagName == null) {
            return arrayList;
        }
        List list;
        if (attributeValue == null) {
            list = null;
        } else {
            list = Arrays.asList(new String[]{attributeValue});
        }
        while (i < elementsByTagName.getLength()) {
            Node item = elementsByTagName.item(i);
            if (item != null && nodeMatchesAttributeFilter(item, attributeName, list)) {
                Object process = nodeProcessor.process(item);
                if (process != null) {
                    arrayList.add(process);
                }
            }
            i++;
        }
        return arrayList;
    }

    public static <T> T getFirstMatchFromDocument(Document vastDoc, String elementName, String attributeName, String attributeValue, NodeProcessor<T> nodeProcessor) {
        int i = 0;
        if (vastDoc == null) {
            return null;
        }
        NodeList elementsByTagName = vastDoc.getElementsByTagName(elementName);
        if (elementsByTagName == null) {
            return null;
        }
        List list;
        if (attributeValue == null) {
            list = null;
        } else {
            list = Arrays.asList(new String[]{attributeValue});
        }
        while (i < elementsByTagName.getLength()) {
            Node item = elementsByTagName.item(i);
            if (item != null && nodeMatchesAttributeFilter(item, attributeName, list)) {
                T process = nodeProcessor.process(item);
                if (process != null) {
                    return process;
                }
            }
            i++;
        }
        return null;
    }

    public static String getFirstMatchingStringData(Document vastDoc, String elementName) {
        return getFirstMatchingStringData(vastDoc, elementName, null, null);
    }

    public static String getFirstMatchingStringData(Document vastDoc, String elementName, String attributeName, String attributeValue) {
        return (String) getFirstMatchFromDocument(vastDoc, elementName, attributeName, attributeValue, new NodeProcessor<String>() {
            public final String process(Node node) {
                return XmlUtils.getNodeValue(node);
            }
        });
    }

    public static List<String> getStringDataAsList(Document vastDoc, String elementName) {
        return getStringDataAsList(vastDoc, elementName, null, null);
    }

    public static List<String> getStringDataAsList(Document vastDoc, String elementName, String attributeName, String attributeValue) {
        return getListFromDocument(vastDoc, elementName, attributeName, attributeValue, new NodeProcessor<String>() {
            public final String process(Node node) {
                return XmlUtils.getNodeValue(node);
            }
        });
    }

    public static List<Node> getNodesWithElementAndAttribute(Document vastDoc, String elementName, String attributeName, String attributeValue) {
        return getListFromDocument(vastDoc, elementName, attributeName, attributeValue, new NodeProcessor<Node>() {
            public final Node process(Node node) {
                return node;
            }
        });
    }
}
