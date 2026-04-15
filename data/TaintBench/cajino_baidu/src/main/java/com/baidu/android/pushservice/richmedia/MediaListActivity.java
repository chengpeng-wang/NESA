package com.baidu.android.pushservice.richmedia;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.b;
import com.baidu.android.pushservice.util.PushDatabase;
import com.baidu.android.pushservice.util.j;
import com.baidu.android.pushservice.util.m;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MediaListActivity extends Activity {
    /* access modifiers changed from: private|static */
    public static String r = "downloadUrl";
    ArrayList a;
    NotificationManager b;
    private ListView c;
    private int d;
    /* access modifiers changed from: private */
    public int e;
    private int f;
    /* access modifiers changed from: private */
    public int g;
    /* access modifiers changed from: private */
    public int h;
    /* access modifiers changed from: private */
    public int i;
    /* access modifiers changed from: private */
    public int j;
    private int k;
    private LinearLayout l = null;
    /* access modifiers changed from: private */
    public RemoteViews m;
    /* access modifiers changed from: private */
    public int n;
    /* access modifiers changed from: private */
    public int o;
    /* access modifiers changed from: private */
    public int p;
    /* access modifiers changed from: private */
    public int q;
    private OnItemClickListener s = new d(this);
    private OnItemLongClickListener t = new e(this);

    /* access modifiers changed from: private */
    public void a(String str, String str2, String str3) {
        Uri parse = Uri.parse(str);
        String path = parse.getPath();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "baidu/pushservice/files" + "/" + parse.getAuthority() + "/" + path.substring(0, path.lastIndexOf(47)));
        if (b.a()) {
            Log.d("MediaListActivity", "<<< download url " + parse.toString());
        }
        n a = p.a(o.REQ_TYPE_GET_ZIP, parse.toString());
        a.b = file.getAbsolutePath();
        a.c = str2;
        a.d = str3;
        new b(this, new h(this)).execute(new n[]{a});
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.d = getResources().getIdentifier("bpush_media_list", "layout", getPackageName());
        requestWindowFeature(1);
        if (this.d != 0) {
            setContentView(this.d);
            Resources resources = getResources();
            String packageName = getPackageName();
            this.e = resources.getIdentifier("bpush_media_list_item", "layout", packageName);
            this.f = resources.getIdentifier("bpush_type_listview", "id", packageName);
            this.g = resources.getIdentifier("bpush_media_list_img", "id", packageName);
            this.h = resources.getIdentifier("bpush_media_list_title", "id", packageName);
            this.i = resources.getIdentifier("bpush_media_list_from_text", "id", packageName);
            this.j = resources.getIdentifier("bpush_media_list_time_text", "id", packageName);
            this.k = resources.getIdentifier("bpush_media_none_layout", "id", packageName);
            this.l = (LinearLayout) findViewById(this.k);
            this.c = (ListView) findViewById(this.f);
            Button button = (Button) findViewById(resources.getIdentifier("bpush_media_list_return_btn", "id", packageName));
            button.setClickable(true);
            button.setOnClickListener(new c(this));
            int identifier = getResources().getIdentifier("bpush_download_progress", "layout", getPackageName());
            if (identifier != 0) {
                this.m = new RemoteViews(getPackageName(), identifier);
                this.n = getResources().getIdentifier("bpush_downLoad_progress", "id", getPackageName());
                this.o = getResources().getIdentifier("bpush_progress_percent", "id", getPackageName());
                this.p = getResources().getIdentifier("bpush_progress_text", "id", getPackageName());
                this.q = getResources().getIdentifier("bpush_downLoad_icon", "id", getPackageName());
            }
            this.c.setOnItemClickListener(this.s);
            this.c.setDividerHeight(0);
            this.c.setOnItemLongClickListener(this.t);
        }
        this.b = (NotificationManager) getSystemService("notification");
    }

    public void onResume() {
        int i = 0;
        super.onResume();
        if (this.d != 0) {
            String[] strArr = new String[]{"img", "title", "fromtext", "timetext"};
            this.a = new ArrayList();
            List selectFileDownloadingInfo = PushDatabase.selectFileDownloadingInfo(PushDatabase.getDb(this));
            if (selectFileDownloadingInfo.isEmpty()) {
                this.l.setVisibility(0);
                this.c.setVisibility(8);
                return;
            }
            this.l.setVisibility(8);
            this.c.setVisibility(0);
            this.c.setItemsCanFocus(true);
            PackageManager packageManager = getPackageManager();
            while (true) {
                int i2 = i;
                if (i2 < selectFileDownloadingInfo.size()) {
                    HashMap hashMap = new HashMap();
                    try {
                        ApplicationInfo applicationInfo = packageManager.getApplicationInfo(((j) selectFileDownloadingInfo.get(i2)).a, 0);
                        hashMap.put(strArr[0], packageManager.getApplicationIcon(applicationInfo));
                        hashMap.put(strArr[1], ((j) selectFileDownloadingInfo.get(i2)).c);
                        hashMap.put(strArr[2], "来自：" + packageManager.getApplicationLabel(applicationInfo));
                        hashMap.put(strArr[3], m.a(((j) selectFileDownloadingInfo.get(i2)).j));
                        hashMap.put(r, ((j) selectFileDownloadingInfo.get(i2)).b);
                        this.a.add(hashMap);
                    } catch (NameNotFoundException e) {
                        Log.w("MediaListActivity", "Media item package NOT found: " + ((j) selectFileDownloadingInfo.get(i2)).a);
                    }
                    i = i2 + 1;
                } else {
                    this.c.setAdapter(new i(this, this, this.a));
                    return;
                }
            }
        }
    }
}
