package org.jivesoftware.smack.util;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.FormField;
import org.xmlpull.v1.XmlPullParser;

public class PacketParserUtils {
    private static final String PROPERTIES_NAMESPACE = "http://www.jivesoftware.com/xmlns/xmpp/properties";

    public static Packet parseMessage(XmlPullParser parser) throws Exception {
        Message message = new Message();
        String id = parser.getAttributeValue("", "id");
        if (id == null) {
            id = Packet.ID_NOT_AVAILABLE;
        }
        message.setPacketID(id);
        message.setTo(parser.getAttributeValue("", "to"));
        message.setFrom(parser.getAttributeValue("", PrivacyRule.SUBSCRIPTION_FROM));
        message.setType(Type.fromString(parser.getAttributeValue("", "type")));
        String language = getLanguageAttribute(parser);
        if (!(language == null || "".equals(language.trim()))) {
            message.setLanguage(language);
        }
        boolean done = false;
        String subject = null;
        String thread = null;
        Map<String, Object> properties = null;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                String elementName = parser.getName();
                String namespace = parser.getNamespace();
                if (elementName.equals("subject")) {
                    if (subject == null) {
                        subject = parser.nextText();
                    }
                } else if (elementName.equals("body")) {
                    message.addBody(getLanguageAttribute(parser), parser.nextText());
                } else if (elementName.equals("thread")) {
                    if (thread == null) {
                        thread = parser.nextText();
                    }
                } else if (elementName.equals("error")) {
                    message.setError(parseError(parser));
                } else if (elementName.equals("properties") && namespace.equals(PROPERTIES_NAMESPACE)) {
                    properties = parseProperties(parser);
                } else {
                    message.addExtension(parsePacketExtension(elementName, namespace, parser));
                }
            } else if (eventType == 3 && parser.getName().equals("message")) {
                done = true;
            }
        }
        message.setSubject(subject);
        message.setThread(thread);
        if (properties != null) {
            for (String name : properties.keySet()) {
                message.setProperty(name, properties.get(name));
            }
        }
        return message;
    }

    public static Presence parsePresence(XmlPullParser parser) throws Exception {
        String str;
        Presence.Type type = Presence.Type.available;
        String typeString = parser.getAttributeValue("", "type");
        if (!(typeString == null || typeString.equals(""))) {
            try {
                type = Presence.Type.valueOf(typeString);
            } catch (IllegalArgumentException e) {
                System.err.println("Found invalid presence type " + typeString);
            }
        }
        Presence presence = new Presence(type);
        presence.setTo(parser.getAttributeValue("", "to"));
        presence.setFrom(parser.getAttributeValue("", PrivacyRule.SUBSCRIPTION_FROM));
        String id = parser.getAttributeValue("", "id");
        if (id == null) {
            str = Packet.ID_NOT_AVAILABLE;
        } else {
            str = id;
        }
        presence.setPacketID(str);
        String language = getLanguageAttribute(parser);
        if (!(language == null || "".equals(language.trim()))) {
            presence.setLanguage(language);
        }
        if (id == null) {
            id = Packet.ID_NOT_AVAILABLE;
        }
        presence.setPacketID(id);
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                String elementName = parser.getName();
                String namespace = parser.getNamespace();
                if (elementName.equals("status")) {
                    presence.setStatus(parser.nextText());
                } else if (elementName.equals("priority")) {
                    try {
                        presence.setPriority(Integer.parseInt(parser.nextText()));
                    } catch (NumberFormatException e2) {
                    } catch (IllegalArgumentException e3) {
                        presence.setPriority(0);
                    }
                } else if (elementName.equals("show")) {
                    String modeText = parser.nextText();
                    try {
                        presence.setMode(Mode.valueOf(modeText));
                    } catch (IllegalArgumentException e4) {
                        System.err.println("Found invalid presence mode " + modeText);
                    }
                } else if (elementName.equals("error")) {
                    presence.setError(parseError(parser));
                } else if (elementName.equals("properties") && namespace.equals(PROPERTIES_NAMESPACE)) {
                    Map<String, Object> properties = parseProperties(parser);
                    for (String name : properties.keySet()) {
                        presence.setProperty(name, properties.get(name));
                    }
                } else {
                    presence.addExtension(parsePacketExtension(elementName, namespace, parser));
                }
            } else if (eventType == 3 && parser.getName().equals("presence")) {
                done = true;
            }
        }
        return presence;
    }

    public static Map<String, Object> parseProperties(XmlPullParser parser) throws Exception {
        Map<String, Object> properties = new HashMap();
        while (true) {
            int eventType = parser.next();
            if (eventType == 2 && parser.getName().equals("property")) {
                boolean done = false;
                String name = null;
                String type = null;
                String valueText = null;
                Object value = null;
                while (!done) {
                    eventType = parser.next();
                    if (eventType == 2) {
                        String elementName = parser.getName();
                        if (elementName.equals("name")) {
                            name = parser.nextText();
                        } else if (elementName.equals("value")) {
                            type = parser.getAttributeValue("", "type");
                            valueText = parser.nextText();
                        }
                    } else if (eventType == 3 && parser.getName().equals("property")) {
                        if ("integer".equals(type)) {
                            value = new Integer(valueText);
                        } else if ("long".equals(type)) {
                            value = new Long(valueText);
                        } else if ("float".equals(type)) {
                            value = new Float(valueText);
                        } else if ("double".equals(type)) {
                            value = new Double(valueText);
                        } else if (FormField.TYPE_BOOLEAN.equals(type)) {
                            value = Boolean.valueOf(valueText);
                        } else if ("string".equals(type)) {
                            String value2 = valueText;
                        } else if ("java-object".equals(type)) {
                            try {
                                value = new ObjectInputStream(new ByteArrayInputStream(StringUtils.decodeBase64(valueText))).readObject();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!(name == null || value == null)) {
                            properties.put(name, value);
                        }
                        done = true;
                    }
                }
            } else if (eventType == 3 && parser.getName().equals("properties")) {
                return properties;
            }
        }
    }

    public static XMPPError parseError(XmlPullParser parser) throws Exception {
        String errorNamespace = "urn:ietf:params:xml:ns:xmpp-stanzas";
        String errorCode = "-1";
        String type = null;
        String message = null;
        String condition = null;
        List<PacketExtension> extensions = new ArrayList();
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            if (parser.getAttributeName(i).equals("code")) {
                errorCode = parser.getAttributeValue("", "code");
            }
            if (parser.getAttributeName(i).equals("type")) {
                type = parser.getAttributeValue("", "type");
            }
        }
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("text")) {
                    message = parser.nextText();
                } else {
                    String elementName = parser.getName();
                    String namespace = parser.getNamespace();
                    if ("urn:ietf:params:xml:ns:xmpp-stanzas".equals(namespace)) {
                        condition = elementName;
                    } else {
                        extensions.add(parsePacketExtension(elementName, namespace, parser));
                    }
                }
            } else if (eventType == 3 && parser.getName().equals("error")) {
                done = true;
            }
        }
        XMPPError.Type errorType = XMPPError.Type.CANCEL;
        if (type != null) {
            try {
                errorType = XMPPError.Type.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException iae) {
                iae.printStackTrace();
            }
        }
        return new XMPPError(Integer.parseInt(errorCode), errorType, condition, message, extensions);
    }

    public static PacketExtension parsePacketExtension(String elementName, String namespace, XmlPullParser parser) throws Exception {
        Object provider = ProviderManager.getInstance().getExtensionProvider(elementName, namespace);
        if (provider != null) {
            if (provider instanceof PacketExtensionProvider) {
                return ((PacketExtensionProvider) provider).parseExtension(parser);
            }
            if (provider instanceof Class) {
                return (PacketExtension) parseWithIntrospection(elementName, (Class) provider, parser);
            }
        }
        DefaultPacketExtension extension = new DefaultPacketExtension(elementName, namespace);
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                String name = parser.getName();
                if (parser.isEmptyElementTag()) {
                    extension.setValue(name, "");
                } else if (parser.next() == 4) {
                    extension.setValue(name, parser.getText());
                }
            } else if (eventType == 3 && parser.getName().equals(elementName)) {
                done = true;
            }
        }
        return extension;
    }

    private static String getLanguageAttribute(XmlPullParser parser) {
        int i = 0;
        while (i < parser.getAttributeCount()) {
            String attributeName = parser.getAttributeName(i);
            if ("xml:lang".equals(attributeName) || ("lang".equals(attributeName) && "xml".equals(parser.getAttributePrefix(i)))) {
                return parser.getAttributeValue(i);
            }
            i++;
        }
        return null;
    }

    public static Object parseWithIntrospection(String elementName, Class objectClass, XmlPullParser parser) throws Exception {
        return null;
    }

    private static Object decode(Class type, String value) throws Exception {
        if (type.getName().equals("java.lang.String")) {
            return value;
        }
        if (type.getName().equals(FormField.TYPE_BOOLEAN)) {
            return Boolean.valueOf(value);
        }
        if (type.getName().equals("int")) {
            return Integer.valueOf(value);
        }
        if (type.getName().equals("long")) {
            return Long.valueOf(value);
        }
        if (type.getName().equals("float")) {
            return Float.valueOf(value);
        }
        if (type.getName().equals("double")) {
            return Double.valueOf(value);
        }
        if (type.getName().equals("java.lang.Class")) {
            return Class.forName(value);
        }
        return null;
    }
}
