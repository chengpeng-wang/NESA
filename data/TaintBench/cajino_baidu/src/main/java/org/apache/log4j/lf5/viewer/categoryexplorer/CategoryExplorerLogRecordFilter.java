package org.apache.log4j.lf5.viewer.categoryexplorer;

import java.util.Enumeration;
import org.apache.log4j.lf5.LogRecord;
import org.apache.log4j.lf5.LogRecordFilter;

public class CategoryExplorerLogRecordFilter implements LogRecordFilter {
    protected CategoryExplorerModel _model;

    public CategoryExplorerLogRecordFilter(CategoryExplorerModel model) {
        this._model = model;
    }

    public boolean passes(LogRecord record) {
        return this._model.isCategoryPathActive(new CategoryPath(record.getCategory()));
    }

    public void reset() {
        resetAllNodes();
    }

    /* access modifiers changed from: protected */
    public void resetAllNodes() {
        Enumeration nodes = this._model.getRootCategoryNode().depthFirstEnumeration();
        while (nodes.hasMoreElements()) {
            CategoryNode current = (CategoryNode) nodes.nextElement();
            current.resetNumberOfContainedRecords();
            this._model.nodeChanged(current);
        }
    }
}
