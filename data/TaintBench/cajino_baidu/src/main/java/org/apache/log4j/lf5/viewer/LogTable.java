package org.apache.log4j.lf5.viewer;

import java.awt.Font;
import java.awt.Graphics;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.http.HttpStatus;
import org.apache.log4j.lf5.util.DateFormatManager;

public class LogTable extends JTable {
    private static final long serialVersionUID = 4867085140195148458L;
    protected int _colCategory = 5;
    protected int _colDate = 0;
    protected int _colLevel = 3;
    protected int _colLocation = 7;
    protected int _colMessage = 6;
    protected int _colMessageNum = 2;
    protected int _colNDC = 4;
    protected LogTableColumn[] _colNames = LogTableColumn.getLogTableColumnArray();
    protected int _colThread = 1;
    protected int _colThrown = 8;
    protected int[] _colWidths = new int[]{40, 40, 40, 70, 70, 360, 440, HttpStatus.SC_OK, 60};
    protected DateFormatManager _dateFormatManager = null;
    protected JTextArea _detailTextArea;
    protected int _numCols = 9;
    protected int _rowHeight = 30;
    protected TableColumn[] _tableColumns = new TableColumn[this._numCols];

    class LogTableListSelectionListener implements ListSelectionListener {
        protected JTable _table;
        private final LogTable this$0;

        public LogTableListSelectionListener(LogTable logTable, JTable table) {
            this.this$0 = logTable;
            this._table = table;
        }

        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (!lsm.isSelectionEmpty()) {
                    Object obj;
                    StringBuffer buf = new StringBuffer();
                    int selectedRow = lsm.getMinSelectionIndex();
                    int i = 0;
                    while (i < this.this$0._numCols - 1) {
                        String value = "";
                        obj = this._table.getModel().getValueAt(selectedRow, i);
                        if (obj != null) {
                            value = obj.toString();
                        }
                        buf.append(new StringBuffer().append(this.this$0._colNames[i]).append(":").toString());
                        buf.append("\t");
                        if (i == this.this$0._colThread || i == this.this$0._colMessage || i == this.this$0._colLevel) {
                            buf.append("\t");
                        }
                        if (i == this.this$0._colDate || i == this.this$0._colNDC) {
                            buf.append("\t\t");
                        }
                        buf.append(value);
                        buf.append("\n");
                        i++;
                    }
                    buf.append(new StringBuffer().append(this.this$0._colNames[this.this$0._numCols - 1]).append(":\n").toString());
                    obj = this._table.getModel().getValueAt(selectedRow, this.this$0._numCols - 1);
                    if (obj != null) {
                        buf.append(obj.toString());
                    }
                    this.this$0._detailTextArea.setText(buf.toString());
                }
            }
        }
    }

    public LogTable(JTextArea detailTextArea) {
        init();
        this._detailTextArea = detailTextArea;
        setModel(new FilteredLogTableModel());
        Enumeration columns = getColumnModel().getColumns();
        int i = 0;
        while (columns.hasMoreElements()) {
            TableColumn col = (TableColumn) columns.nextElement();
            col.setCellRenderer(new LogTableRowRenderer());
            col.setPreferredWidth(this._colWidths[i]);
            this._tableColumns[i] = col;
            i++;
        }
        getSelectionModel().addListSelectionListener(new LogTableListSelectionListener(this, this));
    }

    public DateFormatManager getDateFormatManager() {
        return this._dateFormatManager;
    }

    public void setDateFormatManager(DateFormatManager dfm) {
        this._dateFormatManager = dfm;
    }

    public synchronized void clearLogRecords() {
        getFilteredLogTableModel().clear();
    }

    public FilteredLogTableModel getFilteredLogTableModel() {
        return (FilteredLogTableModel) getModel();
    }

    public void setDetailedView() {
        TableColumnModel model = getColumnModel();
        for (int f = 0; f < this._numCols; f++) {
            model.removeColumn(this._tableColumns[f]);
        }
        for (int i = 0; i < this._numCols; i++) {
            model.addColumn(this._tableColumns[i]);
        }
        sizeColumnsToFit(-1);
    }

    public void setView(List columns) {
        TableColumnModel model = getColumnModel();
        for (int f = 0; f < this._numCols; f++) {
            model.removeColumn(this._tableColumns[f]);
        }
        Vector columnNameAndNumber = getColumnNameAndNumber();
        for (Object indexOf : columns) {
            model.addColumn(this._tableColumns[columnNameAndNumber.indexOf(indexOf)]);
        }
        sizeColumnsToFit(-1);
    }

    public void setFont(Font font) {
        LogTable.super.setFont(font);
        Graphics g = getGraphics();
        if (g != null) {
            int height = g.getFontMetrics(font).getHeight();
            this._rowHeight = (height / 3) + height;
            setRowHeight(this._rowHeight);
        }
    }

    /* access modifiers changed from: protected */
    public void init() {
        setRowHeight(this._rowHeight);
        setSelectionMode(0);
    }

    /* access modifiers changed from: protected */
    public Vector getColumnNameAndNumber() {
        Vector columnNameAndNumber = new Vector();
        for (int i = 0; i < this._colNames.length; i++) {
            columnNameAndNumber.add(i, this._colNames[i]);
        }
        return columnNameAndNumber;
    }
}
