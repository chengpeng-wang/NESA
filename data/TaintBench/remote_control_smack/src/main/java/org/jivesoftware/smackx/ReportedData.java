package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.DataForm.Item;

public class ReportedData {
    private List<Column> columns = new ArrayList();
    private List<Row> rows = new ArrayList();
    private String title = "";

    public static class Column {
        private String label;
        private String type;
        private String variable;

        public Column(String label, String variable, String type) {
            this.label = label;
            this.variable = variable;
            this.type = type;
        }

        public String getLabel() {
            return this.label;
        }

        public String getType() {
            return this.type;
        }

        public String getVariable() {
            return this.variable;
        }
    }

    public static class Field {
        private List<String> values;
        private String variable;

        public Field(String variable, List<String> values) {
            this.variable = variable;
            this.values = values;
        }

        public String getVariable() {
            return this.variable;
        }

        public Iterator<String> getValues() {
            return Collections.unmodifiableList(this.values).iterator();
        }
    }

    public static class Row {
        private List<Field> fields = new ArrayList();

        public Row(List<Field> fields) {
            this.fields = fields;
        }

        public Iterator getValues(String variable) {
            Iterator<Field> it = getFields();
            while (it.hasNext()) {
                Field field = (Field) it.next();
                if (variable.equalsIgnoreCase(field.getVariable())) {
                    return field.getValues();
                }
            }
            return null;
        }

        private Iterator<Field> getFields() {
            return Collections.unmodifiableList(new ArrayList(this.fields)).iterator();
        }
    }

    public static ReportedData getReportedDataFrom(Packet packet) {
        PacketExtension packetExtension = packet.getExtension(GroupChatInvitation.ELEMENT_NAME, "jabber:x:data");
        if (packetExtension != null) {
            DataForm dataForm = (DataForm) packetExtension;
            if (dataForm.getReportedData() != null) {
                return new ReportedData(dataForm);
            }
        }
        return null;
    }

    private ReportedData(DataForm dataForm) {
        FormField field;
        Iterator fields = dataForm.getReportedData().getFields();
        while (fields.hasNext()) {
            field = (FormField) fields.next();
            this.columns.add(new Column(field.getLabel(), field.getVariable(), field.getType()));
        }
        Iterator items = dataForm.getItems();
        while (items.hasNext()) {
            Item item = (Item) items.next();
            List<Field> fieldList = new ArrayList(this.columns.size());
            fields = item.getFields();
            while (fields.hasNext()) {
                field = (FormField) fields.next();
                List<String> values = new ArrayList();
                Iterator<String> it = field.getValues();
                while (it.hasNext()) {
                    values.add(it.next());
                }
                fieldList.add(new Field(field.getVariable(), values));
            }
            this.rows.add(new Row(fieldList));
        }
        this.title = dataForm.getTitle();
    }

    public void addRow(Row row) {
        this.rows.add(row);
    }

    public void addColumn(Column column) {
        this.columns.add(column);
    }

    public Iterator<Row> getRows() {
        return Collections.unmodifiableList(new ArrayList(this.rows)).iterator();
    }

    public Iterator<Column> getColumns() {
        return Collections.unmodifiableList(new ArrayList(this.columns)).iterator();
    }

    public String getTitle() {
        return this.title;
    }
}
