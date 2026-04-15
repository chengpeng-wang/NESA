package com.vr.installer.scanner;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.FormatterClosedException;

public class PackageDescription {
    private static final String DEBUG_TAG = PackageDescription.class.getName();
    public static final String FILE_EXTENSION = ".apk";
    private static final int PART_COUNT = 4;
    private static final String SEPARATOR = "-";
    private String _appName = null;
    private String _appPath = null;
    private String _name = null;
    private int _order = -1;
    private String _packageName = null;
    private int _version = -1;

    public int getOrder() {
        return this._order;
    }

    public String getName() {
        return this._name;
    }

    public String getPackageName() {
        return this._packageName;
    }

    public int getVersion() {
        return this._version;
    }

    public boolean isInstalled(Context ctx) {
        try {
            if (ctx.getPackageManager().getPackageInfo(getPackageName(), 128).versionCode >= getVersion()) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public void install(Context ctx) {
        if (unpack(ctx)) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(new File(this._appPath)), "application/vnd.android.package-archive");
            intent.setFlags(268435456);
            ctx.startActivity(intent);
        }
    }

    public void launch(Context ctx) {
        Intent intent = ctx.getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.setFlags(268435456);
        ctx.startActivity(intent);
    }

    public void removeUnpackedFile() {
        if (this._appPath != null) {
            new File(this._appPath).delete();
        }
    }

    private boolean unpack(Context ctx) {
        if (this._appPath != null) {
            return true;
        }
        try {
            FileOutputStream outStream;
            File storage = Environment.getExternalStorageDirectory();
            if (storage.canRead()) {
                this._appPath = storage.getPath() + '/' + this._appName;
                outStream = new FileOutputStream(this._appPath);
            } else {
                this._appPath = new StringBuilder(String.valueOf(ctx.getFilesDir().getPath())).append('/').append(this._appName).toString();
                outStream = ctx.openFileOutput(this._appName, 1);
            }
            InputStream inStream = ctx.getAssets().open(this._appName);
            byte[] buffer = new byte[1024];
            while (true) {
                int read = inStream.read(buffer);
                if (read == -1) {
                    break;
                }
                outStream.write(buffer, 0, read);
            }
            inStream.close();
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            Log.e(DEBUG_TAG, e.toString());
            removeUnpackedFile();
            this._appPath = null;
        }
        if (this._appPath == null) {
            return false;
        }
        return true;
    }

    public static PackageDescription parseFrom(String source) throws FormatterClosedException {
        String[] parts = source.split(SEPARATOR);
        if (parts.length != PART_COUNT) {
            throw new FormatterClosedException();
        }
        PackageDescription desc = new PackageDescription();
        desc._order = Integer.parseInt(parts[0]);
        desc._packageName = parts[1];
        desc._version = Integer.parseInt(parts[2]);
        desc._name = parts[3];
        desc._appName = new StringBuilder(String.valueOf(source)).append(FILE_EXTENSION).toString();
        return desc;
    }
}
