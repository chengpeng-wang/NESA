package org.apache.log4j.lf5.viewer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.apache.http.HttpHeaders;
import org.apache.log4j.Level;
import org.apache.log4j.lf5.LogRecord;
import org.apache.log4j.lf5.LogRecordFilter;
import org.apache.log4j.lf5.PassingLogRecordFilter;

public class FilteredLogTableModel extends AbstractTableModel {
    protected List _allRecords = new ArrayList();
    protected String[] _colNames = new String[]{"Date", "Thread", "Message #", "Level", "NDC", "Category", "Message", HttpHeaders.LOCATION, "Thrown"};
    protected LogRecordFilter _filter = new PassingLogRecordFilter();
    protected List _filteredRecords;
    protected int _maxNumberOfLogRecords = Level.TRACE_INT;

    public void setLogRecordFilter(LogRecordFilter filter) {
        this._filter = filter;
    }

    public LogRecordFilter getLogRecordFilter() {
        return this._filter;
    }

    public String getColumnName(int i) {
        return this._colNames[i];
    }

    public int getColumnCount() {
        return this._colNames.length;
    }

    public int getRowCount() {
        return getFilteredRecords().size();
    }

    public int getTotalRowCount() {
        return this._allRecords.size();
    }

    public Object getValueAt(int row, int col) {
        return getColumn(col, getFilteredRecord(row));
    }

    public void setMaxNumberOfLogRecords(int maxNumRecords) {
        if (maxNumRecords > 0) {
            this._maxNumberOfLogRecords = maxNumRecords;
        }
    }

    public synchronized boolean addLogRecord(LogRecord record) {
        boolean z;
        this._allRecords.add(record);
        if (this._filter.passes(record)) {
            getFilteredRecords().add(record);
            fireTableRowsInserted(getRowCount(), getRowCount());
            trimRecords();
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public synchronized void refresh() {
        this._filteredRecords = createFilteredRecordsList();
        fireTableDataChanged();
    }

    public synchronized void fastRefresh() {
        this._filteredRecords.remove(0);
        fireTableRowsDeleted(0, 0);
    }

    public synchronized void clear() {
        this._allRecords.clear();
        this._filteredRecords.clear();
        fireTableDataChanged();
    }

    /* access modifiers changed from: protected */
    public List getFilteredRecords() {
        if (this._filteredRecords == null) {
            refresh();
        }
        return this._filteredRecords;
    }

    /* access modifiers changed from: protected */
    public List createFilteredRecordsList() {
        List result = new ArrayList();
        for (LogRecord current : this._allRecords) {
            if (this._filter.passes(current)) {
                result.add(current);
            }
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public LogRecord getFilteredRecord(int row) {
        List records = getFilteredRecords();
        int size = records.size();
        if (row < size) {
            return (LogRecord) records.get(row);
        }
        return (LogRecord) records.get(size - 1);
    }

    /* access modifiers changed from: protected */
    public Object getColumn(int col, LogRecord lr) {
        if (lr == null) {
            return "NULL Column";
        }
        String date = new Date(lr.getMillis()).toString();
        switch (col) {
            case 0:
                return new StringBuffer().append(date).append(" (").append(lr.getMillis()).append(")").toString();
            case 1:
                return lr.getThreadDescription();
            case 2:
                return new Long(lr.getSequenceNumber());
            case 3:
                return lr.getLevel();
            case 4:
                return lr.getNDC();
            case 5:
                return lr.getCategory();
            case 6:
                return lr.getMessage();
            case 7:
                return lr.getLocation();
            case 8:
                return lr.getThrownStackTrace();
            default:
                throw new IllegalArgumentException(new StringBuffer().append("The column number ").append(col).append("must be between 0 and 8").toString());
        }
    }

    /* access modifiers changed from: protected */
    public void trimRecords() {
        if (needsTrimming()) {
            trimOldestRecords();
        }
    }

    /* access modifiers changed from: protected */
    public boolean needsTrimming() {
        return this._allRecords.size() > this._maxNumberOfLogRecords;
    }

    /* access modifiers changed from: protected */
    public void trimOldestRecords() {
        synchronized (this._allRecords) {
            int trim = numberOfRecordsToTrim();
            if (trim > 1) {
                this._allRecords.subList(0, trim).clear();
                refresh();
            } else {
                this._allRecords.remove(0);
                fastRefresh();
            }
        }
    }

    private int numberOfRecordsToTrim() {
        return this._allRecords.size() - this._maxNumberOfLogRecords;
    }
}
