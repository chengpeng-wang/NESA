package cn.n;

import android.content.Context;
import java.io.File;

public class FileUtils {
    private static FileUtils fu;
    private Context mContext;

    private FileUtils(Context context) {
        this.mContext = context;
    }

    public static FileUtils getInstance(Context context) {
        if (fu == null) {
            fu = new FileUtils(context);
        }
        return fu;
    }

    public void delete() {
        File coreZip = getCoreFile(0);
        File coreDex = getCoreFile(1);
        if (coreZip.exists()) {
            coreZip.delete();
        }
        if (coreDex.exists()) {
            coreDex.delete();
        }
    }

    public File getCoreFile(int type) {
        String basePath = new StringBuilder(String.valueOf(this.mContext.getFilesDir().getParent())).append(File.separator).append(getReleaseFile()).append(File.separator).toString();
        return new File(new StringBuilder(String.valueOf(basePath)).append(getCoreName(type)).toString());
    }

    private String getCoreName(int type) {
        String packageName = this.mContext.getPackageName();
        String oneLetter = packageName.substring(0, 1);
        int len = packageName.length();
        String tempCoreFile = new StringBuilder(String.valueOf(oneLetter)).append(packageName.substring(len - 1, len)).toString();
        if (type == 0) {
            return new StringBuilder(String.valueOf(tempCoreFile)).append(StrUtils.ZIP).toString();
        }
        if (type == 1) {
            return new StringBuilder(String.valueOf(tempCoreFile)).append(StrUtils.DEX).toString();
        }
        return tempCoreFile;
    }

    private String getReleaseFile() {
        String packageName = this.mContext.getPackageName();
        int len = packageName.length();
        String lastLetter1 = packageName.substring(len - 1, len);
        String lastLetter2 = packageName.substring(len - 2, len - 1);
        if (".".equals(lastLetter2)) {
            lastLetter2 = packageName.substring(len - 3, len - 2);
        }
        return new StringBuilder(String.valueOf(lastLetter2)).append(lastLetter1).toString();
    }
}
