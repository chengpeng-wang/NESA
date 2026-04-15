package org.apache.harmony.awt.datatransfer;

import java.awt.datatransfer.DataFlavor;
import java.util.Comparator;

public class FlavorsComparator implements Comparator<DataFlavor> {
    public int compare(DataFlavor flav1, DataFlavor flav2) {
        if (!flav1.isFlavorTextType() && !flav2.isFlavorTextType()) {
            return 0;
        }
        if (!flav1.isFlavorTextType() && flav2.isFlavorTextType()) {
            return -1;
        }
        if (flav1.isFlavorTextType() && !flav2.isFlavorTextType()) {
            return 1;
        }
        if (DataFlavor.selectBestTextFlavor(new DataFlavor[]{flav1, flav2}) != flav1) {
            return 1;
        }
        return -1;
    }
}
