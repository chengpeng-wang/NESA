package org.apache.log4j.lf5.viewer.categoryexplorer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

public class CategoryNodeRenderer extends DefaultTreeCellRenderer {
    public static final Color FATAL_CHILDREN = new Color(189, 113, 0);
    protected static ImageIcon _sat = null;
    private static final long serialVersionUID = -6046702673278595048L;
    protected JCheckBox _checkBox = new JCheckBox();
    protected JPanel _panel = new JPanel();

    public CategoryNodeRenderer() {
        this._panel.setBackground(UIManager.getColor("Tree.textBackground"));
        if (_sat == null) {
            _sat = new ImageIcon(getClass().getResource("/org/apache/log4j/lf5/viewer/images/channelexplorer_satellite.gif"));
        }
        setOpaque(false);
        this._checkBox.setOpaque(false);
        this._panel.setOpaque(false);
        this._panel.setLayout(new FlowLayout(0, 0, 0));
        this._panel.add(this._checkBox);
        this._panel.add(this);
        setOpenIcon(_sat);
        setClosedIcon(_sat);
        setLeafIcon(_sat);
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        CategoryNode node = (CategoryNode) value;
        CategoryNodeRenderer.super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (row == 0) {
            this._checkBox.setVisible(false);
        } else {
            this._checkBox.setVisible(true);
            this._checkBox.setSelected(node.isSelected());
        }
        this._panel.setToolTipText(buildToolTip(node));
        if (node.hasFatalChildren()) {
            setForeground(FATAL_CHILDREN);
        }
        if (node.hasFatalRecords()) {
            setForeground(Color.red);
        }
        return this._panel;
    }

    public Dimension getCheckBoxOffset() {
        return new Dimension(0, 0);
    }

    /* access modifiers changed from: protected */
    public String buildToolTip(CategoryNode node) {
        StringBuffer result = new StringBuffer();
        result.append(node.getTitle()).append(" contains a total of ");
        result.append(node.getTotalNumberOfRecords());
        result.append(" LogRecords.");
        result.append(" Right-click for more info.");
        return result.toString();
    }
}
