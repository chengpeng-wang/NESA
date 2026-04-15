package com.facebook.ads.internal.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class m {
    static Bitmap a(Context context, String str) {
        return BitmapFactory.decodeFile(new File(context.getCacheDir(), String.format("%d.png", new Object[]{Integer.valueOf(str.hashCode())})).getAbsolutePath());
    }

    static void a(Context context, String str, Bitmap bitmap) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(context.getCacheDir(), String.format("%d.png", new Object[]{Integer.valueOf(str.hashCode())})));
            bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
        }
    }

    public static void a(Context context, List<String> list, final l lVar) {
        final int[] iArr = new int[]{list.size()};
        if (iArr[0] != 0) {
            for (String str : list) {
                new k(context).a(new l() {
                    public final void a() {
                        int[] iArr = iArr;
                        iArr[0] = iArr[0] - 1;
                        if (iArr[0] == 0 && lVar != null) {
                            lVar.a();
                        }
                    }
                }).execute(new String[]{str});
            }
        } else if (lVar != null) {
            lVar.a();
        }
    }
}
