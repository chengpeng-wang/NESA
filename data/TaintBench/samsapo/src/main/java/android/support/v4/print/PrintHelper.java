package android.support.v4.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import java.io.FileNotFoundException;

public final class PrintHelper {
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    PrintHelperVersionImpl mImpl;

    private static final class PrintHelperKitkatImpl implements PrintHelperVersionImpl {
        private final PrintHelperKitkat printHelper;

        PrintHelperKitkatImpl(Context context) {
            PrintHelperKitkat printHelperKitkat = r6;
            PrintHelperKitkat printHelperKitkat2 = new PrintHelperKitkat(context);
            this.printHelper = printHelperKitkat;
        }

        public void setScaleMode(int i) {
            this.printHelper.setScaleMode(i);
        }

        public int getScaleMode() {
            return this.printHelper.getScaleMode();
        }

        public void setColorMode(int i) {
            this.printHelper.setColorMode(i);
        }

        public int getColorMode() {
            return this.printHelper.getColorMode();
        }

        public void printBitmap(String str, Bitmap bitmap) {
            this.printHelper.printBitmap(str, bitmap);
        }

        public void printBitmap(String str, Uri uri) throws FileNotFoundException {
            this.printHelper.printBitmap(str, uri);
        }
    }

    private static final class PrintHelperStubImpl implements PrintHelperVersionImpl {
        int mColorMode;
        int mScaleMode;

        private PrintHelperStubImpl() {
            this.mScaleMode = 2;
            this.mColorMode = 2;
        }

        /* synthetic */ PrintHelperStubImpl(AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this();
        }

        public void setScaleMode(int i) {
            this.mScaleMode = i;
        }

        public int getColorMode() {
            return this.mColorMode;
        }

        public void setColorMode(int i) {
            this.mColorMode = i;
        }

        public int getScaleMode() {
            return this.mScaleMode;
        }

        public void printBitmap(String str, Bitmap bitmap) {
        }

        public void printBitmap(String str, Uri uri) {
        }
    }

    interface PrintHelperVersionImpl {
        int getColorMode();

        int getScaleMode();

        void printBitmap(String str, Bitmap bitmap);

        void printBitmap(String str, Uri uri) throws FileNotFoundException;

        void setColorMode(int i);

        void setScaleMode(int i);
    }

    public static boolean systemSupportsPrint() {
        if (VERSION.SDK_INT >= 19) {
            return true;
        }
        return false;
    }

    public PrintHelper(Context context) {
        Context context2 = context;
        if (systemSupportsPrint()) {
            PrintHelperKitkatImpl printHelperKitkatImpl = r6;
            PrintHelperKitkatImpl printHelperKitkatImpl2 = new PrintHelperKitkatImpl(context2);
            this.mImpl = printHelperKitkatImpl;
            return;
        }
        PrintHelperStubImpl printHelperStubImpl = r6;
        PrintHelperStubImpl printHelperStubImpl2 = new PrintHelperStubImpl();
        this.mImpl = printHelperStubImpl;
    }

    public void setScaleMode(int i) {
        this.mImpl.setScaleMode(i);
    }

    public int getScaleMode() {
        return this.mImpl.getScaleMode();
    }

    public void setColorMode(int i) {
        this.mImpl.setColorMode(i);
    }

    public int getColorMode() {
        return this.mImpl.getColorMode();
    }

    public void printBitmap(String str, Bitmap bitmap) {
        this.mImpl.printBitmap(str, bitmap);
    }

    public void printBitmap(String str, Uri uri) throws FileNotFoundException {
        this.mImpl.printBitmap(str, uri);
    }
}
