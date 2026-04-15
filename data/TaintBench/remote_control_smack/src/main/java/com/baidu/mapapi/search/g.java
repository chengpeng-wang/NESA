package com.baidu.mapapi.search;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import com.baidu.platform.comapi.c.a;
import com.baidu.platform.comapi.d.c;

class g implements OnClickListener {
    final /* synthetic */ PlaceCaterActivity a;

    g(PlaceCaterActivity placeCaterActivity) {
        this.a = placeCaterActivity;
    }

    public void onClick(View view) {
        try {
            this.a.startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + this.a.j.getText().toString().trim())));
            a.a().a("pkgname", c.p());
            a.a().a("place_telbutton_click");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
