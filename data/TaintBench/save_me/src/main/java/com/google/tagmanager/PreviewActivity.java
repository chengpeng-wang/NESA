package com.google.tagmanager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class PreviewActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            Log.i("Preview activity");
            Uri data = getIntent().getData();
            if (TagManager.getInstance(this).setPreviewData(data)) {
                Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                if (intent != null) {
                    Log.i("Invoke the launch activity for package name: " + getPackageName());
                    startActivity(intent);
                    return;
                }
                Log.i("No launch activity found for package name: " + getPackageName());
                return;
            }
            Log.w("Cannot preview the app with the uri: " + data);
        } catch (Exception e) {
            Log.e("Calling preview threw an exception: " + e.getMessage());
        }
    }
}
