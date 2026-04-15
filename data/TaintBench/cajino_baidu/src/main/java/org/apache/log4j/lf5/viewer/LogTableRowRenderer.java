package org.apache.log4j.lf5.viewer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.log4j.lf5.LogLevel;

public class LogTableRowRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = -3951639953706443213L;
    protected Color _color = new Color(230, 230, 230);
    protected boolean _highlightFatal = true;

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        if (row % 2 == 0) {
            setBackground(this._color);
        } else {
            setBackground(Color.white);
        }
        setForeground(getLogLevelColor(((FilteredLogTableModel) table.getModel()).getFilteredRecord(row).getLevel()));
        return LogTableRowRenderer.super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
    }

    /* access modifiers changed from: protected */
    public Color getLogLevelColor(LogLevel level) {
        return (Color) LogLevel.getLogLevelColorMap().get(level);
    }
}
