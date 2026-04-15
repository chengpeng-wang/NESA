package org.apache.log4j.lf5.viewer.categoryexplorer;

import java.awt.AWTEventMulticaster;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.log4j.lf5.LogRecord;

public class CategoryExplorerModel extends DefaultTreeModel {
    private static final long serialVersionUID = -3413887384316015901L;
    protected ActionEvent _event = new ActionEvent(this, 1001, "Nodes Selection changed");
    protected ActionListener _listener = null;
    protected boolean _renderFatal = true;

    /* renamed from: org.apache.log4j.lf5.viewer.categoryexplorer.CategoryExplorerModel$1 */
    class AnonymousClass1 implements Runnable {
        private final CategoryExplorerModel this$0;
        private final CategoryNode val$node;

        AnonymousClass1(CategoryExplorerModel categoryExplorerModel, CategoryNode categoryNode) {
            this.this$0 = categoryExplorerModel;
            this.val$node = categoryNode;
        }

        public void run() {
            this.this$0.nodeChanged(this.val$node);
        }
    }

    public CategoryExplorerModel(CategoryNode node) {
        super(node);
    }

    public void addLogRecord(LogRecord lr) {
        CategoryPath path = new CategoryPath(lr.getCategory());
        addCategory(path);
        CategoryNode node = getCategoryNode(path);
        node.addRecord();
        if (this._renderFatal && lr.isFatal()) {
            TreeNode[] nodes = getPathToRoot(node);
            int len = nodes.length;
            for (int i = 1; i < len - 1; i++) {
                CategoryNode parent = nodes[i];
                parent.setHasFatalChildren(true);
                nodeChanged(parent);
            }
            node.setHasFatalRecords(true);
            nodeChanged(node);
        }
    }

    public CategoryNode getRootCategoryNode() {
        return (CategoryNode) getRoot();
    }

    public CategoryNode getCategoryNode(String category) {
        return getCategoryNode(new CategoryPath(category));
    }

    public CategoryNode getCategoryNode(CategoryPath path) {
        CategoryNode parent = (CategoryNode) getRoot();
        for (int i = 0; i < path.size(); i++) {
            CategoryElement element = path.categoryElementAt(i);
            Enumeration children = parent.children();
            boolean categoryAlreadyExists = false;
            while (children.hasMoreElements()) {
                CategoryNode node = (CategoryNode) children.nextElement();
                if (node.getTitle().toLowerCase().equals(element.getTitle().toLowerCase())) {
                    categoryAlreadyExists = true;
                    parent = node;
                    break;
                }
            }
            if (!categoryAlreadyExists) {
                return null;
            }
        }
        return parent;
    }

    public boolean isCategoryPathActive(CategoryPath path) {
        CategoryNode parent = (CategoryNode) getRoot();
        boolean active = false;
        int i = 0;
        while (i < path.size()) {
            CategoryElement element = path.categoryElementAt(i);
            Enumeration children = parent.children();
            boolean categoryAlreadyExists = false;
            active = false;
            while (children.hasMoreElements()) {
                CategoryNode node = (CategoryNode) children.nextElement();
                if (node.getTitle().toLowerCase().equals(element.getTitle().toLowerCase())) {
                    categoryAlreadyExists = true;
                    parent = node;
                    if (parent.isSelected()) {
                        active = true;
                    }
                    if (active || !categoryAlreadyExists) {
                        return false;
                    }
                    i++;
                }
            }
            if (active) {
            }
            return false;
        }
        return active;
    }

    public CategoryNode addCategory(CategoryPath path) {
        CategoryNode parent = (CategoryNode) getRoot();
        for (int i = 0; i < path.size(); i++) {
            CategoryElement element = path.categoryElementAt(i);
            Enumeration children = parent.children();
            boolean categoryAlreadyExists = false;
            while (children.hasMoreElements()) {
                CategoryNode node = (CategoryNode) children.nextElement();
                if (node.getTitle().toLowerCase().equals(element.getTitle().toLowerCase())) {
                    categoryAlreadyExists = true;
                    parent = node;
                    break;
                }
            }
            if (!categoryAlreadyExists) {
                CategoryNode newNode = new CategoryNode(element.getTitle());
                insertNodeInto(newNode, parent, parent.getChildCount());
                refresh(newNode);
                parent = newNode;
            }
        }
        return parent;
    }

    public void update(CategoryNode node, boolean selected) {
        if (node.isSelected() != selected) {
            if (selected) {
                setParentSelection(node, true);
            } else {
                setDescendantSelection(node, false);
            }
        }
    }

    public void setDescendantSelection(CategoryNode node, boolean selected) {
        Enumeration descendants = node.depthFirstEnumeration();
        while (descendants.hasMoreElements()) {
            CategoryNode current = (CategoryNode) descendants.nextElement();
            if (current.isSelected() != selected) {
                current.setSelected(selected);
                nodeChanged(current);
            }
        }
        notifyActionListeners();
    }

    public void setParentSelection(CategoryNode node, boolean selected) {
        TreeNode[] nodes = getPathToRoot(node);
        int len = nodes.length;
        for (int i = 1; i < len; i++) {
            CategoryNode parent = nodes[i];
            if (parent.isSelected() != selected) {
                parent.setSelected(selected);
                nodeChanged(parent);
            }
        }
        notifyActionListeners();
    }

    public synchronized void addActionListener(ActionListener l) {
        this._listener = AWTEventMulticaster.add(this._listener, l);
    }

    public synchronized void removeActionListener(ActionListener l) {
        this._listener = AWTEventMulticaster.remove(this._listener, l);
    }

    public void resetAllNodeCounts() {
        Enumeration nodes = getRootCategoryNode().depthFirstEnumeration();
        while (nodes.hasMoreElements()) {
            CategoryNode current = (CategoryNode) nodes.nextElement();
            current.resetNumberOfContainedRecords();
            nodeChanged(current);
        }
    }

    public TreePath getTreePathToRoot(CategoryNode node) {
        if (node == null) {
            return null;
        }
        return new TreePath(getPathToRoot(node));
    }

    /* access modifiers changed from: protected */
    public void notifyActionListeners() {
        if (this._listener != null) {
            this._listener.actionPerformed(this._event);
        }
    }

    /* access modifiers changed from: protected */
    public void refresh(CategoryNode node) {
        SwingUtilities.invokeLater(new AnonymousClass1(this, node));
    }
}
