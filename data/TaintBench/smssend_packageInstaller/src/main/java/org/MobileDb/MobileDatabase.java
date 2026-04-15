package org.MobileDb;

import android.support.v4.view.MotionEventCompat;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class MobileDatabase {
    private static boolean useNativeUtf8Decoder = true;
    public int Version = 0;
    private boolean loadAllDataInMemory = true;
    private String path = null;
    private Vector tables = new Vector();

    public MobileDatabase() {
        useNativeUtf8Decoder = isSupportUtf8();
    }

    public void loadFrom(String path) throws IOException {
        this.loadAllDataInMemory = true;
        this.path = path;
        loadFrom(getClass().getResourceAsStream(path));
    }

    public void loadFrom(String path, boolean loadAllDataInMemory) throws IOException {
        this.loadAllDataInMemory = loadAllDataInMemory;
        this.path = path;
        loadFrom(getClass().getResourceAsStream(path));
    }

    public void loadFrom(InputStream stream) throws IOException {
        byte[] data = new byte[4];
        readDataFromStream(stream, data);
        long position = 0 + 4;
        this.Version = stream.read();
        position++;
        Table table = null;
        while (true) {
            long value = (long) stream.read();
            if (value != -1) {
                position++;
                int length;
                int type;
                if (value == 9) {
                    length = stream.read();
                    position++;
                    data = new byte[length];
                    readDataFromStream(stream, data);
                    position += (long) length;
                    table = new Table(getUtf8String(data), this.loadAllDataInMemory, this.path);
                    this.tables.addElement(table);
                } else if (value == 10) {
                    type = stream.read();
                    position++;
                    length = stream.read();
                    position++;
                    data = new byte[length];
                    readDataFromStream(stream, data);
                    position += (long) length;
                    table.addField(new Field(type, getUtf8String(data)));
                } else if (value == 11) {
                    long rowOffset = position;
                    Row row = table.createRow();
                    for (int i = 0; i < row.fieldsCount(); i++) {
                        type = row.getFieldType(i);
                        byte[] tmp;
                        if (type == Field.SMALL_INT) {
                            position++;
                            row.setValue(i, new Integer(stream.read()));
                        } else if (type == Field.SHORT_INT) {
                            tmp = new byte[2];
                            readDataFromStream(stream, tmp);
                            position += 2;
                            row.setValue(i, new Integer(shortIntFromBytes(tmp)));
                        } else if (type == Field.INT) {
                            tmp = new byte[4];
                            readDataFromStream(stream, tmp);
                            position += 4;
                            row.setValue(i, new Integer(intFromBytes(tmp)));
                        } else if (type == Field.TIME) {
                            tmp = new byte[4];
                            readDataFromStream(stream, tmp);
                            position += 4;
                            row.setValue(i, new Integer(intFromBytes(tmp)));
                        } else if (type == Field.NAME) {
                            length = stream.read();
                            position++;
                            tmp = new byte[length];
                            readDataFromStream(stream, tmp);
                            position += (long) length;
                            row.setValue(i, getUtf8String(tmp));
                        } else if (type == Field.TEXT) {
                            tmp = new byte[2];
                            readDataFromStream(stream, tmp);
                            position += 2;
                            length = shortIntFromBytes(tmp);
                            tmp = new byte[length];
                            readDataFromStream(stream, tmp);
                            position += (long) length;
                            row.setValue(i, getUtf8String(tmp));
                        } else if (type == Field.BINARY) {
                            tmp = new byte[4];
                            readDataFromStream(stream, tmp);
                            position += 4;
                            length = intFromBytes(tmp);
                            tmp = new byte[length];
                            readDataFromStream(stream, data);
                            position += (long) length;
                            row.setValue(i, tmp);
                        }
                    }
                    if (this.loadAllDataInMemory) {
                        table.addRow(row);
                    } else {
                        if (table.getOffset() == -1) {
                            table.setOffset(rowOffset);
                        }
                        table.addRow();
                    }
                }
            } else {
                stream.close();
                return;
            }
        }
    }

    public Table getTable(int index) {
        if (index < 0 || index >= this.tables.size()) {
            return null;
        }
        return (Table) this.tables.elementAt(index);
    }

    public Table getTableByName(String name) {
        for (int i = 0; i < this.tables.size(); i++) {
            Table table = (Table) this.tables.elementAt(i);
            if (table.name.equals(name)) {
                return table;
            }
        }
        return null;
    }

    public int tablesCount() {
        return this.tables.size();
    }

    public void optimize() {
        for (int i = 0; i < this.tables.size(); i++) {
            ((Table) this.tables.elementAt(i)).optimize();
        }
        System.gc();
    }

    public static int shortIntFromBytes(byte[] arr) {
        return ((arr[1] & MotionEventCompat.ACTION_MASK) << 8) + (arr[0] & MotionEventCompat.ACTION_MASK);
    }

    public static int intFromBytes(byte[] arr) {
        return ((((arr[3] & MotionEventCompat.ACTION_MASK) << 24) + ((arr[2] & MotionEventCompat.ACTION_MASK) << 16)) + ((arr[1] & MotionEventCompat.ACTION_MASK) << 8)) + (arr[0] & MotionEventCompat.ACTION_MASK);
    }

    public static boolean isSupportUtf8() {
        try {
            String str = new String(new byte[]{(byte) 50, (byte) 51}, "utf-8");
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    public static String getUtf8String(byte[] data) {
        if (useNativeUtf8Decoder) {
            try {
                return new String(data, "utf-8");
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }
        Utf8StringBuffer buffer = new Utf8StringBuffer();
        buffer.append(data, 0, data.length);
        return buffer.toString();
    }

    public static void readDataFromStream(InputStream stream, byte[] data) throws IOException {
        int pos = 0;
        int length = data.length;
        while (true) {
            int read = stream.read(data, pos, length);
            length -= read;
            if (length != 0) {
                pos += read;
            } else {
                return;
            }
        }
    }
}
