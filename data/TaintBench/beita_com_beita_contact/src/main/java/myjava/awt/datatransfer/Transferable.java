package myjava.awt.datatransfer;

import java.io.IOException;

public interface Transferable {
    Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException;

    DataFlavor[] getTransferDataFlavors();

    boolean isDataFlavorSupported(DataFlavor dataFlavor);
}
