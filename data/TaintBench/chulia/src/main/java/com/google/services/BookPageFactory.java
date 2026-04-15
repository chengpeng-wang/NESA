package com.google.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Vector;

public class BookPageFactory {
    private File book_file = null;
    DecimalFormat df = new DecimalFormat("##.##");
    private int mHeight;
    private int mLineCount;
    private Paint mPaint;
    private float mVisibleHeight;
    private float mVisibleWidth;
    private int mWidth;
    private int m_backColor = -24955;
    private Bitmap m_book_bg = null;
    private int m_fontSize = 24;
    private boolean m_isfirstPage;
    private boolean m_islastPage;
    private Vector<String> m_lines = new Vector();
    private MappedByteBuffer m_mbBuf = null;
    private int m_mbBufBegin = 0;
    private int m_mbBufEnd = 0;
    private int m_mbBufLen = 0;
    private String m_strCharsetName = "GBK";
    private int m_textColor = -16777216;
    private int marginHeight = 20;
    private int marginWidth = 15;

    public BookPageFactory(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
        this.mPaint = new Paint(1);
        this.mPaint.setTextAlign(Align.LEFT);
        this.mPaint.setTextSize((float) this.m_fontSize);
        this.mPaint.setColor(this.m_textColor);
        this.mVisibleWidth = (float) (this.mWidth - (this.marginWidth * 2));
        this.mVisibleHeight = (float) (this.mHeight - (this.marginHeight * 2));
        this.mLineCount = (int) (this.mVisibleHeight / ((float) this.m_fontSize));
    }

