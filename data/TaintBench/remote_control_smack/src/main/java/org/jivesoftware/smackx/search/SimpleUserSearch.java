package org.jivesoftware.smackx.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Column;
import org.jivesoftware.smackx.ReportedData.Field;
import org.jivesoftware.smackx.ReportedData.Row;
import org.xmlpull.v1.XmlPullParser;

class SimpleUserSearch extends IQ {
    private ReportedData data;
    private Form form;

    SimpleUserSearch() {
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public ReportedData getReportedData() {
        return this.data;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"jabber:iq:search\">");
        buf.append(getItemsToSearch());
        buf.append("</query>");
        return buf.toString();
    }

    private String getItemsToSearch() {
        StringBuilder buf = new StringBuilder();
        if (this.form == null) {
            this.form = Form.getFormFrom(this);
        }
        if (this.form == null) {
            return "";
        }
        Iterator<FormField> fields = this.form.getFields();
        while (fields.hasNext()) {
            FormField field = (FormField) fields.next();
            String name = field.getVariable();
            String value = getSingleValue(field);
            if (value.trim().length() > 0) {
                buf.append("<").append(name).append(">").append(value).append("</").append(name).append(">");
            }
        }
        return buf.toString();
    }

    private static String getSingleValue(FormField formField) {
        Iterator<String> values = formField.getValues();
        if (values.hasNext()) {
            return (String) values.next();
        }
        return "";
    }

    /* access modifiers changed from: protected */
    public void parseItems(XmlPullParser parser) throws Exception {
        ReportedData data = new ReportedData();
        data.addColumn(new Column("JID", "jid", FormField.TYPE_TEXT_SINGLE));
        boolean done = false;
        List<Field> fields = new ArrayList();
        while (!done) {
            List<String> valueList;
            if (parser.getAttributeCount() > 0) {
                String jid = parser.getAttributeValue("", "jid");
                valueList = new ArrayList();
                valueList.add(jid);
                fields.add(new Field("jid", valueList));
            }
            int eventType = parser.next();
            if (eventType == 2 && parser.getName().equals("item")) {
                fields = new ArrayList();
            } else if (eventType == 3 && parser.getName().equals("item")) {
                data.addRow(new Row(fields));
            } else if (eventType == 2) {
                String name = parser.getName();
                String value = parser.nextText();
                valueList = new ArrayList();
                valueList.add(value);
                fields.add(new Field(name, valueList));
                boolean exists = false;
                Iterator cols = data.getColumns();
                while (cols.hasNext()) {
                    if (((Column) cols.next()).getVariable().equals(name)) {
                        exists = true;
                    }
                }
                if (!exists) {
                    data.addColumn(new Column(name, name, FormField.TYPE_TEXT_SINGLE));
                }
            } else if (eventType == 3 && parser.getName().equals("query")) {
                done = true;
            }
        }
        this.data = data;
    }
}
