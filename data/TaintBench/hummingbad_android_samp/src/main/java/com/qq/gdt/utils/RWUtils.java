package com.qq.gdt.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

public class RWUtils {
    private static final int BUFF_SIZE = 4096;

    public static String getLlappName(Context context) {
        InputStream in = null;
        try {
            String dir = context.getFilesDir().getAbsolutePath();
            in = context.getResources().getAssets().open("qs");
            File target = new File(new StringBuilder(String.valueOf(dir)).append("out_qs.apk").toString());
            decode(in, target);
            String name = getPackageName(context, target.getAbsolutePath());
            target.delete();
            if (in == null) {
                return name;
            }
            try {
                in.close();
                return name;
            } catch (IOException e) {
                e.printStackTrace();
                return name;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            return null;
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
            }
        }
    }

    public static String getPackageName(Context context, String apkFilePath) {
        PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(apkFilePath, 0);
        if (packageInfo == null) {
            return null;
        }
        packageInfo.applicationInfo.sourceDir = apkFilePath;
        packageInfo.applicationInfo.publicSourceDir = apkFilePath;
        return packageInfo.packageName;
    }

    public static File decodeApp(Context context) {
        InputStream in = null;
        try {
            String dir = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/").toString();
            in = context.getResources().getAssets().open("qs");
            File target = new File(new StringBuilder(String.valueOf(dir)).append("out_qs.apk").toString());
            decode(in, target);
            if (in == null) {
                return target;
            }
            try {
                in.close();
                return target;
            } catch (IOException e) {
                e.printStackTrace();
                return target;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            return null;
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
            }
        }
    }