    public void openbook(Context context) throws IOException {
        try {
            File file = new File(context.getFilesDir(), "test.txt");
            InputStream open = context.getResources().getAssets().open("m.txt");
            byte[] bArr = new byte[1024];
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            while (true) {
                int read = open.read(bArr);
                if (read == -1) {
                    break;
                }
                randomAccessFile.write(bArr, 0, read);
            }
            randomAccessFile.close();
            open.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.book_file = new File(context.getFilesDir(), "test.txt");
        long length = this.book_file.length();
        this.m_mbBufLen = (int) length;
        this.m_mbBuf = new RandomAccessFile(this.book_file, "r").getChannel().map(MapMode.READ_ONLY, 0, length);
    }

    /* access modifiers changed from: protected */
    public byte[] readParagraphBack(int i) {
        int i2;
        int i3 = 0;
        byte b;
        byte b2;
        if (!this.m_strCharsetName.equals("UTF-16LE")) {
            if (!this.m_strCharsetName.equals("UTF-16BE")) {
                i2 = i - 1;
                while (i2 > 0) {
                    if (this.m_mbBuf.get(i2) == (byte) 10 && i2 != i - 1) {
                        i2++;
                        break;
                    }
                    i2--;
                }
            } else {
                i2 = i - 2;
                while (i2 > 0) {
                    b = this.m_mbBuf.get(i2);
                    b2 = this.m_mbBuf.get(i2 + 1);
                    if (b == (byte) 0 && b2 == (byte) 10 && i2 != i - 2) {
                        i2 += 2;
                        break;
                    }
                    i2--;
                }
            }
        } else {
            i2 = i - 2;
            while (i2 > 0) {
                b = this.m_mbBuf.get(i2);
                b2 = this.m_mbBuf.get(i2 + 1);
                if (b == (byte) 10 && b2 == (byte) 0 && i2 != i - 2) {
                    i2 += 2;
                    break;
                }
                i2--;
            }
        }
        if (i2 < 0) {
            i2 = 0;
        }
        int i4 = i - i2;
        byte[] bArr = new byte[i4];
        while (i3 < i4) {
            bArr[i3] = this.m_mbBuf.get(i2 + i3);
            i3++;
        }
        return bArr;
    }

    /* access modifiers changed from: protected */
    public byte[] readParagraphForward(int i) {
        int i2;
        int i3;
        int i4;
        byte b;
        byte b2;
        if (this.m_strCharsetName.equals("UTF-16LE")) {
            i2 = i;
            while (i2 < this.m_mbBufLen - 1) {
                i4 = i2 + 1;
                b = this.m_mbBuf.get(i2);
                i2 = i4 + 1;
                b2 = this.m_mbBuf.get(i4);
                if (b == (byte) 10 && b2 == (byte) 0) {
                    break;
                }
            }
        } else if (this.m_strCharsetName.equals("UTF-16BE")) {
            i2 = i;
            while (i2 < this.m_mbBufLen - 1) {
                i4 = i2 + 1;
                b = this.m_mbBuf.get(i2);
                i2 = i4 + 1;
                b2 = this.m_mbBuf.get(i4);
                if (b == (byte) 0 && b2 == (byte) 10) {
                    break;
                }
            }
        } else {
            i2 = i;
            while (i2 < this.m_mbBufLen) {
                i3 = i2 + 1;
                if (this.m_mbBuf.get(i2) == (byte) 10) {
                    i2 = i3;
                    break;
                }
                i2 = i3;
            }
        }
        i3 = i2 - i;
        byte[] bArr = new byte[i3];
        for (i2 = 0; i2 < i3; i2++) {
            bArr[i2] = this.m_mbBuf.get(i + i2);
        }
        return bArr;
    }

    /* access modifiers changed from: protected */
    public Vector<String> pageDown() {
        String str = "";
        Vector vector = new Vector();
        while (vector.size() < this.mLineCount && this.m_mbBufEnd < this.m_mbBufLen) {
            String str2;
            byte[] readParagraphForward = readParagraphForward(this.m_mbBufEnd);
            this.m_mbBufEnd += readParagraphForward.length;
            try {
                str2 = new String(readParagraphForward, this.m_strCharsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                str2 = str;
            }
            str = "";
            if (str2.indexOf("\r\n") != -1) {
                str = str2.replaceAll("\r\n", "");
                str2 = "\r\n";
            } else if (str2.indexOf("\n") != -1) {
                str = str2.replaceAll("\n", "");
                str2 = "\n";
            } else {
                String str3 = str;
                str = str2;
                str2 = str3;
            }
            if (str.length() == 0) {
                vector.add(str);
            }
            while (str.length() > 0) {
                int breakText = this.mPaint.breakText(str, true, this.mVisibleWidth, null);
                vector.add(str.substring(0, breakText));
                str = str.substring(breakText);
                if (vector.size() >= this.mLineCount) {
                    break;
                }
            }
            if (str.length() != 0) {
                try {
                    this.m_mbBufEnd -= (str + str2).getBytes(this.m_strCharsetName).length;
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return vector;
    }

    /* access modifiers changed from: protected */
    public void pageUp() {
        if (this.m_mbBufBegin < 0) {
            this.m_mbBufBegin = 0;
        }
        Vector vector = new Vector();
        String str = "";
        while (vector.size() < this.mLineCount && this.m_mbBufBegin > 0) {
            String str2;
            Vector vector2 = new Vector();
            byte[] readParagraphBack = readParagraphBack(this.m_mbBufBegin);
            this.m_mbBufBegin -= readParagraphBack.length;
            try {
                str2 = new String(readParagraphBack, this.m_strCharsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                str2 = str;
            }
            str2 = str2.replaceAll("\r\n", "").replaceAll("\n", "");
            if (str2.length() == 0) {
                vector2.add(str2);
            }
            while (str2.length() > 0) {
                int breakText = this.mPaint.breakText(str2, true, this.mVisibleWidth, null);
                vector2.add(str2.substring(0, breakText));
                str2 = str2.substring(breakText);
            }
            vector.addAll(0, vector2);
            str = str2;
        }
        while (vector.size() > this.mLineCount) {
            try {
                this.m_mbBufBegin = ((String) vector.get(0)).getBytes(this.m_strCharsetName).length + this.m_mbBufBegin;
                vector.remove(0);
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            }
        }
        this.m_mbBufEnd = this.m_mbBufBegin;
    }

    /* access modifiers changed from: protected */
    public void prePage() throws IOException {
        if (this.m_mbBufBegin <= 0) {
            this.m_mbBufBegin = 0;
            this.m_isfirstPage = true;
            return;
        }
        this.m_isfirstPage = false;
        this.m_lines.clear();
        pageUp();
        this.m_lines = pageDown();
    }

    public void nextPage() throws IOException {
        if (this.m_mbBufEnd >= this.m_mbBufLen) {
            this.m_islastPage = true;
            return;
        }
        this.m_islastPage = false;
        this.m_lines.clear();
        this.m_mbBufBegin = this.m_mbBufEnd;
        this.m_lines = pageDown();
    }

    public void wilDraw(Canvas canvas) {
        if (this.m_lines.size() == 0) {
            this.m_lines = pageDown();
        }
        if (this.m_lines.size() > 0) {
            if (this.m_book_bg == null) {
                canvas.drawColor(this.m_backColor);
            } else {
                canvas.drawBitmap(this.m_book_bg, 0.0f, 0.0f, null);
            }
            int i = this.marginHeight;
            Iterator it = this.m_lines.iterator();
            int i2 = i;
            while (it.hasNext()) {
                i2 += this.m_fontSize;
                canvas.drawText((String) it.next(), (float) this.marginWidth, (float) i2, this.mPaint);
            }
        }
        canvas.drawText(this.df.format((double) (((float) ((((double) this.m_mbBufBegin) * 1.0d) / ((double) this.m_mbBufLen))) * 100.0f)) + "%", (float) (this.mWidth - (((int) this.mPaint.measureText("999.9%")) + 1)), (float) (this.mHeight - 5), this.mPaint);
    }

    public void setBgBitmap(Bitmap bitmap) {
        this.m_book_bg = bitmap;
    }

    public boolean isfirstPage() {
        return this.m_isfirstPage;
    }

    public boolean islastPage() {
        return this.m_islastPage;
    }
}
