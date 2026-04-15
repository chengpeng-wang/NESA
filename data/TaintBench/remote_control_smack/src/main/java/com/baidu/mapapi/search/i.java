package com.baidu.mapapi.search;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import com.baidu.platform.comapi.c.a;
import com.baidu.platform.comapi.d.c;

class i implements OnClickListener {
    final /* synthetic */ PlaceCaterActivity a;

    i(PlaceCaterActivity placeCaterActivity) {
        this.a = placeCaterActivity;
    }

    public void onClick(View view) {
        d dVar = (d) view.getTag();
        this.a.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(dVar.c)));
        a.a().a("pkgname", c.p());
        a.a().a("cat", dVar.b);
        a.a().a("place_cater_moreinfo_click");
    }
}
