package com.cleanmaster.util;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/* compiled from: BuinessDataReporter */
class d implements e {
    final /* synthetic */ c a;

    d(c cVar) {
        this.a = cVar;
    }

    public void a(InputStream inputStream) {
        if (inputStream != null) {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    stringBuilder.append(readLine);
                    stringBuilder.append(10);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (h.a) {
                Log.d("bdr", "onResult " + stringBuilder.toString());
            }
        } else if (h.a) {
            Log.d("bdr", "onResult null");
        }
    }
}
