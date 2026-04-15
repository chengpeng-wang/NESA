package android.support.v4.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument.Page;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintAttributes.Builder;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentAdapter.LayoutResultCallback;
import android.print.PrintDocumentAdapter.WriteResultCallback;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PrintHelperKitkat {
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    private static final String LOG_TAG = "PrintHelperKitkat";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    int mColorMode = 2;
    final Context mContext;
    int mScaleMode = 2;

    PrintHelperKitkat(Context context) {
        Context context2 = context;
        this.mContext = context2;
    }

    public void setScaleMode(int i) {
        this.mScaleMode = i;
    }

    public int getScaleMode() {
        return this.mScaleMode;
    }

    public void setColorMode(int i) {
        this.mColorMode = i;
    }

    public int getColorMode() {
        return this.mColorMode;
    }

    public void printBitmap(String str, Bitmap bitmap) {
        String str2 = str;
        Bitmap bitmap2 = bitmap;
        if (bitmap2 != null) {
            int i = this.mScaleMode;
            PrintManager printManager = (PrintManager) this.mContext.getSystemService("print");
            MediaSize mediaSize = MediaSize.UNKNOWN_PORTRAIT;
            if (bitmap2.getWidth() > bitmap2.getHeight()) {
                mediaSize = MediaSize.UNKNOWN_LANDSCAPE;
            }
            Builder builder = r15;
            Builder builder2 = new Builder();
            PrintAttributes build = builder.setMediaSize(mediaSize).setColorMode(this.mColorMode).build();
            PrintManager printManager2 = printManager;
            String str3 = str2;
            PrintDocumentAdapter printDocumentAdapter = r15;
            final String str4 = str2;
            final Bitmap bitmap3 = bitmap2;
            final int i2 = i;
            PrintDocumentAdapter anonymousClass1 = new PrintDocumentAdapter(this) {
                private PrintAttributes mAttributes;
                final /* synthetic */ PrintHelperKitkat this$0;

                public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes2, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
                    PrintAttributes printAttributes3 = printAttributes;
                    PrintAttributes printAttributes4 = printAttributes2;
                    CancellationSignal cancellationSignal2 = cancellationSignal;
                    LayoutResultCallback layoutResultCallback2 = layoutResultCallback;
                    Bundle bundle2 = bundle;
                    this.mAttributes = printAttributes4;
                    PrintDocumentInfo.Builder builder = r11;
                    PrintDocumentInfo.Builder builder2 = new PrintDocumentInfo.Builder(str4);
                    layoutResultCallback2.onLayoutFinished(builder.setContentType(1).setPageCount(1).build(), !printAttributes4.equals(printAttributes3));
                }

                public void onWrite(PageRange[] pageRangeArr, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
                    Throwable e;
                    PageRange[] pageRangeArr2 = pageRangeArr;
                    ParcelFileDescriptor parcelFileDescriptor2 = parcelFileDescriptor;
                    CancellationSignal cancellationSignal2 = cancellationSignal;
                    WriteResultCallback writeResultCallback2 = writeResultCallback;
                    PrintedPdfDocument printedPdfDocument = r22;
                    PrintedPdfDocument printedPdfDocument2 = new PrintedPdfDocument(this.this$0.mContext, this.mAttributes);
                    PrintedPdfDocument printedPdfDocument3 = printedPdfDocument;
                    try {
                        Page startPage = printedPdfDocument3.startPage(1);
                        RectF rectF = r22;
                        RectF rectF2 = new RectF(startPage.getInfo().getContentRect());
                        RectF rectF3 = rectF;
                        Matrix matrix = r22;
                        Matrix matrix2 = new Matrix();
                        Matrix matrix3 = matrix;
                        float width = rectF3.width() / ((float) bitmap3.getWidth());
                        if (i2 == 2) {
                            width = Math.max(width, rectF3.height() / ((float) bitmap3.getHeight()));
                        } else {
                            width = Math.min(width, rectF3.height() / ((float) bitmap3.getHeight()));
                        }
                        boolean postScale = matrix3.postScale(width, width);
                        postScale = matrix3.postTranslate((rectF3.width() - (((float) bitmap3.getWidth()) * width)) / 2.0f, (rectF3.height() - (((float) bitmap3.getHeight()) * width)) / 2.0f);
                        startPage.getCanvas().drawBitmap(bitmap3, matrix3, null);
                        printedPdfDocument3.finishPage(startPage);
                        printedPdfDocument = printedPdfDocument3;
                        OutputStream outputStream = r22;
                        OutputStream fileOutputStream = new FileOutputStream(parcelFileDescriptor2.getFileDescriptor());
                        printedPdfDocument.writeTo(outputStream);
                        WriteResultCallback writeResultCallback3 = writeResultCallback2;
                        PageRange[] pageRangeArr3 = new PageRange[1];
                        PageRange[] pageRangeArr4 = pageRangeArr3;
                        pageRangeArr3[0] = PageRange.ALL_PAGES;
                        writeResultCallback3.onWriteFinished(pageRangeArr4);
                    } catch (IOException e2) {
                        int e3 = Log.e(PrintHelperKitkat.LOG_TAG, "Error writing printed content", e2);
                        writeResultCallback2.onWriteFailed(null);
                    } catch (Throwable e22) {
                        Throwable th = e22;
                        if (printedPdfDocument3 != null) {
                            printedPdfDocument3.close();
                        }
                        if (parcelFileDescriptor2 != null) {
                            try {
                                parcelFileDescriptor2.close();
                            } catch (IOException e4) {
                                IOException iOException = e4;
                            }
                        }
                        e22 = th;
                    }
                    if (printedPdfDocument3 != null) {
                        printedPdfDocument3.close();
                    }
                    if (parcelFileDescriptor2 != null) {
                        try {
                            parcelFileDescriptor2.close();
                        } catch (IOException e42) {
                            IOException iOException2 = e42;
                        }
                    }
                }
            };
            PrintJob print = printManager2.print(str3, printDocumentAdapter, build);
        }
    }

    public void printBitmap(String str, Uri uri) throws FileNotFoundException {
        printBitmap(str, loadConstrainedBitmap(uri, MAX_PRINT_SIZE));
    }

    private Bitmap loadConstrainedBitmap(Uri uri, int i) throws FileNotFoundException {
        Uri uri2 = uri;
        int i2 = i;
        if (i2 <= 0 || uri2 == null || this.mContext == null) {
            IllegalArgumentException illegalArgumentException = r12;
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException("bad argument to getScaledBitmap");
            throw illegalArgumentException;
        }
        Options options = r12;
        Options options2 = new Options();
        Options options3 = options;
        options3.inJustDecodeBounds = true;
        Bitmap loadBitmap = loadBitmap(uri2, options3);
        int i3 = options3.outWidth;
        int i4 = options3.outHeight;
        if (i3 <= 0 || i4 <= 0) {
            return null;
        }
        int i5;
        int max = Math.max(i3, i4);
        int i6 = 1;
        while (true) {
            i5 = i6;
            if (max <= i2) {
                break;
            }
            max >>>= 1;
            i6 = i5 << 1;
        }
        if (i5 <= 0 || 0 >= Math.min(i3, i4) / i5) {
            return null;
        }
        options = r12;
        options2 = new Options();
        Options options4 = options;
        options4.inMutable = true;
        options4.inSampleSize = i5;
        return loadBitmap(uri2, options4);
    }

    private Bitmap loadBitmap(Uri uri, Options options) throws FileNotFoundException {
        int w;
        Throwable e;
        Uri uri2 = uri;
        Options options2 = options;
        if (uri2 == null || this.mContext == null) {
            IllegalArgumentException illegalArgumentException = r11;
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException("bad argument to loadBitmap");
            throw illegalArgumentException;
        }
        InputStream inputStream = null;
        try {
            inputStream = this.mContext.getContentResolver().openInputStream(uri2);
            Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, options2);
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    w = Log.w(LOG_TAG, "close fail ", e2);
                }
            }
            return decodeStream;
        } catch (Throwable e22) {
            Throwable th = e22;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e222) {
                    w = Log.w(LOG_TAG, "close fail ", e222);
                }
            }
            e222 = th;
        }
    }
}
