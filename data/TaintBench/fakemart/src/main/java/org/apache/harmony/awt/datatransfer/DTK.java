package org.apache.harmony.awt.datatransfer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.dnd.peer.DropTargetContextPeer;
import java.nio.charset.Charset;
import org.apache.harmony.awt.internal.nls.Messages;
import org.apache.harmony.misc.SystemUtils;

public abstract class DTK {
    protected final DataTransferThread dataTransferThread = new DataTransferThread(this);
    private NativeClipboard nativeClipboard = null;
    private NativeClipboard nativeSelection = null;
    protected SystemFlavorMap systemFlavorMap;

    public abstract DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dragGestureEvent);

    public abstract DropTargetContextPeer createDropTargetContextPeer(DropTargetContext dropTargetContext);

    public abstract void initDragAndDrop();

    public abstract NativeClipboard newNativeClipboard();

    public abstract NativeClipboard newNativeSelection();

    public abstract void runEventLoop();

    protected DTK() {
        this.dataTransferThread.start();
    }

    /* JADX WARNING: Missing block: B:16:?, code skipped:
            return r0;
     */
    public static org.apache.harmony.awt.datatransfer.DTK getDTK() {
        /*
        r2 = org.apache.harmony.awt.ContextStorage.getContextLock();
        monitor-enter(r2);
        r1 = org.apache.harmony.awt.ContextStorage.shutdownPending();	 Catch:{ all -> 0x001d }
        if (r1 == 0) goto L_0x000e;
    L_0x000b:
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        r0 = 0;
    L_0x000d:
        return r0;
    L_0x000e:
        r0 = org.apache.harmony.awt.ContextStorage.getDTK();	 Catch:{ all -> 0x001d }
        if (r0 != 0) goto L_0x001b;
    L_0x0014:
        r0 = createDTK();	 Catch:{ all -> 0x001d }
        org.apache.harmony.awt.ContextStorage.setDTK(r0);	 Catch:{ all -> 0x001d }
    L_0x001b:
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        goto L_0x000d;
    L_0x001d:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.harmony.awt.datatransfer.DTK.getDTK():org.apache.harmony.awt.datatransfer.DTK");
    }

    public synchronized SystemFlavorMap getSystemFlavorMap() {
        return this.systemFlavorMap;
    }

    public synchronized void setSystemFlavorMap(SystemFlavorMap newFlavorMap) {
        this.systemFlavorMap = newFlavorMap;
    }

    public NativeClipboard getNativeClipboard() {
        if (this.nativeClipboard == null) {
            this.nativeClipboard = newNativeClipboard();
        }
        return this.nativeClipboard;
    }

    public NativeClipboard getNativeSelection() {
        if (this.nativeSelection == null) {
            this.nativeSelection = newNativeSelection();
        }
        return this.nativeSelection;
    }

    private static DTK createDTK() {
        String name;
        switch (SystemUtils.getOS()) {
            case 1:
                name = "org.apache.harmony.awt.datatransfer.windows.WinDTK";
                break;
            case 2:
                name = "org.apache.harmony.awt.datatransfer.linux.LinuxDTK";
                break;
            default:
                throw new RuntimeException(Messages.getString("awt.4E"));
        }
        try {
            return (DTK) Class.forName(name).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getDefaultCharset() {
        return "unicode";
    }

    /* access modifiers changed from: protected */
    public String[] getCharsets() {
        return new String[]{"UTF-16", "UTF-8", "unicode", "ISO-8859-1", "US-ASCII"};
    }

    public void initSystemFlavorMap(SystemFlavorMap fm) {
        String[] charsets = getCharsets();
        appendSystemFlavorMap(fm, DataFlavor.stringFlavor, "text/plain");
        appendSystemFlavorMap(fm, charsets, "plain", "text/plain");
        appendSystemFlavorMap(fm, charsets, "html", "text/html");
        appendSystemFlavorMap(fm, DataProvider.urlFlavor, "application/x-java-url");
        appendSystemFlavorMap(fm, charsets, "uri-list", "application/x-java-url");
        appendSystemFlavorMap(fm, DataFlavor.javaFileListFlavor, "application/x-java-file-list");
        appendSystemFlavorMap(fm, DataFlavor.imageFlavor, "image/x-java-image");
    }

    /* access modifiers changed from: protected */
    public void appendSystemFlavorMap(SystemFlavorMap fm, DataFlavor flav, String nat) {
        fm.addFlavorForUnencodedNative(nat, flav);
        fm.addUnencodedNativeForFlavor(flav, nat);
    }

    /* access modifiers changed from: protected */
    public void appendSystemFlavorMap(SystemFlavorMap fm, String[] charsets, String subType, String nat) {
        TextFlavor.addUnicodeClasses(fm, nat, subType);
        int i = 0;
        while (i < charsets.length) {
            if (charsets[i] != null && Charset.isSupported(charsets[i])) {
                TextFlavor.addCharsetClasses(fm, nat, subType, charsets[i]);
            }
            i++;
        }
    }
}
