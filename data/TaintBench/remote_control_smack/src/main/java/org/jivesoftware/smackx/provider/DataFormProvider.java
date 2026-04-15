package org.jivesoftware.smackx.provider;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.FormField.Option;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.DataForm.Item;
import org.jivesoftware.smackx.packet.DataForm.ReportedData;
import org.xmlpull.v1.XmlPullParser;

public class DataFormProvider implements PacketExtensionProvider {
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        boolean done = false;
        DataForm dataForm = new DataForm(parser.getAttributeValue("", "type"));
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("instructions")) {
                    dataForm.addInstruction(parser.nextText());
                } else if (parser.getName().equals("title")) {
                    dataForm.setTitle(parser.nextText());
                } else if (parser.getName().equals("field")) {
                    dataForm.addField(parseField(parser));
                } else if (parser.getName().equals("item")) {
                    dataForm.addItem(parseItem(parser));
                } else if (parser.getName().equals("reported")) {
                    dataForm.setReportedData(parseReported(parser));
                }
            } else if (eventType == 3 && parser.getName().equals(dataForm.getElementName())) {
                done = true;
            }
        }
        return dataForm;
    }

    private FormField parseField(XmlPullParser parser) throws Exception {
        boolean done = false;
        FormField formField = new FormField(parser.getAttributeValue("", "var"));
        formField.setLabel(parser.getAttributeValue("", "label"));
        formField.setType(parser.getAttributeValue("", "type"));
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("desc")) {
                    formField.setDescription(parser.nextText());
                } else if (parser.getName().equals("value")) {
                    formField.addValue(parser.nextText());
                } else if (parser.getName().equals("required")) {
                    formField.setRequired(true);
                } else if (parser.getName().equals("option")) {
                    formField.addOption(parseOption(parser));
                }
            } else if (eventType == 3 && parser.getName().equals("field")) {
                done = true;
            }
        }
        return formField;
    }

    private Item parseItem(XmlPullParser parser) throws Exception {
        boolean done = false;
        List fields = new ArrayList();
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("field")) {
                    fields.add(parseField(parser));
                }
            } else if (eventType == 3 && parser.getName().equals("item")) {
                done = true;
            }
        }
        return new Item(fields);
    }

    private ReportedData parseReported(XmlPullParser parser) throws Exception {
        boolean done = false;
        List fields = new ArrayList();
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("field")) {
                    fields.add(parseField(parser));
                }
            } else if (eventType == 3 && parser.getName().equals("reported")) {
                done = true;
            }
        }
        return new ReportedData(fields);
    }

    private Option parseOption(XmlPullParser parser) throws Exception {
        boolean done = false;
        Option option = null;
        String label = parser.getAttributeValue("", "label");
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("value")) {
                    option = new Option(label, parser.nextText());
                }
            } else if (eventType == 3 && parser.getName().equals("option")) {
                done = true;
            }
        }
        return option;
    }
}
