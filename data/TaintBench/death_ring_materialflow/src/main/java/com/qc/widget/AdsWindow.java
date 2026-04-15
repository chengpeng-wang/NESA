package com.qc.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

public class AdsWindow {
    /* access modifiers changed from: private */
    public View btn_flow;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public boolean isFlow = false;
    private LayoutParams params;
    /* access modifiers changed from: private */
    public WindowManager wm;

    public AdsWindow(Context context) {
        this.context = context;
        this.wm = (WindowManager) context.getSystemService("window");
        this.params = new LayoutParams();
    }

    public void createFlow(Bitmap bitmap, final String urlStr) {
        this.btn_flow = LayoutInflater.from(this.context).inflate(ResourceUtil.getLayoutId(this.context, "abc_view"), null);
        ImageView main = (ImageView) this.btn_flow.findViewById(ResourceUtil.getId(this.context, "abc_img_3"));
        main.setImageBitmap(bitmap);
        main.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (urlStr != null && urlStr.length() > 0) {
                    Uri uri = Uri.parse(urlStr);
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(uri);
                    intent.setFlags(268435456);
                    AdsWindow.this.context.startActivity(intent);
                    if (AdsWindow.this.isFlow) {
                        AdsWindow.this.wm.removeView(AdsWindow.this.btn_flow);
                        AdsWindow.this.isFlow = false;
                    }
                }
            }
        });
        ImageView child = (ImageView) this.btn_flow.findViewById(ResourceUtil.getId(this.context, "abc_exit"));
        child.setImageDrawable(this.context.getResources().getDrawable(ResourceUtil.getDrawableId(this.context, "abc_close")));
        child.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (AdsWindow.this.isFlow) {
                    AdsWindow.this.wm.removeView(AdsWindow.this.btn_flow);
                    AdsWindow.this.isFlow = false;
                }
            }
        });
        this.params.type = 2003;
        this.params.format = 1;
        this.params.flags = 40;
        this.params.gravity = 17;
        this.params.width = -2;
        this.params.height = -2;
        this.wm.addView(this.btn_flow, this.params);
        this.isFlow = true;
    }
}
