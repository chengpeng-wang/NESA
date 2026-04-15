package com.yxx.jiejie;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipTools {
    public static final int BUFFER = 1024;
    public static final String UN_ZIP_DIR = "";
    public static final String ZIP_DIR = "";
    public static final String ZIP_FILENAME = "";

    public static void zipFile(String baseDir, String fileName) throws Exception {
        List fileList = getSubFiles(new File(baseDir));
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(fileName)));
        byte[] buf = new byte[1024];
        for (int i = 0; i < fileList.size(); i++) {
            File f = (File) fileList.get(i);
            ZipEntry ze = new ZipEntry(getAbsFileName(baseDir, f));
            ze.setSize(f.length());
            ze.setTime(f.lastModified());
            zos.putNextEntry(ze);
            InputStream is = new BufferedInputStream(new FileInputStream(f));
            while (true) {
                int readLen = is.read(buf, 0, 1024);
                if (readLen == -1) {
                    break;
                }
                zos.write(buf, 0, readLen);
            }
            is.close();
        }
        zos.close();
    }

    private static String getAbsFileName(String baseDir, File realFileName) {
        File real = realFileName;
        File base = new File(baseDir);
        String ret = real.getName();
        while (true) {
            real = real.getParentFile();
            if (!(real == null || real.equals(base))) {
                ret = real.getName() + "/" + ret;
            }
        }
        return ret;
    }

    private static List getSubFiles(File baseDir) {
        List ret = new ArrayList();
        File[] tmp = baseDir.listFiles();
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].isFile()) {
                ret.add(tmp[i]);
            }
            if (tmp[i].isDirectory()) {
                ret.addAll(getSubFiles(tmp[i]));
            }
        }
        return ret;
    }

    public static void upZipFile() throws Exception {
        ZipFile zfile = new ZipFile("");
        Enumeration zList = zfile.entries();
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ZipEntry ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                new File(ze.getName()).mkdir();
            } else {
                OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName("", ze.getName())));
                InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
                while (true) {
                    int readLen = is.read(buf, 0, 1024);
                    if (readLen == -1) {
                        break;
                    }
                    os.write(buf, 0, readLen);
                }
                is.close();
                os.close();
            }
        }
        zfile.close();
    }

    public static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        if (dirs.length <= 1) {
            return ret;
        }
        int i = 0;
        while (i < dirs.length - 1) {
            i++;
            ret = new File(ret, dirs[i]);
        }
        if (!ret.exists()) {
            ret.mkdirs();
        }
        File ret2 = new File(ret, dirs[dirs.length - 1]);
        return ret2;
    }

    public static void deleteDirFile(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            String[] list = file.list();
            for (String append : list) {
                deleteDirFile(new StringBuilder(String.valueOf(path)).append("\\").append(append).toString());
            }
        }
        file.delete();
    }

    public static String newFolder(String dir) {
        File myFilePath = new File(dir);
        if (!myFilePath.isDirectory()) {
            myFilePath.mkdirs();
        }
        return dir;
    }

    public static String getFileNames(String path) {
        File[] array = new File(path).listFiles();
        String pdfNames = "";
        int i = 0;
        while (i < array.length) {
            if (array[i].isFile() && array[i].getName().endsWith(".pdf")) {
                pdfNames = new StringBuilder(String.valueOf(pdfNames)).append(array[i].getName().substring(0, array[i].getName().length() - 4)).append(",").toString();
            }
            i++;
        }
        if (pdfNames.length() > 0) {
            pdfNames.substring(0, pdfNames.length() - 1);
        }
        return pdfNames;
    }

    public static void copyFile(String oldPath, String newPath) {
        int bytesum = 0;
        try {
            if (new File(oldPath).exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while (true) {
                    int byteread = inStream.read(buffer);
                    if (byteread == -1) {
                        inStream.close();
                        return;
                    } else {
                        bytesum += byteread;
                        fs.write(buffer, 0, byteread);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("copy file error!");
            e.printStackTrace();
        }
    }

    public static boolean fileExist(String fileNames, String pdfName) {
        if ("".equals(fileNames)) {
            return false;
        }
        String[] nameArr = fileNames.split(",");
        for (Object equals : nameArr) {
            if (pdfName.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    public static void deleteFileAndDir(String path) {
        File[] array = new File(path).listFiles();
        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                array[i].delete();
            } else if (array[i].isDirectory()) {
                deleteDirFile(array[i].getPath());
            }
        }
    }
}