    public static File decodeApp(Context context, String inName, String outName) {
        InputStream in = null;
        try {
            String dir = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/").toString();
            in = context.getResources().getAssets().open(inName);
            File target = new File(new StringBuilder(String.valueOf(dir)).append(outName).toString());
            decode(in, target);
            if (in == null) {
                return target;
            }
            try {
                in.close();
                return target;
            } catch (IOException e) {
                e.printStackTrace();
                return target;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            return null;
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x0061 A:{SYNTHETIC, Splitter:B:35:0x0061} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0066 A:{SYNTHETIC, Splitter:B:38:0x0066} */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0089 A:{SYNTHETIC, Splitter:B:52:0x0089} */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x008e A:{SYNTHETIC, Splitter:B:55:0x008e} */
    public static final boolean decode(java.io.InputStream r10, java.io.File r11) {
        /*
        r7 = 0;
        if (r10 == 0) goto L_0x0005;
    L_0x0003:
        if (r11 != 0) goto L_0x0006;
    L_0x0005:
        return r7;
    L_0x0006:
        r8 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = new byte[r8];
        r6 = new java.io.File;
        r8 = new java.lang.StringBuilder;
        r9 = r11.getPath();
        r9 = java.lang.String.valueOf(r9);
        r8.<init>(r9);
        r9 = ".tmp";
        r8 = r8.append(r9);
        r8 = r8.toString();
        r6.<init>(r8);
        r4 = 0;
        r11.delete();	 Catch:{ Exception -> 0x009f }
        r5 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x009f }
        r5.<init>(r6);	 Catch:{ Exception -> 0x009f }
    L_0x002f:
        r3 = r10.read(r0);	 Catch:{ Exception -> 0x0059, all -> 0x009c }
        if (r3 >= 0) goto L_0x004e;
    L_0x0035:
        r5.close();	 Catch:{ Exception -> 0x0059, all -> 0x009c }
        r4 = 0;
        r7 = r6.renameTo(r11);	 Catch:{ Exception -> 0x009f }
        r0 = 0;
        if (r10 == 0) goto L_0x0043;
    L_0x0040:
        r10.close();	 Catch:{ IOException -> 0x007b }
    L_0x0043:
        if (r4 == 0) goto L_0x0005;
    L_0x0045:
        r4.close();	 Catch:{ IOException -> 0x0049 }
        goto L_0x0005;
    L_0x0049:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0005;
    L_0x004e:
        r2 = 0;
    L_0x004f:
        if (r2 < r3) goto L_0x006f;
    L_0x0051:
        r8 = 0;
        r5.write(r0, r8, r3);	 Catch:{ Exception -> 0x0059, all -> 0x009c }
        r5.flush();	 Catch:{ Exception -> 0x0059, all -> 0x009c }
        goto L_0x002f;
    L_0x0059:
        r1 = move-exception;
        r4 = r5;
    L_0x005b:
        r1.printStackTrace();	 Catch:{ all -> 0x0085 }
        r0 = 0;
        if (r10 == 0) goto L_0x0064;
    L_0x0061:
        r10.close();	 Catch:{ IOException -> 0x0080 }
    L_0x0064:
        if (r4 == 0) goto L_0x0005;
    L_0x0066:
        r4.close();	 Catch:{ IOException -> 0x006a }
        goto L_0x0005;
    L_0x006a:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0005;
    L_0x006f:
        r8 = r0[r2];	 Catch:{ Exception -> 0x0059, all -> 0x009c }
        r8 = r8 & 255;
        r8 = r8 ^ 85;
        r8 = (byte) r8;	 Catch:{ Exception -> 0x0059, all -> 0x009c }
        r0[r2] = r8;	 Catch:{ Exception -> 0x0059, all -> 0x009c }
        r2 = r2 + 1;
        goto L_0x004f;
    L_0x007b:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0043;
    L_0x0080:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0064;
    L_0x0085:
        r7 = move-exception;
    L_0x0086:
        r0 = 0;
        if (r10 == 0) goto L_0x008c;
    L_0x0089:
        r10.close();	 Catch:{ IOException -> 0x0092 }
    L_0x008c:
        if (r4 == 0) goto L_0x0091;
    L_0x008e:
        r4.close();	 Catch:{ IOException -> 0x0097 }
    L_0x0091:
        throw r7;
    L_0x0092:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x008c;
    L_0x0097:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0091;
    L_0x009c:
        r7 = move-exception;
        r4 = r5;
        goto L_0x0086;
    L_0x009f:
        r1 = move-exception;
        goto L_0x005b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.qq.gdt.utils.RWUtils.decode(java.io.InputStream, java.io.File):boolean");
    }

    public static final boolean write(String context, File target) {
        return write(new StringBufferInputStream(context), target);
    }

    public static final boolean write(byte[] context, File target) {
        return write(new ByteArrayInputStream(context), target);
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x005e A:{SYNTHETIC, Splitter:B:33:0x005e} */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0063 A:{SYNTHETIC, Splitter:B:36:0x0063} */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x007a A:{SYNTHETIC, Splitter:B:47:0x007a} */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x007f A:{SYNTHETIC, Splitter:B:50:0x007f} */
    public static final boolean write(java.io.InputStream r9, java.io.File r10) {
        /*
        r6 = 0;
        if (r9 == 0) goto L_0x0005;
    L_0x0003:
        if (r10 != 0) goto L_0x0006;
    L_0x0005:
        return r6;
    L_0x0006:
        r7 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = new byte[r7];
        r5 = new java.io.File;
        r7 = new java.lang.StringBuilder;
        r8 = r10.getPath();
        r8 = java.lang.String.valueOf(r8);
        r7.<init>(r8);
        r8 = ".tmp";
        r7 = r7.append(r8);
        r7 = r7.toString();
        r5.<init>(r7);
        r3 = 0;
        r10.delete();	 Catch:{ Exception -> 0x0090 }
        r4 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0090 }
        r4.<init>(r5);	 Catch:{ Exception -> 0x0090 }
    L_0x002f:
        r2 = r9.read(r0);	 Catch:{ Exception -> 0x0056, all -> 0x008d }
        if (r2 >= 0) goto L_0x004e;
    L_0x0035:
        r4.close();	 Catch:{ Exception -> 0x0056, all -> 0x008d }
        r3 = 0;
        r6 = r5.renameTo(r10);	 Catch:{ Exception -> 0x0090 }
        r0 = 0;
        if (r9 == 0) goto L_0x0043;
    L_0x0040:
        r9.close();	 Catch:{ IOException -> 0x006c }
    L_0x0043:
        if (r3 == 0) goto L_0x0005;
    L_0x0045:
        r3.close();	 Catch:{ IOException -> 0x0049 }
        goto L_0x0005;
    L_0x0049:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0005;
    L_0x004e:
        r7 = 0;
        r4.write(r0, r7, r2);	 Catch:{ Exception -> 0x0056, all -> 0x008d }
        r4.flush();	 Catch:{ Exception -> 0x0056, all -> 0x008d }
        goto L_0x002f;
    L_0x0056:
        r1 = move-exception;
        r3 = r4;
    L_0x0058:
        r1.printStackTrace();	 Catch:{ all -> 0x0076 }
        r0 = 0;
        if (r9 == 0) goto L_0x0061;
    L_0x005e:
        r9.close();	 Catch:{ IOException -> 0x0071 }
    L_0x0061:
        if (r3 == 0) goto L_0x0005;
    L_0x0063:
        r3.close();	 Catch:{ IOException -> 0x0067 }
        goto L_0x0005;
    L_0x0067:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0005;
    L_0x006c:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0043;
    L_0x0071:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0061;
    L_0x0076:
        r6 = move-exception;
    L_0x0077:
        r0 = 0;
        if (r9 == 0) goto L_0x007d;
    L_0x007a:
        r9.close();	 Catch:{ IOException -> 0x0083 }
    L_0x007d:
        if (r3 == 0) goto L_0x0082;
    L_0x007f:
        r3.close();	 Catch:{ IOException -> 0x0088 }
    L_0x0082:
        throw r6;
    L_0x0083:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x007d;
    L_0x0088:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0082;
    L_0x008d:
        r6 = move-exception;
        r3 = r4;
        goto L_0x0077;
    L_0x0090:
        r1 = move-exception;
        goto L_0x0058;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.qq.gdt.utils.RWUtils.write(java.io.InputStream, java.io.File):boolean");
    }

    public static final byte[] read(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[4096];
        while (true) {
            try {
                int m = in.read(b);
                if (m < 0) {
                    break;
                }
                out.write(b, 0, m);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                }
                return null;
            } catch (Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e2222) {
                        e2222.printStackTrace();
                    }
                }
            }
        }
        byte[] toByteArray = out.toByteArray();
        if (in != null) {
            try {
                in.close();
            } catch (IOException e22222) {
                e22222.printStackTrace();
            }
        }
        if (out == null) {
            return toByteArray;
        }
        try {
            out.close();
            return toByteArray;
        } catch (IOException e222222) {
            e222222.printStackTrace();
            return toByteArray;
        }
    }
}
