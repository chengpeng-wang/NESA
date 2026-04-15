package vi.com.gdi.bgl.android.java;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;

public class EnvDrawText {
    public static boolean bBmpChange = false;
    public static Bitmap bmp = null;
    public static int[] buffer = null;
    public static Canvas canvasTemp = null;
    public static SparseArray<a> fontCache = null;
    public static int iWordHightMax = 0;
    public static int iWordWidthMax = 0;
    public static Paint pt = null;

    public static int[] drawText(String str, int i, int i2, int[] iArr, int i3, int i4, int i5, int i6) {
        if (pt == null) {
            pt = new Paint();
        } else {
            pt.reset();
        }
        pt.setSubpixelText(true);
        pt.setAntiAlias(true);
        if (!(i2 == 0 || fontCache == null)) {
            a aVar = (a) fontCache.get(i2);
            if (aVar != null) {
                pt.setTypeface(aVar.a);
            }
        }
        pt.setTextSize((float) i);
        int indexOf = str.indexOf(92, 0);
        int measureText;
        int ceil;
        if (indexOf == -1) {
            FontMetrics fontMetrics = pt.getFontMetrics();
            measureText = (int) pt.measureText(str);
            ceil = (int) Math.ceil((double) (fontMetrics.descent - fontMetrics.ascent));
            iArr[0] = measureText;
            iArr[1] = ceil;
            measureText = (int) Math.pow(2.0d, (double) ((int) Math.ceil(Math.log((double) measureText) / Math.log(2.0d))));
            ceil = (int) Math.pow(2.0d, (double) ((int) Math.ceil(Math.log((double) ceil) / Math.log(2.0d))));
            if (iWordWidthMax < measureText || iWordHightMax < ceil) {
                bBmpChange = true;
                iWordWidthMax = measureText;
                iWordHightMax = ceil;
            }
            iArr[2] = iWordWidthMax;
            iArr[3] = iWordHightMax;
            if (bBmpChange) {
                bmp = Bitmap.createBitmap(iWordWidthMax, iWordHightMax, Config.ARGB_8888);
                canvasTemp = new Canvas(bmp);
            } else {
                bmp.eraseColor(0);
            }
            if ((ViewCompat.MEASURED_STATE_MASK & i5) == 0) {
                canvasTemp.drawColor(33554431);
            } else {
                canvasTemp.drawColor(i5);
            }
            if (i6 != 0) {
                pt.setStrokeWidth((float) i6);
                pt.setStrokeCap(Cap.ROUND);
                pt.setStrokeJoin(Join.ROUND);
                pt.setStyle(Style.STROKE);
                pt.setColor(i4);
                canvasTemp.drawText(str, 0.0f, 0.0f - fontMetrics.ascent, pt);
            }
            pt.setStyle(Style.FILL);
            pt.setColor(i3);
            canvasTemp.drawText(str, 0.0f, 0.0f - fontMetrics.ascent, pt);
        } else {
            int indexOf2;
            int indexOf3;
            ceil = indexOf + 1;
            measureText = 2;
            indexOf = (int) pt.measureText(str.substring(0, indexOf));
            while (true) {
                indexOf2 = str.indexOf(92, ceil);
                if (indexOf2 <= 0) {
                    break;
                }
                ceil = (int) pt.measureText(str.substring(ceil, indexOf2));
                if (ceil > indexOf) {
                    indexOf = ceil;
                }
                ceil = indexOf2 + 1;
                measureText++;
            }
            if (ceil != str.length()) {
                ceil = (int) pt.measureText(str.substring(ceil, str.length()));
                if (ceil > indexOf) {
                    indexOf = ceil;
                }
            }
            FontMetrics fontMetrics2 = pt.getFontMetrics();
            indexOf2 = (int) Math.ceil((double) (fontMetrics2.descent - fontMetrics2.ascent));
            measureText *= indexOf2;
            iArr[0] = indexOf;
            iArr[1] = measureText;
            indexOf = (int) Math.pow(2.0d, (double) ((int) Math.ceil(Math.log((double) indexOf) / Math.log(2.0d))));
            measureText = (int) Math.pow(2.0d, (double) ((int) Math.ceil(Math.log((double) measureText) / Math.log(2.0d))));
            if (iWordWidthMax < indexOf || iWordHightMax < measureText) {
                bBmpChange = true;
                iWordWidthMax = indexOf;
                iWordHightMax = measureText;
            }
            iArr[2] = iWordWidthMax;
            iArr[3] = iWordHightMax;
            if (bBmpChange) {
                bmp = Bitmap.createBitmap(iWordWidthMax, iWordHightMax, Config.ARGB_8888);
                canvasTemp = new Canvas(bmp);
            } else {
                bmp.eraseColor(0);
            }
            if ((ViewCompat.MEASURED_STATE_MASK & i5) == 0) {
                canvasTemp.drawColor(33554431);
            } else {
                canvasTemp.drawColor(i5);
            }
            measureText = 0;
            indexOf = 0;
            while (true) {
                indexOf3 = str.indexOf(92, measureText);
                if (indexOf3 <= 0) {
                    break;
                }
                String substring = str.substring(measureText, indexOf3);
                int measureText2 = (int) pt.measureText(substring);
                measureText = indexOf3 + 1;
                if (i6 != 0) {
                    pt.setStrokeWidth((float) i6);
                    pt.setStrokeCap(Cap.ROUND);
                    pt.setStrokeJoin(Join.ROUND);
                    pt.setStyle(Style.STROKE);
                    pt.setColor(i4);
                    canvasTemp.drawText(substring, (float) ((iArr[0] - measureText2) / 2), ((float) (indexOf * indexOf2)) - fontMetrics2.ascent, pt);
                }
                pt.setStyle(Style.FILL);
                pt.setColor(i3);
                canvasTemp.drawText(substring, (float) ((iArr[0] - measureText2) / 2), ((float) (indexOf * indexOf2)) - fontMetrics2.ascent, pt);
                indexOf++;
            }
            if (measureText != str.length()) {
                String substring2 = str.substring(measureText, str.length());
                indexOf3 = (int) pt.measureText(substring2);
                if (i6 != 0) {
                    pt.setStrokeWidth((float) i6);
                    pt.setStrokeCap(Cap.ROUND);
                    pt.setStrokeJoin(Join.ROUND);
                    pt.setStyle(Style.STROKE);
                    pt.setColor(i4);
                    canvasTemp.drawText(substring2, (float) ((iArr[0] - indexOf3) / 2), ((float) (indexOf * indexOf2)) - fontMetrics2.ascent, pt);
                }
                pt.setStyle(Style.FILL);
                pt.setColor(i3);
                canvasTemp.drawText(substring2, (float) ((iArr[0] - indexOf3) / 2), ((float) (indexOf * indexOf2)) - fontMetrics2.ascent, pt);
            }
        }
        indexOf = iWordWidthMax * iWordHightMax;
        if (bBmpChange) {
            buffer = new int[indexOf];
        }
        bmp.getPixels(buffer, 0, iWordWidthMax, 0, 0, iWordWidthMax, iWordHightMax);
        bBmpChange = false;
        return buffer;
    }

    public static short[] getTextSize(String str, int i) {
        int length = str.length();
        if (length == 0) {
            return null;
        }
        Paint paint = new Paint();
        paint.setSubpixelText(true);
        paint.setAntiAlias(true);
        paint.setTextSize((float) i);
        short[] sArr = new short[length];
        for (int i2 = 0; i2 < length; i2++) {
            sArr[i2] = (short) ((int) paint.measureText(str.substring(0, i2 + 1)));
        }
        return sArr;
    }

    public static void registFontCache(int i, Typeface typeface) {
        if (i != 0 && typeface != null) {
            if (fontCache == null) {
                fontCache = new SparseArray();
            }
            a aVar = (a) fontCache.get(i);
            if (aVar == null) {
                aVar = new a();
                aVar.a = typeface;
                aVar.b++;
                fontCache.put(i, aVar);
                return;
            }
            aVar.b++;
        }
    }

    public static void removeFontCache(int i) {
        a aVar = (a) fontCache.get(i);
        if (aVar != null) {
            aVar.b--;
            if (aVar.b == 0) {
                fontCache.remove(i);
            }
        }
    }
}
