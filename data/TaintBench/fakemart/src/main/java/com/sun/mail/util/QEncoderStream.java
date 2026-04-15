package com.sun.mail.util;

import android.support.v4.view.MotionEventCompat;
import java.io.IOException;
import java.io.OutputStream;

public class QEncoderStream extends QPEncoderStream {
    private static String TEXT_SPECIALS = "=_?";
    private static String WORD_SPECIALS = "=_?\"#$%&'(),.:;<>@[\\]^`{|}~";
    private String specials;

    public QEncoderStream(OutputStream out, boolean encodingWord) {
        super(out, Integer.MAX_VALUE);
        this.specials = encodingWord ? WORD_SPECIALS : TEXT_SPECIALS;
    }

    public void write(int c) throws IOException {
        c &= MotionEventCompat.ACTION_MASK;
        if (c == 32) {
            output(95, false);
        } else if (c < 32 || c >= 127 || this.specials.indexOf(c) >= 0) {
            output(c, true);
        } else {
            output(c, false);
        }
    }

    public static int encodedLength(byte[] b, boolean encodingWord) {
        int len = 0;
        String specials = encodingWord ? WORD_SPECIALS : TEXT_SPECIALS;
        for (byte b2 : b) {
            int c = b2 & MotionEventCompat.ACTION_MASK;
            if (c < 32 || c >= 127 || specials.indexOf(c) >= 0) {
                len += 3;
            } else {
                len++;
            }
        }
        return len;
    }
}
