package com.labado.lulaoshi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.os.StrictMode.VmPolicy;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.lulaoshi.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AsyncListImage extends Activity {
    private ArrayAdapter<String> allwords;
    List<ImageAndText> dataArray = new ArrayList();
    Document dc;
    Document doc;
    Handler hl = new Handler() {
        public void handleMessage(Message msg) {
            System.out.println("msg.arg1 == " + msg.arg1);
            switch (msg.what) {
                case 1:
                    AsyncListImage.this.progressDialog = ProgressDialog.show(AsyncListImage.this, "每日一撸，撸撸更健康！", "精彩马上开始  请耐心等待...", true, false);
                    break;
                case 2:
                    AsyncListImage.this.list.setAdapter(new ImageAndTextListAdapter(AsyncListImage.this, AsyncListImage.this.dataArray, AsyncListImage.this.list));
                    AsyncListImage.this.progressDialog.dismiss();
                    break;
                case 3:
                    Toast.makeText(AsyncListImage.this, "资源载入失败，请重新尝试载入！", 1).show();
                    AsyncListImage.this.progressDialog.dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    Integer i = Integer.valueOf(1);
    /* access modifiers changed from: private */
    public ListView list;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    Spinner spinner_1;
    final String[] str = new String[]{"亚洲系列", "自拍偷拍", "欧美系列", "动漫系列", "强奸系列", "制服系列", "有码系列", "三级系列"};
    public List<String> words;

    public class loadres implements Runnable {
        String wzdz = null;

        public void getdz(String dz) {
            this.wzdz = dz;
            System.out.println(this.wzdz);
        }

        public void run() {
            AsyncListImage.this.hl.sendEmptyMessage(1);
            System.out.println("loadres --->" + Thread.currentThread().getName());
            AsyncListImage.this.list = (ListView) AsyncListImage.this.findViewById(R.id.listview);
            try {
                AsyncListImage.this.doc = Jsoup.connect(this.wzdz).get();
                Iterator it = AsyncListImage.this.doc.getElementsByClass("list-pianyuan-box-l").select("a[href*=/vod/]").iterator();
                while (it.hasNext()) {
                    Element scr = (Element) it.next();
                    AsyncListImage.this.dc = Jsoup.connect(scr.attr("abs:href").toString()).get();
                    Elements lk = AsyncListImage.this.dc.select("a[href*=qvod://]");
                    Elements jp = AsyncListImage.this.dc.select("img[src$=.jpg]");
                    Elements vna = AsyncListImage.this.dc.getElementsByClass("vshow").select("h2");
                    Iterator it2 = lk.iterator();
                    while (it2.hasNext()) {
                        AsyncListImage.this.dataArray.add(new ImageAndText(jp.get(0).attr("abs:src").toString(), vna.text().toString(), ((Element) it2.next()).attr("abs:href").split("=")[1]));
                    }
                }
                AsyncListImage.this.hl.sendEmptyMessage(2);
            } catch (IOException e) {
                AsyncListImage.this.hl.sendEmptyMessage(3);
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_and_text_row);
        System.out.println("acrivity --->" + Thread.currentThread().getName());
        this.words = new ArrayList();
        for (Object add : this.str) {
            this.words.add(add);
        }
        this.spinner_1 = (Spinner) findViewById(R.id.sp);
        this.allwords = new ArrayAdapter(this, 17367048, this.words);
        this.allwords.setDropDownViewResource(17367049);
        this.spinner_1.setAdapter(this.allwords);
        this.spinner_1.setOnItemSelectedListener(new OnItemSelectedListener() {
            TextView gcurl;

            {
                this.gcurl = (TextView) AsyncListImage.this.findViewById(R.id.xzurl);
            }

            public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                loadres lr;
                System.out.println(AsyncListImage.this.str[arg2].toString());
                if (AsyncListImage.this.str[arg2].toString() == "自拍偷拍") {
                    AsyncListImage.this.i = Integer.valueOf(1);
                    AsyncListImage.this.dataArray.clear();
                    lr = new loadres();
                    this.gcurl.setText("http://www.henhenlu.com/vodlist/2_1.html");
                    lr.getdz("http://www.henhenlu.com/vodlist/2_1.html");
                    System.out.println("http://www.henhenlu.com/vodlist/2_1.html");
                    new Thread(lr).start();
                }
                if (AsyncListImage.this.str[arg2].toString() == "亚洲系列") {
                    AsyncListImage.this.i = Integer.valueOf(1);
                    AsyncListImage.this.dataArray.clear();
                    lr = new loadres();
                    this.gcurl.setText("http://www.henhenlu.com/vodlist/1_1.html");
                    lr.getdz("http://www.henhenlu.com/vodlist/1_1.html");
                    System.out.println("http://www.henhenlu.com/vodlist/1_1.html");
                    new Thread(lr).start();
                }
                if (AsyncListImage.this.str[arg2].toString() == "欧美系列") {
                    AsyncListImage.this.i = Integer.valueOf(1);
                    AsyncListImage.this.dataArray.clear();
                    lr = new loadres();
                    this.gcurl.setText("http://www.henhenlu.com/vodlist/3_1.html");
                    lr.getdz("http://www.henhenlu.com/vodlist/3_1.html");
                    System.out.println("http://www.henhenlu.com/vodlist/3_1.html");
                    new Thread(lr).start();
                }
                if (AsyncListImage.this.str[arg2].toString() == "动漫系列") {
                    AsyncListImage.this.i = Integer.valueOf(1);
                    AsyncListImage.this.dataArray.clear();
                    lr = new loadres();
                    this.gcurl.setText("http://www.henhenlu.com/vodlist/4_1.html");
                    lr.getdz("http://www.henhenlu.com/vodlist/4_1.html");
                    System.out.println("http://www.henhenlu.com/vodlist/4_1.html");
                    new Thread(lr).start();
                }
                if (AsyncListImage.this.str[arg2].toString() == "强奸系列") {
                    AsyncListImage.this.i = Integer.valueOf(1);
                    AsyncListImage.this.dataArray.clear();
                    lr = new loadres();
                    this.gcurl.setText("http://www.henhenlu.com/vodlist/5_1.html");
                    lr.getdz("http://www.henhenlu.com/vodlist/5_1.html");
                    System.out.println("http://www.henhenlu.com/vodlist/5_1.html");
                    new Thread(lr).start();
                }
                if (AsyncListImage.this.str[arg2].toString() == "制服系列") {
                    AsyncListImage.this.i = Integer.valueOf(1);
                    AsyncListImage.this.dataArray.clear();
                    lr = new loadres();
                    this.gcurl.setText("http://www.henhenlu.com/vodlist/6_1.html");
                    lr.getdz("http://www.henhenlu.com/vodlist/6_1.html");
                    System.out.println("http://www.henhenlu.com/vodlist/6_1.html");
                    new Thread(lr).start();
                }
                if (AsyncListImage.this.str[arg2].toString() == "有码系列") {
                    AsyncListImage.this.i = Integer.valueOf(1);
                    AsyncListImage.this.dataArray.clear();
                    lr = new loadres();
                    this.gcurl.setText("http://www.henhenlu.com/vodlist/9_1.html");
                    lr.getdz("http://www.henhenlu.com/vodlist/9_1.html");
                    System.out.println("http://www.henhenlu.com/vodlist/9_1.html");
                    new Thread(lr).start();
                }
                if (AsyncListImage.this.str[arg2].toString() == "三级系列") {
                    AsyncListImage.this.i = Integer.valueOf(1);
                    AsyncListImage.this.dataArray.clear();
                    lr = new loadres();
                    this.gcurl.setText("http://www.henhenlu.com/vodlist/7_1.html");
                    lr.getdz("http://www.henhenlu.com/vodlist/7_1.html");
                    System.out.println("http://www.henhenlu.com/vodlist/7_1.html");
                    new Thread(lr).start();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                System.out.println("nothing" + arg0.toString());
            }
        });
        ((Button) findViewById(R.id.loadmore)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TextView url = (TextView) AsyncListImage.this.findViewById(R.id.xzurl);
                System.out.println("  btn ---->  " + url.getText().toString());
                try {
                    AsyncListImage.this.doc = Jsoup.connect(url.getText().toString()).get();
                    Elements link = AsyncListImage.this.doc.getElementsByClass("zypag").select("a[href*=vodlist]");
                    String nexturl = null;
                    if (AsyncListImage.this.i.intValue() < link.size()) {
                        AsyncListImage asyncListImage = AsyncListImage.this;
                        asyncListImage.i = Integer.valueOf(asyncListImage.i.intValue() + 1);
                        nexturl = link.get(AsyncListImage.this.i.intValue()).attr("abs:href").toString();
                    }
                    System.out.println(nexturl);
                    AsyncListImage.this.dataArray.clear();
                    loadres lds = new loadres();
                    lds.getdz(nexturl);
                    new Thread(lds).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
