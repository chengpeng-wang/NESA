package com.flurry.sdk;

import com.google.ads.AdRequest;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class kk {
    public static final String a = kk.class.getSimpleName();
    private static final Integer c = Integer.valueOf(50);
    LinkedHashMap b;
    private String d = (AdRequest.LOGTAG + "Main");

    public kk() {
        b(this.d);
    }

    private synchronized void a() {
        LinkedList linkedList = new LinkedList(this.b.keySet());
        new ii(hz.a.b.getFileStreamPath(d(this.d)), ".YFlurrySenderIndex.info.", 1, new ko(this)).b();
        if (!linkedList.isEmpty()) {
            a(this.d, linkedList, this.d);
        }
    }

    private synchronized void a(String str, List list, String str2) {
        lt.b();
        iw.a(5, a, "Saving Index File for " + str + " file name:" + hz.a.b.getFileStreamPath(d(str)));
        ii iiVar = new ii(hz.a.b.getFileStreamPath(d(str)), str2, 1, new kr(this));
        ArrayList arrayList = new ArrayList();
        for (String ksVar : list) {
            arrayList.add(new ks(ksVar));
        }
        iiVar.a(arrayList);
    }

    private synchronized void a(String str, byte[] bArr) {
        lt.b();
        iw.a(5, a, "Saving Block File for " + str + " file name:" + hz.a.b.getFileStreamPath(kg.a(str)));
        new ii(hz.a.b.getFileStreamPath(kg.a(str)), ".yflurrydatasenderblock.", 1, new kq(this)).a(new kg(bArr));
    }

    private void b(String str) {
        this.b = new LinkedHashMap();
        ArrayList<String> arrayList = new ArrayList();
        if (i(str)) {
            List j = j(str);
            if (j != null && j.size() > 0) {
                arrayList.addAll(j);
                for (String c : arrayList) {
                    c(c);
                }
            }
            h(str);
        } else {
            List<ks> list = (List) new ii(hz.a.b.getFileStreamPath(d(this.d)), str, 1, new kl(this)).a();
            if (list == null) {
                iw.a("New main file also not found. returning..");
                return;
            }
            for (ks ksVar : list) {
                arrayList.add(ksVar.a);
            }
        }
        for (String c2 : arrayList) {
            this.b.put(c2, g(c2));
        }
    }

    private void c(String str) {
        List<String> j = j(str);
        if (j == null) {
            iw.a("No old file to replace");
            return;
        }
        for (String str2 : j) {
            byte[] k = k(str2);
            if (k == null) {
                iw.a(6, a, "File does not exist");
            } else {
                a(str2, k);
                lt.b();
                iw.a(5, a, "Deleting  block File for " + str2 + " file name:" + hz.a.b.getFileStreamPath(".flurrydatasenderblock." + str2));
                File fileStreamPath = hz.a.b.getFileStreamPath(".flurrydatasenderblock." + str2);
                if (fileStreamPath.exists()) {
                    iw.a(5, a, "Found file for " + str2 + ". Deleted - " + fileStreamPath.delete());
                }
            }
        }
        a(str, j, ".YFlurrySenderIndex.info.");
        h(str);
    }

    private static String d(String str) {
        return ".YFlurrySenderIndex.info." + str;
    }

    private boolean e(String str) {
        return new ii(hz.a.b.getFileStreamPath(kg.a(str)), ".yflurrydatasenderblock.", 1, new km(this)).b();
    }

    private synchronized boolean f(String str) {
        boolean b;
        lt.b();
        ii iiVar = new ii(hz.a.b.getFileStreamPath(d(str)), ".YFlurrySenderIndex.info.", 1, new kn(this));
        List<String> a = a(str);
        if (a != null) {
            iw.a(4, a, "discardOutdatedBlocksForDataKey: notSentBlocks = " + a.size());
            for (String str2 : a) {
                e(str2);
                iw.a(4, a, "discardOutdatedBlocksForDataKey: removed block = " + str2);
            }
        }
        this.b.remove(str);
        b = iiVar.b();
        a();
        return b;
    }

    private synchronized List g(String str) {
        ArrayList arrayList;
        lt.b();
        iw.a(5, a, "Reading Index File for " + str + " file name:" + hz.a.b.getFileStreamPath(d(str)));
        List<ks> list = (List) new ii(hz.a.b.getFileStreamPath(d(str)), ".YFlurrySenderIndex.info.", 1, new kp(this)).a();
        arrayList = new ArrayList();
        for (ks ksVar : list) {
            arrayList.add(ksVar.a);
        }
        return arrayList;
    }

    private static void h(String str) {
        lt.b();
        iw.a(5, a, "Deleting Index File for " + str + " file name:" + hz.a.b.getFileStreamPath(".FlurrySenderIndex.info." + str));
        File fileStreamPath = hz.a.b.getFileStreamPath(".FlurrySenderIndex.info." + str);
        if (fileStreamPath.exists()) {
            iw.a(5, a, "Found file for " + str + ". Deleted - " + fileStreamPath.delete());
        }
    }

    private synchronized boolean i(String str) {
        File fileStreamPath;
        fileStreamPath = hz.a.b.getFileStreamPath(".FlurrySenderIndex.info." + str);
        iw.a(5, a, "isOldIndexFilePresent: for " + str + fileStreamPath.exists());
        return fileStreamPath.exists();
    }

    private synchronized List j(String str) {
        Throwable th;
        Object obj;
        Throwable th2;
        Throwable th3;
        List obj2 = null;
        synchronized (this) {
            lt.b();
            iw.a(5, a, "Reading Index File for " + str + " file name:" + hz.a.b.getFileStreamPath(".FlurrySenderIndex.info." + str));
            File fileStreamPath = hz.a.b.getFileStreamPath(".FlurrySenderIndex.info." + str);
            ArrayList arrayList;
            if (fileStreamPath.exists()) {
                iw.a(5, a, "Reading Index File for " + str + " Found file.");
                Closeable dataInputStream;
                try {
                    dataInputStream = new DataInputStream(new FileInputStream(fileStreamPath));
                    try {
                        int readUnsignedShort = dataInputStream.readUnsignedShort();
                        if (readUnsignedShort == 0) {
                            lt.a(dataInputStream);
                        } else {
                            arrayList = new ArrayList(readUnsignedShort);
                            int i = 0;
                            while (i < readUnsignedShort) {
                                try {
                                    int readUnsignedShort2 = dataInputStream.readUnsignedShort();
                                    iw.a(4, a, "read iter " + i + " dataLength = " + readUnsignedShort2);
                                    byte[] bArr = new byte[readUnsignedShort2];
                                    dataInputStream.readFully(bArr);
                                    arrayList.add(new String(bArr));
                                    i++;
                                } catch (Throwable th4) {
                                    th = th4;
                                    try {
                                        iw.a(6, a, "Error when loading persistent file", th);
                                        lt.a(dataInputStream);
                                        obj2 = arrayList;
                                        return obj2;
                                    } catch (Throwable th5) {
                                        th2 = th5;
                                        lt.a(dataInputStream);
                                        throw th2;
                                    }
                                }
                            }
                            lt.a(dataInputStream);
                            obj2 = arrayList;
                        }
                    } catch (Throwable th22) {
                        th3 = th22;
                        arrayList = null;
                        th = th3;
                    }
                } catch (Throwable th6) {
                    th22 = th6;
                    dataInputStream = null;
                    lt.a(dataInputStream);
                    throw th22;
                }
            }
            iw.a(5, a, "Agent cache file doesn't exist.");
            arrayList = null;
            obj2 = arrayList;
        }
        return obj2;
    }

    private static byte[] k(String str) {
        Throwable th;
        Throwable th2;
        byte[] bArr = null;
        lt.b();
        iw.a(5, a, "Reading block File for " + str + " file name:" + hz.a.b.getFileStreamPath(".flurrydatasenderblock." + str));
        File fileStreamPath = hz.a.b.getFileStreamPath(".flurrydatasenderblock." + str);
        if (fileStreamPath.exists()) {
            iw.a(5, a, "Reading Index File for " + str + " Found file.");
            Closeable dataInputStream;
            try {
                dataInputStream = new DataInputStream(new FileInputStream(fileStreamPath));
                try {
                    int readUnsignedShort = dataInputStream.readUnsignedShort();
                    if (readUnsignedShort == 0) {
                        lt.a(dataInputStream);
                    } else {
                        bArr = new byte[readUnsignedShort];
                        dataInputStream.readFully(bArr);
                        lt.a(dataInputStream);
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            } catch (Throwable th4) {
                dataInputStream = null;
                th2 = th4;
                lt.a(dataInputStream);
                throw th2;
            }
        }
        iw.a(4, a, "Agent cache file doesn't exist.");
        return bArr;
    }

    public final List a(String str) {
        return (List) this.b.get(str);
    }

    public final synchronized void a(kg kgVar, String str) {
        Object obj = null;
        synchronized (this) {
            List linkedList;
            iw.a(4, a, "addBlockInfo" + str);
            String str2 = kgVar.a;
            List list = (List) this.b.get(str);
            if (list == null) {
                iw.a(4, a, "New Data Key");
                linkedList = new LinkedList();
                obj = 1;
            } else {
                linkedList = list;
            }
            linkedList.add(str2);
            if (linkedList.size() > c.intValue()) {
                e((String) linkedList.get(0));
                linkedList.remove(0);
            }
            this.b.put(str, linkedList);
            a(str, linkedList, ".YFlurrySenderIndex.info.");
            if (obj != null) {
                a();
            }
        }
    }

    public final boolean a(String str, String str2) {
        List list = (List) this.b.get(str2);
        boolean z = false;
        if (list != null) {
            e(str);
            z = list.remove(str);
        }
        if (list == null || list.isEmpty()) {
            f(str2);
        } else {
            this.b.put(str2, list);
            a(str2, list, ".YFlurrySenderIndex.info.");
        }
        return z;
    }
}
