package org.jivesoftware.smackx.provider;

import com.xmpp.client.util.AppPreferences;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.VCard;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class VCardProvider implements IQProvider {
    private static final String PREFERRED_ENCODING = "UTF-8";

    private static class VCardReader {
        private final Document document;
        private final VCard vCard;

        VCardReader(VCard vCard, Document document) {
            this.vCard = vCard;
            this.document = document;
        }

        public void initializeFields() {
            this.vCard.setFirstName(getTagContents("GIVEN"));
            this.vCard.setLastName(getTagContents("FAMILY"));
            this.vCard.setMiddleName(getTagContents("MIDDLE"));
            this.vCard.setEncodedImage(getTagContents("BINVAL"));
            setupEmails();
            this.vCard.setOrganization(getTagContents("ORGNAME"));
            this.vCard.setOrganizationUnit(getTagContents("ORGUNIT"));
            setupSimpleFields();
            setupPhones();
            setupAddresses();
        }

        private void setupEmails() {
            NodeList nodes = this.document.getElementsByTagName(AppPreferences.KEY_PREFS_USERID);
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element element = (Element) nodes.item(i);
                    if ("WORK".equals(element.getParentNode().getFirstChild().getNodeName())) {
                        this.vCard.setEmailWork(getTextContent(element));
                    } else {
                        this.vCard.setEmailHome(getTextContent(element));
                    }
                }
            }
        }

        private void setupPhones() {
            NodeList allPhones = this.document.getElementsByTagName("TEL");
            if (allPhones != null) {
                for (int i = 0; i < allPhones.getLength(); i++) {
                    NodeList nodes = allPhones.item(i).getChildNodes();
                    String type = null;
                    String code = null;
                    String value = null;
                    for (int j = 0; j < nodes.getLength(); j++) {
                        Node node = nodes.item(j);
                        if (node.getNodeType() == (short) 1) {
                            String nodeName = node.getNodeName();
                            if ("NUMBER".equals(nodeName)) {
                                value = getTextContent(node);
                            } else if (isWorkHome(nodeName)) {
                                type = nodeName;
                            } else {
                                code = nodeName;
                            }
                        }
                    }
                    if (!(code == null || value == null)) {
                        if ("HOME".equals(type)) {
                            this.vCard.setPhoneHome(code, value);
                        } else {
                            this.vCard.setPhoneWork(code, value);
                        }
                    }
                }
            }
        }

        private boolean isWorkHome(String nodeName) {
            return "HOME".equals(nodeName) || "WORK".equals(nodeName);
        }

        private void setupAddresses() {
            NodeList allAddresses = this.document.getElementsByTagName("ADR");
            if (allAddresses != null) {
                for (int i = 0; i < allAddresses.getLength(); i++) {
                    int j;
                    Element addressNode = (Element) allAddresses.item(i);
                    String type = null;
                    List code = new ArrayList();
                    List value = new ArrayList();
                    NodeList childNodes = addressNode.getChildNodes();
                    for (j = 0; j < childNodes.getLength(); j++) {
                        Node node = childNodes.item(j);
                        if (node.getNodeType() == (short) 1) {
                            String nodeName = node.getNodeName();
                            if (isWorkHome(nodeName)) {
                                type = nodeName;
                            } else {
                                code.add(nodeName);
                                value.add(getTextContent(node));
                            }
                        }
                    }
                    for (j = 0; j < value.size(); j++) {
                        if ("HOME".equals(type)) {
                            this.vCard.setAddressFieldHome((String) code.get(j), (String) value.get(j));
                        } else {
                            this.vCard.setAddressFieldWork((String) code.get(j), (String) value.get(j));
                        }
                    }
                }
            }
        }

        private String getTagContents(String tag) {
            NodeList nodes = this.document.getElementsByTagName(tag);
            if (nodes == null || nodes.getLength() != 1) {
                return null;
            }
            return getTextContent(nodes.item(0));
        }

        private void setupSimpleFields() {
            NodeList childNodes = this.document.getDocumentElement().getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node instanceof Element) {
                    Element element = (Element) node;
                    String field = element.getNodeName();
                    if (element.getChildNodes().getLength() == 0) {
                        this.vCard.setField(field, "");
                    } else if (element.getChildNodes().getLength() == 1 && (element.getChildNodes().item(0) instanceof Text)) {
                        this.vCard.setField(field, getTextContent(element));
                    }
                }
            }
        }

        private String getTextContent(Node node) {
            StringBuilder result = new StringBuilder();
            appendText(result, node);
            return result.toString();
        }

        private void appendText(StringBuilder result, Node node) {
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node nd = childNodes.item(i);
                String nodeValue = nd.getNodeValue();
                if (nodeValue != null) {
                    result.append(nodeValue);
                }
                appendText(result, nd);
            }
        }
    }

    public IQ parseIQ(XmlPullParser parser) throws Exception {
        StringBuilder sb = new StringBuilder();
        try {
            int event = parser.getEventType();
            while (true) {
                switch (event) {
                    case 2:
                        sb.append('<').append(parser.getName()).append('>');
                        break;
                    case 3:
                        sb.append("</").append(parser.getName()).append('>');
                        break;
                    case 4:
                        sb.append(StringUtils.escapeForXML(parser.getText()));
                        break;
                }
                if (event == 3 && "vCard".equals(parser.getName())) {
                    return createVCardFromXML(sb.toString());
                }
                event = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static VCard createVCardFromXML(String xml) throws Exception {
        VCard vCard = new VCard();
        new VCardReader(vCard, DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes("UTF-8")))).initializeFields();
        return vCard;
    }
}
