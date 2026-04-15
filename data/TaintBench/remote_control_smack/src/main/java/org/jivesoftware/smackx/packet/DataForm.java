package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.GroupChatInvitation;

public class DataForm implements PacketExtension {
    private final List<FormField> fields = new ArrayList();
    private List<String> instructions = new ArrayList();
    private final List<Item> items = new ArrayList();
    private ReportedData reportedData;
    private String title;
    private String type;

    public static class Item {
        private List<FormField> fields = new ArrayList();

        public Item(List<FormField> fields) {
            this.fields = fields;
        }

        public Iterator<FormField> getFields() {
            return Collections.unmodifiableList(new ArrayList(this.fields)).iterator();
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<item>");
            Iterator i = getFields();
            while (i.hasNext()) {
                buf.append(((FormField) i.next()).toXML());
            }
            buf.append("</item>");
            return buf.toString();
        }
    }

    public static class ReportedData {
        private List<FormField> fields = new ArrayList();

        public ReportedData(List<FormField> fields) {
            this.fields = fields;
        }

        public Iterator<FormField> getFields() {
            return Collections.unmodifiableList(new ArrayList(this.fields)).iterator();
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<reported>");
            Iterator i = getFields();
            while (i.hasNext()) {
                buf.append(((FormField) i.next()).toXML());
            }
            buf.append("</reported>");
            return buf.toString();
        }
    }

    public DataForm(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public String getTitle() {
        return this.title;
    }

    public Iterator<String> getInstructions() {
        Iterator it;
        synchronized (this.instructions) {
            it = Collections.unmodifiableList(new ArrayList(this.instructions)).iterator();
        }
        return it;
    }

    public ReportedData getReportedData() {
        return this.reportedData;
    }

    public Iterator<Item> getItems() {
        Iterator it;
        synchronized (this.items) {
            it = Collections.unmodifiableList(new ArrayList(this.items)).iterator();
        }
        return it;
    }

    public Iterator<FormField> getFields() {
        Iterator it;
        synchronized (this.fields) {
            it = Collections.unmodifiableList(new ArrayList(this.fields)).iterator();
        }
        return it;
    }

    public String getElementName() {
        return GroupChatInvitation.ELEMENT_NAME;
    }

    public String getNamespace() {
        return "jabber:x:data";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public void setReportedData(ReportedData reportedData) {
        this.reportedData = reportedData;
    }

    public void addField(FormField field) {
        synchronized (this.fields) {
            this.fields.add(field);
        }
    }

    public void addInstruction(String instruction) {
        synchronized (this.instructions) {
            this.instructions.add(instruction);
        }
    }

    public void addItem(Item item) {
        synchronized (this.items) {
            this.items.add(item);
        }
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\" type=\"" + getType() + "\">");
        if (getTitle() != null) {
            buf.append("<title>").append(getTitle()).append("</title>");
        }
        Iterator it = getInstructions();
        while (it.hasNext()) {
            buf.append("<instructions>").append(it.next()).append("</instructions>");
        }
        if (getReportedData() != null) {
            buf.append(getReportedData().toXML());
        }
        Iterator i = getItems();
        while (i.hasNext()) {
            buf.append(((Item) i.next()).toXML());
        }
        i = getFields();
        while (i.hasNext()) {
            buf.append(((FormField) i.next()).toXML());
        }
        buf.append("</").append(getElementName()).append(">");
        return buf.toString();
    }
}
