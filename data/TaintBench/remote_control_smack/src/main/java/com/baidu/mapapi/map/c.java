package com.baidu.mapapi.map;

import android.view.View;
import android.view.View.OnClickListener;

class c implements OnClickListener {
    final /* synthetic */ MapView a;

    c(MapView mapView) {
        this.a = mapView;
    }

    public void onClick(View view) {
        this.a.b();
    }
}
