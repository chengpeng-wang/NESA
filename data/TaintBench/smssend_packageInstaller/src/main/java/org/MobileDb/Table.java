package org.MobileDb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class Table {
    private long _offset;
    private Vector fields;
    private InputStream inputStream;
    private int last_find_index;
    private Row last_find_row;
    private boolean loadAllDataInMemory;
    public String name;
    private Field[] opt_fields;
    private Row[] opt_rows;
    private boolean optimized;
    private String pathToDb;
    private Vector rows;
    private int rowsCount;
    private boolean transaction;

    public Table() {
        this.optimized = false;
        this.opt_fields = null;
        this.opt_rows = null;
        this.last_find_index = -1;
        this.last_find_row = null;
        this.loadAllDataInMemory = true;
        this.pathToDb = null;
        this.transaction = false;
        this.inputStream = null;
        this.rowsCount = 0;
        this._offset = -1;
        this.name = "";
        this.fields = new Vector();
        this.rows = new Vector();
    }

    public Table(String tableName) {
        this.optimized = false;
        this.opt_fields = null;
        this.opt_rows = null;
        this.last_find_index = -1;
        this.last_find_row = null;
        this.loadAllDataInMemory = true;
        this.pathToDb = null;
        this.transaction = false;
        this.inputStream = null;
        this.rowsCount = 0;
        this._offset = -1;
        this.name = tableName;
        this.fields = new Vector();
        this.rows = new Vector();
    }

    public Table(String tableName, boolean loadAllDataInMemory, String path) {
        this.optimized = false;
        this.opt_fields = null;
        this.opt_rows = null;
        this.last_find_index = -1;
        this.last_find_row = null;
        this.loadAllDataInMemory = true;
        this.pathToDb = null;
        this.transaction = false;
        this.inputStream = null;
        this.rowsCount = 0;
        this._offset = -1;
        this.name = tableName;
        this.fields = new Vector();
        this.loadAllDataInMemory = loadAllDataInMemory;
        if (loadAllDataInMemory) {
            this.rows = new Vector();
            this.pathToDb = null;
            return;
        }
        this.pathToDb = path;
    }

    public void addField(Field field) {
        if (!this.optimized) {
            this.fields.addElement(field);
        }
    }

    public Field getField(int index) {
        if (this.optimized) {
            if (index >= 0 && index < this.opt_fields.length) {
                return this.opt_fields[index];
            }
        } else if (index >= 0 && index < this.fields.size()) {
            return (Field) this.fields.elementAt(index);
        }
        return null;
    }

    public void removeField(Field field) {
        if (!this.optimized) {
            this.fields.removeElement(field);
        }
    }

    public int fieldsCount() {
        if (this.optimized) {
            return this.opt_fields.length;
        }
        return this.fields.size();
    }

    public void removeAllFields() {
        if (!this.optimized) {
            this.fields.removeAllElements();
        }
    }

    public Row createRow() {
        if (this.optimized) {
            return null;
        }
        int[] types = new int[this.fields.size()];
        for (int i = 0; i < this.fields.size(); i++) {
            types[i] = ((Field) this.fields.elementAt(i)).type;
        }
        return new Row(types);
    }

    public void addRow(Row row) {
        if (!this.optimized) {
            this.rows.addElement(row);
        }
    }

    public void removeRow(Row row) {
        if (!this.optimized) {
            this.rows.removeElement(row);
        }
    }

    public int rowsCount() {
        if (!this.loadAllDataInMemory) {
            return this.rowsCount;
        }
        if (this.optimized) {
            return this.opt_rows.length;
        }
        return this.rows.size();
    }

    public Row getRow(int index) {
        if (this.loadAllDataInMemory) {
            if (this.optimized) {
                if (index >= 0 && index < this.opt_rows.length) {
                    this.last_find_row = this.opt_rows[index];
                    this.last_find_index = index;
                    return this.last_find_row;
                }
            } else if (index >= 0 && index < this.rows.size()) {
                this.last_find_row = (Row) this.rows.elementAt(index);
                this.last_find_index = index;
                return this.last_find_row;
            }
        } else if (index >= 0 && index < this.rowsCount) {
            this.last_find_index = index;
            if (this.transaction) {
                InputStream stream = this.inputStream;
                try {
                    if (stream.read() == 11) {
                        Row row = createRow();
                        for (int i = 0; i < row.fieldsCount(); i++) {
                            int type = row.getFieldType(i);
                            byte[] tmp;
                            if (type == Field.SMALL_INT) {
                                row.setValue(i, new Integer(stream.read()));
                            } else if (type == Field.SHORT_INT) {
                                tmp = new byte[2];
                                MobileDatabase.readDataFromStream(stream, tmp);
                                row.setValue(i, new Integer(MobileDatabase.shortIntFromBytes(tmp)));
                            } else if (type == Field.INT) {
                                tmp = new byte[4];
                                MobileDatabase.readDataFromStream(stream, tmp);
                                row.setValue(i, new Integer(MobileDatabase.intFromBytes(tmp)));
                            } else if (type == Field.TIME) {
                                tmp = new byte[4];
                                MobileDatabase.readDataFromStream(stream, tmp);
                                row.setValue(i, new Integer(MobileDatabase.intFromBytes(tmp)));
                            } else if (type == Field.NAME) {
                                tmp = new byte[stream.read()];
                                MobileDatabase.readDataFromStream(stream, tmp);
                                row.setValue(i, MobileDatabase.getUtf8String(tmp));
                            } else if (type == Field.TEXT) {
                                tmp = new byte[2];
                                MobileDatabase.readDataFromStream(stream, tmp);
                                tmp = new byte[MobileDatabase.shortIntFromBytes(tmp)];
                                MobileDatabase.readDataFromStream(stream, tmp);
                                row.setValue(i, MobileDatabase.getUtf8String(tmp));
                            } else if (type == Field.BINARY) {
                                tmp = new byte[4];
                                MobileDatabase.readDataFromStream(stream, tmp);
                                tmp = new byte[MobileDatabase.intFromBytes(tmp)];
                                MobileDatabase.readDataFromStream(stream, tmp);
                                row.setValue(i, tmp);
                            }
                        }
                        this.last_find_row = row;
                        return this.last_find_row;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public void removeAllRows() {
        if (this.loadAllDataInMemory && !this.optimized) {
            this.rows.removeAllElements();
        }
    }

    public boolean isOptimized() {
        return this.optimized;
    }

    public void optimize() {
        if (this.loadAllDataInMemory && !this.optimized) {
            this.opt_fields = new Field[this.fields.size()];
            this.fields.copyInto(this.opt_fields);
            this.opt_rows = new Row[this.rows.size()];
            this.rows.copyInto(this.opt_rows);
            this.optimized = true;
            this.fields = null;
            this.rows = null;
            System.gc();
        }
    }

    public Object getFieldValueByName(String name, int index) {
        int i;
        if (this.loadAllDataInMemory) {
            if (this.optimized) {
                if (this.last_find_index != index && getRow(index) == null) {
                    return null;
                }
                for (i = 0; i < this.opt_fields.length; i++) {
                    if (name.equals(this.opt_fields[i].name)) {
                        return this.last_find_row.getValue(i);
                    }
                }
            } else if (this.last_find_index != index && getRow(index) == null) {
                return null;
            } else {
                for (i = 0; i < this.fields.size(); i++) {
                    if (name.equals(((Field) this.fields.elementAt(i)).name)) {
                        return this.last_find_row.getValue(i);
                    }
                }
            }
        } else if (this.last_find_index != index && getRow(index) == null) {
            return null;
        } else {
            for (i = 0; i < this.fields.size(); i++) {
                if (name.equals(((Field) this.fields.elementAt(i)).name)) {
                    return this.last_find_row.getValue(i);
                }
            }
        }
        return null;
    }

    public void addRow() {
        this.rowsCount++;
    }

    public void setOffset(long offset) {
        this._offset = offset - 1;
    }

    public long getOffset() {
        return this._offset;
    }

    public void startTransaction() {
        if (!this.loadAllDataInMemory && this.rowsCount != 0) {
            try {
                this.inputStream = getClass().getResourceAsStream(this.pathToDb);
                this.inputStream.skip(this._offset);
                this.transaction = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void stopTransaction() {
        if (!this.loadAllDataInMemory) {
            try {
                this.inputStream.close();
            } catch (IOException e) {
            }
            this.transaction = true;
        }
    }
}
