package com.baidu.android.pushservice.richmedia;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.baidu.android.pushservice.util.PushDatabase;
import com.baidu.android.pushservice.util.j;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

public class b extends AsyncTask implements Comparable {
    public static int e = 1;
    public static int f = 2;
    private static HashSet h = new HashSet();
    protected s a;
    public WeakReference b;
    protected long c;
    public n d;
    private l g = l.a(b.class.getName());
    private boolean i = false;
    private boolean j = false;

    public b(Context context, s sVar) {
        this.a = sVar;
        this.b = new WeakReference(context);
        this.c = System.currentTimeMillis();
    }

    private int a(String str) {
        try {
            return ((HttpURLConnection) new URL(str).openConnection()).getContentLength();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return 0;
    }

    private j a(Context context, String str) {
        List selectFileDownloadingInfo = PushDatabase.selectFileDownloadingInfo(PushDatabase.getDb(context));
        if (selectFileDownloadingInfo != null) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= selectFileDownloadingInfo.size()) {
                    break;
                } else if (((j) selectFileDownloadingInfo.get(i2)).b.equalsIgnoreCase(str)) {
                    return (j) selectFileDownloadingInfo.get(i2);
                } else {
                    i = i2 + 1;
                }
            }
        }
        return null;
    }

    private static void a(File file, String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(fileInputStream));
            while (true) {
                ZipEntry nextEntry = zipInputStream.getNextEntry();
                if (nextEntry != null) {
                    try {
                        Log.i("DownloadCompleteReceiver: ", "unzip----=" + nextEntry);
                        byte[] bArr = new byte[4096];
                        String[] split = nextEntry.getName().split("/");
                        File file2 = new File(str + "/" + split[split.length - 1]);
                        if (!nextEntry.isDirectory()) {
                            File file3 = new File(file2.getParent());
                            if (!file3.exists()) {
                                file3.mkdirs();
                            }
                            if (!file2.exists()) {
                                file2.createNewFile();
                            }
                            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2), 4096);
                            while (true) {
                                int read = zipInputStream.read(bArr, 0, 4096);
                                if (read == -1) {
                                    break;
                                }
                                bufferedOutputStream.write(bArr, 0, read);
                            }
                            bufferedOutputStream.flush();
                            bufferedOutputStream.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    fileInputStream.close();
                    zipInputStream.close();
                    return;
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private static synchronized boolean a(n nVar) {
        boolean add;
        synchronized (b.class) {
            add = h.add(nVar);
        }
        return add;
    }

    private static synchronized boolean b(n nVar) {
        boolean remove;
        synchronized (b.class) {
            remove = h.remove(nVar);
        }
        return remove;
    }

    /* renamed from: a */
    public int compareTo(b bVar) {
        if (bVar == null) {
            return -1;
        }
        long a = bVar.a();
        return this.c <= a ? this.c < a ? 1 : 0 : -1;
    }

    public long a() {
        return this.c;
    }

    /* access modifiers changed from: protected|varargs */
    /* renamed from: a */
    public r doInBackground(n... nVarArr) {
        r rVar = new r();
        this.d = nVarArr[0];
        rVar.d = this.d;
        if (this.d != null) {
            rVar.a = this.d.b();
            if (this.d.b == null) {
                if (com.baidu.android.pushservice.b.a()) {
                    Log.d("HttpTask", "download file Request error: " + this.d);
                }
                rVar.c = 3;
            } else if (a(this.d)) {
                j jVar;
                j a = a((Context) this.b.get(), this.d.d());
                if (a == null) {
                    jVar = new j();
                    jVar.b = this.d.d();
                    jVar.a = this.d.a;
                    jVar.c = this.d.c;
                    jVar.d = this.d.d;
                    jVar.g = 0;
                    jVar.h = a(jVar.b);
                    jVar.i = e;
                    jVar.f = jVar.b.substring(jVar.b.lastIndexOf(47) + 1);
                    jVar.e = this.d.b;
                    PushDatabase.insertFileDownloadingInfo(PushDatabase.getDb((Context) this.b.get()), jVar);
                } else {
                    a.h = a(a.b);
                    jVar = a;
                }
                if (jVar.i == f) {
                    rVar.c = 0;
                    rVar.d = this.d;
                    rVar.e = jVar.e + "/" + jVar.f;
                    return rVar;
                }
                this.g.b("Request url: " + this.d.d() + " success");
                if (this.a != null) {
                    this.a.a(this);
                }
                try {
                    HttpResponse a2 = new a().a(this.d.c(), this.d.d(), this.d.a(), this.d.f);
                    if (a2.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        InputStream content = a2.getEntity().getContent();
                        File file = new File(jVar.e);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        File file2 = new File(jVar.e + "/" + jVar.f);
                        if (!file2.exists()) {
                            file2.createNewFile();
                        }
                        RandomAccessFile randomAccessFile = new RandomAccessFile(file2, "rw");
                        randomAccessFile.seek((long) jVar.g);
                        byte[] bArr = new byte[102400];
                        int i = jVar.g;
                        m mVar = new m();
                        mVar.b = (long) jVar.h;
                        mVar.a = (long) i;
                        publishProgress(new m[]{mVar});
                        while (!this.i) {
                            while (true == this.j) {
                                Thread.sleep(500);
                            }
                            int read = content.read(bArr);
                            if (read == -1) {
                                break;
                            }
                            randomAccessFile.write(bArr, 0, read);
                            read += i;
                            m mVar2 = new m();
                            mVar2.b = (long) jVar.h;
                            mVar2.a = (long) read;
                            publishProgress(new m[]{mVar2});
                            if (read == jVar.h) {
                                i = read;
                                break;
                            }
                            Thread.sleep(500);
                            jVar.g = read;
                            PushDatabase.updateFileDownloadingInfo(PushDatabase.getDb((Context) this.b.get()), jVar.b, jVar);
                            i = read;
                        }
                        content.close();
                        randomAccessFile.close();
                        if (this.i) {
                            PushDatabase.deleteFileDownloadingInfo(PushDatabase.getDb((Context) this.b.get()), jVar.b);
                            rVar.c = 2;
                            file2.delete();
                        } else {
                            jVar.g = i;
                            jVar.i = f;
                            PushDatabase.updateFileDownloadingInfo(PushDatabase.getDb((Context) this.b.get()), jVar.b, jVar);
                            rVar.c = 0;
                            rVar.e = file2.getAbsolutePath();
                        }
                    } else {
                        rVar.c = 1;
                        rVar.b = a2.getStatusLine().getStatusCode();
                    }
                } catch (Exception e) {
                    if (com.baidu.android.pushservice.b.a()) {
                        Log.d("HttpTask", "download file Exception:" + e.getMessage());
                    }
                    rVar.c = -1;
                }
            } else {
                this.g.c("Request url: " + this.d.d() + " failed, already in queue");
                this.a = null;
                this.d = null;
                return null;
            }
        }
        return rVar;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onPostExecute(r rVar) {
        try {
            if (this.a == null || rVar == null) {
                b(this.d);
                return;
            }
            if (rVar.c == 0) {
                String str = rVar.e;
                if (rVar.a == o.REQ_TYPE_GET_ZIP && str != null) {
                    String substring = str.substring(0, str.lastIndexOf("."));
                    File file = new File(str);
                    a(file, substring);
                    file.delete();
                    rVar.e = substring;
                }
                this.a.a(this, rVar);
            } else if (rVar.c == 1) {
                this.a.a(this, new Throwable("error: response http error errorCode=" + rVar.b));
            } else if (rVar.c == 3) {
                this.a.a(this, new Throwable("error: request error,request is null or fileName is null."));
            } else if (rVar.c == 2) {
                this.a.b(this);
            } else if (rVar.c == -1) {
                this.a.a(this, new Throwable("IOException"));
            }
            b(this.d);
        } catch (Throwable th) {
            b(this.d);
        }
    }

    /* access modifiers changed from: protected|varargs */
    /* renamed from: a */
    public void onProgressUpdate(m... mVarArr) {
        if (this.a != null) {
            this.a.a(this, mVarArr[0]);
        }
    }

    /* access modifiers changed from: protected */
    public void onCancelled() {
        if (this.a != null) {
            this.a.b(this);
        }
        b(this.d);
        this.i = true;
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
    }
}
