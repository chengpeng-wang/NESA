package org.MobileDb;

public class Row {
    private Object[] fields = new Object[this.types.length];
    private int[] types;

    public Row(int[] fields_types) {
        this.types = fields_types;
    }

    public int fieldsCount() {
        return this.types.length;
    }

    public int getFieldType(int index) {
        if (index < 0 || index >= this.types.length) {
            return Field.NONE;
        }
        return this.types[index];
    }

    public void setValue(int index, Object value) {
        if (index >= 0 && index < this.fields.length) {
            this.fields[index] = value;
        }
    }

    public Object getValue(int index) {
        if (index < 0 || index >= this.fields.length) {
            return null;
        }
        return this.fields[index];
    }
}
