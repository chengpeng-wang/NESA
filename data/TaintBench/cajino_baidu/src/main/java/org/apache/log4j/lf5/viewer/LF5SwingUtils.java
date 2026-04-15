package org.apache.log4j.lf5.viewer;

import java.awt.Adjustable;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

public class LF5SwingUtils {

    /* renamed from: org.apache.log4j.lf5.viewer.LF5SwingUtils$1 */
    final class AnonymousClass1 implements Runnable {
        private final JComponent val$component;

        AnonymousClass1(JComponent jComponent) {
            this.val$component = jComponent;
        }

        public void run() {
            this.val$component.repaint();
        }
    }

    public static void selectRow(int row, JTable table, JScrollPane pane) {
        if (table != null && pane != null && contains(row, table.getModel())) {
            moveAdjustable(table.getRowHeight() * row, pane.getVerticalScrollBar());
            selectRow(row, table.getSelectionModel());
            repaintLater(table);
        }
    }

    public static void makeScrollBarTrack(Adjustable scrollBar) {
        if (scrollBar != null) {
            scrollBar.addAdjustmentListener(new TrackingAdjustmentListener());
        }
    }

    public static void makeVerticalScrollBarTrack(JScrollPane pane) {
        if (pane != null) {
            makeScrollBarTrack(pane.getVerticalScrollBar());
        }
    }

    protected static boolean contains(int row, TableModel model) {
        if (model != null && row >= 0 && row < model.getRowCount()) {
            return true;
        }
        return false;
    }

    protected static void selectRow(int row, ListSelectionModel model) {
        if (model != null) {
            model.setSelectionInterval(row, row);
        }
    }

    protected static void moveAdjustable(int location, Adjustable scrollBar) {
        if (scrollBar != null) {
            scrollBar.setValue(location);
        }
    }

    protected static void repaintLater(JComponent component) {
        SwingUtilities.invokeLater(new AnonymousClass1(component));
    }
}
